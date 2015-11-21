// Generated from normalmap_pars_fragment.glsl

package org.parallax3d.shaders.chunk;

public class normalmap_pars
{
    public static final String fragment =
"#ifdef USE_NORMALMAP\n" +
"\n" +
"	uniform sampler2D normalMap;\n" +
"	uniform vec2 normalScale;\n" +
"\n" +
"			// Per-Pixel Tangent Space Normal Mapping\n" +
"			// http://hacksoflife.blogspot.ch/2009/11/per-pixel-tangent-space-normal-mapping.html\n" +
"\n" +
"	vec3 perturbNormal2Arb( vec3 eye_pos, vec3 surf_norm ) {\n" +
"\n" +
"		vec3 q0 = dFdx( eye_pos.xyz );\n" +
"		vec3 q1 = dFdy( eye_pos.xyz );\n" +
"		vec2 st0 = dFdx( vUv.st );\n" +
"		vec2 st1 = dFdy( vUv.st );\n" +
"\n" +
"		vec3 S = normalize( q0 * st1.t - q1 * st0.t );\n" +
"		vec3 T = normalize( -q0 * st1.s + q1 * st0.s );\n" +
"		vec3 N = normalize( surf_norm );\n" +
"\n" +
"		vec3 mapN = texture2D( normalMap, vUv ).xyz * 2.0 - 1.0;\n" +
"		mapN.xy = normalScale * mapN.xy;\n" +
"		mat3 tsn = mat3( S, T, N );\n" +
"		return normalize( tsn * mapN );\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n";
}
