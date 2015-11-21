// Generated from cube.vs and cube.fs

package org.parallax3d.shaders.source;

public class cube
{
    public static final String vertex =
"varying vec3 vWorldPosition;\n" +
"\n" +
"[*]	\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 worldPosition = modelMatrix * vec4( position, 1.0 );\n" +
"	vWorldPosition = worldPosition.xyz;\n" +
"\n" +
"	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"[*]	\n" +
"\n" +
"}\n";

    public static final String fragment =
"uniform samplerCube tCube;\n" +
"uniform float tFlip;\n" +
"\n" +
"varying vec3 vWorldPosition;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = textureCube( tCube, vec3( tFlip * vWorldPosition.x, vWorldPosition.yz ) );\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n";
}
