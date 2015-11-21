// Generated from map_particle_fragment.glsl

package org.parallax3d.shaders.chunk;

public class map_particle
{
    public static final String fragment =
"#ifdef USE_MAP\n" +
"\n" +
"	gl_FragColor = gl_FragColor * texture2D( map, vec2( gl_PointCoord.x, 1.0 - gl_PointCoord.y ) );\n" +
"\n" +
"#endif\n";
}
