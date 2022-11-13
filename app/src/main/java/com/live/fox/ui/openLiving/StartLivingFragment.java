package com.live.fox.ui.openLiving;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.AnchorLiveActivity;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.LiveFinishActivity;
import com.live.fox.R;
import com.live.fox.adapter.LivingMsgBoxAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentStartLivingBinding;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.TipDialog;
import com.live.fox.dialog.bottomDialog.AnchorLivingRoomSettingDialog;
import com.live.fox.dialog.bottomDialog.AnchorProtectorListDialog;
import com.live.fox.dialog.bottomDialog.ContributionRankDialog;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.dialog.bottomDialog.OnlineNobilityAndUserDialog;
import com.live.fox.dialog.bottomDialog.SetRoomTypeDialog;
import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Pay;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.CountTimerUtil;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;

public class StartLivingFragment extends BaseBindingFragment implements AppIMManager.OnMessageReceivedListener{

    FragmentStartLivingBinding mBind;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans = new ArrayList<>();
    Handler mHandler=new Handler(Looper.myLooper());
    String liveId;

    @Override
    public void onClickView(View view) {

        OpenLivingActivity activity=(OpenLivingActivity)getActivity();

        switch (view.getId())
        {
            case R.id.ivCameraChangeSide:
                if(ClickUtil.isClickWithShortTime(R.id.ivCameraChangeSide,3000))
                {
                    return;
                }
                activity.switchCamera();
                break;
            case R.id.ivRoomType:
                SetRoomTypeDialog setRoomTypeDialog=SetRoomTypeDialog.getInstance(true);
                setRoomTypeDialog.setOnSelectRoomTypeListener(new SetRoomTypeDialog.OnSelectRoomTypeListener() {
                    @Override
                    public void onSelect(int position) {

                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),setRoomTypeDialog);
                break;
            case R.id.rivProfileImage:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.AnchorSelf));
                break;
            case R.id.gtvOnlineAmount:
                OnlineNobilityAndUserDialog onlineNobilityAndUserDialog=OnlineNobilityAndUserDialog.getInstance(mBind.gtvOnlineAmount.getText().toString(),liveId,null,null);
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),onlineNobilityAndUserDialog);
                break;
            case R.id.gtvProtection:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), AnchorProtectorListDialog.getInstance("","",null));
                break;
            case R.id.gtvContribution:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), ContributionRankDialog.getInstance("",""));
                break;
            case R.id.ivSetting:
                AnchorLivingRoomSettingDialog anchorLivingRoomSettingDialog=AnchorLivingRoomSettingDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),anchorLivingRoomSettingDialog);
                break;

        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_start_living;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        mBind.llTopView.setVisibility(View.GONE);

        int screenHeight= ScreenUtils.getScreenHeightWithoutBtnsBar(getActivity());
        int screenWidth=ScreenUtils.getScreenWidth(getActivity());

        RelativeLayout.LayoutParams rlMessages=(RelativeLayout.LayoutParams)mBind.llMessages.getLayoutParams();
        rlMessages.height=(int)(screenHeight*0.5f)-ScreenUtils.getDip2px(getActivity(),45);
        rlMessages.width=(int)(screenWidth*0.7f);
        rlMessages.bottomMargin=ScreenUtils.getDip2px(getActivity(),45);
        rlMessages.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        mBind.llMessages.setLayoutParams(rlMessages);

        mBind.msgBox.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getContext(),2)));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.msgBox.setLayoutManager(linearLayoutManager);

        CountTimerUtil.getInstance().start(mBind.rlMain, new CountTimerUtil.OnAnimationFinishListener() {
            @Override
            public void onFinish() {
                mBind.llTopView.setVisibility(View.VISIBLE);
                checkAuth();
            }
        });
    }

    private void sendSystemMsgToChat(String message)
    {
        LivingMsgBoxBean bean = new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66ffffff);
        bean.setCharSequence(message);
        bean.setType(0);
        addNewMessage(bean);
    }

    private void addNewMessage(LivingMsgBoxBean bean) {
        if (livingMsgBoxAdapter == null) {
            livingMsgBoxAdapter = new LivingMsgBoxAdapter(getContext(), livingMsgBoxBeans);
            mBind.msgBox.setAdapter(livingMsgBoxAdapter);
        }
        if (livingMsgBoxAdapter.getBeans().size() > 499) {
            livingMsgBoxAdapter.getBeans().remove(0);
        }
        livingMsgBoxAdapter.getBeans().add(bean);
        livingMsgBoxAdapter.notifyDataSetChanged();
    }

    public void checkAndJoinIM() {
        String imLoginUser = V2TIMManager.getInstance().getLoginUser();
        //加入群聊前 先判断用户是否连接IM 如果未连接 则先连接IM后再加入IM群聊
        if (TextUtils.isEmpty(imLoginUser)) {
            //当前IM未连接用户 则先让用户连接IM后
            AppIMManager.ins().connectIM(new V2TIMCallback() {
                @Override
                public void onSuccess() {
                    //连接IM成功后 加入群聊
                    sendSystemMsgToChat(getString(R.string.connectedJoin));
                    mHandler.postDelayed(() ->
                            sendSystemMsgToChat(getString(R.string.liveSuccess)), 1000);
                }

                @Override
                public void onError(int code, String desc) {
                    LogUtils.e("IM->onError:" + code + "，" + desc);
                    if (6017 == code && "sdk not initialized".equals(desc)) {
                        getActivity().finish();
                    } else if (code == 9506) { //9506调用频率限制，最大每秒发起5次请求。
                        closeLiveRoom(desc, false);
                    } else if (code == 9520) {
                        closeLiveRoom(desc, false);
                    } else if (code == 6012) {
                        sendSystemMsgToChat(getString(R.string.chatRetrying) + code);
                        checkAndJoinIM();
                    } else {
                        closeLiveRoom(desc, false);
                    }
                }
            });
        } else {
            mHandler.postDelayed(() ->
                    sendSystemMsgToChat(getString(R.string.liveSuccess)), 1600);
        }
    }


    /**
     * 关闭直播间
     * 是否是管理员或者后台强制关播
     */
    public void closeLiveRoom(String finishTip, boolean isKick) {
        if(!isActivityOK())return;
        OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
        if (KeyboardUtils.isSoftInputVisible(getActivity())) {
            KeyboardUtils.toggleSoftInput();
        }
//        LiveFinishActivity.startActivity(getActivity(), anchor, finishTip, isKick);
        openLivingActivity.stopRTMPPush();
        //游戏退出
    }

    @Override
    public void onIMReceived(int protocol, String msg) {
        sendSystemMsgToChat(msg);
    }



    /**
     * 开始直播
     */
    public void checkAuth() {

        showLoadingDialogWithNoBgBlack();
        String nickName= DataCenter.getInstance().getUserInfo().getUser().getNickname();
        String title=getMainActivity().getRoomTitle();
        Api_Live.ins().getAnchorAuth("84","0",nickName,title,"100",new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    Log.e("checkAuth",data);
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        String pushStreamUrl=jsonObject.optString("pushStreamUrl","");
                        liveId=jsonObject.optString("liveId","");
                        if(TextUtils.isEmpty(pushStreamUrl) || TextUtils.isEmpty(liveId))
                        {
                            ToastUtils.showShort(getString(R.string.startLivingFail));
                            return;
                        }
                        getMainActivity().setPushUrl(pushStreamUrl);
                        AppIMManager.ins().addMessageListener(StartLivingFragment.class, StartLivingFragment.this);
                        OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
                        openLivingActivity.startRTMPPush();
                        checkAndJoinIM();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    try {
//                        JSONObject jb = new JSONObject(data);
//                        int auth = jb.optInt("auth");
//                        User user = DataCenter.getInstance().getUserInfo().getUser();
//                        if (user == null) {
//                            LogUtils.e("主播状态：" + "开启直播出错，用户信息失败");
//                            return;
//                        }
//
//                        user.setAuth(auth);
//                        DataCenter.getInstance().getUserInfo().updateUser(user);
//                        if (auth == 2 && BuildConfig.IsAnchorClient) {
//                            Constant.isAppInsideClick = true;
//                            Intent intent = new Intent(CenterOfAnchorActivity.this, AnchorLiveActivity.class);
//                            startActivity(intent);
//                        } else if (auth == 1) { //1待审核
//                            showToastTip(false, getString(R.string.certificating));
//                        } else { //未认证
//                            DialogFactory.showTwoBtnDialog(CenterOfAnchorActivity.this,
//                                    getString(R.string.certiGo), getString(R.string.cancel),
//                                    getString(R.string.goCerti), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
//                                        dialog.dismiss();
//                                        AuthActivity.startActivity(CenterOfAnchorActivity.this);
//                                    });
//                        }
//                    } catch (Exception e) {
//                        e.getStackTrace();
//                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private  OpenLivingActivity getMainActivity()
    {
        if(isActivityOK())
        {
            return ((OpenLivingActivity)getActivity());
        }
        return null;
    }
}
