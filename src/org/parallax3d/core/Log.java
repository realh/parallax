/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.core;


import com.badlogic.gdx.Gdx;

public class Log
{
	private static final String TAG = "Parallax";

	public static void debug(String msg)
	{
		Gdx.app.debug(TAG, msg);
	}

	public static void info(String msg)
	{
		Gdx.app.log(TAG, msg);
	}
	
	public static void warn(String msg)
	{
		Gdx.app.error(TAG, msg);
	}
	
	public static void error(String msg)
	{
		Gdx.app.error(TAG, msg);
	}
	
	public static void error(Object ... all)
	{
		StringBuffer result = new StringBuffer();
		for ( Object mods : all )
		{
			result.append(mods + " ");
		}

		Gdx.app.error(TAG, result.toString());
	}
	
	public static void error(String msg, Throwable thrown)
	{
		Gdx.app.error(TAG, msg, thrown);
	}
}
