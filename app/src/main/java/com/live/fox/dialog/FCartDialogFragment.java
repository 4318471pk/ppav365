package com.live.fox.dialog;

import static com.live.fox.common.CommonLiveControlFragment.TIME_INTERVAL;
import static com.live.fox.dialog.FGameDialogFragment.S_COUNTDOWN;
import static com.live.fox.dialog.FGameDialogFragment.S_EXPECT;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flyco.roundview.RoundTextView;
import com.live.fox.R;
import com.live.fox.adapter.AgentAdapter;
import com.live.fox.adapter.BetCartAdapter;
import com.live.fox.base.BetCartPresenter;
import com.live.fox.contract.BetCartContract;
import com.live.fox.entity.cp.CpOutputFactory;
import com.live.fox.entity.response.ChipsVO;
import com.live.fox.entity.response.CpGameResultInfoVO;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.entity.response.LotteryItem;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.svga.BetCartDataManager;
import com.live.fox.mvp.MvpDialogFragment;
import com.live.fox.mvp.PresenterInject;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.AppUserManger;
import com.live.fox.view.MaxHeightRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 鱼虾蟹
 * 下注确认
 */
@PresenterInject(BetCartPresenter.class)
public class FCartDialogFragment extends MvpDialogFragment<BetCartPresenter> implements
        View.OnClickListener, BetCartContract.View, BetInputDialogFragment.BetInputChangeListener {
    private MaxHeightRecyclerView rvCart;
    private TextView tvCartCount;
    private RadioGroup rgRatio;
    private TextView tvBetNum;
    private TextView tvBetTotalMoney;
    private TextView tvBetBalance;
    private BetCartAdapter mBetCartAdapter;
    private final static int MUTIPLE = 1;
    public final static int NOT_FORM_MINUTEGAMEDIALOGFRAGMENT = 1;
    private RoundTextView rtvBet;
    private long mLastClickTime;
    private int enterForm;
    private String caculteTotal = "0";
    private int times = 1;
    private String lotteryName;
    private long liveId = 0;
    private String realName;
    private long timelong;
    private ChipsVO chipsVO;

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAdded()) {
                long formatTitle = S_COUNTDOWN - SystemClock.elapsedRealtime();
                long second = formatTitle / 1000;
                String text;
                if (tvCartCount == null) return;
                if (timelong == 1) {
                    if (second < 0) {
                        text = getString1(requireContext().getString(R.string.closing), false);
                        doGetCpInfo();
                    } else {
                        text = getString1(realName + requireContext().getString(R.string.phase_number) + S_EXPECT + " " + requireContext().getString(R.string.closing) + " " + ":00" + ":" + second, true);
                        if (second >= 55 && second <= 59) {
                            text = getString1(realName + requireContext().getString(R.string.phase_number) + S_EXPECT + " " + requireContext().getString(R.string.closing), false);
                        }
                    }
                } else {
                    long m = second / 60;
                    long sec = second % 60;
                    long temp = timelong * 60 - 5;
                    if (second < 0) {
                        doGetCpInfo();
                        text = getString1(realName + requireContext().getString(R.string.phase_number) + S_EXPECT + " " + requireContext().getString(R.string.closing), false);
                    } else {
                        text = getString1(String.valueOf(realName + requireContext().getString(R.string.phase_number) + S_EXPECT + " " +
                                requireContext().getString(R.string.closing) + ":" + m + ":" + sec), true);
                        if (second >= temp && second <= timelong * 60) {
                            text = getString1(realName + requireContext().getString(R.string.phase_number) + S_EXPECT + " " + requireContext().getString(R.string.closing), false);
                        }
                    }
                }
                tvCartCount.postDelayed(this, 1000);
                tvCartCount.setText(text);
            }
        }

        private String getString1(String res, boolean bool) {
            rtvBet.setEnabled(bool);
            return res;
        }
    };
    private boolean isMix;

    private void doGetCpInfo() {
        HashMap<String, Object> paramsPeriod = BaseApi.getCommonParams();
        paramsPeriod.put("name", lotteryName);
        presenter.doGetGamePeriodInfo(paramsPeriod);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        presenter.initDialog(dialog);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        Serializable serializable = getArguments().getSerializable(ChipsVO.class.getSimpleName());
        if (serializable != null) {
            chipsVO = (ChipsVO) serializable;
            lotteryName = chipsVO.getName();
            realName = chipsVO.getChinese();
            MinuteTabItem.lotteryTitle = realName;
            isMix = CpOutputFactory.TYPE_CP_JS11.equals(chipsVO.getName()) || CpOutputFactory.TYPE_CP_JX11.equals(chipsVO.getName());
        }
        rvCart = view.findViewById(R.id.betting_confirm_recycler_view);
        tvCartCount = view.findViewById(R.id.tvCartCount);
        rgRatio = view.findViewById(R.id.rgRatio);
        rtvBet = view.findViewById(R.id.rtvBet);
        rtvBet.setOnClickListener(this);
        tvBetNum = view.findViewById(R.id.tvBetNum);
        tvBetTotalMoney = view.findViewById(R.id.tvBetTotalMoney);
        tvBetBalance = view.findViewById(R.id.tvBetBalance);
        List<MinuteTabItem> items = BetCartDataManager.getInstance().asList(isMix);
        mBetCartAdapter = new BetCartAdapter(items); //下注选择数据
        rvCart.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rvCart.addItemDecoration(new AgentAdapter.SpaceItemDecoration());
        if (rvCart.getItemAnimator() != null) {
            rvCart.getItemAnimator().setRemoveDuration(0);
        }
        rvCart.setAdapter(mBetCartAdapter);
        tvBetBalance.setText(RegexUtils.westMoney(AppUserManger.getUserInfo().getGoldCoin()));
        liveId = getArguments().getLong("liveId", 0);
        enterForm = getArguments().getInt("enterForm", NOT_FORM_MINUTEGAMEDIALOGFRAGMENT);
        mBetCartAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            if (view1.getId() == R.id.rrl) {//item
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    mLastClickTime = nowTime;
                    String key = isMix ? mBetCartAdapter.getData().get(position).type : mBetCartAdapter.getData().get(position).getId();
                    BetInputDialogFragment betInputDialogFragment = BetInputDialogFragment.newInstance(key, position, isMix);
                    betInputDialogFragment.setInputChangeListener(this);
                    betInputDialogFragment.show(getChildFragmentManager(), "change bet dialog");
                }
            } else if (view1.getId() == R.id.ivTrash) {//删除
                MinuteTabItem item = mBetCartAdapter.getData().get(position);
                MinuteTabItem it = isMix ? BetCartDataManager.getInstance().getLink().remove(item.type) : BetCartDataManager.getInstance().removeOddeField(item);
                if (isMix) {
                    if (getActivity() != null) {
                        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(FGameDialogFragment.class.getSimpleName());
                        if (fragment != null) {
                            assert it != null;
                            ((FGameDialogFragment) fragment).removeItem(it.type);
                        }
                    }
                }
                int indexOf = mBetCartAdapter.getData().indexOf(it);
                mBetCartAdapter.getData().remove(it);
                mBetCartAdapter.notifyItemRemoved(indexOf);
                updateCartSum();
            }
        });

        setFollowUp();
        tvCartCount.post(mRunnable);
        updateCartSum();
        selectRadioBtn();
        ((RadioButton) rgRatio.getChildAt(BetCartDataManager.betGameIndex)).setChecked(true);
    }

    /**
     * 跟投倍数
     */
    private void setFollowUp() {
        rgRatio.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    BetCartDataManager.betGameIndex = i;
                    break;
                }
            }
            String string = group.findViewById(checkedId).getTag().toString();
            if (!TextUtils.isEmpty(string)) {
                updateOdds(Integer.parseInt(string));
            }
        });
    }

    private void selectRadioBtn() {
        if (enterForm == NOT_FORM_MINUTEGAMEDIALOGFRAGMENT) {
            doGetCpInfo();
            times = getArguments().getInt("times", times);
            int childCount = rgRatio.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = rgRatio.getChildAt(i);
                String s = child.getTag().toString();
                if (s.equals(String.valueOf(times))) {
                    ((RadioButton) child).setChecked(true);
                }
            }
        }
    }

    public void setItemInputValue(int position) {
        mBetCartAdapter.notifyItemChanged(position);
        updateCartSum();
    }

    private void updateCartSum() {
        if (mBetCartAdapter.getData().isEmpty()) {
            dismiss();
            return;
        }
        String caculteTotal = presenter.caculteTotal(mBetCartAdapter.getData());
        this.caculteTotal = caculteTotal;
        tvBetTotalMoney.setText(RegexUtils.westMoney(Long.parseLong(caculteTotal)));
        tvBetNum.setText(String.valueOf(mBetCartAdapter.getData().size()));
    }

    public void doPushCart() {
        if (TextUtils.isEmpty(S_EXPECT)) {
            ToastUtils.showShort(getString(R.string.expectRetry));
            return;
        }
        for (int i = 0; i < rgRatio.getChildCount(); i++) {
            if (((RadioButton) rgRatio.getChildAt(i)).isChecked()) {
                BetCartDataManager.betGameIndex = i;
                break;
            }
        }

        HashMap<String, Object> params = BaseApi.getCommonParams();
        CpGameResultInfoVO cpGameResultInfoVO = new CpGameResultInfoVO();
        cpGameResultInfoVO.setMultiple(MUTIPLE);
        cpGameResultInfoVO.setExpect(S_EXPECT);
        ArrayList<CpGameResultInfoVO> vos = new ArrayList<>();
        vos.add(cpGameResultInfoVO);

        params.put("liveId", liveId);
        params.put("expect", vos);
        params.put("playNum", LotteryItem.addParameter(isMix));
        params.put("lotteryName", lotteryName);
        params.put("isHemai", 0);
        params.put("times", times);
        params.put("isStop", 0);
        presenter.doPushCart(params);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rtvBet) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > 1500) {
                mLastClickTime = nowTime;
                double aDouble = Double.parseDouble(caculteTotal);
                if (aDouble > 1) {
                    doPushCart();
                } else {
                    showToastTip(false, getString(R.string.moneyBiggerZero));
                }
            }
        }
    }

    void updateOdds(int odds) {
        List<MinuteTabItem> data = mBetCartAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            MinuteTabItem minuteTabItem = data.get(i);
            minuteTabItem.mutiple = odds;
            times = odds;
        }
        mBetCartAdapter.notifyDataSetChanged();
        updateCartSum();
    }

    @Override
    public void onPushCart() {
        if (xzMsg != null) xzMsg.onSuccess();
        showToastTip(true, getString(R.string.bet_success));
        dismiss();
        BetCartDataManager.getInstance().clear();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (NOT_FORM_MINUTEGAMEDIALOGFRAGMENT == enterForm)
            BetCartDataManager.getInstance().clear();
        Fragment ffragment = requireActivity().getSupportFragmentManager().findFragmentByTag(FGameDialogFragment.class.getSimpleName());
        if (ffragment != null) {
            ((FGameDialogFragment) ffragment).notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        tvCartCount.removeCallbacks(mRunnable);
        MinuteTabItem.lotteryTitle = null;
        BetCartDataManager.getInstance().clearLink();
        super.onDestroyView();
    }


    public static FCartDialogFragment newInstance(int enterForm, ChipsVO msg, int times, long liveId) {
        FCartDialogFragment fragment = new FCartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChipsVO.class.getSimpleName(), msg);
        bundle.putInt("enterForm", enterForm);
        bundle.putInt("times", times);
        bundle.putLong("liveId", liveId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onGetGamePeriodInfo(GamePeriodInfoVO result) {
        if (result != null) {
            S_COUNTDOWN = result.getDown_time() * 1000L + SystemClock.elapsedRealtime();
            S_EXPECT = result.getExpect();
            timelong = result.getTimelong();
            if (TextUtils.isEmpty(MinuteTabItem.lotteryTitle)) {
                MinuteTabItem.lotteryTitle = result.getName();
                realName = result.getName();
            }
        }
    }

    public void updatePeriod() {
        showToastTip(true, getString(R.string.updateTo) + S_EXPECT);
    }


    OnXzSucess xzMsg;

    public void setOnXzSucess(OnXzSucess xzMsg) {
        this.xzMsg = xzMsg;
    }

    @Override
    public void onChangeListener(int position) {
        setItemInputValue(position);
    }

    public interface OnXzSucess {
        void onSuccess();
    }
}
