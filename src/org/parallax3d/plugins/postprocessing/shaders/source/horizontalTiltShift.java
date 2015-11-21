// Generated from horizontalTiltShift.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class horizontalTiltShift
{
    public static final String fragment =
"uniform sampler2D tDiffuse;\n" +
"uniform float h;\n" +
"uniform float r;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 sum = vec4( 0.0 );\n" +
"\n" +
"	float hh = h * abs( r - vUv.y );\n" +
"\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x - 4.0 * hh, vUv.y ) ) * 0.051;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x - 3.0 * hh, vUv.y ) ) * 0.0918;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x - 2.0 * hh, vUv.y ) ) * 0.12245;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x - 1.0 * hh, vUv.y ) ) * 0.1531;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, 		  	 vUv.y ) ) * 0.1633;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x + 1.0 * hh, vUv.y ) ) * 0.1531;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x + 2.0 * hh, vUv.y ) ) * 0.12245;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x + 3.0 * hh, vUv.y ) ) * 0.0918;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x + 4.0 * hh, vUv.y ) ) * 0.051;\n" +
"\n" +
"	gl_FragColor = sum;\n" +
"\n" +
"}\n";
}
