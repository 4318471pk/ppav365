package com.live.fox.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.entity.RoomListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class RecommendAnchorListFooter extends LinearLayout {

    TextView botText;
    GridLayout gridLayout;
    GradientTextView changeList;
    onClickChangeListListener onClickChangeListListener;
    List<RoomListBean> listBeans;

    public void setOnClickChangeListListener(RecommendAnchorListFooter.onClickChangeListListener onClickChangeListListener) {
        this.onClickChangeListListener = onClickChangeListListener;
    }

    public RecommendAnchorListFooter(Context context) {
        super(context);
        initView();
    }

    public RecommendAnchorListFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RecommendAnchorListFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView()
    {
        removeAllViews();
        setOrientation(VERTICAL);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setBackgroundColor(0xffffffff);

        botText=new TextView(getContext());
        botText.setBackgroundColor(0xffF5F1F8);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ScreenUtils.getDip2px(getContext()
        ,50));
        botText.setLayoutParams(ll);
        botText.setGravity(Gravity.CENTER);
        botText.setText(getResources().getString(R.string.hereIsTheEnd));
        botText.setTextColor(0xffB8B2C8);
        addView(botText);

        int dip2= ScreenUtils.getDip2px(getContext(),2);
        RelativeLayout topRl=new RelativeLayout(getContext());
        topRl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        topRl.setPadding(0,dip2*3,0,0);

        ImageView ivTag=new ImageView(getContext());
        ivTag.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ivTag.setImageDrawable(getResources().getDrawable(R.mipmap.icon_recommend_tag));
        topRl.addView(ivTag);

        changeList=new GradientTextView(getContext());
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(dip2*31, dip2*11);
        rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        rl.rightMargin=(int)(dip2*2.5);
        changeList.setLayoutParams(rl);
        changeList.setGravity(Gravity.CENTER);
        changeList.setTextColor(0xffA800FF);
        changeList.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        changeList.setText(getResources().getString(R.string.changeAnother));
        changeList.setStokeBackground(0xffA800FF,dip2*5,dip2/2);
        changeList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeList.setEnabled(false);
                if(onClickChangeListListener!=null)
                {
                    onClickChangeListListener.onChange();
                }
            }
        });
        topRl.addView(changeList);


        int dip2_5=ScreenUtils.getDip2px(getContext(),2.5f);
        gridLayout=new GridLayout(getContext());
        gridLayout.setBackgroundColor(0xffffff);
        gridLayout.setColumnCount(2);
        gridLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        gridLayout.setPadding(0,dip2*3,dip2_5*2,0);

        addView(topRl);
        addView(gridLayout);
    }


    public void setData(List<RoomListBean> listBeans)
    {
        changeList.setEnabled(true);
        if(listBeans==null)
        {
            this.listBeans=new ArrayList<>();
        }

        int dip2_5=ScreenUtils.getDip2px(getContext(),2.5f);
        int defaultDrawable=R.mipmap.icon_anchor_loading;
        gridLayout.removeAllViews();
        Drawable clock,diamond;
        clock=getContext().getResources().getDrawable(R.mipmap.icon_clock);
        diamond=getContext().getResources().getDrawable(R.mipmap.icon_diamond);
        int itemWidth= (ScreenUtils.getScreenWidth(getContext())-ScreenUtils.getDip2px(getContext(),15))/2;
        for (int i = 0; i < listBeans.size(); i++) {
            View view=View.inflate(getContext(),R.layout.item_anchor_list,null);
            view.setPadding(dip2_5*2,dip2_5*2,0,0);

            GradientTextView gtvUnitPrice = view.findViewById(R.id.gtvUnitPrice);  //类别
            GradientTextView tvAnchorPaymentType=view.findViewById(R.id.tvAnchorPaymentType);
            TextView name=view.findViewById(R.id.tv_nickname);
            ImageView ivRoundBG = view.findViewById(R.id.ivRoundBG);
            GlideUtils.loadDefaultImage(getContext(), listBeans.get(i).getRoomIcon(),defaultDrawable, ivRoundBG);
            name.setText(listBeans.get(i).getTitle());

            SpanUtils spUtils=new SpanUtils();
            spUtils.appendImage(clock,SpanUtils.ALIGN_CENTER);
            spUtils.append(" 21 ").setAlign(Layout.Alignment.ALIGN_CENTER);
            spUtils.appendImage(diamond,SpanUtils.ALIGN_CENTER);
            spUtils.append("/分钟").setAlign(Layout.Alignment.ALIGN_CENTER);
            gtvUnitPrice.setText(spUtils.create());


            //1普通房间2密码房间3计时房间4贵族房间5计场房间
            switch (listBeans.get(i).getRoomType())
            {
                case 1:
                case 2:
                case 4:
                    tvAnchorPaymentType.setVisibility(View.GONE);
                    gtvUnitPrice.setVisibility(View.GONE);
                    break;
                case 3:
                    tvAnchorPaymentType.setVisibility(View.VISIBLE);
                    tvAnchorPaymentType.setText(getContext().getString(R.string.charge_on_time));
                    gtvUnitPrice.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    tvAnchorPaymentType.setVisibility(View.VISIBLE);
                    tvAnchorPaymentType.setText(getContext().getString(R.string.charge_per_site));
                    gtvUnitPrice.setVisibility(View.VISIBLE);
                    break;
                default:
                    tvAnchorPaymentType.setVisibility(View.GONE);
                    gtvUnitPrice.setVisibility(View.GONE);
            }

            view.setTag(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
                        LoginModeSelActivity.startActivity(getContext());
                        return;
                    }
                    int tag=(Integer)view.getTag();
                    LivingActivity.startActivity(getContext(), RecommendAnchorListFooter.this.listBeans,tag);
                }
            });

            gridLayout.addView(view,itemWidth+dip2_5*2,itemWidth+dip2_5*2);
        }
    }

    public void setBotTextVisible(boolean isShow)
    {
        if(isShow)
        {
            botText.setText(getResources().getString(R.string.hereIsTheEnd));
            botText.setVisibility(VISIBLE);
//            botText.getLayoutParams().height=ScreenUtils.getDip2px(getContext(),50);
//            getLayoutParams().height=getLayoutParams().height+ScreenUtils.getDip2px(getContext(),50);
        }
        else
        {
            botText.setText("");
            botText.setVisibility(GONE);
//            botText.getLayoutParams().height=ScreenUtils.getDip2px(getContext(),0);

        }
    }

    public interface onClickChangeListListener
    {
        void onChange();
    }
}
