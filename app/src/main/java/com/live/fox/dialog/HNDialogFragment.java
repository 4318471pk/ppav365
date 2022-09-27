package com.live.fox.dialog;


import static com.live.fox.common.CommonLiveControlFragment.TIME_INTERVAL;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.roundview.RoundTextView;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.HnAdapter;
import com.live.fox.adapter.HnAdapterc;
import com.live.fox.adapter.HnAdapters;
import com.live.fox.base.MinuteGamePresenter;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.contract.MinuteGameContract;
import com.live.fox.entity.cp.CpFactory;
import com.live.fox.entity.cp.CpOutputFactory;
import com.live.fox.entity.response.ChipsVO;
import com.live.fox.entity.response.CpGameResultInfoVO;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.entity.response.LotteryCpVO;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.entity.response.Nums;
import com.live.fox.manager.DataCenter;
import com.live.fox.svga.BetCartDataManager;
import com.live.fox.mvp.MvpDialogFragment;
import com.live.fox.mvp.PresenterInject;
import com.live.fox.server.Api_Cp;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.NumberUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.view.MaxHeightRecyclerView;
import com.live.fox.view.RiseNumberTextView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 河内彩票
 */
@PresenterInject(MinuteGamePresenter.class)
public class HNDialogFragment extends MvpDialogFragment<MinuteGamePresenter> implements View.OnClickListener,
        MinuteGameContract.View, AppIMManager.OnMessageReceivedListener {

    private View aboveView;
    private ImageView ivArrow;
    private AppCompatSpinner spinner_simple;
    private TextView tvCountDown;
    private TextView tvCountDownT;
    private RadioGroup rgTab1;
    private RadioGroup rgTab2;
    private RadioGroup rgTab3;
    private TextView tvGoldCoin;
    private TextView tvCode;
    private ImageView tvCodeBg;
    private RelativeLayout xuanhaoRl;
    private RelativeLayout shuruRl;
    private RadioGroup rgShuru;
    private EditText etShuru;
    private MaxHeightRecyclerView rvCart;
    private RadioButton rb_checked;
    private LinearLayout ll_result;
    private TextView tv_title;
    private RiseNumberTextView tv_tm;
    private RiseNumberTextView tvg1;
    private RiseNumberTextView tvg2;
    private RiseNumberTextView tvg3;
    private RiseNumberTextView tvg4;
    private RiseNumberTextView tvg5;
    private RiseNumberTextView tvg6;
    private RiseNumberTextView tvg7;
    private RiseNumberTextView tvg8;
    private RiseNumberTextView tvg9;
    private RiseNumberTextView tvg10;
    private RiseNumberTextView tvg11;
    private RiseNumberTextView tvg12;
    private RiseNumberTextView tvg13;
    private RiseNumberTextView tvg14;
    private RiseNumberTextView tvg15;
    private RiseNumberTextView tvg16;
    private RiseNumberTextView tvg17;
    private RiseNumberTextView tvg18;
    private RiseNumberTextView tvg19;
    private RiseNumberTextView tvg20;
    private RiseNumberTextView tvg21;
    private RiseNumberTextView tvg22;
    private RiseNumberTextView tvg23;
    private RiseNumberTextView tvg24;
    private RiseNumberTextView tvg25;
    private RiseNumberTextView tvg26;
    private HNCartDialogFragment fragment2;
    private HnAdapter adapter1;
    private HnAdapters adapter2;
    private RadioGroup rgRatio4;
    private HnAdapterc adapter3;
    private final List<Nums> numDatas = new ArrayList<>();
    private String lottryName;
    private List<MinuteTabItem> tabItems;
    private List<CpGameResultInfoVO> globalResult;
    long mLastClickTime;
    long liveId;
    private ChipsVO chipsVO;
    private long timelong;//单位分钟
    public int whichPagea = 1;
    public int whichPage2 = 1;
    public static int whichPage = 0;
    public static long S_COUNTDOWN = 60;
    public static String S_EXPECT = null;
    public static String CurEXPECT = "";
    boolean flag = false;
    boolean isGetResult = true;
    boolean flagc1 = true;
    public static String[] result;
    private boolean isEnterFP = true;

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAdded()) {
                long formatTitle = S_COUNTDOWN - SystemClock.elapsedRealtime();
                long second = formatTitle / 1000;
                String text;
                if (tvCountDown == null) return;

                long m = second / 60;
                long sec = second % 60;
                long temp = timelong * 60 - 5;
                if (second > 0) {
                    isEnterFP = false;
                    result = null;
                    ll_result.setVisibility(View.GONE);
                    spinner_simple.setVisibility(View.VISIBLE);
                    if (second >= temp && second <= timelong * 60) {//175-180
                        text = getString(R.string.closing);
                    } else {
                        isGetResult = true;
                        if (m < 10) {
                            if (sec < 10) {
                                text = "0" + m + ":0" + sec;
                            } else {
                                text = "0" + m + ":" + sec;
                            }
                        } else {
                            if (sec < 10) {
                                text = m + ":0" + sec;
                            } else {
                                text = m + ":" + sec;
                            }
                        }
                    }
                    tvCountDown.postDelayed(this, 1000);
                } else {
                    text = getString(R.string.closing);
                    if (isGetResult) {
                        if (S_EXPECT.equals(CurEXPECT) && result != null && result.length != 0) {
                            showKJ();
                            if (fragment2 != null) {
                                fragment2.dismiss();
                            }
                            ll_result.setVisibility(View.VISIBLE);
                            spinner_simple.setVisibility(View.GONE);
                            tv_title.setText(Html.fromHtml("<font><big>" +
                                    getString(R.string.historyRecord) + "</big><small>(" + getString(R.string.phase_number) + S_EXPECT + ")</small></font>"));
                            tvCountDown.postDelayed(this, 8000);
                        } else {
                            if (isEnterFP) {
                                if (second <= -25) {
                                    startKJPlay();
                                    handler.sendEmptyMessageDelayed(200, (-second - 25) * 1000);
                                    tvCountDown.postDelayed(this, (-second - 2) * 1000);
                                } else if (second < -11) {
                                    startKJPlay();
                                    handler.sendEmptyMessageDelayed(200, 0);
                                    tvCountDown.postDelayed(this, (-second - 2) * 1000);
                                } else if (second < -3) {
                                    tvCountDown.postDelayed(this, (-second - 2) * 1000);
                                } else {
                                    tvCountDown.postDelayed(this, 0);
                                }
                            } else {
                                startKJPlay();
                                handler.sendEmptyMessageDelayed(200, 5000);//等五秒开奖
                                tvCountDown.postDelayed(this, 28000);
                            }
                        }
                        CurEXPECT = S_EXPECT;
                    } else {
                        tvCountDown.postDelayed(this, 1000);//5000?
                        doGetCpInfo();
                    }
                    isGetResult = false;
                }
                tvCountDown.setText(text);
            }
        }
    };

    private final Handler handler = new Handler(message -> {
        if (message.what == 200) {
            doHNInfo();
        }
        return false;
    });

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        return presenter.initDialog(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hn_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aboveView = view.findViewById(R.id.aboveView);
        ImageView ivCp = view.findViewById(R.id.ivCp);
        ImageView ivWanfa = view.findViewById(R.id.ivWanfa);
        ivArrow = view.findViewById(R.id.ivArrow);
        spinner_simple = view.findViewById(R.id.spinner_simple);
        tvCountDown = view.findViewById(R.id.tvCountDown);
        tvCountDownT = view.findViewById(R.id.tvCountDownT);
        rgTab1 = view.findViewById(R.id.rgTab1);
        rgTab2 = view.findViewById(R.id.rgTab2);
        RecyclerView rvCheck1 = view.findViewById(R.id.rvCheck1);
        rgTab3 = view.findViewById(R.id.rgTab3);
        RecyclerView rvCheck2 = view.findViewById(R.id.rvCheck2);
        rgRatio4 = view.findViewById(R.id.rgRatio4);
        tvGoldCoin = view.findViewById(R.id.tvGoldCoin);
        RoundTextView rtvRecharge = view.findViewById(R.id.rtvRecharge);
        RoundTextView rtvBet = view.findViewById(R.id.rtvBet);
        tvCode = view.findViewById(R.id.tvCode);
        tvCodeBg = view.findViewById(R.id.tvCode_bg);
        xuanhaoRl = view.findViewById(R.id.xuanhaoRl);
        shuruRl = view.findViewById(R.id.shuruRl);
        rb_checked = view.findViewById(R.id.rb18);
        TextView tvShuru = view.findViewById(R.id.tv_shuru);
        rgShuru = view.findViewById(R.id.rgShuru);
        etShuru = view.findViewById(R.id.et_shuru);
        rvCart = view.findViewById(R.id.rvCart);
        ll_result = view.findViewById(R.id.ll_result);
        tv_title = view.findViewById(R.id.tv_title);
        tv_tm = view.findViewById(R.id.tv_tm);
        tvg1 = view.findViewById(R.id.tvg1);
        tvg2 = view.findViewById(R.id.tvg2);
        tvg3 = view.findViewById(R.id.tvg3);
        tvg4 = view.findViewById(R.id.tvg4);
        tvg5 = view.findViewById(R.id.tvg5);
        tvg6 = view.findViewById(R.id.tvg6);
        tvg7 = view.findViewById(R.id.tvg7);
        tvg8 = view.findViewById(R.id.tvg8);
        tvg9 = view.findViewById(R.id.tvg9);
        tvg10 = view.findViewById(R.id.tvg10);
        tvg11 = view.findViewById(R.id.tvg11);
        tvg12 = view.findViewById(R.id.tvg12);
        tvg13 = view.findViewById(R.id.tvg13);
        tvg14 = view.findViewById(R.id.tvg14);
        tvg15 = view.findViewById(R.id.tvg15);
        tvg16 = view.findViewById(R.id.tvg16);
        tvg17 = view.findViewById(R.id.tvg17);
        tvg18 = view.findViewById(R.id.tvg18);
        tvg19 = view.findViewById(R.id.tvg19);
        tvg20 = view.findViewById(R.id.tvg20);
        tvg21 = view.findViewById(R.id.tvg21);
        tvg22 = view.findViewById(R.id.tvg22);
        tvg23 = view.findViewById(R.id.tvg23);
        tvg24 = view.findViewById(R.id.tvg24);
        tvg25 = view.findViewById(R.id.tvg25);
        tvg26 = view.findViewById(R.id.tvg26);
        rtvBet.setOnClickListener(this);
        ivWanfa.setOnClickListener(this);
        rtvRecharge.setOnClickListener(this);
        tvCode.setOnClickListener(this);

        if (getArguments() != null) {
            Serializable serializable = getArguments().getSerializable(ChipsVO.class.getSimpleName());
            if (serializable != null) {
                chipsVO = (ChipsVO) serializable;
                lottryName = chipsVO.getName();
                if (TextUtils.isEmpty(chipsVO.getIcon())) {
                    ivCp.setImageResource(chipsVO.resId);
                } else {
                    GlideUtils.loadImage(requireContext(), chipsVO.getIcon(), ivCp);
                }
                MinuteTabItem.lotteryTitle = chipsVO.getChinese();
            }
            Nums num;
            for (int i = 0; i < 100; i++) {
                num = new Nums(i, false);
                numDatas.add(num);
            }
        } else {
            dismiss();
            return;
        }

        adapter1 = new HnAdapter(new ArrayList<>(), whichPagea);
        adapter2 = new HnAdapters(new ArrayList<>(), whichPagea);
        adapter3 = new HnAdapterc(new ArrayList<>(), whichPagea);
        rvCheck1.setAdapter(adapter1);
        rvCheck2.setAdapter(adapter2);
        rvCart.setAdapter(adapter3);
        String goldCoinNum = DataCenter.getInstance().getUserInfo().getUser().getGoldCoin() + "";
        if ("0.00".equals(goldCoinNum))
            goldCoinNum = "0";

        tvGoldCoin.setText(RegexUtils.westMoney(Double.parseDouble(goldCoinNum)));//Integer.parseInt(goldCoinNum)
        liveId = getArguments().getLong("liveId", 0);
        CpFactory factory = new CpFactory();
        CpOutputFactory cpOutputFactory = factory.createFactory(lottryName);//自定义数据lottryName
        LotteryCpVO lotteryCpVO = cpOutputFactory.getCpVoByType(requireActivity());
        tabItems = lotteryCpVO.getTabItems();
        GridLayoutManager grid1 = new GridLayoutManager(getActivity(), 10);
        rvCheck1.setLayoutManager(grid1);
        GridLayoutManager grid2 = new GridLayoutManager(getActivity(), 10);
        rvCheck2.setLayoutManager(grid2);
        GridLayoutManager grid3 = new GridLayoutManager(getActivity(), 10);
        rvCart.setLayoutManager(grid3);

        spinner_simple.setBackgroundColor(Color.TRANSPARENT);
        String[] spinner = getResources().getStringArray(R.array.study_view_spinner_values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.item_simple_spinner, spinner);

        spinner_simple.setAdapter(adapter);
        spinner_simple.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ivArrow.setImageResource(R.drawable.hnblow);
                whichPagea = position + 1;
                whichPage = whichPagea * 10 + whichPage2;
                if (flag) {
                    rgTab1.clearCheck();
                    notifyDataSetChanged();
                } else {
                    adapter1.getData().clear();
                    adapter1.getData().addAll(tabItems.get(0).getBetItems());
                    adapter1.changedPage(whichPagea);
                    adapter1.notifyDataSetChanged();
                    adapter2.getData().clear();
                    adapter2.getData().addAll(tabItems.get(0).getBetItems());
                    adapter2.changedPage(whichPagea);
                    adapter2.notifyDataSetChanged();
                    adapter3.getData().clear();
                    adapter3.getData().addAll(numDatas);
                    adapter3.changedPage(whichPagea);
                    adapter3.notifyDataSetChanged();
                }
                flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ivArrow.setImageResource(R.drawable.hnblow);
            }
        });
        rgTab1.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            if (radioButton == null)
                return;
            if (1 == whichPagea) {//由于RadioGroup监听不到重复事件
                radioButton.setBackgroundResource(R.drawable.arb_tab_selector);
            } else {
                radioButton.setBackgroundResource(R.drawable.arb_tab_selectorb);
            }
            int anInt = Integer.parseInt(group.findViewById(checkedId).getTag().toString());
            whichPage2 = anInt;
            whichPage = whichPagea * 10 + anInt;
            if (1 == anInt) {
                xuanhaoRl.setVisibility(View.VISIBLE);
                shuruRl.setVisibility(View.GONE);
                rvCart.setVisibility(View.GONE);
            } else if (2 == anInt) {
                xuanhaoRl.setVisibility(View.GONE);
                shuruRl.setVisibility(View.VISIBLE);
                rvCart.setVisibility(View.GONE);
            } else {
                xuanhaoRl.setVisibility(View.GONE);
                shuruRl.setVisibility(View.GONE);
                rvCart.setVisibility(View.VISIBLE);
            }
        });
        rgTab2.setOnCheckedChangeListener((group, checkedId) -> {
            adapter1.getData().clear();
            RadioButton radioButton = group.findViewById(checkedId);
            if (radioButton == null)
                return;
            if (1 == whichPagea) {
                radioButton.setBackgroundResource(R.drawable.hn_tab_selector);
            } else {
                radioButton.setBackgroundResource(R.drawable.hn_tab_selectorb);
            }
            int anInt = Integer.parseInt(radioButton.getTag().toString());
            whichPage2 = anInt;
            List<MinuteTabItem> betItems = tabItems.get(0).getBetItems();
            switch (anInt) {
                case 0:
                    for (int j = 0; j < betItems.size(); j++) {
                        betItems.get(j).check = true;
                    }
                    break;
                case 1:
                    for (int j = 0; j < betItems.size(); j++) {
                        betItems.get(j).check = j > 4;
                    }
                    break;
                case 2:
                    for (int j = 0; j < betItems.size(); j++) {
                        betItems.get(j).check = j < 5;
                    }
                    break;
                case 3:
                    for (int j = 1; j < betItems.size(); j += 2) {
                        betItems.get(j).check = true;
                    }
                    for (int j = 0; j < betItems.size(); j += 2) {
                        betItems.get(j).check = false;
                    }
                    break;
                case 4:
                    for (int j = 0; j < betItems.size(); j += 2) {
                        betItems.get(j).check = true;
                    }
                    for (int j = 1; j < betItems.size(); j += 2) {
                        betItems.get(j).check = false;
                    }
                    break;
            }
            adapter1.getData().addAll(betItems);
            adapter1.changedPage(whichPagea);
            adapter1.notifyDataSetChanged();
        });

        rgTab3.setOnCheckedChangeListener((group, checkedId) -> {
            adapter2.getData().clear();
            RadioButton radioButton = group.findViewById(checkedId);
            if (radioButton == null)
                return;
            if (1 == whichPagea) {
                radioButton.setBackgroundResource(R.drawable.hn_tab_selector);
            } else {
                radioButton.setBackgroundResource(R.drawable.hn_tab_selectorb);
            }
            int anInt = Integer.parseInt(group.findViewById(checkedId).getTag().toString());
            List<MinuteTabItem> betItems = tabItems.get(0).getBetItems();
            switch (anInt) {
                case 0:
                    for (int j = 0; j < betItems.size(); j++) {
                        betItems.get(j).hncheck = true;
                    }
                    break;
                case 1:
                    for (int j = 0; j < betItems.size(); j++) {
                        betItems.get(j).hncheck = j > 4;
                    }
                    break;
                case 2:
                    for (int j = 0; j < betItems.size(); j++) {
                        betItems.get(j).hncheck = j < 5;
                    }
                    break;
                case 3:
                    for (int j = 1; j < betItems.size(); j += 2) {
                        betItems.get(j).hncheck = true;
                    }
                    for (int j = 0; j < betItems.size(); j += 2) {
                        betItems.get(j).hncheck = false;
                    }
                    break;
                case 4:
                    for (int j = 0; j < betItems.size(); j += 2) {
                        betItems.get(j).hncheck = true;
                    }
                    for (int j = 1; j < betItems.size(); j += 2) {
                        betItems.get(j).hncheck = false;
                    }
                    break;
            }
            adapter2.getData().addAll(tabItems.get(0).getBetItems());
            adapter2.changedPage(whichPagea);
            adapter2.notifyDataSetChanged();
        });
        adapter1.setOnItemChildClickListener((adapter4, view1, position) -> {
            MinuteTabItem it = adapter1.getData().get(position);
            it.check = !it.check;
            it.hncheck = !it.hncheck;
            adapter1.notifyItemChanged(position);
        });
        adapter2.setOnItemChildClickListener((adapter42, view12, position) -> {
            MinuteTabItem it = adapter2.getData().get(position);
            it.hncheck = !it.hncheck;
            adapter2.notifyItemChanged(position);
        });
        adapter3.setOnItemChildClickListener((adapter43, view13, position) -> {
            Nums it = adapter3.getData().get(position);
            it.check = !it.check;
            adapter3.notifyItemChanged(position);
        });


        setFollowUp();

        //输入
        rgShuru.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = group.findViewById(checkedId);
            if (radioButton == null)
                return;
            if (1 == whichPagea) {
                radioButton.setBackgroundResource(R.drawable.rb_ratio_check_selector);
            } else {
                radioButton.setBackgroundResource(R.drawable.rb_ratio_check_selectorb);
            }
            int anInt = Integer.parseInt(group.findViewById(checkedId).getTag().toString());
            StringBuilder num = new StringBuilder();
            ArrayList<Integer> randomData = new ArrayList<>();
            for (int i = 0; i < anInt; i++) {
                randomData.add(NumberUtils.getRandom(0, 99));
            }
            for (Integer random : randomData) {
                num.append(random).append(",");
            }
            etShuru.setText(num.toString());
        });

        ((RadioButton) rgTab1.getChildAt(0)).setChecked(true);
        setItemInputValue();
        doGetCpInfo();//信息
        AppIMManager.ins().addMessageListener(HNDialogFragment.class, this);
        ((RadioButton) rgRatio4.getChildAt(BetCartDataManager.betGameIndex)).setChecked(true);
    }

    /**
     * 设置跟投
     */
    private void setFollowUp() {
        rgRatio4.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    BetCartDataManager.betGameIndex = i;
                    break;
                }
            }
            RadioButton radioButton = group.findViewById(checkedId);
            if (1 == whichPagea) {
                radioButton.setBackgroundResource(R.drawable.rb_ratio_check_selector);
            } else {
                radioButton.setBackgroundResource(R.drawable.rb_ratio_check_selectorb);
            }
            String string = group.findViewById(checkedId).getTag().toString();
            if (!TextUtils.isEmpty(string)) {
                tabItems.get(0).getBetItems().get(0).mutiple = Integer.parseInt(string);
                tabItems.get(1).getBetItems().get(0).mutiple = Integer.parseInt(string);
                tabItems.get(2).getBetItems().get(0).mutiple = Integer.parseInt(string);
            }
        });
    }

    public void doGetCpInfo() {
        HashMap<String, Object> paramsPeriod = BaseApi.getCommonParams();
        paramsPeriod.put("name", lottryName);
        presenter.doGetGamePeriodInfo(paramsPeriod);
    }

    private void startKJPlay() {
        ll_result.setVisibility(View.VISIBLE);
        spinner_simple.setVisibility(View.GONE);
        tvg1.setTBTextColor(Color.WHITE);
        tvg1.startPlay(10000, 99999);
        tvg2.setTBTextColor(Color.WHITE);
        tvg2.startPlay(10000, 99999);
        tvg3.setTBTextColor(Color.WHITE);
        tvg3.startPlay(10000, 99999);
        tvg4.setTBTextColor(Color.WHITE);
        tvg4.startPlay(10000, 99999);
        tvg5.setTBTextColor(Color.WHITE);
        tvg5.startPlay(10000, 99999);
        tvg6.setTBTextColor(Color.WHITE);
        tvg6.startPlay(10000, 99999);
        tvg7.setTBTextColor(Color.WHITE);
        tvg7.startPlay(10000, 99999);
        tvg8.setTBTextColor(Color.WHITE);
        tvg8.startPlay(10000, 99999);
        tvg9.setTBTextColor(Color.WHITE);
        tvg9.startPlay(10000, 99999);
        tvg10.setTBTextColor(Color.WHITE);
        tvg10.startPlay(10000, 99999);
        tvg11.setTBTextColor(Color.WHITE);
        tvg11.startPlay(1000, 9999);
        tvg12.setTBTextColor(Color.WHITE);
        tvg12.startPlay(1000, 9999);
        tvg13.setTBTextColor(Color.WHITE);
        tvg13.startPlay(1000, 9999);
        tvg14.setTBTextColor(Color.WHITE);
        tvg14.startPlay(1000, 9999);
        tvg15.setTBTextColor(Color.WHITE);
        tvg15.startPlay(1000, 9999);
        tvg16.setTBTextColor(Color.WHITE);
        tvg16.startPlay(1000, 9999);
        tvg17.setTBTextColor(Color.WHITE);
        tvg17.startPlay(1000, 9999);
        tvg18.setTBTextColor(Color.WHITE);
        tvg18.startPlay(1000, 9999);
        tvg19.setTBTextColor(Color.WHITE);
        tvg19.startPlay(1000, 9999);
        tvg20.setTBTextColor(Color.WHITE);
        tvg20.startPlay(1000, 9999);
        tvg21.setTBTextColor(Color.WHITE);
        tvg21.startPlay(100, 999);
        tvg22.setTBTextColor(Color.WHITE);
        tvg22.startPlay(100, 999);
        tvg23.setTBTextColor(Color.WHITE);
        tvg23.startPlay(100, 999);
        tvg24.setTBTextColor(Color.WHITE);
        tvg24.startPlay(10, 99);
        tvg25.setTBTextColor(Color.WHITE);
        tvg25.startPlay(10, 99);
        tvg26.setTBTextColor(Color.WHITE);
        tvg26.startPlay(10, 99);
        tv_tm.setTBTextColor(Color.WHITE);
        tv_tm.startPlay(10000, 99999);
    }

    private void showKJ() {
        tv_tm.stopPlay(result[0], 0, 2);
        tvg1.stopPlay(result[1], 0, 2);
        tvg2.stopPlay(result[2], 0, 2);
        tvg3.stopPlay(result[3], 0, 2);
        tvg4.stopPlay(result[4], 0, 2);
        tvg5.stopPlay(result[5], 0, 2);
        tvg6.stopPlay(result[6], 0, 2);
        tvg7.stopPlay(result[7], 0, 2);
        tvg8.stopPlay(result[8], 0, 2);
        tvg9.stopPlay(result[9], 0, 2);
        tvg10.stopPlay(result[10], 0, 2);
        tvg11.stopPlay(result[11], 0, 2);
        tvg12.stopPlay(result[12], 0, 2);
        tvg13.stopPlay(result[13], 0, 2);
        tvg14.stopPlay(result[14], 0, 2);
        tvg15.stopPlay(result[15], 0, 2);
        tvg16.stopPlay(result[16], 0, 2);
        tvg17.stopPlay(result[17], 0, 2);
        tvg18.stopPlay(result[18], 0, 2);
        tvg19.stopPlay(result[19], 0, 2);
        tvg20.stopPlay(result[20], 0, 2);
        tvg21.stopPlay(result[21], 0, 2);
        tvg22.stopPlay(result[22], 0, 2);
        tvg23.stopPlay(result[23], 0, 2);
        tvg24.stopPlay(result[24], 0, 2);
        tvg25.stopPlay(result[25], 0, 2);
        tvg26.stopPlay(result[26], 0, 2);
    }

    public void doHNInfo() {
        Api_Cp.ins().gethn(lottryName, S_EXPECT, new JsonCallback<String[]>() {
            @Override
            public void onSuccess(int code, String msg, String[] results) {
                if (code == 0) {
                    if (results == null || results.length == 0) {
                        handler.sendEmptyMessageDelayed(200, 1000);
                        return;
                    }
                    if (fragment2 != null) {
                        fragment2.dismiss();
                    }
                    ll_result.setVisibility(View.VISIBLE);
                    spinner_simple.setVisibility(View.GONE);
                    tv_title.setText(Html.fromHtml("<font><big>" + getString(R.string.historyRecord) +
                            "</big><small>(" + getString(R.string.phase_number) + S_EXPECT + ")</small></font>"));

                    result = results;
                    tvg1.stopPlay(results[1], 0, 1);
                    tvg2.stopPlay(results[2], 1000, 1);
                    tvg3.stopPlay(results[3], 1000, 1);
                    tvg4.stopPlay(results[4], 2000, 1);
                    tvg5.stopPlay(results[5], 2000, 1);
                    tvg6.stopPlay(results[6], 2000, 1);
                    tvg7.stopPlay(results[7], 2000, 1);
                    tvg8.stopPlay(results[8], 2000, 1);
                    tvg9.stopPlay(results[9], 2000, 1);
                    tvg10.stopPlay(results[10], 3000, 1);
                    tvg11.stopPlay(results[11], 3000, 1);
                    tvg12.stopPlay(results[12], 3000, 1);
                    tvg13.stopPlay(results[13], 3000, 1);
                    tvg14.stopPlay(results[14], 4000, 1);
                    tvg15.stopPlay(results[15], 4000, 1);
                    tvg16.stopPlay(results[16], 4000, 1);
                    tvg17.stopPlay(results[17], 4000, 1);
                    tvg18.stopPlay(results[18], 4000, 1);
                    tvg19.stopPlay(results[19], 4000, 1);
                    tvg20.stopPlay(results[20], 5000, 1);
                    tvg21.stopPlay(results[21], 5000, 1);
                    tvg22.stopPlay(results[22], 5000, 1);
                    tvg23.stopPlay(results[23], 6000, 1);
                    tvg24.stopPlay(results[24], 6000, 1);
                    tvg25.stopPlay(results[25], 6000, 1);
                    tvg26.stopPlay(results[26], 6000, 1);
                    tv_tm.stopPlay(results[0], 9000, 1);
                }
            }
        });

    }

    @Override
    public void onGetLastGameResult(List<CpGameResultInfoVO> result) {

    }

    @Override
    public void onGetGamePeriodInfo(GamePeriodInfoVO cpGameResultInfoVO) {//获取期号信息
        if (cpGameResultInfoVO != null) {
            timelong = cpGameResultInfoVO.getTimelong();
            S_EXPECT = cpGameResultInfoVO.getExpect();
            CommonApp.LOTERNAME = cpGameResultInfoVO.getName();
            S_COUNTDOWN = cpGameResultInfoVO.getDown_time() * 1000L + SystemClock.elapsedRealtime();
            CommonApp.S_COUNTDOWNa = S_COUNTDOWN;
            if (flagc1) {
                tvCountDown.postDelayed(mRunnable, 0);
                flagc1 = false;
            }
        }
    }

    @Override
    public void onClick(View view) {
        long nowTime = System.currentTimeMillis();
        switch (view.getId()) {
            case R.id.rtvBet://投注
                if (!isGetResult) {
                    showToastTip(false, getString(R.string.closing));
                    return;
                }
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    mLastClickTime = nowTime;
                    List<MinuteTabItem> minuData = tabItems.get(0).getBetItems();//自定义数据
                    BetCartDataManager.getInstance().removeOddeFieldhn(minuData.get(0));//移除自定义数据总第一项中的第一项
                    List<MinuteTabItem> minuData2 = tabItems.get(1).getBetItems();
                    BetCartDataManager.getInstance().removeOddeFieldhn(minuData2.get(0));
                    List<MinuteTabItem> minuData3 = tabItems.get(2).getBetItems();
                    BetCartDataManager.getInstance().removeOddeFieldhn(minuData3.get(0));
                    switch (whichPage) {
                        case 11:
                        case 21:
                            StringBuilder shiwei = new StringBuilder();
                            StringBuilder gewei = new StringBuilder();
                            int shi = 0;
                            int ge = 0;
                            for (int i = 0; i < minuData.size(); i++) {
                                if (minuData.get(i).check) {
                                    shi++;
                                    shiwei.append(minuData.get(i).getTitle()).append(",");
                                    LogUtils.e("数字:" + i + "flag" + minuData.get(i).check + minuData.get(i).getTitle());
                                }
                                if (minuData.get(i).hncheck) {
                                    ge++;
                                    gewei.append(minuData.get(i).getChineseTitle()).append(",");
                                }
                            }
                            if (shi == 0 || ge == 0) {
                                showToastTip(false, getString(R.string.selectLeastOne));
                                return;
                            }
                            minuData.get(0).setHeNum(shiwei.substring(0, shiwei.length() - 1) + "|" + gewei.substring(0, gewei.length() - 1));
                            minuData.get(0).setBetCount(shi * ge);
                            BetCartDataManager.getInstance().addOddeField(minuData.get(0));//增加自定义数据总第一项中的第一项
                            break;
                        case 12:
                        case 22:
                            String numString = etShuru.getText().toString();
                            if (StringUtils.isEmpty(numString)) {
                                showToastTip(false, getString(R.string.selectLeastOne));
                                return;
                            }
                            if (numString.contains(",,")) {
                                showToastTip(false, "Cách thức nhập sai");
                                return;
                            }
                            if (numString.endsWith(",")) {
                                numString = numString.substring(0, numString.length() - 1);
                            }
                            if (numString.startsWith(",")) {
                                numString = numString.substring(1);
                            }
                            String[] numData = numString.split(",");
                            boolean isContainZero = false;
                            for (int i = 0; i < numData.length; i++) {
                                if (numData[i].length() > 2) {
                                    showToastTip(false, "Vui lòng nhập số từ 0-99");
                                    return;
                                }
                                if (numData[i].length() == 2 && numData[i].startsWith("0")) {
                                    isContainZero = true;
                                    numData[i] = numData[i].substring(1);
                                }
                            }
                            StringBuilder sb = new StringBuilder();
                            if (numData.length > 0 && isContainZero) {
                                for (int i = 0; i < numData.length; i++) {
                                    if (i < numData.length - 1) {
                                        sb.append(numData[i]).append(",");
                                    } else {
                                        sb.append(numData[i]);
                                    }
                                }
                                numString = sb.toString();
                            }

                            minuData2.get(0).setHeNum(numString);
                            minuData2.get(0).setBetCount(numData.length);
                            BetCartDataManager.getInstance().addOddeField(minuData2.get(0));
                            break;
                        case 13:
                        case 23:
                            StringBuilder numChecked = new StringBuilder();
                            int jishu = 0;
                            for (int i = 0; i < numDatas.size(); i++) {
                                if (numDatas.get(i).check) {
                                    jishu++;
                                    numChecked.append(numDatas.get(i).getNum()).append(",");
                                }
                            }
                            if (0 == jishu) {
                                showToastTip(false, getString(R.string.selectLeastOne));
                                return;
                            }
                            numChecked = new StringBuilder(numChecked.substring(0, numChecked.length() - 1));
                            minuData3.get(0).setHeNum(numChecked.toString());
                            minuData3.get(0).setBetCount(jishu);
                            BetCartDataManager.getInstance().addOddeField(minuData3.get(0));
                            break;
                    }
                    fragment2 = HNCartDialogFragment.newInstance(2, chipsVO, tabItems.get(0).getBetItems().get(0).mutiple, liveId);
                    if (!fragment2.isAdded()) {
                        aboveView.setVisibility(View.VISIBLE);
                        fragment2.show(requireActivity().getSupportFragmentManager(), HNCartDialogFragment.class.getSimpleName());
                        fragment2.setOnXzSucess(() -> notifyDataSetChanged());
                    }
                }
                break;
            case R.id.rtvRecharge://充值
                RechargeActivity.startActivity(requireActivity());
                break;
            case R.id.tvCode://筹码
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    // do something
                    mLastClickTime = nowTime;
                    ChipsDialogFragment frag = ChipsDialogFragment.newInstance(2);
                    if (!frag.isAdded()) {
                        frag.show(requireActivity().getSupportFragmentManager(), ChipsDialogFragment.class.getSimpleName());
                    }
                }
                break;
            case R.id.tvCountDownT://本期截止
                if (this.globalResult != null && this.globalResult.size() > 0) {
                    presenter.showPopwindow(requireActivity(), this.globalResult, tvCountDownT, false);
                }
                break;
            case R.id.ivWanfa:
                HelpDialogFragment dialogFragment = HelpDialogFragment.newInstance(chipsVO.getPlayMethod(), chipsVO.getChinese());
                if (!dialogFragment.isAdded()) {
                    dialogFragment.show(getActivity().getSupportFragmentManager(), HelpDialogFragment.class.getSimpleName());
                }
                break;
        }

    }

    @Override
    public void onDestroyView() {
        tvCountDown.removeCallbacks(mRunnable);
        if (S_EXPECT != null)
            S_EXPECT = null;
        BetCartDataManager.getInstance().clear();//清空投注兰
        whichPage = 0;
        CommonApp.LOTERNAME = "";
        AppIMManager.ins().removeMessageReceivedListener(HNDialogFragment.class);
        super.onDestroyView();
    }


    public static HNDialogFragment newInstance(ChipsVO chipsVO, long liveId) {
        HNDialogFragment fragment = new HNDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChipsVO.class.getSimpleName(), chipsVO);
        bundle.putLong("liveId", liveId);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 设置筹码值
     */
    public void setItemInputValue() {
        tvCodeBg.setImageResource(ChipsVO.chipsVOS().get(BetCartDataManager.getInstance().getChipsIndex()).resId);
        String chip = String.valueOf(ChipsVO.chipsVOS().get(BetCartDataManager.getInstance().getChipsIndex()).value);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvCodeBg.getLayoutParams();
        int value = DeviceUtils.dp2px(requireActivity(), 40);
        switch (chip.length()) {
            case 2:
                value = DeviceUtils.dp2px(requireActivity(), 48);
                break;
            case 3:
                value = DeviceUtils.dp2px(requireActivity(), 54);
                break;
            case 4:
                value = DeviceUtils.dp2px(requireActivity(), 64);
                break;
            case 5:
                value = DeviceUtils.dp2px(requireActivity(), 84);
                break;
        }

        layoutParams.height = value;
        layoutParams.width = value;
        tvCodeBg.setLayoutParams(layoutParams);
        tvCode.setText(chip);

        for (int i = 0; i < tabItems.size(); i++) {
            MinuteTabItem tabItem = tabItems.get(i);
            List<MinuteTabItem> betItems = tabItem.getBetItems();
            for (int j = 0; j < betItems.size(); j++) {
                betItems.get(j).betMoney = chip;
            }
        }
    }

    public void notifyVisibility() {
        aboveView.setVisibility(View.GONE);
    }

    public void notifyDataSetChanged() {
        rgShuru.clearCheck();
        etShuru.setText("");

        rgTab2.clearCheck();
        rgTab3.clearCheck();

        List<MinuteTabItem> betItems = tabItems.get(0).getBetItems();
        for (int i = 0; i < betItems.size(); i++) {
            betItems.get(i).check = false;
            betItems.get(i).hncheck = false;
        }

        ((RadioButton) rgTab1.getChildAt(0)).setChecked(true);

        adapter1.getData().clear();
        adapter1.getData().addAll(tabItems.get(0).getBetItems());
        adapter1.changedPage(whichPagea);
        adapter1.notifyDataSetChanged();
        adapter2.getData().clear();
        adapter2.getData().addAll(tabItems.get(0).getBetItems());
        adapter2.changedPage(whichPagea);
        adapter2.notifyDataSetChanged();


        for (int i = 0; i < numDatas.size(); i++) {
            numDatas.get(i).check = false;
        }

        adapter3.getData().clear();
        adapter3.getData().addAll(numDatas);
        adapter3.changedPage(whichPagea);
        adapter3.notifyDataSetChanged();
    }


    @Override
    public void onIMReceived(int protocol, String msg) {
        try {
            JSONObject message = new JSONObject(msg);
            if (protocol == Constant.MessageProtocol.PROTOCOL_BALANCE_CHANGE) { //12.金币变动消息
                LogUtils.e(getString(R.string.coinMessage) + msg);
                if (message.has("goldCoin")) {
                    String goldCoinNum = message.getString("goldCoin");
                    if ("0.00".equals(goldCoinNum))
                        goldCoinNum = "0";
                    if (tvGoldCoin != null)
                        tvGoldCoin.setText(RegexUtils.westMoney(Double.parseDouble(goldCoinNum)));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
