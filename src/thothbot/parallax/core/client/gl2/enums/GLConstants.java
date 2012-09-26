/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or  = at your 
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

/**
 * All GL2 flags
 * 
 * @author thothbot
 */
public interface GLConstants 
{
	/* ClearBufferMask */
	int DEPTH_BUFFER_BIT = 0x00000100;
	int STENCIL_BUFFER_BIT = 0x00000400;
	int COLOR_BUFFER_BIT = 0x00004000;
	
	 /* BeginMode */
	int POINTS = 0x0000;
	int LINES = 0x0001;
	int LINE_LOOP = 0x0002;
	int LINE_STRIP = 0x0003;
	int TRIANGLES = 0x0004;
	int TRIANGLE_STRIP = 0x0005;
	int TRIANGLE_FAN = 0x0006;
	
	/* BlendingFactorDest */
	int ZERO = 0;
	int ONE = 1;
	int SRC_COLOR = 0x0300;
	int ONE_MINUS_SRC_COLOR = 0x0301;
	int SRC_ALPHA = 0x0302;
	int ONE_MINUS_SRC_ALPHA = 0x0303;
	int DST_ALPHA = 0x0304;
	int ONE_MINUS_DST_ALPHA = 0x0305;
	
	/* BlendingFactorSrc */
    /*      ZERO */
    /*      ONE */
	int DST_COLOR = 0x0306;
	int ONE_MINUS_DST_COLOR = 0x0307;
	int SRC_ALPHA_SATURATE = 0x0308;
	/*      SRC_ALPHA */
    /*      ONE_MINUS_SRC_ALPHA */
    /*      DST_ALPHA */
    /*      ONE_MINUS_DST_ALPHA */
	
	 /* BlendEquationSeparate */
	int FUNC_ADD = 0x8006;
	int BLEND_EQUATION = 0x8009;
	int BLEND_EQUATION_RGB = 0x8009;
	int BLEND_EQUATION_ALPHA = 0x883D;
	
	 /* BlendSubtract */
	int FUNC_SUBTRACT = 0x800A;
	int FUNC_REVERSE_SUBTRACT = 0x800B;
	
	/* Separate Blend Functions */
	int BLEND_DST_RGB = 0x80C8;
	int BLEND_SRC_RGB = 0x80C9;
	int BLEND_DST_ALPHA = 0x80CA;
	int BLEND_SRC_ALPHA = 0x80CB;
	int CONSTANT_COLOR = 0x8001;
	int ONE_MINUS_CONSTANT_COLOR = 0x8002;
	int CONSTANT_ALPHA = 0x8003;
	int ONE_MINUS_CONSTANT_ALPHA = 0x8004;
	int BLEND_COLOR = 0x8005;
	
	/* Buffer Objects */
	int ARRAY_BUFFER = 0x8892;
	int ELEMENT_ARRAY_BUFFER = 0x8893;
	int ARRAY_BUFFER_BINDING = 0x8894;
	int ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
	
	int STREAM_DRAW = 0x88E0;
	int STATIC_DRAW = 0x88E4;
	int DYNAMIC_DRAW = 0x88E8;
	
	int BUFFER_SIZE = 0x8764;
	int BUFFER_USAGE = 0x8765;
	
	int CURRENT_VERTEX_ATTRIB = 0x8626;
	
	/* CullFaceMode */
	int FRONT = 0x0404;
	int BACK = 0x0405;
	int FRONT_AND_BACK = 0x0408;
	
	/* EnableCap */
	/* TEXTURE_2D */
	int CULL_FACE = 0x0B44;
	int BLEND = 0x0BE2;
	int DITHER = 0x0BD0;
	int STENCIL_TEST = 0x0B90;
	int DEPTH_TEST = 0x0B71;
	int SCISSOR_TEST = 0x0C11;
	int POLYGON_OFFSET_FILL = 0x8037;
	int SAMPLE_ALPHA_TO_COVERAGE = 0x809E;
	int SAMPLE_COVERAGE = 0x80A0;
	
	/* ErrorCode */
	int NO_ERROR = 0;
	int INVALID_ENUM = 0x0500;
	int INVALID_VALUE = 0x0501;
	int INVALID_OPERATION = 0x0502;
	int OUT_OF_MEMORY = 0x0505;
	
	/* FrontFaceDirection */
	int CW = 0x0900;
	int CCW = 0x0901;
	
	 /* GetPName */
	int LINE_WIDTH = 0x0B21;
	int ALIASED_POINT_SIZE_RANGE = 0x846D;
	int ALIASED_LINE_WIDTH_RANGE = 0x846E;
	int CULL_FACE_MODE = 0x0B45;
	int FRONT_FACE = 0x0B46;
	int DEPTH_RANGE = 0x0B70;
	int DEPTH_WRITEMASK = 0x0B72;
	int DEPTH_CLEAR_VALUE = 0x0B73;
	int DEPTH_FUNC = 0x0B74;
	int STENCIL_CLEAR_VALUE = 0x0B91;
	int STENCIL_FUNC = 0x0B92;
	int STENCIL_FAIL = 0x0B94;
	int STENCIL_PASS_DEPTH_FAIL = 0x0B95;
	int STENCIL_PASS_DEPTH_PASS = 0x0B96;
	int STENCIL_REF = 0x0B97;
	int STENCIL_VALUE_MASK = 0x0B93;
	int STENCIL_WRITEMASK = 0x0B98;
	int STENCIL_BACK_FUNC = 0x8800;
	int STENCIL_BACK_FAIL = 0x8801;
	int STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
	int STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
	int STENCIL_BACK_REF = 0x8CA3;
	int STENCIL_BACK_VALUE_MASK = 0x8CA4;
	int STENCIL_BACK_WRITEMASK = 0x8CA5;
	int VIEWPORT = 0x0BA2;
	int SCISSOR_BOX = 0x0C10;
	/*      SCISSOR_TEST */
	int COLOR_CLEAR_VALUE = 0x0C22;
	int COLOR_WRITEMASK = 0x0C23;
	int UNPACK_ALIGNMENT = 0x0CF5;
	int PACK_ALIGNMENT = 0x0D05;
	int MAX_TEXTURE_SIZE = 0x0D33;
	int MAX_VIEWPORT_DIMS = 0x0D3A;
	int SUBPIXEL_BITS = 0x0D50;
	int RED_BITS = 0x0D52;
	int GREEN_BITS = 0x0D53;
	int BLUE_BITS = 0x0D54;
	int ALPHA_BITS = 0x0D55;
	int DEPTH_BITS = 0x0D56;
	int STENCIL_BITS = 0x0D57;
	int POLYGON_OFFSET_UNITS = 0x2A00;
	/*      POLYGON_OFFSET_FILL */
	int POLYGON_OFFSET_FACTOR = 0x8038;
	int TEXTURE_BINDING_2D = 0x8069;
	int SAMPLE_BUFFERS = 0x80A8;
	int SAMPLES = 0x80A9;
	int SAMPLE_COVERAGE_VALUE = 0x80AA;
	int SAMPLE_COVERAGE_INVERT = 0x80AB;
	int NUM_COMPRESSED_TEXTURE_FORMATS = 0x86A2;
	
	int COMPRESSED_TEXTURE_FORMATS = 0x86A3;
	
	/* HintMode */
	int DONT_CARE = 0x1100;
	int FASTEST = 0x1101;
	int NICEST = 0x1102;
	
	/* HintTarget */
	int GENERATE_MIPMAP_HINT = 0x8192;
	
	/* DataType */
	int BYTE = 0x1400;
	int UNSIGNED_BYTE = 0x1401;
	int SHORT = 0x1402;
	int UNSIGNED_SHORT = 0x1403;
	int INT = 0x1404;
	int UNSIGNED_INT = 0x1405;
	int FLOAT = 0x1406;
	
	/* PixelFormat */
	int DEPTH_COMPONENT = 0x1902;
	int ALPHA = 0x1906;
	int RGB = 0x1907;
	int RGBA = 0x1908;
	int LUMINANCE = 0x1909;
	int LUMINANCE_ALPHA = 0x190A;
	
	 /* PixelType */
    /*      UNSIGNED_BYTE */
	int UNSIGNED_SHORT_4_4_4_4 = 0x8033;
	int UNSIGNED_SHORT_5_5_5_1 = 0x8034;
	int UNSIGNED_SHORT_5_6_5 = 0x8363;
	
	/* Shaders */
	int FRAGMENT_SHADER = 0x8B30;
	int VERTEX_SHADER = 0x8B31;
	int MAX_VERTEX_ATTRIBS = 0x8869;
	int MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB;
	int MAX_VARYING_VECTORS = 0x8DFC;
	int MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D;
	int MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C;
	int MAX_TEXTURE_IMAGE_UNITS = 0x8872;
	int MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD;
	int SHADER_TYPE = 0x8B4F;
	int DELETE_STATUS = 0x8B80;
	int LINK_STATUS = 0x8B82;
	int VALIDATE_STATUS = 0x8B83;
	int ATTACHED_SHADERS = 0x8B85;
	int ACTIVE_UNIFORMS = 0x8B86;
	int ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87;
	int ACTIVE_ATTRIBUTES = 0x8B89;
	int ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A;
	int SHADING_LANGUAGE_VERSION = 0x8B8C;
	int CURRENT_PROGRAM = 0x8B8D;
	
	/* StencilFunction */
	int NEVER = 0x0200;
	int LESS = 0x0201;
	int EQUAL = 0x0202;
	int LEQUAL = 0x0203;
	int GREATER = 0x0204;
	int NOTEQUAL = 0x0205;
	int GEQUAL = 0x0206;
	int ALWAYS = 0x0207;
	
	/* StencilOp */
    /*      ZERO */
	int KEEP = 0x1E00;
	int REPLACE = 0x1E01;
	int INCR = 0x1E02;
	int DECR = 0x1E03;
	int INVERT = 0x150A;
	int INCR_WRAP = 0x8507;
	int DECR_WRAP = 0x8508;
	
	/* StringName */
	int VENDOR = 0x1F00;
	int RENDERER = 0x1F01;
	int VERSION = 0x1F02;
	int EXTENSIONS = 0x1F03;
	
	/* TextureMagFilter */
	int NEAREST = 0x2600;
	int LINEAR = 0x2601;
	
	/* TextureMinFilter */
    /*      NEAREST */
    /*      LINEAR */
	int NEAREST_MIPMAP_NEAREST = 0x2700;
	int LINEAR_MIPMAP_NEAREST = 0x2701;
	int NEAREST_MIPMAP_LINEAR = 0x2702;
	int LINEAR_MIPMAP_LINEAR = 0x2703;
	
	/* TextureParameterName */
	int TEXTURE_MAG_FILTER = 0x2800;
	int TEXTURE_MIN_FILTER = 0x2801;
	int TEXTURE_WRAP_S = 0x2802;
	int TEXTURE_WRAP_T = 0x2803;
	
	/* TextureTarget */
	int TEXTURE = 0x1702;
	int TEXTURE_2D = 0x0DE1;
	
	int TEXTURE_CUBE_MAP = 0x8513;
	int TEXTURE_BINDING_CUBE_MAP = 0x8514;
	int TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;
	int TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516;
	int TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517;
	int TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518;
	int TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519;
	int TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A;
	int MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C;
	
	/* TextureUnit */
	int TEXTURE0 = 0x84C0;
	int TEXTURE1 = 0x84C1;
	int TEXTURE2 = 0x84C2;
	int TEXTURE3 = 0x84C3;
	int TEXTURE4 = 0x84C4;
	int TEXTURE5 = 0x84C5;
	int TEXTURE6 = 0x84C6;
	int TEXTURE7 = 0x84C7;
	int TEXTURE8 = 0x84C8;
	int TEXTURE9 = 0x84C9;
	int TEXTURE10 = 0x84CA;
	int TEXTURE11 = 0x84CB;
	int TEXTURE12 = 0x84CC;
	int TEXTURE13 = 0x84CD;
	int TEXTURE14 = 0x84CE;
	int TEXTURE15 = 0x84CF;
	int TEXTURE16 = 0x84D0;
	int TEXTURE17 = 0x84D1;
	int TEXTURE18 = 0x84D2;
	int TEXTURE19 = 0x84D3;
	int TEXTURE20 = 0x84D4;
	int TEXTURE21 = 0x84D5;
	int TEXTURE22 = 0x84D6;
	int TEXTURE23 = 0x84D7;
	int TEXTURE24 = 0x84D8;
	int TEXTURE25 = 0x84D9;
	int TEXTURE26 = 0x84DA;
	int TEXTURE27 = 0x84DB;
	int TEXTURE28 = 0x84DC;
	int TEXTURE29 = 0x84DD;
	int TEXTURE30 = 0x84DE;
	int TEXTURE31 = 0x84DF;
	int ACTIVE_TEXTURE = 0x84E0;
	
	/* TextureWrapMode */
	int REPEAT = 0x2901;
	int CLAMP_TO_EDGE = 0x812F;
	int MIRRORED_REPEAT = 0x8370;
	
	/* Uniform Types */
	int FLOAT_VEC2 = 0x8B50;
	int FLOAT_VEC3 = 0x8B51;
	int FLOAT_VEC4 = 0x8B52;
	int INT_VEC2 = 0x8B53;
	int INT_VEC3 = 0x8B54;
	int INT_VEC4 = 0x8B55;
	int BOOL = 0x8B56;
	int BOOL_VEC2 = 0x8B57;
	int BOOL_VEC3 = 0x8B58;
	int BOOL_VEC4 = 0x8B59;
	int FLOAT_MAT2 = 0x8B5A;
	int FLOAT_MAT3 = 0x8B5B;
	int FLOAT_MAT4 = 0x8B5C;
	int SAMPLER_2D = 0x8B5E;
	int SAMPLER_CUBE = 0x8B60;
	
	/* Uniform Types */
	int VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622;
	int VERTEX_ATTRIB_ARRAY_SIZE = 0x8623;
	int VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624;
	int VERTEX_ATTRIB_ARRAY_TYPE = 0x8625;
	int VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A;
	int VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;
	int VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F;
	int IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A;
	int IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;
	
	/* Shader Source */
	int COMPILE_STATUS = 0x8B81;
	int INFO_LOG_LENGTH = 0x8B84;
	int SHADER_SOURCE_LENGTH = 0x8B88;
	int SHADER_COMPILER = 0x8DFA;
	
	/* Shader Precision-Specified Types */
	int LOW_FLOAT = 0x8DF0;
	int MEDIUM_FLOAT = 0x8DF1;
	int HIGH_FLOAT = 0x8DF2;
	int LOW_INT = 0x8DF3;
	int MEDIUM_INT = 0x8DF4;
	int HIGH_INT = 0x8DF5;
	
	/* Framebuffer Object. */
	int FRAMEBUFFER = 0x8D40;
	int RENDERBUFFER = 0x8D41;
	
	int RGBA4 = 0x8056;
	int RGB5_A1 = 0x8057;
	int RGB565 = 0x8D62;
	int DEPTH_COMPONENT16 = 0x81A5;
	int STENCIL_INDEX = 0x1901;
	int STENCIL_INDEX8 = 0x8D48;
	int DEPTH_STENCIL = 0x84F9;
	
	int RENDERBUFFER_WIDTH = 0x8D42;
	int RENDERBUFFER_HEIGHT = 0x8D43;
	int RENDERBUFFER_INTERNAL_FORMAT = 0x8D44;
	int RENDERBUFFER_RED_SIZE = 0x8D50;
	int RENDERBUFFER_GREEN_SIZE = 0x8D51;
	int RENDERBUFFER_BLUE_SIZE = 0x8D52;
	int RENDERBUFFER_ALPHA_SIZE = 0x8D53;
	int RENDERBUFFER_DEPTH_SIZE = 0x8D54;
	int RENDERBUFFER_STENCIL_SIZE = 0x8D55;
	
	int FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0;
	int FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1;
	int FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2;
	int FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3;
	
	int COLOR_ATTACHMENT0 = 0x8CE0;
	int DEPTH_ATTACHMENT = 0x8D00;
	int STENCIL_ATTACHMENT = 0x8D20;
	int DEPTH_STENCIL_ATTACHMENT = 0x821A;
	
	int NONE = 0;
	
	int FRAMEBUFFER_COMPLETE = 0x8CD5;
	int FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6;
	int FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7;
	int FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9;
	int FRAMEBUFFER_UNSUPPORTED = 0x8CDD;
	int FRAMEBUFFER_BINDING = 0x8CA6;
	
	int RENDERBUFFER_BINDING = 0x8CA7;
	int MAX_RENDERBUFFER_SIZE = 0x84E8;
	
	int INVALID_FRAMEBUFFER_OPERATION = 0x0506;
	
	/* WebGL-specific enums */
	int UNPACK_FLIP_Y_WEBGL = 0x9240;
	int UNPACK_PREMULTIPLY_ALPHA_WEBGL = 0x9241;
	int CONTEXT_LOST_WEBGL = 0x9242;
	
	/**
	 * Gets the enum's numerical value.
	 */
	int getValue();
}
