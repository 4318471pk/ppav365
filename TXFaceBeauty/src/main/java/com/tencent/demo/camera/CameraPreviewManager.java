package com.tencent.demo.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera.Size;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import com.tencent.demo.camera.gltexture.GLTextureView;
import com.tencent.demo.camera.gltexture.GLES20ContextFactory;
import com.tencent.demo.camera.CameraSurfaceRenderer.CustomTextureProcessor;



public class CameraPreviewManager {

    private static final String TAG = CameraPreviewManager.class.getSimpleName();

    private CameraMgr mCameraMgr;
    private GLSurfaceView glSurfaceView;
    private GLTextureView glTextureView;
    private CameraSurfaceRenderer mRenderer;

    public void onCreate(GLSurfaceView glSurfaceView) {
        mCameraMgr = new CameraMgr();
        this.glSurfaceView = glSurfaceView;
        this.glSurfaceView.setEGLContextClientVersion(2);     // select GLES 2.0
        mRenderer = new CameraSurfaceRenderer() {

            @Override
            protected void onSurfaceTextureCreated(final SurfaceTexture surfaceTexture) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    surfaceTexture.setOnFrameAvailableListener(st -> CameraPreviewManager.this.glSurfaceView.requestRender());
                    mCameraMgr.startPreview(surfaceTexture);
                });
            }
        };

        this.glSurfaceView.setRenderer(mRenderer);
        this.glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void onCreate(GLTextureView glTextureView, boolean isTranslation) {
        mCameraMgr = new CameraMgr();
        this.glTextureView = glTextureView;
        this.glTextureView.setEGLContextClientVersion(2);     // select GLES 2.0
        mRenderer = new CameraSurfaceRenderer() {

            @Override
            protected void onSurfaceTextureCreated(final SurfaceTexture surfaceTexture) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    surfaceTexture.setOnFrameAvailableListener(st -> CameraPreviewManager.this.glTextureView.requestRender());
                    mCameraMgr.startPreview(surfaceTexture);
                });
            }
        };
        if (isTranslation) {
            this.glTextureView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            this.glTextureView.setEGLContextFactory(new GLES20ContextFactory());
            this.glTextureView.setOpaque(false);
        }
        this.glTextureView.setRenderer(mRenderer);
        this.glTextureView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /**
     * 设置自定义纹理处理器
     */
    public void setCustomTextureProcessor(CustomTextureProcessor customTextureProcessor) {
        mRenderer.setCustomTextureProcessor(customTextureProcessor);
    }

    public void onResume(Activity activity, int expectCameraPreviewWidth, int expectCameraPreviewHeight) {
        final Size size = mCameraMgr.openCamera(activity, expectCameraPreviewWidth, expectCameraPreviewHeight);//TODO 处理onResume时无权限情况
        if (glSurfaceView != null) {
            glSurfaceView.onResume();
        }
        if (glTextureView != null) {
            glTextureView.onResume();
        }
        mRenderer.setCameraPreviewSize(size.width, size.height, mCameraMgr.getCameraDisplayOrientation());
    }

    public void onPause() {
        if (glSurfaceView != null) {
            glSurfaceView.queueEvent(() -> {
                mRenderer.release();
            });
            glSurfaceView.onPause();
        }
        if (glTextureView != null) {
            glTextureView.queueEvent(() -> {
                mRenderer.release();
            });
            glTextureView.onPause();
        }
    }

    public void releaseCamera(){
        mCameraMgr.releaseCamera();
    }
}
