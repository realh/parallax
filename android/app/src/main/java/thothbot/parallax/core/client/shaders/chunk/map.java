// Generated from map_vertex.glsl and map_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class map
{
    public static final String vertex =
"#if defined( USE_MAP ) || defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( USE_SPECULARMAP ) || defined( USE_ALPHAMAP )\n" +
"\n" +
"	vUv = uv * offsetRepeat.zw + offsetRepeat.xy;\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"#ifdef USE_MAP\n" +
"\n" +
"	vec4 texelColor = texture2D( map, vUv );\n" +
"\n" +
"	#ifdef GAMMA_INPUT\n" +
"\n" +
"		texelColor.xyz *= texelColor.xyz;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	gl_FragColor = gl_FragColor * texelColor;\n" +
"\n" +
"#endif\n";
}
