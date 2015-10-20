package uk.co.realh.parallax3d;

/**
 * Created by tony on 15/10/15.
 */
public class BasicShader extends Shader {

    private final static String vertSrc =
            "#version 100\n" +
            "precision mediump float;\n" +
            "attribute vec2 " + A_POSITION + ";\n" +
            "attribute vec2 " + A_TEX_COORD + ";\n" +
            "uniform mat4 " + U_MVP + ";\n" +
            "varying vec2 v_tex_coord;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = " + U_MVP + " * vec4(position, 0.0, 1.0);\n" +
            "    v_tex_coord = " + A_TEX_COORD + ";\n" +
            "}\n";

    private final static String fragSrc =
            "#version 100\n" +
            "precision lowp float;\n" +
            "uniform sampler2D " + U_TEXTURE_UNIT + ";\n" +
            "varying vec2 v_tex_coord;\n" +
            "void main(void) {\n" +
            "    gl_FragColor = texture2D(" +
                    U_TEXTURE_UNIT + ", v_tex_coord);\n" +
            "}\n";

    public BasicShader() {
        super(vertSrc, fragSrc);
    }
}
