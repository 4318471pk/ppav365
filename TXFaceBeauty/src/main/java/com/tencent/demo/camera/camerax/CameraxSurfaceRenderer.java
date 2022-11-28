package com.tencent.demo.camera.camerax;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.tencent.demo.camera.glrender.CropRenderer;
import com.tencent.demo.camera.glrender.DirectRenderer;
import com.tencent.demo.camera.glrender.OESRenderer;
import com.tencent.demo.camera.gltexture.GLTextureView;
import com.tencent.demo.log.LogUtils;
import com.tencent.demo.utils.GlUtil;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



/**
 * Renderer object for our GLSurfaceView.
 * <p>
 * Do not call any methods here directly from another thread -- use the
 * GLSurfaceView#queueEvent() call.
 */
public abstract class CameraxSurfaceRenderer implements GLSurfaceView.Renderer, GLTextureView.Renderer{

    private static final String TAG = CameraxSurfaceRenderer.class.getSimpleName();
    private static final boolean VERBOSE = false;

    // private FullFrameRect mFullScreen;

    private final float[] mOesMatrix = new float[16];
    private float[] mCropMatrix = new float[16];

    private SurfaceTexture mSurfaceTexture;

    private int mCameraOesTextureId = -1;
    private int mConvertedTextureId = -1;
    private int mCropedTextureId = -1;

    private OESRenderer mYUV2RGBAConverter;
    private DirectRenderer mScreenRenderer;
    private CropRenderer mCropRenderer;

    /**
     * 根据显示方向旋转后的预览帧宽度
     */
    private int mCameraPreviewFrameWidth = -1;
    /**
     * 根据显示方向旋转后的预览帧高度
     */
    private int mCameraPreviewFrameHeight = -1;
    private boolean mCameraPreviewFrameSizeChanged = false;

    private int mSurfaceWidth = -1;
    private int mSurfaceHeight = -1;
    private boolean mSurfaceSizeChanged = false;

    private CustomTextureProcessor mCustomTextureProcessor;
    private int mCroppedImgW;
    private int mCroppedImgH;

    public boolean isBackCamera = false;

    /**
     * Notifies the renderer thread that the activity is pausing.
     * <p>
     * For best results, call this *after* disabling Camera preview.
     */
    public void release() {
        if (mSurfaceTexture != null) {
            LogUtils.d(TAG, "renderer pausing -- releasing SurfaceTexture");
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCameraOesTextureId = releaseTexture(mCameraOesTextureId);
        releaseRenderer();

        mCameraPreviewFrameWidth = mCameraPreviewFrameHeight = -1;
    }


    public void change(int width, int height, int cameraDisplayOrientation) {
        LogUtils.d(TAG, " cameraDisplayOrientation " + cameraDisplayOrientation);
//        if(mYUV2RGBAConverter!=null){
//            mYUV2RGBAConverter.cameraDisplayOrientation = cameraDisplayOrientation;
//            mYUV2RGBAConverter.isBackCamera = isBackCamera;
//        }
//        if (cameraDisplayOrientation == 90 || cameraDisplayOrientation == 270) {//竖屏
            mCameraPreviewFrameWidth = Math.min(width, height);
            mCameraPreviewFrameHeight = Math.max(width, height);
            LogUtils.d(TAG,"width "+width+" height"+height+"  "+mCameraPreviewFrameWidth+"  "+mCameraPreviewFrameHeight);
//        } else {//横屏
//            mCameraPreviewFrameWidth = Math.max(width, height);
//            mCameraPreviewFrameHeight = Math.min(width, height);
//        }
        mCameraPreviewFrameSizeChanged = true;
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
        LogUtils.d(TAG, "onSurfaceCreated");

        mYUV2RGBAConverter = new OESRenderer();
        mYUV2RGBAConverter.init();
        mScreenRenderer = new DirectRenderer();
        mScreenRenderer.init();
        mCropRenderer = new CropRenderer();
        mCropRenderer.init();

        releaseTexture(mCameraOesTextureId);
        mCameraOesTextureId = createOesTexture();

        mSurfaceTexture = new SurfaceTexture(mCameraOesTextureId);

        onSurfaceTextureCreated(mSurfaceTexture);
    }

    protected abstract void onSurfaceTextureCreated(SurfaceTexture surfaceTexture);

    @SuppressLint("RestrictedApi")
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        LogUtils.d(TAG, "onSurfaceChanged() called with: gl10 = [" + gl10 + "], width = [" + width + "], height = [" + height + "]");
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mSurfaceSizeChanged = true;

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        this.onGlTextureViewRenderDrawFrame(unused);
    }

    @Override
    public boolean onGlTextureViewRenderDrawFrame(GL10 unused) {
        if (mSurfaceTexture == null) {
            return false;
        }
        mSurfaceTexture.updateTexImage();


        if (mCameraPreviewFrameWidth <= 0 || mCameraPreviewFrameHeight <= 0) {
            // Texture size isn't set yet.  This is only used for the filters, but to be
            // safe we can just skip drawing while we wait for the various races to resolve.
            // (This seems to happen if you toggle the screen off/on with power button.)
            LogUtils.d(TAG, "Drawing before incoming texture size set; skipping");
            return false;
        }

        if (mCameraPreviewFrameSizeChanged || mSurfaceSizeChanged) {
            mCameraPreviewFrameSizeChanged = false;
            mSurfaceSizeChanged = false;

            //重新创建用于相机 OES => RGBA 的纹理
            releaseTexture(mConvertedTextureId);
            mConvertedTextureId = GlUtil.createTexture(mCameraPreviewFrameWidth, mCameraPreviewFrameHeight, GLES20.GL_RGBA);

            //重新计算裁剪后的图像尺寸
            float imgRatio = mCameraPreviewFrameWidth * 1f / mCameraPreviewFrameHeight;
            float surfaceRatio = mSurfaceWidth * 1f / mSurfaceHeight;

            mCroppedImgW = mCameraPreviewFrameWidth;
            mCroppedImgH = mCameraPreviewFrameHeight;
            if (imgRatio > surfaceRatio) {//图像比屏幕宽, 计算图像新宽度
                mCroppedImgW = (int) (mCameraPreviewFrameHeight * surfaceRatio);
            } else if (imgRatio < surfaceRatio) {//图像比屏幕高, 计算图像新高度
                mCroppedImgH = (int) (mCameraPreviewFrameWidth * (1f / surfaceRatio));
            }

            //重新创建裁剪纹理
            releaseTexture(mCropedTextureId);
            mCropedTextureId = GlUtil.createTexture(mCroppedImgW, mCroppedImgH, GLES20.GL_RGBA);

            //重新计算裁剪矩阵
            computeCropMatrix(mCameraPreviewFrameWidth, mCameraPreviewFrameHeight, mCroppedImgW, mCroppedImgH, mCropMatrix);
        }

        //相机 OES => RGBA 的纹理
        mSurfaceTexture.getTransformMatrix(mOesMatrix);
        mYUV2RGBAConverter.doRender(mCameraOesTextureId, mConvertedTextureId, mCameraPreviewFrameWidth, mCameraPreviewFrameHeight, mOesMatrix, null, null);
        // Bitmap bitmapRGB = GlUtil.readTexture(mConvertedTextureId, mCameraPreviewFrameWidth, mCameraPreviewFrameHeight);

        //裁剪纹理
        mCropRenderer.doRender(mConvertedTextureId, mCropedTextureId, mCroppedImgW, mCroppedImgH, mCropMatrix, null);
        // Bitmap bitmapCropped = GlUtil.readTexture(mCropedTextureId, mCroppedImgW, mCroppedImgH);

        // 外部自定义纹理处理
        int outTexture = mCropedTextureId;
        if (mCustomTextureProcessor != null) {
            outTexture = mCustomTextureProcessor.onCustomProcessTexture(mCropedTextureId, mCroppedImgW, mCroppedImgH);
        }
        // 纹理上屏
        mScreenRenderer.doRender(outTexture, -1, mSurfaceWidth, mSurfaceHeight, null, null, null);
        return true;
    }

    @Override
    public void onSurfaceDestroyed() {
        release();
    }

    private int releaseTexture(int id) {
        if (id >= 0) {
            GLES20.glDeleteTextures(1, new int[]{id}, 0);
        }
        return -1;
    }

    public int createOesTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GlUtil.checkGlError("glGenTextures1");

        int texId = textures[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texId);
        GlUtil.checkGlError("glBindTexture2 " + texId);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GlUtil.checkGlError("glTexParameter3");

        return texId;
    }


    private void releaseRenderer() {

        if (mYUV2RGBAConverter != null) {
            mYUV2RGBAConverter.release();
            mYUV2RGBAConverter = null;
        }

        if (mScreenRenderer != null) {
            mScreenRenderer.release();
            mScreenRenderer = null;
        }
        if (mCropRenderer != null) {
            mCropRenderer.release();
            mCropRenderer = null;
        }
    }

    /**
     * 设置自定义纹理处理器
     */
    public void setCustomTextureProcessor(CustomTextureProcessor customTextureProcessor) {
        mCustomTextureProcessor = customTextureProcessor;
    }





    private static void computeCropMatrix(int cameraWidth, int cameraHeight, int screenWidth, int screenHeight, float[] matrix) {
        float scaleX = 1;
        float scaleY = 1;
        float translateX = 0;
        float translateY = 0;
        Matrix.setIdentityM(matrix, 0);
        //归一化的宽高
        float cameraNormalizedW = cameraWidth * 1f / cameraHeight;
        float screenNormalizedW = screenWidth * 1f / screenHeight;
        float cameraNormalizedH = cameraHeight * 1f / cameraWidth;
        float screenNormalizedH = screenHeight * 1f / screenWidth;
        if (cameraNormalizedW > screenNormalizedW) {//图像比屏幕宽,  如果不调整, 图像宽度会被压缩, 放大(新坐标=原坐标*小于1的系数)图像宽度以抵消
            scaleX = screenNormalizedW / cameraNormalizedW;
            translateX = (cameraNormalizedW - screenNormalizedW) / 2;//偏移宽度差的一半, 实现中心裁剪
        } else if (cameraNormalizedW < screenNormalizedW) {//图像比屏幕高, 如果不调整, 图像高度会被压缩, 放大(新坐标=原坐标*小于1的系数)图像高度以抵消
            scaleY = screenNormalizedH / cameraNormalizedH;
            translateY = (cameraNormalizedH - screenNormalizedH) / 2;//偏移高度差的一半, 实现中心裁剪
        }

        Matrix.translateM(matrix, 0, translateX, translateY, 0);
        Matrix.scaleM(matrix, 0, scaleX, scaleY, 1);
    }


}
