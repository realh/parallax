// Generated from skin_simple.vs and skin_simple.fs

package uk.co.realh.parallax.shaders;

public class skin_simple
{
    public static final String vertex =
"uniform vec4 offsetRepeat;\n" +
"\n" +
"varying vec3 vNormal;\n" +
"varying vec2 vUv;\n" +
"\n" +
"varying vec3 vViewPosition;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
"	vec4 worldPosition = modelMatrix * vec4( position, 1.0 );\n" +
"\n" +
"	vViewPosition = -mvPosition.xyz;\n" +
"\n" +
"	vNormal = normalize( normalMatrix * normal );\n" +
"\n" +
"	vUv = uv * offsetRepeat.zw + offsetRepeat.xy;\n" +
"\n" +
"	gl_Position = projectionMatrix * mvPosition;\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n";

    public static final String fragment =
"#define USE_BUMPMAP\n" +
"\n" +
"uniform bool enableBump;\n" +
"uniform bool enableSpecular;\n" +
"\n" +
"uniform vec3 ambient;\n" +
"uniform vec3 diffuse;\n" +
"uniform vec3 specular;\n" +
"uniform float opacity;\n" +
"\n" +
"uniform float uRoughness;\n" +
"uniform float uSpecularBrightness;\n" +
"\n" +
"uniform vec3 uWrapRGB;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"uniform sampler2D tBeckmann;\n" +
"\n" +
"uniform sampler2D specularMap;\n" +
"\n" +
"varying vec3 vNormal;\n" +
"varying vec2 vUv;\n" +
"\n" +
"uniform vec3 ambientLightColor;\n" +
"\n" +
"#if MAX_DIR_LIGHTS > 0\n" +
"\n" +
"	uniform vec3 directionalLightColor[ MAX_DIR_LIGHTS ];\n" +
"	uniform vec3 directionalLightDirection[ MAX_DIR_LIGHTS ];\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_HEMI_LIGHTS > 0\n" +
"\n" +
"	uniform vec3 hemisphereLightSkyColor[ MAX_HEMI_LIGHTS ];\n" +
"	uniform vec3 hemisphereLightGroundColor[ MAX_HEMI_LIGHTS ];\n" +
"	uniform vec3 hemisphereLightDirection[ MAX_HEMI_LIGHTS ];\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_POINT_LIGHTS > 0\n" +
"\n" +
"	uniform vec3 pointLightColor[ MAX_POINT_LIGHTS ];\n" +
"	uniform vec3 pointLightPosition[ MAX_POINT_LIGHTS ];\n" +
"	uniform float pointLightDistance[ MAX_POINT_LIGHTS ];\n" +
"\n" +
"#endif\n" +
"\n" +
"varying vec3 vViewPosition;\n" +
"\n" +
"[*]\n" +
"\n" +
"			// Fresnel term\n" +
"\n" +
"float fresnelReflectance( vec3 H, vec3 V, float F0 ) {\n" +
"\n" +
"	float base = 1.0 - dot( V, H );\n" +
"	float exponential = pow( base, 5.0 );\n" +
"\n" +
"	return exponential + F0 * ( 1.0 - exponential );\n" +
"\n" +
"}\n" +
"\n" +
"			// Kelemen/Szirmay-Kalos specular BRDF\n" +
"\n" +
"float KS_Skin_Specular( vec3 N, 		// Bumped surface normal\n" +
"						vec3 L, 		// Points to light\n" +
"						vec3 V, 		// Points to eye\n" +
"						float m,  	// Roughness\n" +
"						float rho_s 	// Specular brightness\n" +
"						) {\n" +
"\n" +
"	float result = 0.0;\n" +
"	float ndotl = dot( N, L );\n" +
"\n" +
"	if( ndotl > 0.0 ) {\n" +
"\n" +
"		vec3 h = L + V; // Unnormalized half-way vector\n" +
"		vec3 H = normalize( h );\n" +
"\n" +
"		float ndoth = dot( N, H );\n" +
"\n" +
"		float PH = pow( 2.0 * texture2D( tBeckmann, vec2( ndoth, m ) ).x, 10.0 );\n" +
"\n" +
"		float F = fresnelReflectance( H, V, 0.028 );\n" +
"		float frSpec = max( PH * F / dot( h, h ), 0.0 );\n" +
"\n" +
"		result = ndotl * rho_s * frSpec; // BRDF * dot(N,L) * rho_s\n" +
"\n" +
"	}\n" +
"\n" +
"	return result;\n" +
"\n" +
"}\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = vec4( vec3( 1.0 ), opacity );\n" +
"\n" +
"	vec4 colDiffuse = texture2D( tDiffuse, vUv );\n" +
"	colDiffuse.rgb *= colDiffuse.rgb;\n" +
"\n" +
"	gl_FragColor = gl_FragColor * colDiffuse;\n" +
"\n" +
"	vec3 normal = normalize( vNormal );\n" +
"	vec3 viewPosition = normalize( vViewPosition );\n" +
"\n" +
"	float specularStrength;\n" +
"\n" +
"	if ( enableSpecular ) {\n" +
"\n" +
"		vec4 texelSpecular = texture2D( specularMap, vUv );\n" +
"		specularStrength = texelSpecular.r;\n" +
"\n" +
"	} else {\n" +
"\n" +
"		specularStrength = 1.0;\n" +
"\n" +
"	}\n" +
"\n" +
"	#ifdef USE_BUMPMAP\n" +
"\n" +
"		if ( enableBump ) normal = perturbNormalArb( -vViewPosition, normal, dHdxy_fwd() );\n" +
"\n" +
"	#endif\n" +
"\n" +
"				// point lights\n" +
"\n" +
"	vec3 specularTotal = vec3( 0.0 );\n" +
"\n" +
"	#if MAX_POINT_LIGHTS > 0\n" +
"\n" +
"		vec3 pointTotal = vec3( 0.0 );\n" +
"\n" +
"		for ( int i = 0; i < MAX_POINT_LIGHTS; i ++ ) {\n" +
"\n" +
"			vec4 lPosition = viewMatrix * vec4( pointLightPosition[ i ], 1.0 );\n" +
"\n" +
"			vec3 lVector = lPosition.xyz + vViewPosition.xyz;\n" +
"\n" +
"			float lDistance = 1.0;\n" +
"\n" +
"			if ( pointLightDistance[ i ] > 0.0 )\n" +
"				lDistance = 1.0 - min( ( length( lVector ) / pointLightDistance[ i ] ), 1.0 );\n" +
"\n" +
"			lVector = normalize( lVector );\n" +
"\n" +
"			float pointDiffuseWeightFull = max( dot( normal, lVector ), 0.0 );\n" +
"			float pointDiffuseWeightHalf = max( 0.5 * dot( normal, lVector ) + 0.5, 0.0 );\n" +
"			vec3 pointDiffuseWeight = mix( vec3 ( pointDiffuseWeightFull ), vec3( pointDiffuseWeightHalf ), uWrapRGB );\n" +
"\n" +
"			float pointSpecularWeight = KS_Skin_Specular( normal, lVector, viewPosition, uRoughness, uSpecularBrightness );\n" +
"\n" +
"			pointTotal    += lDistance * diffuse * pointLightColor[ i ] * pointDiffuseWeight;\n" +
"			specularTotal += lDistance * specular * pointLightColor[ i ] * pointSpecularWeight * specularStrength;\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"				// directional lights\n" +
"\n" +
"	#if MAX_DIR_LIGHTS > 0\n" +
"\n" +
"		vec3 dirTotal = vec3( 0.0 );\n" +
"\n" +
"		for( int i = 0; i < MAX_DIR_LIGHTS; i++ ) {\n" +
"\n" +
"			vec4 lDirection = viewMatrix * vec4( directionalLightDirection[ i ], 0.0 );\n" +
"\n" +
"			vec3 dirVector = normalize( lDirection.xyz );\n" +
"\n" +
"			float dirDiffuseWeightFull = max( dot( normal, dirVector ), 0.0 );\n" +
"			float dirDiffuseWeightHalf = max( 0.5 * dot( normal, dirVector ) + 0.5, 0.0 );\n" +
"			vec3 dirDiffuseWeight = mix( vec3 ( dirDiffuseWeightFull ), vec3( dirDiffuseWeightHalf ), uWrapRGB );\n" +
"\n" +
"			float dirSpecularWeight =  KS_Skin_Specular( normal, dirVector, viewPosition, uRoughness, uSpecularBrightness );\n" +
"\n" +
"			dirTotal 	   += diffuse * directionalLightColor[ i ] * dirDiffuseWeight;\n" +
"			specularTotal += specular * directionalLightColor[ i ] * dirSpecularWeight * specularStrength;\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"				// hemisphere lights\n" +
"\n" +
"	#if MAX_HEMI_LIGHTS > 0\n" +
"\n" +
"		vec3 hemiTotal = vec3( 0.0 );\n" +
"\n" +
"		for ( int i = 0; i < MAX_HEMI_LIGHTS; i ++ ) {\n" +
"\n" +
"			vec4 lDirection = viewMatrix * vec4( hemisphereLightDirection[ i ], 0.0 );\n" +
"			vec3 lVector = normalize( lDirection.xyz );\n" +
"\n" +
"			float dotProduct = dot( normal, lVector );\n" +
"			float hemiDiffuseWeight = 0.5 * dotProduct + 0.5;\n" +
"\n" +
"			hemiTotal += diffuse * mix( hemisphereLightGroundColor[ i ], hemisphereLightSkyColor[ i ], hemiDiffuseWeight );\n" +
"\n" +
"						// specular (sky light)\n" +
"\n" +
"			float hemiSpecularWeight = 0.0;\n" +
"			hemiSpecularWeight += KS_Skin_Specular( normal, lVector, viewPosition, uRoughness, uSpecularBrightness );\n" +
"\n" +
"						// specular (ground light)\n" +
"\n" +
"			vec3 lVectorGround = -lVector;\n" +
"			hemiSpecularWeight += KS_Skin_Specular( normal, lVectorGround, viewPosition, uRoughness, uSpecularBrightness );\n" +
"\n" +
"			specularTotal += specular * mix( hemisphereLightGroundColor[ i ], hemisphereLightSkyColor[ i ], hemiDiffuseWeight ) * hemiSpecularWeight * specularStrength;\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"				// all lights contribution summation\n" +
"\n" +
"	vec3 totalLight = vec3( 0.0 );\n" +
"\n" +
"	#if MAX_DIR_LIGHTS > 0\n" +
"		totalLight += dirTotal;\n" +
"	#endif\n" +
"\n" +
"	#if MAX_POINT_LIGHTS > 0\n" +
"		totalLight += pointTotal;\n" +
"	#endif\n" +
"\n" +
"	#if MAX_HEMI_LIGHTS > 0\n" +
"		totalLight += hemiTotal;\n" +
"	#endif\n" +
"\n" +
"	gl_FragColor.xyz = gl_FragColor.xyz * ( totalLight + ambientLightColor * ambient ) + specularTotal;\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n";
}
