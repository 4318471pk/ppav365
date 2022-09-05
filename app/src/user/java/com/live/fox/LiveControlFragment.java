package com.live.fox;


import com.live.fox.common.CommonLiveControlFragment;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.Audience;
import com.live.fox.entity.TwentyNineBean;
import com.live.fox.entity.User;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GsonUtil;
import com.live.fox.view.ChatPanelView;

import org.json.JSONObject;

public class LiveControlFragment extends CommonLiveControlFragment implements ChatPanelView.OnChatActionListener {

    @Override
    public void initAudienceRv() {
        super.initAudienceRv();
        audienceAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (ClickUtil.isFastDoubleClick()) return;
            if (position < 0) return; //bugly有position=-1的情况
            Audience audience = audienceAdapter.getData().get(position);
            User user = new User();
            user.setUid(audience.getUid());
            user.setAvatar(audience.getAvatar());
            user.setNickname(audience.getNickname());
            user.setUserExp(audience.getUserExp());
            user.setUserLevel(audience.getUserLevel());
            if (audience.getChatHide() == 0) {
                showUserDetailDialog(user);
                user.setNickname(audience.getNickname());
            } else {
                user.setNickname(getString(R.string.mysteriousMan));
            }
        });
    }

    @Override
    public void pkCountDown() {
        super.pkCountDown();
        if (pkCountDownTime <= 0) {
            isStartPKCountdown = false;
        } else {
            pkCountdownHandler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    @Override
    public void closeLiveRoom() {
        super.closeLiveRoom();
        if (!isAnchorLiveRoom) {
            ((PlayLiveActivity) requireActivity()).outRoomByUserClose();
        }
    }

    @Override
    public void goToPkRoom() {
        super.goToPkRoom();
        if (isPKing && pkStatus != null) {
            DialogFactory.showTwoBtnDialog(requireActivity(),
                    getString(R.string.sureGo) + pkStatus.getNickname(),
                    (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
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
        if (liveStartLottery.size() == 2) {
            String name;
            String nameTwo;
            if (AppConfig.isThLive()){
                name = liveStartLottery.get(1).getLotteryName();
                nameTwo = liveStartLottery.get(0).getLotteryName();
            }else {
                name = liveStartLottery.get(1).getCpName();
                nameTwo = liveStartLottery.get(0).getCpName();
            }
            if (name.equals(msg.optString("name"))) {
                TwentyNineBean lsResult1 = GsonUtil.getObject(msg.toString(), TwentyNineBean.class);
                kjjgMgr.addDrawResult(lsResult1);
            } else if (nameTwo.equals(msg.optString("name"))) {
                TwentyNineBean lsResult2 = GsonUtil.getObject(msg.toString(), TwentyNineBean.class);
                kjjgMgr2.addDrawResult(lsResult2);
            }
        } else if (liveStartLottery.size() == 1) {
            TwentyNineBean lsResult = GsonUtil.getObject(msg.toString(), TwentyNineBean.class);
            kjjgMgr.addDrawResult(lsResult);
        }
    }

}
