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
import org.parallax3d.geometries.parametric.KleinParametricGeometry;
import org.parallax3d.geometries.parametric.MobiusParametricGeometry;
import org.parallax3d.geometries.parametric.PlaneParametricGeometry;
import org.parallax3d.lights.AmbientLight;
import org.parallax3d.lights.DirectionalLight;
import org.parallax3d.materials.Material;
import org.parallax3d.materials.MeshBasicMaterial;
import org.parallax3d.materials.MeshLambertMaterial;
import org.parallax3d.math.Color;
import org.parallax3d.utils.SceneUtils;

public class ParametricGeometryActivity extends DemoActivity {

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
                light.getPosition().set(0, 0, 1);
                getScene().add(light);

                Texture texture = new Texture(gridImage);
                texture.setWrapS(TextureWrapMode.REPEAT);
                texture.setWrapT(TextureWrapMode.REPEAT);
                texture.setAnisotropy(16);

                List<Material> materials = new ArrayList<Material>();
                MeshLambertMaterial lmaterial = new MeshLambertMaterial();
                lmaterial.setMap(texture);
                lmaterial.setAmbient(new Color(0xbbbbbb));
                lmaterial.setSide(Material.SIDE.DOUBLE);
                materials.add(lmaterial);


                MeshBasicMaterial bmaterial = new MeshBasicMaterial();
                bmaterial.setColor(new Color(0xffffff));
                bmaterial.setWireframe(true);
                bmaterial.setTransparent(true);
                bmaterial.setOpacity(0.1);
                bmaterial.setSide(Material.SIDE.DOUBLE);
                materials.add(bmaterial);

                // KleinParametricGeometry Bottle
                Object3D object1 = SceneUtils.createMultiMaterialObject(new KleinParametricGeometry(20, 20), materials);
                object1.getPosition().set(0, 0, 0);
                object1.getScale().multiply(20);
                getScene().add(object1);

                // MobiusParametricGeometry Strip
                Object3D object2 = SceneUtils.createMultiMaterialObject(new MobiusParametricGeometry(20, 20), materials);
                object2.getPosition().set(10, 0, 0);
                object2.getScale().multiply(100);
                getScene().add(object2);

                Object3D object3 = SceneUtils.createMultiMaterialObject(new PlaneParametricGeometry(200, 200, 10, 20), materials);
                object3.getPosition().set(20, 0, 0);
                getScene().add(object3);

                //         DimensionalObject object4 = SceneUtils.createMultiMaterialObject(
                //                 new Mobius3dParametricGeometry(20,20), materials );
                //         object4.getPosition().set( 10, 0, 0 );
                //         object4.getScale().multiply(100);
                //         getScene().addChild( object4 );
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
