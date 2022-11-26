package com.live.fox.ui.mine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.live.fox.AnchorLiveActivity;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityCenterAnchorBinding;
import com.live.fox.dialog.bottomDialog.EditProfileImageDialog;
import com.live.fox.entity.ConfigPathsBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.editprofile.EditProfileImageActivity;
import com.live.fox.ui.openLiving.OpenLivingActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.rtmp.TXLiveBase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class CenterOfAnchorActivity extends BaseBindingViewActivity {

    ActivityCenterAnchorBinding mBind;
    List<ConfigPathsBean> configPathsBeans;
    List<View> views = new ArrayList<>();
    String liveId = "";

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, CenterOfAnchorActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(views.size()>0)
        {
            for (int i = 0; i < views.size(); i++) {
                getLivingRecord(i);//开播记录
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    try {
                        String url = PictureFileUtils.getPicturePath(this);
                        File file = new File(url);
                        if (file != null && file.exists()) {
                            EditProfileImageActivity.startActivity(this, EditProfileImageActivity.Square, url);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 圖片選擇結果回調
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        LogUtils.e("图片-----》" + localMedia.getPath());
                        EditProfileImageActivity.startActivity(this, EditProfileImageActivity.Square, localMedia.getPath());
                    }
                    break;
                case ConstantValue.REQUEST_CROP_PIC://头像上传到文件服务器成功
                    if (data != null && data.getStringExtra(ConstantValue.pictureOfUpload) != null) {
                        File file = new File(data.getStringExtra(ConstantValue.pictureOfUpload));
                        if (file != null && file.exists()) {
                            uploadBGOfLivingRoom(file);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.rlChangeRoomPic:
                EditProfileImageDialog dialog = EditProfileImageDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(), dialog);
                break;
            case R.id.tvLivingRecordList:
                startActivity(new Intent(this, LivingRecordListActivity.class));
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_center_anchor;
    }

    @Override
    public void initView() {
        setWindowsFlag();
        mBind = getViewDataBinding();
        mBind.setClick(this);

        setActivityTitle(getString(R.string.centerOfAnchor));
        getTvTitleRight().setText(getResources().getString(R.string.goOpenSteam));
        getTvTitleRight().setTextColor(0xffFF008A);
        Drawable leftIcon = getResources().getDrawable(R.mipmap.icon_small_live);
        getTvTitleRight().setCompoundDrawablesRelativeWithIntrinsicBounds(leftIcon, null, null, null);
        getTvTitleRight().setCompoundDrawablePadding(ScreenUtils.dp2px(this, 3));
        getTvTitleRight().setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                if (mBind.gtvTitleOfRoom.getText().toString().length() == 0) {
                    ToastUtils.showShort(getString(R.string.plsFillTitleOfRoom));
                    return;
                }

                if (configPathsBeans != null && configPathsBeans.size() > 0) {
                    TXLiveBase.getInstance().setLicence(CommonApp.getInstance(),
                            configPathsBeans.get(0).getLicenceUrl(), configPathsBeans.get(0).getLicenceKey()
                    );
                    //目前写死 看以后怎么拿
                    openLive("84");
                }
            }
        });


        String array[] = getResources().getStringArray(R.array.dayOfLivingRecord);
        for (int i = 0; i < array.length; i++) {
            View view = View.inflate(this, R.layout.layout_living_record, null);
            views.add(view);
        }
        mBind.vpRecord.setOffscreenPageLimit(array.length);
        mBind.vpRecord.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return array.length;
            }

            @NonNull
            @NotNull
            @Override
            public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
                container.addView(views.get(position), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return views.get(position);
            }

            @Override
            public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
                return view == object;
            }

            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return array[position];
            }
        });

        int itemWidth = (ScreenUtils.getScreenWidth(this) - ScreenUtils.dp2px(this, 120)) / 4;
        mBind.tabLayout.setTabWidthPX(itemWidth);
        mBind.tabLayout.setGradient(0xffA800FF, 0xffEA00FF);
        mBind.tabLayout.setViewPager(mBind.vpRecord);

        mBind.gtvTitleOfRoom.setText(DataCenter.getInstance().getUserInfo().getUser().getNickname());
        getLineList();//线路列表
        getCenterData();//开播信息
        for (int i = 0; i < array.length; i++) {
            getLivingRecord(i);//开播记录
        }

    }

    private void openLive(String liveConfigId) {
        boolean careraPermission = false;
        boolean mircPermission = false;
        if (ContextCompat.checkSelfPermission(CenterOfAnchorActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            careraPermission = true;
        }

        if (ContextCompat.checkSelfPermission(CenterOfAnchorActivity.this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            mircPermission = true;
        }

        if (!careraPermission || !mircPermission) {
            RxPermissions rxPermissions = new RxPermissions(CenterOfAnchorActivity.this);
            Disposable subscribe = rxPermissions.request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        if (granted) {

                            String roomType = mBind.gtvTypeOfRoom.getText().toString();
                            getTvTitleRight().setEnabled(false);
                            String roomTitle = mBind.gtvTitleOfRoom.getText().toString();
                            String icon = mBind.ivRoomIcon.getTag() == null ? "" : (String) mBind.ivRoomIcon.getTag();
                            OpenLivingActivity.startActivity(CenterOfAnchorActivity.this, icon, roomTitle, liveId, liveConfigId, roomType);
                            getTvTitleRight().setEnabled(true);
                        } else { // 有的权限被拒绝或被勾选不再提示
                            LogUtils.e("有的权限被拒绝");
                            new AlertDialog.Builder(CenterOfAnchorActivity.this)
                                    .setCancelable(false)
                                    .setMessage(getString(R.string.notePermission))
                                    .setPositiveButton(getString(R.string.see), (dialog, which) -> LogUtils.e("权限被拒绝"))
                                    .show();
                        }
                    });
        } else {
            String roomType = mBind.gtvTypeOfRoom.getText().toString();
            getTvTitleRight().setEnabled(false);
            String roomTitle = mBind.gtvTitleOfRoom.getText().toString();
            String icon = mBind.ivRoomIcon.getTag() == null ? "" : (String) mBind.ivRoomIcon.getTag();
            OpenLivingActivity.startActivity(CenterOfAnchorActivity.this, icon, roomTitle, liveId, liveConfigId, roomType);
            getTvTitleRight().setEnabled(true);
        }
    }

    private void getLineList() {
        showLoadingDialogWithNoBgBlack();
        Api_Config.ins().getConfigPaths(DataCenter.getInstance().getUserInfo().getUser().getUid(), new JsonCallback<List<ConfigPathsBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<ConfigPathsBean> data) {
                hideLoadingDialog();
                if (code == Constant.Code.SUCCESS) {
                    if (data != null && data.size() > 0) {
                        configPathsBeans = data;
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void uploadBGOfLivingRoom(File file) {
        showLoadingDialogWithNoBgBlack();
        Api_User.ins().uploadLivingRoomPicture(file, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == Constant.Code.SUCCESS) {
                    GlideUtils.loadImage(CenterOfAnchorActivity.this, data, mBind.ivRoomIcon);
                    mBind.ivRoomIcon.setTag(data);
                    showToastTip(true, getString(R.string.modifySuccess));
                    //Log.e("uploadBGOfLivingRoom",data);
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void getCenterData() {
        showLoadingDialogWithNoBgBlack();
        //{"roomId":null,"icon":null,"title":null,"type":null}
        Api_Live.ins().getAnchorCenterInfo(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        liveId = jsonObject.optString("roomId", "");
                        String icon = jsonObject.optString("icon", "");
                        String title = jsonObject.optString("title", "");
                        String type = jsonObject.optString("type", "");
                        GlideUtils.loadDefaultImage(CenterOfAnchorActivity.this, icon, R.mipmap.user_head_error, R.mipmap.user_head_error, mBind.ivRoomIcon);
                        if (!TextUtils.isEmpty(icon)) {
                            mBind.ivRoomIcon.setTag(icon);
                        }

                        if (Strings.isDigitOnly(type)) {
                            int index = Integer.valueOf(type);
                            if (index > -1 && index < 3) {
                                String str[] = getResources().getStringArray(R.array.typeOfRoom);
                                mBind.gtvTypeOfRoom.setText(str[index]);
                            }
                        }

                        mBind.gtvTitleOfRoom.setText(title);
//                        switch (type)
//                        {
//
//                        }
//                        mBind.gtvTypeOfRoom.setText();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void getLivingRecord(int type) {
        Api_Live.ins().getAnchorProfitStatement(type, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (isDestroyed() || isFinishing()) {
                    return;
                }
                if (code == 0) {
                    if (!TextUtils.isEmpty(data)) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            String gifAmount = jsonObject.optString("profiitTotalAmount", "0");
                            String liveSum = jsonObject.optString("liveSum", "0");
                            String betAmount = jsonObject.optString("betAmount", "0");
                            String liveMinutes = jsonObject.optString("liveMinutes", "0");
                            String profiitTotalAmount = jsonObject.optString("profiitTotalAmount", "0");

                            String values[] = {liveSum, gifAmount, betAmount, liveMinutes};
                            String unitOfLivingRecord[] = getResources().getStringArray(R.array.unitOfLivingRecord);
                            int index = Integer.valueOf(getArg());
                            View view = views.get(index);
                            LinearLayout llList = view.findViewById(R.id.llList);
                            for (int i = 0; i < llList.getChildCount(); i++) {
                                LinearLayout linearLayout = (LinearLayout) llList.getChildAt(i);
                                TextView textView = (TextView) linearLayout.getChildAt(1);
                                SpanUtils spanUtils = new SpanUtils();
                                spanUtils.append(values[i]).setFontSize(16, true);
                                spanUtils.append(unitOfLivingRecord[i]).setFontSize(11, true);
                                textView.setText(spanUtils.create());
                            }

                            TextView tvTotalAdvantage = view.findViewById(R.id.tvTotalAdvantage);
                            SpanUtils spanUtils = new SpanUtils();
                            spanUtils.append(profiitTotalAmount).setFontSize(16, true);
                            spanUtils.append(unitOfLivingRecord[2]).setFontSize(11, true);
                            tvTotalAdvantage.setText(spanUtils.create());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
