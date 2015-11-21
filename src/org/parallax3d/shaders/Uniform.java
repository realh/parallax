/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 # Copyright 2015 Tony Houghton, h@realh.co.uk
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

package org.parallax3d.shaders;

import org.parallax3d.renderer.Float32Array;

/**
 * Shader's uniforms.
 * 
 * @author thothbot
 *
 */
public class Uniform
{
	public static enum TYPE {
		I,  // single integer
		F,  // single double
		V2, // single Vector2
		V3, // single Vector3
		V4, // single Vector4
		C,  // single Color
		FV1,// flat array of floats (JS or typed array)
		FV, // flat array of floats with 3 x N size (JS or typed array)
		V2V,// array of Vector2
		V3V,// array of Vector3
		V4V,// array of Vector4
		M4, // single Matrix4
		M4V,// array of Matrix4
		T,  // single Texture (2d or cube)
		TV // array of Texture (2d)
	};

	private Uniform.TYPE type;
	private Object value;
	private Float32Array cache_array;
	private int location = -1;

	public Uniform(Uniform.TYPE type) 
	{
		this(type, null);
	}

	public Uniform(Uniform.TYPE type, Object value) 
	{
		this.type = type;
		this.value = value;
	}
		
	public Uniform.TYPE getType() {
		return this.type;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Float32Array getCacheArray() {
		return this.cache_array;
	}
	
	public void setCacheArray(Float32Array array) {
		this.cache_array = array;
	}
	
	public int getLocation() {
		return this.location;
	}
	
	public void setLocation(int location) {
		this.location = location;
	}
	
	public String toString()
	{
		return "{type=" + type.name() 
				+ ", value=" + value 
				+ ", location=" + location + "}\n";
	}
	
	public Uniform clone()
	{
		Uniform result = new Uniform(this.type, this.value);
		result.cache_array = this.cache_array;

		return result;
	}
}
