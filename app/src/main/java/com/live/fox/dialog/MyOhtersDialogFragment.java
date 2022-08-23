package com.live.fox.dialog;

import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_FF;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.entity.MyTZResult;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.TimeUtils;

import java.util.List;


public class MyOhtersDialogFragment extends DialogFragment {
    private MyTZResult myTZResult;
    private TextView tvTitle;
    private TextView tvNickname;
    private TextView tvQihao;
    private TextView tvDuzhu;
    private TextView tvTouzudian;
    private TextView tvTouzusj;
    private TextView tvTouzuxq;
    private TextView numa;
    private TextView numb;
    private TextView numc;
    private TextView numd;
    private TextView nume;
    private TextView numf;
    private TextView numg;
    private TextView numh;
    private LinearLayout llPk;
    private LinearLayout llAllResult;
    private TextView numi;
    private TextView numj;
    private TextView tvZhuangtai;
    private TextView tvJiangjin;
    private TextView tvMethod;
    private TextView tvJiangliqi;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_my_others);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0.05f);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_others, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        myTZResult = (MyTZResult) bundle.getSerializable("myTZResult");
        initView(view);
    }

    public static MyOhtersDialogFragment newInstance(MyTZResult myTZResult) {
        MyOhtersDialogFragment fragment = new MyOhtersDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("myTZResult", myTZResult);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
        tvQihao = (TextView) view.findViewById(R.id.tv_qihao);
        tvDuzhu = (TextView) view.findViewById(R.id.tv_duzhu);
        tvTouzudian = (TextView) view.findViewById(R.id.tv_touzudian);
        tvTouzusj = (TextView) view.findViewById(R.id.tv_touzusj);
        tvTouzuxq = (TextView) view.findViewById(R.id.tv_touzuxq);
        llAllResult = (LinearLayout) view.findViewById(R.id.llAllResult);
        numa = (TextView) view.findViewById(R.id.numpa);
        numb = (TextView) view.findViewById(R.id.numpb);
        numc = (TextView) view.findViewById(R.id.numpc);
        numd = (TextView) view.findViewById(R.id.numpd);
        nume = (TextView) view.findViewById(R.id.numpe);
        numf = (TextView) view.findViewById(R.id.numpf);
        numg = (TextView) view.findViewById(R.id.numpg);
        numh = (TextView) view.findViewById(R.id.numph);
        llPk = (LinearLayout) view.findViewById(R.id.ll_pk);
        numi = (TextView) view.findViewById(R.id.numpi);
        numj = (TextView) view.findViewById(R.id.numpj);
        List<Integer> lotteryResult5d = myTZResult.getResultList();
        if (lotteryResult5d != null && lotteryResult5d.size() != 0) {
            if ("txssc".equals(myTZResult.getLotteryName())) {
                for (int i = 0; i < lotteryResult5d.size(); i++) {
                    switch (i) {
                        case 0:
                            numa.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 1:
                            numb.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 2:
                            numc.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 3:
                            numd.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 4:
                            nume.setText(lotteryResult5d.get(i) + "");
                            break;
                    }
                }
                numf.setVisibility(View.GONE);
                numg.setVisibility(View.GONE);
                numh.setVisibility(View.GONE);
                llPk.setVisibility(View.GONE);
            } else if ("jsks".equals(myTZResult.getLotteryName())) {
                for (int i = 0; i < lotteryResult5d.size(); i++) {
                    switch (i) {
                        case 0:
                            choicePis(numa, lotteryResult5d.get(i));
                            break;
                        case 1:
                            choicePis(numb, lotteryResult5d.get(i));
                            break;
                        case 2:
                            choicePis(numc, lotteryResult5d.get(i));
                            break;
                    }
                }
                numd.setVisibility(View.GONE);
                nume.setVisibility(View.GONE);
                numf.setVisibility(View.GONE);
                numg.setVisibility(View.GONE);
                numh.setVisibility(View.GONE);
                llPk.setVisibility(View.GONE);
            } else if (TYPE_CP_FF.equals(myTZResult.getLotteryName())) {
                for (int i = 0; i < lotteryResult5d.size(); i++) {
                    switch (i) {
                        case 0:
                            choicePisYxx(numa, lotteryResult5d.get(i));
                            break;
                        case 1:
                            choicePisYxx(numb, lotteryResult5d.get(i));
                            break;
                        case 2:
                            choicePisYxx(numc, lotteryResult5d.get(i));
                            break;
                    }
                }
                numd.setVisibility(View.GONE);
                nume.setVisibility(View.GONE);
                numf.setVisibility(View.GONE);
                numg.setVisibility(View.GONE);
                numh.setVisibility(View.GONE);
                llPk.setVisibility(View.GONE);
            } else if ("pk10".equals(myTZResult.getLotteryName())) {
                llPk.setVisibility(View.VISIBLE);
                for (int i = 0; i < lotteryResult5d.size(); i++) {
                    switch (i) {
                        case 0:
                            numa.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 1:
                            numb.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 2:
                            numc.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 3:
                            numd.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 4:
                            nume.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 5:
                            numf.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 6:
                            numg.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 7:
                            numh.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 8:
                            numi.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 9:
                            numj.setText(lotteryResult5d.get(i) + "");
                            break;
                    }
                }
            } else if ("yflhc".equals(myTZResult.getLotteryName())) {
                for (int i = 0; i < lotteryResult5d.size() - 1; i++) {
                    switch (i) {
                        case 0:
                            numa.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 1:
                            numb.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 2:
                            numc.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 3:
                            numd.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 4:
                            nume.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 5:
                            numf.setText(lotteryResult5d.get(i) + "");
                            break;
                        case 6:
                            numh.setText(lotteryResult5d.get(i) + "");
                            if (3 == lotteryResult5d.get(7)) {
                                numh.setBackgroundResource(R.drawable.prizer_num_bg_blue);
                            } else if (2 == lotteryResult5d.get(7)) {
                                numh.setBackgroundResource(R.drawable.prizer_num_bg_green);
                            } else {
                                numh.setBackgroundResource(R.drawable.prizer_num_bg_red);
                            }
                            break;
                    }
                    numg.setVisibility(View.INVISIBLE);
                    llPk.setVisibility(View.VISIBLE);
                    numi.setVisibility(View.GONE);
                    numj.setVisibility(View.GONE);
                }
            }
        } else {
            llAllResult.setVisibility(View.GONE);
        }

        tvZhuangtai = (TextView) view.findViewById(R.id.tv_zhuangtai);
        tvJiangjin = (TextView) view.findViewById(R.id.tv_jiangjin);
        tvMethod = (TextView) view.findViewById(R.id.tv_method);
        tvJiangliqi = (TextView) view.findViewById(R.id.tv_jiangliqi);
        tvTitle.setText(myTZResult.getNickName());
        tvNickname.setText(myTZResult.getNickName());
        tvQihao.setText(myTZResult.getExpect());
        tvDuzhu.setText(myTZResult.getBetAmount() + "");
        tvTouzudian.setText(myTZResult.getTimes() + "");
        tvTouzusj.setText(TimeUtils.convertShortTime(myTZResult.getCreateTime()));
        if (myTZResult.getPlayNumReq() != null) {
            tvTouzuxq.setText(myTZResult.getPlayNumReq().getNum());
        }
        if (0 == myTZResult.getAwardStatus()) {
            tvZhuangtai.setText(getString(R.string.weikaijiang));
        } else if (1 == myTZResult.getAwardStatus()) {
            tvZhuangtai.setText(getString(R.string.shibai));
        } else {
            tvZhuangtai.setText(getString(R.string.zhongjiang));
        }
        String realProfitAmount = RegexUtils.westMoney(myTZResult.getRealProfitAmount());
        if ("".equals(realProfitAmount)) {
            tvJiangjin.setText("0.00");
        } else {
            tvJiangjin.setText(realProfitAmount);
        }
        if (0 == myTZResult.getPayMethd()) {
            tvMethod.setText(getString(R.string.lottery_open_rewards));
        } else if (1 == myTZResult.getPayMethd()) {
            tvMethod.setText(getString(R.string.lottery_open_rewards));
        } else {
            tvMethod.setText(getString(R.string.lottery_additional_bonuses));
        }
        if (myTZResult.getUpdateTime() != null) {
            tvJiangliqi.setText(TimeUtils.convertShortTime(myTZResult.getUpdateTime()));
        }
    }

    private void choicePis(TextView textView, int item) {
        if (1 == item) {
            textView.setBackgroundResource(R.drawable.dot01);
        } else if (2 == item) {
            textView.setBackgroundResource(R.drawable.dot02);
        } else if (3 == item) {
            textView.setBackgroundResource(R.drawable.dot03);
        } else if (4 == item) {
            textView.setBackgroundResource(R.drawable.dot04);
        } else if (5 == item) {
            textView.setBackgroundResource(R.drawable.dot05);
        } else if (6 == item) {
            textView.setBackgroundResource(R.drawable.dot06);
        }
    }

    private void choicePisYxx(TextView textView, int item) {
        if (1 == item) {
            textView.setBackgroundResource(R.drawable.fllu);
        } else if (2 == item) {
            textView.setBackgroundResource(R.drawable.flxie);
        } else if (3 == item) {
            textView.setBackgroundResource(R.drawable.frji);
        } else if (4 == item) {
            textView.setBackgroundResource(R.drawable.frfish);
        } else if (5 == item) {
            textView.setBackgroundResource(R.drawable.flpangxie);
        } else if (6 == item) {
            textView.setBackgroundResource(R.drawable.flxia);
        }
    }
}
