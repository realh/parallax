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

package thothbot.parallax.loader.shared;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.enums.TextureWrapMode;
import thothbot.parallax.core.client.shaders.NormalMapShader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.client.textures.CompressedTexture;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Geometry.MorphColor;
import thothbot.parallax.core.shared.materials.HasAlphaMap;
import thothbot.parallax.core.shared.materials.HasAmbientEmissiveColor;
import thothbot.parallax.core.shared.materials.HasBumpMap;
import thothbot.parallax.core.shared.materials.HasColor;
import thothbot.parallax.core.shared.materials.HasLightMap;
import thothbot.parallax.core.shared.materials.HasMap;
import thothbot.parallax.core.shared.materials.HasNormalMap;
import thothbot.parallax.core.shared.materials.HasSpecularMap;
import thothbot.parallax.core.shared.materials.HasVertexColors;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.Material.COLORS;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Mathematics;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.math.Vector4;

public class JsonLoader
{
	private static final String TAG = "Parallax";

	private JSONObject object;
	
	private List<Material> materials;

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

	protected AbstractGeometry parse(String string)
	{		 
		if(!isThisJsonStringValid(string))
			return null;
		
		Log.d(TAG, "JSON parse()");
		
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
		} catch (JSONException e)
		{
			Log.e(TAG, "Error parsing JSON model", e);
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
			object = new JSONObject(iJSonString);
		}
		catch ( JSONException e)
		{
			Log.e(TAG, "Could not parser JSON data");
			return false;
		}  

		return true;
	}
		
	private void parseMaterials()
	{
		JSONArray materials;

		try
		{
			materials = object.getJSONArray("materials");
		}
		catch (JSONException e)
		{
			Log.e(TAG, "Unable to read materials from JSON", e);
			return;
		}

		Log.d(TAG, "JSON parseMaterials()");
		
		this.materials = new ArrayList<Material>(); 
		for ( int i = 0; i < materials.length(); ++i)
		{
			try
			{
				this.materials.add( createMaterial( materials.getJSONObject(i) ) );
			}
			catch (JSONException e)
			{
				Log.e(TAG, "Unabe to read material from JSON");
			}
		}
//		geometry.setMaterials(this.materials);
	}
	
	private Material createMaterial(JSONObject jsonMaterial) throws JSONException
	{
		// defaults
		Material material;

		String shading = jsonMaterial.optString("shading");
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

		String blending = jsonMaterial.optString("blending");
		for (JsoBlending bev: JsoBlending.values())
		{
			if (blending.compareToIgnoreCase(bev.toString()) == 0)
			{
				material.setBlending(bev.getValue());
			}
		}

		material.setTransparent(jsonMaterial.optBoolean("transparent"));
		material.setDepthTest(jsonMaterial.optBoolean("depthTest"));
		material.setDepthWrite(jsonMaterial.optBoolean("epthWrite"));
		
		if(jsonMaterial.optBoolean("vertexColors") && material instanceof HasVertexColors)
		{
			((HasVertexColors) material).setVertexColors(COLORS.VERTEX);
		}
			
		Color color  = getColor(jsonMaterial, "colorDiffuse");

		if (color == null)
		{
			int dbgColor = jsonMaterial.optInt("dbgColor");
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
		
		if(jsonMaterial.optBoolean("transparent"))
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

		double specularCoef = jsonMaterial.optDouble("specularCoef", 0);
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

		String map = jsonMaterial.optString("mapDiffuse", null);
		if (map  != null )
		{
			Texture texture = create_texture(map,
					jsonMaterial.optJSONArray("mapDiffuseRepeat"),
					jsonMaterial.optJSONArray("mapDiffuseOffset"),
					jsonMaterial.optJSONArray("mapDiffuseWrap"),
					jsonMaterial.optInt("mapDiffuseAnisotropy"));

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

		map = jsonMaterial.optString("mapLight", null);
		if (map  != null )
		{
			Texture texture = create_texture(map,
					jsonMaterial.optJSONArray("mapLightRepeat"),
					jsonMaterial.optJSONArray("mapLightOffset"),
					jsonMaterial.optJSONArray("mapLightWrap"),
					jsonMaterial.optInt("mapLightAnisotropy"));

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

		map = jsonMaterial.optString("mapBump", null);
		if (map  != null  && material instanceof HasBumpMap)
		{
			Texture texture = create_texture(map,
					jsonMaterial.optJSONArray("mapBumpRepeat"),
					jsonMaterial.optJSONArray("mapBumpOffset"),
					jsonMaterial.optJSONArray("mapBumpWrap"),
					jsonMaterial.optInt("mapBumpAnisotropy"));

			((HasBumpMap)material).setBumpMap(texture);

			double scale = jsonMaterial.optDouble("mapBumpScale", 0);
			if (scale  > 0)
			{
				((HasBumpMap)material).setBumpScale(scale);
			}
		}

		map = jsonMaterial.optString("mapNormal", null);
		if (map  != null  && material instanceof HasNormalMap)
		{
			Map<String, Uniform> uniforms = material.getShader().getUniforms();

			Texture texture = create_texture(map,
					jsonMaterial.optJSONArray("mapNormalRepeat"),
					jsonMaterial.optJSONArray("mapNormalOffset"),
					jsonMaterial.optJSONArray("mapNormalWrap"),
					jsonMaterial.optInt("mapNormalAnisotropy"));

			uniforms.get( "tNormal" ).setValue( texture );

			double factor = jsonMaterial.optDouble("mapNormalFactor", 0);
			if ( factor > 0 )
			{
				((Vector2)uniforms.get( "uNormalScale" ).getValue()).set( factor, factor );
			}
		}

		map = jsonMaterial.optString("mapSpecular", null);
		if (map  != null )
		{
			Texture texture = create_texture(map,
					jsonMaterial.optJSONArray("mapSpecularRepeat"),
					jsonMaterial.optJSONArray("mapSpecularOffset"),
					jsonMaterial.optJSONArray("mapSpecularWrap"),
					jsonMaterial.optInt("mapSpecularAnisotropy"));

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

		map = jsonMaterial.optString("mapAlpha", null);
		if (map != null && material instanceof HasAlphaMap)
		{
			Texture texture = create_texture(map,
					jsonMaterial.optJSONArray("mapAlphaRepeat"),
					jsonMaterial.optJSONArray("mapAlphaOffset"),
					jsonMaterial.optJSONArray("mapAlphaWrap"),
					jsonMaterial.optInt("mapAlphaAnisotropy"));
			((HasAlphaMap)material).setAlphaMap(texture);

		}

		String name = jsonMaterial.optString("dbgName", null);
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

		Log.d(TAG, "JSON parseFaces()");

		double scale = object.optDouble("scale");
		if (scale > 0)
			scale = 1;

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
		JSONArray jsonArray = object.optJSONArray(name);

		return (jsonArray != null) ? (List<T>) jsonArrayToList(jsonArray) : null;
	}

	private <T> List<List<T>> getNestedArrayAsLists(String name) throws JSONException
	{
		JSONArray jsonArray = object.optJSONArray(name);

		if (jsonArray == null)
			return null;

		List<JSONArray> outerList = getArrayAsList(name);
		int len = outerList.size();
		List<List<T>> nestedList = new ArrayList<List<T>>(len);

		for (int i = 0; i < len; ++i)
		{
			nestedList.set(i, (List<T>) jsonArrayToList(outerList.get(i)));
		}

		return nestedList;
	}

	private static <T> List<T> jsonArrayToList(JSONArray jsonArray) throws JSONException
	{
		int len = jsonArray.length();
		List<T> list = new ArrayList<T>(len);

		for (int i = 0; i < len; ++i)
		{
			list.set(i, (T) jsonArray.get(i));
		}

		return list;
	}
	
	private void parseSkin(Geometry geometry) 
	{
		int influencesPerVertex = ( object.getInfluencesPerVertex() > 0 ) ? object.getInfluencesPerVertex() : 2;
		
		Log.d(TAG, "JSON parseSkin()");
		
		if ( object.getSkinWeights() != null ) 
		{
			List<Double> skinWeights = object.getSkinWeights();
			for ( int i = 0, l = skinWeights.size(); i < l; i += influencesPerVertex ) 
			{
				double x =                               skinWeights.get( i     );
				double y = ( influencesPerVertex > 1 ) ? skinWeights.get( i + 1 ) : 0;
				double z = ( influencesPerVertex > 2 ) ? skinWeights.get( i + 2 ) : 0;
				double w = ( influencesPerVertex > 3 ) ? skinWeights.get( i + 3 ) : 0;
			
				geometry.getSkinWeights().add( new Vector4( x, y, z, w ) );
			}

		}

		if ( object.getSkinIndices() != null) 
		{
			List<Integer> skinIndices = object.getSkinIndices();

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

	private void parseMorphing(Geometry geometry) 
	{
		Log.d(TAG, "JSON parseMorphing()");
		
		double scale = object.getScale() > 0 ? 1.0 / object.getScale() : 1.0;
				
		if ( object.getMorphTargets() != null) 
		{
			List<JsoMorphTargets> morphTargets = object.getMorphTargets();
			
			for ( int i = 0, l = morphTargets.size(); i < l; i ++ ) 
			{
				Geometry.MorphTarget morphTarget = geometry.new MorphTarget();
				morphTarget.name = morphTargets.get(i).getName();
				morphTarget.vertices = new ArrayList<Vector3>();
				
				List<Double> srcVertices = morphTargets.get(i).getVertices();
				for( int v = 0, vl = srcVertices.size(); v < vl; v += 3 ) 
				{
					morphTarget.vertices.add( 
							new Vector3(
									srcVertices.get(v) * scale, 
									srcVertices.get(v + 1) * scale,
									srcVertices.get(v + 2) * scale));
				}

				geometry.getMorphTargets().add(morphTarget);
			}
		}

		if ( object.getMorphColors() != null ) 
		{
			List<JsoMorphColors> morphColors = object.getMorphColors();
			
			for ( int i = 0, l = morphColors.size(); i < l; i++ ) 
			{
				Geometry.MorphColor morphColor = geometry.new MorphColor();
				morphColor.name = morphColors.get(i).getName();
				morphColor.colors = new ArrayList<Color>();
								
				List<Double> srcColors = morphColors.get(i).getColors();
				for ( int c = 0, cl = srcColors.size(); c < cl; c += 3 ) 
				{
					Color color = new Color( 0xffaa00 );
					color.setRGB(srcColors.get(c), srcColors.get(c + 1), srcColors.get(c + 2));
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

	private Texture create_texture( String sourceFile, JSONArray repeat, JSONArray offset, JSONArray wrap, int anisotropy )
	{
		boolean isCompressed = sourceFile.toLowerCase().endsWith(".dds");
		final String fullPath =  getTexturePath() + sourceFile;

		final Texture texture;
		
		if ( isCompressed ) 
		{
			texture = new CompressedTexture(fullPath);
		} 
		else 
		{
			texture = new Texture(fullPath, new Texture.ImageLoadHandler() {

				@Override
				public void onImageLoad(Texture texture) 
				{
					int oWidth =  texture.getImage().getOffsetWidth();
					int oHeight = texture.getImage().getOffsetHeight();
							
					if ( !Mathematics.isPowerOfTwo( oWidth ) || !Mathematics.isPowerOfTwo( oHeight ) ) 
					{
						CanvasElement canvas = Document.get().createElement("canvas").cast();
						int width = Mathematics.getNextHighestPowerOfTwo(oWidth);
						int height = Mathematics.getNextHighestPowerOfTwo(oHeight);
						canvas.setWidth(width);
						canvas.setHeight(height);

						Context2d context = canvas.getContext2d();
						context.drawImage( (ImageElement)texture.getImage(), 0, 0, width, height );

						texture.setImage(canvas);
					} 

					texture.setNeedsUpdate(true);
				}
			});
			texture.setNeedsUpdate(false);
		}

		if( repeat != null) 
		{
			texture.getRepeat().set(repeat.get(0), repeat.get(1));

			if ( repeat.get( 0 ) != 1 )
				texture.setWrapS(TextureWrapMode.REPEAT);
			if ( repeat.get( 1 ) != 1 )
				texture.setWrapT(TextureWrapMode.REPEAT);
		}

		if ( offset != null) 
		{
			texture.getOffset().set(offset.get(0), offset.get(1));
		}

		if ( wrap != null) 
		{
			if ( wrap.get(0) != null )
				texture.setWrapS(wrap.get(0).getValue());
			if ( wrap.get(1) != null ) 
				texture.setWrapT(wrap.get(1).getValue());
		}

		if ( anisotropy > 0) 
		{
			texture.setAnisotropy(anisotropy);
		}
		return texture;
	}

	private Color getColor(JSONArray rgb ) throws JSONException
	{
		return new Color(
				  ((int)(rgb.getDouble(0) * 255) << 16 )
				+ ((int)(rgb.getDouble(1) * 255) << 8 )
				+  (int)(rgb.getDouble(2) * 255));
	}

	private Color getColor(JSONObject jsonMaterial, String name ) throws JSONException
	{
		JSONArray color = jsonMaterial.getJSONArray(name);
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

