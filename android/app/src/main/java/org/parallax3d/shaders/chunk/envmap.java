// Generated from envmap_vertex.glsl and envmap_fragment.glsl

package org.parallax3d.shaders.chunk;

public class envmap
{
    public static final String vertex =
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
"#endif\n";

    public static final String fragment =
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
"#endif\n";
}
