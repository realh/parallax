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

package thothbot.parallax.plugin.postprocessing.client;

import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.plugin.postprocessing.client.shaders.ScreenShader;

public class SavePass extends Pass
{
	private RenderTargetTexture renderTarget;
	private String textureID = "tDiffuse";
	private ShaderMaterial material;
	
	private boolean clear = false;

	public SavePass(int width, int height)
	{
		this(new RenderTargetTexture( width, height ));
		
		renderTarget.setMinFilter(TextureMinFilter.LINEAR);
		renderTarget.setMagFilter(TextureMagFilter.LINEAR);
		renderTarget.setFormat(PixelFormat.RGB);
		renderTarget.setStencilBuffer(true);
	}

	public SavePass( RenderTargetTexture renderTarget ) 
	{
		this.renderTarget = renderTarget;	
		
		this.textureID = "tDiffuse";
		
		this.material = new ShaderMaterial(new ScreenShader());
		
		this.setEnabled(true);
		this.setNeedsSwap(false);
	}
	@Override
	public void render(Postprocessing postprocessing, double delta, boolean maskActive)
	{
		if ( this.material.getShader().getUniforms().containsKey(this.textureID))
			this.material.getShader().getUniforms().get("this.textureID").setValue( postprocessing.getReadBuffer() );

		postprocessing.getQuad().setMaterial(this.material);

		postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera(), this.renderTarget, this.clear );

	}

}
