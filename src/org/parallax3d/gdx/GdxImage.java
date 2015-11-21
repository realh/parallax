/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 * 
 * This file is part of the realh fork of the Parallax project.
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

package org.parallax3d.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import java.nio.ByteBuffer;

import org.parallax3d.core.Log;
import org.parallax3d.renderer.Image;

public class GdxImage implements Image
{
	public static boolean flipByDefault = true;

	private final boolean flipY;

	private Pixmap pixmap;

    public GdxImage(Pixmap pixmap)
    {
        this.pixmap = pixmap;
		this.flipY = flipByDefault;
    }

    public GdxImage(Pixmap pixmap, boolean flipY)
    {
        this.pixmap = pixmap;
		this.flipY = flipY;
    }

    @Override
    public void glTexImage2D(int target)
    {
		ByteBuffer pixels = pixmap.getPixels();
		int width = pixmap.getWidth();
		int height = pixmap.getHeight();

		if (flipY)
		{
			int stride = 0;

			switch (pixmap.getFormat())
			{
				case Alpha:
				case Intensity:
					stride = 1;
					break;
				case LuminanceAlpha:
				case RGB565:
				case RGBA4444:
					stride = 2;
					break;
				case RGB888:
					stride = 3;
					break;
				case RGBA8888:
					stride = 4;
					break;
			}

			stride *= width;

			int len = pixels.limit();

			Log.debug("Image stride " + stride + ", total size should be " +
					stride * pixmap.getHeight() + ", actual size " + len);

			ByteBuffer row1 = ByteBuffer.allocateDirect(stride);
			ByteBuffer row2 = ByteBuffer.allocateDirect(stride);

			for (int y = 0; y < height / 2; ++y)
			{
				int offset1 = y * stride;
				int offset2 = (height - y - 1) * stride;

				row1.rewind();
				pixels.position(offset1);
				pixels.limit(offset1 + stride);
				row1.put(pixels);

				row2.rewind();
				pixels.limit(offset2 + stride);
				pixels.position(offset2);
				row2.put(pixels);

				row2.rewind();
				pixels.position(offset1);
				pixels.put(row2);

				row1.rewind();
				pixels.position(offset2);
				pixels.put(row1);
			}

			pixels.position(0);
			pixels.limit(len);
		}

        Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(),
                width, height, 0,
                pixmap.getGLFormat(), pixmap.getGLType(), pixels);
    }

    @Override
    public int getWidth() {
        return pixmap.getWidth();
    }

    @Override
    public int getHeight() {
        return pixmap.getHeight();
    }

    @Override
    public Image createScaledCopy(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, this.pixmap.getFormat());
        Pixmap.setFilter(Pixmap.Filter.BiLinear);
        pixmap.drawPixmap(this.pixmap,
                0, 0, this.pixmap.getWidth(), this.pixmap.getHeight(),
                0, 0, width, height);
        return new GdxImage(pixmap);
    }

    @Override
    public void recycle()
    {
    }
}
