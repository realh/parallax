// Generated from normalmap.vs and normalmap.fs

package org.parallax3d.shaders.source;

public class normalmap
{
    public static final String vertex =
"attribute vec4 tangent;\n" +
"\n" +
"uniform vec2 uOffset;\n" +
"uniform vec2 uRepeat;\n" +
"\n" +
"uniform bool enableDisplacement;\n" +
"\n" +
"#ifdef VERTEX_TEXTURES\n" +
"\n" +
"	uniform sampler2D tDisplacement;\n" +
"	uniform float uDisplacementScale;\n" +
"	uniform float uDisplacementBias;\n" +
"\n" +
"#endif\n" +
"\n" +
"varying vec3 vTangent;\n" +
"varying vec3 vBinormal;\n" +
"varying vec3 vNormal;\n" +
"varying vec2 vUv;\n" +
"\n" +
"varying vec3 vWorldPosition;\n" +
"varying vec3 vViewPosition;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]\n" +
"\n" +
"	// normal, tangent and binormal vectors\n" +
"\n" +
"	#ifdef USE_SKINNING\n" +
"\n" +
"		vNormal = normalize( normalMatrix * skinnedNormal.xyz );\n" +
"\n" +
"		vec4 skinnedTangent = skinMatrix * vec4( tangent.xyz, 0.0 );\n" +
"		vTangent = normalize( normalMatrix * skinnedTangent.xyz );\n" +
"\n" +
"	#else\n" +
"\n" +
"		vNormal = normalize( normalMatrix * normal );\n" +
"		vTangent = normalize( normalMatrix * tangent.xyz );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	vBinormal = normalize( cross( vNormal, vTangent ) * tangent.w );\n" +
"\n" +
"	vUv = uv * uRepeat + uOffset;\n" +
"\n" +
"	// displacement mapping\n" +
"\n" +
"	vec3 displacedPosition;\n" +
"\n" +
"	#ifdef VERTEX_TEXTURES\n" +
"\n" +
"		if ( enableDisplacement ) {\n" +
"\n" +
"			vec3 dv = texture2D( tDisplacement, uv ).xyz;\n" +
"			float df = uDisplacementScale * dv.x + uDisplacementBias;\n" +
"			displacedPosition = position + normalize( normal ) * df;\n" +
"\n" +
"		} else {\n" +
"\n" +
"			#ifdef USE_SKINNING\n" +
"\n" +
"				vec4 skinVertex = bindMatrix * vec4( position, 1.0 );\n" +
"\n" +
"				vec4 skinned = vec4( 0.0 );\n" +
"				skinned += boneMatX * skinVertex * skinWeight.x;\n" +
"				skinned += boneMatY * skinVertex * skinWeight.y;\n" +
"				skinned += boneMatZ * skinVertex * skinWeight.z;\n" +
"				skinned += boneMatW * skinVertex * skinWeight.w;\n" +
"				skinned  = bindMatrixInverse * skinned;\n" +
"\n" +
"				displacedPosition = skinned.xyz;\n" +
"\n" +
"			#else\n" +
"\n" +
"				displacedPosition = position;\n" +
"\n" +
"			#endif\n" +
"\n" +
"		}\n" +
"\n" +
"	#else\n" +
"\n" +
"		#ifdef USE_SKINNING\n" +
"\n" +
"			vec4 skinVertex = bindMatrix * vec4( position, 1.0 );\n" +
"\n" +
"			vec4 skinned = vec4( 0.0 );\n" +
"			skinned += boneMatX * skinVertex * skinWeight.x;\n" +
"			skinned += boneMatY * skinVertex * skinWeight.y;\n" +
"			skinned += boneMatZ * skinVertex * skinWeight.z;\n" +
"			skinned += boneMatW * skinVertex * skinWeight.w;\n" +
"			skinned  = bindMatrixInverse * skinned;\n" +
"\n" +
"			displacedPosition = skinned.xyz;\n" +
"\n" +
"		#else\n" +
"\n" +
"			displacedPosition = position;\n" +
"\n" +
"		#endif\n" +
"\n" +
"	#endif\n" +
"\n" +
"	//\n" +
"\n" +
"	vec4 mvPosition = modelViewMatrix * vec4( displacedPosition, 1.0 );\n" +
"	vec4 worldPosition = modelMatrix * vec4( displacedPosition, 1.0 );\n" +
"\n" +
"	gl_Position = projectionMatrix * mvPosition;\n" +
"\n" +
"[*]\n" +
"\n" +
"	//\n" +
"\n" +
"	vWorldPosition = worldPosition.xyz;\n" +
"	vViewPosition = -mvPosition.xyz;\n" +
"\n" +
"	// shadows\n" +
"\n" +
"	#ifdef USE_SHADOWMAP\n" +
"\n" +
"		for( int i = 0; i < MAX_SHADOWS; i ++ ) {\n" +
"\n" +
"			vShadowCoord[ i ] = shadowMatrix[ i ] * worldPosition;\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"}\n";

    public static final String fragment =
"uniform vec3 ambient;\n" +
"uniform vec3 diffuse;\n" +
"uniform vec3 specular;\n" +
"uniform float shininess;\n" +
"uniform float opacity;\n" +
"\n" +
"uniform bool enableDiffuse;\n" +
"uniform bool enableSpecular;\n" +
"uniform bool enableAO;\n" +
"uniform bool enableReflection;\n" +
"\n" +
"uniform sampler2D tDiffuse;\n" +
"uniform sampler2D tNormal;\n" +
"uniform sampler2D tSpecular;\n" +
"uniform sampler2D tAO;\n" +
"\n" +
"uniform samplerCube tCube;\n" +
"\n" +
"uniform vec2 uNormalScale;\n" +
"\n" +
"uniform bool useRefract;\n" +
"uniform float refractionRatio;\n" +
"uniform float reflectivity;\n" +
"\n" +
"varying vec3 vTangent;\n" +
"varying vec3 vBinormal;\n" +
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
"#if MAX_SPOT_LIGHTS > 0\n" +
"\n" +
"	uniform vec3 spotLightColor[ MAX_SPOT_LIGHTS ];\n" +
"	uniform vec3 spotLightPosition[ MAX_SPOT_LIGHTS ];\n" +
"	uniform vec3 spotLightDirection[ MAX_SPOT_LIGHTS ];\n" +
"	uniform float spotLightAngleCos[ MAX_SPOT_LIGHTS ];\n" +
"	uniform float spotLightExponent[ MAX_SPOT_LIGHTS ];\n" +
"	uniform float spotLightDistance[ MAX_SPOT_LIGHTS ];\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef WRAP_AROUND\n" +
"\n" +
"	uniform vec3 wrapRGB;\n" +
"\n" +
"#endif\n" +
"\n" +
"varying vec3 vWorldPosition;\n" +
"varying vec3 vViewPosition;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"			\n" +
"[*]\n" +
"\n" +
"	gl_FragColor = vec4( vec3( 1.0 ), opacity );\n" +
"\n" +
"	vec3 specularTex = vec3( 1.0 );\n" +
"\n" +
"	vec3 normalTex = texture2D( tNormal, vUv ).xyz * 2.0 - 1.0;\n" +
"	normalTex.xy *= uNormalScale;\n" +
"	normalTex = normalize( normalTex );\n" +
"\n" +
"	if( enableDiffuse ) {\n" +
"\n" +
"		#ifdef GAMMA_INPUT\n" +
"\n" +
"			vec4 texelColor = texture2D( tDiffuse, vUv );\n" +
"			texelColor.xyz *= texelColor.xyz;\n" +
"\n" +
"			gl_FragColor = gl_FragColor * texelColor;\n" +
"\n" +
"		#else\n" +
"\n" +
"			gl_FragColor = gl_FragColor * texture2D( tDiffuse, vUv );\n" +
"\n" +
"		#endif\n" +
"\n" +
"	}\n" +
"\n" +
"	if( enableAO ) {\n" +
"\n" +
"		#ifdef GAMMA_INPUT\n" +
"\n" +
"			vec4 aoColor = texture2D( tAO, vUv );\n" +
"			aoColor.xyz *= aoColor.xyz;\n" +
"\n" +
"			gl_FragColor.xyz = gl_FragColor.xyz * aoColor.xyz;\n" +
"\n" +
"		#else\n" +
"\n" +
"			gl_FragColor.xyz = gl_FragColor.xyz * texture2D( tAO, vUv ).xyz;\n" +
"\n" +
"		#endif\n" +
"\n" +
"	}\n" +
"			\n" +
"[*]\n" +
"\n" +
"	if( enableSpecular )\n" +
"		specularTex = texture2D( tSpecular, vUv ).xyz;\n" +
"\n" +
"	mat3 tsb = mat3( normalize( vTangent ), normalize( vBinormal ), normalize( vNormal ) );\n" +
"	vec3 finalNormal = tsb * normalTex;\n" +
"\n" +
"	#ifdef FLIP_SIDED\n" +
"\n" +
"		finalNormal = -finalNormal;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	vec3 normal = normalize( finalNormal );\n" +
"	vec3 viewPosition = normalize( vViewPosition );\n" +
"\n" +
"	// point lights\n" +
"\n" +
"	#if MAX_POINT_LIGHTS > 0\n" +
"\n" +
"		vec3 pointDiffuse = vec3( 0.0 );\n" +
"		vec3 pointSpecular = vec3( 0.0 );\n" +
"\n" +
"		for ( int i = 0; i < MAX_POINT_LIGHTS; i ++ ) {\n" +
"\n" +
"			vec4 lPosition = viewMatrix * vec4( pointLightPosition[ i ], 1.0 );\n" +
"			vec3 pointVector = lPosition.xyz + vViewPosition.xyz;\n" +
"\n" +
"			float pointDistance = 1.0;\n" +
"			if ( pointLightDistance[ i ] > 0.0 )\n" +
"				pointDistance = 1.0 - min( ( length( pointVector ) / pointLightDistance[ i ] ), 1.0 );\n" +
"\n" +
"			pointVector = normalize( pointVector );\n" +
"\n" +
"			// diffuse\n" +
"\n" +
"			#ifdef WRAP_AROUND\n" +
"\n" +
"				float pointDiffuseWeightFull = max( dot( normal, pointVector ), 0.0 );\n" +
"				float pointDiffuseWeightHalf = max( 0.5 * dot( normal, pointVector ) + 0.5, 0.0 );\n" +
"\n" +
"				vec3 pointDiffuseWeight = mix( vec3( pointDiffuseWeightFull ), vec3( pointDiffuseWeightHalf ), wrapRGB );\n" +
"\n" +
"			#else\n" +
"\n" +
"				float pointDiffuseWeight = max( dot( normal, pointVector ), 0.0 );\n" +
"\n" +
"			#endif\n" +
"\n" +
"			pointDiffuse += pointDistance * pointLightColor[ i ] * diffuse * pointDiffuseWeight;\n" +
"\n" +
"			// specular\n" +
"\n" +
"			vec3 pointHalfVector = normalize( pointVector + viewPosition );\n" +
"			float pointDotNormalHalf = max( dot( normal, pointHalfVector ), 0.0 );\n" +
"			float pointSpecularWeight = specularTex.r * max( pow( pointDotNormalHalf, shininess ), 0.0 );\n" +
"\n" +
"			float specularNormalization = ( shininess + 2.0 ) / 8.0;\n" +
"\n" +
"			vec3 schlick = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( pointVector, pointHalfVector ), 0.0 ), 5.0 );\n" +
"			pointSpecular += schlick * pointLightColor[ i ] * pointSpecularWeight * pointDiffuseWeight * pointDistance * specularNormalization;\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"	// spot lights\n" +
"\n" +
"	#if MAX_SPOT_LIGHTS > 0\n" +
"\n" +
"		vec3 spotDiffuse = vec3( 0.0 );\n" +
"		vec3 spotSpecular = vec3( 0.0 );\n" +
"\n" +
"		for ( int i = 0; i < MAX_SPOT_LIGHTS; i ++ ) {\n" +
"\n" +
"			vec4 lPosition = viewMatrix * vec4( spotLightPosition[ i ], 1.0 );\n" +
"			vec3 spotVector = lPosition.xyz + vViewPosition.xyz;\n" +
"\n" +
"			float spotDistance = 1.0;\n" +
"			if ( spotLightDistance[ i ] > 0.0 )\n" +
"				spotDistance = 1.0 - min( ( length( spotVector ) / spotLightDistance[ i ] ), 1.0 );\n" +
"\n" +
"			spotVector = normalize( spotVector );\n" +
"\n" +
"			float spotEffect = dot( spotLightDirection[ i ], normalize( spotLightPosition[ i ] - vWorldPosition ) );\n" +
"\n" +
"			if ( spotEffect > spotLightAngleCos[ i ] ) {\n" +
"\n" +
"				spotEffect = max( pow( max( spotEffect, 0.0 ), spotLightExponent[ i ] ), 0.0 );\n" +
"\n" +
"				// diffuse\n" +
"\n" +
"				#ifdef WRAP_AROUND\n" +
"\n" +
"					float spotDiffuseWeightFull = max( dot( normal, spotVector ), 0.0 );\n" +
"					float spotDiffuseWeightHalf = max( 0.5 * dot( normal, spotVector ) + 0.5, 0.0 );\n" +
"\n" +
"					vec3 spotDiffuseWeight = mix( vec3( spotDiffuseWeightFull ), vec3( spotDiffuseWeightHalf ), wrapRGB );\n" +
"\n" +
"				#else\n" +
"\n" +
"					float spotDiffuseWeight = max( dot( normal, spotVector ), 0.0 );\n" +
"\n" +
"				#endif\n" +
"\n" +
"				spotDiffuse += spotDistance * spotLightColor[ i ] * diffuse * spotDiffuseWeight * spotEffect;\n" +
"\n" +
"				// specular\n" +
"\n" +
"				vec3 spotHalfVector = normalize( spotVector + viewPosition );\n" +
"				float spotDotNormalHalf = max( dot( normal, spotHalfVector ), 0.0 );\n" +
"				float spotSpecularWeight = specularTex.r * max( pow( spotDotNormalHalf, shininess ), 0.0 );\n" +
"\n" +
"				float specularNormalization = ( shininess + 2.0 ) / 8.0;\n" +
"\n" +
"				vec3 schlick = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( spotVector, spotHalfVector ), 0.0 ), 5.0 );\n" +
"				spotSpecular += schlick * spotLightColor[ i ] * spotSpecularWeight * spotDiffuseWeight * spotDistance * specularNormalization * spotEffect;\n" +
"\n" +
"			}\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"	// directional lights\n" +
"\n" +
"	#if MAX_DIR_LIGHTS > 0\n" +
"\n" +
"		vec3 dirDiffuse = vec3( 0.0 );\n" +
"		vec3 dirSpecular = vec3( 0.0 );\n" +
"\n" +
"		for( int i = 0; i < MAX_DIR_LIGHTS; i++ ) {\n" +
"\n" +
"			vec4 lDirection = viewMatrix * vec4( directionalLightDirection[ i ], 0.0 );\n" +
"			vec3 dirVector = normalize( lDirection.xyz );\n" +
"\n" +
"			// diffuse\n" +
"\n" +
"			#ifdef WRAP_AROUND\n" +
"\n" +
"				float directionalLightWeightingFull = max( dot( normal, dirVector ), 0.0 );\n" +
"				float directionalLightWeightingHalf = max( 0.5 * dot( normal, dirVector ) + 0.5, 0.0 );\n" +
"\n" +
"				vec3 dirDiffuseWeight = mix( vec3( directionalLightWeightingFull ), vec3( directionalLightWeightingHalf ), wrapRGB );\n" +
"\n" +
"			#else\n" +
"\n" +
"				float dirDiffuseWeight = max( dot( normal, dirVector ), 0.0 );\n" +
"\n" +
"			#endif\n" +
"\n" +
"			dirDiffuse += directionalLightColor[ i ] * diffuse * dirDiffuseWeight;\n" +
"\n" +
"			// specular\n" +
"\n" +
"			vec3 dirHalfVector = normalize( dirVector + viewPosition );\n" +
"			float dirDotNormalHalf = max( dot( normal, dirHalfVector ), 0.0 );\n" +
"			float dirSpecularWeight = specularTex.r * max( pow( dirDotNormalHalf, shininess ), 0.0 );\n" +
"\n" +
"			float specularNormalization = ( shininess + 2.0 ) / 8.0;\n" +
"\n" +
"			vec3 schlick = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( dirVector, dirHalfVector ), 0.0 ), 5.0 );\n" +
"			dirSpecular += schlick * directionalLightColor[ i ] * dirSpecularWeight * dirDiffuseWeight * specularNormalization;\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"	// hemisphere lights\n" +
"\n" +
"	#if MAX_HEMI_LIGHTS > 0\n" +
"\n" +
"		vec3 hemiDiffuse = vec3( 0.0 );\n" +
"		vec3 hemiSpecular = vec3( 0.0 );\n" +
"\n" +
"		for( int i = 0; i < MAX_HEMI_LIGHTS; i ++ ) {\n" +
"\n" +
"			vec4 lDirection = viewMatrix * vec4( hemisphereLightDirection[ i ], 0.0 );\n" +
"			vec3 lVector = normalize( lDirection.xyz );\n" +
"\n" +
"			// diffuse\n" +
"\n" +
"			float dotProduct = dot( normal, lVector );\n" +
"			float hemiDiffuseWeight = 0.5 * dotProduct + 0.5;\n" +
"\n" +
"			vec3 hemiColor = mix( hemisphereLightGroundColor[ i ], hemisphereLightSkyColor[ i ], hemiDiffuseWeight );\n" +
"\n" +
"			hemiDiffuse += diffuse * hemiColor;\n" +
"\n" +
"			// specular (sky light)\n" +
"\n" +
"\n" +
"			vec3 hemiHalfVectorSky = normalize( lVector + viewPosition );\n" +
"			float hemiDotNormalHalfSky = 0.5 * dot( normal, hemiHalfVectorSky ) + 0.5;\n" +
"			float hemiSpecularWeightSky = specularTex.r * max( pow( max( hemiDotNormalHalfSky, 0.0 ), shininess ), 0.0 );\n" +
"\n" +
"			// specular (ground light)\n" +
"\n" +
"			vec3 lVectorGround = -lVector;\n" +
"\n" +
"			vec3 hemiHalfVectorGround = normalize( lVectorGround + viewPosition );\n" +
"			float hemiDotNormalHalfGround = 0.5 * dot( normal, hemiHalfVectorGround ) + 0.5;\n" +
"			float hemiSpecularWeightGround = specularTex.r * max( pow( max( hemiDotNormalHalfGround, 0.0 ), shininess ), 0.0 );\n" +
"\n" +
"			float dotProductGround = dot( normal, lVectorGround );\n" +
"\n" +
"			float specularNormalization = ( shininess + 2.0 ) / 8.0;\n" +
"\n" +
"			vec3 schlickSky = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( lVector, hemiHalfVectorSky ), 0.0 ), 5.0 );\n" +
"			vec3 schlickGround = specular + vec3( 1.0 - specular ) * pow( max( 1.0 - dot( lVectorGround, hemiHalfVectorGround ), 0.0 ), 5.0 );\n" +
"			hemiSpecular += hemiColor * specularNormalization * ( schlickSky * hemiSpecularWeightSky * max( dotProduct, 0.0 ) + schlickGround * hemiSpecularWeightGround * max( dotProductGround, 0.0 ) );\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"	// all lights contribution summation\n" +
"\n" +
"	vec3 totalDiffuse = vec3( 0.0 );\n" +
"	vec3 totalSpecular = vec3( 0.0 );\n" +
"\n" +
"	#if MAX_DIR_LIGHTS > 0\n" +
"\n" +
"		totalDiffuse += dirDiffuse;\n" +
"		totalSpecular += dirSpecular;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#if MAX_HEMI_LIGHTS > 0\n" +
"\n" +
"		totalDiffuse += hemiDiffuse;\n" +
"		totalSpecular += hemiSpecular;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#if MAX_POINT_LIGHTS > 0\n" +
"\n" +
"		totalDiffuse += pointDiffuse;\n" +
"		totalSpecular += pointSpecular;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#if MAX_SPOT_LIGHTS > 0\n" +
"\n" +
"		totalDiffuse += spotDiffuse;\n" +
"		totalSpecular += spotSpecular;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#ifdef METAL\n" +
"\n" +
"		gl_FragColor.xyz = gl_FragColor.xyz * ( totalDiffuse + ambientLightColor * ambient + totalSpecular );\n" +
"\n" +
"	#else\n" +
"\n" +
"		gl_FragColor.xyz = gl_FragColor.xyz * ( totalDiffuse + ambientLightColor * ambient ) + totalSpecular;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	if ( enableReflection ) {\n" +
"\n" +
"		vec3 vReflect;\n" +
"		vec3 cameraToVertex = normalize( vWorldPosition - cameraPosition );\n" +
"\n" +
"		if ( useRefract ) {\n" +
"\n" +
"			vReflect = refract( cameraToVertex, normal, refractionRatio );\n" +
"\n" +
"		} else {\n" +
"\n" +
"			vReflect = reflect( cameraToVertex, normal );\n" +
"\n" +
"		}\n" +
"\n" +
"		vec4 cubeColor = textureCube( tCube, vec3( -vReflect.x, vReflect.yz ) );\n" +
"\n" +
"		#ifdef GAMMA_INPUT\n" +
"\n" +
"			cubeColor.xyz *= cubeColor.xyz;\n" +
"\n" +
"		#endif\n" +
"\n" +
"		gl_FragColor.xyz = mix( gl_FragColor.xyz, cubeColor.xyz, specularTex.r * reflectivity );\n" +
"\n" +
"	}\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n";
}
