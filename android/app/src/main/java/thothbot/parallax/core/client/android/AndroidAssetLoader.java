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

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import thothbot.parallax.core.client.gl2.Image;
import thothbot.parallax.loader.shared.AssetLoader;
import thothbot.parallax.loader.shared.ImageLoader;

public class AndroidAssetLoader extends AssetLoader implements ImageLoader
{
	private static final String TAG = "Parallax";

	protected AssetManager assets;

	/**
	 * @param dirName Use a trailing / if this is a directory, otherwise
	 *                the directory containing the named file is used.
	 */
	public AndroidAssetLoader(AssetManager assets, String dirName)
	{
		super(dirName);
		this.assets = assets;
	}

	public InputStream open(String filename) throws IOException
	{
		if (filename.charAt(0) != '/')
			filename = getPathname(filename);
		return assets.open(filename);
	}

	@Override
	public Image loadImage(String leafname) throws IOException
	{
		InputStream strm = open(leafname);
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
}
