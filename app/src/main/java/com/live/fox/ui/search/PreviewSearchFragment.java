package com.live.fox.ui.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentPreviewSearchBinding;
import com.live.fox.db.LocalWatchHistoryDao;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.FlowDataBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.entity.RoomWatchedHistoryBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.AnchorRoundImageView;
import com.live.fox.view.EmptyDataView;
import com.live.fox.view.GradientTextView;
import com.live.fox.view.MyFlowLayout;
import com.live.fox.view.RecommendAnchorListFooter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreviewSearchFragment extends BaseBindingFragment {

    FragmentPreviewSearchBinding mBind;
    RecommendAnchorListFooter footer;
    EmptyDataView emptyDataView;

    public static PreviewSearchFragment getInstance()
    {
        return new PreviewSearchFragment();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.ivDelete:
                SPUtils.getInstance().put(ConstantValue.searchHistory,"");
                setHistory();
                break;
        }
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if(args!=null && mBind!=null && isAdded())
        {
            int status=args.getInt(SearchAnchorActivity.Status,SearchAnchorActivity.Normal);
            switch (status)
            {
                case 0:
                    setHistory();
                    setGL();
                    mBind.llMain.removeView(emptyDataView);
                    break;
                case 1:
                    mBind.llRecentWatchedHistory.setVisibility(View.GONE);
                    mBind.llSearchHistory.setVisibility(View.GONE);
                    mBind.llMain.addView(emptyDataView,0);
                    footer.setBotTextVisible(false);
                    break;
            }
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_preview_search;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        emptyDataView=new EmptyDataView(getActivity());

        footer=new RecommendAnchorListFooter(getActivity());
        footer.setBotTextVisible(false);
        footer.setOnClickChangeListListener(new RecommendAnchorListFooter.onClickChangeListListener() {
            @Override
            public void onChange() {
                getRecommendList();
            }
        });
        mBind.llMain.addView(footer);
        mBind.myFL.setOnClickItemListener(new MyFlowLayout.OnClickItemListener() {
            @Override
            public void onItemClick(View v, String text, int pos) {
                getMainActivity().mBind.etSearch.setText(text);
                getMainActivity().mBind.etSearch.setSelection(getMainActivity().mBind.etSearch.getText().length());
                getMainActivity().searchAnchor(text);
            }
        });
        setGL();
        getRecommendList();
    }

    @Override
    public void onStart() {
        super.onStart();
        setHistory();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
        {
            setHistory();
        }
    }

    private void setHistory()
    {
        if(mBind.myFL!=null)
        {
            int dip10=ScreenUtils.getDip2px(getActivity(),10);
            mBind.myFL.setHorizontalMargin(dip10);
            mBind.myFL.setVerticalMargin(dip10);
            mBind.myFL.setMaxLines(4);
            mBind.myFL.setTextMaxLength(20);
            List<FlowDataBean> mData = new ArrayList<>();
            String history= SPUtils.getInstance().getString(ConstantValue.searchHistory,"");
            if(!TextUtils.isEmpty(history) && history.length()>1)
            {

                String text[]=history.substring(1).split(",");
                if(text!=null)
                {
                    boolean isMoreThan50=text.length>50;
                    int count=text.length>30?30:text.length;
                    StringBuilder sb=new StringBuilder();
                    //超过50条就取前面的30条保存
                    for (int i = 0; i<count ; i++) {
                        if(isMoreThan50)
                        {
                            sb.append(",").append(text[i]);
                        }
                        mData.add(new FlowDataBean(text[i]));
                    }
                    if(sb.length()>0)
                    {
                        SPUtils.getInstance().put(ConstantValue.searchHistory,sb.toString());
                    }
                    mBind.myFL.setTextList(mData);
                }

            }

            if(mData.size()==0)
            {
                mBind.llSearchHistory.setVisibility(View.GONE);
            }
            else
            {
                mBind.llSearchHistory.setVisibility(View.VISIBLE);
            }
        }
    }

    private SearchAnchorActivity getMainActivity()
    {
        return (SearchAnchorActivity)getActivity();
    }

    private void setGL()
    {
        mBind.gl.removeAllViews();
        int dip2_5=ScreenUtils.getDip2px(getContext(),2.5f);
        mBind.gl.setBackgroundColor(0xffffff);
        mBind.gl.setColumnCount(2);
        mBind.gl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBind.gl.setPadding(0,dip2_5*2,dip2_5*2,0);

        List<RoomWatchedHistoryBean> list=LocalWatchHistoryDao.getInstance().queryList();
        List<RoomListBean> listBeans =new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listBeans.add(RoomWatchedHistoryBean.convertBean(list.get(i)));
        }
        setGLData(listBeans);

    }


    public void setGLData(List<RoomListBean> listBeans)
    {
        if(listBeans==null || listBeans.size()==0)
        {
            mBind.llRecentWatchedHistory.setVisibility(View.GONE);
            footer.setBotTextVisible(false);
        }
        else
        {
            mBind.llRecentWatchedHistory.setVisibility(View.VISIBLE);
            footer.setBotTextVisible(true);
        }

        int dip2_5=ScreenUtils.getDip2px(getContext(),2.5f);
        int dip13=ScreenUtils.getDip2px(getContext(),13);
        int defaultDrawable=R.mipmap.icon_anchor_loading;
        mBind.gl.removeAllViews();
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
                GlideUtils.loadRoundedImage(getContext(), dip2_5*4,data.getRoomIcon(),defaultDrawable,defaultDrawable, ivRoundBG);
            }            name.setText(listBeans.get(i).getTitle());

            tvNum.setText(data.getLiveSum()+"");

            SpanUtils spUtils=new SpanUtils();
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
                    gtvUnitPrice.setSolidBackground(0x4c000000,ScreenUtils.getDip2px(getContext(),10));
                    spUtils.appendImage(ImageUtils.scale(clock, dip13, dip13),SpanUtils.ALIGN_BASELINE);
                    spUtils.append(" ").append(data.getRoomPrice()).append(" ");

                    spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_BASELINE);
                    spUtils.append(getContext().getString(R.string.unitPriceMin));
                    gtvUnitPrice.setText(spUtils.create());
                    gtvUnitPrice.setVisibility(View.VISIBLE);
                    gtvUnitPrice.setSolidBackground(0x4c000000,ScreenUtils.getDip2px(getContext(),7.5f));

                    tvAnchorPaymentType.setVisibility(View.VISIBLE);
                    tvAnchorPaymentType.setText(getContext().getString(R.string.charge_on_time));
                    tvAnchorPaymentType.setSolidBackground(0x4c000000,ScreenUtils.getDip2px(getContext(),7.5f));

                    break;
                case 5:
                    gtvUnitPrice.setSolidBackground(0x4cBF003A,ScreenUtils.getDip2px(getContext(),10));

                    gtvUnitPrice.setSolidBackground(0x4c000000,ScreenUtils.getDip2px(getContext(),10));
                    spUtils.appendImage(ImageUtils.scale(ticket, dip13, dip13),SpanUtils.ALIGN_BASELINE);
                    spUtils.append(" ").append(data.getRoomPrice()).append(" ");

                    spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_BASELINE);
                    spUtils.append(getContext().getResources().getString(R.string.unitPriceMin));
                    gtvUnitPrice.setText(spUtils.create());
                    gtvUnitPrice.setVisibility(View.VISIBLE);
                    gtvUnitPrice.setSolidBackground(0x4cBF003A,ScreenUtils.getDip2px(getContext(),7.5f));

                    tvAnchorPaymentType.setVisibility(View.VISIBLE);
                    tvAnchorPaymentType.setText(getContext().getString(R.string.charge_per_site));
                    tvAnchorPaymentType.setSolidBackground(0x4cBF003A,ScreenUtils.getDip2px(getContext(),7.5f));

                    break;
                default:
                    tvAnchorPaymentType.setVisibility(View.GONE);
                    gtvUnitPrice.setVisibility(View.GONE);
            }

            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
                        LoginModeSelActivity.startActivity(getContext());
                        return;
                    }
                    int tag=(Integer)view.getTag();
                    LivingActivity.startActivity(getContext(), listBeans,tag);
                }
            });

            mBind.gl.addView(view,itemWidth+dip2_5*2,itemWidth+dip2_5*2);
        }
    }

    public void getRecommendList() {
        Api_Live.ins().getRecommendLiveList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (!isActivityOK()) {
                    return;
                }

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
                            footer.setData(listBeans);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
