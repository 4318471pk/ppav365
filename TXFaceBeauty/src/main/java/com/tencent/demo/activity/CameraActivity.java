package com.tencent.demo.activity;


import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.tencent.demo.R;
import com.tencent.demo.XMagicImpl;
import com.tencent.demo.camera.CameraPreviewManager;
import com.tencent.demo.camera.gltexture.GLTextureView;
import com.tencent.demo.panel.XmagicPanelDataManager;
import com.tencent.demo.utils.PermissionHandler;
import com.tencent.demo.widget.DraggableViewGroup;
import com.tencent.xmagic.XmagicProperty;


public class CameraActivity extends AppCompatActivity implements XMagicImpl.XmagicImplCallback {

    private static final String TAG = "CameraActivity";

    private XMagicImpl mXmagicApi;
    private CameraPreviewManager floatPreviewManager = null;
    private CameraPreviewManager commonPreviewManager = null;

    private GLSurfaceView commonGLSurfaceView;
    private GLTextureView floatGLSurfaceView;

    private RelativeLayout panelLayout = null;
    private DraggableViewGroup draggableViewGroup = null;

    private boolean mIsResumed = false;
    private boolean mIsPermissionGranted = false;
    private boolean isSupportBeauty;

    private int selectedSurfaceViewId = COMMON_SURFACE_VIEW_ID;   //1表示全屏的surfaceView 2表示非全屏surfaceView
    private static final int COMMON_SURFACE_VIEW_ID = 1;
    private static final int FLOAT_SURFACE_VIEW_ID = 2;

    private int expectCameraPreviewWidth;
    private int expectCameraPreviewHeight;
    private int floatGLSurfaceViewPreviewWidth = 0;
    private int floatGLSurfaceViewPreviewHeight = 0;

    private final PermissionHandler mPermissionHandler = new PermissionHandler(this) {

        @Override
        protected void onAllPermissionGranted() {
            mIsPermissionGranted = true;
            tryResume();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity_layout);
        expectCameraPreviewWidth = getIntent().getIntExtra(CameraPreviewConfig.KEY_PREVIEW_WIDTH, 1280);
        expectCameraPreviewHeight = getIntent().getIntExtra(CameraPreviewConfig.KEY_PREVIEW_HEIGHT, 720);
        floatGLSurfaceViewPreviewWidth = getResources().getDisplayMetrics().widthPixels / 2;
        floatGLSurfaceViewPreviewHeight = getResources().getDisplayMetrics().heightPixels / 2;

        panelLayout = findViewById(R.id.panel_layout);
        commonGLSurfaceView = findViewById(R.id.common_glsurface_view);
        floatGLSurfaceView = findViewById(R.id.float_surface_view);
        draggableViewGroup = findViewById(R.id.float_surface_view_layout);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) floatGLSurfaceView.getLayoutParams();
        layoutParams.width = floatGLSurfaceViewPreviewWidth;
        layoutParams.height = floatGLSurfaceViewPreviewHeight;
        floatGLSurfaceView.setLayoutParams(layoutParams);
        findViewById(R.id.back_btn).setOnClickListener(v -> finish());
        // 检查和请求系统权限  check or request camera permission
        mPermissionHandler.start();
        createCameraPreviewMgr();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);// 必须有这个调用, mPermissionHandler 才能正常工作
    }


    private void createCameraPreviewMgr() {
        commonPreviewManager = new CameraPreviewManager();
        commonPreviewManager.onCreate(commonGLSurfaceView);
        commonPreviewManager.setCustomTextureProcessor((textureId, textureWidth, textureHeight) -> {
            if (selectedSurfaceViewId == COMMON_SURFACE_VIEW_ID && isSupportBeauty && mXmagicApi != null) {
                return mXmagicApi.process(textureId, textureWidth, textureHeight);
            } else {
                return textureId;
            }
        });
        floatPreviewManager = new CameraPreviewManager();
        floatPreviewManager.onCreate(floatGLSurfaceView, true);
        floatPreviewManager.setCustomTextureProcessor((textureId, textureWidth, textureHeight) -> {
            if (selectedSurfaceViewId == FLOAT_SURFACE_VIEW_ID && isSupportBeauty && mXmagicApi != null) {
                return mXmagicApi.process(textureId, textureWidth, textureHeight);
            } else {
                return textureId;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsResumed = true;
        tryResume();
    }

    private void tryResume() {
        if (mIsResumed && mIsPermissionGranted) {
            if (selectedSurfaceViewId == COMMON_SURFACE_VIEW_ID) {
                commonPreviewManager.onResume(this, expectCameraPreviewWidth, expectCameraPreviewHeight);
            } else if (selectedSurfaceViewId == FLOAT_SURFACE_VIEW_ID) {
                floatPreviewManager.onResume(this, floatGLSurfaceViewPreviewWidth, floatGLSurfaceViewPreviewHeight);
            }
            initXMagic();
        }
    }


    @Override
    public boolean onUpdateProperty(XmagicProperty xmagicProperty) {
        if (xmagicProperty == null) {
            return true;
        }
        if (xmagicProperty.category == XmagicProperty.Category.SEGMENTATION) {
            if ("video_segmentation_transparent_bg".equals(xmagicProperty.id)) {  //点击了透明背景素材 on click transparent bg
                if (selectedSurfaceViewId != FLOAT_SURFACE_VIEW_ID) {
                    pause();
                    draggableViewGroup.setVisibility(View.VISIBLE);
                    selectedSurfaceViewId = FLOAT_SURFACE_VIEW_ID;
                    tryResume();
                    return true;
                }
            } else if ("video_segmentation_blur_75".equals(xmagicProperty.id)) {    //背景模糊-强  on click blurred background-severe
                if (selectedSurfaceViewId != COMMON_SURFACE_VIEW_ID) {
                    pause();
                    draggableViewGroup.setVisibility(View.INVISIBLE);
                    selectedSurfaceViewId = COMMON_SURFACE_VIEW_ID;
                    tryResume();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mXmagicApi != null) {
            mXmagicApi.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsResumed = false;
        pause();
    }

    private void pause() {
        if (selectedSurfaceViewId == COMMON_SURFACE_VIEW_ID) {
            commonPreviewManager.releaseCamera();
            commonGLSurfaceView.queueEvent(() -> {
                if (mXmagicApi != null) {
                    mXmagicApi.onPause();
                    mXmagicApi.onDestroy();
                }
            });
            commonPreviewManager.onPause();
        } else if (selectedSurfaceViewId == FLOAT_SURFACE_VIEW_ID) {
            floatPreviewManager.releaseCamera();
            floatGLSurfaceView.queueEvent(() -> {
                if (mXmagicApi != null) {
                    mXmagicApi.onPause();
                    mXmagicApi.onDestroy();
                }
            });
            floatPreviewManager.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        XmagicPanelDataManager.getInstance().clearData();
        super.onDestroy();
    }


    private void initXMagic() {
        if (mXmagicApi == null) {
            mXmagicApi = new XMagicImpl(this, panelLayout,this);
            checkSupportBeauty();
        } else {
            mXmagicApi.onResume();
        }
    }

    private void checkSupportBeauty(){
        isSupportBeauty = mXmagicApi.isSupportBeauty();
        if (!isSupportBeauty) {
            findViewById(R.id.tv_not_support_beauty).setVisibility(View.VISIBLE);
        }
    }

    public static class CameraPreviewConfig {
        public static final String KEY_PREVIEW_WIDTH = "KEY_PREVIEW_WIDTH";
        public static final String KEY_PREVIEW_HEIGHT = "KEY_PREVIEW_HEIGHT";
    }
}