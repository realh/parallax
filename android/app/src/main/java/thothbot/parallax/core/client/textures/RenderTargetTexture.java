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

import thothbot.parallax.core.client.gl2.GLES20Ext;
import thothbot.parallax.core.shared.math.Mathematics;

public class RenderTargetTexture extends Texture
{
	private int width;
	private int height;

	private boolean isDepthBuffer = true;
	private boolean isStencilBuffer = true;

	private int[] webglFramebuffer = {0};
	private int[] webglRenderbuffer = {0};
	
	public RenderTargetTexture shareDepthFrom;

	public RenderTargetTexture(int width, int height) 
	{
		this(width, height, 
				GLES20.GL_CLAMP_TO_EDGE,	GLES20.GL_CLAMP_TO_EDGE,
				GLES20.GL_LINEAR,      		GLES20.GL_LINEAR_MIPMAP_LINEAR,
				GLES20.GL_RGBA,             GLES20.GL_UNSIGNED_BYTE);
	}

	public RenderTargetTexture(int width, int height, 
			int wrapS,      int wrapT,
			int magFilter,	int minFilter,
			int format,     int type)
	{
		super(); // call super Texture

		this.width = width;
		this.height = height;

		setWrapS( wrapS );
		setWrapT( wrapT );
		
		setMagFilter( magFilter );
		setMinFilter( minFilter );
		
		setFormat( format );
		setType( type );
	}
	
	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
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

	public boolean getDepthBuffer() {
		return this.isDepthBuffer;
	}
	
	public void setDepthBuffer(boolean depthBuffer) {
		this.isDepthBuffer = depthBuffer;
	}
	
	public boolean getStencilBuffer() {
		return this.isStencilBuffer;
	}
	
	public void setStencilBuffer(boolean stencilBuffer) {
		this.isStencilBuffer = stencilBuffer;
	}
	
	public int getWebGLFramebuffer() {
		return this.webglFramebuffer[0];
	}

	public void deallocate()
	{
		if (this.webglTexture[0] == 0)
			return;

		GLES20.glDeleteTextures(1, this.webglTexture, 0);
		GLES20.glDeleteFramebuffers(1, this.webglFramebuffer, 0);
		GLES20.glDeleteRenderbuffers(1, this.webglRenderbuffer, 0);

		this.webglTexture[0] = 0;
		this.webglFramebuffer[0] = 0;
		this.webglRenderbuffer[0] = 0;
	}

	public RenderTargetTexture clone()
	{
		RenderTargetTexture tmp = new RenderTargetTexture(this.width, this.height);

		super.clone(tmp);
		
		tmp.setWrapS( getWrapS() );
		tmp.setWrapT( getWrapT() );

		tmp.setMagFilter( getMagFilter() );
		tmp.setMinFilter( getMinFilter() );

		tmp.getOffset().copy(getOffset());
		tmp.getRepeat().copy(getRepeat());

		tmp.setFormat( getFormat() );
		tmp.setType( getType() );

		tmp.isDepthBuffer = this.isDepthBuffer;
		tmp.isStencilBuffer = this.isStencilBuffer;

		return tmp;
	}
	
	public void setRenderTarget()
	{
		if (this.webglFramebuffer[0] != 0)
			return;

		this.deallocate();
		GLES20.glGenTextures(1, webglTexture, 0);

		// Setup texture, create render and frame buffers

		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(this.width)
				&& Mathematics.isPowerOfTwo(this.height);

		GLES20.glGenFramebuffers(1, webglFramebuffer, 0);

		if ( this.shareDepthFrom != null ) 
		{
			this.webglRenderbuffer[0] = this.shareDepthFrom.webglRenderbuffer[0];
		} 
		else 
		{
			GLES20.glGenRenderbuffers(1, webglRenderbuffer, 0);
		}

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, webglTexture[0]);
		setTextureParameters(GLES20.GL_TEXTURE_2D, isTargetPowerOfTwo);

		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, getFormat(),
				this.width, this.height, 0, getFormat(), getType(), null);

		setupFrameBuffer(this.webglFramebuffer[0], GLES20.GL_TEXTURE_2D);
		
		if ( this.shareDepthFrom != null ) 
		{

			if ( this.isDepthBuffer && ! this.isStencilBuffer ) {

				GLES20.glFramebufferRenderbuffer( GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
						GLES20.GL_RENDERBUFFER, this.webglRenderbuffer[0] );

			} else if ( this.isDepthBuffer && this.isStencilBuffer ) {

                // Not actually supported in OpenGL ES 2.0?
				GLES20.glFramebufferRenderbuffer( GLES20.GL_FRAMEBUFFER,
                        GLES20Ext.GL_DEPTH_STENCIL_ATTACHMENT,
						GLES20.GL_RENDERBUFFER, this.webglRenderbuffer[0] );

			}

		} else {

			setupRenderBuffer( this.webglRenderbuffer[0] );

		}

		if (isTargetPowerOfTwo)
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

		// Release everything
		GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}

	public void updateRenderTargetMipmap()
	{	
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.webglTexture[0]);
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	public void setupFrameBuffer(int framebuffer, int textureTarget)
	{	
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                textureTarget, this.webglTexture[0], 0);
	}

	public void setupRenderBuffer(int renderbuffer)
	{	
		GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderbuffer);

		if (this.isDepthBuffer && !this.isStencilBuffer) 
		{
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,
                    this.width, this.height);
			GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                    GLES20.GL_RENDERBUFFER, renderbuffer);

			/*
			 * For some reason this is not working. Defaulting to RGBA4. } else
			 * if( ! this.depthBuffer && this.stencilBuffer ) {
			 * 
			 * _gl.renderbufferStorage( WebGLConstants.RENDERBUFFER,
			 * WebGLConstants.STENCIL_INDEX8, this.width, this.height );
			 * _gl.framebufferRenderbuffer( WebGLConstants.FRAMEBUFFER,
			 * WebGLConstants.STENCIL_ATTACHMENT,
			 * WebGLConstants.RENDERBUFFER, renderbuffer );
			 */
		} 
		else if (this.isDepthBuffer && this.isStencilBuffer) 
		{
            // Not actually supported in OpenGL ES 2.0?
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,
                    GLES20Ext.GL_DEPTH_STENCIL, this.width, this.height);
			GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER,
                    GLES20Ext.GL_DEPTH_STENCIL_ATTACHMENT,
                    GLES20.GL_RENDERBUFFER, renderbuffer);
		} 
		else 
		{
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_RGBA4,
                    this.width, this.height);
		}
	}
}
