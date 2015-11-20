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

package org.parallax3d.gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.IOException;

import org.parallax3d.renderer.Image;
import org.parallax3d.loader.AssetLoader;
import org.parallax3d.loader.ImageLoader;

public abstract class GdxLoader extends AssetLoader implements ImageLoader
{
	/**
	 * @param dirName Use a trailing / if this is a directory, otherwise
	 *                the directory containing the named file is used.
	 */
	public GdxLoader(String dirName)
	{
		super(dirName);
	}

	public abstract FileHandle openFileHandle(String filename) throws IOException;

	@Override
	public Image loadImage(String leafname) throws IOException
	{
		return new GdxImage(new Pixmap(openFileHandle(leafname)));
	}

	@Override
	public byte[] loadData(String leafname) throws IOException
	{
		return openFileHandle(getPathname(leafname)).readBytes();
	}

	public String loadText(String leafname) throws IOException
	{
		return openFileHandle(getPathname(leafname)).readString();
	}
}
