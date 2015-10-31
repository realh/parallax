// Generated from alphatest_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class alphatest
{
    public static final String fragment =
"#ifdef ALPHATEST\n" +
"\n" +
"	if ( gl_FragColor.a < ALPHATEST ) discard;\n" +
"\n" +
"#endif\n";
}
