// Generated from color_vertex.glsl and color_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class color
{
    public static final String vertex =
"#ifdef USE_COLOR\n" +
"\n" +
"	#ifdef GAMMA_INPUT\n" +
"\n" +
"		vColor = color * color;\n" +
"\n" +
"	#else\n" +
"\n" +
"		vColor = color;\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"#ifdef USE_COLOR\n" +
"\n" +
"	gl_FragColor = gl_FragColor * vec4( vColor, 1.0 );\n" +
"\n" +
"#endif\n";
}
