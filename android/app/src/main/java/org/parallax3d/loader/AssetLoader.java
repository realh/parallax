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

package org.parallax3d.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Loads assets from a certain directory.
 */
public class AssetLoader
{
	private static final int CHUNK_SIZE = 8192;

	protected String dirName;

	/**
	 * @param dirName	Use a trailing / if this is a directory, otherwise
	 *                  the directory containing the named file is used.
	 */
	protected AssetLoader(String dirName)
	{
		int sep = dirName.lastIndexOf('/');
		this.dirName = (sep == -1) ? "." : dirName.substring(0, sep);
	}

	/**
	 *
	 * @return	Loader's directory name without trailing /.
	 */
	public String getDirName()
	{
		return dirName;
	}

	public String getPathname(String leafName)
	{
		return dirName + "/" + leafName;
	}

	/**
	 * Note this does not close the stream.
	 * @param strm
	 * @return
	 * @throws IOException
	 */
	public byte[] loadData(InputStream strm) throws IOException
	{
		// There isn't a reliable way to find the length of a stream,
		// so just keep reading until we get -1.
		byte[] buf = null;
		int offset = 0;

        int numRead = 0;

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

		if (offset < buf.length)
		{
			buf = Arrays.copyOf(buf, offset);
		}

		return buf;
	}

	/*
	 * This is the most efficient way I can think of to load a String with
	 * the API available.
	 * Note this closes the stream when done.
	 */
	public String loadText(InputStream strm) throws IOException
	{
		InputStreamReader reader = null;
		StringBuilder builder = null;
		String result = null;

		try
		{
			reader = new InputStreamReader(strm);

			int numRead = 0;

			char[] buf = null;
			int bufLen = 0;

			do
			{
				// If we're lucky available() might return the total size
				// of the file
				int available = strm.available();

				//Log.debug("" + available + " bytes available");

				if (available < CHUNK_SIZE)
					available = CHUNK_SIZE;

				// Create new buffer if necessary
				if (buf == null)
				{
					buf = new char[bufLen = available];
				}

				// Try to fill the buffer
				numRead = reader.read(buf);

				//Log.debug("Read " + numRead + " chars");

				if (numRead > 0)
				{
					// We've read something, make/add to string
					if (builder == null)
					{
						if (result == null)
						{
							// On first pass make a plain String, we can return
							// this without making a StringBuilder if there is
							// no second pass
							result = new String(buf, 0, numRead);
						}
						else
						{
							// This is second pass, make a StringBuilder with
							// contents of first pass, then current and
							// further passes will be added to the builder
							builder = new StringBuilder(result);
							result = null;
						}
					}
					// If we already had a builder or a new one was just
					// created we need to add the just-read buffer to it
					if (builder != null)
						builder.append(buf, 0, numRead);
				}
			} while (numRead != -1);
		} finally
		{
			if (reader != null)
				reader.close();
			else
				strm.close();
		}

		if (result == null && builder != null)
		{
			builder.trimToSize();
			result = builder.toString();
		}
		return result;
	}
}
