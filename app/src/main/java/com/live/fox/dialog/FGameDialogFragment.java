package com.live.fox.dialog;

import static com.live.fox.common.CommonLiveControlFragment.TIME_INTERVAL;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_FF;

import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.roundview.RoundTextView;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.FMinuteAdapter;
import com.live.fox.adapter.PrizeShowAdapter;
import com.live.fox.base.MinuteGamePresenter;
import com.live.fox.common.CommonApp;
import com.live.fox.contract.MinuteGameContract;
import com.live.fox.entity.cp.CpFactory;
import com.live.fox.entity.cp.CpOutputFactory;
import com.live.fox.entity.response.ChipsVO;
import com.live.fox.entity.response.CpGameResultInfoVO;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.entity.response.LotteryCpVO;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.manager.DataCenter;
import com.live.fox.svga.BetCartDataManager;
import com.live.fox.mvp.MvpDialogFragment;
import com.live.fox.mvp.PresenterInject;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.device.DeviceUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@PresenterInject(MinuteGamePresenter.class)
public class FGameDialogFragment extends MvpDialogFragment<MinuteGamePresenter> implements View.OnClickListener,
        MinuteGameContract.View, AppIMManager.OnMessageReceivedListener {

    private RecyclerView rvOpenPrize;
    private RadioGroup rgTab;
    private View view1, view2, view3;
    private RecyclerView rvCheck;
    private TextView tvCountDown;
    private TextView tvGameTitle;
    private TextView tvOpen;
    private TextView tvGoldCoin;
    private PrizeShowAdapter mPrizeShowAdapter;
    private FMinuteAdapter mFMinuteAdapter;
    private TextView tvCode;
    private ImageView tvCodeBg;
    private RoundTextView rtvBet;
    private String lotteryName;
    private int count = 2;
    private ImageView ivCp;
    private ImageView ivWanfa;
    private List<MinuteTabItem> tabItems;
    private List<CpGameResultInfoVO> globalResult;
    long mLastClickTime;
    long liveId;
    private ChipsVO chipsVO;
    private long timelong;//单位分钟
    List<String> asList = new ArrayList<>();
    public static long S_COUNTDOWN = 60;
    public static String S_EXPECT = null;
    boolean flagc1 = true;

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAdded()) {
                long formatTitle = S_COUNTDOWN - SystemClock.elapsedRealtime();
                long second = formatTitle / 1000;
                String text = null;
                if (tvCountDown == null) return;
                if (timelong == 1) {//一分彩游戏
                    if (second > 0) {
                        if (second >= 55 && second <= 59) {
                            text = getString(R.string.closing);
                            rtvBet.setEnabled(false);
                        } else {
                            text = String.valueOf("00:" + second);
                            rtvBet.setEnabled(true);
                        }
                    } else {
                        text = getString(R.string.closing);
                        rtvBet.setEnabled(false);
                        doGetCpInfo();
                        tvOpen.postDelayed(mRunnable3, 5000);
                    }
                } else if (timelong > 1) {//1分钟以上 一期
                    long m = second / 60;
                    long sec = second % 60;
                    long temp = timelong * 60 - 5;
                    if (second > 0) {
                        if (second >= temp && second <= timelong * 60) {
                            text = getString(R.string.closing);
                            rtvBet.setEnabled(false);
                        } else {
                            text = String.valueOf(m + ":" + sec);
                            rtvBet.setEnabled(true);
                        }
                    } else {
                        text = getString(R.string.closing);
                        rtvBet.setEnabled(false);
                        doGetCpInfo();
                        tvOpen.postDelayed(mRunnable3, 5000);
                    }
                }
                tvCountDown.postDelayed(this, 1000);
                tvCountDown.setText(text);
            }
        }
    };

    Runnable mRunnable3 = this::doGetCpResultInfo;
    private MinuteTabItem curTabItem;
    private boolean isMix;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        return presenter.initDialog(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one_minutegame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvOpenPrize = view.findViewById(R.id.rvOpenPrize);
        rgTab = view.findViewById(R.id.rgTab);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        rvCheck = view.findViewById(R.id.rvCheck);
        tvCountDown = view.findViewById(R.id.tvCountDown);
        tvOpen = view.findViewById(R.id.tvOpen);
        ivWanfa = view.findViewById(R.id.ivWanfa);
        tvOpen.setOnClickListener(this);
        ivWanfa.setOnClickListener(this);
        tvGoldCoin = view.findViewById(R.id.tvGoldCoin);
        tvGameTitle = view.findViewById(R.id.tvGameTitle);
        tvCode = view.findViewById(R.id.tvCode);
        tvCodeBg = view.findViewById(R.id.tvCode_bg);
        tvCode.setOnClickListener(this);
        tvCodeBg.setOnClickListener(this);
        ivCp = view.findViewById(R.id.ivCp);
        rtvBet = view.findViewById(R.id.rtvBet);
        rtvBet.setOnClickListener(this);
        view.findViewById(R.id.rtvRecharge).setOnClickListener(this);
        Serializable serializable = getArguments().getSerializable(ChipsVO.class.getSimpleName());
        if (serializable != null) {
            chipsVO = (ChipsVO) serializable;
            lotteryName = chipsVO.getName();
            if (TextUtils.isEmpty(chipsVO.getIcon())) {
                ivCp.setImageResource(chipsVO.resId);
            } else {
                GlideUtils.loadImage(getActivity(), chipsVO.getIcon(), ivCp);
            }
            MinuteTabItem.lotteryTitle = chipsVO.getChinese();
            tvGameTitle.setText(chipsVO.getChinese());
            isMix = CpOutputFactory.TYPE_CP_JS11.equals(chipsVO.getName()) || CpOutputFactory.TYPE_CP_JX11.equals(chipsVO.getName());
        }

        mPrizeShowAdapter = new PrizeShowAdapter(new ArrayList<>(), lotteryName);
        mFMinuteAdapter = new FMinuteAdapter(new ArrayList<>());
        rvOpenPrize.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvOpenPrize.setAdapter(mPrizeShowAdapter);
        rvCheck.setAdapter(mFMinuteAdapter);
        String goldCoinNum = DataCenter.getInstance().getUserInfo().getUser().getGoldCoin() + "";
        tvGoldCoin.setText(RegexUtils.westMoney(Double.parseDouble(goldCoinNum)));
        liveId = getArguments().getLong("liveId", 0);
        CpFactory factory = new CpFactory();
        CpOutputFactory cpOutputFactory = factory.createFactory(lotteryName);//自定义数据
        LotteryCpVO lotteryCpVO = cpOutputFactory.getCpVoByType(requireActivity());
        tabItems = lotteryCpVO.getTabItems();
        rgTab.setWeightSum(tabItems.size());
        for (int i = 0; i < tabItems.size() * 2 - 1; i++) {
            if (1 == i) {
                view1.setVisibility(View.VISIBLE);
            } else if (3 == i) {
                view2.setVisibility(View.VISIBLE);
            } else if (5 == i) {
                view3.setVisibility(View.VISIBLE);
            } else {
                AppCompatRadioButton childAt = (AppCompatRadioButton) rgTab.getChildAt(i);
                childAt.setVisibility(View.VISIBLE);
                childAt.setText(tabItems.get(i / 2).getTabTitle());
            }
        }

        GridLayoutManager grid = new GridLayoutManager(requireActivity(), 3);
        rvCheck.setLayoutManager(grid);
        FMinuteAdapter.RecyclerSpace recyclerSpace =
                new FMinuteAdapter.RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 10));
        rvCheck.addItemDecoration(recyclerSpace);
        rgTab.setOnCheckedChangeListener((group, checkedId) -> {
            mFMinuteAdapter.getData().clear();

            int anInt = Integer.parseInt(group.findViewById(checkedId).getTag().toString());
            curTabItem = tabItems.get(anInt);
            for (MinuteTabItem tabItem : curTabItem.getBetItems()) {
                tabItem.setType_text_show(curTabItem.getTabTitle());
            }
            mFMinuteAdapter.getData().addAll(curTabItem.getBetItems());
            grid.setSpanCount(curTabItem.getSpanCount());
            recyclerSpace.space = curTabItem.getSpace();
            mFMinuteAdapter.setDataNotifacation(anInt);
        });

        mFMinuteAdapter.setOnItemChildClickListener((adapter, view4, position) -> {
            MinuteTabItem it = mFMinuteAdapter.getData().get(position);
            List<MinuteTabItem> betItems = curTabItem.getBetItems();
            it.check = !it.check;
            if (curTabItem.getLimit() != null) {
                int selectCount = 0;
                for (int j = 0; j < betItems.size(); j++) {
                    if (betItems.get(j).check)
                        selectCount++;

                }
                if (selectCount > curTabItem.getLimit()) {
                    it.check = false;
                    return;
                }
            }

            if (it.check) {
                BetCartDataManager.getInstance().addOddeField(it);
            } else {
                BetCartDataManager.getInstance().removeOddeField(it);
            }
            mFMinuteAdapter.notifyItemChanged(position);
        });

        ((AppCompatRadioButton) rgTab.getChildAt(0)).setChecked(true);
        setItemInputValue();
        doGetCpInfo();
        doGetCpResultInfo();

        AppIMManager.ins().addMessageListener(FGameDialogFragment.class, this);
    }

    public void doGetCpInfo() {
        HashMap<String, Object> paramsPeriod = BaseApi.getCommonParams();
        paramsPeriod.put("name", lotteryName);
        presenter.doGetGamePeriodInfo(paramsPeriod);
    }

    public void doGetCpResultInfo() {
        HashMap<String, Object> paramsPeriod = BaseApi.getCommonParams();
        paramsPeriod.put("name", lotteryName);
        presenter.doGetLastGameResult(paramsPeriod);
    }

    @Override
    public void onGetLastGameResult(List<CpGameResultInfoVO> result) {
        if (this.globalResult != null && this.globalResult.size() > 0) {
            if (result != null && result.size() > 0) {
                if (this.globalResult.get(0).getCode().equals(result.get(0).getCode())) {
                    if (count > 0) {
                        if (tvOpen != null) tvOpen.postDelayed(mRunnable3, 4000);
                        count--;
                    }
                } else {
                    normalReturn(result);
                }
            }
        } else {
            normalReturn(result);
        }
    }

    private void normalReturn(List<CpGameResultInfoVO> result) {
        count = 2;
        if (result != null && result.size() > 0) {
            this.globalResult = result;
            CpGameResultInfoVO resultInfoVO = result.get(0);
            if (!TextUtils.isEmpty(resultInfoVO.getCode())) {
                String[] split = resultInfoVO.getCode().split(",");
                asList.clear();
                asList.addAll(Arrays.asList(split));
                mPrizeShowAdapter.getData().clear();
                mPrizeShowAdapter.getData().addAll(asList);
                mPrizeShowAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onGetGamePeriodInfo(GamePeriodInfoVO cpGameResultInfoVO) {
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

            if (getActivity() != null) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = manager.findFragmentByTag(FCartDialogFragment.class.getSimpleName());
                if (fragment != null) ((FCartDialogFragment) fragment).updatePeriod();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rtvBet://投注
                if (BetCartDataManager.getInstance().size() == 0) {
                    showToastTip(false, getString(R.string.selectLeastOne));
                } else {
                    if (isMix) {
                        for (int i = 0; i < tabItems.size(); i++) {
                            MinuteTabItem tabItem = tabItems.get(i);
                            List<MinuteTabItem> betItems = tabItem.getBetItems();
                            int selectCount = 0;
                            for (int j = 0; j < betItems.size(); j++) {
                                if (betItems.get(j).check)
                                    selectCount++;
                            }
                            if (selectCount != 0 && selectCount != tabItem.getLimit()) {
                                showToastTip(false, tabItem.getTabTitle() + getString(R.string.betsChosen) + tabItem.getLimit() + getString(R.string.an));
                                return;
                            }
                        }
                    }
                    long nowTime = System.currentTimeMillis();
                    if (nowTime - mLastClickTime > TIME_INTERVAL) {
                        mLastClickTime = nowTime;
                        FCartDialogFragment fragment = FCartDialogFragment.newInstance(2, chipsVO, 1, liveId);
                        if (!fragment.isAdded()) {
                            fragment.show(requireActivity().getSupportFragmentManager(), FCartDialogFragment.class.getSimpleName());
                            fragment.setOnXzSucess(() -> {
                            });
                        }
                    }
                }
                break;
            case R.id.rtvRecharge://充值
                RechargeActivity.startActivity(getActivity());
                break;

            case R.id.tvCode://筹码
            case R.id.tvCode_bg://筹码
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    // do something
                    mLastClickTime = nowTime;
                    ChipsDialogFragment frag = ChipsDialogFragment.newInstance(3);
                    if (!frag.isAdded()) {
                        frag.show(requireActivity().getSupportFragmentManager(), ChipsDialogFragment.class.getSimpleName());
                    }
                }
                break;

            case R.id.tvOpen:
                if (TYPE_CP_FF.equals(chipsVO.getName())) {
                    YXXDialogFragment fastDialogFragment = YXXDialogFragment.newInstance(chipsVO.getName(), chipsVO.getChinese());
                    if (!fastDialogFragment.isAdded()) {
                        fastDialogFragment.show(requireActivity().getSupportFragmentManager(), YXXDialogFragment.class.getSimpleName());
                    }
                }
                break;

            case R.id.ivWanfa:
                if (!StringUtils.isEmpty(chipsVO.getPlayMethod())) {
                    HelpDialogFragment dialogFragment = HelpDialogFragment.newInstance(chipsVO.getPlayMethod(), chipsVO.getChinese());
                    if (!dialogFragment.isAdded()) {
                        dialogFragment.show(requireActivity().getSupportFragmentManager(), HelpDialogFragment.class.getSimpleName());
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        tvCountDown.removeCallbacks(mRunnable);
        tvOpen.removeCallbacks(mRunnable3);
        if (S_EXPECT != null)
            S_EXPECT = null;
        isMix = false;
        CommonApp.LOTERNAME = "";
        BetCartDataManager.getInstance().clear();//清空投注兰
        AppIMManager.ins().removeMessageReceivedListener(FGameDialogFragment.class);
        super.onDestroyView();
    }


    public static FGameDialogFragment newInstance(ChipsVO chipsVO, long liveId) {
        FGameDialogFragment fragment = new FGameDialogFragment();
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

    public void notifyDataSetChanged() {
        mFMinuteAdapter.notifyDataSetChanged();
    }

    public void removeItem(String typeKey) {
        if (isMix) for (int i = 0; i < tabItems.size(); i++) {
            MinuteTabItem tab = tabItems.get(i);
            if (typeKey.equals(tab.type)) {
                List<MinuteTabItem> betItems = tab.getBetItems();
                for (int j = 0; j < betItems.size(); j++) {
                    MinuteTabItem item = betItems.get(j);
                    if (item.check) {
                        item.check = false;
                        BetCartDataManager.getInstance().removeOddeField(item);
                    }
                }
            }
        }
    }

    @Override
    public void onIMReceived(int protocol, String msg) {
        try {
            JSONObject message = new JSONObject(msg);
            if (protocol == Constant.MessageProtocol.PROTOCOL_BALANCE_CHANGE) { //12.金币变动消息
                LogUtils.e(getString(R.string.coinMessage) + msg);
                if (message.has("goldCoin")) {
                    String goldCoinNum = message.getString("goldCoin");
                    if (tvGoldCoin != null)
                        tvGoldCoin.setText(RegexUtils.westMoney(Double.parseDouble(goldCoinNum)));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
