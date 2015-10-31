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


/**
 * Source for all chunks.
 * 
 * @author thothbot
 *
 */
public class Chunks
{
	static String getAlphamapFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.alphamap.fragment;
	}
	
	static String getAlphamapParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.alphamap_pars.fragment;
	}

	static String getAlphatestFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.alphatest.fragment;
	}
	
	static String getBumpmapParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.bumpmap_pars.fragment;
	}

	static String getColorFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.color.fragment;
	}

	static String getColorParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.color_pars.fragment;
	}

	static String getColorParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.color_pars.vertex;
	}

	static String getColorVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.color.vertex;
	}

	static String getDefaultNormalVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.defaultnormal.vertex;
	}
		
	static String getDefaultVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.default_shader.vertex;
	}

	static String getEnvmapFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.envmap.fragment;
	}

	static String getEnvmapParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.envmap_pars.fragment;
	}

	static String getEnvmapParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.envmap_pars.vertex;
	}

	static String getEnvmapVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.envmap.vertex;
	}

	static String getFogFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.fog.fragment;
	}

	static String getFogParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.fog_pars.fragment;
	}

	static String getLightmapFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lightmap.fragment;
	}

	static String getLightmapParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lightmap_pars.fragment;
	}

	static String getLightmapParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lightmap_pars.vertex;
	}

	static String getLightmapVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lightmap.vertex;
	}

	static String getLightsLambertParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lights_lambert_pars.vertex;
	}

	static String getLightsLambertVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lights_lambert.vertex;
	}

	static String getLightsPhongFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lights_phong.fragment;
	}

	static String getLightsPhongParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lights_phong_pars.fragment;
	}

	static String getLightsPhongParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lights_phong_pars.vertex;
	}

	static String getLightsPhongVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.lights_phong.vertex;
	}

	static String getLinearToGammaFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.linear_to_gamma.fragment;
	}
	
	static String getLogdepthbufFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.logdepthbuf.fragment;
	}
	
	static String getLogdepthbufParFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.logdepthbuf_par.fragment;
	}
	
	static String getLogdepthbufParVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.logdepthbuf_par.vertex;
	}
	
	static String getLogdepthbufVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.logdepthbuf.vertex;
	}

	static String getMapFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.map.fragment;
	}

	static String getMapParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.map_pars.fragment;
	}

	static String getMapParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.map_pars.vertex;
	}

	static String getMapParticleFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.map_particle.fragment;
	}

	static String getMapParticleParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.map_particle_pars.fragment;
	}

	static String getMapVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.map.vertex;
	}

	static String getMorphnormalVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.morphnormal.vertex;
	}

	static String getMorphtargetParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.morphtarget_pars.vertex;
	}

	static String getMorphtargetVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.morphtarget.vertex;
	}
	
	static String getNormalmapParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.normalmap_pars.fragment;
	}

	static String getShadowmapFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.shadowmap.fragment;
	}

	static String getShadowmapParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.shadowmap_pars.fragment;
	}

	static String getShadowmapParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.shadowmap_pars.vertex;
	}

	static String getShadowmapVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.shadowmap.vertex;
	}

	static String getSkinningParsVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.skinning_pars.vertex;
	}

	static String getSkinningVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.skinning.vertex;
	}
	
	static String getSkinBaseVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.skinbase.vertex;
	}
	
	static String getSkinNormalVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.skinnormal.vertex;
	}
	
	static String getSpecularmapFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.specularmap.fragment;
	}
	
	static String getSpecularmapParsFragment()
	{
	    return thothbot.parallax.core.client.shaders.chunk.specularmap_pars.fragment;
	}
	
	static String getWorldposVertex()
	{
	    return thothbot.parallax.core.client.shaders.chunk.worldpos.vertex;
	}
}
