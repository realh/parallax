// Generated from ssao.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class ssao
{
    public static final String fragment =
"uniform float cameraNear;\n" +
"uniform float cameraFar;\n" +
"\n" +
"uniform float fogNear;\n" +
"uniform float fogFar;\n" +
"\n" +
"uniform bool fogEnabled;		// attenuate AO with linear fog\n" +
"uniform bool onlyAO; 		// use only ambient occlusion pass?\n" +
"\n" +
"uniform vec2 size;			// texture width, height\n" +
"uniform float aoClamp; 		// depth clamp - reduces haloing at screen edges\n" +
"\n" +
"uniform float lumInfluence;  // how much luminance affects occlusion\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"uniform sampler2D tDepth;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"//#define PI 3.14159265\n" +
"#define DL 2.399963229728653 // PI * ( 3.0 - sqrt( 5.0 ) )\n" +
"#define EULER 2.718281828459045\n" +
"\n" +
"// helpers\n" +
"\n" +
"float width = size.x; 	// texture width\n" +
"float height = size.y; 	// texture height\n" +
"\n" +
"float cameraFarPlusNear = cameraFar + cameraNear;\n" +
"float cameraFarMinusNear = cameraFar - cameraNear;\n" +
"float cameraCoef = 2.0 * cameraNear;\n" +
"\n" +
"			// user variables\n" +
"\n" +
"const int samples = 8; 		// ao sample count\n" +
"const float radius = 5.0; 	// ao radius\n" +
"\n" +
"const bool useNoise = false; 		 // use noise instead of pattern for sample dithering\n" +
"const float noiseAmount = 0.0003; // dithering amount\n" +
"\n" +
"const float diffArea = 0.4; 		// self-shadowing reduction\n" +
"const float gDisplace = 0.4; 	// gauss bell center\n" +
"\n" +
"const vec3 onlyAOColor = vec3( 1.0, 0.7, 0.5 );\n" +
"			//const vec3 onlyAOColor = vec3( 1.0, 1.0, 1.0 );\n" +
"\n" +
"\n" +
"			// RGBA depth\n" +
"\n" +
"float unpackDepth( const in vec4 rgba_depth ) {\n" +
"\n" +
"	const vec4 bit_shift = vec4( 1.0 / ( 256.0 * 256.0 * 256.0 ), 1.0 / ( 256.0 * 256.0 ), 1.0 / 256.0, 1.0 );\n" +
"	float depth = dot( rgba_depth, bit_shift );\n" +
"	return depth;\n" +
"\n" +
"}\n" +
"\n" +
"// generating noise / pattern texture for dithering\n" +
"\n" +
"vec2 rand( const vec2 coord ) {\n" +
"\n" +
"	vec2 noise;\n" +
"\n" +
"	if ( useNoise ) {\n" +
"\n" +
"		float nx = dot ( coord, vec2( 12.9898, 78.233 ) );\n" +
"		float ny = dot ( coord, vec2( 12.9898, 78.233 ) * 2.0 );\n" +
"\n" +
"		noise = clamp( fract ( 43758.5453 * sin( vec2( nx, ny ) ) ), 0.0, 1.0 );\n" +
"\n" +
"	} else {\n" +
"\n" +
"		float ff = fract( 1.0 - coord.s * ( width / 2.0 ) );\n" +
"		float gg = fract( coord.t * ( height / 2.0 ) );\n" +
"\n" +
"		noise = vec2( 0.25, 0.75 ) * vec2( ff ) + vec2( 0.75, 0.25 ) * gg;\n" +
"\n" +
"	}\n" +
"\n" +
"	return ( noise * 2.0  - 1.0 ) * noiseAmount;\n" +
"\n" +
"}\n" +
"\n" +
"float doFog() {\n" +
"\n" +
"	float zdepth = unpackDepth( texture2D( tDepth, vUv ) );\n" +
"	float depth = -cameraFar * cameraNear / ( zdepth * cameraFarMinusNear - cameraFar );\n" +
"\n" +
"	return smoothstep( fogNear, fogFar, depth );\n" +
"\n" +
"}\n" +
"\n" +
"float readDepth( const in vec2 coord ) {\n" +
"\n" +
"	//return ( 2.0 * cameraNear ) / ( cameraFar + cameraNear - unpackDepth( texture2D( tDepth, coord ) ) * ( cameraFar - cameraNear ) );\n" +
"	return cameraCoef / ( cameraFarPlusNear - unpackDepth( texture2D( tDepth, coord ) ) * cameraFarMinusNear );\n" +
"}\n" +
"\n" +
"float compareDepths( const in float depth1, const in float depth2, inout int far ) {\n" +
"\n" +
"	float garea = 2.0; 						 // gauss bell width\n" +
"	float diff = ( depth1 - depth2 ) * 100.0; // depth difference (0-100)\n" +
"\n" +
"	// reduce left bell width to avoid self-shadowing\n" +
"\n" +
"	if ( diff < gDisplace ) {\n" +
"\n" +
"		garea = diffArea;\n" +
"\n" +
"	} else {\n" +
"\n" +
"		far = 1;\n" +
"\n" +
"	}\n" +
"\n" +
"	float dd = diff - gDisplace;\n" +
"	float gauss = pow( EULER, -2.0 * dd * dd / ( garea * garea ) );\n" +
"	return gauss;\n" +
"\n" +
"}\n" +
"\n" +
"float calcAO( float depth, float dw, float dh ) {\n" +
"\n" +
"	float dd = radius - depth * radius;\n" +
"	vec2 vv = vec2( dw, dh );\n" +
"\n" +
"	vec2 coord1 = vUv + dd * vv;\n" +
"	vec2 coord2 = vUv - dd * vv;\n" +
"\n" +
"	float temp1 = 0.0;\n" +
"	float temp2 = 0.0;\n" +
"\n" +
"	int far = 0;\n" +
"	temp1 = compareDepths( depth, readDepth( coord1 ), far );\n" +
"\n" +
"				// DEPTH EXTRAPOLATION\n" +
"\n" +
"	if ( far > 0 ) {\n" +
"\n" +
"		temp2 = compareDepths( readDepth( coord2 ), depth, far );\n" +
"		temp1 += ( 1.0 - temp1 ) * temp2;\n" +
"\n" +
"	}\n" +
"\n" +
"	return temp1;\n" +
"\n" +
"}\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec2 noise = rand( vUv );\n" +
"	float depth = readDepth( vUv );\n" +
"\n" +
"	float tt = clamp( depth, aoClamp, 1.0 );\n" +
"\n" +
"	float w = ( 1.0 / width )  / tt + ( noise.x * ( 1.0 - noise.x ) );\n" +
"	float h = ( 1.0 / height ) / tt + ( noise.y * ( 1.0 - noise.y ) );\n" +
"\n" +
"	float pw;\n" +
"	float ph;\n" +
"\n" +
"	float ao;\n" +
"\n" +
"	float dz = 1.0 / float( samples );\n" +
"	float z = 1.0 - dz / 2.0;\n" +
"	float l = 0.0;\n" +
"\n" +
"	for ( int i = 0; i <= samples; i ++ ) {\n" +
"\n" +
"		float r = sqrt( 1.0 - z );\n" +
"\n" +
"		pw = cos( l ) * r;\n" +
"		ph = sin( l ) * r;\n" +
"		ao += calcAO( depth, pw * w, ph * h );\n" +
"		z = z - dz;\n" +
"		l = l + DL;\n" +
"\n" +
"	}\n" +
"\n" +
"	ao /= float( samples );\n" +
"	ao = 1.0 - ao;\n" +
"\n" +
"	if ( fogEnabled ) {\n" +
"\n" +
"		ao = mix( ao, 1.0, doFog() );\n" +
"\n" +
"	}\n" +
"\n" +
"	vec3 color = texture2D( tDiffuse, vUv ).rgb;\n" +
"\n" +
"	vec3 lumcoeff = vec3( 0.299, 0.587, 0.114 );\n" +
"	float lum = dot( color.rgb, lumcoeff );\n" +
"	vec3 luminance = vec3( lum );\n" +
"\n" +
"	vec3 final = vec3( color * mix( vec3( ao ), vec3( 1.0 ), luminance * lumInfluence ) ); // mix( color * ao, white, luminance )\n" +
"\n" +
"	if ( onlyAO ) {\n" +
"\n" +
"		final = onlyAOColor * vec3( mix( vec3( ao ), vec3( 1.0 ), luminance * lumInfluence ) ); // ambient occlusion only\n" +
"\n" +
"	}\n" +
"\n" +
"	gl_FragColor = vec4( final, 1.0 );\n" +
"\n" +
"}\n";
}
