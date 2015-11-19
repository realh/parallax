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

import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.math.Mathematics;

public class RenderTargetCubeTexture extends RenderTargetTexture
{
	private int activeCubeFace = 0;

	private List<Integer> webglFramebuffer;
	private List<Integer> webglRenderbuffer;

	public RenderTargetCubeTexture(int width, int height ) 
	{
		super(width, height);
	}
	
	public int getActiveCubeFace() {
		return this.activeCubeFace;
	}
	
	public void setActiveCubeFace(int activeCubeFace) {
		this.activeCubeFace = activeCubeFace;
	}

	private int[] tmpArray = {0};

	@Override
	public void deallocate()
	{
		if (this.webglTexture[0] == 0)
			return;
	
		gl.glDeleteTextures(1, this.webglTexture, 0);
		this.webglTexture[0] = 0;
		
		for (int i = 0; i < 6; i++) 
		{
			tmpArray[0] = this.webglFramebuffer.get(i);
			gl.glDeleteFramebuffers(1, tmpArray, 0);
			tmpArray[0] = this.webglRenderbuffer.get(i);
			gl.glDeleteRenderbuffers(1, tmpArray, 0);
		}
		
		this.webglFramebuffer = null;
		this.webglRenderbuffer = null;
	}
	
	@Override
	public int getWebGLFramebuffer()
	{
		return this.webglFramebuffer.get( getActiveCubeFace() );
	}
	
	@Override
	public void setRenderTarget()
	{
		if (this.webglFramebuffer != null)
			return;

		gl.glGenTextures(1, webglTexture, 0);

		// Setup texture, create render and frame buffers
		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(getWidth())
				&& Mathematics.isPowerOfTwo(getHeight());

		this.webglFramebuffer = new ArrayList<Integer>();
		this.webglRenderbuffer = new ArrayList<Integer>();

		gl.glBindTexture( GL20.GL_TEXTURE_CUBE_MAP, webglTexture[0] );

		setTextureParameters( GL20.GL_TEXTURE_CUBE_MAP, isTargetPowerOfTwo );

		for ( int i = 0; i < 6; i ++ ) 
		{
			gl.glGenFramebuffers(1, tmpArray, 0);
			this.webglFramebuffer.add( tmpArray[0] );
			gl.glGenRenderbuffers(1, tmpArray, 0);
			this.webglRenderbuffer.add( tmpArray[0] );

			gl.glTexImage2D( GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, i, 0, getWidth(), getHeight(), 0,
					getFormat(), getType(), null );

			this.setupFrameBuffer(this.webglFramebuffer.get( i ),
					GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, i);
			this.setupRenderBuffer(this.webglRenderbuffer.get( i ));
		}

		if ( isTargetPowerOfTwo ) 
			gl.glGenerateMipmap( GL20.GL_TEXTURE_CUBE_MAP );

		// Release everything
		gl.glBindTexture( GL20.GL_TEXTURE_CUBE_MAP, 0 );
		gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0);
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
	}
	
	public void setupFrameBuffer(int framebuffer, int textureTarget, int slot)
	{	
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer);
		gl.glFramebufferTexture2D(GL20.GL_COLOR_ATTACHMENT0, textureTarget,
				slot, this.webglTexture[0], 0);
	}
	
	@Override
	public void updateRenderTargetMipmap()
	{	
		gl.glBindTexture( GL20.GL_TEXTURE_CUBE_MAP, this.webglTexture[0] );
		gl.glGenerateMipmap( GL20.GL_TEXTURE_CUBE_MAP );
		gl.glBindTexture( GL20.GL_TEXTURE_CUBE_MAP, 0 );
	}
}
