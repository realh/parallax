// Generated from default.vs and default.fs

package org.parallax3d.shaders.source;

public class default_shader
{
    public static final String vertex =
"void main() {\n" +
"\n" +
"	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"}\n";

    public static final String fragment =
"void main() {\n" +
"\n" +
"	gl_FragColor = vec4( 1.0, 0.0, 0.0, 0.5 );\n" +
"\n" +
"}\n";
}
