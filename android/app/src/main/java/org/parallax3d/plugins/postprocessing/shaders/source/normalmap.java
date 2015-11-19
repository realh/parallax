// Generated from normalmap.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class normalmap
{
    public static final String fragment =
"uniform float height;\n" +
"uniform vec2 resolution;\n" +
"uniform sampler2D heightMap;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	float val = texture2D( heightMap, vUv ).x;\n" +
"\n" +
"	float valU = texture2D( heightMap, vUv + vec2( 1.0 / resolution.x, 0.0 ) ).x;\n" +
"	float valV = texture2D( heightMap, vUv + vec2( 0.0, 1.0 / resolution.y ) ).x;\n" +
"\n" +
"	gl_FragColor = vec4( ( 0.5 * normalize( vec3( val - valU, val - valV, height  ) ) + 0.5 ), 1.0 );\n" +
"\n" +
"}\n";
}
