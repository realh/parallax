// Generated from shadowmap_vertex.glsl and shadowmap_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class shadowmap
{
    public static final String vertex =
"#ifdef USE_SHADOWMAP\n" +
"\n" +
"	for( int i = 0; i < MAX_SHADOWS; i ++ ) {\n" +
"\n" +
"		vShadowCoord[ i ] = shadowMatrix[ i ] * worldPosition;\n" +
"\n" +
"	}\n" +
"\n" +
"#endif\n";

    public static final String fragment =
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
"#endif\n";
}
