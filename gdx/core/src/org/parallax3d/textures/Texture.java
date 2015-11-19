/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Copyright 2015 Tony Houghton, h@realh.co.uk
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

package org.parallax3d.textures;

import com.badlogic.gdx.graphics.GL20;

import org.parallax3d.renderer.GL20Ext;
import org.parallax3d.renderer.Image;
import org.parallax3d.renderer.WebGLRenderer;
import org.parallax3d.math.Vector2;

/**
 * Basic implementation of texture.
 * <p>
 * This code based on three.js code.
 * 
 * @author thothbot
 *
 */
public class Texture
{
	public static enum OPERATIONS
	{
		MULTIPLY(0), // MultiplyOperation
		MIX(1); // MixOperation

		private final int value;
		private OPERATIONS(int value) { this.value = value; }
		public int getValue() { return value; }
	};

	/**
	 * Mapping modes
	 */
	public static enum MAPPING_MODE 
	{
		UV,

		CUBE_REFLECTION,
		CUBE_REFRACTION,

		SPHERICAL_REFLECTION,
		SPHERICAL_REFRACTION
	};

	private static int TextureCount = 0;

	private Image image;

	private int id;

	private Vector2 offset;
	private Vector2 repeat;

	private Texture.MAPPING_MODE mapping;

	private int wrapS;
	private int wrapT;

	private int magFilter;
	private int minFilter;

	private int format;
	private int type;

	private boolean isGenerateMipmaps = true;
	private boolean isPremultiplyAlpha = false;
	private int unpackAlignment = 4; // valid values: 1, 2, 4, 8
									 // (see http://www.khronos.org/opengles/sdk/docs/man/xhtml/glPixelStorei.xml)

	private boolean isNeedsUpdate = true;
	
	protected int[] webglTexture = { 0 };
	
	private int anisotropy;
	
	private int cache_oldAnisotropy;

	/**
	 * Default constructor will create new instance of texture.
	 */
	public Texture()
	{
		this(null);
	}
	
	/**
	 * Constructor will create a texture instance.
	 *  
	 * @param image the Image.
	 */
	public Texture(Image image)
	{
		this(image, 
				Texture.MAPPING_MODE.UV, 
				GL20.GL_CLAMP_TO_EDGE,
				GL20.GL_CLAMP_TO_EDGE,
				GL20.GL_LINEAR,
				GL20.GL_LINEAR_MIPMAP_LINEAR,
				GL20.GL_RGBA,
				GL20.GL_UNSIGNED_BYTE,
				1);
	}
	
	/**
	 * Constructor will create a texture instance.
	 * 
	 * @param image     the media element
	 * @param mapping   the @{link Texture.MAPPING_MODE} value
	 * @param wrapS     the wrap parameter for texture coordinate S.
	 * @param wrapT     the wrap parameter for texture coordinate T
	 * @param magFilter the texture magnification function.
	 * @param minFilter the texture minifying function.
	 * @param format    the PixelFormat value.
	 * @param type      the DataType value.
	 * @param anisotropy the anisotropy value.
	 */
	public Texture(Image image, Texture.MAPPING_MODE mapping, int wrapS,
			int wrapT, int magFilter, int minFilter,
			int format, int type, int anisotropy)
	{	
		this.image = image;		
		this.mapping = mapping;
		
		this.wrapS = wrapS;
		this.wrapT = wrapT;

		this.magFilter = magFilter;
		this.minFilter = minFilter;

		this.format = format;
		this.type = type;
		
		this.id = Texture.TextureCount++;
		this.offset = new Vector2(0, 0);
		this.repeat = new Vector2(1, 1);
	}
	
	/**
	 * Gets texture ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the @{link Texture.MAPPING_MODE} value.
	 */
	public Texture.MAPPING_MODE getMapping() {
		return this.mapping;
	}
	
	/**
	 * Sets the @{link Texture.MAPPING_MODE} value.
	 */
	public void setMapping(Texture.MAPPING_MODE mapping) {
		this.mapping = mapping;
	}

	/**
	 * Sets the wrap parameter for texture coordinate S.
	 * 
	 * @param wrapS the wrap parameter 
	 */
	public void setWrapS(int wrapS)	{
		this.wrapS = wrapS;
	}

	/**
	 * Gets the wrap parameter for texture coordinate S.
	 * 
	 * @return the wrap parameter. 
	 */
	public int getWrapS(){
		return this.wrapS;
	}

	/**
	 * Sets the wrap parameter for texture coordinate T.
	 * 
	 * @param wrapT the wrap parameter 
	 */
	public void setWrapT(int wrapT) {
		this.wrapT = wrapT;
	}
	
	/**
	 * Gets the wrap parameter for texture coordinate T.
	 * 
	 * @return the wrap parameter. 
	 */
	public int getWrapT() {
		return this.wrapT;
	}

	/**
	 * Gets the texture magnification function.
	 * 
	 * @return the texture magnification function.
	 */
	public int getMagFilter() {
		return this.magFilter;
	}
	
	/**
	 * Sets the texture magnification function.
	 */
	public void setMagFilter(int magFilter) {
		this.magFilter = magFilter;
	}

	/**
	 * Gets the texture minifying function.
	 * 
	 * @return the texture minifying function.
	 */
	public int getMinFilter() {
		return this.minFilter;
	}
	
	/**
	 * Sets the texture minifying function.
	 */
	public void setMinFilter(int minFilter) {
		this.minFilter = minFilter;
	}
	
	/**
	 * Checks if the texture needs to be updated.
	 */
	public boolean isNeedsUpdate()	{
		return this.isNeedsUpdate;
	}
		
	/**
	 * Sets flag to updated the texture.
	 */
	public void setNeedsUpdate(boolean needsUpdate) {
		this.isNeedsUpdate = needsUpdate;
	}
	
	/**
	 * Gets texture media element.
	 * 
	 * @return the media element: image or canvas.
	 */
	public Image getImage() {
		return this.image;
	}
	
	/**
	 * Sets texture media element.
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * Gets texture offset.
	 * 
	 * @return the offset vector.
	 */
	public Vector2 getOffset() {
		return offset;
	}

	/**
	 * Set texture offset vector.
	 * 
	 * @param offset the offset vector.
	 */
	public void setOffset(Vector2 offset) {
		this.offset = offset;
	}

	/**
	 * Gets repeat vector.
	 * 
	 * @return the repeat vector.
	 */
	public Vector2 getRepeat() {
		return repeat;
	}

	/**
	 * Sets the repeat vector.
	 * 
	 * @param repeat the repeat vector.
	 */
	public void setRepeat(Vector2 repeat) {
		this.repeat = repeat;
	}

	/**
	 * Gets the PixelFormat value.
	 * 
	 * @return the PixelFormat value.
	 */
	public int getFormat() {
		return format;
	}

	/**
	 * Sets the PixelFormat value.
	 * 
	 * @param format the PixelFormat value.
	 */
	public void setFormat(int format) {
		this.format = format;
	}

	/**
	 * Sets the PixelType value.
	 * 
	 * @return the PixelType value.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the PixelType value.
	 * 
	 * @param type the PixelType} value.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Checks if needed to generate Mipmaps.
	 */
	public boolean isGenerateMipmaps() {
		return isGenerateMipmaps;
	}

	/**
	 * Sets generate Mipmaps flag.
	 */
	public void setGenerateMipmaps(boolean generateMipmaps) {
		this.isGenerateMipmaps = generateMipmaps;
	}

	/**
	 * Gets premultiply alpha flag.
	 */
	public boolean isPremultiplyAlpha() {
		return isPremultiplyAlpha;
	}
	
	public int getAnisotropy() {
		return this.anisotropy;
	}
	
	/**
	 * Method of enhancing the image quality of texture on surfaces 
	 * that are at oblique viewing angles.
	 */
	public void setAnisotropy(int anisotropy) {
		this.anisotropy = anisotropy;
	}

	/**
	 * Sets premultiply alpha flag.
	 */
	public void setPremultiplyAlpha(boolean premultiplyAlpha) {
		this.isPremultiplyAlpha = premultiplyAlpha;
	}
	
	public int getUnpackAlignment() {
		return unpackAlignment;
	}

	public void setUnpackAlignment(int unpackAlignment) {
		this.unpackAlignment = unpackAlignment;
	}

	public int getWebGlTexture() {
		return webglTexture[0];
	}

	public void setWebGlTexture(int webglTexture) {
		this.webglTexture[0] = webglTexture;
	}

	public void setTextureParameters (int textureType, boolean isImagePowerOfTwo )
	{
		setTextureParameters(0, textureType, isImagePowerOfTwo);
	}

	public void setTextureParameters (int maxAnisotropy, int textureType, boolean isImagePowerOfTwo )
	{	
		if ( isImagePowerOfTwo ) 
		{
			gl.glTexParameteri(textureType, GL20.GL_TEXTURE_WRAP_S, this.wrapS);
			gl.glTexParameteri(textureType, GL20.GL_TEXTURE_WRAP_T, this.wrapT);
			gl.glTexParameteri(textureType, GL20.GL_TEXTURE_MAG_FILTER, this.magFilter);
			gl.glTexParameteri(textureType, GL20.GL_TEXTURE_MIN_FILTER, this.minFilter);
		} 
		else 
		{
			gl.glTexParameteri( textureType, GL20.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE );
			gl.glTexParameteri( textureType, GL20.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE );
			gl.glTexParameteri( textureType, GL20.GL_TEXTURE_MAG_FILTER, filterFallback( this.magFilter ) );
			gl.glTexParameteri( textureType, GL20.GL_TEXTURE_MIN_FILTER, filterFallback( this.minFilter ) );
		}
		
		if ( maxAnisotropy > 0 ) 
		{
			if ( this.anisotropy > 1 || this.cache_oldAnisotropy > 1 ) 
			{
				gl.glTexParameterf( textureType,
						GL20Ext.GL_TEXTURE_MAX_ANISOTROPY_EXT,
						Math.min( this.anisotropy, maxAnisotropy ) );
				this.cache_oldAnisotropy = this.anisotropy;
			}
		}
	}
	
	/**
	 * Fallback filters for non-power-of-2 textures.
	 */
	private int filterFallback ( int f ) 
	{
		if(f == GL20.GL_NEAREST || f == GL20.GL_NEAREST_MIPMAP_NEAREST || f == GL20.GL_NEAREST_MIPMAP_LINEAR)
			return GL20.GL_NEAREST;

		return GL20.GL_LINEAR;
	}
	
	/**
	 * Releases a texture from the GL context.
	 */
	public void deallocate(WebGLRenderer renderer)
	{
		if ( webglTexture[0] == 0 ) return;

		gl.glDeleteTextures(1, webglTexture, 0);

		renderer.getInfo().getMemory().textures--;
	}

	public Texture clone(Texture texture)
	{
		texture.offset.copy(this.offset);
		texture.repeat.copy(this.repeat);
		
		texture.setGenerateMipmaps(this.isGenerateMipmaps);
		texture.setPremultiplyAlpha(this.isPremultiplyAlpha);

		return texture;
	}

	/**
	 * Clone the texture, where
	 * {@code this.clone() != this}
	 */
	public Texture clone()
	{
		return clone(new Texture(this.image, this.mapping, this.wrapS, this.wrapT,
				this.magFilter, this.minFilter, this.format, this.type, this.anisotropy));
	}
}
