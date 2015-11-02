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

package thothbot.parallax.core.client.gl2;

import android.opengl.GLES20;

public final class WebGLShaderPrecisionFormat {

	private int[] precision = new int[3];

	public WebGLShaderPrecisionFormat(int shaderType, int precisionType) {
		GLES20.glGetShaderPrecisionFormat(shaderType, precisionType,
				precision, 0, precision, 2);
	}
	
	public int getRangeMin() {
		return this.precision[0];
	}
	
	public int getRangeMax() {
		return this.precision[1];
	}

	public int getPrecision() {
		return this.precision[2];
	}

}
