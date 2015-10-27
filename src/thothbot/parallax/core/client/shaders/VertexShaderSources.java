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

public class VertexShaderSources
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
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]			\n" +
"\n" +
"	#ifdef USE_ENVMAP\n" +
"\n" +
"[*]			\n" +
"\n" +
"	#endif\n" +
"\n" +
"[*]			\n" +
"	\n" +
"}\n");

        set("cube",
"varying vec3 vWorldPosition;\n" +
"\n" +
"[*]	\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 worldPosition = modelMatrix * vec4( position, 1.0 );\n" +
"	vWorldPosition = worldPosition.xyz;\n" +
"\n" +
"	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"[*]	\n" +
"\n" +
"}\n");

        set("dashed",
"uniform float scale;\n" +
"attribute float lineDistance;\n" +
"\n" +
"varying float vLineDistance;\n" +
"\n" +
"[*]		\n" +
"\n" +
"void main() {\n" +
"\n" +
"[*]		\n" +
"\n" +
"	vLineDistance = scale * lineDistance;\n" +
"\n" +
"	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
"	gl_Position = projectionMatrix * mvPosition;\n" +
"\n" +
"[*]		\n" +
"\n" +
"}\n");

        set("default",
"void main() {\n" +
"\n" +
"	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"}\n");

        set("depth",
"[*]\n" +
"				\n" +
"void main() {\n" +
"				\n" +
"[*]\n" +
"				\n" +
"}\n");

        set("depthRGBA",
"[*]\n" +
"				\n" +
"void main() {\n" +
"				\n" +
"[*]\n" +
"				\n" +
"}\n");

        set("fresnel",
"uniform float mRefractionRatio;\n" +
"uniform float mFresnelBias;\n" +
"uniform float mFresnelScale;\n" +
"uniform float mFresnelPower;\n" +
"\n" +
"varying vec3 vReflect;\n" +
"varying vec3 vRefract[3];\n" +
"varying float vReflectionFactor;\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
"	vec4 mPosition = modelMatrix * vec4( position, 1.0 );\n" +
"\n" +
"	vec3 nWorld = normalize ( mat3( modelMatrix[0].xyz, modelMatrix[1].xyz, modelMatrix[2].xyz ) * normal );\n" +
"\n" +
"	vec3 I = mPosition.xyz - cameraPosition;\n" +
"\n" +
"	vReflect = reflect( I, nWorld );\n" +
"	vRefract[0] = refract( normalize( I ), nWorld, mRefractionRatio );\n" +
"	vRefract[1] = refract( normalize( I ), nWorld, mRefractionRatio * 0.99 );\n" +
"	vRefract[2] = refract( normalize( I ), nWorld, mRefractionRatio * 0.98 );\n" +
"	vReflectionFactor = mFresnelBias + mFresnelScale * pow( 1.0 + dot( normalize( I ), nWorld ), mFresnelPower );\n" +
"\n" +
"	gl_Position = projectionMatrix * mvPosition;\n" +
"\n" +
"}\n");

        set("lambert",
"#define LAMBERT\n" +
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
"	\n" +
"[*]\n" +
"						\n" +
"}\n");

        set("normal",
"varying vec3 vNormal;\n" +
"\n" +
"[*]\n" +
"\n" +
"void main() {\n" +
"\n" +
"	vNormal = normalize( normalMatrix * normal );\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n");

        set("normalmap",
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
"}\n");

        set("particle_basic",
"uniform float size;\n" +
"uniform float scale;\n" +
"			\n" +
"[*]			\n" +
"\n" +
"void main() {\n" +
"			\n" +
"[*]\n" +
"			\n" +
"vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );\n" +
"\n" +
"#ifdef USE_SIZEATTENUATION\n" +
"	gl_PointSize = size * ( scale / length( mvPosition.xyz ) );\n" +
"#else\n" +
"	gl_PointSize = size;\n" +
"#endif\n" +
"\n" +
"gl_Position = projectionMatrix * mvPosition;\n" +
"			\n" +
"[*]\n" +
"\n" +
"}\n");

        set("phong",
"#define PHONG\n" +
"\n" +
"varying vec3 vViewPosition;\n" +
"varying vec3 vNormal;\n" +
"\n" +
"[*]\n" +
"			\n" +
"void main() {\n" +
"			\n" +
"[*]\n" +
"			\n" +
"vNormal = normalize( transformedNormal );\n" +
"\n" +
"[*]\n" +
"\n" +
"vViewPosition = -mvPosition.xyz;\n" +
"\n" +
"[*]\n" +
"\n" +
"}\n");
    }
}