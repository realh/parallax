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

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;


/**
 * Two pass Gaussian blur filter (horizontal and vertical blur shaders)
 * <p>
 * Described in <a href="http://www.gamerendering.com/2008/10/11/gaussian-blur-filter-shader/">gamerendering.com</a>
 * and used in <a href="http://www.cake23.de/traveling-wavefronts-lit-up.html">cake23.de</a>
 * <p>
 * Based on three.js code
 * 
 * <ul>
 * <li>9 samples per pass</li>
 * <li>standard deviation 2.7</li>
 * <li>"h" and "v" parameters should be set to "1 / width" and "1 / height"</li>
 * </ul>
 * 
 * @author thothbot
 * 
 */
public final class VerticalBlurShader extends Shader
{
	static class Resources extends DefaultResources
	{
		static Resources INSTANCE = new Resources();
		
		@Override
		public String getVertexShader()
		{
			return thothbot.parallax.plugins.postprocessing.shaders.source.defaultUv.vertex;
		}

		@Override
		public String getFragmentShader()
		{
			return thothbot.parallax.plugins.postprocessing.shaders.source.verticalBlur.fragment;
		}
	}
	
	public VerticalBlurShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T));
		this.addUniform("v", new Uniform(Uniform.TYPE.F, 1.0/512.0));
	}

}
