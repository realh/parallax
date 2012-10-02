/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.plugin.scene.lensflare.shaders;

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

public class LensFlareShader extends Shader 
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/lensFlare.vs")
		TextResource getVertexShader();

		@Source("source/lensFlare.fs")
		TextResource getFragmentShader();
	}

	public LensFlareShader() 
	{
		this(Resources.INSTANCE);
	}
	
	public LensFlareShader(DefaultResources resource) 
	{
		super(resource);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("renderType", new Uniform(Uniform.TYPE.I,  1 ));
		this.addUniform("map", new Uniform(Uniform.TYPE.T ) );
		this.addUniform("occlusionMap", new Uniform(Uniform.TYPE.T ) );
		
		this.addUniform("opacity", new Uniform(Uniform.TYPE.F,  1.0 ));
		this.addUniform("color", new Uniform(Uniform.TYPE.C, new Color( 0xffffff ) ));
		this.addUniform("scale", new Uniform(Uniform.TYPE.V2, new Vector2( 1.0, 1.0 ) ));
		this.addUniform("rotation", new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("screenPosition", new Uniform(Uniform.TYPE.V3,  new Vector3( 0.0, 0.0, 0.0 ) ));
	}
}