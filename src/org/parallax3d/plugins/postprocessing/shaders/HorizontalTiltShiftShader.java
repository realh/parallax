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

package org.parallax3d.plugins.postprocessing.shaders;

import org.parallax3d.shaders.Shader;
import org.parallax3d.shaders.Uniform;


/**
 * Simple fake tilt-shift effect, modulating two pass Gaussian blur (see above) by vertical position
 * <p>
 * Based on three.js code
 * 
 * <ul>
 * <li>9 samples per pass</li>
 * <li>standard deviation 2.7</li>
 * <li>"h" parameters should be set to "1 / width" and "1 / height"</li>
 * <li>"r" parameter control where "focused" horizontal line lies</li>
 * </ul>
 * 
 * @author thothbot
 *  
 */
public final class HorizontalTiltShiftShader extends Shader
{
	static class Resources extends DefaultResources
	{
		static Resources INSTANCE = new Resources();
		
		@Override
		public String getVertexShader()
		{
			return org.parallax3d.plugins.postprocessing.shaders.source.defaultUv.vertex;
		}

		@Override
		public String getFragmentShader()
		{
			return org.parallax3d.plugins.postprocessing.shaders.source.horizontalTiltShift.fragment;
		}
	}

	public HorizontalTiltShiftShader() 
	{
		super(Resources.INSTANCE);
	}
	
	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("h", new Uniform(Uniform.TYPE.F, 1.0/512.0));
		this.addUniform("r", new Uniform(Uniform.TYPE.F, 0.35));
	}
}
