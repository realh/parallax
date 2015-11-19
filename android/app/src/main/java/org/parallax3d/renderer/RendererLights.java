/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
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

package org.parallax3d.renderer;

import java.util.List;
import java.util.Map;

import org.parallax3d.shaders.Uniform;
import org.parallax3d.lights.AmbientLight;
import org.parallax3d.lights.DirectionalLight;
import org.parallax3d.lights.HemisphereLight;
import org.parallax3d.lights.Light;
import org.parallax3d.lights.PointLight;
import org.parallax3d.lights.ShadowLight;
import org.parallax3d.lights.SpotLight;
import org.parallax3d.scenes.Scene;

/**
 * Lights used in the {@link Scene}.
 * 
 * @author thothbot
 *
 */
public class RendererLights
{
	public AmbientLight.UniformAmbient ambient;
	public DirectionalLight.UniformDirectional directional;
	public PointLight.UniformPoint point;
	public SpotLight.UniformSport spot;
	public HemisphereLight.UniformHemisphere hemi;
	
	public RendererLights() 
	{
		ambient = new AmbientLight.UniformAmbient();
		directional = new DirectionalLight.UniformDirectional();
		point = new PointLight.UniformPoint();
		spot = new SpotLight.UniformSport();
		hemi = new HemisphereLight.UniformHemisphere();
	}
				
	public void setupLights ( List<Light> lights, boolean  isGammaInput) 
	{
		// Reset uniforms
		ambient.reset();
		directional.reset();
		point.reset();
		spot.reset();
		hemi.reset();
		
		for ( Light light: lights) 
		{
			if ( (light instanceof ShadowLight && ((ShadowLight)light).isOnlyShadow()) || ! light.isVisible()) 
				continue;

			light.setupRendererLights(this, isGammaInput);
		}
	}
	
	public void refreshUniformsLights ( Map<String, Uniform> uniforms ) 
	{
		ambient.refreshUniform(uniforms);
		directional.refreshUniform(uniforms);
		point.refreshUniform(uniforms);
		spot.refreshUniform(uniforms);
		hemi.refreshUniform(uniforms);
	}
}
