package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.entity.MyTZResult;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.TimeUtils;

import java.util.List;


public class MyLHCDialogFragment extends DialogFragment {

    private MyTZResult myTZResult;
    private TextView tvTitle;
    private TextView tvNickname;
    private TextView tvQihao;
    private TextView tvDuzhu;
    private TextView tvTouzudian;
    private TextView tvTouzusj;
    private TextView tvTouzuxq;
    private LinearLayout llup;
    private TextView numa;
    private TextView numb;
    private LinearLayout llExtend;
    private TextView tvZhuangtai;
    private TextView tvJiangjin;
    private TextView tvMethod;
    private TextView tvJiangliqi;
    private ImageView iv_hn;
    private List<Integer> lotteryResult5d;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_lhc);
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
        return inflater.inflate(R.layout.fragment_lhc, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        myTZResult = (MyTZResult) bundle.getSerializable("myTZResult");
        initView(view);
        initListener();
    }

    private void initListener() {
        llExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lotteryResult5d != null && lotteryResult5d.size() != 0) {
                    showPopwindow();
                }
            }
        });
    }

    public static MyLHCDialogFragment newInstance(MyTZResult myTZResult) {
        MyLHCDialogFragment fragment = new MyLHCDialogFragment();
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
        llExtend = (LinearLayout) view.findViewById(R.id.llExtend);
        numa = (TextView) view.findViewById(R.id.numa);
        numb = (TextView) view.findViewById(R.id.numb);
        llup = (LinearLayout) view.findViewById(R.id.llup);
        iv_hn = (ImageView) view.findViewById(R.id.iv_hn);
        lotteryResult5d = myTZResult.getResultList();
        if (lotteryResult5d != null && lotteryResult5d.size() != 0) {
            numa.setText(String.valueOf(lotteryResult5d.get(0)));
            numb.setText(String.valueOf(lotteryResult5d.get(1)));
        } else {
            llup.setVisibility(View.GONE);
        }

        tvZhuangtai = view.findViewById(R.id.tv_zhuangtai);
        tvJiangjin = view.findViewById(R.id.tv_jiangjin);
        tvMethod = view.findViewById(R.id.tv_method);
        tvJiangliqi = view.findViewById(R.id.tv_jiangliqi);
        tvTitle.setText(myTZResult.getNickName());
        tvNickname.setText(myTZResult.getNickName());
        tvQihao.setText(myTZResult.getExpect());
        tvDuzhu.setText(String.valueOf(myTZResult.getBetAmount()));
        tvTouzudian.setText(String.valueOf(myTZResult.getTimes()));
        tvTouzusj.setText(TimeUtils.convertShortTime(myTZResult.getCreateTime()));
        if (myTZResult.getPlayNumReq() != null) {
            tvTouzuxq.setText(myTZResult.getPlayNumReq().getNum());
        }
        if (0 == myTZResult.getAwardStatus()) {
            tvZhuangtai.setText(getString(R.string.lottery_open_prize_yet));
        } else if (1 == myTZResult.getAwardStatus()) {
            tvZhuangtai.setText(getString(R.string.lottery_open_prize_lost));
        } else {
            tvZhuangtai.setText(getString(R.string.zyd));
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
            tvMethod.setText(getString(R.string.lottery_bet_automatic));
        } else {
            tvMethod.setText(getString(R.string.lottery_additional_bonuses));
        }
        if (myTZResult.getUpdateTime() != null) {
            tvJiangliqi.setText(TimeUtils.convertShortTime(myTZResult.getUpdateTime()));
        }
    }

    public void showPopwindow() {
        //加载弹出框的布局
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.hn_pop_layout, null);
        LinearLayout llup = contentView.findViewById(R.id.llup);
        TextView numa = contentView.findViewById(R.id.numa);
        TextView numb = contentView.findViewById(R.id.numb);
        TextView numc = contentView.findViewById(R.id.numc);
        TextView numd = contentView.findViewById(R.id.numd);
        ImageView ivHn = contentView.findViewById(R.id.iv_hn);
        LinearLayout llblow = contentView.findViewById(R.id.llblow);
        TextView tvg4 = contentView.findViewById(R.id.tvg4);
        TextView tvg5 = contentView.findViewById(R.id.tvg5);
        TextView tvg6 = contentView.findViewById(R.id.tvg6);
        TextView tvg7 = contentView.findViewById(R.id.tvg7);
        TextView tvg8 = contentView.findViewById(R.id.tvg8);
        TextView tvg9 = contentView.findViewById(R.id.tvg9);
        TextView tvg10 = contentView.findViewById(R.id.tvg10);
        TextView tvg11 = contentView.findViewById(R.id.tvg11);
        TextView tvg12 = contentView.findViewById(R.id.tvg12);
        TextView tvg13 = contentView.findViewById(R.id.tvg13);
        TextView tvg14 = contentView.findViewById(R.id.tvg14);
        TextView tvg15 = contentView.findViewById(R.id.tvg15);
        TextView tvg16 = contentView.findViewById(R.id.tvg16);
        TextView tvg17 = contentView.findViewById(R.id.tvg17);
        TextView tvg18 = contentView.findViewById(R.id.tvg18);
        TextView tvg19 = contentView.findViewById(R.id.tvg19);
        TextView tvg20 = contentView.findViewById(R.id.tvg20);
        TextView tvg21 = contentView.findViewById(R.id.tvg21);
        TextView tvg22 = contentView.findViewById(R.id.tvg22);
        TextView tvg23 = contentView.findViewById(R.id.tvg23);
        TextView tvg24 = contentView.findViewById(R.id.tvg24);
        TextView tvg25 = contentView.findViewById(R.id.tvg25);
        TextView tvg26 = contentView.findViewById(R.id.tvg26);
        numa.setText(lotteryResult5d.get(0) + "");
        numb.setText(lotteryResult5d.get(1) + "");
        numc.setText(lotteryResult5d.get(2) + "");
        numd.setText(lotteryResult5d.get(3) + "");
        tvg4.setText(lotteryResult5d.get(4) + "");
        tvg5.setText(lotteryResult5d.get(5) + "");
        tvg6.setText(lotteryResult5d.get(6) + "");
        tvg7.setText(lotteryResult5d.get(7) + "");
        tvg8.setText(lotteryResult5d.get(8) + "");
        tvg9.setText(lotteryResult5d.get(9) + "");
        tvg10.setText(lotteryResult5d.get(10) + "");
        tvg11.setText(lotteryResult5d.get(11) + "");
        tvg12.setText(lotteryResult5d.get(12) + "");
        tvg13.setText(lotteryResult5d.get(13) + "");
        tvg14.setText(lotteryResult5d.get(14) + "");
        tvg15.setText(lotteryResult5d.get(15) + "");
        tvg16.setText(lotteryResult5d.get(16) + "");
        tvg17.setText(lotteryResult5d.get(17) + "");
        tvg18.setText(lotteryResult5d.get(18) + "");
        tvg19.setText(lotteryResult5d.get(19) + "");
        tvg20.setText(lotteryResult5d.get(20) + "");
        tvg21.setText(lotteryResult5d.get(21) + "");
        tvg22.setText(lotteryResult5d.get(22) + "");
        if (lotteryResult5d.get(23) != null && lotteryResult5d.get(23) < 10) {
            tvg23.setText("0" + lotteryResult5d.get(23));
        } else {
            tvg23.setText(lotteryResult5d.get(23) + "");
        }
        if (lotteryResult5d.get(24) != null && lotteryResult5d.get(24) < 10) {
            tvg24.setText("0" + lotteryResult5d.get(24));
        } else {
            tvg24.setText(lotteryResult5d.get(24) + "");
        }
        if (lotteryResult5d.get(25) != null && lotteryResult5d.get(25) < 10) {
            tvg25.setText("0" + lotteryResult5d.get(25));
        } else {
            tvg25.setText(lotteryResult5d.get(25) + "");
        }
        if (lotteryResult5d.get(26) != null && lotteryResult5d.get(26) < 10) {
            tvg26.setText("0" + lotteryResult5d.get(26));
        } else {
            tvg26.setText(lotteryResult5d.get(26) + "");
        }

        PopupWindow window = new PopupWindow(iv_hn, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setContentView(contentView);
        window.setOutsideTouchable(false);
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        window.setBackgroundDrawable(null);//?
        window.setAnimationStyle(R.style.PopupWindowAnimStyle2);
        window.showAsDropDown(iv_hn);
        //进入退出的动画，指定刚才定义的style
    }

}
