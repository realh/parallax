package uk.co.realh.parallax;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class ParallaxDemo extends ApplicationAdapter
{
	private DemoAnimatedScene demo;

	@Override
	public void create ()
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		demo = new DynamicReflectionScene();
		demo.onStart();
	}

	@Override
	public void render ()
	{
		demo.onUpdate(16.66666666666667);
	}
}
