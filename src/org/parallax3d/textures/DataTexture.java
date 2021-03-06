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

package org.parallax3d.textures;

import android.opengl.GLES20;

import org.parallax3d.renderer.Uint8Array;
import org.parallax3d.math.Color;

/**
 * Implementation of data texture.
 * 
 * @author thothbot
 *
 */
public class DataTexture extends Texture
{
	private Uint8Array data;
	private int width;
	private int height;

	public DataTexture( int width, int height )
	{
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructor which can be used to generate random data texture.
	 * 
	 * @param width
	 * @param height
	 * @param color
	 */
	public DataTexture( int width, int height, Color color )
	{
		this.width = width;
		this.height = height;

		generateDataTexture(color);
	}
	
	public Uint8Array getData() {
		return data;
	}

	public void setData(Uint8Array data) {
		this.data = data;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void generateDataTexture( Color color ) 
	{
		int size = width * height;
		Uint8Array data = Uint8Array.create( 3 * size );

		int r = (int)Math.floor( color.getR() * 255 );
		int g = (int)Math.floor( color.getG() * 255 );
		int b = (int)Math.floor( color.getB() * 255 );

		for ( int i = 0; i < size; i ++ ) 
		{
			data.set( i * 3, r);
			data.set( i * 3 + 1, g);
			data.set( i * 3 + 2, b);

		}

		setData( data );
		setFormat( GLES20.GL_RGB );
		setNeedsUpdate( true );
	}
}
