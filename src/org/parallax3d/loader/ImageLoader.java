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

import org.parallax3d.renderer.Image;

/**
 * An ImageLoader will usually be implemented with an AssetLoader, but this
 * interface is independent for greater flexibility.
 */
public interface ImageLoader
{
	public Image loadImage(String leafname) throws IOException;

	/**
	 *
	 * @param leafname
	 * @param flipY		Indicates image needs inverting for OpenGL coordinates
	 * @return
	 * @throws IOException
	 */
	public Image loadImage(String leafname, boolean flipY) throws IOException;

	public byte[] loadData(String leafname) throws IOException;
}
