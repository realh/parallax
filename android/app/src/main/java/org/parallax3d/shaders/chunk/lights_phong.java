// Generated from lights_phong_vertex.glsl and lights_phong_fragment.glsl

package org.parallax3d.shaders.chunk;

public class lights_phong
{
    public static final String vertex =
"#if MAX_SPOT_LIGHTS > 0 || defined( USE_BUMPMAP ) || defined( USE_ENVMAP )\n" +
"\n" +
"	vWorldPosition = worldPosition.xyz;\n" +
"\n" +
"#endif\n";

    public static final String fragment =
"vec3 normal = normalize( vNormal );\n" +
"vec3 viewPosition = normalize( vViewPosition );\n" +
"\n" +
"#ifdef DOUBLE_SIDED\n" +
"\n" +
"	normal = normal * ( -1.0 + 2.0 * float( gl_FrontFacing ) );\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef USE_NORMALMAP\n" +
"\n" +
"	normal = perturbNormal2Arb( -vViewPosition, normal );\n" +
"\n" +
"#elif defined( USE_BUMPMAP )\n" +
"\n" +
"	normal = perturbNormalArb( -vViewPosition, normal, dHdxy_fwd() );\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_POINT_LIGHTS > 0\n" +
"\n" +
"	vec3 pointDiffuse = vec3( 0.0 );\n" +
"	vec3 pointSpecular = vec3( 0.0 );\n" +
"\n" +
"	for ( int i = 0; i < MAX_POINT_LIGHTS; i ++ ) {\n" +
"\n" +
"		vec4 lPosition = viewMatrix * vec4( pointLightPosition[ i ], 1.0 );\n" +
"		vec3 lVector = lPosition.xyz + vViewPosition.xyz;\n" +
"\n" +
"		float lDistance = 1.0;\n" +
"		if ( pointLightDistance[ i ] > 0.0 )\n" +
"			lDistance = 1.0 - min( ( length( lVector ) / pointLightDistance[ i ] ), 1.0 );\n" +
"\n" +
"		lVector = normalize( lVector );\n" +
"\n" +
"				// diffuse\n" +
"\n" +
"		float dotProduct = dot( normal, lVector );\n" +
"\n" +
"		#ifdef WRAP_AROUND\n" +
"\n" +
"			float pointDiffuseWeightFull = max( dotProduct, 0.0 );\n" +
"			float pointDiffuseWeightHalf = max( 0.5 * dotProduct + 0.5, 0.0 );\n" +
"\n" +
"			vec3 pointDiffuseWeight = mix( vec3( pointDiffuseWeightFull ), vec3( pointDiffuseWeightHalf ), wrapRGB );\n" +
"\n" +
"		#else\n" +
"\n" +
"			float pointDiffuseWeight = max( dotProduct, 0.0 );\n" +
"\n" +
"		#endif\n" +
"\n" +
"		pointDiffuse += diffuse * pointLightColor[ i ] * pointDiffuseWeight * lDistance;\n" +
"\n" +
"				// specular\n" +
"\n" +
"		vec3 pointHalfVector = normalize( lVector + viewPosition );\n" +
"		float pointDotNormalHalf = max( dot( normal, pointHalfVector ), 0.0 );\n" +
"		float pointSpecularWeight = specularStrength * max( pow( pointDotNormalHalf, shininess ), 0.0 );\n" +
"\n" +
"		float specularNormalization = ( shininess + 2.0 ) / 8.0;\n" +
"\n" +
"		vec3 schlick = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( lVector, pointHalfVector ), 0.0 ), 5.0 );\n" +
"		pointSpecular += schlick * pointLightColor[ i ] * pointSpecularWeight * pointDiffuseWeight * lDistance * specularNormalization;\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_SPOT_LIGHTS > 0\n" +
"\n" +
"	vec3 spotDiffuse = vec3( 0.0 );\n" +
"	vec3 spotSpecular = vec3( 0.0 );\n" +
"\n" +
"	for ( int i = 0; i < MAX_SPOT_LIGHTS; i ++ ) {\n" +
"\n" +
"		vec4 lPosition = viewMatrix * vec4( spotLightPosition[ i ], 1.0 );\n" +
"		vec3 lVector = lPosition.xyz + vViewPosition.xyz;\n" +
"\n" +
"		float lDistance = 1.0;\n" +
"		if ( spotLightDistance[ i ] > 0.0 )\n" +
"			lDistance = 1.0 - min( ( length( lVector ) / spotLightDistance[ i ] ), 1.0 );\n" +
"\n" +
"		lVector = normalize( lVector );\n" +
"\n" +
"		float spotEffect = dot( spotLightDirection[ i ], normalize( spotLightPosition[ i ] - vWorldPosition ) );\n" +
"\n" +
"		if ( spotEffect > spotLightAngleCos[ i ] ) {\n" +
"\n" +
"			spotEffect = max( pow( max( spotEffect, 0.0 ), spotLightExponent[ i ] ), 0.0 );\n" +
"\n" +
"					// diffuse\n" +
"\n" +
"			float dotProduct = dot( normal, lVector );\n" +
"\n" +
"			#ifdef WRAP_AROUND\n" +
"\n" +
"				float spotDiffuseWeightFull = max( dotProduct, 0.0 );\n" +
"				float spotDiffuseWeightHalf = max( 0.5 * dotProduct + 0.5, 0.0 );\n" +
"\n" +
"				vec3 spotDiffuseWeight = mix( vec3( spotDiffuseWeightFull ), vec3( spotDiffuseWeightHalf ), wrapRGB );\n" +
"\n" +
"			#else\n" +
"\n" +
"				float spotDiffuseWeight = max( dotProduct, 0.0 );\n" +
"\n" +
"			#endif\n" +
"\n" +
"			spotDiffuse += diffuse * spotLightColor[ i ] * spotDiffuseWeight * lDistance * spotEffect;\n" +
"\n" +
"					// specular\n" +
"\n" +
"			vec3 spotHalfVector = normalize( lVector + viewPosition );\n" +
"			float spotDotNormalHalf = max( dot( normal, spotHalfVector ), 0.0 );\n" +
"			float spotSpecularWeight = specularStrength * max( pow( spotDotNormalHalf, shininess ), 0.0 );\n" +
"\n" +
"			float specularNormalization = ( shininess + 2.0 ) / 8.0;\n" +
"\n" +
"			vec3 schlick = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( lVector, spotHalfVector ), 0.0 ), 5.0 );\n" +
"			spotSpecular += schlick * spotLightColor[ i ] * spotSpecularWeight * spotDiffuseWeight * lDistance * specularNormalization * spotEffect;\n" +
"\n" +
"		}\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_DIR_LIGHTS > 0\n" +
"\n" +
"	vec3 dirDiffuse = vec3( 0.0 );\n" +
"	vec3 dirSpecular = vec3( 0.0 );\n" +
"\n" +
"	for( int i = 0; i < MAX_DIR_LIGHTS; i ++ ) {\n" +
"\n" +
"		vec4 lDirection = viewMatrix * vec4( directionalLightDirection[ i ], 0.0 );\n" +
"		vec3 dirVector = normalize( lDirection.xyz );\n" +
"\n" +
"				// diffuse\n" +
"\n" +
"		float dotProduct = dot( normal, dirVector );\n" +
"\n" +
"		#ifdef WRAP_AROUND\n" +
"\n" +
"			float dirDiffuseWeightFull = max( dotProduct, 0.0 );\n" +
"			float dirDiffuseWeightHalf = max( 0.5 * dotProduct + 0.5, 0.0 );\n" +
"\n" +
"			vec3 dirDiffuseWeight = mix( vec3( dirDiffuseWeightFull ), vec3( dirDiffuseWeightHalf ), wrapRGB );\n" +
"\n" +
"		#else\n" +
"\n" +
"			float dirDiffuseWeight = max( dotProduct, 0.0 );\n" +
"\n" +
"		#endif\n" +
"\n" +
"		dirDiffuse += diffuse * directionalLightColor[ i ] * dirDiffuseWeight;\n" +
"\n" +
"		// specular\n" +
"\n" +
"		vec3 dirHalfVector = normalize( dirVector + viewPosition );\n" +
"		float dirDotNormalHalf = max( dot( normal, dirHalfVector ), 0.0 );\n" +
"		float dirSpecularWeight = specularStrength * max( pow( dirDotNormalHalf, shininess ), 0.0 );\n" +
"\n" +
"		/*\n" +
"		// fresnel term from skin shader\n" +
"		const float F0 = 0.128;\n" +
"\n" +
"		float base = 1.0 - dot( viewPosition, dirHalfVector );\n" +
"		float exponential = pow( base, 5.0 );\n" +
"\n" +
"		float fresnel = exponential + F0 * ( 1.0 - exponential );\n" +
"		*/\n" +
"\n" +
"		/*\n" +
"		// fresnel term from fresnel shader\n" +
"		const float mFresnelBias = 0.08;\n" +
"		const float mFresnelScale = 0.3;\n" +
"		const float mFresnelPower = 5.0;\n" +
"\n" +
"		float fresnel = mFresnelBias + mFresnelScale * pow( 1.0 + dot( normalize( -viewPosition ), normal ), mFresnelPower );\n" +
"		*/\n" +
"\n" +
"		float specularNormalization = ( shininess + 2.0 ) / 8.0;\n" +
"\n" +
"		// 		dirSpecular += specular * directionalLightColor[ i ] * dirSpecularWeight * dirDiffuseWeight * specularNormalization * fresnel;\n" +
"\n" +
"		vec3 schlick = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( dirVector, dirHalfVector ), 0.0 ), 5.0 );\n" +
"		dirSpecular += schlick * directionalLightColor[ i ] * dirSpecularWeight * dirDiffuseWeight * specularNormalization;\n" +
"\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_HEMI_LIGHTS > 0\n" +
"\n" +
"	vec3 hemiDiffuse = vec3( 0.0 );\n" +
"	vec3 hemiSpecular = vec3( 0.0 );\n" +
"\n" +
"	for( int i = 0; i < MAX_HEMI_LIGHTS; i ++ ) {\n" +
"\n" +
"		vec4 lDirection = viewMatrix * vec4( hemisphereLightDirection[ i ], 0.0 );\n" +
"		vec3 lVector = normalize( lDirection.xyz );\n" +
"\n" +
"		// diffuse\n" +
"\n" +
"		float dotProduct = dot( normal, lVector );\n" +
"		float hemiDiffuseWeight = 0.5 * dotProduct + 0.5;\n" +
"\n" +
"		vec3 hemiColor = mix( hemisphereLightGroundColor[ i ], hemisphereLightSkyColor[ i ], hemiDiffuseWeight );\n" +
"\n" +
"		hemiDiffuse += diffuse * hemiColor;\n" +
"\n" +
"		// specular (sky light)\n" +
"\n" +
"		vec3 hemiHalfVectorSky = normalize( lVector + viewPosition );\n" +
"		float hemiDotNormalHalfSky = 0.5 * dot( normal, hemiHalfVectorSky ) + 0.5;\n" +
"		float hemiSpecularWeightSky = specularStrength * max( pow( max( hemiDotNormalHalfSky, 0.0 ), shininess ), 0.0 );\n" +
"\n" +
"		// specular (ground light)\n" +
"\n" +
"		vec3 lVectorGround = -lVector;\n" +
"\n" +
"		vec3 hemiHalfVectorGround = normalize( lVectorGround + viewPosition );\n" +
"		float hemiDotNormalHalfGround = 0.5 * dot( normal, hemiHalfVectorGround ) + 0.5;\n" +
"		float hemiSpecularWeightGround = specularStrength * max( pow( max( hemiDotNormalHalfGround, 0.0 ), shininess ), 0.0 );\n" +
"\n" +
"		float dotProductGround = dot( normal, lVectorGround );\n" +
"\n" +
"		float specularNormalization = ( shininess + 2.0 ) / 8.0;\n" +
"\n" +
"		vec3 schlickSky = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( lVector, hemiHalfVectorSky ), 0.0 ), 5.0 );\n" +
"		vec3 schlickGround = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( lVectorGround, hemiHalfVectorGround ), 0.0 ), 5.0 );\n" +
"		hemiSpecular += hemiColor * specularNormalization * ( schlickSky * hemiSpecularWeightSky * max( dotProduct, 0.0 ) + schlickGround * hemiSpecularWeightGround * max( dotProductGround, 0.0 ) );\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n" +
"\n" +
"vec3 totalDiffuse = vec3( 0.0 );\n" +
"vec3 totalSpecular = vec3( 0.0 );\n" +
"\n" +
"#if MAX_DIR_LIGHTS > 0\n" +
"\n" +
"	totalDiffuse += dirDiffuse;\n" +
"	totalSpecular += dirSpecular;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_HEMI_LIGHTS > 0\n" +
"\n" +
"	totalDiffuse += hemiDiffuse;\n" +
"	totalSpecular += hemiSpecular;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_POINT_LIGHTS > 0\n" +
"\n" +
"	totalDiffuse += pointDiffuse;\n" +
"	totalSpecular += pointSpecular;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_SPOT_LIGHTS > 0\n" +
"\n" +
"	totalDiffuse += spotDiffuse;\n" +
"	totalSpecular += spotSpecular;\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef METAL\n" +
"\n" +
"	gl_FragColor.xyz = gl_FragColor.xyz * ( emissive + totalDiffuse + ambientLightColor * ambient + totalSpecular );\n" +
"\n" +
"#else\n" +
"\n" +
"	gl_FragColor.xyz = gl_FragColor.xyz * ( emissive + totalDiffuse + ambientLightColor * ambient ) + totalSpecular;\n" +
"\n" +
"#endif\n";
}
