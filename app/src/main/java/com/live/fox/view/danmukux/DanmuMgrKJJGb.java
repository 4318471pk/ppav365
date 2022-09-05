package com.live.fox.view.danmukux;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.entity.TwentyNineBean;

import java.util.List;


public class DanmuMgrKJJGb {

    private Context mContext;
    private DanmuKJJGViewb mDanmuContainerView;
    private long lastDanmuTime = 0L;
    private TwentyNineBean lsResult;
    ViewGroup mDanmuContainer;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
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

    public DanmuMgrKJJGb(Context context) {
        this.mContext = context;
    }

    /**
     * 设置弹幕view
     *
     * @param danmakuView 弹幕view
     */
    public void setDanmakuView(DanmuKJJGViewb danmakuView) {
        this.mDanmuContainerView = danmakuView;
        initDanmuView();
    }


    private void initDanmuView() {
        mDanmuContainerView.setConverter(danmuConverter);
        mDanmuContainerView.setLeader(this);
        mDanmuContainerView.setSpeed(DanmuKJJGViewb.NORMAL_SPEED);
    }

    DanmuConverter danmuConverter = new DanmuConverter<DanmuEntity>() {
        @Override
        public int getSingleLineHeight() {
            //将所有类型弹幕的布局拿出来，找到高度最大值，作为弹道高度
            View view = LayoutInflater.from(mContext).inflate(R.layout.ppkjjg, null);
            //指定行高
            view.measure(0, 0);

            return view.getMeasuredHeight();
        }

        @Override
        public View convert(DanmuEntity model) {
            View view = null;
            //普通弹幕
            if (model.getType() == 15) { //直播间cp
                view = LayoutInflater.from(mContext).inflate(R.layout.ppkjjg, null);
                RelativeLayout reWhole = view.findViewById(R.id.reWhole);
                TextView tv_message = view.findViewById(R.id.tv_message);
                ImageView iv_colse = view.findViewById(R.id.iv_colse);
                tv_message.setText(model.content);
                LinearLayout llAllResult = (LinearLayout) view.findViewById(R.id.llAllResult);
                TextView numa = (TextView) view.findViewById(R.id.numpa);
                TextView numb = (TextView) view.findViewById(R.id.numpb);
                TextView numc = (TextView) view.findViewById(R.id.numpc);
                TextView numd = (TextView) view.findViewById(R.id.numpd);
                TextView nume = (TextView) view.findViewById(R.id.numpe);
                TextView numf = (TextView) view.findViewById(R.id.numpf);
                TextView numg = (TextView) view.findViewById(R.id.numpg);
                TextView numh = (TextView) view.findViewById(R.id.numph);
                TextView numi = (TextView) view.findViewById(R.id.numpi);
                TextView numj = (TextView) view.findViewById(R.id.numpj);
                List<Integer> lotteryResultList = lsResult.getResultList();
                if (lotteryResultList != null && lotteryResultList.size() != 0) {
                    llAllResult.setVisibility(View.VISIBLE);
                    if ("txssc".equals(lsResult.getName())) {
                        for (int i = 0; i < lotteryResultList.size(); i++) {
                            switch (i) {
                                case 0:
                                    numa.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 1:
                                    numb.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 2:
                                    numc.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 3:
                                    numd.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 4:
                                    nume.setText(lotteryResultList.get(i) + "");
                                    break;
                            }
                        }
                        numf.setVisibility(View.INVISIBLE);
                        numg.setVisibility(View.INVISIBLE);
                        numh.setVisibility(View.INVISIBLE);
                        numi.setVisibility(View.INVISIBLE);
                        numj.setVisibility(View.INVISIBLE);
                    } else if ("jsks".equals(lsResult.getName())) {
                        for (int i = 0; i < lotteryResultList.size(); i++) {
                            switch (i) {
                                case 0:
                                    choicePis(numa, lotteryResultList.get(i));
                                    break;
                                case 1:
                                    choicePis(numb, lotteryResultList.get(i));
                                    break;
                                case 2:
                                    choicePis(numc, lotteryResultList.get(i));
                                    break;
                            }
                        }
                        numd.setVisibility(View.INVISIBLE);
                        nume.setVisibility(View.INVISIBLE);
                        numf.setVisibility(View.INVISIBLE);
                        numg.setVisibility(View.INVISIBLE);
                        numh.setVisibility(View.INVISIBLE);
                        numi.setVisibility(View.INVISIBLE);
                        numj.setVisibility(View.INVISIBLE);
                    } else if ("yuxx".equals(lsResult.getName())) {
                        for (int i = 0; i < lotteryResultList.size(); i++) {
                            switch (i) {
                                case 0:
                                    choicePisYxx(numa, lotteryResultList.get(i));
                                    break;
                                case 1:
                                    choicePisYxx(numb, lotteryResultList.get(i));
                                    break;
                                case 2:
                                    choicePisYxx(numc, lotteryResultList.get(i));
                                    break;
                            }
                        }
                        numd.setVisibility(View.INVISIBLE);
                        nume.setVisibility(View.INVISIBLE);
                        numf.setVisibility(View.INVISIBLE);
                        numg.setVisibility(View.INVISIBLE);
                        numh.setVisibility(View.INVISIBLE);
                        numi.setVisibility(View.INVISIBLE);
                        numj.setVisibility(View.INVISIBLE);
                    } else if ("pk10".equals(lsResult.getName())) {
                        for (int i = 0; i < lotteryResultList.size(); i++) {
                            switch (i) {
                                case 0:
                                    numa.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 1:
                                    numb.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 2:
                                    numc.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 3:
                                    numd.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 4:
                                    nume.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 5:
                                    numf.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 6:
                                    numg.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 7:
                                    numh.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 8:
                                    numi.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 9:
                                    numj.setText(lotteryResultList.get(i) + "");
                                    break;
                            }
                        }
                    } else if ("yflhc".equals(lsResult.getName())) {
                        for (int i = 0; i < lotteryResultList.size() - 1; i++) {
                            switch (i) {
                                case 0:
                                    numa.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 1:
                                    numb.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 2:
                                    numc.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 3:
                                    numd.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 4:
                                    nume.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 5:
                                    numf.setText(lotteryResultList.get(i) + "");
                                    break;
                                case 6:
                                    numh.setText(lotteryResultList.get(i) + "");
                                    if (3 == lotteryResultList.get(7)) {
                                        numh.setBackgroundResource(R.drawable.prizer_num_bg_blue);
                                    } else if (2 == lotteryResultList.get(7)) {
                                        numh.setBackgroundResource(R.drawable.prizer_num_bg_green);
                                    } else {
                                        numh.setBackgroundResource(R.drawable.prizer_num_bg_red);
                                    }
                                    break;
                            }
                            numg.setText("+");
                            numg.setBackgroundResource(0);
                            numi.setVisibility(View.INVISIBLE);
                            numj.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    llAllResult.setVisibility(View.GONE);
                }
                iv_colse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reWhole.setVisibility(View.GONE);
                    }
                });
            }
            return view;
        }
    };

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



    /**
     * 设置开奖结果
     *
     * @param lsResult 开奖结果
     */
    public void addDrawResult(TwentyNineBean lsResult) {
        this.lsResult = lsResult;
        DanmuEntity danmuEntity = new DanmuEntity();
        String text = String.format(mContext.getString(R.string.colon),
                lsResult.getNickName(), mContext.getString(R.string.draw_issue_number),
                lsResult.getExpect());
        danmuEntity.setContent(text);
        danmuEntity.setType(15);
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

    public void destroy() {
        mHandler.removeMessages(1011);
    }
}
