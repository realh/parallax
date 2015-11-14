// Generated from verticalTiltShift.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class verticalTiltShift
{
    public static final String fragment =
"uniform sampler2D tDiffuse;\n" +
"uniform float v;\n" +
"uniform float r;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 sum = vec4( 0.0 );\n" +
"\n" +
"	float vv = v * abs( r - vUv.y );\n" +
"\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y - 4.0 * vv ) ) * 0.051;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y - 3.0 * vv ) ) * 0.0918;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y - 2.0 * vv ) ) * 0.12245;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y - 1.0 * vv ) ) * 0.1531;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y			   ) ) * 0.1633;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y + 1.0 * vv ) ) * 0.1531;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y + 2.0 * vv ) ) * 0.12245;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y + 3.0 * vv ) ) * 0.0918;\n" +
"	sum += texture2D( tDiffuse, vec2( vUv.x, vUv.y + 4.0 * vv ) ) * 0.051;\n" +
"\n" +
"	gl_FragColor = sum;\n" +
"\n" +
"}\n";
}
