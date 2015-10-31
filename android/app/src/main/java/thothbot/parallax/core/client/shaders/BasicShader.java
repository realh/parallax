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


/**
 * Basic shader.
 * <p>
 * Based on the three.js code. 
 * 
 * @author thothbot
 *
 */
public final class BasicShader extends Shader
{

	static class Resources extends DefaultResources
	{
		static Resources INSTANCE = new Resources();

		public String getVertexShader()
		{
		    return thothbot.parallax.core.client.shaders.source.basic.vertex;
		}

		public String getFragmentShader()
		{
		    return thothbot.parallax.core.client.shaders.source.basic.fragment;
		}
	}

	public BasicShader()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.setUniforms(UniformsLib.getCommon());
		this.setUniforms(UniformsLib.getFog());
		this.setUniforms(UniformsLib.getShadowmap());
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.MAP_PARS,
			ChunksVertexShader.LIGHTMAP_PARS,
			ChunksVertexShader.ENVMAP_PARS,
			ChunksVertexShader.COLOR_PARS,
			ChunksVertexShader.MORPHTARGET_PARS,
			ChunksVertexShader.SKINNING_PARS,
			ChunksVertexShader.SHADOWMAP_PARS,
			ChunksVertexShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main1 = Arrays.asList(
			ChunksVertexShader.MAP,
			ChunksVertexShader.LIGHTMAP,
			ChunksVertexShader.COLOR,
			ChunksVertexShader.SKINBASE
		);
		
		List<String> mainEnv = Arrays.asList(
			ChunksVertexShader.MORPHNORMAL,
			ChunksVertexShader.SKINNORMAL,
			ChunksVertexShader.DEFAULTNORMAL
		);

		List<String> main2 = Arrays.asList(
			ChunksVertexShader.MORPHTARGET,
			ChunksVertexShader.SKINNING,
			ChunksVertexShader.DEFAULT,
			ChunksVertexShader.LOGDEPTHBUF,
			
			ChunksVertexShader.WORLDPOS,
			ChunksVertexShader.ENVMAP,
			ChunksVertexShader.SHADOWMAP
		);

		super.updateVertexSource(Shader.updateShaderSource(src, vars, main1, mainEnv, main2));
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksFragmentShader.COLOR_PARS,
			ChunksFragmentShader.MAP_PARS,
			ChunksFragmentShader.ALPHAMAP_PARS,
			ChunksFragmentShader.LIGHTMAP_PARS,
			ChunksFragmentShader.ENVMAP_PARS,
			ChunksFragmentShader.FOG_PARS,
			ChunksFragmentShader.SHADOWMAP_PARS,
			ChunksFragmentShader.SPECULARMAP_PARS,
			ChunksFragmentShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF,
			ChunksFragmentShader.MAP,
			ChunksFragmentShader.ALPHAMAP,
			ChunksFragmentShader.ALPHA_TEST,
			ChunksFragmentShader.SPECULARMAP,
			ChunksFragmentShader.LIGHTMAP,
			ChunksFragmentShader.COLOR,
			ChunksFragmentShader.ENVMAP,
			ChunksFragmentShader.SHADOWMAP,
			ChunksFragmentShader.LINEAR_TO_GAMMA,
			ChunksFragmentShader.FOG
		);
		
		super.updateFragmentSource(Shader.updateShaderSource(src, vars, main));		
	}
}
