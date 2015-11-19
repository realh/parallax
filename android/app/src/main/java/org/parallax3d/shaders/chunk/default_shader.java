// Generated from default_vertex.glsl

package org.parallax3d.shaders.chunk;

public class default_shader
{
    public static final String vertex =
"vec4 mvPosition;\n" +
"\n" +
"#ifdef USE_SKINNING\n" +
"\n" +
"	mvPosition = modelViewMatrix * skinned;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if !defined( USE_SKINNING ) && defined( USE_MORPHTARGETS )\n" +
"\n" +
"	mvPosition = modelViewMatrix * vec4( morphed, 1.0 );\n" +
"\n" +
"#endif\n" +
"\n" +
"#if !defined( USE_SKINNING ) && ! defined( USE_MORPHTARGETS )\n" +
"\n" +
"	mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"#endif\n" +
"\n" +
"gl_Position = projectionMatrix * mvPosition;\n";
}
