// Generated from brightnessContrast.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class brightnessContrast
{
    public static final String fragment =
"uniform sampler2D tDiffuse;\n" +
"uniform float brightness;\n" +
"uniform float contrast;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = texture2D( tDiffuse, vUv );\n" +
"\n" +
"	gl_FragColor.rgb += brightness;\n" +
"\n" +
"	if (contrast > 0.0) {\n" +
"		gl_FragColor.rgb = (gl_FragColor.rgb - 0.5) / (1.0 - contrast) + 0.5;\n" +
"	} else {\n" +
"		gl_FragColor.rgb = (gl_FragColor.rgb - 0.5) * (1.0 + contrast) + 0.5;\n" +
"	}\n" +
"\n" +
"}\n";
}
