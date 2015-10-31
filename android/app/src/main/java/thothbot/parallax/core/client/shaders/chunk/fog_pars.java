// Generated from fog_pars_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class fog_pars
{
    public static final String fragment =
"#ifdef USE_FOG\n" +
"\n" +
"	uniform vec3 fogColor;\n" +
"\n" +
"	#ifdef FOG_EXP2\n" +
"\n" +
"		uniform float fogDensity;\n" +
"\n" +
"	#else\n" +
"\n" +
"		uniform float fogNear;\n" +
"		uniform float fogFar;\n" +
"	#endif\n" +
"\n" +
"#endif\n";
}
