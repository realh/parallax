// Generated from dashed.vs and dashed.fs

package org.parallax3d.shaders.source;

public class dashed
{
    public static final String vertex =
"uniform float scale;\n" +
"attribute float lineDistance;\n" +
"\n" +
"varying float vLineDistance;\n" +
"\n" +
"[*]		\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]		\n" +
"\n" +
"	vLineDistance = scale * lineDistance;\n" +
"\n" +
"	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
"	gl_Position = projectionMatrix * mvPosition;\n" +
"\n" +
"[*]		\n" +
"\n" +
"}\n";

    public static final String fragment =
"uniform vec3 diffuse;\n" +
"uniform float opacity;\n" +
"\n" +
"uniform float dashSize;\n" +
"uniform float totalSize;\n" +
"\n" +
"varying float vLineDistance;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	if ( mod( vLineDistance, totalSize ) > dashSize ) {\n" +
"\n" +
"		discard;\n" +
"\n" +
"	}\n" +
"\n" +
"	gl_FragColor = vec4( diffuse, opacity );\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n";
}
