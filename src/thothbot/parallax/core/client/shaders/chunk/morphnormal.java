// Generated from morphnormal_vertex.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class morphnormal
{
    public static final String vertex =
"#ifdef USE_MORPHNORMALS\n" +
"\n" +
"	vec3 morphedNormal = vec3( 0.0 );\n" +
"\n" +
"	morphedNormal += ( morphNormal0 - normal ) * morphTargetInfluences[ 0 ];\n" +
"	morphedNormal += ( morphNormal1 - normal ) * morphTargetInfluences[ 1 ];\n" +
"	morphedNormal += ( morphNormal2 - normal ) * morphTargetInfluences[ 2 ];\n" +
"	morphedNormal += ( morphNormal3 - normal ) * morphTargetInfluences[ 3 ];\n" +
"\n" +
"	morphedNormal += normal;\n" +
"\n" +
"#endif\n";
}
