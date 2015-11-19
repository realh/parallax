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

import org.parallax3d.renderer.DummyImage;
import org.parallax3d.renderer.GL20Ext;

import org.parallax3d.math.Mathematics;
import org.parallax3d.renderer.WebGLRenderer;

public class RenderTargetTexture extends Texture
{
	private int width;
	private int height;

	private boolean isDepthBuffer = true;
	private boolean isStencilBuffer = true;

	private int webglFramebuffer;
	private int webglRenderbuffer;
	
	public RenderTargetTexture shareDepthFrom;

	public RenderTargetTexture(int width, int height) 
	{
		this(width, height, 
				GL20.GL_CLAMP_TO_EDGE,	GL20.GL_CLAMP_TO_EDGE,
				GL20.GL_LINEAR,      		GL20.GL_LINEAR_MIPMAP_LINEAR,
				GL20.GL_RGBA,             GL20.GL_UNSIGNED_BYTE);
	}

	public RenderTargetTexture(int width, int height, 
			int wrapS,      int wrapT,
			int magFilter,	int minFilter,
			int format,     int type)
	{
		// Create a dummy Image so that WebGLRenderer can get size
		super(new DummyImage(width, height));

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
		((DummyImage) getImage()).setWidth(width);
		((DummyImage) getImage()).setHeight(height);
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
		return this.webglFramebuffer;
	}

	@Override
	public void deallocate(WebGLRenderer renderer)
	{
		if (this.webglTexture == 0)
			return;

		GL20 gl = renderer.getGL();

		gl.glDeleteTexture(this.webglTexture);
		gl.glDeleteFramebuffer(this.webglFramebuffer);
		gl.glDeleteRenderbuffer(this.webglRenderbuffer);

		this.webglTexture = 0;
		this.webglFramebuffer = 0;
		this.webglRenderbuffer = 0;
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
	
	public void setRenderTarget(WebGLRenderer renderer)
	{
		if (this.webglFramebuffer != 0)
			return;

		GL20 gl = renderer.getGL();

		this.deallocate(renderer);
		webglTexture = gl.glGenTexture();

		// Setup texture, create render and frame buffers

		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(this.width)
				&& Mathematics.isPowerOfTwo(this.height);

		webglFramebuffer = gl.glGenFramebuffer();

		if ( this.shareDepthFrom != null ) 
		{
			this.webglRenderbuffer = this.shareDepthFrom.webglRenderbuffer;
		} 
		else 
		{
			webglRenderbuffer = gl.glGenRenderbuffer();
		}

		gl.glBindTexture(GL20.GL_TEXTURE_2D, webglTexture);
		setTextureParameters(gl, GL20.GL_TEXTURE_2D, isTargetPowerOfTwo);

		gl.glTexImage2D(GL20.GL_TEXTURE_2D, 0, getFormat(),
				this.width, this.height, 0, getFormat(), getType(), null);

		setupFrameBuffer(gl, this.webglFramebuffer, GL20.GL_TEXTURE_2D);
		
		if ( this.shareDepthFrom != null ) 
		{

			if ( this.isDepthBuffer && ! this.isStencilBuffer ) {

				gl.glFramebufferRenderbuffer( GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT,
						GL20.GL_RENDERBUFFER, this.webglRenderbuffer );

			} else if ( this.isDepthBuffer && this.isStencilBuffer ) {

                // Not actually supported in OpenGL ES 2.0?
				gl.glFramebufferRenderbuffer( GL20.GL_FRAMEBUFFER,
                        GL20Ext.GL_DEPTH_STENCIL_ATTACHMENT,
						GL20.GL_RENDERBUFFER, this.webglRenderbuffer );

			}

		} else {

			setupRenderBuffer( gl, this.webglRenderbuffer );

		}

		if (isTargetPowerOfTwo)
			gl.glGenerateMipmap(GL20.GL_TEXTURE_2D);

		// Release everything
		gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0);
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
	}

	public void updateRenderTargetMipmap(GL20 gl)
	{	
		gl.glBindTexture(GL20.GL_TEXTURE_2D, this.webglTexture);
		gl.glGenerateMipmap(GL20.GL_TEXTURE_2D);
		gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
	}

	public void setupFrameBuffer(GL20 gl, int framebuffer, int textureTarget)
	{	
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer);
		gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0,
                textureTarget, this.webglTexture, 0);
	}

	public void setupRenderBuffer(GL20 gl, int renderbuffer)
	{	
		gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, renderbuffer);

		if (this.isDepthBuffer && !this.isStencilBuffer) 
		{
			gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER, GL20.GL_DEPTH_COMPONENT16,
                    this.width, this.height);
			gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT,
                    GL20.GL_RENDERBUFFER, renderbuffer);

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
			gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER,
                    GL20Ext.GL_DEPTH_STENCIL, this.width, this.height);
			gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER,
                    GL20Ext.GL_DEPTH_STENCIL_ATTACHMENT,
                    GL20.GL_RENDERBUFFER, renderbuffer);
		} 
		else 
		{
			gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER, GL20.GL_RGBA4,
                    this.width, this.height);
		}
	}
}
