/*
 * Copyright 2009-2011 Sönke Sothmann, Steffen Schäfer and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.parallax3d.renderer;

import org.parallax3d.core.BufferAttribute;

/**
 * A TypeArray suitable for use as indices in a
 * {@link BufferAttribute}
 *
 * @author h@realh.co.uk
 */
public abstract class IndexTypeArray extends TypeArray {

	protected IndexTypeArray(int capacity) {
        super(capacity);
	}

    public abstract int getUnsigned(int index);
}
