// Generated from logdepthbuf_vertex.glsl and logdepthbuf_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class logdepthbuf
{
    public static final String vertex =
"#ifdef USE_LOGDEPTHBUF\n" +
"\n" +
"	gl_Position.z = log2(max(1e-6, gl_Position.w + 1.0)) * logDepthBufFC;\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		vFragDepth = 1.0 + gl_Position.w;\n" +
"\n" +
"#else\n" +
"\n" +
"		gl_Position.z = (gl_Position.z - 1.0) * gl_Position.w;\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"#if defined(USE_LOGDEPTHBUF) && defined(USE_LOGDEPTHBUF_EXT)\n" +
"\n" +
"	gl_FragDepthEXT = log2(vFragDepth) * logDepthBufFC * 0.5;\n" +
"\n" +
"#endif\n";
}
