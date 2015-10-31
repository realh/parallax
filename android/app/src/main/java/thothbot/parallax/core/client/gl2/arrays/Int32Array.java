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
package thothbot.parallax.core.client.gl2.arrays;

import android.opengl.GLES20;

import java.nio.IntBuffer;

/**
 * The typed array that holds int (32-bit signed integer) as its element.
 * 
 * @author h@realh.co.uk
 */
public final class Int32Array extends TypeArray {
	public static final int BYTES_PER_ELEMENT = 2;

	private IntBuffer intBuffer;

	/**
	 * @param capacity	In bytes.
	 */
	protected Int32Array(int capacity) {
		super(capacity);
		createTypedBuffer();
	}

	@Override
	protected void createTypedBuffer() {
		intBuffer = getBuffer().asIntBuffer();
	}

	@Override
	public int getElementType() {
		return GLES20.GL_INT;
	}

	@Override
	public int getElementSize() {
		return 4;
	}

	/**
	 * Create a new {@link java.nio.ByteBuffer} with enough bytes to hold length
	 * elements of this typed array.
	 * 
	 * @param length
	 */
	public static Int32Array create(int length) {
		return new Int32Array(length * BYTES_PER_ELEMENT);
	}
	
	/**
	 * Create a copy of array.
	 * 
	 * @param array
	 */
	public static Int32Array create(TypeArray array) {
		Int32Array result = create(array.getLength());
		result.set(array);
		return result;
	}
	
	/**
	 * Create an array .
	 *
	 * @param array
	 */
	public static Int32Array create(int[] array) {
		Int32Array result = create(array.length);
		result.intBuffer.put(array);
		return result;
	}

	/**
	 * Returns the element at the given numeric index.
	 *
	 * @param index
	 */
	public int get(int index) {
		return intBuffer.get(index);
	}

	/**
	 * Sets the element at the given numeric index to the given value.
	 *
	 * @param index
	 * @param value
	 */
	public void set(int index, int value) {
		intBuffer.put(index, value);
	}

	public void set(Int32Array array) {
        super.set(array);
    }

	public void set(Int32Array array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

    public void set(TypeArray array, int offset) {
        super.set(array, offset * BYTES_PER_ELEMENT);
    }

	/**
	 * slice methods were not used.
     */
}
