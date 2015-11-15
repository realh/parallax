/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.plugins.postprocessing;

import android.opengl.GLES20;

import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.scenes.Scene;

public class MaskPass extends Pass
{
	private Scene scene;
	
	private Camera camera;
	private boolean clear = true;
	private boolean inverse = false;
	
	public MaskPass( Scene scene, Camera camera ) 
	{
		this.scene = scene;
		this.camera = camera;
		this.setEnabled(true);
	}
	
	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}
			
	@Override
	public void render (Postprocessing effectComposer, double delta, boolean maskActive)
	{
		// don't update color or depth
		GLES20.glColorMask(false, false, false, false);
		GLES20.glDepthMask( false );

		// set up stencil

		int writeValue, clearValue;

		if ( this.inverse ) 
		{
			writeValue = 0;
			clearValue = 1;
		} 
		else 
		{
			writeValue = 1;
			clearValue = 0;
		}

		GLES20.glEnable(GLES20.GL_STENCIL_TEST);
		GLES20.glStencilOp(GLES20.GL_REPLACE, GLES20.GL_REPLACE, GLES20.GL_REPLACE);
		GLES20.glStencilFunc(GLES20.GL_ALWAYS, writeValue, 0xffffffff);
		GLES20.glClearStencil( clearValue );

		// draw into the stencil buffer
		effectComposer.getRenderer().render( this.scene, this.camera, effectComposer.getReadBuffer(), this.clear );
		effectComposer.getRenderer().render( this.scene, this.camera, effectComposer.getWriteBuffer(), this.clear );

		// re-enable update of color and depth
		GLES20.glColorMask( true, true, true, true );
		GLES20.glDepthMask( true );

		// only render where stencil is set to 1
		GLES20.glStencilFunc( GLES20.GL_EQUAL, 1, 0xffffffff );  // draw if == 1
		GLES20.glStencilOp( GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP );
	}
	
	@Override
	public boolean isMaskActive()
	{
		return true;
	}

}
