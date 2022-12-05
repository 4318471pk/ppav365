package com.live.fox.ui.mine.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
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
import com.live.fox.entity.ContributionRankItemBean;
import com.live.fox.entity.NobleListBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.entity.User;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Order;
import com.live.fox.server.Api_Rank;
import com.live.fox.server.Api_User;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.chat.ChatActivity;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.mine.MyFollowListActivity;
import com.live.fox.ui.mine.contribution.ContributionRankActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.ProfileScrollView;
import com.live.fox.view.RankProfileView;
import com.live.fox.view.Top20CircleImage;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 用户详情
 */
public class UserDetailActivity extends BaseActivity  {

    UserdetatilActivityBinding mBind;
    Long uid;
    User mUser;
    List<String> listJob = new ArrayList<>();
    List<String> listGq = new ArrayList();
    String ContributionDataList;


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
        SVGAParser.Companion.shareParser().init(this);
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
        getContributionList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Long myUid=DataCenter.getInstance().getUserInfo().getUser().getUid();
        if(myUid!=null && myUid.longValue()==uid.longValue())
        {
            refreshPage(DataCenter.getInstance().getUserInfo().getUser());
        }
    }

    public void refreshPage(User currentUser) {
        mUser = currentUser;
       // mBind.tvIcon.setText(ChatSpanUtils.ins().getAllIconSpan(mUser, context));
        mBind.tvFollowAmount.setText(mUser.getFollows() + "");
        mBind.tvFansAmount.setText(String.valueOf(mUser.getFans()));
      //  mBind.tvFansnum.setText("");

        Long localUID=DataCenter.getInstance().getUserInfo().getUser().getUid();
        if (uid!=null && localUID!=null && uid.longValue() == localUID.longValue()) {
            mBind.btnFollow.setVisibility(View.GONE);

            mBind.editProfileImage.setVisibility(View.VISIBLE);
        }
        else
        {
            mBind.btnFollow.setVisibility(View.VISIBLE);
            mBind.btnFollow.setSelected(mUser.isFollow());


            mBind.editProfileImage.setVisibility(View.GONE);
        }

        SpanUtils spanUtils=new SpanUtils();
        if(ChatSpanUtils.appendSexIcon(spanUtils,mUser.getSex(), context, SpanUtils.ALIGN_CENTER))
        {
            spanUtils.append(" ");
        }
        if(ChatSpanUtils.appendLevelIcon(spanUtils,mUser.getUserLevel(), context))
        {
            spanUtils.append(" ");
        }
        if(ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,mUser.getVipLevel(), context))
        {
            spanUtils.append(" ");
        }
        mBind.tvIcons.setText(spanUtils.create());

        String uid=String.valueOf(mUser.getUid());
        StringBuilder sb=new StringBuilder();
        sb.append(getString(R.string.identity_id_3));
        sb.append(uid);
        SpannableString spannableString=new SpannableString(sb.toString());
        spannableString.setSpan(new ForegroundColorSpan(0xff404040),spannableString.length()-uid.length(), spannableString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        mBind.tvSignature.setText((StringUtils.isEmpty(mUser.getSignature()) ? getString(R.string.noSignature) : mUser.getSignature()));


//        if (mUser.getUserLevel() > 10) {
//            mBind.editProfileImage.setVisibility(View.VISIBLE);
//        } else {
//            mBind.editProfileImage.setVisibility(View.GONE);
//        }


        GlideUtils.loadDefaultImage(UserDetailActivity.this, mUser.getAvatar(), 0, mBind.ivHeader);
        mBind.tvName.setText(TextUtils.isEmpty(mUser.getNickname())?"- -":mUser.getNickname());
        if (DataCenter.getInstance().getUserInfo().getUser().getUid().longValue()
                == mUser.getUid().longValue()) {
            mBind.btnFollow.setVisibility(View.GONE);
        } else {
            mBind.btnFollow.setVisibility(View.VISIBLE);
            mBind.btnFollow.setSelected(mUser.isFollow());
        }

        updateFollow();

        if(mUser.getBroadcast() && !TextUtils.isEmpty(mUser.getLiveId()))
        {
            RoomListBean roomListBean=new RoomListBean();
            roomListBean.setId(mUser.getLiveId());
            roomListBean.setAid(mUser.getUid()+"");
            mBind.ivLiving.setOnClickListener(new OnClickFrequentlyListener() {
                @Override
                public void onClickView(View view) {
                    List<RoomListBean> roomListBeans=new ArrayList<>();
                    roomListBeans.add(roomListBean);
                    LivingActivity.startActivity(UserDetailActivity.this,roomListBeans,0);
                }
            });
            SVGAParser parser = SVGAParser.Companion.shareParser();
            parser.decodeFromAssets("living.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    if (mBind.ivLiving != null) {
                        mBind.ivLiving.setVisibility(View.VISIBLE);
                        mBind.ivLiving.setVideoItem(svgaVideoEntity);
                        mBind.ivLiving.stepToFrame(0, true);
                    }
                }

                @Override
                public void onError() {
                }
            }, new SVGAParser.PlayCallback() {
                @Override
                public void onPlay(@NotNull List<? extends File> list) {

                }
            });
        }
        else
        {
            mBind.ivLiving.clear();
            mBind.ivLiving.setVisibility(View.GONE);
        }
    }



    public void updateFollow() {
        mBind.btnFollow.setSelected(mUser.isFollow());
        if (mUser.isFollow()) {
            mBind.btnFollow.setText(getString(R.string.cancle_gz));
        } else {
            mBind.btnFollow.setText(getString(R.string.follow2));
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
                    refreshPage(new Gson().fromJson(data,User.class));
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
                if (code == 0 ) {
                    if(data.getSendDiamond()!=null)
                    {
                        mBind.tvGiftAmount.setText("" + data.getSendDiamond().intValue());
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }

    public void onViewClick(View view) {
        if (ClickUtil.isClickWithShortTime(view.getId(),1000)) return;
        switch (view.getId()) {
            case R.id.rlContribution:
                ContributionRankActivity.startActivity(this,ContributionDataList);
                break;
            case R.id.editProfileImage:
                EditUserInfoActivity.startActivity(this);
//                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), EditProfileImageDialog.getInstance());
                break;
            case R.id.tvFollowAmount:
            case R.id.tvFollow:
                MyFollowListActivity.startActivity(this,false);
                break;
            case R.id.tvFans:
            case R.id.tvFansAmount:
                MyFollowListActivity.startActivity(this,true);
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
                            mBind.tvGiftAmount.setText(mUser.getFans() + "");
                            updateFollow();
                        }
                    }
                });
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

    public void getContributionList()
    {
        String uid=String.valueOf(DataCenter.getInstance().getUserInfo().getUser().getUid());

        Api_Rank.ins().getContributionRankList("",uid,new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                Log.e("getContributionList",data);
                if(code==0 )
                {
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        ContributionDataList=data;
                        List<ContributionRankItemBean> beans=GsonUtil.getObjects(jsonObject.optString("allList"), ContributionRankItemBean[].class);
                        List<Top20CircleImage> images=new ArrayList<>();
                        images.add(mBind.top1Image);
                        images.add(mBind.top2Image);
                        images.add(mBind.top3Image);
                        if(beans!=null && beans.size()>0)
                        {
                            for (int i = 0; i < images.size(); i++) {
                                View parent= (View)images.get(i).getParent();
                                if(beans.size()>i)
                                {
                                    parent.setVisibility(View.VISIBLE);
                                    String url=beans.get(i).getAvatar();
                                    if(!TextUtils.isEmpty(url))
                                    {
                                        GlideUtils.loadCircleImage(UserDetailActivity.this,url,0,0,images.get(i));
                                    }
                                }
                                else
                                {
                                    parent.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
