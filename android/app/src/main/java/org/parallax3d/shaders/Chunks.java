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
	    return org.parallax3d.shaders.chunk.alphamap.fragment;
	}
	
	static String getAlphamapParsFragment()
	{
	    return org.parallax3d.shaders.chunk.alphamap_pars.fragment;
	}

	static String getAlphatestFragment()
	{
	    return org.parallax3d.shaders.chunk.alphatest.fragment;
	}
	
	static String getBumpmapParsFragment()
	{
	    return org.parallax3d.shaders.chunk.bumpmap_pars.fragment;
	}

	static String getColorFragment()
	{
	    return org.parallax3d.shaders.chunk.color.fragment;
	}

	static String getColorParsFragment()
	{
	    return org.parallax3d.shaders.chunk.color_pars.fragment;
	}

	static String getColorParsVertex()
	{
	    return org.parallax3d.shaders.chunk.color_pars.vertex;
	}

	static String getColorVertex()
	{
	    return org.parallax3d.shaders.chunk.color.vertex;
	}

	static String getDefaultNormalVertex()
	{
	    return org.parallax3d.shaders.chunk.defaultnormal.vertex;
	}
		
	static String getDefaultVertex()
	{
	    return org.parallax3d.shaders.chunk.default_shader.vertex;
	}

	static String getEnvmapFragment()
	{
	    return org.parallax3d.shaders.chunk.envmap.fragment;
	}

	static String getEnvmapParsFragment()
	{
	    return org.parallax3d.shaders.chunk.envmap_pars.fragment;
	}

	static String getEnvmapParsVertex()
	{
	    return org.parallax3d.shaders.chunk.envmap_pars.vertex;
	}

	static String getEnvmapVertex()
	{
	    return org.parallax3d.shaders.chunk.envmap.vertex;
	}

	static String getFogFragment()
	{
	    return org.parallax3d.shaders.chunk.fog.fragment;
	}

	static String getFogParsFragment()
	{
	    return org.parallax3d.shaders.chunk.fog_pars.fragment;
	}

	static String getLightmapFragment()
	{
	    return org.parallax3d.shaders.chunk.lightmap.fragment;
	}

	static String getLightmapParsFragment()
	{
	    return org.parallax3d.shaders.chunk.lightmap_pars.fragment;
	}

	static String getLightmapParsVertex()
	{
	    return org.parallax3d.shaders.chunk.lightmap_pars.vertex;
	}

	static String getLightmapVertex()
	{
	    return org.parallax3d.shaders.chunk.lightmap.vertex;
	}

	static String getLightsLambertParsVertex()
	{
	    return org.parallax3d.shaders.chunk.lights_lambert_pars.vertex;
	}

	static String getLightsLambertVertex()
	{
	    return org.parallax3d.shaders.chunk.lights_lambert.vertex;
	}

	static String getLightsPhongFragment()
	{
	    return org.parallax3d.shaders.chunk.lights_phong.fragment;
	}

	static String getLightsPhongParsFragment()
	{
	    return org.parallax3d.shaders.chunk.lights_phong_pars.fragment;
	}

	static String getLightsPhongParsVertex()
	{
	    return org.parallax3d.shaders.chunk.lights_phong_pars.vertex;
	}

	static String getLightsPhongVertex()
	{
	    return org.parallax3d.shaders.chunk.lights_phong.vertex;
	}

	static String getLinearToGammaFragment()
	{
	    return org.parallax3d.shaders.chunk.linear_to_gamma.fragment;
	}
	
	static String getLogdepthbufFragment()
	{
	    return org.parallax3d.shaders.chunk.logdepthbuf.fragment;
	}
	
	static String getLogdepthbufParFragment()
	{
	    return org.parallax3d.shaders.chunk.logdepthbuf_par.fragment;
	}
	
	static String getLogdepthbufParVertex()
	{
	    return org.parallax3d.shaders.chunk.logdepthbuf_par.vertex;
	}
	
	static String getLogdepthbufVertex()
	{
	    return org.parallax3d.shaders.chunk.logdepthbuf.vertex;
	}

	static String getMapFragment()
	{
	    return org.parallax3d.shaders.chunk.map.fragment;
	}

	static String getMapParsFragment()
	{
	    return org.parallax3d.shaders.chunk.map_pars.fragment;
	}

	static String getMapParsVertex()
	{
	    return org.parallax3d.shaders.chunk.map_pars.vertex;
	}

	static String getMapParticleFragment()
	{
	    return org.parallax3d.shaders.chunk.map_particle.fragment;
	}

	static String getMapParticleParsFragment()
	{
	    return org.parallax3d.shaders.chunk.map_particle_pars.fragment;
	}

	static String getMapVertex()
	{
	    return org.parallax3d.shaders.chunk.map.vertex;
	}

	static String getMorphnormalVertex()
	{
	    return org.parallax3d.shaders.chunk.morphnormal.vertex;
	}

	static String getMorphtargetParsVertex()
	{
	    return org.parallax3d.shaders.chunk.morphtarget_pars.vertex;
	}

	static String getMorphtargetVertex()
	{
	    return org.parallax3d.shaders.chunk.morphtarget.vertex;
	}
	
	static String getNormalmapParsFragment()
	{
	    return org.parallax3d.shaders.chunk.normalmap_pars.fragment;
	}

	static String getShadowmapFragment()
	{
	    return org.parallax3d.shaders.chunk.shadowmap.fragment;
	}

	static String getShadowmapParsFragment()
	{
	    return org.parallax3d.shaders.chunk.shadowmap_pars.fragment;
	}

	static String getShadowmapParsVertex()
	{
	    return org.parallax3d.shaders.chunk.shadowmap_pars.vertex;
	}

	static String getShadowmapVertex()
	{
	    return org.parallax3d.shaders.chunk.shadowmap.vertex;
	}

	static String getSkinningParsVertex()
	{
	    return org.parallax3d.shaders.chunk.skinning_pars.vertex;
	}

	static String getSkinningVertex()
	{
	    return org.parallax3d.shaders.chunk.skinning.vertex;
	}
	
	static String getSkinBaseVertex()
	{
	    return org.parallax3d.shaders.chunk.skinbase.vertex;
	}
	
	static String getSkinNormalVertex()
	{
	    return org.parallax3d.shaders.chunk.skinnormal.vertex;
	}
	
	static String getSpecularmapFragment()
	{
	    return org.parallax3d.shaders.chunk.specularmap.fragment;
	}
	
	static String getSpecularmapParsFragment()
	{
	    return org.parallax3d.shaders.chunk.specularmap_pars.fragment;
	}
	
	static String getWorldposVertex()
	{
	    return org.parallax3d.shaders.chunk.worldpos.vertex;
	}
}
