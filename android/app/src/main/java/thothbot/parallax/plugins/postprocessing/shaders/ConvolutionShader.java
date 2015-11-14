/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Copyright 2015 Tony Houghton, h@realh.co.uk
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

package thothbot.parallax.plugins.postprocessing.shaders;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.math.Vector2;


/**
 * Convolution shader
 * <p>
 * Based on three.js code<b>
 * Ported from o3d sample to WebGL / GLSL <a href="http://o3d.googlecode.com/svn/trunk/samples/convolution.html">o3d.googlecode.com</a>
 * 
 * @author thothbot
 *
 */
public final class ConvolutionShader extends Shader
{
	static class Resources extends DefaultResources
	{
		static Resources INSTANCE = new Resources();
		
		@Override
		public String getVertexShader()
		{
			return thothbot.parallax.plugins.postprocessing.shaders.source.convolution.vertex;
		}

		@Override
		public String getFragmentShader()
		{
			return thothbot.parallax.plugins.postprocessing.shaders.source.convolution.fragment;
		}
	}

	public ConvolutionShader() 
	{
		super(Resources.INSTANCE);
	}
	
	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("uImageIncrement", new Uniform(Uniform.TYPE.V2, new Vector2(0.001953125, 0.0)));
		this.addUniform("cKernel", new Uniform(Uniform.TYPE.FV1, Float32Array.createArray()));
	}
}
