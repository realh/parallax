package uk.co.realh.parallax;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import thothbot.parallax.core.client.renderers.Duration;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.scenes.Scene;

public abstract class DemoActivity extends Activity
{
    protected static final String TAG = "Parallax3D";

    private GLSurfaceView surfaceView;

    private DemoAnimatedScene demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo = getDemo();
        demo.onCreate(this);
        surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(new DemoSceneRenderer());
        setContentView(surfaceView);
    }

    protected abstract DemoAnimatedScene getDemo();

    private class DemoSceneRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            // Do nothing until we know viewport size, but ensure everything
            // is recreated in case surface has been recreated after context
            // loss
            demo.deleteRenderer();
        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            demo.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            demo.onDrawFrame();
        }

        public void stop() {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                public void run() {
                    DemoActivity.this.finish();
                }
            } );
        }
    }

    public abstract class DemoAnimatedScene {

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
                onUpdate((double) duration.elapsedMillis());
            }
        }

        protected void onCreate(Activity activity)
        {}

        protected abstract void onStart();

        protected abstract void onUpdate(double duration);


    }
}
