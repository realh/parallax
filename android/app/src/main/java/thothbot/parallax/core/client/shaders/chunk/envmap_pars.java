// Generated from envmap_pars_vertex.glsl and envmap_pars_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class envmap_pars
{
    public static final String vertex =
"#if defined( USE_ENVMAP ) && ! defined( USE_BUMPMAP ) && ! defined( USE_NORMALMAP ) && ! defined( PHONG )\n" +
"\n" +
"	varying vec3 vReflect;\n" +
"\n" +
"	uniform float refractionRatio;\n" +
"	uniform bool useRefract;\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"#ifdef USE_ENVMAP\n" +
"\n" +
"	uniform float reflectivity;\n" +
"	uniform samplerCube envMap;\n" +
"	uniform float flipEnvMap;\n" +
"	uniform int combine;\n" +
"\n" +
"	#if defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( PHONG )\n" +
"\n" +
"		uniform bool useRefract;\n" +
"		uniform float refractionRatio;\n" +
"\n" +
"	#else\n" +
"\n" +
"		varying vec3 vReflect;\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n";
}
