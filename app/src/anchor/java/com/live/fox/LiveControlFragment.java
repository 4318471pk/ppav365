package com.live.fox;


import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_HNCP;

import android.view.View;
import android.widget.TextView;

import com.live.fox.common.CommonLiveControlFragment;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.Audience;
import com.live.fox.entity.TwentyNineBean;
import com.live.fox.entity.User;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GsonUtil;
import com.live.fox.view.ChatPanelView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

/**
 * 主播端看直播页面
 * 直播控制页面
 */
public class LiveControlFragment extends CommonLiveControlFragment implements ChatPanelView.OnChatActionListener {

    TextView tvChat;
    RoundedImageView ivAd;

    @Override
    public void initAudienceRv() {
        super.initAudienceRv();
        rvAudience.setHorizontalFadingEdgeEnabled(true);
        rvAudience.setFadingEdgeLength(50);
        audienceAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (ClickUtil.isFastDoubleClick()) return;
            if (position < 0) return; //bugly有position=-1的情况
            Audience audience = audienceAdapter.getData().get(position);
            User user = new User();
            user.setUid(audience.getUid());
            user.setAvatar(audience.getAvatar());
            user.setUserExp(((Double)audience.getUserExp()).floatValue());
            user.setUserLevel(audience.getUserLevel());
            if (audience.getChatHide() == 0) {
                user.setNickname(getString(R.string.mysteriousMan));
                showUserDetailDialog(user);
            } else {
                user.setNickname(audience.getNickname());
            }
        });
    }

    @Override
    public void initChildView(View view) {
        super.initChildView(view);
        tvChat = view.findViewById(R.id.tv_chat);
        ivAd = view.findViewById(R.id.iv_ad);
    }

    @Override
    public void pkCountDown() {
        super.pkCountDown();
        if (pkCountDownTime <= 0) {
            isStartPKCountdown = false;
            if (pkType == 2) {
                //如果是惩罚阶段结束
                if (isAnchorLiveRoom) {
                    ((AnchorLiveActivity) requireActivity()).finishPK();
                }
            }
        } else {
            pkCountdownHandler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    @Override
    public void clickPlayRoot() {
        super.clickPlayRoot();
        if (getActivity() != null && isAnchorLiveRoom) {
            ((AnchorLiveActivity) getActivity()).hideBeauty();
        }
    }

    @Override
    public void setLiveRoom() {
        super.setLiveRoom();
        if (isAnchorLiveRoom) {
            ((AnchorLiveActivity) requireActivity()).showLiveSetDialog();
        }
    }

    @Override
    public void closeLiveRoom() {
        super.closeLiveRoom();
        if (isAnchorLiveRoom) {
            ((AnchorLiveActivity) requireActivity()).showFinishLiveDialog();
        } else {
            ((PlayLiveActivity) requireActivity()).outRoomByUserClose();
        }
    }

    @Override
    public void goToPkRoom() {
        super.goToPkRoom();
        if (isPKing && pkStatus != null) {
            DialogFactory.showTwoBtnDialog(getActivity(), getString(R.string.sureGo) +
                    pkStatus.getNickname(), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                dialog.dismiss();
                if (!isAnchorLiveRoom) {
                    ((PlayLiveActivity) requireActivity()).toLiveRoom(pkStatus);
                }
            });
        }
    }

    @Override
    public void lotteryDraw(JSONObject msg) {
        super.lotteryDraw(msg);
        String name;
        String nameTwo;
        if (liveStartLottery != null && liveStartLottery.size() == 2) {
            //后台相同的地方，用的参数不一样，为了兼容后台的差异
            if (AppConfig.isThLive()) {
                name = liveStartLottery.get(0).getLotteryName();
                nameTwo = liveStartLottery.get(1).getLotteryName();
            } else {
                name = liveStartLottery.get(0).getCpName();
                nameTwo = liveStartLottery.get(1).getCpName();
            }

            boolean flag = TYPE_CP_HNCP.equals(name) || TYPE_CP_HNCP.equals(nameTwo);

            if (nameTwo.equals(msg.optString("name"))) {
                TwentyNineBean lsResult1 = GsonUtil.getObject(msg.toString(), TwentyNineBean.class);
                kjjgMgr.addDrawResult(lsResult1);
            } else if (name.equals(msg.optString("name"))) {
                TwentyNineBean lsResult2 = GsonUtil.getObject(msg.toString(), TwentyNineBean.class);
                if (flag) {
                    kjjgMgr.addDrawResult(lsResult2);
                } else {
                    kjjgMgr2.addDrawResult(lsResult2);
                }
            }
        } else if (liveStartLottery != null && liveStartLottery.size() == 1) {
            TwentyNineBean lsResult = GsonUtil.getObject(msg.toString(), TwentyNineBean.class);
            kjjgMgr.addDrawResult(lsResult);
        }
    }

}
