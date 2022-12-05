package com.live.fox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
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
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ImageUtils;
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
                if(ClickUtil.isClickWithShortTime(changeList.hashCode(),2000))
                {
                    return;
                }
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


    public void setData(List<RoomListBean> mListBeans)
    {
        if(this.listBeans==null)
        {
            this.listBeans=new ArrayList<>();
        }
        listBeans.clear();
        listBeans.addAll(mListBeans);

        int dip2_5=ScreenUtils.getDip2px(getContext(),2.5f);
        int dip13=ScreenUtils.getDip2px(getContext(),13);
        int defaultDrawable=R.mipmap.icon_anchor_loading;
        gridLayout.removeAllViews();
        Bitmap clock,diamond,ticket;
        ticket= BitmapFactory.decodeResource(getResources(),R.mipmap.icon_ticket);
        clock= BitmapFactory.decodeResource(getResources(),R.mipmap.icon_clock);
        diamond=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_diamond);
        int diamondWidth=ScreenUtils.getDip2px(getContext(),12.5f);
        int diamondHeight=ScreenUtils.getDip2px(getContext(),9.5f);

        int itemWidth= (ScreenUtils.getScreenWidth(getContext())-ScreenUtils.getDip2px(getContext(),15))/2;
        for (int i = 0; i < listBeans.size(); i++) {
            View view=View.inflate(getContext(),R.layout.item_anchor_list,null);
            view.setPadding(dip2_5*2,dip2_5*2,0,0);

            GradientTextView gtvUnitPrice = view.findViewById(R.id.gtvUnitPrice);  //类别
            GradientTextView tvAnchorPaymentType=view.findViewById(R.id.tvAnchorPaymentType);
            TextView name=view.findViewById(R.id.tv_nickname);
            TextView tvNum=view.findViewById(R.id.tvNum);
            AnchorRoundImageView ivRoundBG = view.findViewById(R.id.ivRoundBG);
            RoomListBean data=listBeans.get(i);
            if(TextUtils.isEmpty(data.getRoomIcon()))
            {
                ivRoundBG.setImageDrawable(getResources().getDrawable(defaultDrawable));
            }
            else
            {
                ivRoundBG.setRadius(dip2_5*4);
                GlideUtils.loadRoundedImage(getContext(), dip2_5*4,data.getRoomIcon(),0,0, ivRoundBG);
            }            name.setText(listBeans.get(i).getTitle());

            tvNum.setText(data.getLiveSum()+"");

            SpanUtils spUtils=new SpanUtils();
            //房间类型:0普通房间 1计时付费 2场次付费
            switch (listBeans.get(i).getRoomType())
            {
                case 0:
                    tvAnchorPaymentType.setVisibility(View.INVISIBLE);
                    gtvUnitPrice.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    gtvUnitPrice.setSolidBackground(0x4c000000, com.live.fox.utils.ScreenUtils.dp2px(getContext(),7.5f));
                    spUtils.appendImage(ImageUtils.scale(clock, dip13, dip13),SpanUtils.ALIGN_CENTER);
                    spUtils.append(" ").append(data.getRoomPrice()).setAlign(Layout.Alignment.ALIGN_CENTER).append(" ");

                    spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_CENTER);
                    spUtils.append(getContext().getResources().getString(R.string.unitPriceMin)).setAlign(Layout.Alignment.ALIGN_CENTER);
                    gtvUnitPrice.setText(spUtils.create());
                    gtvUnitPrice.setVisibility(View.VISIBLE);
                    gtvUnitPrice.setSolidBackground(0x4c000000, com.live.fox.utils.ScreenUtils.dp2px(getContext(),7.5f));

                    tvAnchorPaymentType.setVisibility(View.VISIBLE);
                    tvAnchorPaymentType.setText(getContext().getResources().getString(R.string.charge_on_time));
                    tvAnchorPaymentType.setSolidBackground(0x4c000000, com.live.fox.utils.ScreenUtils.dp2px(getContext(),7.5f));

                    break;
                case 2:
                    gtvUnitPrice.setSolidBackground(0x4cBF003A, com.live.fox.utils.ScreenUtils.dp2px(getContext(),7.5f));

                    gtvUnitPrice.setSolidBackground(0x4c000000, com.live.fox.utils.ScreenUtils.dp2px(getContext(),10));
                    spUtils.appendImage(ImageUtils.scale(ticket, dip13, dip13),SpanUtils.ALIGN_CENTER);
                    spUtils.append(" ").append(data.getRoomPrice()).setFontSize(11,true).setAlign(Layout.Alignment.ALIGN_CENTER).append(" ");

                    spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_CENTER);
                    spUtils.append(getContext().getResources().getString(R.string.unitPriceShow)).setFontSize(11,true).setAlign(Layout.Alignment.ALIGN_CENTER);
                    gtvUnitPrice.setText(spUtils.create());
                    gtvUnitPrice.setVisibility(View.VISIBLE);
                    gtvUnitPrice.setSolidBackground(0x4cBF003A, com.live.fox.utils.ScreenUtils.dp2px(getContext(),7.5f));

                    tvAnchorPaymentType.setVisibility(View.VISIBLE);
                    tvAnchorPaymentType.setText(getContext().getString(R.string.charge_per_site));
                    tvAnchorPaymentType.setSolidBackground(0x4cBF003A, com.live.fox.utils.ScreenUtils.dp2px(getContext(),7.5f));

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
