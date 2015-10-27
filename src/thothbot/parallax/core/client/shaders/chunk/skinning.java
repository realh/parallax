// Generated from skinning_vertex.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class skinning
{
    public static final String vertex =
"#ifdef USE_SKINNING\n" +
"\n" +
"	#ifdef USE_MORPHTARGETS\n" +
"\n" +
"	vec4 skinVertex = bindMatrix * vec4( morphed, 1.0 );\n" +
"\n" +
"	#else\n" +
"\n" +
"	vec4 skinVertex = bindMatrix * vec4( position, 1.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	vec4 skinned = vec4( 0.0 );\n" +
"	skinned += boneMatX * skinVertex * skinWeight.x;\n" +
"	skinned += boneMatY * skinVertex * skinWeight.y;\n" +
"	skinned += boneMatZ * skinVertex * skinWeight.z;\n" +
"	skinned += boneMatW * skinVertex * skinWeight.w;\n" +
"	skinned  = bindMatrixInverse * skinned;\n" +
"\n" +
"#endif\n";
}
