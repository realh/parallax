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

public class VertexShaderChunks
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

        set("color_pars",
"#ifdef USE_COLOR\n" +
"\n" +
"	varying vec3 vColor;\n" +
"\n" +
"#endif\n");

        set("color",
"#ifdef USE_COLOR\n" +
"\n" +
"	#ifdef GAMMA_INPUT\n" +
"\n" +
"		vColor = color * color;\n" +
"\n" +
"	#else\n" +
"\n" +
"		vColor = color;\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n");

        set("default",
"vec4 mvPosition;\n" +
"\n" +
"#ifdef USE_SKINNING\n" +
"\n" +
"	mvPosition = modelViewMatrix * skinned;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if !defined( USE_SKINNING ) && defined( USE_MORPHTARGETS )\n" +
"\n" +
"	mvPosition = modelViewMatrix * vec4( morphed, 1.0 );\n" +
"\n" +
"#endif\n" +
"\n" +
"#if !defined( USE_SKINNING ) && ! defined( USE_MORPHTARGETS )\n" +
"\n" +
"	mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"#endif\n" +
"\n" +
"gl_Position = projectionMatrix * mvPosition;\n");

        set("defaultnormal",
"vec3 objectNormal;\n" +
"\n" +
"#ifdef USE_SKINNING\n" +
"\n" +
"	objectNormal = skinnedNormal.xyz;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if !defined( USE_SKINNING ) && defined( USE_MORPHNORMALS )\n" +
"\n" +
"	objectNormal = morphedNormal;\n" +
"\n" +
"#endif\n" +
"\n" +
"#if !defined( USE_SKINNING ) && ! defined( USE_MORPHNORMALS )\n" +
"\n" +
"	objectNormal = normal;\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef FLIP_SIDED\n" +
"\n" +
"	objectNormal = -objectNormal;\n" +
"\n" +
"#endif\n" +
"\n" +
"vec3 transformedNormal = normalMatrix * objectNormal;\n");

        set("envmap_pars",
"#if defined( USE_ENVMAP ) && ! defined( USE_BUMPMAP ) && ! defined( USE_NORMALMAP ) && ! defined( PHONG )\n" +
"\n" +
"	varying vec3 vReflect;\n" +
"\n" +
"	uniform float refractionRatio;\n" +
"	uniform bool useRefract;\n" +
"\n" +
"#endif\n");

        set("envmap",
"#if defined( USE_ENVMAP ) && ! defined( USE_BUMPMAP ) && ! defined( USE_NORMALMAP ) && ! defined( PHONG )\n" +
"\n" +
"	vec3 worldNormal = mat3( modelMatrix[ 0 ].xyz, modelMatrix[ 1 ].xyz, modelMatrix[ 2 ].xyz ) * objectNormal;\n" +
"	worldNormal = normalize( worldNormal );\n" +
"\n" +
"	vec3 cameraToVertex = normalize( worldPosition.xyz - cameraPosition );\n" +
"\n" +
"	if ( useRefract ) {\n" +
"\n" +
"		vReflect = refract( cameraToVertex, worldNormal, refractionRatio );\n" +
"\n" +
"	} else {\n" +
"\n" +
"		vReflect = reflect( cameraToVertex, worldNormal );\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n");

        set("lightmap_pars",
"#ifdef USE_LIGHTMAP\n" +
"\n" +
"	varying vec2 vUv2;\n" +
"\n" +
"#endif\n");

        set("lightmap",
"#ifdef USE_LIGHTMAP\n" +
"\n" +
"	vUv2 = uv2;\n" +
"\n" +
"#endif\n");

        set("lights_lambert_pars",
"uniform vec3 ambient;\n" +
"uniform vec3 diffuse;\n" +
"uniform vec3 emissive;\n" +
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
"	uniform float spotLightDistance[ MAX_SPOT_LIGHTS ];\n" +
"	uniform float spotLightAngleCos[ MAX_SPOT_LIGHTS ];\n" +
"	uniform float spotLightExponent[ MAX_SPOT_LIGHTS ];\n" +
"\n" +
"#endif\n" +
"\n" +
"#ifdef WRAP_AROUND\n" +
"\n" +
"	uniform vec3 wrapRGB;\n" +
"\n" +
"#endif\n");

        set("lights_lambert",
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
"#endif\n");

        set("lights_phong_pars",
"#if MAX_SPOT_LIGHTS > 0 || defined( USE_BUMPMAP ) || defined( USE_ENVMAP )\n" +
"\n" +
"	varying vec3 vWorldPosition;\n" +
"\n" +
"#endif\n");

        set("lights_phong",
"#if MAX_SPOT_LIGHTS > 0 || defined( USE_BUMPMAP ) || defined( USE_ENVMAP )\n" +
"\n" +
"	vWorldPosition = worldPosition.xyz;\n" +
"\n" +
"#endif\n");

        set("logdepthbuf_par",
"#ifdef USE_LOGDEPTHBUF\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		varying float vFragDepth;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	uniform float logDepthBufFC;\n" +
"\n" +
"#endif\n");

        set("logdepthbuf",
"#ifdef USE_LOGDEPTHBUF\n" +
"\n" +
"	gl_Position.z = log2(max(1e-6, gl_Position.w + 1.0)) * logDepthBufFC;\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		vFragDepth = 1.0 + gl_Position.w;\n" +
"\n" +
"#else\n" +
"\n" +
"		gl_Position.z = (gl_Position.z - 1.0) * gl_Position.w;\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n");

        set("map_pars",
"#if defined( USE_MAP ) || defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( USE_SPECULARMAP ) || defined( USE_ALPHAMAP )\n" +
"\n" +
"	varying vec2 vUv;\n" +
"	uniform vec4 offsetRepeat;\n" +
"\n" +
"#endif\n");

        set("map",
"#if defined( USE_MAP ) || defined( USE_BUMPMAP ) || defined( USE_NORMALMAP ) || defined( USE_SPECULARMAP ) || defined( USE_ALPHAMAP )\n" +
"\n" +
"	vUv = uv * offsetRepeat.zw + offsetRepeat.xy;\n" +
"\n" +
"#endif\n");

        set("morphnormal",
"#ifdef USE_MORPHNORMALS\n" +
"\n" +
"	vec3 morphedNormal = vec3( 0.0 );\n" +
"\n" +
"	morphedNormal += ( morphNormal0 - normal ) * morphTargetInfluences[ 0 ];\n" +
"	morphedNormal += ( morphNormal1 - normal ) * morphTargetInfluences[ 1 ];\n" +
"	morphedNormal += ( morphNormal2 - normal ) * morphTargetInfluences[ 2 ];\n" +
"	morphedNormal += ( morphNormal3 - normal ) * morphTargetInfluences[ 3 ];\n" +
"\n" +
"	morphedNormal += normal;\n" +
"\n" +
"#endif\n");

        set("morphtarget_pars",
"#ifdef USE_MORPHTARGETS\n" +
"\n" +
"	#ifndef USE_MORPHNORMALS\n" +
"\n" +
"	uniform float morphTargetInfluences[ 8 ];\n" +
"\n" +
"	#else\n" +
"\n" +
"	uniform float morphTargetInfluences[ 4 ];\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n");

        set("morphtarget",
"#ifdef USE_MORPHTARGETS\n" +
"\n" +
"	vec3 morphed = vec3( 0.0 );\n" +
"	morphed += ( morphTarget0 - position ) * morphTargetInfluences[ 0 ];\n" +
"	morphed += ( morphTarget1 - position ) * morphTargetInfluences[ 1 ];\n" +
"	morphed += ( morphTarget2 - position ) * morphTargetInfluences[ 2 ];\n" +
"	morphed += ( morphTarget3 - position ) * morphTargetInfluences[ 3 ];\n" +
"\n" +
"	#ifndef USE_MORPHNORMALS\n" +
"\n" +
"	morphed += ( morphTarget4 - position ) * morphTargetInfluences[ 4 ];\n" +
"	morphed += ( morphTarget5 - position ) * morphTargetInfluences[ 5 ];\n" +
"	morphed += ( morphTarget6 - position ) * morphTargetInfluences[ 6 ];\n" +
"	morphed += ( morphTarget7 - position ) * morphTargetInfluences[ 7 ];\n" +
"\n" +
"	#endif\n" +
"\n" +
"	morphed += position;\n" +
"\n" +
"#endif\n");

        set("shadowmap_pars",
"#ifdef USE_SHADOWMAP\n" +
"\n" +
"	varying vec4 vShadowCoord[ MAX_SHADOWS ];\n" +
"	uniform mat4 shadowMatrix[ MAX_SHADOWS ];\n" +
"\n" +
"#endif\n");

        set("shadowmap",
"#ifdef USE_SHADOWMAP\n" +
"\n" +
"	for( int i = 0; i < MAX_SHADOWS; i ++ ) {\n" +
"\n" +
"		vShadowCoord[ i ] = shadowMatrix[ i ] * worldPosition;\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n");

        set("skinbase",
"#ifdef USE_SKINNING\n" +
"\n" +
"	mat4 boneMatX = getBoneMatrix( skinIndex.x );\n" +
"	mat4 boneMatY = getBoneMatrix( skinIndex.y );\n" +
"	mat4 boneMatZ = getBoneMatrix( skinIndex.z );\n" +
"	mat4 boneMatW = getBoneMatrix( skinIndex.w );\n" +
"\n" +
"#endif\n");

        set("skinning_pars",
"#ifdef USE_SKINNING\n" +
"\n" +
"	uniform mat4 bindMatrix;\n" +
"	uniform mat4 bindMatrixInverse;\n" +
"\n" +
"	#ifdef BONE_TEXTURE\n" +
"\n" +
"		uniform sampler2D boneTexture;\n" +
"		uniform int boneTextureWidth;\n" +
"		uniform int boneTextureHeight;\n" +
"\n" +
"		mat4 getBoneMatrix( const in float i ) {\n" +
"\n" +
"			float j = i * 4.0;\n" +
"			float x = mod( j, float( boneTextureWidth ) );\n" +
"			float y = floor( j / float( boneTextureWidth ) );\n" +
"\n" +
"			float dx = 1.0 / float( boneTextureWidth );\n" +
"			float dy = 1.0 / float( boneTextureHeight );\n" +
"\n" +
"			y = dy * ( y + 0.5 );\n" +
"\n" +
"			vec4 v1 = texture2D( boneTexture, vec2( dx * ( x + 0.5 ), y ) );\n" +
"			vec4 v2 = texture2D( boneTexture, vec2( dx * ( x + 1.5 ), y ) );\n" +
"			vec4 v3 = texture2D( boneTexture, vec2( dx * ( x + 2.5 ), y ) );\n" +
"			vec4 v4 = texture2D( boneTexture, vec2( dx * ( x + 3.5 ), y ) );\n" +
"\n" +
"			mat4 bone = mat4( v1, v2, v3, v4 );\n" +
"\n" +
"			return bone;\n" +
"\n" +
"		}\n" +
"\n" +
"	#else\n" +
"\n" +
"		uniform mat4 boneGlobalMatrices[ MAX_BONES ];\n" +
"\n" +
"		mat4 getBoneMatrix( const in float i ) {\n" +
"\n" +
"			mat4 bone = boneGlobalMatrices[ int(i) ];\n" +
"			return bone;\n" +
"\n" +
"		}\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n");

        set("skinning",
"#ifdef USE_SKINNING\n" +
"\n" +
"	#ifdef USE_MORPHTARGETS\n" +
"\n" +
"	vec4 skinVertex = bindMatrix * vec4( morphed, 1.0 );\n" +
"\n" +
"	#else\n" +
"\n" +
"	vec4 skinVertex = bindMatrix * vec4( position, 1.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	vec4 skinned = vec4( 0.0 );\n" +
"	skinned += boneMatX * skinVertex * skinWeight.x;\n" +
"	skinned += boneMatY * skinVertex * skinWeight.y;\n" +
"	skinned += boneMatZ * skinVertex * skinWeight.z;\n" +
"	skinned += boneMatW * skinVertex * skinWeight.w;\n" +
"	skinned  = bindMatrixInverse * skinned;\n" +
"\n" +
"#endif\n");

        set("skinnormal",
"#ifdef USE_SKINNING\n" +
"\n" +
"	mat4 skinMatrix = mat4( 0.0 );\n" +
"	skinMatrix += skinWeight.x * boneMatX;\n" +
"	skinMatrix += skinWeight.y * boneMatY;\n" +
"	skinMatrix += skinWeight.z * boneMatZ;\n" +
"	skinMatrix += skinWeight.w * boneMatW;\n" +
"	skinMatrix  = bindMatrixInverse * skinMatrix * bindMatrix;\n" +
"\n" +
"	#ifdef USE_MORPHNORMALS\n" +
"\n" +
"	vec4 skinnedNormal = skinMatrix * vec4( morphedNormal, 0.0 );\n" +
"\n" +
"	#else\n" +
"\n" +
"	vec4 skinnedNormal = skinMatrix * vec4( normal, 0.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n");

        set("worldpos",
"#if defined( USE_ENVMAP ) || defined( PHONG ) || defined( LAMBERT ) || defined ( USE_SHADOWMAP )\n" +
"\n" +
"	#ifdef USE_SKINNING\n" +
"\n" +
"		vec4 worldPosition = modelMatrix * skinned;\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#if defined( USE_MORPHTARGETS ) && ! defined( USE_SKINNING )\n" +
"\n" +
"		vec4 worldPosition = modelMatrix * vec4( morphed, 1.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	#if ! defined( USE_MORPHTARGETS ) && ! defined( USE_SKINNING )\n" +
"\n" +
"		vec4 worldPosition = modelMatrix * vec4( position, 1.0 );\n" +
"\n" +
"	#endif\n" +
"\n" +
"#endif\n");
    }
}