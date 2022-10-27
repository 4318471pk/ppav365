package com.live.fox.dialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.live.fox.R;
import com.live.fox.adapter.TreasureBoxPagerAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTreasureBoxBinding;
import com.live.fox.entity.TreasureItemBean;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.view.BotTriangleBubbleView;

import java.util.ArrayList;
import java.util.List;

//包含礼物特权背包
public class TreasureBoxDialog extends BaseBindingDialogFragment {

    DialogTreasureBoxBinding mBind;
    List<List<TreasureItemBean>> lists;
    TreasureBoxPagerAdapter adapter;

    public static TreasureBoxDialog getInstance()
    {
        return new TreasureBoxDialog();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                startAnimate(mBind.rlContent,false);
                break;
            case R.id.tvAmount:
                addBubbleView();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_treasure_box;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        lists=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<TreasureItemBean> list=new ArrayList<>();
            for (int j = 0; j < 36; j++) {
                TreasureItemBean bean=new TreasureItemBean();
                bean.setCostDiamond(99);
                bean.setImgUrl("");
                bean.setSelected(false);
                bean.setName("谢谢");
            }
            lists.add(list);
        }
        adapter=new TreasureBoxPagerAdapter(getContext(),lists);
        mBind.viewPager.setAdapter(adapter);

        view.setVisibility(View.GONE);
        int screenWidth= ScreenUtils.getScreenWidth(getActivity());
        int screenHeight=ScreenUtils.getScreenHeight(getActivity());
        mBind.rlContent.getLayoutParams().height=(int)(screenHeight*0.69f);

        FixImageSize.setImageSizeOnWidthWithSRC(mBind.ivFirstTimeTopUp, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {

                RelativeLayout.LayoutParams rlContent=(RelativeLayout.LayoutParams)mBind.rrlContent.getLayoutParams();
                rlContent.topMargin=height/2;
                mBind.rrlContent.setLayoutParams(rlContent);
                mBind.rrlContent.setPadding(0,height/2,0,0);

                mBind.tabLayout.setGradient(0xffA800FF,0xffEA00FF);


                view.setVisibility(View.VISIBLE);
            }
        });

        startAnimate(mBind.rlContent,true);
    }

    private void addBubbleView()
    {
        int height=mBind.rlContent.getLayoutParams().height;
        RelativeLayout relativeLayout=new RelativeLayout(getActivity());
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBind.rlContent.addView(view);
            }
        });

        BotTriangleBubbleView botTriangleBubbleView=new BotTriangleBubbleView(getActivity());
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl.leftMargin=mBind.tvAmount.getLeft();
        rl.bottomMargin=mBind.tvAmount.getTop();
        rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        botTriangleBubbleView.setLayoutParams(rl);
        relativeLayout.addView(botTriangleBubbleView);

        mBind.rlContent.addView(relativeLayout);
    }
}
