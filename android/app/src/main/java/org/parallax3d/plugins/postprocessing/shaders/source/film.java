// Generated from film.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class film
{
    public static final String fragment =
"// control parameter\n" +
"uniform float time;\n" +
"\n" +
"uniform bool grayscale;\n" +
"\n" +
"// noise effect intensity value (0 = no effect, 1 = full effect)\n" +
"uniform float nIntensity;\n" +
"\n" +
"// scanlines effect intensity value (0 = no effect, 1 = full effect)\n" +
"uniform float sIntensity;\n" +
"\n" +
"// scanlines effect count value (0 = no effect, 4096 = full effect)\n" +
"uniform float sCount;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	// sample the source\n" +
"	vec4 cTextureScreen = texture2D( tDiffuse, vUv );\n" +
"\n" +
"	// make some noise\n" +
"	float x = vUv.x * vUv.y * time *  1000.0;\n" +
"	x = mod( x, 13.0 ) * mod( x, 123.0 );\n" +
"	float dx = mod( x, 0.01 );\n" +
"\n" +
"	// add noise\n" +
"	vec3 cResult = cTextureScreen.rgb + cTextureScreen.rgb * clamp( 0.1 + dx * 100.0, 0.0, 1.0 );\n" +
"\n" +
"	// get us a sine and cosine\n" +
"	vec2 sc = vec2( sin( vUv.y * sCount ), cos( vUv.y * sCount ) );\n" +
"\n" +
"	// add scanlines\n" +
"	cResult += cTextureScreen.rgb * vec3( sc.x, sc.y, sc.x ) * sIntensity;\n" +
"\n" +
"	// interpolate between source and result by intensity\n" +
"	cResult = cTextureScreen.rgb + clamp( nIntensity, 0.0,1.0 ) * ( cResult - cTextureScreen.rgb );\n" +
"\n" +
"	// convert to grayscale if desired\n" +
"	if( grayscale ) {\n" +
"\n" +
"		cResult = vec3( cResult.r * 0.3 + cResult.g * 0.59 + cResult.b * 0.11 );\n" +
"\n" +
"	}\n" +
"\n" +
"	gl_FragColor =  vec4( cResult, cTextureScreen.a );\n" +
"\n" +
"}\n";
}
