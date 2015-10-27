// Generated from lightmap_pars_vertex.glsl and lightmap_pars_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class lightmap_pars
{
    public static final String vertex =
"#ifdef USE_LIGHTMAP\n" +
"\n" +
"	varying vec2 vUv2;\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"#ifdef USE_LIGHTMAP\n" +
"\n" +
"	varying vec2 vUv2;\n" +
"	uniform sampler2D lightMap;\n" +
"\n" +
"#endif\n";
}
