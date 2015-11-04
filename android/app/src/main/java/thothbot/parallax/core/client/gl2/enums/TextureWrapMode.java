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

package thothbot.parallax.core.client.gl2.enums;

import android.opengl.GLES20;

/**
 * Implements the wrap parameter for texture coordinate.
 * It is useful for preventing wrapping artifacts when mapping 
 * a single image onto an object.
 * 
 * @author thothbot
 *
 */
public class TextureWrapMode
{
	/**
	 *  This causes the integer part of the s coordinate to be ignored; 
	 *  the GL uses only the fractional part, thereby creating a 
	 *  repeating pattern. 
	 */
	public final static int REPEAT = GLES20.GL_REPEAT;
	/**
	 * This causes s or t coordinates to be clamped to the range [0, 1] 
	 * and is useful for preventing wrapping artifacts when mapping a 
	 * single image onto an object.
	 */
	public final static int CLAMP_TO_EDGE = GLES20.GL_CLAMP_TO_EDGE;

	public final static int MIRRORED_REPEAT = GLES20.GL_MIRRORED_REPEAT;

}
