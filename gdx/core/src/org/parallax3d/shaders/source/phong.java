// Generated from phong.vs and phong.fs

package org.parallax3d.shaders.source;

public class phong
{
    public static final String vertex =
"#define PHONG\n" +
"\n" +
"varying vec3 vViewPosition;\n" +
"varying vec3 vNormal;\n" +
"\n" +
"[*]\n" +
"			\n" +
"void main() {\n" +
"			\n" +
"[*]\n" +
"			\n" +
"vNormal = normalize( transformedNormal );\n" +
"\n" +
"[*]\n" +
"\n" +
"vViewPosition = -mvPosition.xyz;\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n";

    public static final String fragment =
"#define PHONG\n" +
"\n" +
"uniform vec3 diffuse;\n" +
"uniform float opacity;\n" +
"\n" +
"uniform vec3 ambient;\n" +
"uniform vec3 emissive;\n" +
"uniform vec3 specular;\n" +
"uniform float shininess;\n" +
"\n" +
"[*]\n" +
"			\n" +
"void main() {\n" +
"	gl_FragColor = vec4( vec3( 1.0 ), opacity );\n" +
"				\n" +
"[*]\n" +
"				\n" +
"}\n";
}
