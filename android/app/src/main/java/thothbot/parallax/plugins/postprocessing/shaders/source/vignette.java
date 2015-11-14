// Generated from vignette.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class vignette
{
    public static final String fragment =
"uniform float offset;\n" +
"uniform float darkness;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	// Eskil's vignette\n" +
"\n" +
"	vec4 texel = texture2D( tDiffuse, vUv );\n" +
"	vec2 uv = ( vUv - vec2( 0.5 ) ) * vec2( offset );\n" +
"	gl_FragColor = vec4( mix( texel.rgb, vec3( 1.0 - darkness ), dot( uv, uv ) ), texel.a );\n" +
"\n" +
"\n" +
"	// alternative version from glfx.js\n" +
"	// this one makes more "dusty" look (as opposed to "burned")\n" +
"\n" +
"	// vec4 color = texture2D( tDiffuse, vUv );\n" +
"	// float dist = distance( vUv, vec2( 0.5 ) );\n" +
"	// color.rgb *= smoothstep( 0.8, offset * 0.799, dist *( darkness + offset ) );\n" +
"	// gl_FragColor = color;\n" +
"}\n";
}
