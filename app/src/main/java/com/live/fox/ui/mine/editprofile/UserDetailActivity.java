package com.live.fox.ui.mine.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.UserdetatilActivityBinding;
import com.live.fox.dialog.bottomDialog.AreaListSelectorDialog;
import com.live.fox.dialog.bottomDialog.EditPersonalIntroDialog;
import com.live.fox.dialog.bottomDialog.EditProfileImageDialog;
import com.live.fox.dialog.bottomDialog.SimpleSelectorDialog;
import com.live.fox.dialog.bottomDialog.TimePickerDialog;
import com.live.fox.dialog.temple.EditNickNameConfirmDialog;
import com.live.fox.entity.NobleListBean;
import com.live.fox.entity.User;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.DataCenter2;
import com.live.fox.server.Api_Order;
import com.live.fox.server.Api_User;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.chat.ChatActivity;
import com.live.fox.ui.mine.contribution.ContributionRankActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.ProfileScrollView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 用户详情
 */
public class UserDetailActivity extends BaseActivity  {

    private boolean isFollow;
    UserdetatilActivityBinding mBind;

    Long uid;
    User mUser;

    List<String> listJob = new ArrayList<>();
    List<String> listGq = new ArrayList();


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
                case ConstantValue.REQUEST_CROP_PIC://头像上传到文件服务器成功
                    if(data!=null && data.getStringExtra(ConstantValue.pictureOfUpload)!=null)
                    {
                        File file=new File(data.getStringExtra(ConstantValue.pictureOfUpload));
                        if(file!=null && file.exists())
                        {
                            updateFile(file);
                        }
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
        mBind.rlTop.setAlpha(0f);
        mBind.rlTop.setPadding(0,StatusBarUtil.getStatusBarHeight(this),0,0);
        mBind.svProfile.setOnScrollListener(new ProfileScrollView.OnScrollListener() {
            @Override
            public void onScroll(int y, float alpha) {
                mBind.rlTop.setAlpha(alpha);
            }
        });

        StatusBarUtil.setStatusBarFulAlpha(this,mBind.topView);
        BarUtils.setStatusBarLightMode(this, true);

        Long localUID=DataCenter.getInstance().getUserInfo().getUser().getUid();
        if (uid!=null && localUID!=null && uid.longValue() == localUID.longValue()) {
            mBind.btnFollow.setVisibility(View.GONE);
            mBind.btnLetter.setVisibility(View.GONE);
        }

        listJob.add(getString(R.string.job_1));listJob.add(getString(R.string.job_2));listJob.add(getString(R.string.job_3));
        listJob.add(getString(R.string.job_4));listJob.add(getString(R.string.job_5));listJob.add(getString(R.string.job_6));
        listJob.add(getString(R.string.job_7));listJob.add(getString(R.string.job_8));listJob.add(getString(R.string.job_9));
        listJob.add(getString(R.string.job_10));listJob.add(getString(R.string.job_11));listJob.add(getString(R.string.job_12));
        listJob.add(getString(R.string.job_13));listJob.add(getString(R.string.job_14));

        listGq.add(getString(R.string.loving_1));
        listGq.add(getString(R.string.loving_2));
        listGq.add(getString(R.string.loving_3));
        listGq.add(getString(R.string.loving_4));
        listGq.add(getString(R.string.privacyStr));


        doGetUserInfoByUidApi(uid);
        getAssetsData();
    }

    public void refreshPage() {
        mUser = DataCenter2.getInstance().getUserInfo().getUser();
       // mBind.tvIcon.setText(ChatSpanUtils.ins().getAllIconSpan(mUser, context));
        mBind.tvCirclenum.setText(mUser.getFans() + "");
        mBind.tvFollownum.setText(String.valueOf(mUser.getFollows()));
      //  mBind.tvFansnum.setText("");

        String uid=String.valueOf(mUser.getUid());
        StringBuilder sb=new StringBuilder();
        sb.append(getString(R.string.identity_id_3));
        sb.append(uid);
        SpannableString spannableString=new SpannableString(sb.toString());
        spannableString.setSpan(new ForegroundColorSpan(0xffb8b2c8),spannableString.length()-uid.length(), spannableString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBind.tvIdnum.setText(spannableString);
        if (mUser.getSex() == 0 ){
            mBind.tvGender.setText(getString(R.string.privacyStr));
        } else{
            int sexResId = mUser.getSex() == 1 ? R.string.boy : R.string.girl;
            mBind.tvGender.setText(getString(sexResId));
        }

        if (TextUtils.isEmpty(mUser.getBirthday())) {
            mBind.tvAge.setText(getString(R.string.privacyStr));
        } else {
            mBind.tvAge.setText(mUser.getBirthday());
        }
        //mBind.tvAge.setText(getString(R.string.privacyStr));
        mBind.tvArea.setText(TextUtils.isEmpty(mUser.getCity())?
                getString(R.string.privacyStr):mUser.getProvince() + "-" + mUser.getCity());
        setGq();
      //  mBind.tvRelationshipStatus.setText(getString(R.string.privacyStr));
        if (mUser.getJob() == -1) {
            mBind.tvOccupation.setText(getString(R.string.privacyStr));
        } else {
            if (mUser.getJob() < listJob.size()) {
                mBind.tvOccupation.setText(listJob.get(mUser.getJob()));}
        }
        mBind.tvNickName.setText(TextUtils.isEmpty(mUser.getNickname())?"- -":mUser.getNickname());
        mBind.tvSignature.setText((StringUtils.isEmpty(mUser.getSignature()) ? getString(R.string.noWrite) : mUser.getSignature()));

        if (mUser!= null && mUser.getSex() != 0) {
            mBind.ivSex.setBackground(mUser.getSex() == 1 ? getResources().getDrawable(R.mipmap.men) : getResources().getDrawable(R.mipmap.women));
        }

//        if (mUser.getUserLevel() > 10) {
//            mBind.editProfileImage.setVisibility(View.VISIBLE);
//        } else {
//            mBind.editProfileImage.setVisibility(View.GONE);
//        }

        mBind.ivLiang.setVisibility(mUser.getVipUid() == null ? View.GONE : View.VISIBLE );

        GlideUtils.loadDefaultImage(UserDetailActivity.this, mUser.getAvatar(), R.mipmap.user_head_error, mBind.ivHeader);
        mBind.tvName.setText(TextUtils.isEmpty(mUser.getNickname())?"- -":mUser.getNickname());
        if (DataCenter.getInstance().getUserInfo().getUser().getUid().longValue()
                == mUser.getUid().longValue()) {
            mBind.btnFollow.setVisibility(View.GONE);
            mBind.btnLetter.setVisibility(View.GONE);
        } else {
            mBind.btnFollow.setVisibility(View.VISIBLE);
            mBind.btnLetter.setVisibility(View.VISIBLE);
        }

        updateFollow();
        getMyNoble();

//        new SVGAParser(this).decodeFromAssets("living.svga", new SVGAParser.ParseCompletion() {
//            @RequiresApi(api = Build.VERSION_CODES.P)
//            @Override
//            public void onComplete(@NotNull SVGAVideoEntity videoItem) {
//                if (mBind.ivLiving != null) {
//                    mBind.ivLiving.setVideoItem(videoItem);
//                    mBind.ivLiving.stepToFrame(0, true);
//                }
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
    }

    public void updateFollow() {
        if (mUser.isFollow()) {
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


    private void getAssetsData(){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        Api_Order.ins().getAssets(new JsonCallback<UserAssetsBean>() {
            @Override
            public void onSuccess(int code, String msg, UserAssetsBean data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    mBind.tvFansnum.setText("" + data.getSendDiamond());
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }

    private void getMyNoble(){
        Api_Order.ins().getMyNoble(new JsonCallback<NobleListBean>() {
            @Override
            public void onSuccess(int code, String msg, NobleListBean data) {
                //  hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    if (data !=null && data.getVipLevel() > 0) {

                        int index=data.getVipLevel()%7 - 1;
                        int[] level = new ResourceUtils().getResourcesID(R.array.rankTagPics);
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), level[index]);
                        mBind.ivNoble.setImageBitmap(bitmap);
                    }
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
                EditUserInfoActivity.startActivity(this, mUser.getPhone());
//                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), EditProfileImageDialog.getInstance());
                break;
            case R.id.tvGender:
               // SimpleSelectorDialog ;
              SimpleSelectorDialog dialog =  SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        User user=new User();

                        if (index == 0) {
                            user.setSex(1);
                            modifyUser(user, 3);
                        } else {
                            user.setSex(2);
                            modifyUser(user, 3);
                        }

                    }
                });
                simpleSelectorDialog = dialog;
                List<String> list = new ArrayList();
                list.add(getString(R.string.boy));
                list.add(getString(R.string.girl));
                dialog.setData(list);
                dialog.setTitle(getString(R.string.selectGender));
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), dialog);
                break;
            case R.id.tvOccupation:
                SimpleSelectorDialog dialogOccupation=SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        User user=new User();
                        user.setJob(index);
                        modifyUser(user, 7);
                    }
                });

                dialogOccupation.setData(listJob);
                dialogOccupation.setTitle(getString(R.string.occupation));
                simpleSelectorDialog = dialogOccupation;
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), dialogOccupation);
                break;
            case R.id.tvArea:
                AreaListSelectorDialog areaListSelectorDialog =new AreaListSelectorDialog();
                areaListSelectorDialog.setOnCityConfirm(new AreaListSelectorDialog.OnCityConfirm() {
                    @Override
                    public void onSelect(String province, String city) {
                        User user = new User();
                        user.setCity(city);
                        user.setProvince(province);
                        modifyUser(user, 8);
                    }
                });
                simpleSelectorDialog = areaListSelectorDialog;
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), areaListSelectorDialog);
                break;
            case R.id.tvAge:
                TimePickerDialog timePickerDialog = new TimePickerDialog();
                timePickerDialog.setOnSelectedListener(new TimePickerDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int year, int month, int date, long time) {
                        String s = year + "-" + month + "-" + date;
                        User user=new User();
                        user.setBirthday(s);
                        simpleSelectorDialog = timePickerDialog;
                        modifyUser(user, 6);

                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),timePickerDialog);
                break;
            case R.id.tvRelationshipStatus:
                SimpleSelectorDialog dialogRelationshipStatus=SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        User user=new User();
                        index ++;
                        user.setEmotionalState(index);
                        modifyUser(user, 5);
                    }
                });
                simpleSelectorDialog = dialogRelationshipStatus;


                dialogRelationshipStatus.setData(listGq);
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
                Api_User.ins().follow(mUser.getUid(), !mUser.isFollow(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null) LogUtils.e("follow result : " + result);
                        if (code == 0 && result != null) {
                            mUser.setFollow(!mUser.isFollow());
                            mUser.setFans(mUser.isFollow() ? mUser.getFans() + 1 : mUser.getFans() - 1);
                            mBind.tvFansnum.setText(mUser.getFans() + "");
                            updateFollow();
                        }

                    }
                });
                break;
            case R.id.btn_letter:
                ChatActivity.startActivity(UserDetailActivity.this, mUser);
                break;
            case R.id.tvCopyId:
                ClipboardUtils.copyText(String.valueOf(mUser.getUid()));
                showToastTip(true, getString(R.string.userCopy));
                break;
//            case R.id.tvEditInfo:
//                EditUserInfoActivity.startActivity(this, DataCenter.getInstance().getUserInfo().getUser().getPhone());
//                break;
        }
    }


    BaseBindingDialogFragment simpleSelectorDialog;

    private void modifyUser(User userTemp, int type){
        showLoadingDialog();
        Api_User.ins().modifyUserInfo(userTemp, type, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (simpleSelectorDialog != null) {
                    simpleSelectorDialog.dismissAllowingStateLoss();
                }

                if(code==0) {
                    showToastTip(true, getString(R.string.modifySuccess));
                    if (type == 3) {
                        mUser.setSex(userTemp.getSex());
                        mBind.tvGender.setText(mUser.getSex() == 1? getString(R.string.boy): getString(R.string.girl));
                        mBind.ivSex.setBackground(mUser.getSex() == 1 ? getResources().getDrawable(R.mipmap.men) : getResources().getDrawable(R.mipmap.women));
                    } else if (type == 5) {
                        mUser.setEmotionalState(userTemp.getEmotionalState());
                        setGq();
                    } else if (type == 6) {
                        mBind.tvAge.setText(userTemp.getBirthday());
                    } else if (type == 7){
                        mBind.tvOccupation.setText(listJob.get(userTemp.getJob()));
                    } else if (type == 8){
                        mBind.tvArea.setText(userTemp.getProvince() + "-" + userTemp.getCity());
                    }
                    //DataCenter.getInstance().getUserInfo().updateUser(user);


                } else {
                    showToastTip(true, msg);
                }
            }

        });

    }

    private void setGq(){
        if (mUser.getEmotionalState() == 1) {
            mBind.tvRelationshipStatus.setText(getString(R.string.loving_1));
        } else if (mUser.getEmotionalState() == 2) {
            mBind.tvRelationshipStatus.setText(getString(R.string.loving_2));
        } else if (mUser.getEmotionalState() == 3) {
            mBind.tvRelationshipStatus.setText(getString(R.string.loving_3));
        } else if (mUser.getEmotionalState() == 4) {
            mBind.tvRelationshipStatus.setText(getString(R.string.loving_4));
        } else if (mUser.getEmotionalState() == 5) {
            mBind.tvRelationshipStatus.setText(getString(R.string.privacyStr));
        }

    }



    private void updateFile(File file){
        showLoadingDialog();
        Api_User.ins().uploadUserPhoto(file, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    modifyUser(data, 1);
                } else {
                    showToastTip(true, msg);
                }

            }
        });

    }


    private void modifyUser(String picUrl, int type){
        User user = new User();
        user.setAvatar(picUrl);
        Api_User.ins().modifyUserInfo(user, type, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if(code==0) {
                    mUser.setAvatar(picUrl);
                    GlideUtils.loadImage(UserDetailActivity.this, picUrl, mBind.ivHeader);
                    showToastTip(true, getString(R.string.modifySuccess));
                } else {
                    showToastTip(true, msg);
                }
            }
        });
    }
}
