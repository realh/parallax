// Generated from bumpmap_pars_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class bumpmap_pars
{
    public static final String fragment =
"#ifdef USE_BUMPMAP\n" +
"\n" +
"	uniform sampler2D bumpMap;\n" +
"	uniform float bumpScale;\n" +
"\n" +
"			// Derivative maps - bump mapping unparametrized surfaces by Morten Mikkelsen\n" +
"			//	http://mmikkelsen3d.blogspot.sk/2011/07/derivative-maps.html\n" +
"\n" +
"			// Evaluate the derivative of the height w.r.t. screen-space using forward differencing (listing 2)\n" +
"\n" +
"	vec2 dHdxy_fwd() {\n" +
"\n" +
"		vec2 dSTdx = dFdx( vUv );\n" +
"		vec2 dSTdy = dFdy( vUv );\n" +
"\n" +
"		float Hll = bumpScale * texture2D( bumpMap, vUv ).x;\n" +
"		float dBx = bumpScale * texture2D( bumpMap, vUv + dSTdx ).x - Hll;\n" +
"		float dBy = bumpScale * texture2D( bumpMap, vUv + dSTdy ).x - Hll;\n" +
"\n" +
"		return vec2( dBx, dBy );\n" +
"\n" +
"	}\n" +
"\n" +
"	vec3 perturbNormalArb( vec3 surf_pos, vec3 surf_norm, vec2 dHdxy ) {\n" +
"\n" +
"		vec3 vSigmaX = dFdx( surf_pos );\n" +
"		vec3 vSigmaY = dFdy( surf_pos );\n" +
"		vec3 vN = surf_norm;		// normalized\n" +
"\n" +
"		vec3 R1 = cross( vSigmaY, vN );\n" +
"		vec3 R2 = cross( vN, vSigmaX );\n" +
"\n" +
"		float fDet = dot( vSigmaX, R1 );\n" +
"\n" +
"		vec3 vGrad = sign( fDet ) * ( dHdxy.x * R1 + dHdxy.y * R2 );\n" +
"		return normalize( abs( fDet ) * surf_norm - vGrad );\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n";
}
