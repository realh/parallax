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

package org.parallax3d.renderer;

import com.badlogic.gdx.graphics.GL20;
import org.parallax3d.core.Log;

public final class WebGLExtensions {

    public static enum Id {
		OES_texture_float,
		OES_texture_float_linear,
		OES_standard_derivatives,
		EXT_texture_filter_anisotropic,
		EXT_compressed_texture_s3tc,
		EXT_compressed_texture_pvrtc,
		OES_element_index_uint,
		EXT_blend_minmax,
		EXT_frag_depth
	};

	private final String allExtensions;

	private GL20 gl;

	private final boolean isOpenGLES;

	public WebGLExtensions(GL20 gl, boolean isOpenGLES)
	{
		this.gl = gl;
		this.isOpenGLES = isOpenGLES;
		allExtensions = gl.glGetString(GL20.GL_EXTENSIONS);
		Log.debug("OpenGL " + (isOpenGLES ? "ES " : "") + "Extensions: " + allExtensions);
	}

	public boolean get(Id id)
	{
        boolean result;

		if (isBuiltIn(id))
		{
			return true;
		}

		result = allExtensions == null ? false : allExtensions.contains(getName(id));

		if ( !result )
		{
			Log.warn("WebGLRenderer: " + id.toString() + " extension not supported.");
		}
		
		return result;

	}

	public boolean isBuiltIn(Id id)
	{
		if (isOpenGLES)
			return false;
		switch (id)
		{
			case OES_standard_derivatives:
			case OES_element_index_uint:
				return true;
			default:
				return false;
		}
	}

	public String getGLSLHeader(Id id)
	{
		if (!get(id) || isBuiltIn(id))
			return "";
		else
			return "#extension " + getName(id) + ": enable\n\n";
	}

	private String getName(Id id)
	{
		String name = id.toString();

		if (!isOpenGLES)
		{
			if (name.substring(0, 3).equals("OES"))
				name = "ARB" + name.substring(3);
		}

		return "GL_" + name;
	}
}
