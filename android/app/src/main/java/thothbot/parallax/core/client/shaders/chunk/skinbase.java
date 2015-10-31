// Generated from skinbase_vertex.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class skinbase
{
    public static final String vertex =
"#ifdef USE_SKINNING\n" +
"\n" +
"	mat4 boneMatX = getBoneMatrix( skinIndex.x );\n" +
"	mat4 boneMatY = getBoneMatrix( skinIndex.y );\n" +
"	mat4 boneMatZ = getBoneMatrix( skinIndex.z );\n" +
"	mat4 boneMatW = getBoneMatrix( skinIndex.w );\n" +
"\n" +
"#endif\n";
}
