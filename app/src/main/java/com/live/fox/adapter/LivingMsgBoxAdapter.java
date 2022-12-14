package com.live.fox.adapter;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.utils.LinkClickMovementMethod;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.GradientTextView;

import java.util.List;

public class LivingMsgBoxAdapter extends RecyclerView.Adapter<LivingMsgBoxAdapter.LivingMsgBoxHolder> {

    Context context;
    List<LivingMsgBoxBean> beans;
    int dip1;
    int dip10_5;

    public LivingMsgBoxAdapter(Context context,List<LivingMsgBoxBean> beans) {
        this.context=context;
        dip1= ScreenUtils.getDip2px(context,1);
        dip10_5= ScreenUtils.getDip2px(context,10.5f);
        this.beans=beans;
    }

    public List<LivingMsgBoxBean> getBeans() {
        return beans;
    }

    @NonNull
    @Override
    public LivingMsgBoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GradientTextView gradientTextView=new GradientTextView(context);
        gradientTextView.setPadding(dip1*5,dip1*5,dip1*5,dip1*5);
        gradientTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        gradientTextView.setMovementMethod(LinkClickMovementMethod.getInstance());
        gradientTextView.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
        gradientTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new LivingMsgBoxHolder(gradientTextView);
    }

    @Override
    public void onBindViewHolder(@NonNull LivingMsgBoxHolder holder, int position) {

//        if(beans.get(position).getStrokeColor()!=null)
//        {
//
//        }
//        else
//        {
//
//        }
        SpanUtils spanUtils=new SpanUtils();
        LivingMsgBoxBean bean=beans.get(position);
        int strokeColor=bean.getStrokeColor()==null?0:bean.getStrokeColor();
        int strokeWidth=bean.getStrokeColor()==null?0:1;
        int backgroundColor=bean.getBackgroundColor()==null?0:bean.getBackgroundColor();
        holder.gradientTextView.setStokeWithSolidBackground(strokeColor,strokeWidth,bean.getBackgroundColor(),dip10_5);

        holder.gradientTextView.setText(bean.getCharSequence());

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public static final class LivingMsgBoxHolder extends RecyclerView.ViewHolder
    {
        GradientTextView gradientTextView;
        public LivingMsgBoxHolder(@NonNull View itemView) {
            super(itemView);
            gradientTextView=(GradientTextView)itemView;
        }
    }


}
