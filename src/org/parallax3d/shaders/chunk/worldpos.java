// Generated from worldpos_vertex.glsl

package org.parallax3d.shaders.chunk;

public class worldpos
{
    public static final String vertex =
"#if defined( USE_ENVMAP ) || defined( PHONG ) || defined( LAMBERT ) || defined ( USE_SHADOWMAP )\n" +
"\n" +
"	#ifdef USE_SKINNING\n" +
"\n" +
"		vec4 worldPosition = modelMatrix * skinned;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#if defined( USE_MORPHTARGETS ) && ! defined( USE_SKINNING )\n" +
"\n" +
"		vec4 worldPosition = modelMatrix * vec4( morphed, 1.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#if ! defined( USE_MORPHTARGETS ) && ! defined( USE_SKINNING )\n" +
"\n" +
"		vec4 worldPosition = modelMatrix * vec4( position, 1.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n";
}
