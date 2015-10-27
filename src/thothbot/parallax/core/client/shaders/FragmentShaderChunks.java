/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 * 
 * This file is part of the realh fork of the Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package thothbot.parallax.core.client.shaders;

import java.util.HashMap;

public class FragmentShaderChunks
{
    private static HashMap<String, String> shaderMap;

    public static void set(String k, String v)
    {
        if (shaderMap == null)
            shaderMap = new HashMap<String, String>();
        shaderMap.put(k, v);
    }

    public static String get(String k)
    {
        return shaderMap.get(k);
    }

    static
    {

        set("alphamap",
"#ifdef USE_ALPHAMAP\n" +
"\n" +
"	gl_FragColor.a *= texture2D( alphaMap, vUv ).g;\n" +
"\n" +
"#endif\n");

        set("alphamap_pars",
"#ifdef USE_ALPHAMAP\n" +
"\n" +
"	uniform sampler2D alphaMap;\n" +
"\n" +
"#endif\n");

        set("alphatest",
"#ifdef ALPHATEST\n" +
"\n" +
"	if ( gl_FragColor.a < ALPHATEST ) discard;\n" +
"\n" +
"#endif\n");

        set("bumpmap_pars",
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
"#endif\n");

        set("color",
"#ifdef USE_COLOR\n" +
"\n" +
"	gl_FragColor = gl_FragColor * vec4( vColor, 1.0 );\n" +
"\n" +
"#endif\n");

        set("color_pars",
"#ifdef USE_COLOR\n" +
"\n" +
"	varying vec3 vColor;\n" +
"\n" +
"#endif\n");

        set("envmap",
"#ifdef USE_ENVMAP\n" +
"\n" +
"	vec3 reflectVec;\n" +
"\n" +
"	#if defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( PHONG )\n" +
"\n" +
"		vec3 cameraToVertex = normalize( vWorldPosition - cameraPosition );\n" +
"\n" +
"		// http://en.wikibooks.org/wiki/GLSL_Programming/Applying_Matrix_Transformations\n" +
"		// Transforming Normal Vectors with the Inverse Transformation\n" +
"\n" +
"		vec3 worldNormal = normalize( vec3( vec4( normal, 0.0 ) * viewMatrix ) );\n" +
"\n" +
"		if ( useRefract ) {\n" +
"\n" +
"			reflectVec = refract( cameraToVertex, worldNormal, refractionRatio );\n" +
"\n" +
"		} else { \n" +
"\n" +
"			reflectVec = reflect( cameraToVertex, worldNormal );\n" +
"\n" +
"		}\n" +
"\n" +
"	#else\n" +
"\n" +
"		reflectVec = vReflect;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#ifdef DOUBLE_SIDED\n" +
"\n" +
"		float flipNormal = ( -1.0 + 2.0 * float( gl_FrontFacing ) );\n" +
"		vec4 cubeColor = textureCube( envMap, flipNormal * vec3( flipEnvMap * reflectVec.x, reflectVec.yz ) );\n" +
"\n" +
"	#else\n" +
"\n" +
"		vec4 cubeColor = textureCube( envMap, vec3( flipEnvMap * reflectVec.x, reflectVec.yz ) );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#ifdef GAMMA_INPUT\n" +
"\n" +
"		cubeColor.xyz *= cubeColor.xyz;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	if ( combine == 1 ) {\n" +
"\n" +
"		gl_FragColor.xyz = mix( gl_FragColor.xyz, cubeColor.xyz, specularStrength * reflectivity );\n" +
"\n" +
"	} else if ( combine == 2 ) {\n" +
"\n" +
"		gl_FragColor.xyz += cubeColor.xyz * specularStrength * reflectivity;\n" +
"\n" +
"	} else {\n" +
"\n" +
"		gl_FragColor.xyz = mix( gl_FragColor.xyz, gl_FragColor.xyz * cubeColor.xyz, specularStrength * reflectivity );\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n");

        set("envmap_pars",
"#ifdef USE_ENVMAP\n" +
"\n" +
"	uniform float reflectivity;\n" +
"	uniform samplerCube envMap;\n" +
"	uniform float flipEnvMap;\n" +
"	uniform int combine;\n" +
"\n" +
"	#if defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( PHONG )\n" +
"\n" +
"		uniform bool useRefract;\n" +
"		uniform float refractionRatio;\n" +
"\n" +
"	#else\n" +
"\n" +
"		varying vec3 vReflect;\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n");

        set("fog",
"#ifdef USE_FOG\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		float depth = gl_FragDepthEXT / gl_FragCoord.w;\n" +
"\n" +
"	#else\n" +
"\n" +
"		float depth = gl_FragCoord.z / gl_FragCoord.w;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#ifdef FOG_EXP2\n" +
"\n" +
"		const float LOG2 = 1.442695;\n" +
"		float fogFactor = exp2( - fogDensity * fogDensity * depth * depth * LOG2 );\n" +
"		fogFactor = 1.0 - clamp( fogFactor, 0.0, 1.0 );\n" +
"\n" +
"	#else\n" +
"\n" +
"		float fogFactor = smoothstep( fogNear, fogFar, depth );\n" +
"\n" +
"	#endif\n" +
"	\n" +
"	gl_FragColor = mix( gl_FragColor, vec4( fogColor, gl_FragColor.w ), fogFactor );\n" +
"\n" +
"#endif\n");

        set("fog_pars",
"#ifdef USE_FOG\n" +
"\n" +
"	uniform vec3 fogColor;\n" +
"\n" +
"	#ifdef FOG_EXP2\n" +
"\n" +
"		uniform float fogDensity;\n" +
"\n" +
"	#else\n" +
"\n" +
"		uniform float fogNear;\n" +
"		uniform float fogFar;\n" +
"	#endif\n" +
"\n" +
"#endif\n");

        set("lightmap",
"#ifdef USE_LIGHTMAP\n" +
"\n" +
"	gl_FragColor = gl_FragColor * texture2D( lightMap, vUv2 );\n" +
"\n" +
"#endif\n");

        set("lightmap_pars",
"#ifdef USE_LIGHTMAP\n" +
"\n" +
"	varying vec2 vUv2;\n" +
"	uniform sampler2D lightMap;\n" +
"\n" +
"#endif\n");

        set("lights_phong",
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
"#endif\n");

        set("lights_phong_pars",
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
"\n" +
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
"\n" +
"	uniform float spotLightDistance[ MAX_SPOT_LIGHTS ];\n" +
"\n" +
"#endif\n" +
"\n" +
"#if MAX_SPOT_LIGHTS > 0 || defined( USE_BUMPMAP ) || defined( USE_ENVMAP )\n" +
"\n" +
"	varying vec3 vWorldPosition;\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef WRAP_AROUND\n" +
"\n" +
"	uniform vec3 wrapRGB;\n" +
"\n" +
"#endif\n" +
"\n" +
"varying vec3 vViewPosition;\n" +
"varying vec3 vNormal;\n");

        set("linear_to_gamma",
"#ifdef GAMMA_OUTPUT\n" +
"\n" +
"	gl_FragColor.xyz = sqrt( gl_FragColor.xyz );\n" +
"\n" +
"#endif\n");

        set("logdepthbuf",
"#if defined(USE_LOGDEPTHBUF) && defined(USE_LOGDEPTHBUF_EXT)\n" +
"\n" +
"	gl_FragDepthEXT = log2(vFragDepth) * logDepthBufFC * 0.5;\n" +
"\n" +
"#endif\n");

        set("logdepthbuf_par",
"#ifdef USE_LOGDEPTHBUF\n" +
"\n" +
"	uniform float logDepthBufFC;\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		#extension GL_EXT_frag_depth : enable\n" +
"		varying float vFragDepth;\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n");

        set("map",
"#ifdef USE_MAP\n" +
"\n" +
"	vec4 texelColor = texture2D( map, vUv );\n" +
"\n" +
"	#ifdef GAMMA_INPUT\n" +
"\n" +
"		texelColor.xyz *= texelColor.xyz;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	gl_FragColor = gl_FragColor * texelColor;\n" +
"\n" +
"#endif\n");

        set("map_pars",
"#if defined( USE_MAP ) || defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( USE_SPECULARMAP ) || defined( USE_ALPHAMAP )\n" +
"\n" +
"	varying vec2 vUv;\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef USE_MAP\n" +
"\n" +
"	uniform sampler2D map;\n" +
"\n" +
"#endif\n");

        set("map_particle",
"#ifdef USE_MAP\n" +
"\n" +
"	gl_FragColor = gl_FragColor * texture2D( map, vec2( gl_PointCoord.x, 1.0 - gl_PointCoord.y ) );\n" +
"\n" +
"#endif\n");

        set("map_particle_pars",
"#ifdef USE_MAP\n" +
"\n" +
"	uniform sampler2D map;\n" +
"\n" +
"#endif\n");

        set("normalmap_pars",
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
"#endif\n");

        set("shadowmap",
"#ifdef USE_SHADOWMAP\n" +
"\n" +
"	#ifdef SHADOWMAP_DEBUG\n" +
"\n" +
"		vec3 frustumColors[3];\n" +
"		frustumColors[0] = vec3( 1.0, 0.5, 0.0 );\n" +
"		frustumColors[1] = vec3( 0.0, 1.0, 0.8 );\n" +
"		frustumColors[2] = vec3( 0.0, 0.5, 1.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#ifdef SHADOWMAP_CASCADE\n" +
"\n" +
"		int inFrustumCount = 0;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	float fDepth;\n" +
"	vec3 shadowColor = vec3( 1.0 );\n" +
"\n" +
"	for( int i = 0; i < MAX_SHADOWS; i ++ ) {\n" +
"\n" +
"		vec3 shadowCoord = vShadowCoord[ i ].xyz / vShadowCoord[ i ].w;\n" +
"\n" +
"				// if ( something && something ) breaks ATI OpenGL shader compiler\n" +
"				// if ( all( something, something ) ) using this instead\n" +
"\n" +
"		bvec4 inFrustumVec = bvec4 ( shadowCoord.x >= 0.0, shadowCoord.x <= 1.0, shadowCoord.y >= 0.0, shadowCoord.y <= 1.0 );\n" +
"		bool inFrustum = all( inFrustumVec );\n" +
"\n" +
"				// don't shadow pixels outside of light frustum\n" +
"				// use just first frustum (for cascades)\n" +
"				// don't shadow pixels behind far plane of light frustum\n" +
"\n" +
"		#ifdef SHADOWMAP_CASCADE\n" +
"\n" +
"			inFrustumCount += int( inFrustum );\n" +
"			bvec3 frustumTestVec = bvec3( inFrustum, inFrustumCount == 1, shadowCoord.z <= 1.0 );\n" +
"\n" +
"		#else\n" +
"\n" +
"			bvec2 frustumTestVec = bvec2( inFrustum, shadowCoord.z <= 1.0 );\n" +
"\n" +
"		#endif\n" +
"\n" +
"		bool frustumTest = all( frustumTestVec );\n" +
"\n" +
"		if ( frustumTest ) {\n" +
"\n" +
"			shadowCoord.z += shadowBias[ i ];\n" +
"\n" +
"			#if defined( SHADOWMAP_TYPE_PCF )\n" +
"\n" +
"						// Percentage-close filtering\n" +
"						// (9 pixel kernel)\n" +
"						// http://fabiensanglard.net/shadowmappingPCF/\n" +
"\n" +
"				float shadow = 0.0;\n" +
"\n" +
"		/*\n" +
"						// nested loops breaks shader compiler / validator on some ATI cards when using OpenGL\n" +
"						// must enroll loop manually\n" +
"\n" +
"				for ( float y = -1.25; y <= 1.25; y += 1.25 )\n" +
"					for ( float x = -1.25; x <= 1.25; x += 1.25 ) {\n" +
"\n" +
"						vec4 rgbaDepth = texture2D( shadowMap[ i ], vec2( x * xPixelOffset, y * yPixelOffset ) + shadowCoord.xy );\n" +
"\n" +
"								// doesn't seem to produce any noticeable visual difference compared to simple texture2D lookup\n" +
"								//vec4 rgbaDepth = texture2DProj( shadowMap[ i ], vec4( vShadowCoord[ i ].w * ( vec2( x * xPixelOffset, y * yPixelOffset ) + shadowCoord.xy ), 0.05, vShadowCoord[ i ].w ) );\n" +
"\n" +
"						float fDepth = unpackDepth( rgbaDepth );\n" +
"\n" +
"						if ( fDepth < shadowCoord.z )\n" +
"							shadow += 1.0;\n" +
"\n" +
"				}\n" +
"\n" +
"				shadow /= 9.0;\n" +
"\n" +
"		*/\n" +
"\n" +
"				const float shadowDelta = 1.0 / 9.0;\n" +
"\n" +
"				float xPixelOffset = 1.0 / shadowMapSize[ i ].x;\n" +
"				float yPixelOffset = 1.0 / shadowMapSize[ i ].y;\n" +
"\n" +
"				float dx0 = -1.25 * xPixelOffset;\n" +
"				float dy0 = -1.25 * yPixelOffset;\n" +
"				float dx1 = 1.25 * xPixelOffset;\n" +
"				float dy1 = 1.25 * yPixelOffset;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx0, dy0 ) ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( 0.0, dy0 ) ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx1, dy0 ) ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx0, 0.0 ) ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx1, 0.0 ) ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx0, dy1 ) ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( 0.0, dy1 ) ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				fDepth = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx1, dy1 ) ) );\n" +
"				if ( fDepth < shadowCoord.z ) shadow += shadowDelta;\n" +
"\n" +
"				shadowColor = shadowColor * vec3( ( 1.0 - shadowDarkness[ i ] * shadow ) );\n" +
"\n" +
"			#elif defined( SHADOWMAP_TYPE_PCF_SOFT )\n" +
"\n" +
"						// Percentage-close filtering\n" +
"						// (9 pixel kernel)\n" +
"						// http://fabiensanglard.net/shadowmappingPCF/\n" +
"\n" +
"				float shadow = 0.0;\n" +
"\n" +
"				float xPixelOffset = 1.0 / shadowMapSize[ i ].x;\n" +
"				float yPixelOffset = 1.0 / shadowMapSize[ i ].y;\n" +
"\n" +
"				float dx0 = -1.0 * xPixelOffset;\n" +
"				float dy0 = -1.0 * yPixelOffset;\n" +
"				float dx1 = 1.0 * xPixelOffset;\n" +
"				float dy1 = 1.0 * yPixelOffset;\n" +
"\n" +
"				mat3 shadowKernel;\n" +
"				mat3 depthKernel;\n" +
"\n" +
"				depthKernel[0][0] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx0, dy0 ) ) );\n" +
"				depthKernel[0][1] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx0, 0.0 ) ) );\n" +
"				depthKernel[0][2] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx0, dy1 ) ) );\n" +
"				depthKernel[1][0] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( 0.0, dy0 ) ) );\n" +
"				depthKernel[1][1] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy ) );\n" +
"				depthKernel[1][2] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( 0.0, dy1 ) ) );\n" +
"				depthKernel[2][0] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx1, dy0 ) ) );\n" +
"				depthKernel[2][1] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx1, 0.0 ) ) );\n" +
"				depthKernel[2][2] = unpackDepth( texture2D( shadowMap[ i ], shadowCoord.xy + vec2( dx1, dy1 ) ) );\n" +
"\n" +
"				vec3 shadowZ = vec3( shadowCoord.z );\n" +
"				shadowKernel[0] = vec3(lessThan(depthKernel[0], shadowZ ));\n" +
"				shadowKernel[0] *= vec3(0.25);\n" +
"\n" +
"				shadowKernel[1] = vec3(lessThan(depthKernel[1], shadowZ ));\n" +
"				shadowKernel[1] *= vec3(0.25);\n" +
"\n" +
"				shadowKernel[2] = vec3(lessThan(depthKernel[2], shadowZ ));\n" +
"				shadowKernel[2] *= vec3(0.25);\n" +
"\n" +
"				vec2 fractionalCoord = 1.0 - fract( shadowCoord.xy * shadowMapSize[i].xy );\n" +
"\n" +
"				shadowKernel[0] = mix( shadowKernel[1], shadowKernel[0], fractionalCoord.x );\n" +
"				shadowKernel[1] = mix( shadowKernel[2], shadowKernel[1], fractionalCoord.x );\n" +
"\n" +
"				vec4 shadowValues;\n" +
"				shadowValues.x = mix( shadowKernel[0][1], shadowKernel[0][0], fractionalCoord.y );\n" +
"				shadowValues.y = mix( shadowKernel[0][2], shadowKernel[0][1], fractionalCoord.y );\n" +
"				shadowValues.z = mix( shadowKernel[1][1], shadowKernel[1][0], fractionalCoord.y );\n" +
"				shadowValues.w = mix( shadowKernel[1][2], shadowKernel[1][1], fractionalCoord.y );\n" +
"\n" +
"				shadow = dot( shadowValues, vec4( 1.0 ) );\n" +
"\n" +
"				shadowColor = shadowColor * vec3( ( 1.0 - shadowDarkness[ i ] * shadow ) );\n" +
"\n" +
"			#else\n" +
"\n" +
"				vec4 rgbaDepth = texture2D( shadowMap[ i ], shadowCoord.xy );\n" +
"				float fDepth = unpackDepth( rgbaDepth );\n" +
"\n" +
"				if ( fDepth < shadowCoord.z )\n" +
"\n" +
"		// spot with multiple shadows is darker\n" +
"\n" +
"					shadowColor = shadowColor * vec3( 1.0 - shadowDarkness[ i ] );\n" +
"\n" +
"		// spot with multiple shadows has the same color as single shadow spot\n" +
"\n" +
"		// 					shadowColor = min( shadowColor, vec3( shadowDarkness[ i ] ) );\n" +
"\n" +
"			#endif\n" +
"\n" +
"		}\n" +
"\n" +
"\n" +
"		#ifdef SHADOWMAP_DEBUG\n" +
"\n" +
"			#ifdef SHADOWMAP_CASCADE\n" +
"\n" +
"				if ( inFrustum && inFrustumCount == 1 ) gl_FragColor.xyz *= frustumColors[ i ];\n" +
"\n" +
"			#else\n" +
"\n" +
"				if ( inFrustum ) gl_FragColor.xyz *= frustumColors[ i ];\n" +
"\n" +
"			#endif\n" +
"\n" +
"		#endif\n" +
"\n" +
"	}\n" +
"\n" +
"	#ifdef GAMMA_OUTPUT\n" +
"\n" +
"		shadowColor *= shadowColor;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	gl_FragColor.xyz = gl_FragColor.xyz * shadowColor;\n" +
"\n" +
"#endif\n");

        set("shadowmap_pars",
"#ifdef USE_SHADOWMAP\n" +
"\n" +
"	uniform sampler2D shadowMap[ MAX_SHADOWS ];\n" +
"	uniform vec2 shadowMapSize[ MAX_SHADOWS ];\n" +
"\n" +
"	uniform float shadowDarkness[ MAX_SHADOWS ];\n" +
"	uniform float shadowBias[ MAX_SHADOWS ];\n" +
"\n" +
"	varying vec4 vShadowCoord[ MAX_SHADOWS ];\n" +
"\n" +
"	float unpackDepth( const in vec4 rgba_depth ) {\n" +
"\n" +
"		const vec4 bit_shift = vec4( 1.0 / ( 256.0 * 256.0 * 256.0 ), 1.0 / ( 256.0 * 256.0 ), 1.0 / 256.0, 1.0 );\n" +
"		float depth = dot( rgba_depth, bit_shift );\n" +
"		return depth;\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n");

        set("specularmap",
"float specularStrength;\n" +
"\n" +
"#ifdef USE_SPECULARMAP\n" +
"\n" +
"	vec4 texelSpecular = texture2D( specularMap, vUv );\n" +
"	specularStrength = texelSpecular.r;\n" +
"\n" +
"#else\n" +
"\n" +
"	specularStrength = 1.0;\n" +
"\n" +
"#endif\n");

        set("specularmap_pars",
"#ifdef USE_SPECULARMAP\n" +
"\n" +
"	uniform sampler2D specularMap;\n" +
"\n" +
"#endif\n");
    }
}