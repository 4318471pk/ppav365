package com.live.fox.ui.mine.activity.gamerecord;

import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_FF;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.entity.MyTouzuBean;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeUtils;

import java.util.List;

public class CaiDetailActivity extends BaseHeadActivity {

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
    private TextView numi;
    private TextView numj;
    private LinearLayout llPk;
    private LinearLayout llAllResult;
    private TextView tvZhuangtai;
    private TextView tvJiangjin;
    private TextView tvMethod;
    private TextView tvJiangliqi;
    MyTouzuBean myTZResult;

    public static void startActivity(Context context, MyTouzuBean myTZResult) {
        Constant.isAppInsideClick=true;
        Intent i = new Intent(context, CaiDetailActivity.class);
        i.putExtra("myTZResult", myTZResult);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cai_detail_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        myTZResult = (MyTouzuBean) getIntent().getSerializableExtra("myTZResult");
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(myTZResult.getNickName(), true, true);

        tvNickname = findViewById(R.id.tv_nickname);
        tvQihao = findViewById(R.id.tv_qihao);
        tvDuzhu = findViewById(R.id.tv_duzhu);
        tvTouzudian = findViewById(R.id.tv_touzudian);
        tvTouzusj = findViewById(R.id.tv_touzusj);
        tvTouzuxq = findViewById(R.id.tv_touzuxq);
        numa = findViewById(R.id.numpa);
        numb = findViewById(R.id.numpb);
        numc = findViewById(R.id.numpc);
        numd = findViewById(R.id.numpd);
        nume = findViewById(R.id.numpe);
        numf = findViewById(R.id.numpf);
        numg = findViewById(R.id.numpg);
        numh = findViewById(R.id.numph);
        numi = findViewById(R.id.numpi);
        numj = findViewById(R.id.numpj);
        llPk = findViewById(R.id.ll_pk);
        llAllResult = findViewById(R.id.llAllResult);
        tvZhuangtai = findViewById(R.id.tv_zhuangtai);
        tvJiangjin = findViewById(R.id.tv_jiangjin);
        tvMethod = findViewById(R.id.tv_method);
        tvJiangliqi = findViewById(R.id.tv_jiangliqi);


        tvNickname.setText(myTZResult.getNickName());
        tvQihao.setText(myTZResult.getExpect());
        tvDuzhu.setText(String.valueOf(myTZResult.getBetAmount()));
        tvTouzudian.setText(String.valueOf(myTZResult.getTimes()));
        tvTouzusj.setText(TimeUtils.convertShortTime(myTZResult.getCreateTime()));
        if (myTZResult.getPlayNumReq()!=null) {
            tvTouzuxq.setText(myTZResult.getPlayNumReq().getNum());
        }
        if (0 == myTZResult.getAwardStatus()) {
            tvZhuangtai.setText(getString(R.string.weikaijiang));
        } else if (1 == myTZResult.getAwardStatus()) {
            tvZhuangtai.setText(getString(R.string.shibai));
        } else {
            tvZhuangtai.setText(getString(R.string.zyd));
        }
        String realProfitAmount = (long)myTZResult.getRealProfitAmount() + "";
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
        if(myTZResult.getUpdateTime()!=null) {
            tvJiangliqi.setText(TimeUtils.convertShortTime(myTZResult.getUpdateTime()));
        }

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
            }  else if (TYPE_CP_FF.equals(myTZResult.getLotteryName())) {
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
            }else if ("pk10".equals(myTZResult.getLotteryName())) {
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
    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
