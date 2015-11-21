// Generated from particle_basic.vs and particle_basic.fs

package org.parallax3d.shaders.source;

public class particle_basic
{
    public static final String vertex =
"uniform float size;\n" +
"uniform float scale;\n" +
"			\n" +
"[*]			\n" +
"\n" +
"void main() {\n" +
"			\n" +
"[*]\n" +
"			\n" +
"vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"#ifdef USE_SIZEATTENUATION\n" +
"	gl_PointSize = size * ( scale / length( mvPosition.xyz ) );\n" +
"#else\n" +
"	gl_PointSize = size;\n" +
"#endif\n" +
"\n" +
"gl_Position = projectionMatrix * mvPosition;\n" +
"			\n" +
"[*]\n" +
"\n" +
"}\n";

    public static final String fragment =
"uniform vec3 psColor;\n" +
"uniform float opacity;\n" +
"			\n" +
"[*]\n" +
"			\n" +
"void main() {\n" +
"	gl_FragColor = vec4( psColor, opacity );\n" +
"				\n" +
"[*]\n" +
"					\n" +
"}\n";
}
