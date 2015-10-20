package uk.co.realh.parallax3d;

import android.opengl.GLES20;

/**
 * Created by tony on 15/10/15.
 */
public class Shader {

    private static final String TAG = "Parallax3D";

    public final static String A_POSITION = "position";
    public final static String A_TEX_COORD = "tex_coord";
    public final static String U_MVP = "mvp";
    public final static String U_TEXTURE_UNIT = "texture_unit";

    private int shaderProgram = 0;

    private static int[] status = { GLES20.GL_TRUE };

    public Shader(String vertSrc, String fragSrc) {
        int vertex = 0, fragment = 0;
        try {
            //Log.d(TAG, "Compiling vertex shader\n" + vertSrc + "*********\n");
            vertex = compileShader(vertSrc, GLES20.GL_VERTEX_SHADER);
            //Log.d(TAG, "Compiling fragment shader\n" +
            // fragSrc + "*********\n");
            fragment = compileShader(fragSrc, GLES20.GL_FRAGMENT_SHADER);
            linkShaders(vertex, fragment);
        } catch (Throwable e) {
            if (0 != fragment)
                GLES20.glDeleteShader(fragment);
            if (0 != vertex)
                GLES20.glDeleteShader(vertex);
            throw e;
        }
    }

    public final int getGlShaderProgram() {
        return shaderProgram;
    }

    public final int getAttribLocation(String name) {
        int a = GLES20.glGetAttribLocation(shaderProgram, name);
        if (a < 0) {
            String s = "Attribute '" + name + "' not found in shader";
            GlError.check(s);
            throw new GlError(s);
        }
        return a;
    }

    public final int getUniformLocation(String name) {
        int u = GLES20.glGetUniformLocation(shaderProgram, name);
        if (u < 0) {
            String s = "Uniform '" + name + "' not found in shader";
            GlError.check(s);
            throw new GlError(s);
        }
        return u;
    }

    private final int compileShader(String src, int type) {
        int shader = GLES20.glCreateShader(type);
        if (0 == shader) {
            String s = "Unable to create shader";
            GlError.check(s);
            throw new GlError(s);
        }
        try {
            GLES20.glShaderSource(shader, src);
            GlError.check("Setting shader source");
            GLES20.glCompileShader(shader);
            GlError.check("Compiling shader");
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
            if (GLES20.GL_FALSE == status[0]) {
                throw new GlError("Error in " +
                    (type == GLES20.GL_VERTEX_SHADER ? "vertex" : "fragment") +
                    " shader: " + GLES20.glGetShaderInfoLog(shader));
            }
        } catch (Throwable e) {
            GLES20.glDeleteShader(shader);
            throw e;
        }
        return shader;
    }

    private final void linkShaders(int vertex, int fragment) {
        try {
            shaderProgram = GLES20.glCreateProgram();
            if (0 == shaderProgram) {
                String s = "Unable to create shader program";
                GlError.check(s);
                throw new GlError(s);
            }
            GLES20.glAttachShader(shaderProgram, vertex);
            GLES20.glAttachShader(shaderProgram, fragment);
            GlError.check("Attaching shaders to program");
            GLES20.glLinkProgram(shaderProgram);
            GlError.check("Linking shaders");
            GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS,
                    status, 0);
            if (GLES20.GL_FALSE == status[0]) {
                throw new GlError("Error linking shader: " +
                        GLES20.glGetProgramInfoLog(shaderProgram));
            }
        } catch (Throwable e) {
            if (0 != shaderProgram) {
                GLES20.glDeleteProgram(shaderProgram);
                shaderProgram = 0;
            }
            throw e;
        }
    }
}
