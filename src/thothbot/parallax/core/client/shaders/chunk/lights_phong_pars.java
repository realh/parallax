// Generated from lights_phong_pars_vertex.glsl and lights_phong_pars_fragment.glsl

package thothbot.parallax.core.client.shaders.chunk;

public class lights_phong_pars
{
    public static final String vertex =
"#if MAX_SPOT_LIGHTS > 0 || defined( USE_BUMPMAP ) || defined( USE_ENVMAP )\n" +
"\n" +
"	varying vec3 vWorldPosition;\n" +
"\n" +
"#endif\n";

    public static final String fragment =
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
"varying vec3 vNormal;\n";
}
