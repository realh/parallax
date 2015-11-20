package uk.co.realh.parallax;

import com.badlogic.gdx.ApplicationAdapter;

public class DynamicReflectionDemo extends ApplicationAdapter
{
	private DemoAnimatedScene demo;

	@Override
	public void create ()
	{
		demo = new DynamicReflectionScene();
		demo.onStart();
	}

	@Override
	public void render ()
	{
		demo.onUpdate(16.66666666666667);
	}
}
