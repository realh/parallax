/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 * 
 * This file is part of the realh fork of the Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */


package uk.co.realh.parallax3d;

import android.app.Activity;
import android.util.Log;

import thothbot.parallax.core.client.gl2.Image;
import thothbot.parallax.core.client.gl2.enums.TextureWrapMode;
import thothbot.parallax.core.client.renderers.Duration;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.geometries.PlaneGeometry;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.FogExp2;

public class MovingVerticesDemo extends DemoAnimatedScene
{
    private static final String imageAsset = "textures/water.jpg";
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
        try {
            waterImage = ImageAssetLoader.createImageFromAsset(
                    activity.getAssets(), imageAsset);
        } catch (Throwable e) {
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

        this.geometry = new PlaneGeometry( 20000, 20000, worldWidth - 1, worldDepth - 1 );
        this.geometry.applyMatrix(new Matrix4().makeRotationX(-Math.PI / 2.0));

        for ( int i = 0, il = this.geometry.getVertices().size(); i < il; i ++ )
        {
            this.geometry.getVertices().get(i).setY(35.0 * Math.sin(i / 2.0));
        }

        this.geometry.computeFaceNormals();
        this.geometry.computeVertexNormals();

        Texture texture = new Texture(waterImage);
        texture.setWrapS(TextureWrapMode.REPEAT);
        texture.setWrapT(TextureWrapMode.REPEAT);
        texture.getRepeat().set( 5.0, 5.0 );

        MeshBasicMaterial material = new MeshBasicMaterial();
        material.setColor( new Color(0x0044ff) );
        material.setMap( texture );

        this.mesh = new Mesh( this.geometry, material );
        getScene().add( this.mesh );

        this.oldTime = Duration.currentTimeMillis();
    }

    @Override
    protected void onUpdate(double duration)
    {
        for ( int i = 0, l = this.geometry.getVertices().size(); i < l; i ++ )
            this.geometry.getVertices().get( i ).setY(35.0 * Math.sin( i / 5.0 + ( duration * 0.01 + i ) / 7.0 ));

        this.mesh.getGeometry().setVerticesNeedUpdate(true);

        this.oldTime = Duration.currentTimeMillis();

        getRenderer().render(getScene(), camera);
    }
}
