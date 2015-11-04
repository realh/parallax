package uk.co.realh.parallax3d;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
}
