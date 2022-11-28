package com.tencent.demo.camera.glrender;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.ByteBuffer;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;

// 用于OES纹理向RGBA纹理的转换
public class OESRenderer extends BaseRenderer{

    private static final String FRAG_SHADER = "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "uniform samplerExternalOES uTextureSampler;\n" +
            "varying vec2 textureCoordinate;\n" +
            "void main()\n" +
            "{\n" +
            "  vec4 vCameraColor = texture2D(uTextureSampler, textureCoordinate);\n" +
            "  gl_FragColor = vCameraColor;\n" +
            "}\n";

    private static final String TAG = "OESRenderer";

    private int oesTextureLocation = -1;

    public void init(){
        super.init(DEFAULT_VERT_SHADER, FRAG_SHADER);

        oesTextureLocation = glGetUniformLocation(mShaderProgram, "uTextureSampler");
    }

    public void doRender(int srcTextureId, int desTextureId, int frameWidth, int frameHeight, float[] srcTransformMatrix, float[] destTransformMatrix, ByteBuffer buffer)
    {
        super.beforeRender(desTextureId, frameWidth, frameHeight, srcTransformMatrix, destTransformMatrix);
        super.setVertex();

        glActiveTexture(GLES20.GL_TEXTURE0);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, srcTextureId);
        glUniform1i(oesTextureLocation, 0);

        glDrawArrays(GL_TRIANGLES, 0, 6);

        saveToBuffer(buffer);
    }

}
