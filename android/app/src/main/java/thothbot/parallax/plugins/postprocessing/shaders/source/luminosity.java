// Generated from luminosity.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class luminosity
{
    public static final String fragment =
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 texel = texture2D( tDiffuse, vUv );\n" +
"\n" +
"	vec3 luma = vec3( 0.299, 0.587, 0.114 );\n" +
"\n" +
"	float v = dot( texel.xyz, luma );\n" +
"\n" +
"	gl_FragColor = vec4( v, v, v, texel.w );\n" +
"\n" +
"}\n";
}
