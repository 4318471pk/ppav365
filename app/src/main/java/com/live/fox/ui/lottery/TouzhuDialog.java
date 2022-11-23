package com.live.fox.ui.lottery;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogTouzhuBinding;
import com.live.fox.dialog.bottomDialog.TimePickerDialog;

import com.live.fox.ui.lottery.adapter.KaiJiangRecordYflhcAdapter;
import com.live.fox.ui.lottery.adapter.KaiJiangResultIvAdapter;
import com.live.fox.ui.lottery.adapter.KaiJiangResultTvAdapter;
import com.live.fox.ui.lottery.adapter.LotteryTypeAdapter;
import com.live.fox.ui.lottery.adapter.NiuNiuAdapter;
import com.live.fox.ui.lottery.adapter.TouZhuRecordAdapter;
import com.live.fox.ui.lottery.adapter.TouZhuRecordMoreAdapter;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TouzhuDialog extends BaseBindingDialogFragment implements TouzhuItemListFragment.TouzhuSelectSuc {

    public static final int KAI_JIANG = 0; //开奖结果
    public static final int TZ_RECORD = 1; //投注记录
    public static final int KJ_RECORD = 2; //开奖记录
    public static final int TZ_RECORD_MORE = 3; //更多投注记录
    public static final int KJ_RECORD_MORE = 4; //更多开奖记录
    public static final int CZ_PLAY = 5; //游戏玩法说明

    DialogTouzhuBinding mBind;

    KaiJiangResultIvAdapter kaiJiangResultIvAdapter;
    KaiJiangResultTvAdapter kaiJiangResultTvAdapter;
    List<String> kjTitleList = new ArrayList<>();

    NiuNiuAdapter niuniuAdapter;
    List<String> niuniuList = new ArrayList<>();


    TouZhuRecordAdapter touZhuRecordAdapter;
    TouZhuRecordMoreAdapter touZhuRecordMoreAdapter;
    List<String> tzList = new ArrayList<>();

    BaseQuickAdapter kaiJiangRecordAdapter;//开奖结果，有一分快三，一分六合彩，鱼虾蟹

    List<String> kjList = new ArrayList<>();

    LotteryTypeAdapter lotteryTypeAdapter;
    List<Boolean> lotteryTypeList = new ArrayList<>();


    List<BaseFragment> fragmentList = new ArrayList<>();

    int showView = KAI_JIANG;

    SelectLotteryDialog selectLotteryDialog; //选择游戏
    SelectLotteryDialog selectLotteryStatusDialog; //选择游戏状态

    ChouMaDialog chouMaDialog; //选择投注dialog

    boolean isStartKjAni = false; // 是否不停换动开奖色子

    private final Timer mTimer = new Timer();
    private TimerTask kjTask;

    Handler mHandler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            for (int i = 0; i < kjTitleList.size(); i ++) {
                int res = (int) (Math.random() *6 + 1);
                kjTitleList.set(i, res+"");
            }
            kaiJiangResultIvAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClickView(View view) {
        if (view == mBind.rlMain || view == mBind.ivBack || view == mBind.ivBack2){
            dismiss();
        } else if (view == mBind.tvCharge2){
            RechargeActivity.startActivity(this.getContext());
        } else if (view == mBind.ivCz2) {
            showView = KAI_JIANG;
            changeShowView();
        } else if (view == mBind.tvTz || view == mBind.tvTz2) {
            if (showView == TZ_RECORD) return;
            showView = TZ_RECORD;
            mBind.tvMoreKj.setText(getResources().getText(R.string.more_tz));
            changeShowView();
        } else if (view == mBind.tvKj || view == mBind.tvKj2) {
            if (showView == KJ_RECORD) return;
            showView = KJ_RECORD;
            mBind.tvMoreKj.setText(getResources().getText(R.string.more_kj));
            changeShowView();
        } else if (view == mBind.tvMoreKj) {
            if (showView == KJ_RECORD) { //点击更多开奖
                showView = KJ_RECORD_MORE;
                changeShowView();
            } else { //点击更多投注
                showView = TZ_RECORD_MORE;
                changeShowView();
            }
        } else if (view == mBind.tvCzName || view == mBind.ivCzNameBot ) { //点击切换彩种
            if (selectLotteryDialog == null) {
                selectLotteryDialog = SelectLotteryDialog.newInstance(true);
            }
            selectLotteryDialog.setSelectLotterySuc(new SelectLotteryDialog.SelectLotterySuc() {
                @Override
                public void select(String name, int code) {
                    mBind.tvCzName.setText(name);
                }
            });
            DialogFramentManager.getInstance().showDialog(this.getActivity().getSupportFragmentManager(), selectLotteryDialog);
        } else if (view == mBind.tvStatus || view == mBind.ivStatus ) { //点击切换彩种状态
            if (selectLotteryStatusDialog == null) {
                selectLotteryStatusDialog = SelectLotteryDialog.newInstance(false);
            }
            selectLotteryStatusDialog.setSelectLotterySuc(new SelectLotteryDialog.SelectLotterySuc() {
                @Override
                public void select(String name, int code) {
                    mBind.tvStatus.setText(name);
                }
            });
            DialogFramentManager.getInstance().showDialog(this.getActivity().getSupportFragmentManager(), selectLotteryStatusDialog);
        } else if (view == mBind.tvTimeSelect|| view == mBind.ivTimeSelect ) { //点击切换彩种时间
            TimePickerDialog timePickerDialog = TimePickerDialog.getInstance(getResources().getString(R.string.choice_ymd));
            timePickerDialog.setOnSelectedListener(new TimePickerDialog.OnSelectedListener() {
                @Override
                public void onSelected(int year, int month, int date, long time) {
                    String s = month + getResources().getString(R.string.yueban) + date + getResources().getString(R.string.effectiveDay);
                    mBind.tvTimeSelect.setText(s);
                }
            });
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(this.getActivity().getSupportFragmentManager(),timePickerDialog);
            //timePickerDialog.setBtnColor(getResources().getColor(R.color.color0F86FF));
        } else if (view == mBind.layoutShowChouma) { //用户电点击选择筹码
            if (chouMaDialog == null) {
                chouMaDialog = ChouMaDialog.newInstance();
            }
            chouMaDialog.setSelectChouMaSuc(new ChouMaDialog.ChouMaSelectSuc() {
                @Override
                public void select(String money, Drawable img, boolean isZdy) {
                    mBind.ivChouma.setBackground(img);
                    if (isZdy) {
                        mBind.tvChouma.setVisibility(View.VISIBLE);
                        mBind.tvChouma.setText(money);
                        if (money.length() == 1) {
                            mBind.tvChouma.setTextSize(ScreenUtils.dp2px(TouzhuDialog.this.getContext(), 4));
                        } else if (money.length() == 2) {
                            mBind.tvChouma.setTextSize(ScreenUtils.dp2px(TouzhuDialog.this.getContext(), 3));
                        } else if (money.length() == 3) {
                            mBind.tvChouma.setTextSize(ScreenUtils.dp2px(TouzhuDialog.this.getContext(), 2));
                        } else if (money.length() == 4 || money.length() == 5) {
                            mBind.tvChouma.setTextSize(ScreenUtils.dp2px(TouzhuDialog.this.getContext(), 1));
                        }
                    } else {
                        mBind.tvChouma.setVisibility(View.GONE);
                    }
                }
            });
            DialogFramentManager.getInstance().showDialog(this.getActivity().getSupportFragmentManager(), chouMaDialog);
        } else if (view == mBind.tvTzAll) {
            ConfirmTouzhuDialog confirmTouzhuDialog = ConfirmTouzhuDialog.newInstance();
            DialogFramentManager.getInstance().showDialog(this.getActivity().getSupportFragmentManager(), confirmTouzhuDialog);
        } else if (view == mBind.tvWen) { //点击玩法说明
            showView = CZ_PLAY;
            changeShowView();
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_touzhu;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen_2);
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        changeShowView();
        mBind.tvMoreKj.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        test();
//        for(int i=0 ; i< lotteryTypeList.size(); i++) {
//
//        }

        TouzhuItemListFragment touzhuItemListFragment1 = TouzhuItemListFragment.newInstance(4);
        touzhuItemListFragment1.setTouzhuSelectSuc(this);
        TouzhuItemListFragment touzhuItemListFragment2 = TouzhuItemListFragment.newInstance(7);
        touzhuItemListFragment1.setTouzhuSelectSuc(this);
        TouzhuItemListFragment touzhuItemListFragment3 = TouzhuItemListFragment.newInstance(3, TouzhuItemListFragment.VIEW_YXX_2);
        touzhuItemListFragment1.setTouzhuSelectSuc(this);
        TouzhuItemListFragment touzhuItemListFragment4 = TouzhuItemListFragment.newInstance(4,TouzhuItemListFragment.VIEW_YXX_1);
        touzhuItemListFragment1.setTouzhuSelectSuc(this);
        TouzhuItemListFragment touzhuItemListFragment5 = TouzhuItemListFragment.newInstance(4);
        touzhuItemListFragment1.setTouzhuSelectSuc(this);
        fragmentList.add(touzhuItemListFragment1);
        fragmentList.add(touzhuItemListFragment2);
        fragmentList.add(touzhuItemListFragment3);
        fragmentList.add(touzhuItemListFragment4);
        fragmentList.add(touzhuItemListFragment5);


        kaiJiangResultIvAdapter = new KaiJiangResultIvAdapter(kjTitleList);
        kaiJiangResultTvAdapter = new KaiJiangResultTvAdapter(kjTitleList);
        LinearLayoutManager layoutManagerKjTv = new LinearLayoutManager(this.getContext());
        layoutManagerKjTv.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.rcKjTv.setLayoutManager(layoutManagerKjTv);
        mBind.rcKjTv.setAdapter(kaiJiangResultTvAdapter);
        LinearLayoutManager layoutManagerKjIv = new LinearLayoutManager(this.getContext());
        layoutManagerKjIv.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.rcKjIv.setLayoutManager(layoutManagerKjIv);
        mBind.rcKjIv.setAdapter(kaiJiangResultIvAdapter);

        niuniuList.add("1"); niuniuList.add("1");
        niuniuAdapter = new NiuNiuAdapter(niuniuList);
        mBind.rcKjTv.setAdapter(niuniuAdapter);
        mBind.rcKjIv.setVisibility(View.GONE);


        touZhuRecordAdapter = new TouZhuRecordAdapter(tzList);
        touZhuRecordMoreAdapter = new TouZhuRecordMoreAdapter(tzList);

       // kaiJiangRecordAdapter = new KaiJiangRecordAdapter(kjList); //一分快三
        kaiJiangRecordAdapter = new KaiJiangRecordYflhcAdapter(kjList,true); //一分六合彩和牛牛
       // kaiJiangRecordAdapter = new KaiJiangRecordYxxAdapter(kjList); //鱼虾蟹
        LinearLayoutManager layoutManagerKj = new LinearLayoutManager(this.getContext());
        layoutManagerKj.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rcRecord.setLayoutManager(layoutManagerKj);


        lotteryTypeAdapter = new LotteryTypeAdapter(lotteryTypeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.rcTzType.setLayoutManager(layoutManager);
        mBind.rcTzType.setAdapter(lotteryTypeAdapter);

        lotteryTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                changeLotteryHead(position,true);
            }
        });

        mBind.vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        mBind.vp.setCurrentItem(0);
        mBind.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeLotteryHead(position,false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       // startKjAni();

    }

    private void changeLotteryHead(int position, boolean changeItem){
        for (int i = 0; i < lotteryTypeList.size(); i ++) {
            if (lotteryTypeList.get(i)) {
                if (i == position) {
                    return;
                } else {
                    lotteryTypeList.set(i, false);
                    break;
                }
            }
        }
        lotteryTypeList.set(position, true);
        lotteryTypeAdapter.notifyDataSetChanged();
        if (changeItem){
            mBind.vp.setCurrentItem(position);
        }
    }

    private void test(){
        kjTitleList.add("1");kjTitleList.add("2");kjTitleList.add("3");
        tzList.add("1");tzList.add("1");tzList.add("1");tzList.add("1");tzList.add("1");
        tzList.add("1");tzList.add("1");tzList.add("1");tzList.add("1");tzList.add("1");
        kjList.add("1"); kjList.add("1"); kjList.add("1");kjList.add("1"); kjList.add("1"); kjList.add("1");
        kjList.add("1"); kjList.add("1"); kjList.add("1");kjList.add("1"); kjList.add("1"); kjList.add("1");
        lotteryTypeList.add(true);
        lotteryTypeList.add(false);
        lotteryTypeList.add(false);
        lotteryTypeList.add(false);
        lotteryTypeList.add(false);

    }

    private void changeShowView(){ //切换布局
        if (showView == KAI_JIANG) {
            mBind.layoutKj.setVisibility(View.VISIBLE);
            mBind.layoutTzRecord.setVisibility(View.GONE);
            mBind.layoutCz.setVisibility(View.VISIBLE);
            mBind.layoutCzStatus.setVisibility(View.GONE);
            mBind.layoutTzZj.setVisibility(View.GONE);
            changeCzPlayView(false);
        } else if (showView == TZ_RECORD || showView == KJ_RECORD){
            mBind.layoutKj.setVisibility(View.GONE);
            mBind.layoutTzRecord.setVisibility(View.VISIBLE);
            setRcRecordHeight(140);
            mBind.layoutCz.setVisibility(View.VISIBLE);
            mBind.tvMoreKj.setVisibility(View.VISIBLE);
            mBind.tvMoreKj2.setVisibility(View.GONE);
            if (showView == TZ_RECORD) {
                mBind.rcRecord.setAdapter(touZhuRecordAdapter);
                mBind.tvTz2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_type_2));
                mBind.tvKj2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_record));
            } else {
                mBind.rcRecord.setAdapter(kaiJiangRecordAdapter);
                mBind.tvTz2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_record));
                mBind.tvKj2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_type_2));
            }
            mBind.layoutCzStatus.setVisibility(View.GONE);
            mBind.layoutTzZj.setVisibility(View.GONE);
            changeCzPlayView(false);
            mBind.layoutRecord.setVisibility(View.VISIBLE);
        } else if (showView == KJ_RECORD_MORE){ //更多开奖页面
            changeCzPlayView(false);
            mBind.layoutCz.setVisibility(View.GONE);

            setRcRecordHeight(140+38+186+46);
            mBind.tvTz2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_record));
            mBind.tvKj2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_record));
            mBind.tvMoreKj.setVisibility(View.GONE);
            mBind.tvMoreKj2.setVisibility(View.VISIBLE);
            mBind.tvMoreKj2.setText(getResources().getText(R.string.more_kj));

            mBind.layoutCzStatus.setVisibility(View.GONE);
            mBind.layoutTzZj.setVisibility(View.GONE);

        } else if (showView == TZ_RECORD_MORE){ //更多投注页面
            mBind.tvMoreKj.setVisibility(View.GONE);
            mBind.tvMoreKj2.setVisibility(View.VISIBLE);
            mBind.tvMoreKj2.setText(getResources().getText(R.string.more_tz));
            mBind.tvTz2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_record));
            mBind.tvKj2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_record));

            mBind.layoutCzStatus.setVisibility(View.VISIBLE);
            mBind.layoutTzZj.setVisibility(View.VISIBLE);
            mBind.layoutCz.setVisibility(View.GONE);
            setRcRecordHeight(140+38+186+46-70-47);
            mBind.rcRecord.setAdapter(touZhuRecordMoreAdapter);

            changeCzPlayView(false);
        } else if (showView == CZ_PLAY){
            changeCzPlayView(true);
        }
    }

    private void changeCzPlayView(boolean isShow){ //切换彩种玩法说明
        if (isShow) {
            mBind.layoutCzPlay.setVisibility(View.VISIBLE);
            mBind.ivWen.setVisibility(View.VISIBLE);
            mBind.tvWen.setVisibility(View.GONE);
            mBind.tvMoreKj.setVisibility(View.GONE);

            mBind.layoutKj.setVisibility(View.GONE);
            mBind.layoutTzRecord.setVisibility(View.VISIBLE);
            mBind.layoutCz.setVisibility(View.GONE);
            mBind.layoutCzStatus.setVisibility(View.GONE);
            mBind.layoutRecord.setVisibility(View.GONE);
            mBind.layoutTzZj.setVisibility(View.GONE);
            mBind.layoutCz.setVisibility(View.GONE);

            mBind.tvTz2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_record));
            mBind.tvKj2.setBackground(getResources().getDrawable(R.drawable.bg_lottery_record));
        } else {
            //mBind.layoutTzRecord.setVisibility(View.VISIBLE);
            mBind.layoutCzPlay.setVisibility(View.GONE);
            mBind.ivWen.setVisibility(View.GONE);
            mBind.tvWen.setVisibility(View.VISIBLE);
        }
    }


    private void setRcRecordHeight(int height) {
        LinearLayout.LayoutParams linearParams =  (LinearLayout.LayoutParams)mBind.layoutRecord.getLayoutParams();
        linearParams.height = DeviceUtils.dp2px(this.getContext(), height);
        mBind.layoutRecord.setLayoutParams(linearParams);
    }


    private List<String> tzBackList = new ArrayList<>();

    @Override
    public void clickTz(String text) {
        if (text != null) {
            tzBackList.add(text);
        }
        mBind.tvTzAll.setAlpha(1);
    }

    @Override
    public void cancelTz(String text) {

    }

    public void startKjAni() {
        kjTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
        //启动定时器 参数对应为 TimerTask 延迟时间 间隔时间
        mTimer.schedule(kjTask, 5000, 50);
    }
}
