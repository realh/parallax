// Generated from alphamap_fragment.glsl

package org.parallax3d.shaders.chunk;

public class alphamap
{
    public static final String fragment =
"#ifdef USE_ALPHAMAP\n" +
"\n" +
"	gl_FragColor.a *= texture2D( alphaMap, vUv ).g;\n" +
"\n" +
"#endif\n";
}
