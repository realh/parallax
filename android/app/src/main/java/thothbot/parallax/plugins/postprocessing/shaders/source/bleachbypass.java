// Generated from bleachbypass.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class bleachbypass
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
"	vec4 base = texture2D( tDiffuse, vUv );\n" +
"\n" +
"	vec3 lumCoeff = vec3( 0.25, 0.65, 0.1 );\n" +
"	float lum = dot( lumCoeff, base.rgb );\n" +
"	vec3 blend = vec3( lum );\n" +
"\n" +
"	float L = min( 1.0, max( 0.0, 10.0 * ( lum - 0.45 ) ) );\n" +
"\n" +
"	vec3 result1 = 2.0 * base.rgb * blend;\n" +
"	vec3 result2 = 1.0 - 2.0 * ( 1.0 - blend ) * ( 1.0 - base.rgb );\n" +
"\n" +
"	vec3 newColor = mix( result1, result2, L );\n" +
"\n" +
"	float A2 = opacity * base.a;\n" +
"	vec3 mixRGB = A2 * newColor.rgb;\n" +
"	mixRGB += ( ( 1.0 - A2 ) * base.rgb );\n" +
"\n" +
"	gl_FragColor = vec4( mixRGB, base.a );\n" +
"\n" +
"}\n";
}
