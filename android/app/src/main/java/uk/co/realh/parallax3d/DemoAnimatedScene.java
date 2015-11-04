package uk.co.realh.parallax3d;

import android.app.Activity;

import thothbot.parallax.core.client.renderers.Duration;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.scenes.Scene;

public abstract class DemoAnimatedScene {

    protected static final String TAG = "Parallax3D";

    private Scene scene;

    private WebGLRenderer renderer;

    private Duration duration;

    public Scene getScene()
    {
        if (scene == null)
            scene = new Scene();
        return scene;
    }

    public WebGLRenderer getRenderer()
    {
        return renderer;
    }

    void deleteRenderer()
    {
        renderer = null;
    }

    void onSurfaceChanged(int width, int height)
    {
        if (null == renderer)
        {
            renderer = new WebGLRenderer(width, height);
            scene = null;
            onStart();
        }
        else
        {
            renderer.onViewportResize(width, height);
        }
    }

    void onDrawFrame()
    {
        if (duration == null)
        {
            duration = new Duration();
            onUpdate(0);
        }
        else
        {
            int dur = duration.elapsedMillis();
            duration.reset();
            onUpdate((double) dur);
        }
    }

    protected abstract void onCreate(Activity activity);

    protected abstract void onStart();

    protected abstract void onUpdate(double duration);


}
