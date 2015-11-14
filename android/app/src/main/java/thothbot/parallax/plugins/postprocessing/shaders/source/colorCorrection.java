// Generated from colorCorrection.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class colorCorrection
{
    public static final String fragment =
"uniform sampler2D tDiffuse;\n" +
"uniform vec3 powRGB;\n" +
"uniform vec3 mulRGB;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = texture2D( tDiffuse, vUv );\n" +
"	gl_FragColor.rgb = mulRGB * pow( gl_FragColor.rgb, powRGB );\n" +
"\n" +
"}\n";
}
