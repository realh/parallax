// Generated from focus.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class focus
{
    public static final String fragment =
"uniform float screenWidth;\n" +
"uniform float screenHeight;\n" +
"uniform float sampleDistance;\n" +
"uniform float waveFactor;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 color, org, tmp, add;\n" +
"	float sample_dist, f;\n" +
"	vec2 vin;\n" +
"	vec2 uv = vUv;\n" +
"\n" +
"	add = color = org = texture2D( tDiffuse, uv );\n" +
"\n" +
"	vin = ( uv - vec2( 0.5 ) ) * vec2( 1.4 );\n" +
"	sample_dist = dot( vin, vin ) * 2.0;\n" +
"\n" +
"	f = ( waveFactor * 100.0 + sample_dist ) * sampleDistance * 4.0;\n" +
"\n" +
"	vec2 sampleSize = vec2(  1.0 / screenWidth, 1.0 / screenHeight ) * vec2( f );\n" +
"\n" +
"	add += tmp = texture2D( tDiffuse, uv + vec2( 0.111964, 0.993712 ) * sampleSize );\n" +
"	if( tmp.b < color.b ) color = tmp;\n" +
"\n" +
"	add += tmp = texture2D( tDiffuse, uv + vec2( 0.846724, 0.532032 ) * sampleSize );\n" +
"	if( tmp.b < color.b ) color = tmp;\n" +
"\n" +
"	add += tmp = texture2D( tDiffuse, uv + vec2( 0.943883, -0.330279 ) * sampleSize );\n" +
"	if( tmp.b < color.b ) color = tmp;\n" +
"\n" +
"	add += tmp = texture2D( tDiffuse, uv + vec2( 0.330279, -0.943883 ) * sampleSize );\n" +
"	if( tmp.b < color.b ) color = tmp;\n" +
"\n" +
"	add += tmp = texture2D( tDiffuse, uv + vec2( -0.532032, -0.846724 ) * sampleSize );\n" +
"	if( tmp.b < color.b ) color = tmp;\n" +
"\n" +
"	add += tmp = texture2D( tDiffuse, uv + vec2( -0.993712, -0.111964 ) * sampleSize );\n" +
"	if( tmp.b < color.b ) color = tmp;\n" +
"\n" +
"	add += tmp = texture2D( tDiffuse, uv + vec2( -0.707107, 0.707107 ) * sampleSize );\n" +
"	if( tmp.b < color.b ) color = tmp;\n" +
"\n" +
"	color = color * vec4( 2.0 ) - ( add / vec4( 8.0 ) );\n" +
"	color = color + ( add / vec4( 8.0 ) - color ) * ( vec4( 1.0 ) - vec4( sample_dist * 0.5 ) );\n" +
"\n" +
"	gl_FragColor = vec4( color.rgb * color.rgb * vec3( 0.95 ) + color.rgb, 1.0 );\n" +
"\n" +
"}\n";
}
