package com.tencent.demo.activity;

import static com.tencent.demo.activity.CameraXActivity.EXTRA_IS_BACK_CAMERA;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View.OnClickListener;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.demo.R;
import com.tencent.demo.XMagicImpl;
import com.tencent.demo.activity.CameraActivity.CameraPreviewConfig;
import com.tencent.demo.avatar.activity.AvatarLaunchActivity;
import com.tencent.demo.download.DownloadErrorCode;
import com.tencent.demo.download.FileUtil;
import com.tencent.demo.download.MotionDLModel;
import com.tencent.demo.download.ResDownloadConfig;
import com.tencent.demo.download.ResDownloadUtil;
import com.tencent.demo.module.XmagicResParser;
import com.tencent.demo.utils.AppUtils;
import com.tencent.demo.utils.NoDoubleClickListener;
import com.tencent.demo.utils.OnDownloadListener;
import com.tencent.demo.utils.PermissionHandler;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.telicense.TELicenseCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    private static final String TAG = "LaunchActivity";
    private static final int MSG_UPDATE_PROGRESS = 0;
    private TextView mTextDownloadLibs = null;
    private TextView mTextDownloadAssets = null;
    private TextView mTextDownloadMotion = null;
    private CheckBox checkBoxUseBackCamera = null;
    private ConstraintLayout mLayoutDownload = null;
    private boolean mUseLocalRes = true;

    private boolean isDownloadingMotions = false;
    private int downloadMotionFinishCount = 0;
    private int downloadMotionSuccessCount = 0;
    private int downloadMotionFailCount = 0;
    private List<String> motionSuccessList = new ArrayList<>();
    private List<String> motionFailList = new ArrayList<>();


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_PROGRESS:
                    if (msg.arg1 == ResDownloadUtil.FILE_TYPE_LIBS) {
                        mTextDownloadLibs.setText("libs" + getString(R.string.downloading_tip) + (String) msg.obj);
                    } else if (msg.arg1 == ResDownloadUtil.FILE_TYPE_ASSETS) {
                        mTextDownloadAssets.setText("assets" + getString(R.string.downloading_tip) + (String) msg.obj);
                    }
                    break;
            }
        }
    };
    private final PermissionHandler mPermissionHandler = new PermissionHandler(this) {

        @Override
        protected void onAllPermissionGranted() {
            initUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mPermissionHandler.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    private interface AuthCallback {

        void onAuthSucceed();

        void onAuthFailed(int errorCode, String msg);
    }

    private void asyncAuth(AuthCallback authCallback) {
        XMagicImpl.checkAuth((errorCode, msg) -> {
            if (errorCode == TELicenseCheck.ERROR_OK) {
                authCallback.onAuthSucceed();
            } else {
                authCallback.onAuthFailed(errorCode, msg);
            }
        }, false);
    }

    private void initUI() {
        Intent intent = new Intent(this, CameraXActivity.class);
        RadioGroup previewSizeGroup = findViewById(R.id.previewSizeGroup);
        previewSizeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int width, height;
                if (checkedId == R.id.preview540p) {
                    width = 960;
                    height = 540;
                } else if (checkedId == R.id.preview720p) {
                    width = 1280;
                    height = 720;
                } else {// 默认 1080p
                    width = 1920;
                    height = 1080;
                }
                intent.putExtra(CameraPreviewConfig.KEY_PREVIEW_WIDTH, width);
                intent.putExtra(CameraPreviewConfig.KEY_PREVIEW_HEIGHT, height);
            }
        });
        findViewById(R.id.openTestPage).setOnClickListener(v -> {
            Intent target = new Intent(intent);
            target.setClass(LaunchActivity.this, PreviewOnlyActivity.class);
            startActivity(target);
        });
        RadioButton radioLocal = findViewById(R.id.radio_use_local);
        RadioButton radioDownload = findViewById(R.id.radio_use_download);
        radioLocal.setOnClickListener(v -> {
            mUseLocalRes = true;
            mLayoutDownload.setVisibility(View.GONE);
        });
        radioDownload.setOnClickListener(v -> {
            Dialog dialog = new AlertDialog.Builder(LaunchActivity.this)
                    .setTitle(getString(R.string.download_dialog_title))
                    .setMessage(getString(R.string.download_dialog_content))
                    .setPositiveButton(R.string.download_dialog_positive_btn, (dialog1, which) -> {
                        dialog1.dismiss();
                        mUseLocalRes = false;
                        mLayoutDownload.setVisibility(View.VISIBLE);
                    })
                    .setNegativeButton(getString(R.string.download_dialog_negative_btn), (dialog12, which) -> {
                        dialog12.dismiss();
                        radioLocal.setChecked(true);
                    })
                    .setOnCancelListener(dialog13 -> {
                        radioLocal.setChecked(true);
                    })
                    .create();
            dialog.show();
        });

        // 捏脸
        findViewById(R.id.btnMakeAvatar).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                Intent avatarIntent = new Intent(LaunchActivity.this, AvatarLaunchActivity.class);
                avatarIntent.putExtra(EXTRA_IS_BACK_CAMERA, checkBoxUseBackCamera.isChecked());
                startBeautyActivity(avatarIntent);
            }
        });

        //图片精修
        findViewById(R.id.btnImageInput).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onViewClick(View v) {
                startBeautyActivity(new Intent(LaunchActivity.this, ImageInputActivity.class));
            }
        });

        //实时拍摄
        findViewById(R.id.start).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onViewClick(View v) {
                startBeautyActivity(intent);
            }
        });

        mLayoutDownload = findViewById(R.id.layout_download);

        mTextDownloadLibs = findViewById(R.id.tv_download_libs);
        findViewById(R.id.btn_check_download).setOnClickListener(v -> {
            if (FileUtil.getAvailableInternalMemorySize() < 100 * 1024 * 1024) {
                String downloadTipStr = getString(R.string.unable_to_download_tip);
                mTextDownloadLibs.setText(String.format(downloadTipStr, "libs"));
            } else {
                if (isCpuV8a()) {
                    checkDownload(ResDownloadUtil.FILE_TYPE_LIBS, ResDownloadConfig.DOWNLOAD_URL_LIBS_V8A,
                            ResDownloadConfig.DOWNLOAD_MD5_LIBS_V8A);
                } else {
                    checkDownload(ResDownloadUtil.FILE_TYPE_LIBS, ResDownloadConfig.DOWNLOAD_URL_LIBS_V7A,
                            ResDownloadConfig.DOWNLOAD_MD5_LIBS_V7A);
                }
            }
        });
        mTextDownloadAssets = findViewById(R.id.tv_download_assets);
        findViewById(R.id.btn_check_download_assets).setOnClickListener(v -> {
            if (FileUtil.getAvailableInternalMemorySize() < 100 * 1024 * 1024) {
                String downloadTipStr = getString(R.string.unable_to_download_tip);
                mTextDownloadAssets.setText(String.format(downloadTipStr, "assets"));
            } else {
                checkDownload(ResDownloadUtil.FILE_TYPE_ASSETS, ResDownloadConfig.DOWNLOAD_URL_ASSETS,
                        ResDownloadConfig.DOWNLOAD_MD5_ASSETS);
            }
        });
        mTextDownloadMotion = findViewById(R.id.tv_download_motion);
        findViewById(R.id.btn_check_download_motion).setOnClickListener(v -> {
            if (FileUtil.getAvailableInternalMemorySize() < 100 * 1024 * 1024) {
                String downloadTipStr = getString(R.string.unable_to_download_tip);
                mTextDownloadMotion.setText(String.format(downloadTipStr, "动效"));
            } else {
                checkDownloadMotion();
            }
        });

        boolean enableResumeBreakPoint = getSharedPreferences("demo_settings", Context.MODE_PRIVATE)
                .getBoolean("ENABLE_RESUME_BREAK_POINT", true);
        ResDownloadUtil.ENABLE_RESUME_FROM_BREAKPOINT = enableResumeBreakPoint;

        CheckBox checkBoxResumeBreakPoint = findViewById(R.id.checkbox_resume_breakpoint);
        checkBoxResumeBreakPoint.setChecked(enableResumeBreakPoint);
        checkBoxResumeBreakPoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sp = getSharedPreferences("demo_settings", Context.MODE_PRIVATE);
                sp.edit().putBoolean("ENABLE_RESUME_BREAK_POINT", isChecked).apply();
                ResDownloadUtil.ENABLE_RESUME_FROM_BREAKPOINT = isChecked;
            }
        });

        checkBoxUseBackCamera = findViewById(R.id.checkbox_use_back_camera);

        findViewById(R.id.preview1080p).performClick();

    }

    private AlertDialog copyResDialog;

    private void startBeautyActivity(Intent intent) {
        if (copyResDialog == null) {
            copyResDialog = new ProgressDialog.Builder(LaunchActivity.this).setTitle(getString(R.string.copying_tip))
                    .setCancelable(false)
                    .create();
        }
        if (mUseLocalRes) {
            asyncAuth(new AuthCallback() {
                @Override
                public void onAuthSucceed() {
                    XmagicResParser.setResPath(new File(getFilesDir(), "xmagic").getAbsolutePath());
                    runOnUiThread(() -> {
                        copyResDialog.show();
                    });
                    if (!isCopyRes()) {
                        XmagicResParser.copyRes(getApplicationContext());
                        saveCopyData();
                    }
                    XmagicResParser.parseRes(getApplicationContext());

                    runOnUiThread(() -> {
                        copyResDialog.dismiss();
                        intent.putExtra(EXTRA_IS_BACK_CAMERA,checkBoxUseBackCamera.isChecked());
                        startActivity(intent);
                    });
                }

                @Override
                public void onAuthFailed(int errorCode, String msg) {
                    runOnUiThread(() -> {
                        Toast.makeText(LaunchActivity.this, getResources().getString(R.string.auth_failed_tip) + msg,
                                Toast.LENGTH_LONG).show();
                    });
                }
            });
        } else {
            String validLibsDirectory = ResDownloadUtil.getValidLibsDirectory(LaunchActivity.this,
                    isCpuV8a() ? ResDownloadConfig.DOWNLOAD_MD5_LIBS_V8A : ResDownloadConfig.DOWNLOAD_MD5_LIBS_V7A);
            if (validLibsDirectory == null) {
                Toast.makeText(LaunchActivity.this,
                        String.format(getResources().getString(R.string.please_download_tip), "libs"),
                        Toast.LENGTH_LONG).show();
                return;
            }

            String validAssetsDirectory = ResDownloadUtil
                    .getValidAssetsDirectory(LaunchActivity.this, ResDownloadConfig.DOWNLOAD_MD5_ASSETS);
            if (validAssetsDirectory == null) {
                Toast.makeText(LaunchActivity.this,
                        String.format(getResources().getString(R.string.please_download_tip), "assets"),
                        Toast.LENGTH_LONG).show();
                return;
            }
            XmagicResParser.setResPath(validAssetsDirectory);

            //asyncAuth里面需要用到so，因此需要先调用XmagicApi.setLibPathAndLoad设置so路径
            XmagicApi.setLibPathAndLoad(validLibsDirectory);

            asyncAuth(new AuthCallback() {
                @Override
                public void onAuthSucceed() {
                    runOnUiThread(() -> {
                        Toast.makeText(LaunchActivity.this, getResources().getString(R.string.auth_success_tip),
                                Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                    });
                }

                @Override
                public void onAuthFailed(int errorCode, String msg) {
                    runOnUiThread(() -> {
                        Toast.makeText(LaunchActivity.this, getResources().getString(R.string.auth_failed_tip) + msg,
                                Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }

    private boolean isCpuV8a() {
        String cpuType = null;
        try {
            cpuType = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(
                    "getprop ro.product.cpu.abi").getInputStream())).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cpuType != null && cpuType.contains("arm64-v8a")) {
            return true;
        }
        return false;
    }


    private void checkDownloadMotion() {
        if (isDownloadingMotions) {
            return;
        }
        isDownloadingMotions = true;
        downloadMotionFinishCount = 0;
        downloadMotionSuccessCount = 0;
        downloadMotionFailCount = 0;
        motionSuccessList.clear();
        motionFailList.clear();

        List<MotionDLModel> motionList = ResDownloadConfig.getMotionList();
        downloadOneByOne(motionList, 0);
    }

    private void downloadOneByOne(List<MotionDLModel> motionList, int index) {
        String text = mTextDownloadMotion.getText().toString();
        if (text == null) {
            text = "";
        }
        text = text + "\n" + getResources().getString(R.string.downloading_motion_tip) + motionList.get(index)
                .getName();
        mTextDownloadMotion.setText(text);
        ResDownloadUtil.checkOrDownloadMotions(this, motionList.get(index), new OnDownloadListener() {

            @Override
            public void onDownloadSuccess(String motionName) {
                runOnUiThread(() -> {
                    motionSuccessList.add(motionName + "\r\r" + getResources().getString(R.string.success_tip));
                    downloadMotionFinishCount++;
                    downloadMotionSuccessCount++;
                    updateTextAndDownloadNextOne();
                });
            }

            @Override
            public void onDownloading(int progress) {
            }

            @Override
            public void onDownloadFailed(int errorCode) {
                runOnUiThread(() -> {
                    motionFailList.add(
                            motionList.get(index).getName() + "\r\r" + getResources().getString(R.string.failed_tip)
                                    + DownloadErrorCode
                                    .getErrorMsg(errorCode));
                    downloadMotionFinishCount++;
                    downloadMotionFailCount++;
                    updateTextAndDownloadNextOne();
                });
            }

            @SuppressLint("StringFormatMatches")
            private void updateTextAndDownloadNextOne() {
                String tip = getResources().getString(R.string.motion_downloading_tip);
                mTextDownloadMotion.setText(
                        String.format(tip, downloadMotionSuccessCount, motionList.size(), downloadMotionFailCount));
                if (downloadMotionFinishCount < motionList.size()) {
                    downloadOneByOne(motionList, index + 1);
                } else {

                    mTextDownloadMotion
                            .setText(String.format(getResources().getString(R.string.motion_download_complete_tip),
                                    downloadMotionSuccessCount, downloadMotionFailCount));
                    isDownloadingMotions = false;
                    showSummaryDialog();
                }
            }

            private void showSummaryDialog() {
                StringBuilder sb = new StringBuilder();
                for (String str : motionFailList) {
                    sb.append(str).append("\n");
                }
                for (String str : motionSuccessList) {
                    sb.append(str).append("\n");
                }

                Dialog dialog = new AlertDialog.Builder(LaunchActivity.this)
                        .setTitle(getResources().getString(R.string.motion_download_result_tip))
                        .setMessage(sb.toString())
                        .setNegativeButton(null, null)
                        .setPositiveButton(getResources().getString(R.string.confirm_tip),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                dialog.show();
            }
        });
    }


    private void checkDownload(int fileType, String downloadUrl, String downloadMd5) {
        ResDownloadUtil.checkOrDownloadFiles(this, fileType, downloadUrl, downloadMd5, new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String directory) {
                runOnUiThread(() -> {
                    String tip = getResources().getString(R.string.download_success_tip);
                    if (fileType == ResDownloadUtil.FILE_TYPE_LIBS) {
                        mTextDownloadLibs.setText("libs" + tip);
                    } else if (fileType == ResDownloadUtil.FILE_TYPE_ASSETS) {
                        mTextDownloadAssets.setText("assets" + tip);
                    }
                });
            }

            @Override
            public void onDownloading(int progress) {
                Message msg = mHandler.obtainMessage(MSG_UPDATE_PROGRESS);
                msg.arg1 = fileType;
                msg.obj = progress + "%";
                mHandler.sendMessage(msg);
            }

            @Override
            public void onDownloadFailed(int errorCode) {
                runOnUiThread(() -> {
                    TextView textView = null;
                    String textPrefix = null;
                    if (fileType == ResDownloadUtil.FILE_TYPE_LIBS) {
                        textView = mTextDownloadLibs;
                        textPrefix = "libs";
                    } else if (fileType == ResDownloadUtil.FILE_TYPE_ASSETS) {
                        textView = mTextDownloadAssets;
                        textPrefix = "assets";
                    }
                    if (textView == null) {
                        return;
                    }
                    textView.setText(
                            textPrefix + getResources().getString(R.string.download_failed_tip) + "：(" + errorCode + ")"
                                    + DownloadErrorCode.getErrorMsg(errorCode));
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isCopyRes() {
        String appVersionName = AppUtils.getAppVersionName(this);
        SharedPreferences sp = getSharedPreferences("demo_settings", Context.MODE_PRIVATE);
        String savedVersionName = sp.getString("resource_copied","");
        return savedVersionName.equals(appVersionName);
    }

    private void saveCopyData() {
        String appVersionName = AppUtils.getAppVersionName(this);
        getSharedPreferences("demo_settings", Context.MODE_PRIVATE).edit().putString("resource_copied", appVersionName).commit();
    }

}
