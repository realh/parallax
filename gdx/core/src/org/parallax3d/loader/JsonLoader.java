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

package org.parallax3d.loader;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import org.parallax3d.core.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.parallax3d.renderer.TextureWrapMode;
import org.parallax3d.shaders.NormalMapShader;
import org.parallax3d.shaders.Uniform;
import org.parallax3d.textures.CompressedTexture;
import org.parallax3d.textures.Texture;
import org.parallax3d.core.AbstractGeometry;
import org.parallax3d.core.Face3;
import org.parallax3d.core.Geometry;
import org.parallax3d.core.Geometry.MorphColor;
import org.parallax3d.materials.HasAlphaMap;
import org.parallax3d.materials.HasAmbientEmissiveColor;
import org.parallax3d.materials.HasBumpMap;
import org.parallax3d.materials.HasColor;
import org.parallax3d.materials.HasLightMap;
import org.parallax3d.materials.HasMap;
import org.parallax3d.materials.HasNormalMap;
import org.parallax3d.materials.HasSpecularMap;
import org.parallax3d.materials.HasVertexColors;
import org.parallax3d.materials.Material;
import org.parallax3d.materials.Material.COLORS;
import org.parallax3d.materials.MeshBasicMaterial;
import org.parallax3d.materials.MeshLambertMaterial;
import org.parallax3d.materials.MeshPhongMaterial;
import org.parallax3d.materials.ShaderMaterial;
import org.parallax3d.math.Color;
import org.parallax3d.math.Vector2;
import org.parallax3d.math.Vector3;
import org.parallax3d.math.Vector4;

public class JsonLoader
{

	private JsonValue object;
	
	private List<Material> materials;

	private ImageLoader imageLoader;

	private enum JsoBlending
	{
		NormalBlending        { @Override public Material.BLENDING getValue() { return Material.BLENDING.NORMAL; }},
		AdditiveBlending      { @Override public Material.BLENDING getValue() { return Material.BLENDING.ADDITIVE; }},
		SubtractiveBlending   { @Override public Material.BLENDING getValue() { return Material.BLENDING.SUBTRACTIVE; }},
		MultiplyBlending      { @Override public Material.BLENDING getValue() { return Material.BLENDING.MULTIPLY; }},
		AdditiveAlphaBlending { @Override public Material.BLENDING getValue() { return Material.BLENDING.ADDITIVE_ALPHA; }},
		CustomBlending        { @Override public Material.BLENDING getValue() { return Material.BLENDING.CUSTOM; }};

		public abstract Material.BLENDING getValue();
	}

	public JsonLoader(ImageLoader loader)
	{
		this.imageLoader = loader;
	}

	public void setImageLoader(ImageLoader loader)
	{
		this.imageLoader = loader;
	}

	public AbstractGeometry parse(String string)
	{		 
		if(!isThisJsonStringValid(string))
			return null;
		
		Log.debug("JSON parse()");
		
		Geometry geometry = new Geometry();

		try
		{
			parseMaterials();

			parseModel(geometry);

			parseSkin(geometry);
			parseMorphing(geometry);

			geometry.computeFaceNormals();
			geometry.computeBoundingSphere();

			if ( hasNormals() )
				geometry.computeTangents();

			geometry.computeMorphNormals();
		} catch (Throwable e)
		{
			Log.error("Error parsing JSON model", e);
			geometry = null;
		}

		return geometry;
	}

	public List<Material> getMaterials() {
		return this.materials;
	}
	
	public void morphColorsToFaceColors(Geometry geometry) 
	{
		if ( geometry.getMorphColors() != null && geometry.getMorphColors().size() > 0 ) 
		{
			MorphColor colorMap = geometry.getMorphColors().get( 0 );

			for ( int i = 0; i < colorMap.colors.size(); i ++ ) 
			{
				geometry.getFaces().get(i).setColor( colorMap.colors.get(i) );
				geometry.getFaces().get(i).getColor().offsetHSL( 0, 0.3, 0 );
			}
		}
	}
	
	private boolean isThisJsonStringValid(String iJSonString) 
	{
		try
		{
			object = new JsonReader().parse(iJSonString);
		}
		catch ( Throwable e)
		{
			object = null;
			Log.error("Could not parse JSON data", e);
			return false;
		}

		if (object == null)
		{
			Log.error("Could not parse JSON data");
			return false;
		}

		return true;
	}
		
	private void parseMaterials()
	{
		JsonValue materials;

		materials = object.get("materials");

		if (materials == null || !materials.isArray())
			return;

		Log.debug("JSON parseMaterials()");
		
		this.materials = new ArrayList<Material>(); 
		for ( int i = 0; i < materials.size; ++i)
		{
			this.materials.add( createMaterial( materials.get(i) ) );
		}
//		geometry.setMaterials(this.materials);
	}
	
	private Material createMaterial(JsonValue jsonMaterial)
	{
		// defaults
		Material material;

		String shading = jsonMaterial.getString("shading");
		if (shading.compareToIgnoreCase("phong") == 0)
		{
			material = new MeshPhongMaterial();
		}
		else if (shading.compareToIgnoreCase("basic") == 0)
		{
			material = new MeshBasicMaterial();
		}
		else if (jsonMaterial.has("mapNormal"))
		{
			// Special case for normal map
			material = new ShaderMaterial(new NormalMapShader());
			((ShaderMaterial) material).setLights(true);
			((ShaderMaterial) material).setFog(true);
		}
		else
		{
			material = new MeshLambertMaterial();
			material.setOpacity(1.0);
			((MeshLambertMaterial) material).setColor(new Color(0xeeeeee));
			material.setOpacity(1.0);
		}

		// parameters from model file

		String blending = jsonMaterial.getString("blending");
		for (JsoBlending bev: JsoBlending.values())
		{
			if (blending.compareToIgnoreCase(bev.toString()) == 0)
			{
				material.setBlending(bev.getValue());
			}
		}

		material.setTransparent(jsonMaterial.getBoolean("transparent"));
		material.setDepthTest(jsonMaterial.getBoolean("depthTest"));
		material.setDepthWrite(jsonMaterial.getBoolean("epthWrite"));
		
		if(jsonMaterial.getBoolean("vertexColors") && material instanceof HasVertexColors)
		{
			((HasVertexColors) material).setVertexColors(COLORS.VERTEX);
		}
			
		Color color  = getColor(jsonMaterial, "colorDiffuse");

		if (color == null)
		{
			int dbgColor = jsonMaterial.getInt("dbgColor");
			if (dbgColor > 0)
				color = new Color(dbgColor);
		}

		if (color != null)
		{
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "diffuse" ).setValue(color);
			}
			else if(material instanceof HasColor)
			{
				((HasColor) material).setColor(color);
			}
		}

		color = getColor(jsonMaterial, "colorSpecular");
		if (color != null)
		{
			if (material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get("specular").setValue(color);
			} else if (material instanceof MeshPhongMaterial)
			{
				((MeshPhongMaterial) material).setSpecular(color);
			}
		}

		color = getColor(jsonMaterial, "colorAmbient");
		if (color != null)
		{
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "ambient" ).setValue(color);
			}
			else if( material instanceof HasAmbientEmissiveColor)
			{
				((HasAmbientEmissiveColor)material).setAmbient(color);	
			}
		}
		
		color = getColor(jsonMaterial, "colorEmissive");
		if (color != null)
		{
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "emissive" ).setValue(color);
			}
			else if( material instanceof HasAmbientEmissiveColor)
			{
				((HasAmbientEmissiveColor)material).setEmissive(color);	
			}
		}
		
		if(jsonMaterial.getBoolean("transparent"))
		{
			double transparency = jsonMaterial.getDouble("transparency");
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "opacity" ).setValue(transparency);
			}
			else 
			{
				material.setOpacity(transparency);
			}
		}

		double specularCoef = jsonMaterial.getDouble("specularCoef", 0);
		if(specularCoef > 0)
		{
			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "shininess" ).setValue(specularCoef);
			}
			else if(material instanceof MeshPhongMaterial)
			{
				((MeshPhongMaterial)material).setShininess(specularCoef);
			}
		}

		// textures

		String map = jsonMaterial.getString("mapDiffuse", null);
		if (map  != null )
		{
			Texture texture = create_texture(map,
					jsonMaterial.get("mapDiffuseRepeat"),
					jsonMaterial.get("mapDiffuseOffset"),
					jsonMaterial.get("mapDiffuseWrap"),
					jsonMaterial.getInt("mapDiffuseAnisotropy"));

			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "tDiffuse" ).setValue(texture);
				uniforms.get( "enableDiffuse" ).setValue(true);
			}
			else if(material instanceof HasMap)
			{
				((HasMap)material).setMap(texture);
			}
		}

		map = jsonMaterial.getString("mapLight", null);
		if (map  != null )
		{
			Texture texture = create_texture(map,
					jsonMaterial.get("mapLightRepeat"),
					jsonMaterial.get("mapLightOffset"),
					jsonMaterial.get("mapLightWrap"),
					jsonMaterial.getInt("mapLightAnisotropy"));

			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "tAO" ).setValue(texture);
				uniforms.get( "enableAO" ).setValue(true);
			}
			else if(material instanceof HasLightMap)
			{
				((HasLightMap)material).setLightMap(texture);
			}
		}

		map = jsonMaterial.getString("mapBump", null);
		if (map  != null  && material instanceof HasBumpMap)
		{
			Texture texture = create_texture(map,
					jsonMaterial.get("mapBumpRepeat"),
					jsonMaterial.get("mapBumpOffset"),
					jsonMaterial.get("mapBumpWrap"),
					jsonMaterial.getInt("mapBumpAnisotropy"));

			((HasBumpMap)material).setBumpMap(texture);

			double scale = jsonMaterial.getDouble("mapBumpScale", 0);
			if (scale  > 0)
			{
				((HasBumpMap)material).setBumpScale(scale);
			}
		}

		map = jsonMaterial.getString("mapNormal", null);
		if (map  != null  && material instanceof HasNormalMap)
		{
			Map<String, Uniform> uniforms = material.getShader().getUniforms();

			Texture texture = create_texture(map,
					jsonMaterial.get("mapNormalRepeat"),
					jsonMaterial.get("mapNormalOffset"),
					jsonMaterial.get("mapNormalWrap"),
					jsonMaterial.getInt("mapNormalAnisotropy"));

			uniforms.get( "tNormal" ).setValue( texture );

			double factor = jsonMaterial.getDouble("mapNormalFactor", 0);
			if ( factor > 0 )
			{
				((Vector2)uniforms.get( "uNormalScale" ).getValue()).set( factor, factor );
			}
		}

		map = jsonMaterial.getString("mapSpecular", null);
		if (map  != null )
		{
			Texture texture = create_texture(map,
					jsonMaterial.get("mapSpecularRepeat"),
					jsonMaterial.get("mapSpecularOffset"),
					jsonMaterial.get("mapSpecularWrap"),
					jsonMaterial.getInt("mapSpecularAnisotropy"));

			if(material instanceof ShaderMaterial)
			{
				Map<String, Uniform> uniforms = material.getShader().getUniforms();
				uniforms.get( "tSpecular" ).setValue(texture);
				uniforms.get( "enableSpecular" ).setValue(true);
			}
			else if(material instanceof HasSpecularMap)
			{
				((HasSpecularMap)material).setSpecularMap(texture);
			}
		}

		map = jsonMaterial.getString("mapAlpha", null);
		if (map != null && material instanceof HasAlphaMap)
		{
			Texture texture = create_texture(map,
					jsonMaterial.get("mapAlphaRepeat"),
					jsonMaterial.get("mapAlphaOffset"),
					jsonMaterial.get("mapAlphaWrap"),
					jsonMaterial.getInt("mapAlphaAnisotropy"));
			((HasAlphaMap)material).setAlphaMap(texture);

		}

		String name = jsonMaterial.getString("dbgName", null);
		if(name != null)
		{
			material.setName(name);
		}

		return material;
	}
	
	private void parseModel(Geometry geometry) throws JSONException
	{
		List<Integer> faces = getArrayAsList("faces");

		if (faces == null)
			return;

		Log.debug("JSON parseFaces()");

		double scale = object.getDouble("scale", 0);
		scale = scale > 0 ? 1 / scale : 1;

		List<Double> vertices = getArrayAsList("vertices");
		List<List<Double>> uvs = getNestedArrayAsLists("uvs");
		List<Double> normals = getArrayAsList("normals");
		List<Integer> colors = getArrayAsList("colors");
	
		int nUvLayers = 0;

		if(uvs != null)
		{
			// disregard empty arrays
			for ( int i = 0; i < uvs.size(); i++ )
			{
				if ( uvs.get( i ).size() > 0) 
					nUvLayers ++;
			}
	
			// 0-index is initialized already
			for ( int i = 0; i < nUvLayers; i++ ) 
			{
				geometry.getFaceVertexUvs().add( i, new ArrayList<List<Vector2>>());
			}
		}
		

		int offset = 0;
		int zLength = vertices.size();
		
		while ( offset < zLength ) {

			Vector3 vertex = new Vector3();

			vertex.setX( vertices.get( offset ++ ) * scale );
			vertex.setY( vertices.get( offset ++ ) * scale );
			vertex.setZ( vertices.get( offset ++ ) * scale );

			geometry.getVertices().add( vertex );

		}

		offset = 0;
		zLength = faces.size();

		while ( offset < zLength ) 
		{
			int type = faces.get(offset++);

			boolean isQuad          	= isBitSet( type, 0 );
			boolean hasMaterial         = isBitSet( type, 1 );
			boolean hasFaceVertexUv     = isBitSet( type, 3 );
			boolean hasFaceNormal       = isBitSet( type, 4 );
			boolean hasFaceVertexNormal = isBitSet( type, 5 );
			boolean hasFaceColor	    = isBitSet( type, 6 );
			boolean hasFaceVertexColor  = isBitSet( type, 7 );
			
			if( isQuad )
			{
				Face3 faceA = new Face3(faces.get( offset ), faces.get( offset + 1 ), faces.get( offset + 3 ));

				Face3 faceB = new Face3(faces.get( offset + 1 ), faces.get( offset + 2 ), faces.get( offset + 3 ));

				offset += 4;
				
				if ( hasMaterial ) {

					int materialIndex = faces.get( offset ++ );
					faceA.setMaterialIndex( materialIndex );
					faceB.setMaterialIndex( materialIndex );

				}
				
				// to get face <=> uv index correspondence

				int fi = geometry.getFaces().size();

				if ( hasFaceVertexUv ) {

					for ( int i = 0; i < nUvLayers; i ++ ) {

						List<Double> uvLayer = uvs.get( i );

						geometry.getFaceVertexUvs().get( i ).add( fi, new ArrayList<Vector2>() );
						geometry.getFaceVertexUvs().get( i ).add( fi + 1, new ArrayList<Vector2>() );

						for ( int j = 0; j < 4; j ++ ) {

							Integer uvIndex = faces.get( offset ++ );

							Double u = uvLayer.get( uvIndex * 2 );
							Double v = uvLayer.get( uvIndex * 2 + 1 );

							Vector2 uv = new Vector2( u, v );

							if ( j != 2 ) geometry.getFaceVertexUvs().get( i ).get( fi ).add( uv );
							if ( j != 0 ) geometry.getFaceVertexUvs().get( i ).get( fi + 1 ).add( uv );

						}

					}

				}
				
				if ( hasFaceNormal ) {

					Integer normalIndex = faces.get( offset ++ ) * 3;

					faceA.getNormal().set(
						normals.get( normalIndex ++ ),
						normals.get( normalIndex ++ ),
						normals.get( normalIndex )
					);

					faceB.getNormal().copy( faceA.getNormal() );

				}

				if ( hasFaceVertexNormal ) {

					for ( int i = 0; i < 4; i ++ ) {

						Integer normalIndex = faces.get( offset ++ ) * 3;

						Vector3 normal = new Vector3(
							normals.get( normalIndex ++ ),
							normals.get( normalIndex ++ ),
							normals.get( normalIndex )
						);


						if ( i != 2 ) faceA.getVertexNormals().add( normal );
						if ( i != 0 ) faceB.getVertexNormals().add( normal );

					}

				}
				
				if ( hasFaceColor ) {

					Integer colorIndex = faces.get( offset ++ );
					Integer hex = colors.get( colorIndex );

					faceA.getColor().setHex( hex );
					faceB.getColor().setHex( hex );

				}
				
				if ( hasFaceVertexColor ) {

					for ( int i = 0; i < 4; i ++ ) {

						Integer colorIndex = faces.get( offset ++ );
						Integer hex = colors.get( colorIndex );

						if ( i != 2 ) faceA.getVertexColors().add( new Color( hex ) );
						if ( i != 0 ) faceB.getVertexColors().add( new Color( hex ) );

					}

				}

				geometry.getFaces().add( faceA );
				geometry.getFaces().add( faceB );

			}
			else
			{
			
				Face3 face = new Face3( faces.get( offset ++ ), faces.get( offset ++ ), faces.get( offset ++ ));

				if ( hasMaterial ) {

					int materialIndex = faces.get( offset ++ );
					face.setMaterialIndex( materialIndex );

				}
				
				// to get face <=> uv index correspondence

				if ( hasFaceVertexUv ) {

					for ( int i = 0; i < nUvLayers; i ++ ) {

						List<Double> uvLayer = uvs.get( i );

						ArrayList<Vector2> getFaceVertexUvs = new ArrayList<Vector2>();
						int fi = geometry.getFaces().size();

						for ( int j = 0; j < 3; j ++ ) {

							Integer uvIndex = faces.get( offset ++ );

							double u = uvLayer.get( uvIndex * 2 );
							double v = uvLayer.get( uvIndex * 2 + 1 );

							Vector2 uv = new Vector2( u, v );

							getFaceVertexUvs.add( uv );

						}
						
						geometry.getFaceVertexUvs().get( i ).add(getFaceVertexUvs);

					}

				}
				
				if ( hasFaceNormal ) {

					Integer normalIndex = faces.get( offset ++ ) * 3;

					face.getNormal().set(
						normals.get( normalIndex ++ ),
						normals.get( normalIndex ++ ),
						normals.get( normalIndex )
					);

				}

				if ( hasFaceVertexNormal ) {

					for ( int i = 0; i < 3; i ++ ) {

						Integer normalIndex = faces.get( offset ++ ) * 3;

						Vector3 normal = new Vector3(
							normals.get( normalIndex ++ ),
							normals.get( normalIndex ++ ),
							normals.get( normalIndex )
						);

						face.getVertexNormals().add( normal );

					}

				}


				if ( hasFaceColor ) {

					Integer colorIndex = faces.get( offset ++ );
					face.getColor().setHex( colors.get( colorIndex ) );

				}
				
				if ( hasFaceVertexColor ) {

					for ( int i = 0; i < 3; i ++ ) {

						Integer colorIndex = faces.get( offset ++ );
						face.getVertexColors().add( new Color( colors.get( colorIndex ) ) );

					}

				}

				geometry.getFaces().add( face );

			}
		}
	}

	private <T> List<T> getArrayAsList(String name) throws JSONException
	{
		JsonValue jsonArray = object.get(name);

		return (jsonArray != null) ? (List<T>) jsonArrayToList(jsonArray) : null;
	}

	private <T> List<List<T>> getNestedArrayAsLists(String name) throws JSONException
	{
		JsonValue jsonArray = object.get(name);

		if (jsonArray == null)
			return null;

		List<JsonValue> outerList = getArrayAsList(name);
		int len = outerList.size();
		List<List<T>> nestedList = new ArrayList<List<T>>(len);

		for (int i = 0; i < len; ++i)
		{
			nestedList.add((List<T>) jsonArrayToList(outerList.get(i)));
		}

		return nestedList;
	}

	private static <T> List<T> jsonArrayToList(JsonValue jsonArray) throws JSONException
	{
		int len = jsonArray.length();
		List<T> list = new ArrayList<T>(len);

		for (int i = 0; i < len; ++i)
		{
			list.add((T) jsonArray.get(i));
		}

		return list;
	}
	
	private void parseSkin(Geometry geometry) throws JSONException
	{
		int influencesPerVertex = object.getInt("influencesPerVertex");
		if (influencesPerVertex <= 0)
			influencesPerVertex = 2;

		Log.debug("JSON parseSkin()");

		List<Double> skinWeights = getArrayAsList("skinWeights");
		if ( skinWeights != null )
		{
			for ( int i = 0, l = skinWeights.size(); i < l; i += influencesPerVertex )
			{
				double x =                               skinWeights.get( i     );
				double y = ( influencesPerVertex > 1 ) ? skinWeights.get( i + 1 ) : 0;
				double z = ( influencesPerVertex > 2 ) ? skinWeights.get( i + 2 ) : 0;
				double w = ( influencesPerVertex > 3 ) ? skinWeights.get( i + 3 ) : 0;
			
				geometry.getSkinWeights().add( new Vector4( x, y, z, w ) );
			}

		}

		List<Integer> skinIndices = getArrayAsList("skinIndices");
		if ( skinIndices != null)
		{

			for ( int i = 0, l = skinIndices.size(); i < l; i += 2 ) 
			{
				double a =                               skinIndices.get( i     );
				double b = ( influencesPerVertex > 1 ) ? skinIndices.get( i + 1 ) : 0;
				double c = ( influencesPerVertex > 2 ) ? skinIndices.get( i + 2 ) : 0;
				double d = ( influencesPerVertex > 3 ) ? skinIndices.get( i + 3 ) : 0;
			
				geometry.getSkinIndices().add( new Vector4( a, b, c, d ) );
			}
		}

//	    geometry.bones = json.bones;
//		geometry.animation = json.animation;
	}

	private void parseMorphing(Geometry geometry) throws JSONException
	{
		Log.debug("JSON parseMorphing()");

		double scale = object.getDouble("scale", 0);
		scale = scale > 0 ? 1 / scale : 1;

		JsonValue morphTargets = object.get("morphTargets");
		if ( morphTargets != null)
		{
			for ( int i = 0, l = morphTargets.length(); i < l; i ++ )
			{
				JSONObject jsonTarget = morphTargets.getJSONObject(i);

				Geometry.MorphTarget morphTarget = geometry.new MorphTarget();
				morphTarget.name = jsonTarget.getString("name");
				morphTarget.vertices = new ArrayList<Vector3>();

				JsonValue srcVertices = jsonTarget.get("vertices");
				for( int v = 0, vl = srcVertices.length(); v < vl; v += 3 )
				{
					morphTarget.vertices.add( 
							new Vector3(
									srcVertices.getDouble(v) * scale,
									srcVertices.getDouble(v + 1) * scale,
									srcVertices.getDouble(v + 2) * scale));
				}

				geometry.getMorphTargets().add(morphTarget);
			}
		}

		JsonValue morphColors = object.get("morphColors");
		if ( morphColors != null )
		{
			for ( int i = 0, l = morphColors.length(); i < l; i++ )
			{
				JSONObject jsonColor = morphColors.getJSONObject(i);
				Geometry.MorphColor morphColor = geometry.new MorphColor();
				morphColor.name = jsonColor.getString("name");
				morphColor.colors = new ArrayList<Color>();

				JsonValue srcColors = jsonColor.get("colors");
				for ( int c = 0, cl = srcColors.length(); c < cl; c += 3 )
				{
					Color color = new Color( 0xffaa00 );
					color.setRGB(srcColors.getDouble(c), srcColors.getDouble(c + 1), srcColors.getDouble(c + 2));
					morphColor.colors.add(color);
				}
				
				geometry.getMorphColors().add(morphColor);
			}
		}
	}
	
	private boolean isBitSet( int value, int position ) 
	{
		return (value & ( 1 << position )) > 0;
	}

	private Texture create_texture( String sourceFile, JsonValue repeat, JsonValue offset, JsonValue wrap, int anisotropy )
			throws IOException
	{
		boolean isCompressed = sourceFile.toLowerCase().endsWith(".dds");

		final Texture texture;
		
		if ( isCompressed ) 
		{
			byte[] data = imageLoader.loadData(sourceFile);
			ByteBuffer bbuf = ByteBuffer.allocate(data.length);
			bbuf.put(data);
			bbuf.flip();

			texture = new CompressedTexture(bbuf, true);
		} 
		else 
		{
			texture = new Texture(imageLoader.loadImage(sourceFile));
			texture.setNeedsUpdate(false);
		}

		if( repeat != null) 
		{
			int x = repeat.getInt(0);
			int y = repeat.getInt(1);
			texture.getRepeat().set(x, y);

			if ( x != 1 )
				texture.setWrapS(TextureWrapMode.REPEAT);
			if ( y != 1 )
				texture.setWrapT(TextureWrapMode.REPEAT);
		}

		if ( offset != null) 
		{
			texture.getOffset().set(offset.getInt(0), offset.getInt(1));
		}

		if ( wrap != null) 
		{
			String w = wrap.getString(0);
			if (w != null && w.compareToIgnoreCase("repeat") == 0)
				texture.setWrapS(TextureWrapMode.REPEAT);
			else if (w != null && w.compareToIgnoreCase("mirror") == 0)
				texture.setWrapS(TextureWrapMode.MIRRORED_REPEAT);
			w = wrap.getString(1);
			if (w != null && w.compareToIgnoreCase("repeat") == 0)
				texture.setWrapT(TextureWrapMode.REPEAT);
			else if (w != null && w.compareToIgnoreCase("mirror") == 0)
				texture.setWrapT(TextureWrapMode.MIRRORED_REPEAT);
		}

		if ( anisotropy > 0) 
		{
			texture.setAnisotropy(anisotropy);
		}
		return texture;
	}

	private Color getColor(JsonValue rgb )
	{
		if (!rgb.isArray() || rgb.size < 3)
			return null;
		return new Color(
				  ((int)(rgb.getDouble(0) * 255) << 16 )
				+ ((int)(rgb.getDouble(1) * 255) << 8 )
				+  (int)(rgb.getDouble(2) * 255));
	}

	private Color getColor(JsonValue jsonMaterial, String name )
	{
		JsonValue color = jsonMaterial.get(name);
		return (color != null) ? getColor(color) : null;
	}

	private boolean hasNormals()
	{
		for( int i = 0; i < this.materials.size(); i ++ ) 
			if (  this.materials.get(i) instanceof ShaderMaterial ) 
				return true;

		return false;
	}	
}

