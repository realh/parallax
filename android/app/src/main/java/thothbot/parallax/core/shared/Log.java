/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 *
 * This file is part of Parallax project.
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

package thothbot.parallax.core.shared;


public class Log
{
	private static final String TAG = "Parallax";

	public static void debug(String msg)
	{
		android.util.Log.d(TAG, msg);
	}

	public static void info(String msg)
	{
		android.util.Log.i(TAG, msg);
	}
	
	public static void warn(String msg)
	{
		android.util.Log.w(TAG, msg);
	}
	
	public static void error(String msg)
	{
		android.util.Log.e(TAG, msg);
	}
	
	public static void error(Object ... all)
	{
		StringBuffer result = new StringBuffer();
		for ( Object mods : all )
		{
			result.append(mods + " ");
		}

		android.util.Log.e(TAG, result.toString());
	}
	
	public static void error(String msg, Throwable thrown)
	{
		android.util.Log.e(TAG, msg, thrown);
	}
}
