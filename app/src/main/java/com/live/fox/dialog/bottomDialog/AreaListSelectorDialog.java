package com.live.fox.dialog.bottomDialog;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogArealistSelectorBinding;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.wheel.OnWheelChangedListener;
import com.live.fox.view.wheel.WheelView;
import com.live.fox.view.wheel.adapters.ListWheelAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AreaListSelectorDialog extends BaseBindingDialogFragment {

    DialogArealistSelectorBinding mBind;

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
            case R.id.tvCancel:
                mBind.rlMain.setEnabled(false);
                dismissAllowingStateLoss();
                break;
            case R.id.tvConfirm:
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_arealist_selector;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        startAnimate();

        HashMap<String,List<String>> lists=new HashMap<>();
        List<String> strings=new ArrayList<>();
        strings.add("1123");
        strings.add("32432");
        strings.add("sdsad");
        strings.add("sadsa");
        strings.add("32xcxzc432");
        strings.add("sdsadsasad");
        List<String> strings2=new ArrayList<>();
        strings2.add("sad");
        strings2.add("231");
        strings2.add("sdssadsadad");
        strings2.add("sa213213dsa");
        strings2.add("31232xcxzc432");
        strings2.add("sad");
        lists.put("1111",strings);
        lists.put("2222",strings2);

        List<String> list1=new ArrayList<>();
        list1.add("1111");
        list1.add("2222");

        int dip45= ScreenUtils.getDip2px(getContext(),45);
        ListWheelAdapter listWheelAdapter1=new ListWheelAdapter(getContext(),list1);
        ListWheelAdapter listWheelAdapter2=new ListWheelAdapter(getContext(),lists.get(list1.get(0)));
        listWheelAdapter1.setTextSize(16);
        listWheelAdapter1.setItemHeight(dip45);
        listWheelAdapter2.setTextSize(16);
        listWheelAdapter2.setItemHeight(dip45);

        mBind.wheel1.setViewAdapter(listWheelAdapter1);
        mBind.wheel2.setViewAdapter(listWheelAdapter2);
        mBind.wheel1.setCyclic(true);
        mBind.wheel2.setCyclic(true);
        mBind.wheel1.setVisibleItems(3);
        mBind.wheel2.setVisibleItems(3);

        mBind.wheel1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                ListWheelAdapter listWheelAdapter2=new ListWheelAdapter(getContext(),lists.get(list1.get(newValue)));
                listWheelAdapter2.setTextSize(16);
                listWheelAdapter2.setItemHeight(dip45);
                mBind.wheel2.setViewAdapter(listWheelAdapter2);
            }
        });

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
}
