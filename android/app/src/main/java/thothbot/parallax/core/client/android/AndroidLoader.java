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
import thothbot.parallax.core.shared.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import thothbot.parallax.core.client.gl2.Image;
//import thothbot.parallax.core.client.renderers.Duration;
import thothbot.parallax.loader.shared.AssetLoader;
import thothbot.parallax.loader.shared.ImageLoader;

public abstract class AndroidLoader extends AssetLoader implements ImageLoader
{

	private static final int CHUNK_SIZE = 8192;

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
		return new AndroidImage(bmp);
	}

	@Override
	public byte[] loadData(String leafname) throws IOException
	{
		// AssetManager doesn't provide a reliable way to find the length of an
		// asset, so just keep reading from an InputStream until we get -1.
		InputStream strm = null;

		byte[] buf = null;
		int offset = 0;

		//Duration dur = new Duration();

		try
		{
			int numRead = 0;

			strm = openInputStream(leafname);

			do
			{
				// At least we can try to make a good guess at size
				int available = strm.available();
				if (available < CHUNK_SIZE)
					available = CHUNK_SIZE;
				if (buf == null)
				{
					buf = new byte[available];
				} else if (available <= buf.length - offset)
				{
					available = buf.length - offset;
				} else
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
		}
		finally
		{
			if (strm != null)
				strm.close();
		}

		//Log.debug("Loaded data '" + leafname + "' in " +
		//		((double) dur.elapsedMillis() / 1000) + "s");

		if (offset < buf.length)
		{
			//dur.reset();
			buf = Arrays.copyOf(buf, offset);
			//Log.debug("Took another " +
			//		((double) dur.elapsedMillis() / 1000) + "s" +
			//		" to truncate array");
		}

		return buf;
	}

	public String loadText(String leafname) throws IOException
	{
		byte[] buf = loadData(leafname);

		//Duration dur = new Duration();

		String text = new String(buf);

		//Log.debug("Converted '" + leafname + "' to text in " +
		//		((double) dur.elapsedMillis() / 1000) + "s");

		return text;
	}
}
