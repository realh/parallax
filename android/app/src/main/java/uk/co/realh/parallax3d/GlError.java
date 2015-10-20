package uk.co.realh.parallax3d;

import android.opengl.GLES20;

/**
 * Created by tony on 15/10/15.
 */
public class GlError extends RuntimeException {

    /**
     * @param msg   Additional contextual info to display if there is an error.
     */
    public static void check(String msg) {
        int error = GLES20.glGetError();
        if (GLES20.GL_NO_ERROR == error)
            return;
        String es;
        switch (error) {
            case GLES20.GL_INVALID_ENUM:
                es = "GL_INVALID_ENUM";
                break;
            case GLES20.GL_INVALID_VALUE:
                es = "GL_INVALID_VALUE";
                break;
            case GLES20.GL_INVALID_OPERATION:
                es = "GL_INVALID_OPERATION";
                break;
            case GLES20.GL_OUT_OF_MEMORY:
                es = "GL_OUT_OF_MEMORY";
                break;
            case GLES20.GL_INVALID_FRAMEBUFFER_OPERATION:
                es = "GL_INVALID_FRAMEBUFFER_OPERATION";
                break;
            default:
                es = "Unknown code " + error;
                break;
        }
        throw new GlError(msg + ": " + es);
    }

    public GlError(String s) {
        super(s);
    }
}
