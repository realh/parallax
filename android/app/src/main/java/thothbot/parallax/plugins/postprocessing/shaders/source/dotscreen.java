// Generated from dotscreen.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;


public class dotscreen
{
    public static final String fragment =
"uniform vec2 center;\n" +
"uniform float angle;\n" +
"uniform float scale;\n" +
"uniform vec2 tSize;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"float pattern() {\n" +
"\n" +
"	float s = sin( angle ), c = cos( angle );\n" +
"\n" +
"	vec2 tex = vUv * tSize - center;\n" +
"	vec2 point = vec2( c * tex.x - s * tex.y, s * tex.x + c * tex.y ) * scale;\n" +
"\n" +
"	return ( sin( point.x ) * sin( point.y ) ) * 4.0;\n" +
"\n" +
"}\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 color = texture2D( tDiffuse, vUv );\n" +
"\n" +
"	float average = ( color.r + color.g + color.b ) / 3.0;\n" +
"\n" +
"	gl_FragColor = vec4( vec3( average * 10.0 - 5.0 + pattern() ), color.a );\n" +
"\n" +
"}\n";
}
