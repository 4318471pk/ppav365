package com.live.fox.dialog.bottomDialog;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.hwangjr.rxbus.RxBus;
import com.live.fox.App;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogPurchaseCarBinding;
import com.live.fox.dialog.temple.TempleDialog;
import com.live.fox.entity.MyBagStoreListItemBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;

import java.util.HashMap;

public class PurchaseCarDialog extends BaseBindingDialogFragment {


    DialogPurchaseCarBinding mBind;
    int status;
    MyBagStoreListItemBean myBagStoreListItemBean;
    OnBuyVehicleSuccessListener onBuyVehicleSuccessListener;

    public static PurchaseCarDialog getInstance(int status, MyBagStoreListItemBean bean)
    {
        PurchaseCarDialog purchaseCarDialog=new PurchaseCarDialog();
        purchaseCarDialog.status=status;
        purchaseCarDialog.myBagStoreListItemBean = bean;
        return purchaseCarDialog;
    }

    public void setOnBuyVehicleSuccessListener(OnBuyVehicleSuccessListener onBuyVehicleSuccessListener) {
        this.onBuyVehicleSuccessListener = onBuyVehicleSuccessListener;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                dismissAllowingStateLoss();
                break;
            case R.id.gtvCommit:
                //openBuy();
               // buyCar();
                openAlert();
                //dismissAllowingStateLoss();
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

        mBind.tvName.setText(myBagStoreListItemBean.getName());
        GlideUtils.loadDefaultImage(this.getContext(), myBagStoreListItemBean.getLogUrl(),0,R.mipmap.img_error ,mBind.ivCar);

        mBind.tvMoneyBuy.setText(String.format(getString(R.string.buy_car_money), Strings.cutOff(myBagStoreListItemBean.getPrice(),0)));
        mBind.tvMoneyXu.setText(String.format(getString(R.string.xu_car_money),  Strings.cutOff(myBagStoreListItemBean.getPrice(),0)));

        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(String.format(getString(R.string.all), Strings.cutOff(myBagStoreListItemBean.getPrice(),0)));
        spanUtils.append(" ");
        spanUtils.appendImage(getResources().getDrawable(R.mipmap.icon_diamond),SpanUtils.ALIGN_BASELINE);

        mBind.tvAll.setText(spanUtils.create());


        if(!myBagStoreListItemBean.isHave())
        {
            mBind.gtvCommit.setColors(getResources().getIntArray(R.array.myBagBuyColor));
            mBind.gtvCommit.setText(getResources().getString(R.string.confirmPurchase));
        }
        else
        {
            mBind.gtvCommit.setColors(getResources().getIntArray(R.array.startingUseColor));
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
        //??????????????????
        valueAnimator.setStartDelay(0);
        //????????????
        valueAnimator.setDuration(300);
        //????????????
        valueAnimator.setRepeatCount(0);
        //?????????????????? ValueAnimator.RESTART??????????????????  ValueAnimator.REVERSE??????????????????
//        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //???????????????
                int number = (int) animation.getAnimatedValue();
                mBind.rlMain.setBackgroundColor(number<<24);
            }
        });
        //????????????
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
                spanUtils.append(Strings.cutOff(myBagStoreListItemBean.getPrice(),0)).setForegroundColor(0xffF42C2C);
                spanUtils.appendImage(getResources().getDrawable(R.mipmap.icon_diamond),SpanUtils.ALIGN_BASELINE);

                String confirm = getString(R.string.confirm);

                if(!myBagStoreListItemBean.isHave()) {
                    spanUtils.append(getString(R.string.buy));
                }
                else {
                    confirm = getString(R.string.renew);
                    spanUtils.append(getString(R.string.renew)).append(getString(R.string.buy));
                }

                spanUtils.append("\n");
                spanUtils.append(myBagStoreListItemBean.getName()).setForegroundColor(0xffF42C2C);
                spanUtils.append("30???????");
                dialog.getBind().tvContent.setText(spanUtils.create());

                dialog.getBind().gtCommit.setText(confirm);

            }

            @Override
            public void clickCancel(TempleDialog dialog) {
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void clickOk(TempleDialog dialog) {
                buyCar(dialog);
            }
        });

        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getFragmentManager(),templeDialog);
    }



    private void buyCar(TempleDialog dialog){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        commonParams.put("propId", myBagStoreListItemBean.getId());
        Api_Order.ins().buyCar(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(!isConditionOk())
                {
                    return;
                }
                if (code == 0 ) {
                    dialog.dismissAllowingStateLoss();
                    dismissAllowingStateLoss();
                    if (!myBagStoreListItemBean.isHave()) {
                        if(onBuyVehicleSuccessListener!=null)
                        {
                            onBuyVehicleSuccessListener.onBuySuccess();
                        }
                        ToastUtils.showShort(getStringWithoutContext(R.string.successfulOpening));


                        RxBus.get().post(ConstantValue.refreshUser,"1");
                    } else {
                        ToastUtils.showShort(App.getInstance().getString(R.string.xufei_suc));
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }

    public interface OnBuyVehicleSuccessListener
    {
        void onBuySuccess();
    }
}
