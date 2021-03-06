/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 *
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.textures;

import org.parallax3d.core.Log;

import java.nio.ByteBuffer;
import java.util.List;

import org.parallax3d.renderer.DummyImage;
import org.parallax3d.renderer.GLES20Ext;
import org.parallax3d.renderer.Uint8Array;

public class CompressedTexture extends Texture
{

	private int compressedFormat;
	private List<DataTexture> mipmaps;

    public CompressedTexture()
    {
		super(new DummyImage(0, 0));
	}
	
    public CompressedTexture ( ByteBuffer buffer, boolean loadMipmaps )
	{
		this();
        parseDDS(buffer, loadMipmaps);
	}

	public int getCompressedFormat() {
		return compressedFormat;
	}

	public void setCompressedFormat(int compressedFormat) {
		this.compressedFormat = compressedFormat;
	}

	public List<DataTexture> getMipmaps() {
		return mipmaps;
	}

	public void setMipmaps(List<DataTexture> mipmaps) {
		this.mipmaps = mipmaps;
	}

	/**
	 * Adapted from @toji's DDS utils
	 * <a href="https://github.com/toji/webgl-texture-utils/blob/master/texture-util/dds.js">github.com/toji</a>
	 * <p>
	 * All values and structures referenced from: 
	 * <a href="http://msdn.microsoft.com/en-us/library/bb943991.aspx/">msdn.microsoft.com</a>
	 * 
	 * @param buffer
	 * @param loadMipmaps
	 */
	public void parseDDS( ByteBuffer buffer, boolean loadMipmaps )
	{
//		var dds = { mipmaps: [], width: 0, height: 0, format: null, mipmapCount: 1 };

		int DDS_MAGIC = 0x20534444;

		int DDSD_CAPS = 0x1,
			DDSD_HEIGHT = 0x2,
			DDSD_WIDTH = 0x4,
			DDSD_PITCH = 0x8,
			DDSD_PIXELFORMAT = 0x1000,
			DDSD_MIPMAPCOUNT = 0x20000,
			DDSD_LINEARSIZE = 0x80000,
			DDSD_DEPTH = 0x800000;

		int DDSCAPS_COMPLEX = 0x8,
			DDSCAPS_MIPMAP = 0x400000,
			DDSCAPS_TEXTURE = 0x1000;

		int DDSCAPS2_CUBEMAP = 0x200,
			DDSCAPS2_CUBEMAP_POSITIVEX = 0x400,
			DDSCAPS2_CUBEMAP_NEGATIVEX = 0x800,
			DDSCAPS2_CUBEMAP_POSITIVEY = 0x1000,
			DDSCAPS2_CUBEMAP_NEGATIVEY = 0x2000,
			DDSCAPS2_CUBEMAP_POSITIVEZ = 0x4000,
			DDSCAPS2_CUBEMAP_NEGATIVEZ = 0x8000,
			DDSCAPS2_VOLUME = 0x200000;

		int DDPF_ALPHAPIXELS = 0x1,
			DDPF_ALPHA = 0x2,
			DDPF_FOURCC = 0x4,
			DDPF_RGB = 0x40,
			DDPF_YUV = 0x200,
			DDPF_LUMINANCE = 0x20000;

		int FOURCC_DXT1 = fourCCToInt32("DXT1");
		int FOURCC_DXT3 = fourCCToInt32("DXT3");
		int FOURCC_DXT5 = fourCCToInt32("DXT5");

		// The header length in 32 bit ints
		int headerLengthInt = 31; 

		// Offsets into the header array

		int off_magic = 0;

		int off_size = 1;
		int off_flags = 2;
		int off_height = 3;
		int off_width = 4;

		int off_mipmapCount = 7;

		int off_pfFlags = 20;
		int off_pfFourCC = 21;

		buffer.rewind();

		// Parse header

        if ( buffer.getInt(off_magic) != DDS_MAGIC )
        {
            Log.error("ImageUtils.parseDDS(): Invalid magic number in DDS header");
            return;
        }

        if ( (buffer.getInt(off_pfFlags) & DDPF_FOURCC) == 0 )
        {
            Log.error("ImageUtils.parseDDS(): Unsupported format, must contain a FourCC code");
            return;
        }

		int blockBytes;

		int fourCC = buffer.getInt(off_pfFourCC);

		if ( fourCC == FOURCC_DXT1)
		{
			blockBytes = 8;
			this.compressedFormat = GLES20Ext.GL_COMPRESSED_RGB_S3TC_DXT1_EXT;
		}
		else if(fourCC == FOURCC_DXT3)
		{
			blockBytes = 16;
			this.compressedFormat = GLES20Ext.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
		}
		else if(fourCC == FOURCC_DXT5)
		{
			blockBytes = 16;
			this.compressedFormat = GLES20Ext.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
		}
		else
		{
			Log.error("ImageUtils.parseDDS(): Unsupported FourCC code: " + int32ToFourCC( fourCC ) );
			return;
		}

		int mipmapCount = 1;

        if ( ((buffer.getInt(off_flags) & DDSD_MIPMAPCOUNT) != 0) && loadMipmaps != false )
        {
        	mipmapCount = Math.max( 1, buffer.getInt(off_mipmapCount) );
        }

//        setWidth( buffer.getInt( off_width ) );
//        setHeight( buffer.getInt( off_height ) );

        int dataOffset = buffer.getInt(off_size) + 4;

		// Extract mipmaps buffers

		int width = buffer.getInt(off_width) ;
		int height = buffer.getInt(off_height);

		((DummyImage) getImage()).setWidth(width);
		((DummyImage) getImage()).setHeight(height);

		for ( int i = 0; i < mipmapCount; i ++ ) 
		{
			int dataLength = Math.max( 4, width ) / 4 * Math.max( 4, height ) / 4 * blockBytes;

			buffer.limit(dataOffset + dataLength);
			buffer.position(dataOffset);

			byte[] byteArray = new byte[dataLength];
			buffer.get(byteArray);

			DataTexture mipmap = new DataTexture(width, height);

			mipmap.setData(Uint8Array.create(byteArray));
			mipmaps.add( mipmap );

			dataOffset += dataLength;

			width = (int)Math.max( width * 0.5, 1 );
			height = (int)Math.max( height * 0.5, 1 );
		}
	}
	
	private int fourCCToInt32( String value ) 
	{
		return (int)value.charAt(0) +
			((int)value.charAt(1) << 8) +
			((int)value.charAt(2) << 16) +
			((int)value.charAt(3) << 24);

	}

	private String int32ToFourCC( int value ) 
	{
		return fromCharCode(
			value & 0xff,
			(value >> 8) & 0xff,
			(value >> 16) & 0xff,
			(value >> 24) & 0xff
		);
	}
	
	private static String fromCharCode(int... codePoints) 
	{
	    return new String(codePoints, 0, codePoints.length);
	}
}
