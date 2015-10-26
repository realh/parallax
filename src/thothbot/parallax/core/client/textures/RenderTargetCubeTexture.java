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

package thothbot.parallax.core.client.textures;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.math.Mathematics;

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
	
		GLES20.glDeleteTextures(1, this.webglTexture, 0);
		this.webglTexture[0] = 0;
		
		for (int i = 0; i < 6; i++) 
		{
			tmpArray[0] = this.webglFramebuffer.get(i);
			GLES20.glDeleteFramebuffers(1, tmpArray, 0);
			tmpArray[0] = this.webglRenderbuffer.get(i);
			GLES20.glDeleteRenderbuffers(1, tmpArray, 0);
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

		GLES20.glGenTextures(1, webglTexture, 0);

		// Setup texture, create render and frame buffers
		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(getWidth())
				&& Mathematics.isPowerOfTwo(getHeight());

		this.webglFramebuffer = new ArrayList<Integer>();
		this.webglRenderbuffer = new ArrayList<Integer>();

		GLES20.glBindTexture( GLES20.GL_TEXTURE_CUBE_MAP, webglTexture[0] );

		setTextureParameters( GLES20.GL_TEXTURE_CUBE_MAP, isTargetPowerOfTwo );

		for ( int i = 0; i < 6; i ++ ) 
		{
			GLES20.glGenFramebuffers(1, tmpArray, 0);
			this.webglFramebuffer.add( tmpArray[0] );
			GLES20.glGenRenderbuffers(1, tmpArray, 0);
			this.webglRenderbuffer.add( tmpArray[0] );

			GLES20.glTexImage2D( GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, i, 0, getWidth(), getHeight(), 0,
					getFormat().getValue(), getType().getValue(), null );

			this.setupFrameBuffer(this.webglFramebuffer.get( i ),
					GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, i);
			this.setupRenderBuffer(this.webglRenderbuffer.get( i ));
		}

		if ( isTargetPowerOfTwo ) 
			GLES20.glGenerateMipmap( GLES20.GL_TEXTURE_CUBE_MAP );

		// Release everything
		GLES20.glBindTexture( GLES20.GL_TEXTURE_CUBE_MAP, 0 );
		GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}
	
	public void setupFrameBuffer(int framebuffer, int textureTarget, int slot)
	{	
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);
		GLES20.glFramebufferTexture2D(GLES20.GL_COLOR_ATTACHMENT0, textureTarget,
				slot, this.webglTexture[0], 0);
	}
	
	@Override
	public void updateRenderTargetMipmap()
	{	
		GLES20.glBindTexture( GLES20.GL_TEXTURE_CUBE_MAP, this.webglTexture[0] );
		GLES20.glGenerateMipmap( GLES20.GL_TEXTURE_CUBE_MAP );
		GLES20.glBindTexture( GLES20.GL_TEXTURE_CUBE_MAP, 0 );
	}
}
