package com.live.fox.ui.mine.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.UserdetatilActivityBinding;
import com.live.fox.dialog.bottomdialog.AreaListSelectorDialog;
import com.live.fox.dialog.bottomdialog.EditPersonalIntroDialog;
import com.live.fox.dialog.bottomdialog.EditProfileImageDialog;
import com.live.fox.dialog.bottomdialog.SimpleSelectorDialog;
import com.live.fox.dialog.bottomdialog.TimePickerDialog;
import com.live.fox.dialog.temple.EditNickNameConfirmDialog;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.ui.chat.ChatActivity;
import com.live.fox.ui.mine.ContributionRankActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 用户详情
 */
public class UserDetailActivity extends BaseActivity  {

    private boolean isFollow;
    UserdetatilActivityBinding mBind;

    Long uid;
    User user;

    public static void startActivity(Context context, long uid) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, UserDetailActivity.class);
        i.putExtra("uid", uid);
        context.startActivity(i);
    }

    public static void startActivityForResult(Activity activity, long uid) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra("uid", uid);
        activity.startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    try {
                      String url=  PictureFileUtils.getPicturePath(this);
                      File file=new File(url);
                      if(file!=null && file.exists())
                      {
                          EditProfileImageActivity.startActivity(this,url);
                      }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 圖片選擇結果回調
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList!=null && selectList.size()>0)
                    {
                        LocalMedia localMedia = selectList.get(0);
                        LogUtils.e("图片-----》" + localMedia.getPath());
                        EditProfileImageActivity.startActivity(this,localMedia.getPath());
                    }
                    break;
            }
        }
    }

    private static final String PATH_DOCUMENT = "document";
    public static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() < 2) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        return paths.get(1);
    }

    public void getIntentData() {
        if (getIntent() != null) {
            uid = getIntent().getLongExtra("uid", 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind=DataBindingUtil.setContentView(this,R.layout.userdetatil_activity);
        mBind.setClick(this);

        getIntentData();
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarAlpha(this,0,mBind.topView);
        BarUtils.setStatusBarLightMode(this, true);

        Long localUID=DataCenter.getInstance().getUserInfo().getUser().getUid();
        if (uid!=null && localUID!=null && uid.longValue() == localUID.longValue()) {
            mBind.btnFollow.setVisibility(View.GONE);
            mBind.btnLetter.setVisibility(View.GONE);
        }
        doGetUserInfoByUidApi(uid);
    }

    public void refreshPage() {
        user = DataCenter.getInstance().getUserInfo().getUser();
        mBind.tvIcon.setText(ChatSpanUtils.ins().getAllIconSpan(user, context));
        mBind.tvCirclenum.setText("0");
        mBind.tvFollownum.setText(String.valueOf(user.getFollows()));
        mBind.tvFansnum.setText(String.valueOf(user.getFans()));

        String uid=String.valueOf(user.getUid());
        StringBuilder sb=new StringBuilder();
        sb.append(getString(R.string.identity_id_3));
        sb.append("  ");
        sb.append(uid);
        SpannableString spannableString=new SpannableString(sb.toString());
        spannableString.setSpan(new ForegroundColorSpan(0xffb8b2c8),spannableString.length()-uid.length(), spannableString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBind.tvIdnum.setText(spannableString);
        int sexResId = user.getSex() == 1 ? R.string.boy : R.string.girl;
        mBind.tvGender.setText(getString(sexResId));
        mBind.tvAge.setText(getString(R.string.privacyStr));
        mBind.tvArea.setText(TextUtils.isEmpty(user.getCity())?getString(R.string.privacyStr):user.getCity());
        mBind.tvRelationshipStatus.setText(getString(R.string.privacyStr));
        mBind.tvOccupation.setText(getString(R.string.privacyStr));
        mBind.tvSignature.setText((StringUtils.isEmpty(user.getSignature()) ? getString(R.string.noWrite) : user.getSignature()));

        GlideUtils.loadDefaultImage(UserDetailActivity.this, user.getAvatar(), mBind.ivHeader);
        mBind.tvName.setText(TextUtils.isEmpty(user.getNickname())?"- -":user.getNickname());
        if (DataCenter.getInstance().getUserInfo().getUser().getUid().longValue()
                == user.getUid().longValue()) {
            mBind.btnFollow.setVisibility(View.GONE);
            mBind.btnLetter.setVisibility(View.GONE);
        } else {
            mBind.btnFollow.setVisibility(View.VISIBLE);
            mBind.btnLetter.setVisibility(View.VISIBLE);
        }

        updateFollow();
    }

    public void updateFollow() {
        if (user.isFollow()) {
            mBind.btnFollow.setBackgroundResource(R.drawable.shape_white_round_20);
            mBind.btnFollow.setTextColor(Color.parseColor("#868686"));
            mBind.btnFollow.setText(getString(R.string.focused));
            isFollow = true;
        } else {
            mBind.btnFollow.setBackgroundResource(R.drawable.btn1_userdetail);
            mBind.btnFollow.setTextColor(Color.WHITE);
            mBind.btnFollow.setText(getString(R.string.focus));
            isFollow = false;
        }
    }

    /**
     * 获取用户信息
     */
    public void doGetUserInfoByUidApi(long uid) {
        Api_User.ins().getUserInfo(uid, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    refreshPage();
                } else {
                    ToastUtils.showShort(msg);
                    finish();
                }
            }
        });
    }




    public void onViewClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.rlContribution:
                ContributionRankActivity.startActivity(this);
                break;
            case R.id.editProfileImage:
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), EditProfileImageDialog.getInstance());
                break;
            case R.id.tvGender:
                SimpleSelectorDialog dialog=SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {

                    }
                });
                dialog.setData(new ArrayList<>());
                dialog.setTitle(getString(R.string.selectGender));
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), dialog);
                break;
            case R.id.tvOccupation:
                SimpleSelectorDialog dialogOccupation=SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {

                    }
                });
                dialogOccupation.setData(new ArrayList<>());
                dialogOccupation.setTitle(getString(R.string.occupation));
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), dialogOccupation);
                break;
            case R.id.tvArea:
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), new AreaListSelectorDialog());
                break;
            case R.id.tvAge:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),new TimePickerDialog());
                break;
            case R.id.tvRelationshipStatus:
                SimpleSelectorDialog dialogRelationshipStatus=SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {

                    }
                });
                dialogRelationshipStatus.setData(new ArrayList<>());
                dialogRelationshipStatus.setTitle(getString(R.string.relationshipStatus2));
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), dialogRelationshipStatus);
                break;
            case R.id.tvName:
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), EditNickNameConfirmDialog.getInstance());
                break;
            case R.id.tvSignature:
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), EditPersonalIntroDialog.getInstance());
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_follow:
                Api_User.ins().follow(user.getUid(), !user.isFollow(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null) LogUtils.e("follow result : " + result);
                        if (code == 0 && result != null) {
                            user.setFollow(!user.isFollow());
                            user.setFans(user.isFollow() ? user.getFans() + 1 : user.getFans() - 1);
                            mBind.tvFansnum.setText(user.getFans() + "");
                            updateFollow();
                        }

                    }
                });
                break;
            case R.id.btn_letter:
                ChatActivity.startActivity(UserDetailActivity.this, user);
                break;
            case R.id.tvCopyId:
                ClipboardUtils.copyText(String.valueOf(user.getUid()));
                showToastTip(true, getString(R.string.userCopy));
                break;
//            case R.id.tvEditInfo:
//                EditUserInfoActivity.startActivity(this, DataCenter.getInstance().getUserInfo().getUser().getPhone());
//                break;
        }
    }

}
