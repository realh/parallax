// Generated from depthRGBA.vs and depthRGBA.fs

package thothbot.parallax.core.client.shaders.source;

public class depthRGBA
{
    public static final String vertex =
"[*]\n" +
"				\n" +
"void main() {\n" +
"				\n" +
"[*]\n" +
"				\n" +
"}\n";

    public static final String fragment =
"[*]\n" +
"\n" +
"vec4 pack_depth( const in float depth ) {\n" +
"\n" +
"	const vec4 bit_shift = vec4( 256.0 * 256.0 * 256.0, 256.0 * 256.0, 256.0, 1.0 );\n" +
"	const vec4 bit_mask = vec4( 0.0, 1.0 / 256.0, 1.0 / 256.0, 1.0 / 256.0 );\n" +
"	vec4 res = mod( depth * bit_shift * vec4( 255 ), vec4( 256 ) ) / vec4( 255 ); // vec4 res = fract( depth * bit_shift );\n" +
"	res -= res.xxyz * bit_mask;\n" +
"	return res;\n" +
"\n" +
"}\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		gl_FragData[ 0 ] = pack_depth( gl_FragDepthEXT );\n" +
"\n" +
"	#else\n" +
"\n" +
"		gl_FragData[ 0 ] = pack_depth( gl_FragCoord.z );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	//"gl_FragData[ 0 ] = pack_depth( gl_FragCoord.z / gl_FragCoord.w );\n" +
"	//"float z = ( ( gl_FragCoord.z / gl_FragCoord.w ) - 3.0 ) / ( 4000.0 - 3.0 );\n" +
"	//"gl_FragData[ 0 ] = pack_depth( z );\n" +
"	//"gl_FragData[ 0 ] = vec4( z, z, z, 1.0 );\n" +
"\n" +
"}\n";
}
