// Generated from hueSaturation.fs

package org.parallax3d.plugins.postprocessing.shaders.source;


public class hueSaturation
{
    public static final String fragment =
"uniform sampler2D tDiffuse;\n" +
"uniform float hue;\n" +
"uniform float saturation;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = texture2D( tDiffuse, vUv );\n" +
"\n" +
"	// hue\n" +
"	float angle = hue * 3.14159265;\n" +
"	float s = sin(angle), c = cos(angle);\n" +
"	vec3 weights = (vec3(2.0 * c, -sqrt(3.0) * s - c, sqrt(3.0) * s - c) + 1.0) / 3.0;\n" +
"	float len = length(gl_FragColor.rgb);\n" +
"	gl_FragColor.rgb = vec3(\n" +
"		dot(gl_FragColor.rgb, weights.xyz),\n" +
"		dot(gl_FragColor.rgb, weights.zxy),\n" +
"		dot(gl_FragColor.rgb, weights.yzx)\n" +
"	);\n" +
"\n" +
"	// saturation\n" +
"	float average = (gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3.0;\n" +
"	if (saturation > 0.0) {\n" +
"		gl_FragColor.rgb += (average - gl_FragColor.rgb) * (1.0 - 1.0 / (1.001 - saturation));\n" +
"	} else {\n" +
"		gl_FragColor.rgb += (average - gl_FragColor.rgb) * (-saturation);\n" +
"	}\n" +
"\n" +
"}\n";
}
