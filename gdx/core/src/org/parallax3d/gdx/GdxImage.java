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

import org.parallax3d.renderer.Image;

public class GdxImage implements Image
{
    private Pixmap pixmap;

    public GdxImage(Pixmap pixmap)
    {
        this.pixmap = pixmap;
    }

    @Override
    public void glTexImage2D(int target)
    {
        Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(),
                pixmap.getWidth(), pixmap.getHeight(), 0,
                pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
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
