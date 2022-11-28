package com.tencent.demo.avatar.activity;

import static com.tencent.demo.activity.CameraXActivity.EXTRA_IS_BACK_CAMERA;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Size;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.demo.R;
import com.tencent.demo.avatar.AvatarResManager;
import com.tencent.demo.avatar.CaptureAvatarDataManager;
import com.tencent.demo.camera.camerax.CustomTextureProcessor;
import com.tencent.demo.camera.camerax.GLCameraXView;
import com.tencent.demo.module.XmagicResParser;
import com.tencent.demo.utils.BitmapUtil;
import com.tencent.demo.utils.GlUtil;
import com.tencent.demo.utils.PermissionHandler;
import com.tencent.demo.utils.UriUtils;
import com.tencent.demo.widget.diaolog.ProgressDialog;
import com.tencent.xmagic.XmagicApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 没有加载美颜的相机预览界面
 */
public class CaptureActivity extends AppCompatActivity {

    private static final String TAG = "PreviewOnlyActivity";
    public static final String IS_CAPTURE_KEY = "isCapture";
    public static final String IS_MALE_KEY = "isMale";
    public static final String AVATAR_RES_NAME = "avatar_res_name";

    private boolean mIsCaptureClicked;        //是否点击了拍照
    private GLCameraXView glCameraXView = null;
    private ProgressDialog mProgressDialog;
    private XmagicApi xmagicApi;

    private boolean isMale = true;   //默认是男性模型
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String filePath = (String) msg.obj;
                if (!TextUtils.isEmpty(filePath)) {
                    request(filePath);
                }
            }
        }
    };
    private static final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_activity_layout);
        this.glCameraXView = findViewById(R.id.camera_view);
        this.isMale = getIntent().getBooleanExtra(IS_MALE_KEY, true);
        boolean isBackCamera = getIntent().getBooleanExtra(EXTRA_IS_BACK_CAMERA, false);
        glCameraXView.setIsBackCamera(isBackCamera);

        findViewById(R.id.pick_img).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImg();
            }
        });
        findViewById(R.id.change_camera).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                glCameraXView.switchCamera();
            }
        });

        findViewById(R.id.btn_capture).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 拍照
                mIsCaptureClicked = true;
            }
        });
        this.glCameraXView.setCustomTextureProcessor(new CustomTextureProcessor() {
            @Override
            public void onGLContextCreated() {

            }

            @Override
            public int onCustomProcessTexture(int textureId, int textureWidth, int textureHeight) {
                if (mIsCaptureClicked) {
                    mIsCaptureClicked = false;
                    catchBitmap(textureId, textureWidth, textureHeight);
                }
                return textureId;
            }

            @Override
            public void onGLContextDestroy() {

            }
        });
        // 检查和请求系统权限
        mPermissionHandler.start();
    }


    private void catchBitmap(int textureId, int textureWidth, int textureHeight) {
        Bitmap bitmap = GlUtil.readTexture(textureId, textureWidth, textureHeight);
        String directory = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "capture_avatar";
        File folder = new File(directory);
        if (!folder.exists()) {
            folder.mkdir();
        }
        String filePath = directory + File.separator + "photo.jpg";
        // 压缩到600K以下
        int compress = 70;
        BitmapUtil.saveBitmap(bitmap, filePath, compress);
        Message message = Message.obtain();
        message.what = 0;
        message.obj = filePath;
        handler.sendMessage(message);
    }


    private void request(String filePath) {
        showProgressDialog();
        String avatarResName = isMale ? AvatarResManager.AVATAR_RES_MALE : AvatarResManager.AVATAR_RES_FEMALE;
        String path = AvatarResManager.getInstance().getAvatarResPath() + avatarResName;
        //将对应关系添加进map中,用于在匹配成功之后进行查询
        Map<String, String> resMap = new ArrayMap<>();
        resMap.put(path, avatarResName);

        List<String> avatarResList = new ArrayList<>();
        avatarResList.add(path);
        if (xmagicApi == null) {
            xmagicApi = new XmagicApi(getApplicationContext(), XmagicResParser.getResPath(), null);
        }
        xmagicApi.createAvatarByPhoto(filePath, avatarResList, isMale, new XmagicApi.FaceAttributeListener() {

            @Override
            public void onError(int errCode, String msg) {
                runOnUiThread(() -> {
                    dismissDialog();
                    Toast.makeText(CaptureActivity.this, "识别失败[" + errCode+"]:" + msg,
                            Toast.LENGTH_LONG).show();
                    dismissDialog();
                });
            }

            @Override
            public void onFinish(String matchedResPath,  String matchedData) {
                runOnUiThread(() -> {
                    dismissDialog();
                    Intent intent = new Intent(CaptureActivity.this, AvatarActivity.class);
                    intent.putExtra(IS_CAPTURE_KEY, true);
                    // 此处将avatarResName传递过去，不传递完整路径，因为此处的完整路径本身就是demo给SDK的，所以demo是可以对应上的
                    intent.putExtra(AVATAR_RES_NAME, resMap.get(matchedResPath));
                    CaptureAvatarDataManager.getInstance().setMatchData(matchedData);
                    startActivity(intent);
                    finish();
                });
            }
        });
    }


    private void pickImg(){
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, REQUEST_CODE); // 打开相册，选择图片
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && requestCode == REQUEST_CODE) {
            if (data != null) {
                Uri uri = data.getData();     // 获取选择文件Uri   get uri
                String path = UriUtils.getFilePathByUri(this,uri);
                request(path);
            }
        }
    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog();
            mProgressDialog.createLoadingDialog(this);
            mProgressDialog.setCancelable(false);               // 设置是否可以通过点击Back键取消
            mProgressDialog.setCanceledOnTouchOutside(false);   // 设置在点击Dialog外是否取消Dialog进度条
            mProgressDialog.setMsg("analyzing...");
            mProgressDialog.show();
        }
    }

    private void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);// 必须有这个调用, mPermissionHandler 才能正常工作
    }

    private void startCamera() {
        this.glCameraXView.setCameraSize(new Size(720,1280));
    }


    private final PermissionHandler mPermissionHandler = new PermissionHandler(this) {
        @Override
        protected void onAllPermissionGranted() {
            startCamera();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (glCameraXView != null) {
            glCameraXView.startPreview();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (glCameraXView != null) {
            glCameraXView.stopPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (xmagicApi != null) {
            xmagicApi.onDestroy();
        }
        handler.removeMessages(0);
        if (glCameraXView != null) {
            glCameraXView.release();
        }
    }


}