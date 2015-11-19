// Generated from colorify.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class colorify
{
    public static final String fragment =
"uniform vec3 color;\n" +
"uniform sampler2D tDiffuse;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 texel = texture2D( tDiffuse, vUv );\n" +
"\n" +
"	vec3 luma = vec3( 0.299, 0.587, 0.114 );\n" +
"	float v = dot( texel.xyz, luma );\n" +
"\n" +
"	gl_FragColor = vec4( v * color, texel.w );\n" +
"\n" +
"}\n";
}
