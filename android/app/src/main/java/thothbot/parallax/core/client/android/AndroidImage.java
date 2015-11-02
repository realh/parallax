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

package thothbot.parallax.core.client.android;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import thothbot.parallax.core.client.gl2.Image;

public class AndroidImage implements Image
{
    private Bitmap bitmap;

    AndroidImage(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    @Override
    public void glTexImage2D(int target)
    {
        GLUtils.texImage2D(target, 0, bitmap, 0);
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Image createScaledCopy(int width, int height) {
        return new AndroidImage(Bitmap.createScaledBitmap(this.bitmap, width, height, true));
    }
}
