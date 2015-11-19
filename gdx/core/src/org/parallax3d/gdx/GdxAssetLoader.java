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

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class GdxAssetLoader extends GdxLoader
{

	protected AssetManager assets;

	/**
	 * @param dirName Use a trailing / if this is a directory, otherwise
	 *                the directory containing the named file is used.
	 */
	public GdxAssetLoader(AssetManager assets, String dirName)
	{
		super(dirName);
		this.assets = assets;
	}

	@Override
	public InputStream openInputStream(String filename) throws IOException
	{
		if (filename.charAt(0) != '/')
			filename = getPathname(filename);
		return assets.open(filename);
	}
}
