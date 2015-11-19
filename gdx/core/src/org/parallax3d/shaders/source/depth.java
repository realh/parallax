// Generated from depth.vs and depth.fs

package org.parallax3d.shaders.source;

public class depth
{
    public static final String vertex =
"[*]\n" +
"				\n" +
"void main() {\n" +
"				\n" +
"[*]\n" +
"				\n" +
"}\n";

    public static final String fragment =
"uniform float mNear;\n" +
"uniform float mFar;\n" +
"uniform float opacity;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]\n" +
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
"	float color = 1.0 - smoothstep( mNear, mFar, depth );\n" +
"	gl_FragColor = vec4( vec3( color ), opacity );\n" +
"\n" +
"}\n";
}
