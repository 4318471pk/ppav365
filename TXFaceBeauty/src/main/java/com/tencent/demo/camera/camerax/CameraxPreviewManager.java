package com.tencent.demo.camera.camerax;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.Size;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceRequest;
import com.tencent.demo.camera.gltexture.GLES20ContextFactory;
import com.tencent.demo.camera.gltexture.GLTextureView;
import java.util.concurrent.Executors;


class CameraxPreviewManager implements Preview.SurfaceProvider {


    private GLSurfaceView glSurfaceView;
    private GLTextureView glTextureView;
    private CameraxSurfaceRenderer mRenderer;
    private CustomTextureProcessor customTextureProcessor = null;
    private SurfaceTexture mSurfaceTexture = null;
//    private boolean isRelease = false;

    public void onCreate(GLSurfaceView glSurfaceView, boolean isTransparent, boolean isBackCamera) {
        this.glSurfaceView = glSurfaceView;
        this.glSurfaceView.setEGLContextClientVersion(2);     // select GLES 2.0
        mRenderer = new CameraxSurfaceRenderer() {

            @Override
            protected void onSurfaceTextureCreated(SurfaceTexture surfaceTexture) {
                if (customTextureProcessor != null) {
                    customTextureProcessor.onGLContextCreated();
                }
                mSurfaceTexture = surfaceTexture;
                setOnFrameAvailableListener(surfaceTexture);
            }
        };
        mRenderer.isBackCamera = isBackCamera;
        if (isTransparent) {
            this.glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            this.glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//            this.glSurfaceView.setZOrderOnTop(true);
        }
        this.glSurfaceView.setRenderer(mRenderer);
        this.glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void onCreate(GLTextureView glTextureView, boolean isTransparent, boolean isBackCamera) {
        this.glTextureView = glTextureView;
        this.glTextureView.setEGLContextClientVersion(2);     // select GLES 2.0
        mRenderer = new CameraxSurfaceRenderer() {

            @Override
            protected void onSurfaceTextureCreated(SurfaceTexture surfaceTexture) {
                if (customTextureProcessor != null) {
                    customTextureProcessor.onGLContextCreated();
                }
                mSurfaceTexture = surfaceTexture;
                setOnFrameAvailableListener(surfaceTexture);
            }
        };
        mRenderer.isBackCamera = isBackCamera;
        if (isTransparent) {
            this.glTextureView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            this.glTextureView.setEGLContextFactory(new GLES20ContextFactory());
            this.glTextureView.setOpaque(false);
        }
        this.glTextureView.setRenderer(mRenderer);
        this.glTextureView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    private void setOnFrameAvailableListener(SurfaceTexture surfaceTexture) {
        surfaceTexture.setOnFrameAvailableListener(st -> {
            if (glSurfaceView != null) {
                glSurfaceView.requestRender();
            }
            if (glTextureView != null) {
                glTextureView.requestRender();
            }
        });
    }


    /**
     * 设置自定义纹理处理器
     */
    public void setCustomTextureProcessor(CustomTextureProcessor customTextureProcessor) {
        this.customTextureProcessor = customTextureProcessor;
        if (mRenderer == null) {
            return;
        }
        mRenderer.setCustomTextureProcessor(customTextureProcessor);
    }


    public void destroy() {
        if (mRenderer != null) {
            if (glSurfaceView != null) {
                glSurfaceView.queueEvent(() -> {
                    mRenderer.release();
                    if (customTextureProcessor != null) {
                        customTextureProcessor.onGLContextDestroy();
                    }
                });
                glSurfaceView.onPause();
            }
            if (glTextureView != null) {
                glTextureView.queueEvent(() -> {
                    mRenderer.release();
                    if (customTextureProcessor != null) {
                        customTextureProcessor.onGLContextDestroy();
                    }
                });
                glTextureView.onPause();
            }
        }
//        isRelease = true;
    }

    public void setIsBackCamera(boolean isBackCamera) {
        if (mRenderer != null) {
            mRenderer.isBackCamera = isBackCamera;
        }
    }


    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public void onSurfaceRequested(@NonNull SurfaceRequest request) {
        Size size = request.getResolution();
        request.setTransformationInfoListener(Executors.newSingleThreadExecutor(), transformationInfo -> {
                    mRenderer.change(size.getWidth(), size.getHeight(), transformationInfo.getRotationDegrees());
                }
        );

        if (mSurfaceTexture == null) {
            return;
        } else {
            mSurfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
        }
        Surface surface = new Surface(mSurfaceTexture);
        request.provideSurface(surface, Executors.newSingleThreadExecutor(), result -> {
            if (result.getResultCode() == SurfaceRequest.Result.RESULT_SURFACE_USED_SUCCESSFULLY) {
                result.getSurface().release();
//                mRenderer.release();
            }
        });
    }
}
