package uk.co.realh.parallax;

import android.app.Activity;
import android.opengl.GLES20;
import android.util.Log;

import java.util.Map;

import org.parallax3d.android.AndroidAssetLoader;
import org.parallax3d.renderer.Image;
import org.parallax3d.renderer.TextureWrapMode;
import org.parallax3d.renderer.ShadowMap;
import org.parallax3d.shaders.Uniform;
import org.parallax3d.textures.RenderTargetTexture;
import org.parallax3d.textures.Texture;
import org.parallax3d.cameras.PerspectiveCamera;
import org.parallax3d.core.AbstractGeometry;
import org.parallax3d.core.Geometry;
import org.parallax3d.lights.AmbientLight;
import org.parallax3d.lights.DirectionalLight;
import org.parallax3d.lights.PointLight;
import org.parallax3d.lights.SpotLight;
import org.parallax3d.materials.ShaderMaterial;
import org.parallax3d.math.Color;
import org.parallax3d.math.Vector4;
import org.parallax3d.objects.Mesh;
import org.parallax3d.loader.JsonLoader;
import org.parallax3d.plugins.postprocessing.Postprocessing;
import org.parallax3d.plugins.postprocessing.ShaderPass;
import org.parallax3d.plugins.postprocessing.shaders.CopyShader;

public class SkinMaterialActivity extends DemoActivity
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
			private static final String textureSpec = "Map-SPEC.jpg";
			private static final String textureCol = "Map-COL.jpg";

			PerspectiveCamera camera;

			Mesh mesh;

			Image textureImage;
			Image specImage;
			Image colImage;

			String jsonModel;

			AndroidAssetLoader loader;

			Postprocessing composerBeckmann;

			int mouseX = 0, mouseY = 0;

			boolean firstPass = true;

			@Override
			protected void onCreate(Activity activity)
			{
				try
				{
					loader = new AndroidAssetLoader(activity.getAssets(), folder);
					textureImage = loader.loadImage(texture);
					specImage = loader.loadImage(textureSpec);
					colImage = loader.loadImage(textureCol);
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

				getScene().add( new AmbientLight( 0x555555 ) );

				PointLight pointLight = new PointLight( 0xffffff, 1.5, 1000 );
				pointLight.getPosition().set( 0, 0, 600 );

				getScene().add( pointLight );

				// shadow for PointLight

				SpotLight spotLight = new SpotLight( 0xffffff, 1 );
				spotLight.getPosition().set( 0.05, 0.05, 1 );
				getScene().add( spotLight );

				spotLight.getPosition().multiply( 700 );

				spotLight.setCastShadow(true);
				spotLight.setOnlyShadow(true);
				//spotLight.shadowCameraVisible = true;

				spotLight.setShadowMapWidth( 2048 );
				spotLight.setShadowMapHeight( 2048 );

				spotLight.setShadowCameraNear( 200 );
				spotLight.setShadowCameraFar( 1500 );

				spotLight.setShadowCameraFov( 40 );

				spotLight.setShadowBias( -0.005 );
				spotLight.setShadowDarkness( 0.15 );

				//

				DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 0.85 );
				directionalLight.getPosition().set( 1, -0.5, 1 );
				directionalLight.getColor().setHSL( 0.6, 1.0, 0.85 );
				getScene().add( directionalLight );

				directionalLight.getPosition().multiply( 500 );

				directionalLight.setCastShadow(true);
				//directionalLight.shadowCameraVisible = true;

				directionalLight.setShadowMapWidth( 2048 );
				directionalLight.setShadowMapHeight( 2048 );

				directionalLight.setShadowCameraNear( 200 );
				directionalLight.setShadowCameraFar( 1500 );

				directionalLight.setShadowCameraLeft( -500 );
				directionalLight.setShadowCameraRight( 500 );
				directionalLight.setShadowCameraTop( 500 );
				directionalLight.setShadowCameraBottom( -500 );

				directionalLight.setShadowBias( -0.005 );
				directionalLight.setShadowDarkness( 0.15 );

				//

				DirectionalLight directionalLight2 = new DirectionalLight( 0xffffff, 0.85 );
				directionalLight2.getPosition().set( 1, -0.5, -1 );
				getScene().add( directionalLight2 );

				// COMPOSER BECKMANN

				ShaderPass effectBeckmann = new ShaderPass( new BeckmannShader() );
				ShaderPass effectCopy = new ShaderPass( new CopyShader() );

				effectCopy.setRenderToScreen(true);

				RenderTargetTexture target = new RenderTargetTexture( 512, 512 );
				target.setMinFilter(GLES20.GL_LINEAR);
				target.setMagFilter(GLES20.GL_LINEAR);
				target.setFormat(GLES20.GL_RGB);
				target.setStencilBuffer(false);
				composerBeckmann = new Postprocessing( getRenderer(), getScene(), target );
//         		composerBeckmann.addPass( effectBeckmann );
//         		composerBeckmann.addPass( effectScreen );

				//

				AbstractGeometry geometry = new JsonLoader(loader).parse(jsonModel);
				createScene( (Geometry) geometry, 100 );

				getRenderer().setClearColor(0x4c5159);

				ShadowMap shadowMap = new ShadowMap(getRenderer(), getScene());
				shadowMap.setCullFrontFaces(false);

				getRenderer().setAutoClear(false);
				getRenderer().setGammaInput(true);
				getRenderer().setGammaOutput(true);
			}

			private void createScene( Geometry geometry, double scale )
			{
				Texture mapHeight = new Texture( textureImage );

				mapHeight.setAnisotropy(4);
				mapHeight.getRepeat().set( 0.998, 0.998 );
				mapHeight.getOffset().set( 0.001, 0.001 );
				mapHeight.setWrapS(TextureWrapMode.REPEAT);
				mapHeight.setWrapT(TextureWrapMode.REPEAT);
				mapHeight.setFormat(GLES20.GL_RGB);

				Texture mapSpecular = new Texture( specImage );
				mapSpecular.getRepeat().set( 0.998, 0.998 );
				mapSpecular.getOffset().set( 0.001, 0.001 );
				mapSpecular.setWrapS(TextureWrapMode.REPEAT);
				mapSpecular.setWrapT(TextureWrapMode.REPEAT);
				mapSpecular.setFormat(GLES20.GL_RGB);

				Texture mapColor = new Texture( colImage );
				mapColor.getRepeat().set( 0.998, 0.998 );
				mapColor.getOffset().set( 0.001, 0.001 );
				mapColor.setWrapS(TextureWrapMode.REPEAT);
				mapColor.setWrapT(TextureWrapMode.REPEAT);
				mapColor.setFormat(GLES20.GL_RGB);

				SkinSimpleShader shader = new SkinSimpleShader();

				Map<String, Uniform> uniforms = shader.getUniforms();

				uniforms.get( "enableBump" ).setValue( true );
				uniforms.get( "enableSpecular" ).setValue( true );

				uniforms.get( "tBeckmann" ).setValue( composerBeckmann.getRenderTarget1() );
				uniforms.get( "tDiffuse" ).setValue( mapColor );

				uniforms.get( "bumpMap" ).setValue( mapHeight );
				uniforms.get( "specularMap" ).setValue( mapSpecular );

				((Color)uniforms.get( "ambient" ).getValue()).setHex( 0xa0a0a0 );
				((Color)uniforms.get( "diffuse" ).getValue()).setHex( 0xa0a0a0 );
				((Color)uniforms.get( "specular" ).getValue()).setHex( 0xa0a0a0 );

				uniforms.get( "uRoughness" ).setValue( 0.145 );
				uniforms.get( "uSpecularBrightness" ).setValue( 0.75 );

				uniforms.get( "bumpScale" ).setValue( 16.0 );

				((Vector4)uniforms.get( "offsetRepeat" ).getValue()).set( 0.001, 0.001, 0.998, 0.998 );

				ShaderMaterial material = new ShaderMaterial( shader );
				material.setLights(true);

				mesh = new Mesh( geometry, material );

				mesh.getPosition().setY(- 50 );
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

//         		if ( firstPass )
//         		{
//            		composerBeckmann.render();
//            		firstPass = false;
//         		}

				getRenderer().clear();
				getRenderer().render(getScene(), camera);
			}
		};
    }
}
