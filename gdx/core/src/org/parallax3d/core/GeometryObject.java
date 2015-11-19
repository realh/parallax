/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
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

package org.parallax3d.core;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.parallax3d.renderer.Float32Array;
import org.parallax3d.renderer.WebGLGeometry;
import org.parallax3d.renderer.WebGLRenderer;
import org.parallax3d.shaders.Attribute;
import org.parallax3d.materials.Material;
import org.parallax3d.objects.Line;
import org.parallax3d.objects.Mesh;
import org.parallax3d.objects.PointCloud;

public abstract class GeometryObject extends Object3D
{
	protected AbstractGeometry geometry;
	protected Material material;
	
	private float _oldLineWidth = -1;

	private int[] tmpBufArray = {0};
	
	public GeometryObject(AbstractGeometry geometry, Material material) {
		this.geometry = geometry;
		this.material = material;
	}

	public AbstractGeometry getGeometry()
	{
		return this.geometry;
	}

	public void setGeometry(AbstractGeometry geometry)
	{
		this.geometry = geometry;
	}

	public Material getMaterial()
	{
		return this.material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	/**
	 * Abstract method to get intersections between a casted ray and this object. 
	 * Subclasses such as {@link Mesh}, {@link Line}, and {@link PointCloud} implement this method in order to participate in raycasting.
	 * @param raycaster
	 * @param intersects
	 */
	public abstract void raycast( Raycaster raycaster, List<Raycaster.Intersect> intersects);
	
	public abstract void renderBuffer(WebGLRenderer renderer, WebGLGeometry geometryBuffer, boolean updateBuffers);
	
//	/**
//	 * object � an instance of Object3D
//	 * Removes an object from the GL context and releases all the data (geometry, matrices...) 
//	 * that the GL context keeps about the object, but it doesn't release textures or affect any 
//	 * JavaScript data.
//	 */
//	public void deallocate(WebGLRenderer renderer) 
//	{
//		if ( ! isWebglInit ) return;
//
//		isWebglInit = false;
//
//		_modelViewMatrix = null;
//		_normalMatrix = null;
//
//		_normalMatrixArray = null;
//		_modelViewMatrixArray = null;
//		_modelMatrixArray = null;
//
//		deleteBuffers(renderer);
//	}
	
	public void deleteBuffers(WebGLRenderer renderer) 
	{
		tmpBufArray[0] = geometry.__webglVertexBuffer;
		GLES20.glDeleteBuffers(1, tmpBufArray, 0);
		tmpBufArray[0] = geometry.__webglColorBuffer ;
		GLES20.glDeleteBuffers(1, tmpBufArray, 0);

		renderer.getInfo().getMemory().geometries --;
	}

	public void setLineWidth ( float width )
	{
		if ( width != this._oldLineWidth ) 
		{
			GLES20.glLineWidth(width);
			this._oldLineWidth = width;
		}
	}
		
	protected void initCustomAttributes (Geometry geometry )
	{		
		int nvertices = geometry.getVertices().size();
		Material material = this.getMaterial();

		Map<String, Attribute> attributes = material.getShader().getAttributes();
		
		if ( attributes != null) 
		{
			if ( geometry.__webglCustomAttributesList == null ) 
				geometry.__webglCustomAttributesList = new ArrayList<Attribute>();

			for ( String a : attributes.keySet() ) 
			{
				Attribute attribute = attributes.get( a );
				if( ! attribute.__webglInitialized || attribute.createUniqueBuffers ) 
				{
					attribute.__webglInitialized = true;

					int size = 1;		// "f" and "i"

					if ( attribute.type == Attribute.TYPE.V2 ) size = 2;
					else if ( attribute.type == Attribute.TYPE.V3 ) size = 3;
					else if ( attribute.type == Attribute.TYPE.V4 ) size = 4;
					else if ( attribute.type == Attribute.TYPE.C  ) size = 3;

					attribute.size = size;

					attribute.array = Float32Array.create( nvertices * size );

					GLES20.glGenBuffers(1, tmpBufArray, 0);
					attribute.buffer = tmpBufArray[0];
					attribute.belongsToAttribute = a;

					attribute.needsUpdate = true;
				}

				geometry.__webglCustomAttributesList.add( attribute );
			}
		}
	}
}
