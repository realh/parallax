// Generated from blend.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class blend
{
    public static final String fragment =
"uniform float opacity;\n" +
"uniform float mixRatio;\n" +
"\n" +
"uniform sampler2D tDiffuse1;\n" +
"uniform sampler2D tDiffuse2;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 texel1 = texture2D( tDiffuse1, vUv );\n" +
"	vec4 texel2 = texture2D( tDiffuse2, vUv );\n" +
"	gl_FragColor = opacity * mix( texel1, texel2, mixRatio );\n" +
"\n" +
"}\n";
}
