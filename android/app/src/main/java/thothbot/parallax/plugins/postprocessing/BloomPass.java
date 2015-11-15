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

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.plugins.postprocessing.shaders.ConvolutionShader;
import thothbot.parallax.plugins.postprocessing.shaders.CopyShader;

public class BloomPass extends Pass
{
	private static Vector2 blurX = new Vector2( 0.001953125, 0.0 );
	private static Vector2 blurY = new Vector2( 0.0, 0.001953125 );
	
	private RenderTargetTexture renderTargetX;
	private RenderTargetTexture renderTargetY;
	
	private ShaderMaterial materialScreen;
	private ShaderMaterial materialConvolution;
	
	private boolean clear = false;
	
	public BloomPass()
	{
		this(1);
	}
	
	public BloomPass( double strength )
	{
		this(strength, 25, 4.0, 256);
	}

	public BloomPass( double strength, int kernelSize, double sigma, int resolution ) 
	{
		super();

		// render targets
		this.renderTargetX = new RenderTargetTexture( resolution, resolution );
		this.renderTargetX.setMinFilter(GLES20.GL_LINEAR);
		this.renderTargetX.setMagFilter(GLES20.GL_LINEAR);
		this.renderTargetX.setFormat(GLES20.GL_RGB);
		
		this.renderTargetY = new RenderTargetTexture( resolution, resolution );
		this.renderTargetY.setMinFilter(GLES20.GL_LINEAR);
		this.renderTargetY.setMagFilter(GLES20.GL_LINEAR);
		this.renderTargetY.setFormat(GLES20.GL_RGB);

		// screen material

		this.materialScreen = new ShaderMaterial(new CopyShader());
		this.materialScreen.getShader().getUniforms().get("opacity").setValue( strength );
		this.materialScreen.setBlending(Material.BLENDING.ADDITIVE);
		this.materialScreen.setTransparent(true);

		// convolution material
		Shader convolutionShader = new ConvolutionShader();

		this.materialConvolution = new ShaderMaterial(
				"#define KERNEL_SIZE " + kernelSize + ".0\n" + convolutionShader.getVertexSource(),
				"#define KERNEL_SIZE " + kernelSize + "\n"   + convolutionShader.getFragmentSource()
				);
		this.materialConvolution.getShader().setUniforms(convolutionShader.getUniforms());
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue( BloomPass.blurX );
		this.materialConvolution.getShader().getUniforms().get("cKernel").setValue( Shader.buildKernel( sigma ) );
	}

	@Override
	public void render(Postprocessing postprocessing, double delta, boolean maskActive)
	{
		if ( maskActive ) 
			GLES20.glDisable(GLES20.GL_STENCIL_TEST);

		// Render quad with blured scene into texture (convolution pass 1)
		postprocessing.getQuad().setMaterial(this.materialConvolution);

		this.materialConvolution.getShader().getUniforms().get("tDiffuse" ).setValue( postprocessing.getReadBuffer() );
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue(BloomPass.blurX);

		postprocessing.getRenderer().render(
				postprocessing.getScene(), postprocessing.getCamera(), this.renderTargetX, true);


		// Render quad with blured scene into texture (convolution pass 2)
		this.materialConvolution.getShader().getUniforms().get("tDiffuse").setValue( this.renderTargetX );
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue(BloomPass.blurY);

		postprocessing.getRenderer().render(
				postprocessing.getScene(), postprocessing.getCamera(), this.renderTargetY, true);

		// Render original scene with superimposed blur to texture
		postprocessing.getQuad().setMaterial(this.materialScreen);

		this.materialScreen.getShader().getUniforms().get("tDiffuse").setValue(this.renderTargetY);

		if ( maskActive ) 
			GLES20.glEnable( GLES20.GL_STENCIL_TEST );

		postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera(), postprocessing.getReadBuffer(), this.clear );
	}

}
