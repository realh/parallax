// Generated from convolution.vs and convolution.fs

package thothbot.parallax.plugins.postprocessing.shaders.source;

public class convolution
{
    public static final String vertex =
"uniform vec2 uImageIncrement;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vUv = uv - ( ( KERNEL_SIZE - 1.0 ) / 2.0 ) * uImageIncrement;\n" +
"	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"}\n";

    public static final String fragment =
"uniform float cKernel[ KERNEL_SIZE ];\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"uniform vec2 uImageIncrement;\n" +
"\n" +
"varying vec2 vUv;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec2 imageCoord = vUv;\n" +
"	vec4 sum = vec4( 0.0, 0.0, 0.0, 0.0 );\n" +
"\n" +
"	for( int i = 0; i < KERNEL_SIZE; i ++ ) {\n" +
"\n" +
"		sum += texture2D( tDiffuse, imageCoord ) * cKernel[ i ];\n" +
"		imageCoord += uImageIncrement;\n" +
"\n" +
"	}\n" +
"\n" +
"	gl_FragColor = sum;\n" +
"\n" +
"}\n";
}
