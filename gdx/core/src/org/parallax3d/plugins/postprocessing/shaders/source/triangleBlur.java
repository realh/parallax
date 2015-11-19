// Generated from triangleBlur.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class triangleBlur
{
    public static final String fragment =
"#define ITERATIONS 10.0\n" +
"\n" +
"uniform sampler2D texture;                           \n" +
"uniform vec2 delta;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"float random( vec3 scale, float seed ) {\n" +
"\n" +
"	// use the fragment position for a different seed per-pixel\n" +
"\n" +
"	return fract( sin( dot( gl_FragCoord.xyz + seed, scale ) ) * 43758.5453 + seed );\n" +
"\n" +
"}\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 color = vec4( 0.0 );\n" +
"\n" +
"	float total = 0.0;\n" +
"\n" +
"	// randomize the lookup values to hide the fixed number of samples\n" +
"\n" +
"	float offset = random( vec3( 12.9898, 78.233, 151.7182 ), 0.0 );\n" +
"\n" +
"	for ( float t = -ITERATIONS; t <= ITERATIONS; t ++ ) {\n" +
"\n" +
"		float percent = ( t + offset - 0.5 ) / ITERATIONS;\n" +
"		float weight = 1.0 - abs( percent );\n" +
"\n" +
"		color += texture2D( texture, vUv + delta * percent ) * weight;\n" +
"		total += weight;\n" +
"\n" +
"	}\n" +
"\n" +
"	gl_FragColor = color / total;\n" +
"\n" +
"}\n";
}
