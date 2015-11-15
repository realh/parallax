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

package thothbot.parallax.core.client.renderers;

import android.opengl.GLES20;
import thothbot.parallax.core.shared.Log;

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

	private static String allExtensions = null;

	public static boolean get(Id id) {

        if (allExtensions == null)
		{
			allExtensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
			Log.debug("OpenGL ES Extensions: " + allExtensions);
		}

        boolean result = allExtensions.contains(id.toString());

		if ( !result ) {

			Log.warn("WebGLRenderer: " + id.toString() + " extension not supported.");

		}
		
		return result;

	}
	
}
