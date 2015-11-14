// Generated from fxaa.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class fxaa
{
    public static final String fragment =
"uniform sampler2D tDiffuse;\n" +
"uniform vec2 resolution;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"#define FXAA_REDUCE_MIN   (1.0/128.0)\n" +
"#define FXAA_REDUCE_MUL   (1.0/8.0)\n" +
"#define FXAA_SPAN_MAX     8.0\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec3 rgbNW = texture2D( tDiffuse, ( gl_FragCoord.xy + vec2( -1.0, -1.0 ) ) * resolution ).xyz;\n" +
"	vec3 rgbNE = texture2D( tDiffuse, ( gl_FragCoord.xy + vec2( 1.0, -1.0 ) ) * resolution ).xyz;\n" +
"	vec3 rgbSW = texture2D( tDiffuse, ( gl_FragCoord.xy + vec2( -1.0, 1.0 ) ) * resolution ).xyz;\n" +
"	vec3 rgbSE = texture2D( tDiffuse, ( gl_FragCoord.xy + vec2( 1.0, 1.0 ) ) * resolution ).xyz;\n" +
"	vec4 rgbaM = texture2D( tDiffuse,  gl_FragCoord.xy  * resolution );\n" +
"	vec3 rgbM  = rgbaM.xyz;\n" +
"	float opacity  = rgbaM.w;\n" +
"\n" +
"	vec3 luma = vec3( 0.299, 0.587, 0.114 );\n" +
"\n" +
"	float lumaNW = dot( rgbNW, luma );\n" +
"	float lumaNE = dot( rgbNE, luma );\n" +
"	float lumaSW = dot( rgbSW, luma );\n" +
"	float lumaSE = dot( rgbSE, luma );\n" +
"	float lumaM  = dot( rgbM,  luma );\n" +
"	float lumaMin = min( lumaM, min( min( lumaNW, lumaNE ), min( lumaSW, lumaSE ) ) );\n" +
"	float lumaMax = max( lumaM, max( max( lumaNW, lumaNE) , max( lumaSW, lumaSE ) ) );\n" +
"\n" +
"	vec2 dir;\n" +
"	dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));\n" +
"	dir.y =  ((lumaNW + lumaSW) - (lumaNE + lumaSE));\n" +
"\n" +
"	float dirReduce = max( ( lumaNW + lumaNE + lumaSW + lumaSE ) * ( 0.25 * FXAA_REDUCE_MUL ), FXAA_REDUCE_MIN );\n" +
"\n" +
"	float rcpDirMin = 1.0 / ( min( abs( dir.x ), abs( dir.y ) ) + dirReduce );\n" +
"	dir = min( vec2( FXAA_SPAN_MAX,  FXAA_SPAN_MAX),\n" +
"					  max( vec2(-FXAA_SPAN_MAX, -FXAA_SPAN_MAX),\n" +
"				dir * rcpDirMin)) * resolution;\n" +
"\n" +
"	vec3 rgbA = 0.5 * (\n" +
"		texture2D( tDiffuse, gl_FragCoord.xy  * resolution + dir * ( 1.0 / 3.0 - 0.5 ) ).xyz +\n" +
"		texture2D( tDiffuse, gl_FragCoord.xy  * resolution + dir * ( 2.0 / 3.0 - 0.5 ) ).xyz );\n" +
"\n" +
"	vec3 rgbB = rgbA * 0.5 + 0.25 * (\n" +
"		texture2D( tDiffuse, gl_FragCoord.xy  * resolution + dir * -0.5 ).xyz +\n" +
"		texture2D( tDiffuse, gl_FragCoord.xy  * resolution + dir * 0.5 ).xyz );\n" +
"\n" +
"	float lumaB = dot( rgbB, luma );\n" +
"\n" +
"	if ( ( lumaB < lumaMin ) || ( lumaB > lumaMax ) ) {\n" +
"\n" +
"		gl_FragColor = vec4( rgbA, opacity );\n" +
"\n" +
"	} else {\n" +
"\n" +
"		gl_FragColor = vec4( rgbB, opacity );\n" +
"\n" +
"	}\n" +
"}\n";
}
