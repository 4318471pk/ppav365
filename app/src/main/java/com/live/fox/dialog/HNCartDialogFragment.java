package com.live.fox.dialog;

import static com.live.fox.dialog.HNDialogFragment.S_EXPECT;
import static com.live.fox.dialog.HNDialogFragment.whichPage;
import static com.live.fox.dialog.MinuteGameDialogFragment.S_COUNTDOWN;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.base.BetCartPresenter;
import com.live.fox.contract.BetCartContract;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@PresenterInject(BetCartPresenter.class)
public class HNCartDialogFragment extends MvpDialogFragment<BetCartPresenter> implements View.OnClickListener, BetCartContract.View {
    private TextView tvCartCount;
    private TextView tvName;
    private TextView tvNum;
    private TextView tvPelv;
    private TextView tvZhushu;
    private TextView tvGoldCoin;
    private TextView tvCancel;
    private TextView tvConfirm;

    private final static int MUTIPLE = 1;
    public final static int NOT_FORM_MINUTEGAMEDIALOGFRAGMENT = 1;
    private long mLastClickTime;
    private int enterForm;
    private String caculteTotal = "0";
    private int times = 1;
    private String lotteryName;
    private long liveId = 0;
    private long timelong;
    private ChipsVO chipsVO;
    private List<MinuteTabItem> items;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAdded()) {
                long formatTitle = S_COUNTDOWN - SystemClock.elapsedRealtime();
                long second = formatTitle / 1000;
                String text = null;
                if (tvCartCount == null) return;
                long temp = timelong * 60 - 5;
                if (second < 0) {
                    doGetCpInfo();
                    text = getString1(getString(R.string.phase_number) + S_EXPECT, false);
                } else {
                    text = getString1(getString(R.string.phase_number) + S_EXPECT, true);
                    if (second >= temp && second <= timelong * 60) {
                        text = getString1(getString(R.string.phase_number) + S_EXPECT, false);
                    }
                }

                tvCartCount.postDelayed(this, 1000);
                tvCartCount.setText(text);
            }
        }

        private String getString1(String res, boolean bool) {
            tvConfirm.setEnabled(bool);
            return res;
        }
    };

    private void doGetCpInfo() {
        HashMap<String, Object> paramsPeriod = BaseApi.getCommonParams();
        paramsPeriod.put("name", lotteryName);
        presenter.doGetGamePeriodInfo(paramsPeriod);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(requireActivity(), R.style.DialogDefault);
        presenter.initDialog(dialog);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_hn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Serializable serializable = getArguments().getSerializable(ChipsVO.class.getSimpleName());
        if (serializable != null) {
            chipsVO = (ChipsVO) serializable;
            lotteryName = chipsVO.getName();
            MinuteTabItem.lotteryTitle = chipsVO.getChinese();
        }
        tvCartCount = view.findViewById(R.id.tvCartCount);
        tvName = view.findViewById(R.id.tvName);
        tvNum = view.findViewById(R.id.tvNum);
        tvPelv = view.findViewById(R.id.tvPelv);
        tvZhushu = view.findViewById(R.id.tvZhushu);
        tvGoldCoin = view.findViewById(R.id.tvGoldCoin);
        tvCancel = view.findViewById(R.id.tvCancel);
        tvConfirm = view.findViewById(R.id.tvConfirm);
        tvConfirm.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        items = BetCartDataManager.getInstance().asList(false);
//        tvBetBalance.setText(RegexUtils.westMoney( SPManager.getUserInfo().getGoldCoin()));
        liveId = getArguments().getLong("liveId", 0);
        enterForm = getArguments().getInt("enterForm", NOT_FORM_MINUTEGAMEDIALOGFRAGMENT);
        times = getArguments().getInt("times", times);
        tvCartCount.setText(getString(R.string.phase_number) + S_EXPECT);
//        rgRatio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                String string = group.findViewById(checkedId).getTag().toString();
//                if (!TextUtils.isEmpty(string)) {
//                    updateOdds(Integer.parseInt(string));
//                }
//            }
//        });
        tvCartCount.post(mRunnable);
        updateCartSum();
//        selectRadioBtn();
    }

//    private void selectRadioBtn() {
//        if (enterForm == NOT_FORM_MINUTEGAMEDIALOGFRAGMENT) {
//            doGetCpInfo();
//            times = getArguments().getInt("times", times);
//            int childCount = rgRatio.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                View child = rgRatio.getChildAt(i);
//                String s = child.getTag().toString();
//                if (s.equals(String.valueOf(times))) {
//                    ((RadioButton) child).setChecked(true);
//                }
//            }
//        }
//    }

    private void updateCartSum() {
        caculteTotal = presenter.caculteTotalhn(items);
        items.get(0).setBetAmount(Integer.parseInt(caculteTotal));//总数据中的第一项
        tvNum.setText(items.get(0).getHeNum());
        tvGoldCoin.setText(Html.fromHtml(getString(R.string.tzje) + "<font color='colorEB4A81'>" +
                RegexUtils.westMoney(Double.parseDouble(caculteTotal)) + "</font>"));
        String info = String.format(getString(R.string.flowingWater_add_info), items.get(0).getBetCount());
        tvZhushu.setText(Html.fromHtml(info));
        switch (whichPage) {
            case 11:
            case 12:
            case 13:
                tvName.setText(getString(R.string.backpack_number_two));
                tvPelv.setText(Html.fromHtml(getString(R.string.odds) +
                        " <font color='colorEB4A81'>1</font>" + getString(R.string.stakes) +
                        " <font color='colorEB4A81'>3.68</font>"));
                break;
            case 21:
            case 22:
            case 23:
                tvName.setText(getString(R.string.special_topics));
                tvPelv.setText(Html.fromHtml(getString(R.string.odds) +
                        "<font color='colorEB4A81'>1</font>" + getString(R.string.stakes) +
                        "<font color='colorEB4A81'>99.5</font>"));
                break;
        }
    }

    public void doPushCart() {
        if (TextUtils.isEmpty(S_EXPECT)) {
            ToastUtils.showShort(getString(R.string.expectRetry));
            return;
        }

        HashMap<String, Object> params = BaseApi.getCommonParams();
        CpGameResultInfoVO cpGameResultInfoVO = new CpGameResultInfoVO();
        cpGameResultInfoVO.setMultiple(MUTIPLE);//固定数
        cpGameResultInfoVO.setExpect(S_EXPECT);
        ArrayList<CpGameResultInfoVO> vos = new ArrayList<>();
        vos.add(cpGameResultInfoVO);

        params.put("liveId", liveId);
        params.put("expect", vos);
        params.put("playNum", LotteryItem.addParameter(false));
        params.put("lotteryName", lotteryName);
        params.put("isHemai", 0);
        params.put("times", times);
        params.put("isStop", 0);
        params.put("betCount", items.get(0).getBetCount());
        params.put("betAmount", items.get(0).getBetAmount());
        presenter.doPushCart(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvConfirm:
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > 1500) {
                    mLastClickTime = nowTime;
                    double aDouble = Double.valueOf(caculteTotal);
                    if (aDouble > 1) {
                        doPushCart();
                    } else {
                        showToastTip(false, getString(R.string.moneyBiggerZero));
                    }
                }
                break;
            case R.id.tvCancel:
                dismiss();
                break;
        }
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

        if (NOT_FORM_MINUTEGAMEDIALOGFRAGMENT == enterForm) {
            BetCartDataManager.getInstance().clear();//清除追投数据
        }
        if (getActivity() != null) {
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(HNDialogFragment.class.getSimpleName());
            if (fragment != null) {
                ((HNDialogFragment) fragment).notifyVisibility();//清楚数据
            }
        }
    }

    @Override
    public void onDestroyView() {
        tvCartCount.removeCallbacks(mRunnable);
        MinuteTabItem.lotteryTitle = null;
        BetCartDataManager.getInstance().clearLink();
        super.onDestroyView();
    }


    public static HNCartDialogFragment newInstance(int enterForm, ChipsVO msg, int times, long liveId) {
        HNCartDialogFragment fragment = new HNCartDialogFragment();
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
            S_COUNTDOWN = result.getDown_time() * 1000 + SystemClock.elapsedRealtime();
            S_EXPECT = result.getExpect();
            timelong = result.getTimelong();
//            updatePeriod();
            if (TextUtils.isEmpty(MinuteTabItem.lotteryTitle)) {
                MinuteTabItem.lotteryTitle = result.getName();
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

    public interface OnXzSucess {
        void onSuccess();
    }
}
