// Generated from logdepthbuf_par_vertex.glsl and logdepthbuf_par_fragment.glsl

package org.parallax3d.shaders.chunk;

public class logdepthbuf_par
{
    public static final String vertex =
"#ifdef USE_LOGDEPTHBUF\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		varying float vFragDepth;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	uniform float logDepthBufFC;\n" +
"\n" +
"#endif\n";

    public static final String fragment = vertex;
}
