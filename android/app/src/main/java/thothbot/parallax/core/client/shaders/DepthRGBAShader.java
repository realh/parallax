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
 * Depth encoding into RGBA texture.
 * <p>
 * Based on SpiderGL shadow map example @see <a href="http://spidergl.org/example.php?id=6">http://spidergl.org</a><br>
 * Originally from @see <a href="http://www.gamedev.net/topic/442138-packing-a-float-into-a-a8r8g8b8-texture-shader/page__whichpage__1%25EF%25BF%25BD">http://www.gamedev.net</a><br>
 * See also here @see <a href="http://aras-p.info/blog/2009/07/30/encoding-floats-to-rgba-the-final/">http://aras-p.info</a>
 * 
 * @author thothbot
 */
public final class DepthRGBAShader extends Shader
{
	static class Resources extends DefaultResources
	{
		static Resources INSTANCE = new Resources();
		
		public String getVertexShader()
		{
		    return thothbot.parallax.core.client.shaders.source.depthRGBA.vertex;
		}

		public String getFragmentShader()
		{
		    return thothbot.parallax.core.client.shaders.source.depthRGBA.fragment;
		}
	}

	public DepthRGBAShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.MORPHTARGET_PARS,
			ChunksVertexShader.SKINNING_PARS,
			ChunksVertexShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main = Arrays.asList(
			ChunksVertexShader.SKINBASE,
			ChunksVertexShader.MORPHTARGET,
			ChunksVertexShader.SKINNING,
			ChunksVertexShader.DEFAULT,
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
