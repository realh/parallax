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

package org.parallax3d.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import org.parallax3d.core.Log;

import java.io.IOException;
import java.io.InputStream;

import org.parallax3d.renderer.Image;
import org.parallax3d.loader.AssetLoader;
import org.parallax3d.loader.ImageLoader;

public abstract class AndroidLoader extends AssetLoader implements ImageLoader
{
	private static Matrix flipYMatrix;

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
		return loadImage(leafname, true);
	}

	public Image loadImage(String leafname, boolean flipY) throws IOException
	{
		InputStream strm = null;
		Bitmap bmp = null;

		try
		{
			strm = openInputStream(leafname);
			bmp = BitmapFactory.decodeStream(strm);
		}
		finally
		{
			if (strm != null)
				strm.close();
		}

		if (null == bmp) {
			throw new RuntimeException("Unable to create bitmap from asset '" +
					leafname + "'");
		}
		if (null == bmp.getConfig()) {
			Log.debug("Bitmap has unsupported format, converting");
			Bitmap orig = bmp;
			bmp = bmp.copy(Bitmap.Config.RGB_565, true);
			orig.recycle();
		}

		if (flipY)
		{
			Log.debug("Flipping Bitmap vertically");
			if (flipYMatrix == null)
			{
				flipYMatrix = new Matrix();
				flipYMatrix.setScale(1, -1);
			}
			bmp = Bitmap.createBitmap(bmp, 0, 0,
					bmp.getWidth(), bmp.getHeight(), flipYMatrix, false);
		}

		return new AndroidImage(bmp);
	}

	@Override
	public byte[] loadData(String leafname) throws IOException
	{
		InputStream strm = null;

		byte[] buf = null;

		try
		{
			strm = openInputStream(leafname);
			buf = loadData(strm);
		}
		finally
		{
			if (strm != null)
				strm.close();
		}

		return buf;
	}

	public String loadText(String leafname) throws IOException
	{
		InputStream strm = null;

		try
		{
			strm = openInputStream(leafname);
		} finally
		{
			if (strm != null)
				strm.close();
		}

		return loadText(strm);
	}
}
