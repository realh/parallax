// Generated from lightmap_vertex.glsl and lightmap_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class lightmap
{
    public static final String vertex =
"#ifdef USE_LIGHTMAP\n" +
"\n" +
"	vUv2 = uv2;\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"#ifdef USE_LIGHTMAP\n" +
"\n" +
"	gl_FragColor = gl_FragColor * texture2D( lightMap, vUv2 );\n" +
"\n" +
"#endif\n";
}
