package uk.co.realh.parallax;

import android.app.Activity;
import android.opengl.GLES20;
import android.util.Log;

import org.parallax3d.android.AndroidAssetLoader;
import org.parallax3d.renderer.Image;
import org.parallax3d.renderer.TextureWrapMode;
import org.parallax3d.renderer.ShadowMap;
import org.parallax3d.textures.Texture;
import org.parallax3d.cameras.PerspectiveCamera;
import org.parallax3d.core.AbstractGeometry;
import org.parallax3d.core.Geometry;
import org.parallax3d.lights.AmbientLight;
import org.parallax3d.lights.DirectionalLight;
import org.parallax3d.lights.PointLight;
import org.parallax3d.lights.SpotLight;
import org.parallax3d.materials.Material;
import org.parallax3d.materials.MeshPhongMaterial;
import org.parallax3d.math.Color;
import org.parallax3d.objects.Mesh;
import org.parallax3d.loader.JsonLoader;

public class BumpMappingActivity extends DemoActivity
{

    @Override
    protected DemoAnimatedScene getDemo()
    {
        return new DemoAnimatedScene()
        {
			private static final String folder =
					"models/obj/leeperrysmith/";

			private static final String texture =
					"Infinite-Level_02_Disp_NoSmoothUV-4096.jpg";
			private static final String model = "LeePerrySmith.js";

			PerspectiveCamera camera;

			Mesh mesh;

			int mouseX = 0, mouseY = 0;

			Image textureImage;

			String jsonModel;

			AndroidAssetLoader loader;

			@Override
			protected void onCreate(Activity activity)
			{
				try
				{
					loader = new AndroidAssetLoader(activity.getAssets(), folder);
					textureImage = loader.loadImage(texture);
					jsonModel = loader.loadText(model);
				} catch (Throwable e)
				{
					Log.e(TAG, "Exception in onCreate", e);
				}
			}

			@Override
			protected void onStart()
			{
				camera = new PerspectiveCamera(
						27, // fov
						getRenderer().getAbsoluteAspectRation(), // aspect
						1, // near
						10000 // far
				);

				camera.getPosition().setZ(1200);

				// LIGHTS

				getScene().add( new AmbientLight( 0x444444 ) );

				//

				PointLight pointLight = new PointLight( 0xffffff, 1.5, 1000 );
				pointLight.getColor().setHSL( 0.05, 1.0, 0.95 );
				pointLight.getPosition().set( 0, 0, 600 );

				getScene().add( pointLight );

				// shadow for PointLight

				SpotLight spotLight = new SpotLight( 0xffffff, 1.5 );
				spotLight.getPosition().set( 0.05, 0.05, 1 );
				spotLight.getColor().setHSL( 0.6, 1.0, 0.95 );
				getScene().add( spotLight );

				spotLight.getPosition().multiply( 700 );

				spotLight.setCastShadow(true);
				spotLight.setOnlyShadow(true);
//         spotLight.setShadowCameraVisible(true);

				spotLight.setShadowMapWidth( 2048 );
				spotLight.setShadowMapHeight( 2048 );

				spotLight.setShadowCameraNear( 200 );
				spotLight.setShadowCameraFar( 1500 );

				spotLight.setShadowCameraFov( 40 );

				spotLight.setShadowBias( -0.005 );
				spotLight.setShadowDarkness( 0.35 );

				//

				DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 1.5 );
				directionalLight.getPosition().set( 1, -0.5, 1 );
				directionalLight.getColor().setHSL( 0.6, 1, 0.95 );
				getScene().add( directionalLight );

				directionalLight.getPosition().multiply( 500 );

				directionalLight.setCastShadow( true );
//         directionalLight.setShadowCameraVisible(true);

				directionalLight.setShadowMapWidth( 2048 );
				directionalLight.setShadowMapHeight( 2048 );

				directionalLight.setShadowCameraNear( 200 );
				directionalLight.setShadowCameraFar( 1500 );

				directionalLight.setShadowCameraLeft( -500 );
				directionalLight.setShadowCameraRight( 500 );
				directionalLight.setShadowCameraTop( 500 );
				directionalLight.setShadowCameraBottom( -500 );

				directionalLight.setShadowBias( -0.005 );
				directionalLight.setShadowDarkness( 0.35 );

				//

				DirectionalLight directionalLight2 = new DirectionalLight( 0xffffff, 1.2 );
				directionalLight2.getPosition().set(1, -0.5, -1);
				directionalLight2.getColor().setHSL(0.08, 1.0, 0.825);
				getScene().add(directionalLight2);

				Texture mapHeight = new Texture( textureImage );

				mapHeight.setAnisotropy(4);
				mapHeight.getRepeat().set(0.998, 0.998);
				mapHeight.getOffset().set(0.001, 0.001);
				mapHeight.setWrapS(TextureWrapMode.REPEAT);
				mapHeight.setWrapT(TextureWrapMode.REPEAT);
				mapHeight.setFormat(GLES20.GL_RGB);

				final MeshPhongMaterial material = new MeshPhongMaterial();
				material.setAmbient(new Color(0x552811));
				material.setColor(new Color(0x552811));
				material.setSpecular(new Color(0x333333));
				material.setShininess(25);
				material.setBumpMap(mapHeight);
				material.setBumpScale(19);
				material.setMetal(false);

				AbstractGeometry geometry = new JsonLoader(loader).parse(jsonModel);

				createScene((Geometry) geometry, 100, material );

				ShadowMap shadowMap = new ShadowMap(getRenderer(), getScene());
				shadowMap.setCullFrontFaces(false);

				//

				getRenderer().setClearColor(0x060708);
				getRenderer().setGammaInput(true);
				getRenderer().setGammaOutput(true);
			}

			private void createScene( Geometry geometry, double scale, Material material )
			{
				mesh = new Mesh( geometry, material );

				mesh.getPosition().setY( - 50 );
				mesh.getScale().set( scale );

				mesh.setCastShadow(true);
				mesh.setReceiveShadow(true);

				getScene().add( mesh );
			}

			@Override
			protected void onUpdate(double duration)
			{
				/*
				double targetX = mouseX * .001;
				double targetY = mouseY * .001;

				if ( mesh != null )
				{
					mesh.getRotation().addY( 0.05 * ( targetX - mesh.getRotation().getY() ) );
					mesh.getRotation().addX( 0.05 * ( targetY - mesh.getRotation().getX() ) );
				}
				*/

				getRenderer().render(getScene(), camera);
			}
        };
    }
}
