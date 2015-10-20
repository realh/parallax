package uk.co.realh.parallax3d;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by tony on 15/10/15.
 */
public class AndroidGLES20Texture {

    private static final String TAG = "Parallax3D";

    private final Bitmap bitmap;

    static public AndroidGLES20Texture fromAsset(AssetManager assets,
                                                 String filename)
            throws IOException {
        InputStream strm = assets.open(filename);
        Bitmap bmp = BitmapFactory.decodeStream(strm);
        strm.close();
        if (null == bmp) {
            throw new RuntimeException("Unable to create bitmap from asset '" +
                    filename + "'");
        }
        if (null == bmp.getConfig()) {
            Log.d(TAG, "Bitmap has unsupported format, trying to convert");
            bmp = bmp.copy(Bitmap.Config.RGB_565, true);
        }
        AndroidGLES20Texture tex = new AndroidGLES20Texture(bmp);
        Log.d(TAG, "Created AndroidGLES20Texture from asset " + filename +
                        ": " + tex.getDescription());
        return tex;
    }

    public AndroidGLES20Texture(Bitmap bmp) {
        bitmap = bmp;
    }

    public String getDescription() {
        return String.format(
                "Android format %s, gl format 0x%x, word type 0x%x",
                bitmap.getConfig().toString(), getGlFormat(), getGlWordType());
    }

    public void uploadtoGPU() {
        Buffer buffer = ByteBuffer.allocate(bitmap.getByteCount()).
                order(ByteOrder.nativeOrder());
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GlError.check("Failed to set parameters on new texture");
        int format = getGlFormat();
        bitmap.copyPixelsToBuffer(buffer);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, format,
                bitmap.getWidth(), bitmap.getHeight(), 0,
                format, getGlWordType(), buffer);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GlError.check("Failed to generate mipmaps");
        buffer.clear();
    }

    public int getGlFormat() {
        if (bitmap.getConfig() == Bitmap.Config.ALPHA_8)
            return GLES20.GL_LUMINANCE_ALPHA;
        else
            return hasAlpha() ? GLES20.GL_RGBA : GLES20.GL_RGB;
    }

    public boolean hasAlpha() {
        return bitmap.hasAlpha();
    }

    public int getGlWordType() {
        switch (bitmap.getConfig()) {
            case ALPHA_8:
                return GLES20.GL_UNSIGNED_BYTE;
            case ARGB_4444:
                return GLES20.GL_UNSIGNED_SHORT_4_4_4_4;
            case ARGB_8888:
                return GLES20.GL_UNSIGNED_BYTE;
            case RGB_565:
                return GLES20.GL_UNSIGNED_SHORT_5_6_5;
        }
        return 0;
    }
}
