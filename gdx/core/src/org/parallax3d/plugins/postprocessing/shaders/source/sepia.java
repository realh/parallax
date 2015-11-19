// Generated from sepia.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class sepia
{
    public static final String fragment =
"uniform float amount;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 color = texture2D( tDiffuse, vUv );\n" +
"	vec3 c = color.rgb;\n" +
"\n" +
"	color.r = dot( c, vec3( 1.0 - 0.607 * amount, 0.769 * amount, 0.189 * amount ) );\n" +
"	color.g = dot( c, vec3( 0.349 * amount, 1.0 - 0.314 * amount, 0.168 * amount ) );\n" +
"	color.b = dot( c, vec3( 0.272 * amount, 0.534 * amount, 1.0 - 0.869 * amount ) );\n" +
"\n" +
"	gl_FragColor = vec4( min( vec3( 1.0 ), color.rgb ), color.a );\n" +
"\n" +
"}\n";
}
