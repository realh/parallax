// Generated from shadowmap_pars_vertex.glsl and shadowmap_pars_fragment.glsl

package org.parallax3d.shaders.chunk;

public class shadowmap_pars
{
    public static final String vertex =
"#ifdef USE_SHADOWMAP\n" +
"\n" +
"	varying vec4 vShadowCoord[ MAX_SHADOWS ];\n" +
"	uniform mat4 shadowMatrix[ MAX_SHADOWS ];\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"#ifdef USE_SHADOWMAP\n" +
"\n" +
"	uniform sampler2D shadowMap[ MAX_SHADOWS ];\n" +
"	uniform vec2 shadowMapSize[ MAX_SHADOWS ];\n" +
"\n" +
"	uniform float shadowDarkness[ MAX_SHADOWS ];\n" +
"	uniform float shadowBias[ MAX_SHADOWS ];\n" +
"\n" +
"	varying vec4 vShadowCoord[ MAX_SHADOWS ];\n" +
"\n" +
"	float unpackDepth( const in vec4 rgba_depth ) {\n" +
"\n" +
"		const vec4 bit_shift = vec4( 1.0 / ( 256.0 * 256.0 * 256.0 ), 1.0 / ( 256.0 * 256.0 ), 1.0 / 256.0, 1.0 );\n" +
"		float depth = dot( rgba_depth, bit_shift );\n" +
"		return depth;\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n";
}
