/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 *
 * This file is part of the Android port of the Parallax project.
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

package uk.co.realh.parallax;

import com.badlogic.gdx.Gdx;

import org.parallax3d.renderer.WebGLRenderer;
import org.parallax3d.scenes.Scene;

public abstract class DemoAnimatedScene
{
	private WebGLRenderer renderer;

	private Scene scene;

	public DemoAnimatedScene()
	{
		renderer = new WebGLRenderer(Gdx.gl,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		scene = new Scene();
	}

	public abstract void onStart();

	public abstract void onUpdate(double duration);

	protected WebGLRenderer getRenderer()
	{
		return renderer;
	}

	protected Scene getScene()
	{
		return scene;
	}
}
