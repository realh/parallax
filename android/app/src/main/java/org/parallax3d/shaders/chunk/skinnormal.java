// Generated from skinnormal_vertex.glsl

package org.parallax3d.shaders.chunk;

public class skinnormal
{
    public static final String vertex =
"#ifdef USE_SKINNING\n" +
"\n" +
"	mat4 skinMatrix = mat4( 0.0 );\n" +
"	skinMatrix += skinWeight.x * boneMatX;\n" +
"	skinMatrix += skinWeight.y * boneMatY;\n" +
"	skinMatrix += skinWeight.z * boneMatZ;\n" +
"	skinMatrix += skinWeight.w * boneMatW;\n" +
"	skinMatrix  = bindMatrixInverse * skinMatrix * bindMatrix;\n" +
"\n" +
"	#ifdef USE_MORPHNORMALS\n" +
"\n" +
"	vec4 skinnedNormal = skinMatrix * vec4( morphedNormal, 0.0 );\n" +
"\n" +
"	#else\n" +
"\n" +
"	vec4 skinnedNormal = skinMatrix * vec4( normal, 0.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n";
}
