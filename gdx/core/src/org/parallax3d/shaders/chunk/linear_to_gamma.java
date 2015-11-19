// Generated from linear_to_gamma_fragment.glsl

package org.parallax3d.shaders.chunk;

public class linear_to_gamma
{
    public static final String fragment =
"#ifdef GAMMA_OUTPUT\n" +
"\n" +
"	gl_FragColor.xyz = sqrt( gl_FragColor.xyz );\n" +
"\n" +
"#endif\n";
}
