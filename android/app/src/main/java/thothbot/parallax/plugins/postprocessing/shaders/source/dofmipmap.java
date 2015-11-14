// Generated from dofmipmap.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class dofmipmap
{
    public static final String fragment =
"uniform float focus;\n" +
"uniform float maxblur;\n" +
"\n" +
"uniform sampler2D tColor;\n" +
"uniform sampler2D tDepth;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 depth = texture2D( tDepth, vUv );\n" +
"\n" +
"	float factor = depth.x - focus;\n" +
"\n" +
"	vec4 col = texture2D( tColor, vUv, 2.0 * maxblur * abs( focus - depth.x ) );\n" +
"\n" +
"	gl_FragColor = col;\n" +
"	gl_FragColor.a = 1.0;\n" +
"\n" +
"}\n";
}
