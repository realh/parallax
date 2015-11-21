// Generated from basic.vs and basic.fs

package org.parallax3d.shaders.source;

public class basic
{
    public static final String vertex =
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]			\n" +
"\n" +
"	#ifdef USE_ENVMAP\n" +
"\n" +
"[*]			\n" +
"\n" +
"	#endif\n" +
"\n" +
"[*]			\n" +
"	\n" +
"}\n";

    public static final String fragment =
"uniform vec3 diffuse; \n" +
"uniform float opacity;\n" +
"				\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"	gl_FragColor = vec4( diffuse, opacity );\n" +
"		\n" +
"[*]\n" +
"		\n" +
"}\n";
}
