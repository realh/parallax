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
 * Depth-of-field shader using mipmaps
 * <p>
 * requires power-of-2 sized render target with enabled mipmaps
 * <p>
 * Based on three.js code<br>
 * From Matt Handley \@applmak
 * 
 * @author thothbot
 *
 */
public final class DOFMipMapShader extends Shader
{
	public static class Resources extends DefaultResources
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
			return thothbot.parallax.plugins.postprocessing.shaders.source.dofmipmap.fragment;
		}
	}

	public DOFMipMapShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tColor", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tDepth", new Uniform(Uniform.TYPE.T ));
		this.addUniform("focus", new Uniform(Uniform.TYPE.F, 1.0));
		this.addUniform("maxblur", new Uniform(Uniform.TYPE.I, true));
	}

}
