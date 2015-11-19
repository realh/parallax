/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.shaders;

import android.opengl.GLES20;
import org.parallax3d.core.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parallax3d.renderer.Float32Array;
import org.parallax3d.math.Mathematics;

/**
 * Basic abstract shader.
 * 
 * @author thothbot
 * 
 */
public abstract class Shader
{

	/**
	 * Sets the Shader's precision value.
	 */
	public static enum PRECISION 
	{
		HIGHP,
		MEDIUMP,
		LOWP
	}
	
	public static class DefaultResources
	{
		public String getVertexShader()
		{
		    return org.parallax3d.shaders.source.default_shader.vertex;
		}

		public String getFragmentShader()
		{
		    return org.parallax3d.shaders.source.default_shader.fragment;
		}
	}
	
	// shader precision. Can be "highp", "mediump" or "lowp".
	private PRECISION precision = PRECISION.HIGHP;

	private int program;

	// Store uniforms and locations 
	private Map<String, Uniform> uniforms;
	// Should be null by default. Think how we can merge two maps.
	private Map<String, Attribute> attributes;
	// Store locations
	private Map<String, Integer> attributesLocations;

	private String vertexShaderSource = "";
	private String fragmentShaderSource = "";

	private String vertexExtensions = "";
	private String fragmentExtensions = "";

	private boolean cache_areCustomAttributesDirty;

	private int id;

	private static int shaderCounter;

	private int[] tmpArray = {0};

	/**
	 * This constructor will create new Shader instance. 
	 * 
	 * @param resource the {@link Shader.DefaultResources} instance.
	 */
	public Shader(DefaultResources resource)
	{
		this(resource.getVertexShader(), resource.getFragmentShader());
	}

	public Shader(String vertexShader, String fragmentShader)
	{
		this.id = shaderCounter++;

		updateVertexSource(vertexShader);
		updateFragmentSource(fragmentShader);

		this.uniforms = new HashMap<String, Uniform>();

		this.attributesLocations = new HashMap<String, Integer>();

		initUniforms();
	}

	/**
	 * Gets program ID
	 * 
	 * @return ID
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Gets the shader program.
	 */
	public int getProgram()
	{
		return this.program;
	}
	
	// Called in renderer org.parallax3d.plugins
	public Shader buildProgram()
	{
		return buildProgram(false, 0, 0);
	}
	
	public Shader buildProgram(boolean useVertexTexture, int maxMorphTargets, int maxMorphNormals)
	{
		Log.debug("Building new program...");

		initShaderProgram();

		// Adds default uniforms
		addUniform("viewMatrix",            new Uniform(Uniform.TYPE.FV1 ));
		addUniform("modelViewMatrix",       new Uniform(Uniform.TYPE.FV1 ));
		addUniform("projectionMatrix",      new Uniform(Uniform.TYPE.FV1 ));
		addUniform("normalMatrix",          new Uniform(Uniform.TYPE.FV1 ));
		addUniform("modelMatrix",           new Uniform(Uniform.TYPE.FV1 ));
		addUniform("cameraPosition",        new Uniform(Uniform.TYPE.FV1 ));
		addUniform("morphTargetInfluences", new Uniform(Uniform.TYPE.FV1 ));
		addUniform("bindMatrix",            new Uniform(Uniform.TYPE.FV1 ));
		addUniform("bindMatrixInverse",     new Uniform(Uniform.TYPE.FV1 ));
				
		if ( useVertexTexture ) 
		{
			addUniform("boneTexture", new Uniform(Uniform.TYPE.FV1, null));
			addUniform("boneTextureWidth", new Uniform(Uniform.TYPE.F, null));
			addUniform("boneTextureHeight", new Uniform(Uniform.TYPE.F, null));
		} 
		else 
		{
			addUniform("boneGlobalMatrices", new Uniform(Uniform.TYPE.FV1, null));
		}
		
		// Cache location
		Map<String, Uniform> uniforms = getUniforms();
		for (String id : uniforms.keySet())
			uniforms.get(id).setLocation( GLES20.glGetUniformLocation(this.program, id) );

		// cache attributes locations
		List<String> attributesIds = new ArrayList<String>(Arrays.asList("position", "normal",
				"uv", "uv2", "tangent", "color", "skinIndex", "skinWeight", "lineDistance"));

		for (int i = 0; i < maxMorphTargets; i++)
			attributesIds.add("morphTarget" + i);

		for (int i = 0; i < maxMorphNormals; i++)
			attributesIds.add("morphNormal" + i);

		Map<String, Attribute> attributes = getAttributes();
		if(attributes != null)
			for (String a : attributes.keySet())
				attributesIds.add(a);

		Map<String, Integer> attributesLocations = getAttributesLocations();
		for (String id : attributesIds)
			attributesLocations.put(id, GLES20.glGetAttribLocation(this.program, id));
		
		return this;
	}
	

	private void initShaderProgram()
	{
		Log.debug("Called initProgram()");

		this.program = GLES20.glCreateProgram();

		String vertex = vertexExtensions + getShaderPrecisionDefinition() + "\n" + getVertexSource();
		String fragment = fragmentExtensions + getShaderPrecisionDefinition() + "\n" + getFragmentSource();
		
		int glVertexShader = getShaderProgram(ChunksVertexShader.class, vertex);
		int glFragmentShader = getShaderProgram(ChunksFragmentShader.class, fragment);
		GLES20.glAttachShader(this.program, glVertexShader);
		GLES20.glAttachShader(this.program, glFragmentShader);

		GLES20.glLinkProgram(this.program);

		GLES20.glGetProgramiv(this.program, GLES20.GL_LINK_STATUS, tmpArray, 0);

		if (tmpArray[0] == GLES20.GL_FALSE)
			logMultiline("Could not initialise shader\n"
							+ "GL error: " + GLES20.glGetProgramInfoLog(program)
							+ "\nShader: " + this.getClass().getName()
							+ "\n-----\nVERTEX: " + glVertexShader + "\n" + vertex
							+ "\n-----\nFRAGMENT: " + glFragmentShader + "\n" + fragment
			);

		else
			Log.info("initProgram(): shader has been initialised");

		// clean up
		GLES20.glDeleteShader(glVertexShader);
		GLES20.glDeleteShader(glFragmentShader);
	}
	
	public void setPrecision(Shader.PRECISION precision) {
		this.precision = precision;
	}

	private String getShaderPrecisionDefinition() {
		return "precision " + precision.name().toLowerCase() + " float;";
	}

	/**
	 * Gets the shader.
	 */
	private int getShaderProgram(Class<?> type, String string)
	{
		int shader = 0;

		if (type == ChunksFragmentShader.class)
			shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		else if (type == ChunksVertexShader.class)
			shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

		GLES20.glShaderSource(shader, string);
		GLES20.glCompileShader(shader);

		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, tmpArray, 0);
		if (tmpArray[0] == GLES20.GL_FALSE)
		{
			logMultiline(GLES20.glGetShaderInfoLog(shader));
			return 0;
		}

		return shader;
	}

	/**
	 * Gets source of the Vertex shader.
	 * 
	 * @return the string with Vertex shader code.
	 */
	public String getVertexSource() {
		return this.vertexShaderSource;
	}

	public void setVertexExtensions(String vertexExtensions)
	{
		this.vertexExtensions = vertexExtensions;
	}

	public String getVertexExtensions()
	{
		return vertexExtensions;
	}

	public void setVertexSource(String src) {
		this.vertexShaderSource = src;
	}
	
	protected void updateVertexSource(String src) {
		setVertexSource(src);		
	}

	/**
	 * Gets source of the Fragment shader.
	 * 
	 * @return the string with Fragment shader code.
	 */
	public String getFragmentSource() {
		return this.fragmentShaderSource;		
	}
	
	public void setFragmentSource(String src) {
		this.fragmentShaderSource = src;		
	}

	public void setFragmentExtensions(String fragmentExtensions)
	{
		this.fragmentExtensions = fragmentExtensions;
	}

	public String getFragmentExtensions()
	{
		return fragmentExtensions;
	}

	protected void updateFragmentSource(String src) {
		setFragmentSource(src);		
	}

	// Uniforms
	protected abstract void initUniforms();
	
	/**
	 * Gets shader's uniforms.
	 * 
	 * @return the map of name-uniform 
	 */
	public Map<String, Uniform> getUniforms()
	{
		return this.uniforms;
	}
	
	public void setUniforms(Map<String, Uniform> uniforms)
	{
		this.uniforms.putAll(uniforms);
	}

	public void addUniform(String id, Uniform uniform)
	{
		this.uniforms.put(id, uniform);
	}

	@Deprecated
	public Map<String, Integer> getAttributesLocations() {
		return this.attributesLocations;
	}

	public Map<String, Attribute> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttributes(String id, Attribute attribute) 
	{
		if(this.attributes == null)
		{
			this.attributes = new HashMap<String, Attribute>();
		}
		this.attributes.put(id, attribute);
	}

	public boolean areCustomAttributesDirty() 
	{
		if(this.cache_areCustomAttributesDirty)
			return true;

		if(getAttributes() == null)
			return false;
 
		for ( Attribute attribute: getAttributes().values() )
		{
			if ( attribute.needsUpdate )
			{
				this.cache_areCustomAttributesDirty = true;
				return true;
			}
		}

		return false;
	}

	public void clearCustomAttributes() 
	{
		if(!this.cache_areCustomAttributesDirty)
			return;

		if(getAttributes() == null)
			return;

		for ( Attribute attribute: getAttributes().values() )
		{
			attribute.needsUpdate = false;
			this.cache_areCustomAttributesDirty = false;
		}
	}
	
	public String toString()
	{
		return "{id=" + this.id + ", class=" + getClass().getName() + ", uniforms=" + getUniforms() + "}";		
	}
		
	// Methods
	public static String updateShaderSource(String src, List<String> ... allMods)
	{
		String[] mods = new String[allMods.length];
		for(int i = 0; i < allMods.length; i++)
		{
			String replace = "";

			for(String mod : allMods[i])
				replace += mod + "\n";

			mods[i] = replace;
		}

		return Shader.updateShaderSource(src, mods);
	}

	private static String SHADER_REPLACE_ARG = "[*]";
	public static String updateShaderSource(String src, String ... allMods)
	{	
		if(src.indexOf(SHADER_REPLACE_ARG) >= 0)
		{
			StringBuffer result = new StringBuffer();
			int s = 0;
			int e = 0;

			for ( String replace : allMods )
			{
				if((e = src.indexOf(SHADER_REPLACE_ARG, s)) >= 0)
				{
					result.append(src.substring(s, e));
			        result.append(replace);
			        s = e + SHADER_REPLACE_ARG.length();
				}
			}

			result.append(src.substring(s));

			return result.toString();
		}

		return src;
	}

	public static Float32Array buildKernel( double sigma ) 
	{ 
		int kMaxKernelSize = 25; 
		int kernelSize = (int) (2 * Math.ceil( sigma * 3 ) + 1);

		if ( kernelSize > kMaxKernelSize ) 
			kernelSize = kMaxKernelSize;
		
		double halfWidth = ( kernelSize - 1.0 ) * 0.5;

		Float32Array values = Float32Array.create(kernelSize);

		double sum = 0.0;
		for ( int i = 0; i < kernelSize; ++i ) 
		{
			double result = Mathematics.gauss( i - halfWidth, sigma ); 
			values.set(i, result);
			sum += result;
		}

		// normalize the kernel
		for ( int i = 0; i < kernelSize; ++i ) 
			values.set( i, values.get(i) / sum);

		return values;
	}

	/**
	 * Logs error message s one line at a time because Android has a limit on
	 * log message length.
	 *
	 * @param s
	 */
	private static void logMultiline(String s)
	{
		String[] lines = s.split("\n");
		for (String l: lines)
			Log.error(l);
	}
}
