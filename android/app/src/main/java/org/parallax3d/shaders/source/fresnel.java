// Generated from fresnel.vs and fresnel.fs

package org.parallax3d.shaders.source;

public class fresnel
{
    public static final String vertex =
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
"}\n";

    public static final String fragment =
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
"}\n";
}
