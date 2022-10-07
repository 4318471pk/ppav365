package com.live.fox.dialog.bottomdialog;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogPurchaseCarBinding;
import com.live.fox.dialog.temple.TempleDialog;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.SpanUtils;

public class PurchaseCarDialog extends BaseBindingDialogFragment {

    public static final int purchase=0;
    public static final int renew=1;

    DialogPurchaseCarBinding mBind;
    int status;

    public static PurchaseCarDialog getInstance(int status)
    {
        PurchaseCarDialog purchaseCarDialog=new PurchaseCarDialog();
        purchaseCarDialog.status=status;
        return purchaseCarDialog;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                dismissAllowingStateLoss();
                break;
            case R.id.gtvCommit:
                openAlert();
                dismissAllowingStateLoss();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_purchase_car;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        if(status==purchase)
        {
            mBind.gtvCommit.setColors(getResources().getIntArray(R.array.myBagBuyColor));
            mBind.gtvCommit.setText(getResources().getString(R.string.confirmPurchase));
        }
        else if(status==renew)
        {
            mBind.gtvCommit.setColors(getResources().getIntArray(R.array.confirmRenew));
            mBind.gtvCommit.setText(getResources().getString(R.string.confirmRenew));
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

    private void openAlert()
    {
        TempleDialog templeDialog=TempleDialog.getInstance();
        templeDialog.setOnCreateDialogListener(new TempleDialog.OnCreateDialogListener() {
            @Override
            public void onCreate(TempleDialog dialog) {
                dialog.getBind().tvTitle.setText(getString(R.string.dialog_words));
                dialog.getBind().gtCancel.setText(getString(R.string.cancel));
                SpanUtils spanUtils=new SpanUtils();
                spanUtils.append(getString(R.string.confirmSpend));
                spanUtils.append("18888").setForegroundColor(0xffF42C2C);
                spanUtils.appendImage(getResources().getDrawable(R.mipmap.icon_diamond),SpanUtils.ALIGN_BASELINE);

                if(status==purchase)
                {
                    dialog.getBind().gtCommit.setText(getString(R.string.confirm));
                    spanUtils.append(getString(R.string.buy));
                }
                else if(status==renew)
                {
                    dialog.getBind().gtCommit.setText(getString(R.string.renew));
                    spanUtils.append(getString(R.string.renew)).append(getString(R.string.buy));
                }
                spanUtils.append("\n");
                spanUtils.append("布加迪威龙").setForegroundColor(0xffF42C2C);
                spanUtils.append("30天吗?");
                dialog.getBind().tvContent.setText(spanUtils.create());

                dialog.getBind().gtCancel.setOnClickListener(new OnClickFrequentlyListener() {
                    @Override
                    public void onClickView(View view) {
                        dialog.dismissAllowingStateLoss();
                    }
                });
                dialog.getBind().gtCommit.setOnClickListener(new OnClickFrequentlyListener() {
                    @Override
                    public void onClickView(View view) {
                        dialog.dismissAllowingStateLoss();
                    }
                });
            }
        });

        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getParentFragmentManager(),templeDialog);
    }
}
