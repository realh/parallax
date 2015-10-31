// Generated from specularmap_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class specularmap
{
    public static final String fragment =
"float specularStrength;\n" +
"\n" +
"#ifdef USE_SPECULARMAP\n" +
"\n" +
"	vec4 texelSpecular = texture2D( specularMap, vUv );\n" +
"	specularStrength = texelSpecular.r;\n" +
"\n" +
"#else\n" +
"\n" +
"	specularStrength = 1.0;\n" +
"\n" +
"#endif\n";
}
