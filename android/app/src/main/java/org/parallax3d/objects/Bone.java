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

package org.parallax3d.objects;

import org.parallax3d.core.Object3D;

/**
 * A bone which is part of a SkinnedMesh.
 *
 */
public class Bone extends Object3D
{

	private SkinnedMesh skin;

	/**
	 * 
	 * @param belongsToSkin An instance of {@link SkinnedMesh}.
	 */
	public Bone(SkinnedMesh belongsToSkin) 
	{
		this.setSkin(belongsToSkin);		
	}

	/**
	 * @return the skin
	 */
	public SkinnedMesh getSkin() {
		return skin;
	}

	/**
	 * @param skin the skin to set
	 */
	public void setSkin(SkinnedMesh skin) {
		this.skin = skin;
	}

}
