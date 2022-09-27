package com.live.fox.ui.mine.gamerecord;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.entity.MyTouzuBean;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeUtils;

import java.util.List;


public class HNCPCaiDetailActivity extends BaseHeadActivity {

    private TextView tvNickname;
    private TextView tvQihao;
    private TextView tvDuzhu;
    private TextView tvTouzudian;
    private TextView tvTouzusj;
    private TextView tvTouzuxq;
    private TextView numa;
    private TextView numb;
    private ImageView iv_hn;
    private LinearLayout llup;
    private LinearLayout llExtend;
    private TextView tvZhuangtai;
    private TextView tvJiangjin;
    private TextView tvMethod;
    private TextView tvJiangliqi;
    private MyTouzuBean myTZResult;
    private List<Integer> lotteryResult5d;

    public static void startActivity(Context context, MyTouzuBean myTZResult) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, HNCPCaiDetailActivity.class);
        i.putExtra("myTZResult", myTZResult);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hncpcai_detail_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        myTZResult = (MyTouzuBean) getIntent().getSerializableExtra("myTZResult");
        initView();
        initListener();
    }

    private void initListener() {
        llExtend.setOnClickListener(v -> {
            if (lotteryResult5d != null && lotteryResult5d.size() != 0) {
                showPopwindow();
            }
        });
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        tvNickname = findViewById(R.id.tv_nickname);
        tvQihao = findViewById(R.id.tv_qihao);
        tvDuzhu = findViewById(R.id.tv_duzhu);
        tvTouzudian = findViewById(R.id.tv_touzudian);
        tvTouzusj = findViewById(R.id.tv_touzusj);
        tvTouzuxq = findViewById(R.id.tv_touzuxq);
        numa = findViewById(R.id.numa);
        numb = findViewById(R.id.numb);
        iv_hn = findViewById(R.id.iv_hn);
        llup = findViewById(R.id.llup);
        llExtend = findViewById(R.id.llExtend);
        tvZhuangtai = findViewById(R.id.tv_zhuangtai);
        tvJiangjin = findViewById(R.id.tv_jiangjin);
        tvMethod = findViewById(R.id.tv_method);
        tvJiangliqi = findViewById(R.id.tv_jiangliqi);
        setHead(myTZResult.getNickName(), true, true);

        tvNickname.setText(myTZResult.getNickName());
        tvQihao.setText(myTZResult.getExpect());
        tvDuzhu.setText(String.valueOf(myTZResult.getBetAmount()));
        tvTouzudian.setText(String.valueOf(myTZResult.getTimes()));
        tvTouzusj.setText(TimeUtils.convertShortTime(myTZResult.getCreateTime()));
        if (myTZResult.getPlayNumReq() != null) {
            tvTouzuxq.setText(myTZResult.getPlayNumReq().getNum());
        }
        if (0 == myTZResult.getAwardStatus()) {
            tvZhuangtai.setText(getString(R.string.weikaijiang));
        } else if (1 == myTZResult.getAwardStatus()) {
            tvZhuangtai.setText(getString(R.string.shibai));
        } else {
            tvZhuangtai.setText(getString(R.string.zyd));
        }

        String realProfitAmount = RegexUtils.westMoney(myTZResult.getRealProfitAmount());
        if ("".equals(realProfitAmount)) {
            tvJiangjin.setText(getString(R.string.number_zero_point_zero));
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

        lotteryResult5d = myTZResult.getResultList();
        if (lotteryResult5d != null && lotteryResult5d.size() != 0) {
            numa.setText(String.valueOf(lotteryResult5d.get(0)));
            numb.setText(String.valueOf(lotteryResult5d.get(1)));
        } else {
            llup.setVisibility(View.GONE);
        }
    }

    public void showPopwindow() {
        //加载弹出框的布局
        View contentView = LayoutInflater.from(getCtx()).inflate(R.layout.hn_pop_layout, null);
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
        numa.setText(String.valueOf(lotteryResult5d.get(0)));
        numb.setText(String.valueOf(lotteryResult5d.get(1)));
        numc.setText(String.valueOf(lotteryResult5d.get(2)));
        numd.setText(String.valueOf(lotteryResult5d.get(3)));
        tvg4.setText(String.valueOf(lotteryResult5d.get(4)));
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

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
