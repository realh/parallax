// Generated from unpackDepthRGBA.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class unpackDepthRGBA
{
    public static final String fragment =
"uniform float opacity;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"// RGBA depth\n" +
"\n" +
"float unpackDepth( const in vec4 rgba_depth ) {\n" +
"\n" +
"	const vec4 bit_shift = vec4( 1.0 / ( 256.0 * 256.0 * 256.0 ), 1.0 / ( 256.0 * 256.0 ), 1.0 / 256.0, 1.0 );\n" +
"	float depth = dot( rgba_depth, bit_shift );\n" +
"	return depth;\n" +
"\n" +
"}\n" +
"\n" +
"void main() {\n" +
"\n" +
"	float depth = 1.0 - unpackDepth( texture2D( tDiffuse, vUv ) );\n" +
"	gl_FragColor = opacity * vec4( vec3( depth ), 1.0 );\n" +
"\n" +
"}\n";
}
