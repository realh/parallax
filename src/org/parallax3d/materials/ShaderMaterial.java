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

package org.parallax3d.materials;

import org.parallax3d.renderer.WebGLExtensions;
import org.parallax3d.shaders.ProgramParameters;
import org.parallax3d.shaders.Shader;
import org.parallax3d.math.Color;

public class ShaderMaterial extends Material 
	implements HasWireframe, HasFog, HasColor, HasVertexColors, HasSkinning, HasShading
{
	class ShaderMaterialShader extends Shader
	{

		public ShaderMaterialShader(String vertexShader, String fragmentShader) 
		{
			super(vertexShader, fragmentShader);
		}

		@Override
		protected void initUniforms() {

		}
		
	}
	
	private boolean isWireframe;
	private int wireframeLineWidth;
	
	// set to use scene fog
	private boolean isFog;
	
	private Material.SHADING shading;
	
	private Color color;
	
	private Material.COLORS vertexColors;
	
	// set to use skinning attribute streams
	private boolean isSkinning;
	// set to use morph targets
	private boolean isMorphTargets;
	// set to use morph normals
	private boolean isMorphNormals;
	
	private int numSupportedMorphTargets;
	private int numSupportedMorphNormals;
	
	// set to use scene lights
	private boolean isLights;
	
	private Shader shader;

	private String vertexExtensions;
	private String fragmentExtensions;

	public ShaderMaterial(Shader.DefaultResources resource) 
	{
		this(resource.getVertexShader(), resource.getFragmentShader());
	}

	public ShaderMaterial(Shader.DefaultResources resource,
						  String vertexExtensions, String fragmentExtensions)
	{
		this(resource.getVertexShader(), resource.getFragmentShader(),
				vertexExtensions, fragmentExtensions);
	}

	public ShaderMaterial(String vertexShader, String fragmentShader)
	{		
		this(vertexShader, fragmentShader, null, null);
	}

	public ShaderMaterial(String vertexShader, String fragmentShader,
						  String vertexExtensions, String fragmentExtensions)
	{
		this();
		this.shader = new ShaderMaterialShader(vertexShader, fragmentShader);
		this.vertexExtensions = vertexExtensions;
		this.fragmentExtensions = fragmentExtensions;
	}

	/**
	 * If shader already has extensions at this point they will override the
	 * ones created dynamically from ProgramParameters.
	 *
	 * @param shader
	 */
	public ShaderMaterial(Shader shader)
	{
		this();
		this.shader = shader;
		if (shader.getVertexExtensions() != null)
			this.vertexExtensions = shader.getVertexExtensions();
		if (shader.getFragmentExtensions() != null)
			this.fragmentExtensions = shader.getFragmentExtensions();
	}

	private ShaderMaterial()
	{
		setWireframe(false);
		setWireframeLineWidth(1);
		
		setFog(false);
		
		setShading(Material.SHADING.SMOOTH);
		
		setColor(new Color(0xffffff));
		
		setVertexColors(Material.COLORS.NO);
	}
	
	public boolean bufferGuessUVType () 
	{
		return true;
	}
	
	@Override
	public Shader getAssociatedShader() {
		return shader;
	}
		
	public boolean isLights() {
		return this.isLights;
	}
	
	/**
	 * Enable/Disable scene lights
	 */
	public void setLights(boolean isLights) {
		this.isLights = isLights;
	}
	
	@Override
	public boolean isWireframe() {
		return this.isWireframe;
	}

	@Override
	public void setWireframe(boolean wireframe) {
		this.isWireframe = wireframe;
	}

	@Override
	public int getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public void setWireframeLineWidth(int wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
	}
	
	@Override
	public boolean isFog() {
		return this.isFog;
	}

	@Override
	public void setFog(boolean fog) {
		this.isFog = fog;
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public Material.COLORS isVertexColors() {
		return this.vertexColors;
	}

	@Override
	public void setVertexColors(Material.COLORS vertexColors) {
		this.vertexColors = vertexColors;
	}

	@Override
	public boolean isSkinning() {
		return this.isSkinning;
	}

	@Override
	public void setSkinning(boolean isSkinning) {
		this.isSkinning = isSkinning;
	}

	@Override
	public boolean isMorphTargets() {
		return this.isMorphTargets;
	}

	@Override
	public void setMorphTargets(boolean isMorphTargets) {
		this.isMorphTargets = isMorphTargets;
	}

	@Override
	public boolean isMorphNormals() {
		return this.isMorphNormals;
	}

	@Override
	public void setMorphNormals(boolean isMorphNormals) {
		this.isMorphNormals = isMorphNormals;
	}
	
	public int getNumSupportedMorphTargets() {
		return this.numSupportedMorphTargets;
	}
	
	public void setNumSupportedMorphTargets(int num) {
		this.numSupportedMorphTargets = num;
	}
	
	public int getNumSupportedMorphNormals() {
		return this.numSupportedMorphNormals;
	}
	
	public void setNumSupportedMorphNormals(int num) {
		this.numSupportedMorphNormals = num;
	}
	
	public Material.SHADING getShading() {
		return this.shading;
	}

	public void setShading(Material.SHADING shading) {
		this.shading = shading;
	}

	@Override
	protected String getExtensionsVertex(WebGLExtensions extensions, ProgramParameters params)
	{
		return vertexExtensions == null ?
				super.getExtensionsVertex(extensions, params) : vertexExtensions;
	}

	@Override
	protected String getExtensionsFragment(WebGLExtensions extensions, ProgramParameters params)
	{
		return fragmentExtensions == null ?
				super.getExtensionsFragment(extensions, params) : fragmentExtensions;
	}

	public ShaderMaterial clone() {

		ShaderMaterial material = new ShaderMaterial();
		
		super.clone(material);

		material.shader = this.shader;

//		material.uniforms = THREE.UniformsUtils.clone( this.uniforms );

//		material.attributes = this.attributes;
//		material.defines = this.defines;

		material.shading = this.shading;

		material.isWireframe = this.isWireframe;
		material.wireframeLineWidth = this.wireframeLineWidth;

		material.isFog = this.isFog;

		material.isLights = this.isLights;

		material.vertexColors = this.vertexColors;

		material.isSkinning = this.isSkinning;

		material.isMorphTargets = this.isMorphTargets;
		material.isMorphNormals = this.isSkinning;

		return material;

	}
}
