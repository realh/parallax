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
 * Hue and saturation adjustment
 * <a href="https://github.com/evanw/glfx.js">github.com</a>
 * <p>
 * Based on three.js code
 * 
 * <ul>
 * <li>hue: -1 to 1 (-1 is 180 degrees in the negative direction, 0 is no change, etc.)</li>
 * <li>saturation: -1 to 1 (-1 is solid gray, 0 is no change, and 1 is maximum contrast)</li>
 * </ul>
 * 
 * @author thothbot
 * 
 */
public final class HueSaturationShader extends Shader 
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
			return thothbot.parallax.plugins.postprocessing.shaders.source.hueSaturation.fragment;
		}
	}
	
	public HueSaturationShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("hue", new Uniform(Uniform.TYPE.F, 0.0));
		this.addUniform("saturation", new Uniform(Uniform.TYPE.F, 0.0));
	}

}
