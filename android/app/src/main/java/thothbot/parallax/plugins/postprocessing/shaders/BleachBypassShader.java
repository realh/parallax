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
 * Bleach bypass shader
 * <a href="http://en.wikipedia.org/wiki/Bleach_bypass">wikipedia.org</a>
 * <p>
 * Based on three.js code <br>
 * Based on Nvidia example <a href="http://developer.download.nvidia.com/shaderlibrary/webpages/shader_library.html#post_bleach_bypass">nvidia.com</a>
 * 
 * @author thothbot
 *
 */
public final class BleachBypassShader extends Shader
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
			return thothbot.parallax.plugins.postprocessing.shaders.source.bleachbypass.fragment;
		}
	}
	
	public BleachBypassShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("opacity", new Uniform(Uniform.TYPE.F, 1.0));
	}

}
