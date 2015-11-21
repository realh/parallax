// Generated from beckmann.vs and beckmann.fs

package uk.co.realh.parallax.shaders;

public class beckmann
{
    public static final String vertex =
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vUv = uv;\n" +
"	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"}\n";

    public static final String fragment =
"varying vec2 vUv;\n" +
"\n" +
"float PHBeckmann( float ndoth, float m ) {\n" +
"\n" +
"	float alpha = acos( ndoth );\n" +
"	float ta = tan( alpha );\n" +
"\n" +
"	float val = 1.0 / ( m * m * pow( ndoth, 4.0 ) ) * exp( -( ta * ta ) / ( m * m ) );\n" +
"	return val;\n" +
"\n" +
"}\n" +
"\n" +
"float KSTextureCompute( vec2 tex ) {\n" +
"\n" +
"	// Scale the value to fit within [0,1]  invert upon lookup.\n" +
"\n" +
"	return 0.5 * pow( PHBeckmann( tex.x, tex.y ), 0.1 );\n" +
"\n" +
"}\n" +
"\n" +
"void main() {\n" +
"\n" +
"	float x = KSTextureCompute( vUv );\n" +
"\n" +
"	gl_FragColor = vec4( x, x, x, 1.0 );\n" +
"\n" +
"}\n";
}
