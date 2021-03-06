// Generated from verticalBlur.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class verticalBlur
{
    public static final String fragment =
"uniform sampler2D tDiffuse;\n" +
"uniform float v;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 sum = vec4( 0.0 );\n" +
"\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y - 4.0 * v ) ) * 0.051;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y - 3.0 * v ) ) * 0.0918;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y - 2.0 * v ) ) * 0.12245;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y - 1.0 * v ) ) * 0.1531;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y			  ) ) * 0.1633;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y + 1.0 * v ) ) * 0.1531;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y + 2.0 * v ) ) * 0.12245;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y + 3.0 * v ) ) * 0.0918;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y + 4.0 * v ) ) * 0.051;\n" +
"\n" +
"	gl_FragColor = sum;\n" +
"\n" +
"}\n";
}
