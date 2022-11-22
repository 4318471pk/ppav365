package com.live.fox.ui.living;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ViewLivingFinishBinding;
import com.live.fox.entity.RoomListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_User;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.AnchorRoundImageView;
import com.live.fox.view.GradientTextView;
import com.live.fox.view.RecommendAnchorListFooter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LivingFinishView extends RelativeLayout {

    ViewLivingFinishBinding mBind;
    LivingFragment livingFragment;
    boolean isVisible;

    public LivingFinishView(LivingFragment fragment, ViewGroup parent,boolean isVisible) {
        super(fragment.getActivity());
        initView(fragment,parent,isVisible);
    }

    public LivingFinishView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LivingFinishView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(LivingFragment fragment,ViewGroup parent,boolean isVisible)
    {
        livingFragment=fragment;
        mBind= DataBindingUtil.inflate(fragment.getLayoutInflater(), R.layout.view_living_finish,parent,false);
        mBind.setClick(this);
        addView(mBind.getRoot());

        mBind.llFinishLiving.setPadding(0, StatusBarUtil.getStatusBarHeight(fragment.getActivity()),0,0);
        if(isVisible)
        {
            showView();
        }
        else
        {
            setVisibility(GONE);
        }

    }

    //显示View就初始化数据
    public void showView()
    {
        setVisibility(VISIBLE);
        if(getMainActivity().recommendListAdapter!=null && getMainActivity().recommendListAdapter.getData()!=null)
        {
            setGridLayout(getMainActivity().recommendListAdapter.getData());
        }

        if(livingFragment.getRoomBean()!=null)
        {
            RoomListBean roomListBean=livingFragment.getRoomBean();
            mBind.tvName.setText(roomListBean.getTitle());
        }

        int dip17_5=ScreenUtils.getDip2px(livingFragment.getActivity(),17.5f);
        if(livingFragment.livingCurrentAnchorBean!=null)
        {
            mBind.tvName.setText(livingFragment.livingCurrentAnchorBean.getNickname());
            mBind.gtvFollow.setVisibility(VISIBLE);
            if(!livingFragment.livingCurrentAnchorBean.getFollow())
            {
                int color[]=getResources().getIntArray(R.array.contactCardBtn);
                mBind.gtvFollow.setGradientBackground(color,dip17_5);
                mBind.gtvFollow.setTextColor(0xffFFFFFF);
                mBind.gtvFollow.setText(livingFragment.getStringWithoutContext(R.string.follow2));
                mBind.gtvFollow.setEnabled(true);
            }
            else
            {
                mBind.gtvFollow.setEnabled(false);
                mBind.gtvFollow.setSolidBackground(0xffffffff,dip17_5);
                mBind.gtvFollow.setTextColor(0xffA2A0A9);
                mBind.gtvFollow.setText(livingFragment.getStringWithoutContext(R.string.followed));
            }

            if(livingFragment.livingCurrentAnchorBean.getLiveSum()>0)
            {
                String str=livingFragment.getStringWithoutContext(R.string.hasWatched);
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(livingFragment.livingCurrentAnchorBean.getLiveSum()).append(str);
                mBind.tvWatched.setText(stringBuilder.toString());

            }

            String url=livingFragment.livingCurrentAnchorBean.getAvatar();
            GlideUtils.loadCircleImage(getMainActivity(),url,R.mipmap.user_head_error,R.mipmap.user_head_error,mBind.rivImage);


        }

        getRecommendList();
    }

    public void onClickView(View view)
    {
        switch (view.getId())
        {
            case R.id.gtvFollow:
                follow();
                break;
            case R.id.ivBack:
                livingFragment.getActivity().finish();
                break;
            case R.id.gtvChangeList:
                getRecommendList();
                break;
        }
    }

    private boolean isActivityOK()
    {
        if(livingFragment!=null && livingFragment.getActivity()!=null
                && !livingFragment.getActivity().isFinishing() && !livingFragment.getActivity().isDestroyed())
        {
            return true;
        }
        return false;
    }

    private LivingActivity getMainActivity()
    {
        if(isActivityOK())
        {
            return (LivingActivity)livingFragment.getActivity();
        }
        return null;
    }


    public void getRecommendList() {
        mBind.gtvChangeList.setEnabled(false);
        Api_Live.ins().getRecommendLiveList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (!isActivityOK()) {
                    return;
                }

                mBind.gtvChangeList.setEnabled(true);

                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray list = jsonObject.getJSONArray("list");
                        if (list != null && list.length() > 0) {
                            List<RoomListBean> listBeans = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                RoomListBean bean = new Gson().fromJson(list.getJSONObject(i).toString(), RoomListBean.class);
                                listBeans.add(bean);
                            }
                            setGridLayout(listBeans);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void follow()
    {
        if(livingFragment.getRoomBean()==null)
        {
            return;
        }

        mBind.gtvFollow.setEnabled(false);
        Api_User.ins().followUser(livingFragment.getRoomBean().getAid(), true, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(livingFragment.isActivityOK())
                {
                    mBind.gtvFollow.setEnabled(true);
                    if(code==0)
                    {
                        int dip17_5=ScreenUtils.getDip2px(livingFragment.getActivity(),17.5f);
                        mBind.gtvFollow.setEnabled(false);
                        mBind.gtvFollow.setSolidBackground(0xffffffff,dip17_5);
                        mBind.gtvFollow.setTextColor(0xffA2A0A9);
                        mBind.gtvFollow.setText(livingFragment.getStringWithoutContext(R.string.followed));
                        livingFragment.livingCurrentAnchorBean.setFollow(true);
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }

    private void setGridLayout(List<RoomListBean> listBeans)
    {
        int dip2_5= ScreenUtils.getDip2px(getContext(),2.5f);
        int dip13=ScreenUtils.getDip2px(getMainActivity(),13);
        int defaultDrawable=R.mipmap.icon_anchor_loading;
        mBind.glBotView.removeAllViews();
        Bitmap clock,diamond,ticket;
        ticket=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_ticket);
        clock= BitmapFactory.decodeResource(getResources(),R.mipmap.icon_clock);
        diamond=BitmapFactory.decodeResource(getResources(),R.mipmap.icon_diamond);
        int itemWidth= (ScreenUtils.getScreenWidth(getContext())-ScreenUtils.getDip2px(getContext(),15))/2;
        int diamondWidth=ScreenUtils.getDip2px(getMainActivity(),12.5f);
        int diamondHeight=ScreenUtils.getDip2px(getMainActivity(),9.5f);

        if(listBeans!=null && listBeans.size()>0)
        {
            for (int i = 0; i < 4; i++) {
                if(i>=listBeans.size())
                {
                    break;
                }

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
                    GlideUtils.loadRoundedImage(getContext(), dip2_5*4,data.getRoomIcon(),0,defaultDrawable, ivRoundBG);
                }            name.setText(listBeans.get(i).getTitle());

                SpanUtils spUtils=new SpanUtils();

                tvNum.setText(data.getLiveSum()+"");

                //1普通房间2密码房间3计时房间4贵族房间5计场房间
                switch (listBeans.get(i).getRoomType())
                {
                    case 1:
                    case 2:
                    case 4:
                        tvAnchorPaymentType.setVisibility(View.INVISIBLE);
                        gtvUnitPrice.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        gtvUnitPrice.setSolidBackground(0x4c000000,ScreenUtils.getDip2px(getMainActivity(),10));
                        spUtils.appendImage(ImageUtils.scale(clock, dip13, dip13),SpanUtils.ALIGN_BASELINE);
                        spUtils.append(" ").append(data.getRoomPrice()).append(" ");

                        spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_BASELINE);
                        spUtils.append(livingFragment.getStringWithoutContext(R.string.unitPriceMin));
                        gtvUnitPrice.setText(spUtils.create());
                        gtvUnitPrice.setVisibility(View.VISIBLE);
                        gtvUnitPrice.setSolidBackground(0x4c000000,ScreenUtils.getDip2px(getMainActivity(),7.5f));

                        tvAnchorPaymentType.setVisibility(View.VISIBLE);
                        tvAnchorPaymentType.setText(getContext().getString(R.string.charge_on_time));
                        tvAnchorPaymentType.setSolidBackground(0x4c000000,ScreenUtils.getDip2px(getMainActivity(),7.5f));

                        break;
                    case 5:
                        gtvUnitPrice.setSolidBackground(0x4cBF003A,ScreenUtils.getDip2px(getMainActivity(),10));

                        gtvUnitPrice.setSolidBackground(0x4c000000,ScreenUtils.getDip2px(getMainActivity(),10));
                        spUtils.appendImage(ImageUtils.scale(ticket, dip13, dip13),SpanUtils.ALIGN_BASELINE);
                        spUtils.append(" ").append(data.getRoomPrice()).append(" ");

                        spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_BASELINE);
                        spUtils.append(livingFragment.getStringWithoutContext(R.string.unitPriceMin));
                        gtvUnitPrice.setText(spUtils.create());
                        gtvUnitPrice.setVisibility(View.VISIBLE);
                        gtvUnitPrice.setSolidBackground(0x4cBF003A,ScreenUtils.getDip2px(getMainActivity(),7.5f));

                        tvAnchorPaymentType.setVisibility(View.VISIBLE);
                        tvAnchorPaymentType.setText(getContext().getString(R.string.charge_per_site));
                        tvAnchorPaymentType.setSolidBackground(0x4cBF003A,ScreenUtils.getDip2px(getMainActivity(),7.5f));

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
                        getMainActivity().resetRoomList(listBeans,tag);
//                        LivingActivity.startActivity(getContext(), listBeans,tag);
                    }
                });

                mBind.glBotView.addView(view,itemWidth+dip2_5*2,itemWidth+dip2_5*2);
            }
        }
    }
}
