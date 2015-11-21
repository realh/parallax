// Generated from fog_fragment.glsl

package org.parallax3d.shaders.chunk;

public class fog
{
    public static final String fragment =
"#ifdef USE_FOG\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		float depth = gl_FragDepthEXT / gl_FragCoord.w;\n" +
"\n" +
"	#else\n" +
"\n" +
"		float depth = gl_FragCoord.z / gl_FragCoord.w;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#ifdef FOG_EXP2\n" +
"\n" +
"		const float LOG2 = 1.442695;\n" +
"		float fogFactor = exp2( - fogDensity * fogDensity * depth * depth * LOG2 );\n" +
"		fogFactor = 1.0 - clamp( fogFactor, 0.0, 1.0 );\n" +
"\n" +
"	#else\n" +
"\n" +
"		float fogFactor = smoothstep( fogNear, fogFar, depth );\n" +
"\n" +
"	#endif\n" +
"	\n" +
"	gl_FragColor = mix( gl_FragColor, vec4( fogColor, gl_FragColor.w ), fogFactor );\n" +
"\n" +
"#endif\n";
}
