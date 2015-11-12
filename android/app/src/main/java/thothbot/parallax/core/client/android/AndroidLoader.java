/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 *
 * This file is part of the Android port of the Parallax project.
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

package thothbot.parallax.core.client.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import thothbot.parallax.core.client.gl2.Image;
import thothbot.parallax.loader.shared.AssetLoader;
import thothbot.parallax.loader.shared.ImageLoader;

public abstract class AndroidLoader extends AssetLoader implements ImageLoader
{
	private static final String TAG = "Parallax";

	private static final int CHUNK_SIZE = 1024;

	/**
	 * @param dirName Use a trailing / if this is a directory, otherwise
	 *                the directory containing the named file is used.
	 */
	public AndroidLoader(String dirName)
	{
		super(dirName);
	}

	public abstract InputStream openInputStream(String filename) throws IOException;

	@Override
	public Image loadImage(String leafname) throws IOException
	{
		InputStream strm = openInputStream(leafname);
		Bitmap bmp = BitmapFactory.decodeStream(strm);
		strm.close();
		if (null == bmp) {
			throw new RuntimeException("Unable to create bitmap from asset '" +
					leafname + "'");
		}
		if (null == bmp.getConfig()) {
			Log.d(TAG, "Bitmap has unsupported format, converting");
			Bitmap orig = bmp;
			bmp = bmp.copy(Bitmap.Config.RGB_565, true);
			orig.recycle();
		}
		return new AndroidImage(bmp);
	}

	@Override
	public ByteBuffer loadData(String leafname) throws IOException
	{
		// AssetManager doesn't provide a reliable way to find the length of an
		// asset, so just keep reading from an InputStream until we get -1.
		InputStream strm = openInputStream(leafname);
		byte[] buf = null;
		int numRead = -1;
		int offset = 0;

		do
		{
			// At least we can make a good guess
			int available = strm.available();
			if (available < CHUNK_SIZE)
				available = CHUNK_SIZE;
			if (buf == null)
			{
				buf = new byte[available];
			}
			else if (available <= buf.length - offset)
			{
				available = buf.length - offset;
			}
			else
			{
				byte[] oldBuf = buf;

				available -= oldBuf.length - offset;
				if (available < CHUNK_SIZE)
					available = CHUNK_SIZE;
				offset = oldBuf.length;
				buf = new byte[offset + available];
				System.arraycopy(oldBuf, 0, buf, 0, offset);
			}
			numRead = strm.read(buf, offset, available);
			if (numRead != -1)
				offset += numRead;
		} while (numRead != -1);
		
		strm.close();

		ByteBuffer bbuf = ByteBuffer.allocate(offset);
		bbuf.put(buf, 0, offset);
		bbuf.flip();
		return bbuf;
	}
}
