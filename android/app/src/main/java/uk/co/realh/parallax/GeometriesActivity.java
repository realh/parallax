package uk.co.realh.parallax;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.android.AndroidAssetLoader;
import org.parallax3d.renderer.Image;
import org.parallax3d.renderer.TextureWrapMode;
import org.parallax3d.textures.Texture;
import org.parallax3d.cameras.PerspectiveCamera;
import org.parallax3d.core.Object3D;
import org.parallax3d.geometries.BoxGeometry;
import org.parallax3d.geometries.CircleGeometry;
import org.parallax3d.geometries.CylinderGeometry;
import org.parallax3d.geometries.IcosahedronGeometry;
import org.parallax3d.geometries.LatheGeometry;
import org.parallax3d.geometries.OctahedronGeometry;
import org.parallax3d.geometries.PlaneGeometry;
import org.parallax3d.geometries.RingGeometry;
import org.parallax3d.geometries.SphereGeometry;
import org.parallax3d.geometries.TetrahedronGeometry;
import org.parallax3d.geometries.TorusGeometry;
import org.parallax3d.geometries.TorusKnotGeometry;
import org.parallax3d.helpers.ArrowHelper;
import org.parallax3d.helpers.AxisHelper;
import org.parallax3d.lights.AmbientLight;
import org.parallax3d.lights.DirectionalLight;
import org.parallax3d.materials.Material;
import org.parallax3d.materials.MeshLambertMaterial;
import org.parallax3d.math.Color;
import org.parallax3d.math.Vector3;
import org.parallax3d.objects.Mesh;

public class GeometriesActivity extends DemoActivity {

    @Override
    protected DemoAnimatedScene getDemo()
    {
        return new DemoAnimatedScene()
        {
            private static final String imageAsset = "UV_Grid_Sm.jpg";

            private Image gridImage;

            PerspectiveCamera camera;

            @Override
            protected void onCreate(Activity activity)
            {
                try
                {
                    gridImage = new AndroidAssetLoader(activity.getAssets(), "textures/").loadImage(imageAsset);
                } catch (Throwable e)
                {
                    Log.e(TAG, "Exception in onCreate", e);
                }
            }

            @Override
            protected void onStart()
            {
                camera = new PerspectiveCamera(45,
                        getRenderer().getAbsoluteAspectRation(),
                        1,
                        2000
                );
                camera.getPosition().setY(400);

                getScene().add(new AmbientLight(0x404040));

                DirectionalLight light = new DirectionalLight(0xffffff);
                light.getPosition().set(0, 1, 0);
                getScene().add(light);

                Texture texture = new Texture(gridImage);
                texture.setWrapS(TextureWrapMode.REPEAT);
                texture.setWrapT(TextureWrapMode.REPEAT);
                texture.setAnisotropy(16);

                MeshLambertMaterial material = new MeshLambertMaterial();
                material.setMap(texture);
                material.setAmbient(new Color(0xbbbbbb));
                material.setSide(Material.SIDE.DOUBLE);

                Object3D object1 = new Mesh(new SphereGeometry(75, 20, 10), material);
                object1.getPosition().set(-400, 0, 200);
                getScene().add(object1);

                Object3D object2 = new Mesh(new IcosahedronGeometry(75, 1), material);
                object2.getPosition().set(-200, 0, 200);
                getScene().add(object2);

                Object3D object3 = new Mesh(new OctahedronGeometry(75, 2), material);
                object3.getPosition().set(0, 0, 200);
                getScene().add(object3);

                Object3D object4 = new Mesh(new TetrahedronGeometry(75, 0), material);
                object4.getPosition().set(200, 0, 200);
                getScene().add(object4);

                //

                Object3D object5 = new Mesh(new PlaneGeometry(100, 100, 4, 4), material);
                object5.getPosition().set(-400, 0, 0);
                getScene().add(object5);

                Object3D object6 = new Mesh(new BoxGeometry(100, 100, 100, 4, 4, 4), material);
                object6.getPosition().set(-200, 0, 0);
                getScene().add(object6);

                Object3D object7 = new Mesh(new CircleGeometry(50, 20, 0, Math.PI * 2), material);
                object7.getPosition().set(0, 0, 0);
                getScene().add(object7);

                Object3D object8 = new Mesh(new RingGeometry(10, 50, 20, 5, 0, Math.PI * 2), material);
                object8.getPosition().set(200, 0, 0);
                getScene().add(object8);

                Object3D object9 = new Mesh(new CylinderGeometry(25, 75, 100, 40, 5), material);
                object9.getPosition().set(400, 0, 0);
                getScene().add(object9);

                List<Vector3> points = new ArrayList<Vector3>();

                for (int i = 0; i < 50; i++)
                {
                    points.add(new Vector3(Math.sin(i * 0.2) * Math.sin(i * 0.1) * 15.0 + 50.0, 0.0, (i - 5.0) * 2.0));
                }

                Object3D object10 = new Mesh(new LatheGeometry(points, 20), material);
                object10.getPosition().set(-400, 0, -200);
                getScene().add(object10);

                Object3D object11 = new Mesh(new TorusGeometry(50, 20, 20, 20), material);
                object11.getPosition().set(-200, 0, -200);
                getScene().add(object11);

                Object3D object12 = new Mesh(new TorusKnotGeometry(50, 10, 50, 20), material);
                object12.getPosition().set(0, 0, -200);
                getScene().add(object12);

                AxisHelper object13 = new AxisHelper();
                object13.getPosition().set(200, 0, -200);
                getScene().add(object13);

                ArrowHelper object14 = new ArrowHelper(new Vector3(0, 1, 0), new Vector3(0, 0, 0), 50);
                object14.getPosition().set(400, 0, -200);
                getScene().add(object14);
            }

            @Override
            protected void onUpdate(double duration)
            {
                camera.getPosition().setX(Math.cos(duration * 0.0001) * 800.0);
                camera.getPosition().setZ(Math.sin(duration * 0.0001) * 800.0);

                camera.lookAt(getScene().getPosition());

                for (int i = 0, l = getScene().getChildren().size(); i < l; i++)
                {
                    Object3D object = getScene().getChildren().get(i);

                    object.getRotation().addX(0.01);
                    object.getRotation().addY(0.005);
                }

                getRenderer().render(getScene(), camera);
            }
        };
    }
}
