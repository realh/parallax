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

package org.parallax3d.renderer;

import com.badlogic.gdx.graphics.GL20;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class WebGLShaderPrecisionFormat {

	private int min, max, precision;

	public WebGLShaderPrecisionFormat(GL20 gl, int shaderType, int precisionType) {
		IntBuffer range = ByteBuffer.allocateDirect(8).asIntBuffer();
		IntBuffer precision = ByteBuffer.allocateDirect(4).asIntBuffer();
		gl.glGetShaderPrecisionFormat(shaderType, precisionType, range, precision);
		this.min = range.get(0);
		this.max = range.get(1);
		this.precision = precision.get(0);
	}
	
	public int getRangeMin() {
		return min;
	}
	
	public int getRangeMax() {
		return max;
	}

	public int getPrecision() {
		return precision;
	}

}
