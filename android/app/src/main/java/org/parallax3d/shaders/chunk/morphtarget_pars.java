// Generated from morphtarget_pars_vertex.glsl

package org.parallax3d.shaders.chunk;

public class morphtarget_pars
{
    public static final String vertex =
"#ifdef USE_MORPHTARGETS\n" +
"\n" +
"	#ifndef USE_MORPHNORMALS\n" +
"\n" +
"	uniform float morphTargetInfluences[ 8 ];\n" +
"\n" +
"	#else\n" +
"\n" +
"	uniform float morphTargetInfluences[ 4 ];\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n";
}
