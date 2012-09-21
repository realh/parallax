/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.lights;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Vector3;

public final class VirtualLight extends DirectionalLight 
{

	private Camera originalCamera;
	
	private List<Vector3> pointsWorld;
	private List<Vector3> pointsFrustum;
	
	public VirtualLight(int hex) 
	{
		super(hex);
		
		pointsWorld = new ArrayList<Vector3>();
		pointsFrustum = new ArrayList<Vector3>();
		
		for ( int i = 0; i < 8; i ++ ) 
		{
			getPointsWorld().set( i, new Vector3() );
			getPointsFrustum().set( i, new Vector3() );
		}
	}

	public Camera getOriginalCamera() {
		return originalCamera;
	}

	public void setOriginalCamera(Camera originalCamera) {
		this.originalCamera = originalCamera;
	}

	public List<Vector3> getPointsWorld() {
		return pointsWorld;
	}

	public void setPointsWorld(List<Vector3> pointsWorld) {
		this.pointsWorld = pointsWorld;
	}

	public List<Vector3> getPointsFrustum() {
		return pointsFrustum;
	}

	public void setPointsFrustum(List<Vector3> pointsFrustum) {
		this.pointsFrustum = pointsFrustum;
	}

}