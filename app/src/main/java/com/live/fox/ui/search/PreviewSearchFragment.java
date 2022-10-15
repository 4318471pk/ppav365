package com.live.fox.ui.search;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentPreviewSearchBinding;
import com.live.fox.entity.FlowDataBean;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.EmptyDataView;
import com.live.fox.view.GradientTextView;
import com.live.fox.view.RecommendAnchorListFooter;

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
                    mBind.llRecentWatchedHistory.setVisibility(View.VISIBLE);
                    mBind.llSearchHistory.setVisibility(View.VISIBLE);
                    mBind.llMain.removeView(emptyDataView);
                    footer.setBotTextVisible(true);
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
        mBind.llMain.addView(footer);

        setGL();
        setHistory();
    }

    private void setHistory()
    {
        int dip10=ScreenUtils.getDip2px(getActivity(),10);
        mBind.myFL.setHorizontalMargin(dip10);
        mBind.myFL.setVerticalMargin(dip10);
        mBind.myFL.setMaxLines(4);
        mBind.myFL.setTextMaxLength(20);

        List<FlowDataBean> mData = new ArrayList<>();
        mData.add(new FlowDataBean("阿是假的"));
        mData.add(new FlowDataBean("我气哦额我去哦额我去"));
        mData.add(new FlowDataBean("阿是的"));
        mData.add(new FlowDataBean("i我去恶意我去额"));
        mData.add(new FlowDataBean("阿是达拉斯空间的合理撒娇的拉萨剪刀手拉大距离撒娇了撒开多久啊深刻的哈萨克"));
        mData.add(new FlowDataBean("222撒娇了撒开多哈萨克"));
        mBind.myFL.setTextList(mData);


    }

    private void setGL()
    {
        mBind.gl.removeAllViews();
        int dip2_5=ScreenUtils.getDip2px(getContext(),2.5f);
        mBind.gl.setBackgroundColor(0xffffff);
        mBind.gl.setColumnCount(2);
        mBind.gl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBind.gl.setPadding(0,dip2_5*2,dip2_5*2,0);

        Drawable clock,diamond;
        clock=getContext().getResources().getDrawable(R.mipmap.icon_clock);
        diamond=getContext().getResources().getDrawable(R.mipmap.icon_diamond);
        int itemWidth= (ScreenUtils.getScreenWidth(getContext())-ScreenUtils.getDip2px(getContext(),15))/2;
        for (int i = 0; i < 6; i++) {
            View view=View.inflate(getContext(),R.layout.item_anchor_list,null);
            view.setPadding(dip2_5*2,dip2_5*2,0,0);

            GradientTextView gtvUnitPrice = view.findViewById(R.id.gtvUnitPrice);  //类别
            ImageView ivRoundBG = view.findViewById(R.id.ivRoundBG);

            SpanUtils spUtils=new SpanUtils();
            spUtils.appendImage(clock,SpanUtils.ALIGN_CENTER);
            spUtils.append(" 21 ").setAlign(Layout.Alignment.ALIGN_CENTER);
            spUtils.appendImage(diamond,SpanUtils.ALIGN_CENTER);
            spUtils.append("/分钟").setAlign(Layout.Alignment.ALIGN_CENTER);
            gtvUnitPrice.setText(spUtils.create());

            mBind.gl.addView(view,itemWidth+dip2_5*2,itemWidth+dip2_5*2);
        }
    }
}
