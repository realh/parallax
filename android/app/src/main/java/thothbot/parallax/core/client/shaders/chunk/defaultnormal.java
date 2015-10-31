// Generated from defaultnormal_vertex.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class defaultnormal
{
    public static final String vertex =
"vec3 objectNormal;\n" +
"\n" +
"#ifdef USE_SKINNING\n" +
"\n" +
"	objectNormal = skinnedNormal.xyz;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if !defined( USE_SKINNING ) && defined( USE_MORPHNORMALS )\n" +
"\n" +
"	objectNormal = morphedNormal;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if !defined( USE_SKINNING ) && ! defined( USE_MORPHNORMALS )\n" +
"\n" +
"	objectNormal = normal;\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef FLIP_SIDED\n" +
"\n" +
"	objectNormal = -objectNormal;\n" +
"\n" +
"#endif\n" +
"\n" +
"vec3 transformedNormal = normalMatrix * objectNormal;\n";
}
