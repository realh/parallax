// Generated from defaultUv.vs

package org.parallax3d.plugins.postprocessing.shaders.source;

public class defaultUv
{
    public static final String vertex =
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vUv = uv;\n" +
"	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"}\n";
}
