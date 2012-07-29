/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.parallax.postprocessing.client;

import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Color3f;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.scenes.Scene;

public class RenderPass extends Pass
{
	private Scene scene;
	private Camera camera;
	private Material overrideMaterial;
	
	private Color3f clearColor;
	private float clearAlpha;
	
	private Color3f oldClearColor;
	private float oldClearAlpha;
	
	private boolean clear = true;
	
	public RenderPass ( Scene scene, Camera camera, Material overrideMaterial, Color3f clearColor )
	{
		this(scene, camera, overrideMaterial, clearColor, 1.0f);
	}
	
	public RenderPass ( Scene scene, Camera camera, Material overrideMaterial, Color3f clearColor, float clearAlpha ) 
	{
		this.scene = scene;
		this.camera = camera;

		this.overrideMaterial = overrideMaterial;

		this.clearColor = clearColor;
		this.clearAlpha = clearAlpha;

		this.oldClearColor = new Color3f();
		this.oldClearAlpha = 1.0f;

		this.setEnabled(true);
		this.setNeedsSwap(false);
	}

	@Override
	public void render(WebGLRenderer renderer, RenderTargetTexture writeBuffer, RenderTargetTexture readBuffer, float delta,
			boolean maskActive)
	{
		this.scene.overrideMaterial = this.overrideMaterial;

		if ( this.clearColor != null ) 
		{

			this.oldClearColor.copy( renderer.getClearColor() );
			this.oldClearAlpha = renderer.getClearAlpha();

			renderer.setClearColor( this.clearColor, this.clearAlpha );

		}

		renderer.render( this.scene, this.camera, readBuffer, this.clear );

		if ( this.clearColor != null)
			renderer.setClearColor( this.oldClearColor, this.oldClearAlpha );

		this.scene.overrideMaterial = null;
	}
}