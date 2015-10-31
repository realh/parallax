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

package thothbot.parallax.core.client.shaders;

import java.util.Arrays;
import java.util.List;


public class DashedShader extends Shader 
{

	static class Resources extends DefaultResources
	{
		static Resources INSTANCE = new Resources();

		String getVertexShader()
		{
		    return thothbot.parallax.core.client.shaders.source.dashed.vertex;
		}

		String getFragmentShader()
		{
		    return thothbot.parallax.core.client.shaders.source.dashed.fragment;
		}
	}

	public DashedShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.setUniforms(UniformsLib.getCommon());
		this.setUniforms(UniformsLib.getFog());
		this.addUniform("scale",     new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("dashSize",  new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("totalSize", new Uniform(Uniform.TYPE.F, 2.0 ));
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.COLOR_PARS,
			ChunksVertexShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main1 = Arrays.asList(
			ChunksVertexShader.COLOR
		);
		
		List<String> main2 = Arrays.asList(
			ChunksVertexShader.LOGDEPTHBUF
		);
		
		super.updateVertexSource(Shader.updateShaderSource(src, vars, main1, main2));	
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksFragmentShader.COLOR_PARS,
			ChunksFragmentShader.FOG_PARS,
			ChunksFragmentShader.LOGDEPTHBUF_PAR
		);
			
		List<String> main = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF,
			ChunksFragmentShader.COLOR,
			ChunksFragmentShader.FOG
		);
			
		super.updateFragmentSource(Shader.updateShaderSource(src, vars, main));	
	}
}
