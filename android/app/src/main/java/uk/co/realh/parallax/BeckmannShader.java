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

package uk.co.realh.parallax;

import thothbot.parallax.core.client.shaders.Shader;
import uk.co.realh.parallax.shaders.beckmann;


/**
 * Beckmann distribution function
 * <p>
 * - to be used in specular term of skin shader<br>
 * - render a screen-aligned quad to precompute a 512 x 512 texture
 * - from <a href="http://developer.nvidia.com/node/171">nvidia.com</a>
 * <p>
 * Based on three.js.code
 * 
 * @author thothbot
 *
 */
public final class BeckmannShader extends Shader 
{
	static class Resources extends DefaultResources
	{
		static Resources INSTANCE = new Resources();
		
		public String getVertexShader()
		{
			return beckmann.vertex;
		}

		public String getFragmentShader()
		{
			return beckmann.fragment;
		}
	}

	public BeckmannShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms() {

	}
}
