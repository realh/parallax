package uk.co.realh.parallax;

import android.app.Activity;
import android.util.Log;

import org.parallax3d.android.AndroidAssetLoader;
import org.parallax3d.renderer.Image;
import org.parallax3d.textures.Texture;
import org.parallax3d.cameras.PerspectiveCamera;
import org.parallax3d.geometries.BoxGeometry;
import org.parallax3d.materials.MeshBasicMaterial;
import org.parallax3d.math.Color;
import org.parallax3d.objects.Mesh;

public class CubeTextureActivity extends DemoActivity {

    @Override
    protected DemoAnimatedScene getDemo()
    {
        return new DemoAnimatedScene()
        {

            private static final String imageAsset = "crate.gif";

            private Image crateImage;

            private PerspectiveCamera camera;

            private Mesh mesh;

            @Override
            protected void onCreate(Activity activity)
            {
                try
                {
                    crateImage = new AndroidAssetLoader(activity.getAssets(), "textures/").loadImage(imageAsset);
                } catch (Throwable e)
                {
                    Log.e(TAG, "Exception loading crate image", e);
                }
            }

            @Override
            protected void onStart()
            {
                camera = new PerspectiveCamera(
                        70, // fov
                        getRenderer().getAbsoluteAspectRation(),
                        1, // near
                        1000 // far
                );
                camera.getPosition().setZ(400);

                BoxGeometry geometry = new BoxGeometry(200, 200, 200);

                MeshBasicMaterial material = new MeshBasicMaterial();

                material.setMap(new Texture(crateImage));
                material.setColor(new Color(0x8080ff));

                mesh = new Mesh(geometry, material);

                getScene().add(mesh);
            }

            @Override
            protected void onUpdate(double duration)
            {
                mesh.getRotation().addX(0.005);
                mesh.getRotation().addY(0.01);

                getRenderer().render(getScene(), camera);
            }

        };
    }
}
