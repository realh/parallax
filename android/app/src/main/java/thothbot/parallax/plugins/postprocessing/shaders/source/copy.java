// Generated from copy.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class copy
{
    public static final String fragment =
"uniform float opacity;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 texel = texture2D( tDiffuse, vUv );\n" +
"	gl_FragColor = opacity * texel;\n" +
"\n" +
"}\n";
}
