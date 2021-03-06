package uk.co.realh.parallax;

import android.app.Activity;
import android.util.Log;

import org.parallax3d.android.AndroidAssetLoader;
import org.parallax3d.renderer.Image;
import org.parallax3d.renderer.TextureWrapMode;
import org.parallax3d.renderer.Duration;
import org.parallax3d.textures.Texture;
import org.parallax3d.cameras.PerspectiveCamera;
import org.parallax3d.geometries.PlaneGeometry;
import org.parallax3d.materials.MeshBasicMaterial;
import org.parallax3d.math.Color;
import org.parallax3d.math.Matrix4;
import org.parallax3d.math.Vector3;
import org.parallax3d.math.Vector4;
import org.parallax3d.objects.Mesh;
import org.parallax3d.scenes.FogExp2;

public class MovingVerticesActivity extends DemoActivity
{

    @Override
    protected DemoAnimatedScene getDemo()
    {
        return new DemoAnimatedScene()
        {
            private static final String imageAsset = "water.jpg";
            private static final int FOG_COLOR = 0xAACCFF;

            private Image waterImage;
            PerspectiveCamera camera;

            PlaneGeometry geometry;
            Mesh mesh;

            int worldWidth = 32;
            int worldDepth = 32;

            private double oldTime;

            @Override
            protected void onCreate(Activity activity)
            {
                try
                {
                    waterImage = new AndroidAssetLoader(activity.getAssets(), "textures/").loadImage(imageAsset);
                } catch (Throwable e)
                {
                    Log.e(TAG, "Exception in onCreate", e);
                }
            }

            @Override
            protected void onStart()
            {
                camera = new PerspectiveCamera(
                        60, // fov
                        getRenderer().getAbsoluteAspectRation(), // aspect
                        1, // near
                        20000 // far
                );

                camera.getPosition().setY(200);

                getScene().setFog(new FogExp2(FOG_COLOR, 0.0007));
                getRenderer().setClearColor(FOG_COLOR);

                this.geometry = new PlaneGeometry(20000, 20000, worldWidth - 1, worldDepth - 1);
                this.geometry.applyMatrix(new Matrix4().makeRotationX(-Math.PI / 2.0));

                for (int i = 0, il = this.geometry.getVertices().size(); i < il; i++)
                {
                    this.geometry.getVertices().get(i).setY(35.0 * Math.sin(i / 2.0));
                }

                this.geometry.computeFaceNormals();
                this.geometry.computeVertexNormals();

                Texture texture = new Texture(waterImage);
                texture.setWrapS(TextureWrapMode.REPEAT);
                texture.setWrapT(TextureWrapMode.REPEAT);
                texture.getRepeat().set(5.0, 5.0);

                MeshBasicMaterial material = new MeshBasicMaterial();
                material.setColor(new Color(0x0044ff));
                material.setMap(texture);

                this.mesh = new Mesh(this.geometry, material);
                getScene().add(this.mesh);

                this.oldTime = Duration.currentTimeMillis();
            }

            @Override
            protected void onUpdate(double duration)
            {
                for (int i = 0, l = this.geometry.getVertices().size(); i < l; i++)
                    this.geometry.getVertices().get(i).setY(35.0 * Math.sin(i / 5.0 + (duration * 0.01 + i) / 7.0));

                this.mesh.getGeometry().setVerticesNeedUpdate(true);

                this.oldTime = Duration.currentTimeMillis();

                getRenderer().render(getScene(), camera);

                if (!logged)
                {
                    logged = true;
                    debugVert(0);
                    debugVert(geometry.getVertices().size() - 1);
                }
            }

            private boolean logged = false;

            private void debugVert(int index)
            {
                Vector3 v3 = geometry.getVertices().get(index);
                Log.d(TAG, "Vertex " + index + " is " + v3);
                Vector4 v4 = new Vector4(v3.getX(), v3.getY(), v3.getZ(), 1);
                v4.applyMatrix4(mesh._modelViewMatrix);
                Log.d(TAG, "  Applied model-view: " + v4);
                v4.applyMatrix4(camera.getProjectionMatrix());
                Log.d(TAG, "  Applied projection: " + v4);
                Log.d(TAG, "  z / w = " + (v4.getZ() / v4.getW()));
            }
        };
    }
}
