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

package thothbot.parallax.core.client.gl2.enums;

public enum VertexArrays implements GLConstants
{
	VERTEX_ATTRIB_ARRAY_ENABLED(GLConstants.VERTEX_ATTRIB_ARRAY_ENABLED),
	VERTEX_ATTRIB_ARRAY_SIZE(GLConstants.VERTEX_ATTRIB_ARRAY_SIZE),
	VERTEX_ATTRIB_ARRAY_STRIDE(GLConstants.VERTEX_ATTRIB_ARRAY_STRIDE),
	VERTEX_ATTRIB_ARRAY_TYPE(GLConstants.VERTEX_ATTRIB_ARRAY_TYPE),
	VERTEX_ATTRIB_ARRAY_NORMALIZED(GLConstants.VERTEX_ATTRIB_ARRAY_NORMALIZED),
	VERTEX_ATTRIB_ARRAY_POINTER(GLConstants.VERTEX_ATTRIB_ARRAY_POINTER),
	VERTEX_ATTRIB_ARRAY_BUFFER_BINDING(GLConstants.VERTEX_ATTRIB_ARRAY_BUFFER_BINDING);

	private final int value;

	private VertexArrays(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
