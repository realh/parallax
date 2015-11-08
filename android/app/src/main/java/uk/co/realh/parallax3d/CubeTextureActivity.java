package uk.co.realh.parallax3d;

import android.app.Activity;
import android.util.Log;

import thothbot.parallax.core.client.android.AndroidImage;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.geometries.BoxGeometry;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.objects.Mesh;

public class CubeTextureActivity extends DemoActivity {

    @Override
    protected DemoAnimatedScene getDemo()
    {
        return new DemoAnimatedScene()
        {

            private static final String imageAsset = "textures/crate.gif";

            private AndroidImage crateImage;

            private PerspectiveCamera camera;

            private Mesh mesh;

            @Override
            protected void onCreate(Activity activity)
            {
                try
                {
                    crateImage = ImageAssetLoader.createImageFromAsset(
                            activity.getAssets(), imageAsset);
                } catch (Throwable e)
                {
                    Log.e(TAG, "Exception in onCreate", e);
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
