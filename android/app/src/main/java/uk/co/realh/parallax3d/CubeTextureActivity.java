package uk.co.realh.parallax3d;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeTextureActivity extends AppCompatActivity {

    private static final String TAG = "Parallax3D";

    private GLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            crateTexture = AndroidGLES20Texture.fromAsset(
                    getAssets(), "textures/crate.gif");
            surfaceView = new GLSurfaceView(this);
            surfaceView.setEGLContextClientVersion(2);
            surfaceView.setRenderer(new CubeTextureRenderer());
            setContentView(surfaceView);
        } catch (Throwable e) {
            Log.e(TAG, "Exception in onCreate", e);
        }

    }

    private static float CAMERA_Z = 400;

    private static final float L = 0.75f;   // Half the length of side of crate
    private static final int STRIDE = 5 * 4;
    private static final int POS_OFFSET = 0;
    private static final int TEX_OFFSET = 3 * 4;
    /*
    private static final float[] vertsArray = {
            -0.5f, -0.5f, 0,    0   , 0,
             0.5f, -0.5f, 0,    1   , 0,
             0   ,  0.5f, 0,    0.5f, 1,
    };
    private static final byte[] indexArray = { 0, 1, 2 };
    */
    private static final float[] vertsArray = {
            // Front face
            -L, -L,  L,     0, 0,
             L, -L,  L,     1, 0,
            -L,  L,  L,     0, 1,
             L,  L,  L,     1, 1,
            // Left face
            -L, -L, -L,     0, 1,
            -L, -L,  L,     1, 0,
            -L,  L, -L,     0, 1,
            -L,  L,  L,     1, 1,
            // Rear face
             L, -L,  L,     0, 0,
            -L, -L,  L,     1, 0,
             L,  L,  L,     0, 1,
            -L,  L,  L,     1, 1,
            // Top face
            -L,  L,  L,     0, 0,
             L,  L,  L,     1, 0,
            -L,  L, -L,     0, 1,
             L,  L, -L,     1, 1,
            // Right face
             L, -L,  L,     0, 0,
             L, -L, -L,     1, 0,
             L,  L,  L,     0, 1,
             L,  L, -L,     1, 1,
            // Bottom face
            -L, -L, -L,     0, 0,
             L, -L,  L,     1, 0,
            -L, -L, -L,     0, 1,
             L, -L,  L,     1, 1,
    };
    private static final byte[] indexArray = {
            0, 1, 3,    3, 2, 0,
            4, 5, 7,    7, 6, 4,
            8, 9, 11,   8, 11, 10,
            15, 14, 12, 12, 13, 15,
            19, 18, 16, 17, 19, 16,
            23, 22, 20, 20, 21, 23,
    };

    private final void makeBuffers() {
        GLES20.glGenBuffers(2, buffers, 0);
        GlError.check("Unable to create buffers");
        int l = vertsArray.length * 4;
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(l).
                order(ByteOrder.nativeOrder());
        FloatBuffer vertBuf = byteBuf.asFloatBuffer();
        vertBuf.clear();
        vertBuf.put(vertsArray);
        vertBuf.flip();
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, l, byteBuf,
                GLES20.GL_STATIC_DRAW);
        GlError.check("Unable to fill vertex buffer");
        l = indexArray.length;
        byteBuf = ByteBuffer.allocateDirect(l);
        byteBuf.clear();
        byteBuf.put(indexArray);
        byteBuf.flip();
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, l, byteBuf,
                GLES20.GL_STATIC_DRAW);
        GlError.check("Unable to fill index buffer");
    }

    private static final int TEXTURE_UNIT = GLES20.GL_TEXTURE0;
    private AndroidGLES20Texture crateTexture;
    private int[] buffers = { 0, 0 };
    private Shader shader;
    private float[] mvpMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private int u_mvp = -1;

    private class CubeTextureRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            try {
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                GLES20.glEnable(GLES20.GL_CULL_FACE);
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                Log.d(TAG, "Set initial state");
                makeBuffers();
                Log.d(TAG, "Made buffers");
                shader = new BasicShader();
                Log.d(TAG, "Created shader");
                int shaderId = shader.getGlShaderProgram();
                GLES20.glUseProgram(shaderId);
                u_mvp = shader.getUniformLocation(Shader.U_MVP);
                int u_tex = shader.getUniformLocation(Shader.U_TEXTURE_UNIT);
                GLES20.glUniform1i(u_tex, 0);
                GlError.check("Error initialising texture unit uniform");
                int a_position = shader.getAttribLocation(Shader.A_POSITION);
                int a_tex_coord = shader.getAttribLocation(Shader.A_TEX_COORD);
                GLES20.glEnableVertexAttribArray(a_position);
                GLES20.glVertexAttribPointer(a_position, 3,
                        GLES20.GL_FLOAT, false,
                        STRIDE, POS_OFFSET);
                GLES20.glEnableVertexAttribArray(a_tex_coord);
                GLES20.glVertexAttribPointer(a_tex_coord, 2,
                        GLES20.GL_FLOAT, false,
                        STRIDE, TEX_OFFSET);
                Log.d(TAG, "Initialised shader");
                crateTexture.uploadtoGPU();
                Log.d(TAG, "Uploaded texture");
            } catch (Throwable e) {
                Log.e(TAG, "Exception in onSurfaceCreated", e);
                stop();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            try {
                int size = width > height ? height : width;
                GLES20.glViewport((width - size) / 2, (height - size) / 2,
                        size, size);
                GlError.check("Failed to set glViewport");
                Log.d(TAG, "Set viewport size " + size);
                /*
                Matrix.perspectiveM(projectionMatrix, 0,
                        70, 1, 1, 1000);
                Matrix.setLookAtM(viewMatrix, 0,
                        0, 0, CAMERA_Z,
                        0, 0, 0,
                        0, 1, 0);
                Matrix.multiplyMM(mvpMatrix, 0,
                        viewMatrix, 0, projectionMatrix, 0);
                */
                Matrix.setIdentityM(mvpMatrix, 0);
                GLES20.glUniformMatrix4fv(u_mvp, 1, false, mvpMatrix, 0);
                GlError.check("Unable to set MVP uniform");
                Log.d(TAG, "Set MVP in shader");
            } catch (Throwable e) {
                Log.e(TAG, "Exception in onSurfaceChanged", e);
                stop();
            }
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            try {
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT |
                        GLES20.GL_DEPTH_BUFFER_BIT);
                GlError.check("Error clearing view");
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexArray.length,
                        GLES20.GL_UNSIGNED_BYTE, 0);
                GlError.check("Rendering error");
            } catch (Throwable e) {
                Log.e(TAG, "Exception in onDrawFrame", e);
                stop();
            }
        }

        private void stop() {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                public void run() {
                    CubeTextureActivity.this.finish();
                }
            } );
        }
    }
}
