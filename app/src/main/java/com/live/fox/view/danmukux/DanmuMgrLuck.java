package com.live.fox.view.danmukux;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.view.danmu2.DanmuViewHolder;

import java.util.ArrayList;
import java.util.List;


public class DanmuMgrLuck {

    private Context mContext;
    private DanmuContainerLuckView mDanmuContainerView;
    private long lastDanmuTime = 0L;

    ViewGroup mDanmuContainer;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1011:
                    DanmuEntity danmuEntity = (DanmuEntity) msg.obj;
                    mDanmuContainerView.addDanmu(danmuEntity);
                    break;
            }
        }
    };

    public DanmuMgrLuck(Context context) {
        this.mContext = context;
    }

    /**
     * 设置弹幕view
     *
     * @param danmakuView 弹幕view
     */
    public void setDanmakuView(DanmuContainerLuckView danmakuView) {
        this.mDanmuContainerView = danmakuView;
        initDanmuView();
    }

    public void setDanmakuView(ViewGroup danmuContainer) {
        this.mDanmuContainer = danmuContainer;
//        initDanmuView();
    }

    private void initDanmuView() {
        mDanmuContainerView.setConverter(danmuConverter);
        mDanmuContainerView.setLeader(this);
        mDanmuContainerView.setSpeed(DanmuContainerLuckView.NORMAL_SPEED);
    }

    DanmuConverter danmuConverter = new DanmuConverter<DanmuEntity>() {
        @Override
        public int getSingleLineHeight() {
            //将所有类型弹幕的布局拿出来，找到高度最大值，作为弹道高度
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_haoqigift_pp, null);
            //指定行高
            view.measure(0, 0);

            return view.getMeasuredHeight();
        }

        @Override
        public View convert(DanmuEntity model) {
            View view = null;
            //普通弹幕
            if (model.getType() == 0) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_danmu, null);
                TextView content = view.findViewById(R.id.content);
                content.setText(model.content);
            } else if (model.getType() == 11) { //豪气礼物
                view = LayoutInflater.from(mContext).inflate(R.layout.item_haoqigift_pp, null);
                TextView content = view.findViewById(R.id.content);
                content.setText(model.spannableStr);
            } else if (model.getType() == 13) { //直播间飘屏广告

                view = LayoutInflater.from(mContext).inflate(R.layout.item_piaopingad_danmu, null);
                TextView content = view.findViewById(R.id.content);
                content.setText(model.content);

                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                            H5Activity.start(mContext, "广告", roomAd.getJumpUrl());
                        try {
                            if (!StringUtils.isEmpty(model.getNickname())) {
                                IntentUtils.toBrowser(mContext, model.getNickname());
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            }
            return view;
        }
    };


    public void addDanmu(String headUrl, String sender, String text) {
        DanmuEntity danmuEntity = new DanmuEntity();
        danmuEntity.setAvatar(headUrl);
        danmuEntity.setContent(new SpanUtils().append("  " + sender.trim()).setForegroundColor(Color.parseColor("#FFFFFF")).append("\n").append("  " + text).setForegroundColor(Color.parseColor("#FEEF41")).create());
        danmuEntity.setType(0);
        addDanmu(danmuEntity);
    }


    public void addHQDanmu(String sendName, String anchorName, String giftName) {
        DanmuEntity danmuEntity = new DanmuEntity();
        String text = sendName + mContext.getString(R.string.zai) + anchorName + mContext.getString(R.string.anchorRoomSend) + giftName;
        if (!TextUtils.isEmpty(sendName)) {
            SpannableString spanStr = new SpannableString(text);
            spanStr.setSpan(new ForegroundColorSpan(Color.parseColor("#F7D55B")), 0, sendName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            danmuEntity.spannableStr = spanStr;
        }
        danmuEntity.setContent(text);
        danmuEntity.setType(11);
        addDanmu(danmuEntity);
    }


    public void addDanmu(DanmuEntity danmuEntity) {
        long delayTime = 0L;
        if (lastDanmuTime > System.currentTimeMillis() - 2 * 1000) {
            lastDanmuTime += 5 * 1000;
            delayTime = lastDanmuTime - System.currentTimeMillis();
        } else {
            lastDanmuTime = System.currentTimeMillis();
            delayTime = 0L;
        }
        Message message = new Message();
        message.obj = danmuEntity;
        message.what = 1011;
        mHandler.sendMessageDelayed(message, delayTime);
    }

    List<DanmuEntity> mList = new ArrayList<>();
    DanmuViewHolder danmuHolder;

    //直播间飘屏广告
    public void addRoomPiaoPingAd(String content, String jumpUrl) {
        DanmuEntity danmuEntity = new DanmuEntity();
        danmuEntity.setType(13);
        danmuEntity.setContent(content);
        danmuEntity.setNickname(jumpUrl);
//        addDanmu(danmuEntity);

        if (mList.size() == 0) {
            mList.add(danmuEntity);
            danmuHolder = new DanmuViewHolder(mContext, mDanmuContainer);
            danmuHolder.setActionListener(new DanmuViewHolder.ActionListener() {
                @Override
                public void onCanNext(int lineNum) {

                }

                @Override
                public void onAnimEnd(DanmuViewHolder vh) {
                    mList.remove(0);
                    if (mList.size() > 0) {
                        danmuHolder.show(mList.get(0), 0);
                    }
                }
            });
            danmuHolder.show(danmuEntity, 0);
        } else {
            mList.add(danmuEntity);
        }

    }

    public void destroy() {
        mHandler.removeMessages(1011);
    }
}
