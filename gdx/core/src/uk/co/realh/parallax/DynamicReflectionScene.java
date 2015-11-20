/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 *
 * This file is part of the Android port of the Parallax project.
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

package uk.co.realh.parallax;


import com.badlogic.gdx.graphics.GL20;

import org.parallax3d.cameras.CubeCamera;
import org.parallax3d.cameras.PerspectiveCamera;
import org.parallax3d.core.Log;
import org.parallax3d.gdx.GdxAssetLoader;
import org.parallax3d.geometries.BoxGeometry;
import org.parallax3d.geometries.SphereGeometry;
import org.parallax3d.geometries.TorusKnotGeometry;
import org.parallax3d.materials.MeshBasicMaterial;
import org.parallax3d.math.Mathematics;
import org.parallax3d.objects.Mesh;
import org.parallax3d.textures.Texture;

import java.io.IOException;

class DynamicReflectionScene extends DemoAnimatedScene
{
	private static final String texture = "./static/textures/ruins.jpg";

	PerspectiveCamera camera;

	public int onMouseDownMouseX = 0;
	public int onMouseDownMouseY = 0;

	public boolean onMouseDown = false;

	public double fov = 70;

	public double lat = 0;
	public double lon = 0;
	public double phi = 0;
	public double theta = 0;

	private Mesh sphere;
	private Mesh cube;
	private Mesh torus;

	private CubeCamera cubeCamera;

	@Override
	public void onStart()
	{
		camera = new PerspectiveCamera(
				this.fov, // fov
				getRenderer().getAbsoluteAspectRation(), // aspect
				1, // near
				1000 // far
		);

		Texture texture = null;
		try
		{
			texture = new Texture(new GdxAssetLoader("textures/").loadImage("ruins.jpg"));
			MeshBasicMaterial mbOpt = new MeshBasicMaterial();
			mbOpt.setMap( texture );

			Mesh mesh = new Mesh( new SphereGeometry( 500, 60, 40 ), mbOpt );
			mesh.getScale().setX( -1 );
			getScene().add( mesh );

			this.cubeCamera = new CubeCamera( 1, 1000, 256 );
			this.cubeCamera.getRenderTarget().setMinFilter( GL20.GL_LINEAR_MIPMAP_LINEAR );
			getScene().add( cubeCamera );

			MeshBasicMaterial material = new MeshBasicMaterial();
			material.setEnvMap( cubeCamera.getRenderTarget() );

			sphere = new Mesh( new SphereGeometry( 20, 30, 15 ), material );
			getScene().add( sphere );

			cube = new Mesh( new BoxGeometry( 20, 20, 20 ), material );
			getScene().add( cube );

			torus = new Mesh( new TorusKnotGeometry( 20, 5, 100, 25 ), material );
			getScene().add( torus );

			getRenderer().render( getScene(), camera );
		} catch (IOException e)
		{
			Log.error("Failed to load texture", e);
		}
	}

	@Override
	public void onUpdate(double duration)
	{
		this.lon += .15;

		this.lat = Math.max( - 85.0, Math.min( 85.0, this.lat ) );
		this.phi = Mathematics.degToRad( 90 - lat ) ;
		this.theta = Mathematics.degToRad(this.lon);

		this.sphere.getPosition().setX(Math.sin( duration * 0.001 ) * 30.0 );
		this.sphere.getPosition().setY(Math.sin( duration * 0.0011 ) * 30.0 );
		this.sphere.getPosition().setZ(Math.sin( duration * 0.0012 ) * 30.0 );

		this.sphere.getRotation().addX( 0.02 );
		this.sphere.getRotation().addY( 0.03 );

		this.cube.getPosition().setX(Math.sin( duration * 0.001 + 2.0 ) * 30.0 );
		this.cube.getPosition().setY(Math.sin( duration * 0.0011 + 2.0 ) * 30.0 );
		this.cube.getPosition().setZ(Math.sin( duration * 0.0012 + 2.0 ) * 30.0 );

		this.cube.getRotation().addX( 0.02 );
		this.cube.getRotation().addY( 0.03 );

		this.torus.getPosition().setX(Math.sin( duration * 0.001 + 4.0 ) * 30.0 );
		this.torus.getPosition().setY(Math.sin( duration * 0.0011 + 4.0 ) * 30.0 );
		this.torus.getPosition().setZ(Math.sin( duration * 0.0012 + 4.0 ) * 30.0 );

		this.torus.getRotation().addX( 0.02 );
		this.torus.getRotation().addY( 0.03 );

		camera.getPosition().setX(100.0 * Math.sin( phi ) * Math.cos( theta ) );
		camera.getPosition().setY(100.0 * Math.cos( phi ) );
		camera.getPosition().setZ(100.0 * Math.sin( phi ) * Math.sin( theta ) );

		camera.lookAt( getScene().getPosition() );

		this.sphere.setVisible(false); // *cough*

		cubeCamera.updateCubeMap( getRenderer(), getScene() );

		this.sphere.setVisible(true); // *cough*

		getRenderer().render(getScene(), camera);
	}
}

