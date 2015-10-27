// Generated from lambert.vs and lambert.fs

package thothbot.parallax.core.client.shaders.source;

public class lambert
{
    public static final String vertex =
"#define LAMBERT\n" +
"\n" +
"varying vec3 vLightFront;\n" +
"\n" +
"#ifdef DOUBLE_SIDED\n" +
"\n" +
"	varying vec3 vLightBack;\n" +
"\n" +
"#endif\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"	\n" +
"[*]\n" +
"						\n" +
"}\n";

    public static final String fragment =
"uniform float opacity;\n" +
"\n" +
"varying vec3 vLightFront;\n" +
"\n" +
"#ifdef DOUBLE_SIDED\n" +
"\n" +
"	varying vec3 vLightBack;\n" +
"\n" +
"#endif\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = vec4( vec3 ( 1.0 ), opacity );\n" +
"			\n" +
"[*]\n" +
"			\n" +
"#ifdef DOUBLE_SIDED\n" +
"\n" +
"	//"float isFront = float( gl_FrontFacing );\n" +
"	//"gl_FragColor.xyz *= isFront * vLightFront + ( 1.0 - isFront ) * vLightBack;\n" +
"\n" +
"	if ( gl_FrontFacing )\n" +
"		gl_FragColor.xyz *= vLightFront;\n" +
"	else\n" +
"		gl_FragColor.xyz *= vLightBack;\n" +
"\n" +
"#else\n" +
"\n" +
"	gl_FragColor.xyz *= vLightFront;\n" +
"\n" +
"#endif\n" +
"			\n" +
"[*]\n" +
"			\n" +
"}\n";
}
