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

package org.parallax3d.shaders;

import java.util.Arrays;
import java.util.List;


/**
 * CubeGeometry map shader.
 * <p>
 * Based on three.js code.
 *  
 * @author thothbot
 *
 */
public final class CubeShader extends Shader 
{
	static class Resources extends DefaultResources
	{
		static Resources INSTANCE = new Resources();

		public String getVertexShader()
		{
		    return org.parallax3d.shaders.source.cube.vertex;
		}

		public String getFragmentShader()
		{
		    return org.parallax3d.shaders.source.cube.fragment;
		}
	}

	public CubeShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tCube", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tFlip", new Uniform(Uniform.TYPE.F, -1.0 ));
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main = Arrays.asList(
			ChunksVertexShader.LOGDEPTHBUF
		);

		super.updateVertexSource(Shader.updateShaderSource(src, vars, main));	
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF_PAR
		);
			
		List<String> main = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF
		);
			
		super.updateFragmentSource(Shader.updateShaderSource(src, vars, main));	
	}
}
