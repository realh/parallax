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

public class FragmentShaderSources
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

        set("basic",
"uniform vec3 diffuse; \n" +
"uniform float opacity;\n" +
"				\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"	gl_FragColor = vec4( diffuse, opacity );\n" +
"		\n" +
"[*]\n" +
"		\n" +
"}\n");

        set("cube",
"uniform samplerCube tCube;\n" +
"uniform float tFlip;\n" +
"\n" +
"varying vec3 vWorldPosition;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = textureCube( tCube, vec3( tFlip * vWorldPosition.x, vWorldPosition.yz ) );\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n");

        set("dashed",
"uniform vec3 diffuse;\n" +
"uniform float opacity;\n" +
"\n" +
"uniform float dashSize;\n" +
"uniform float totalSize;\n" +
"\n" +
"varying float vLineDistance;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	if ( mod( vLineDistance, totalSize ) > dashSize ) {\n" +
"\n" +
"		discard;\n" +
"\n" +
"	}\n" +
"\n" +
"	gl_FragColor = vec4( diffuse, opacity );\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n");

        set("default",
"void main() {\n" +
"\n" +
"	gl_FragColor = vec4( 1.0, 0.0, 0.0, 0.5 );\n" +
"\n" +
"}\n");

        set("depth",
"uniform float mNear;\n" +
"uniform float mFar;\n" +
"uniform float opacity;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]\n" +
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
"	float color = 1.0 - smoothstep( mNear, mFar, depth );\n" +
"	gl_FragColor = vec4( vec3( color ), opacity );\n" +
"\n" +
"}\n");

        set("depthRGBA",
"[*]\n" +
"\n" +
"vec4 pack_depth( const in float depth ) {\n" +
"\n" +
"	const vec4 bit_shift = vec4( 256.0 * 256.0 * 256.0, 256.0 * 256.0, 256.0, 1.0 );\n" +
"	const vec4 bit_mask = vec4( 0.0, 1.0 / 256.0, 1.0 / 256.0, 1.0 / 256.0 );\n" +
"	vec4 res = mod( depth * bit_shift * vec4( 255 ), vec4( 256 ) ) / vec4( 255 ); // vec4 res = fract( depth * bit_shift );\n" +
"	res -= res.xxyz * bit_mask;\n" +
"	return res;\n" +
"\n" +
"}\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]\n" +
"\n" +
"	#ifdef USE_LOGDEPTHBUF_EXT\n" +
"\n" +
"		gl_FragData[ 0 ] = pack_depth( gl_FragDepthEXT );\n" +
"\n" +
"	#else\n" +
"\n" +
"		gl_FragData[ 0 ] = pack_depth( gl_FragCoord.z );\n" +
"\n" +
"	#endif\n" +
"\n" +
"	//"gl_FragData[ 0 ] = pack_depth( gl_FragCoord.z / gl_FragCoord.w );\n" +
"	//"float z = ( ( gl_FragCoord.z / gl_FragCoord.w ) - 3.0 ) / ( 4000.0 - 3.0 );\n" +
"	//"gl_FragData[ 0 ] = pack_depth( z );\n" +
"	//"gl_FragData[ 0 ] = vec4( z, z, z, 1.0 );\n" +
"\n" +
"}\n");

        set("fresnel",
"uniform samplerCube tCube;\n" +
"\n" +
"varying vec3 vReflect;\n" +
"varying vec3 vRefract[3];\n" +
"varying float vReflectionFactor;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 reflectedColor = textureCube( tCube, vec3( -vReflect.x, vReflect.yz ) );\n" +
"	vec4 refractedColor = vec4( 1.0, 1.0, 1.0, 1.0 );\n" +
"\n" +
"	refractedColor.r = textureCube( tCube, vec3( -vRefract[0].x, vRefract[0].yz ) ).r;\n" +
"	refractedColor.g = textureCube( tCube, vec3( -vRefract[1].x, vRefract[1].yz ) ).g;\n" +
"	refractedColor.b = textureCube( tCube, vec3( -vRefract[2].x, vRefract[2].yz ) ).b;\n" +
"	refractedColor.a = 1.0;\n" +
"\n" +
"	gl_FragColor = mix( refractedColor, reflectedColor, clamp( vReflectionFactor, 0.0, 1.0 ) );\n" +
"\n" +
"}\n");

        set("lambert",
"uniform float opacity;\n" +
"\n" +
"varying vec3 vLightFront;\n" +
"\n" +
"#ifdef DOUBLE_SIDED\n" +
"\n" +
"	varying vec3 vLightBack;\n" +
"\n" +
"#endif\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	gl_FragColor = vec4( vec3 ( 1.0 ), opacity );\n" +
"			\n" +
"[*]\n" +
"			\n" +
"#ifdef DOUBLE_SIDED\n" +
"\n" +
"	//"float isFront = float( gl_FrontFacing );\n" +
"	//"gl_FragColor.xyz *= isFront * vLightFront + ( 1.0 - isFront ) * vLightBack;\n" +
"\n" +
"	if ( gl_FrontFacing )\n" +
"		gl_FragColor.xyz *= vLightFront;\n" +
"	else\n" +
"		gl_FragColor.xyz *= vLightBack;\n" +
"\n" +
"#else\n" +
"\n" +
"	gl_FragColor.xyz *= vLightFront;\n" +
"\n" +
"#endif\n" +
"			\n" +
"[*]\n" +
"			\n" +
"}\n");

        set("normal",
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
"}\n");

        set("normalmap",
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
"}\n");

        set("particle_basic",
"uniform vec3 psColor;\n" +
"uniform float opacity;\n" +
"			\n" +
"[*]\n" +
"			\n" +
"void main() {\n" +
"	gl_FragColor = vec4( psColor, opacity );\n" +
"				\n" +
"[*]\n" +
"					\n" +
"}\n");

        set("phong",
"#define PHONG\n" +
"\n" +
"uniform vec3 diffuse;\n" +
"uniform float opacity;\n" +
"\n" +
"uniform vec3 ambient;\n" +
"uniform vec3 emissive;\n" +
"uniform vec3 specular;\n" +
"uniform float shininess;\n" +
"\n" +
"[*]\n" +
"			\n" +
"void main() {\n" +
"	gl_FragColor = vec4( vec3( 1.0 ), opacity );\n" +
"				\n" +
"[*]\n" +
"				\n" +
"}\n");
    }
}