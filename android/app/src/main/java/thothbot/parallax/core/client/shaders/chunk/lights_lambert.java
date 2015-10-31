// Generated from lights_lambert_vertex.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class lights_lambert
{
    public static final String vertex =
"vLightFront = vec3( 0.0 );\n" +
"\n" +
"#ifdef DOUBLE_SIDED\n" +
"\n" +
"	vLightBack = vec3( 0.0 );\n" +
"\n" +
"#endif\n" +
"\n" +
"transformedNormal = normalize( transformedNormal );\n" +
"\n" +
"#if MAX_DIR_LIGHTS > 0\n" +
"\n" +
"for( int i = 0; i < MAX_DIR_LIGHTS; i ++ ) {\n" +
"\n" +
"	vec4 lDirection = viewMatrix * vec4( directionalLightDirection[ i ], 0.0 );\n" +
"	vec3 dirVector = normalize( lDirection.xyz );\n" +
"\n" +
"	float dotProduct = dot( transformedNormal, dirVector );\n" +
"	vec3 directionalLightWeighting = vec3( max( dotProduct, 0.0 ) );\n" +
"\n" +
"	#ifdef DOUBLE_SIDED\n" +
"\n" +
"		vec3 directionalLightWeightingBack = vec3( max( -dotProduct, 0.0 ) );\n" +
"\n" +
"		#ifdef WRAP_AROUND\n" +
"\n" +
"			vec3 directionalLightWeightingHalfBack = vec3( max( -0.5 * dotProduct + 0.5, 0.0 ) );\n" +
"\n" +
"		#endif\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#ifdef WRAP_AROUND\n" +
"\n" +
"		vec3 directionalLightWeightingHalf = vec3( max( 0.5 * dotProduct + 0.5, 0.0 ) );\n" +
"		directionalLightWeighting = mix( directionalLightWeighting, directionalLightWeightingHalf, wrapRGB );\n" +
"\n" +
"		#ifdef DOUBLE_SIDED\n" +
"\n" +
"			directionalLightWeightingBack = mix( directionalLightWeightingBack, directionalLightWeightingHalfBack, wrapRGB );\n" +
"\n" +
"		#endif\n" +
"\n" +
"	#endif\n" +
"\n" +
"	vLightFront += directionalLightColor[ i ] * directionalLightWeighting;\n" +
"\n" +
"	#ifdef DOUBLE_SIDED\n" +
"\n" +
"		vLightBack += directionalLightColor[ i ] * directionalLightWeightingBack;\n" +
"\n" +
"	#endif\n" +
"\n" +
"}\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_POINT_LIGHTS > 0\n" +
"\n" +
"	for( int i = 0; i < MAX_POINT_LIGHTS; i ++ ) {\n" +
"\n" +
"		vec4 lPosition = viewMatrix * vec4( pointLightPosition[ i ], 1.0 );\n" +
"		vec3 lVector = lPosition.xyz - mvPosition.xyz;\n" +
"\n" +
"		float lDistance = 1.0;\n" +
"		if ( pointLightDistance[ i ] > 0.0 )\n" +
"			lDistance = 1.0 - min( ( length( lVector ) / pointLightDistance[ i ] ), 1.0 );\n" +
"\n" +
"		lVector = normalize( lVector );\n" +
"		float dotProduct = dot( transformedNormal, lVector );\n" +
"\n" +
"		vec3 pointLightWeighting = vec3( max( dotProduct, 0.0 ) );\n" +
"\n" +
"		#ifdef DOUBLE_SIDED\n" +
"\n" +
"			vec3 pointLightWeightingBack = vec3( max( -dotProduct, 0.0 ) );\n" +
"\n" +
"			#ifdef WRAP_AROUND\n" +
"\n" +
"				vec3 pointLightWeightingHalfBack = vec3( max( -0.5 * dotProduct + 0.5, 0.0 ) );\n" +
"\n" +
"			#endif\n" +
"\n" +
"		#endif\n" +
"\n" +
"		#ifdef WRAP_AROUND\n" +
"\n" +
"			vec3 pointLightWeightingHalf = vec3( max( 0.5 * dotProduct + 0.5, 0.0 ) );\n" +
"			pointLightWeighting = mix( pointLightWeighting, pointLightWeightingHalf, wrapRGB );\n" +
"\n" +
"			#ifdef DOUBLE_SIDED\n" +
"\n" +
"				pointLightWeightingBack = mix( pointLightWeightingBack, pointLightWeightingHalfBack, wrapRGB );\n" +
"\n" +
"			#endif\n" +
"\n" +
"		#endif\n" +
"\n" +
"		vLightFront += pointLightColor[ i ] * pointLightWeighting * lDistance;\n" +
"\n" +
"		#ifdef DOUBLE_SIDED\n" +
"\n" +
"			vLightBack += pointLightColor[ i ] * pointLightWeightingBack * lDistance;\n" +
"\n" +
"		#endif\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_SPOT_LIGHTS > 0\n" +
"\n" +
"	for( int i = 0; i < MAX_SPOT_LIGHTS; i ++ ) {\n" +
"\n" +
"		vec4 lPosition = viewMatrix * vec4( spotLightPosition[ i ], 1.0 );\n" +
"		vec3 lVector = lPosition.xyz - mvPosition.xyz;\n" +
"\n" +
"		float spotEffect = dot( spotLightDirection[ i ], normalize( spotLightPosition[ i ] - worldPosition.xyz ) );\n" +
"\n" +
"		if ( spotEffect > spotLightAngleCos[ i ] ) {\n" +
"\n" +
"			spotEffect = max( pow( max( spotEffect, 0.0 ), spotLightExponent[ i ] ), 0.0 );\n" +
"\n" +
"			float lDistance = 1.0;\n" +
"			if ( spotLightDistance[ i ] > 0.0 )\n" +
"				lDistance = 1.0 - min( ( length( lVector ) / spotLightDistance[ i ] ), 1.0 );\n" +
"\n" +
"			lVector = normalize( lVector );\n" +
"\n" +
"			float dotProduct = dot( transformedNormal, lVector );\n" +
"			vec3 spotLightWeighting = vec3( max( dotProduct, 0.0 ) );\n" +
"\n" +
"			#ifdef DOUBLE_SIDED\n" +
"\n" +
"				vec3 spotLightWeightingBack = vec3( max( -dotProduct, 0.0 ) );\n" +
"\n" +
"				#ifdef WRAP_AROUND\n" +
"\n" +
"					vec3 spotLightWeightingHalfBack = vec3( max( -0.5 * dotProduct + 0.5, 0.0 ) );\n" +
"\n" +
"				#endif\n" +
"\n" +
"			#endif\n" +
"\n" +
"			#ifdef WRAP_AROUND\n" +
"\n" +
"				vec3 spotLightWeightingHalf = vec3( max( 0.5 * dotProduct + 0.5, 0.0 ) );\n" +
"				spotLightWeighting = mix( spotLightWeighting, spotLightWeightingHalf, wrapRGB );\n" +
"\n" +
"				#ifdef DOUBLE_SIDED\n" +
"\n" +
"					spotLightWeightingBack = mix( spotLightWeightingBack, spotLightWeightingHalfBack, wrapRGB );\n" +
"\n" +
"				#endif\n" +
"\n" +
"			#endif\n" +
"\n" +
"			vLightFront += spotLightColor[ i ] * spotLightWeighting * lDistance * spotEffect;\n" +
"\n" +
"			#ifdef DOUBLE_SIDED\n" +
"\n" +
"				vLightBack += spotLightColor[ i ] * spotLightWeightingBack * lDistance * spotEffect;\n" +
"\n" +
"			#endif\n" +
"\n" +
"		}\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_HEMI_LIGHTS > 0\n" +
"\n" +
"	for( int i = 0; i < MAX_HEMI_LIGHTS; i ++ ) {\n" +
"\n" +
"		vec4 lDirection = viewMatrix * vec4( hemisphereLightDirection[ i ], 0.0 );\n" +
"		vec3 lVector = normalize( lDirection.xyz );\n" +
"\n" +
"		float dotProduct = dot( transformedNormal, lVector );\n" +
"\n" +
"		float hemiDiffuseWeight = 0.5 * dotProduct + 0.5;\n" +
"		float hemiDiffuseWeightBack = -0.5 * dotProduct + 0.5;\n" +
"\n" +
"		vLightFront += mix( hemisphereLightGroundColor[ i ], hemisphereLightSkyColor[ i ], hemiDiffuseWeight );\n" +
"\n" +
"		#ifdef DOUBLE_SIDED\n" +
"\n" +
"			vLightBack += mix( hemisphereLightGroundColor[ i ], hemisphereLightSkyColor[ i ], hemiDiffuseWeightBack );\n" +
"\n" +
"		#endif\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n" +
"\n" +
"vLightFront = vLightFront * diffuse + ambient * ambientLightColor + emissive;\n" +
"\n" +
"#ifdef DOUBLE_SIDED\n" +
"\n" +
"	vLightBack = vLightBack * diffuse + ambient * ambientLightColor + emissive;\n" +
"\n" +
"#endif\n";
}
