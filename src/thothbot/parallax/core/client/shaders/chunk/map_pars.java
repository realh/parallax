// Generated from map_pars_vertex.glsl and map_pars_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class map_pars
{
    public static final String vertex =
"#if defined( USE_MAP ) || defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( USE_SPECULARMAP ) || defined( USE_ALPHAMAP )\n" +
"\n" +
"	varying vec2 vUv;\n" +
"	uniform vec4 offsetRepeat;\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"#if defined( USE_MAP ) || defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( USE_SPECULARMAP ) || defined( USE_ALPHAMAP )\n" +
"\n" +
"	varying vec2 vUv;\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef USE_MAP\n" +
"\n" +
"	uniform sampler2D map;\n" +
"\n" +
"#endif\n";
}
