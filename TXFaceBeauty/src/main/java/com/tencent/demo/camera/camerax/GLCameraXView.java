package com.tencent.demo.camera.camerax;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import com.tencent.demo.R;
import com.tencent.demo.camera.gltexture.GLTextureView;
import com.tencent.demo.log.LogUtils;
import java.util.concurrent.ExecutionException;



/**
 *demo中默认使用此类进行相机预览处理
 */

public class GLCameraXView extends FrameLayout {

    private static final String TAG = GLCameraXView.class.getName();
    private GLTextureView glTextureView = null;
    private GLSurfaceView glSurfaceView = null;

    private CameraxPreviewManager previewManager = null;
    private CameraXLifecycleOwner cameraxLifecycle = null;

    private ProcessCameraProvider cameraProvider = null;
    private Preview preview = null;

    private boolean isSurfaceView = false;
    private boolean isBackCamera = true;
    private boolean isTransparent = false;
    private float ratio = 0f;//宽高比


    private OrientationEventListener orientationEventListener = null;
    private Size size = null;
    private GLCameraXViewErrorListener glCameraXViewErrorListener = null;

    public GLCameraXView(@NonNull Context context) {
        this(context, null);
    }

    public GLCameraXView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLCameraXView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            getParameter(context, attrs);
        }
        this.init(context);
    }

    /**
     * 获取xml文件中的配置信息
     * @param context
     * @param attrs
     */
    private void getParameter(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GLCameraView);
        isBackCamera = typedArray.getBoolean(R.styleable.GLCameraView_back_camera, true);
        isSurfaceView = typedArray.getBoolean(R.styleable.GLCameraView_surface_view, true);
        isTransparent = typedArray.getBoolean(R.styleable.GLCameraView_transparent, false);
        ratio = typedArray.getFloat(R.styleable.GLCameraView_ratio, 0f);
        typedArray.recycle();
    }

    private void init(Context context) {
        previewManager = new CameraxPreviewManager();
        if (isSurfaceView) {
            glSurfaceView = new GLSurfaceView(context);
            this.removeAllViews();
            this.addView(glSurfaceView);
            previewManager.onCreate(glSurfaceView, isTransparent, isBackCamera);
        } else {
            glTextureView = new GLTextureView(context);
            this.removeAllViews();
            this.addView(glTextureView);
            previewManager.onCreate(glTextureView, isTransparent, isBackCamera);
        }
        cameraxLifecycle = new CameraXLifecycleOwner();
        cameraxLifecycle.doOnCreate();
    }

    public void setCameraSize(Size size) {
        this.size = size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (ratio > 0) {
            // 宽模式
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            // 宽大小
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            // 高大小
            int heightSize;
            // 只有宽的值是精确的才对高做精确的比例校对
            if (widthMode == MeasureSpec.EXACTLY && ratio > 0) {
                heightSize = (int) (widthSize / ratio + 0.5f);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 开启相机预览
     */
    public void startPreview() {
        cameraxLifecycle.doOnStart();
        cameraxLifecycle.doOnResume();
        if (orientationEventListener != null) {
            orientationEventListener.enable();
        }
        post(this::initCameraWhenCreated);
    }

    /**
     * 停止相机预览
     */
    public void stopPreview() {
        cameraxLifecycle.doOnPause();
        cameraxLifecycle.doOnStop();
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
    }


    /**
     * 销毁资源
     */
    public void release() {
        if (previewManager != null) {
            previewManager.destroy();
        }
        cameraxLifecycle.doOnDestroy();
        removeAllViews();
    }

    private void initCameraWhenCreated() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext().getApplicationContext());
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            if (this.size != null) {
                preview = new Preview.Builder().setTargetResolution(this.size).build();
            } else {
                preview = new Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build();
            }
            preview.setSurfaceProvider(previewManager);
            setupCamera();
        }, ContextCompat.getMainExecutor(getContext()));
        if (orientationEventListener == null) {
            orientationEventListener = new OrientationEventListener(GLCameraXView.this.getContext()) {
                @SuppressLint("UnsafeOptInUsageError")
                @Override
                public void onOrientationChanged(int orientation) {
                    if (orientation == ORIENTATION_UNKNOWN) {
                        return;
                    }
                    int rotation = 0;
                    if (orientation >= 45 && orientation < 135) {
                        rotation = Surface.ROTATION_270;
                    } else if (orientation >= 135 && orientation < 225) {
                        rotation = Surface.ROTATION_180;
                    } else if (orientation >= 225 && orientation < 315) {
                        rotation = Surface.ROTATION_90;
                    } else {
                        rotation = Surface.ROTATION_0;
                    }

                    if (preview != null) {
                        preview.setTargetRotation(rotation);
                    }
                }
            };
            orientationEventListener.enable();
        }
    }


    public void setupCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(isBackCamera ? CameraSelector.LENS_FACING_BACK : CameraSelector.LENS_FACING_FRONT)
                    .build();
            boolean hasCamera = false;
            try {
                hasCamera = cameraProvider.hasCamera(cameraSelector);
            } catch (CameraInfoUnavailableException e) {
                e.printStackTrace();
            }
            //使用camera之前先判断camera是否可用
            if (hasCamera) {
                cameraProvider.bindToLifecycle(
                        cameraxLifecycle,
                        cameraSelector,
                        preview
                );
            }else {
                LogUtils.d(TAG,"camera unavailable ");
                if (this.glCameraXViewErrorListener != null) {
                    this.glCameraXViewErrorListener.onError();
                }
            }
        }
    }

    /**
     * 设置回调
     *
     * @param customTextureProcessor
     */
    public void setCustomTextureProcessor(CustomTextureProcessor customTextureProcessor) {
        if (previewManager != null) {
            previewManager.setCustomTextureProcessor(customTextureProcessor);
        }
    }

    /**
     * 切换前后摄像头
     */
    public void switchCamera() {
        isBackCamera = !isBackCamera;
        setupCamera();
    }

    public void setIsBackCamera(boolean isBackCamera) {
        this.isBackCamera = isBackCamera;
    }

    public void setGlCameraXViewErrorListener(GLCameraXViewErrorListener glCameraXViewErrorListener) {
        this.glCameraXViewErrorListener = glCameraXViewErrorListener;
    }

    public interface GLCameraXViewErrorListener {
        void onError();
    }
}
