package com.live.fox.dialog.bottomdialog;

import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogSimpleSelectorBinding;
import com.live.fox.view.wheel.adapters.ArrayWheelAdapter;
import com.live.fox.view.wheel.adapters.ListWheelAdapter;

import java.util.List;

public class SimpleSelectorDialog extends BaseBindingDialogFragment {

    DialogSimpleSelectorBinding mBind;
    List<String> data;
    OnItemSelectedListener onItemSelectedListener;
    String title;

    public static SimpleSelectorDialog getInstance(OnItemSelectedListener onItemSelectedListener)
    {
        SimpleSelectorDialog simpleSelectorDialog=new SimpleSelectorDialog();
        simpleSelectorDialog.onItemSelectedListener=onItemSelectedListener;
        return simpleSelectorDialog;
    }

    public void setData(List<String> data){
        this.data=data;
    }

    @Override
    public void onClickView(View view) {

        switch (view.getId())
        {
            case R.id.rlMain:
            case R.id.tvCancel:
                dismissAllowingStateLoss();
                break;
            case R.id.tvConfirm:
                if(onItemSelectedListener!=null)
                {
                    onItemSelectedListener.onItemSelected(mBind.wheel.getCurrentItem());
                }
                break;
        }


    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_simple_selector;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        mBind.wheel.setCyclic(false);
        mBind.wheel.setVisibleItems(3);

//        mBind.wheel.setDividerType(WheelView.DividerType.WRAP);
        mBind.wheel.setViewAdapter(new ListWheelAdapter<String>(getContext(),data));

        startAnimate();
        if(!TextUtils.isEmpty(title))
        {
            mBind.tvTitle.setText(title);
        }
    }


    public void startAnimate(){

        Animation animation= new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0
                ,Animation.RELATIVE_TO_PARENT,1f
                ,Animation.RELATIVE_TO_PARENT,0f);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBind.rllMain.startAnimation(animation);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 0x88);
        //开始延时时长
        valueAnimator.setStartDelay(0);
        //动画时长
        valueAnimator.setDuration(300);
        //重复次数
        valueAnimator.setRepeatCount(0);
        //设置重复模式 ValueAnimator.RESTART正序重新开始  ValueAnimator.REVERSE逆序重新开始
//        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取当前值
                int number = (int) animation.getAnimatedValue();
                mBind.rlMain.setBackgroundColor(number<<24);
            }
        });
        //开启动画
        valueAnimator.start();
    }

    public void setTitle(String text)
    {
      title=text;
    }

    public interface OnItemSelectedListener
    {
        void onItemSelected(int index);
    }
}
