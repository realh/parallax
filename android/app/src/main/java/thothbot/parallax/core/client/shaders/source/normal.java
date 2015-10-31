// Generated from normal.vs and normal.fs

package thothbot.parallax.core.client.shaders.source;

public class normal
{
    public static final String vertex =
"varying vec3 vNormal;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vNormal = normalize( normalMatrix * normal );\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n";

    public static final String fragment =
"uniform float opacity;\n" +
"varying vec3 vNormal;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = vec4( 0.5 * normalize( vNormal ) + 0.5, opacity );\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n";
}
