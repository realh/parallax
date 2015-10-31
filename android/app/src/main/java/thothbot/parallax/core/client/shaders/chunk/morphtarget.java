// Generated from morphtarget_vertex.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class morphtarget
{
    public static final String vertex =
"#ifdef USE_MORPHTARGETS\n" +
"\n" +
"	vec3 morphed = vec3( 0.0 );\n" +
"	morphed += ( morphTarget0 - position ) * morphTargetInfluences[ 0 ];\n" +
"	morphed += ( morphTarget1 - position ) * morphTargetInfluences[ 1 ];\n" +
"	morphed += ( morphTarget2 - position ) * morphTargetInfluences[ 2 ];\n" +
"	morphed += ( morphTarget3 - position ) * morphTargetInfluences[ 3 ];\n" +
"\n" +
"	#ifndef USE_MORPHNORMALS\n" +
"\n" +
"	morphed += ( morphTarget4 - position ) * morphTargetInfluences[ 4 ];\n" +
"	morphed += ( morphTarget5 - position ) * morphTargetInfluences[ 5 ];\n" +
"	morphed += ( morphTarget6 - position ) * morphTargetInfluences[ 6 ];\n" +
"	morphed += ( morphTarget7 - position ) * morphTargetInfluences[ 7 ];\n" +
"\n" +
"	#endif\n" +
"\n" +
"	morphed += position;\n" +
"\n" +
"#endif\n";
}
