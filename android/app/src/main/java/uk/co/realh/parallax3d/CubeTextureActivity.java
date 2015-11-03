package uk.co.realh.parallax3d;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import thothbot.parallax.core.client.gl2.Image;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.geometries.BoxGeometry;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.Scene;

public class CubeTextureActivity extends AppCompatActivity {

    private static final String TAG = "Parallax3D";

    private GLSurfaceView surfaceView;

    //private PerspectiveCamera camera;
    private Camera camera;

    private Mesh mesh;

    private Image crateImage;

    private Scene scene;

    private WebGLRenderer renderer;

    private void setupScene(int width, int height)
    {
        /*
        camera = new PerspectiveCamera(
                70, // fov
                (double) width / (double) height,
                1, // near
                1000 // far
        );
        camera.getPosition().setZ(400);
        */
        camera = new OrthographicCamera(2, 2, 1, -1);

        BoxGeometry geometry = new BoxGeometry( 1.5, 1.5, 1.5 );

        MeshBasicMaterial material = new MeshBasicMaterial();
        //material.setMap( new Texture(crateImage) );
        material.setColor(new Color(0x8080ff));

        mesh = new Mesh(geometry, material);

        scene = new Scene();
        scene.add(mesh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            crateImage = ImageAssetLoader.createImageFromAsset(
                    getAssets(), "textures/crate.gif");
            surfaceView = new GLSurfaceView(this);
            surfaceView.setEGLContextClientVersion(2);
            surfaceView.setRenderer(new CubeTextureRenderer());
            setContentView(surfaceView);
        } catch (Throwable e) {
            Log.e(TAG, "Exception in onCreate", e);
        }

    }

    private class CubeTextureRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            // Do nothing until we know viewport size, but ensure everything
            // is recreated in case surface has been recreated after context
            // loss
            renderer = null;
        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            int size = width > height ? height : width;
            boolean create = renderer == null;
            if (create)
                renderer = new WebGLRenderer(width, height);
            renderer.setViewport((width - size) / 2, (height - size) / 2,
                    size, size);
            if (create)
                setupScene(size, size);
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            mesh.getRotation().addX(0.005);
            mesh.getRotation().addY(0.01);

            renderer.render(scene, camera);
        }

        private void stop() {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                public void run() {
                    CubeTextureActivity.this.finish();
                }
            } );
        }
    }
}
