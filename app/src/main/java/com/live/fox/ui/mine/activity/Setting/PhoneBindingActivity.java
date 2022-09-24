package com.live.fox.ui.mine.activity.Setting;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityPhoneBindingBinding;
import com.live.fox.entity.CountryCode;
import com.live.fox.entity.RegisterEntity;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Auth;
import com.live.fox.ui.login.LoginViewModel;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.DropDownWindowsOfCountry;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;


public class PhoneBindingActivity extends BaseBindingViewActivity {

    ActivityPhoneBindingBinding mBind;
    int remainSecond=60;
    List<CountryCode> countryCodes;
    DropDownWindowsOfCountry dropDownWindowsOfCountry;
    Handler handler = new Handler(Looper.myLooper()) {

        @Override
        public void dispatchMessage(@NonNull @NotNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    remainSecond--;
                    if (remainSecond > 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(String.valueOf(remainSecond)).append("s").append(getString(R.string.reGet));
                        mBind.tvSendVerifyCode.setText(sb.toString());
                        mBind.tvSendVerifyCode.setEnabled(false);
                        sendEmptyMessageDelayed(0, 1000);
                    } else {
                        remainSecond = 60;
                        mBind.tvSendVerifyCode.setText(getString(R.string.get_verification_code));
                        mBind.tvSendVerifyCode.setEnabled(true);
                    }
                    break;
            }
        }
    };

    @Override
    public void onClickView(View view)
    {
        if(ClickUtil.isClickWithShortTime(view.getId(),700))
        {
            return;
        }

        switch (view.getId())
        {
            case R.id.gtCommit:
                guestBindPhone();
                break;
            case R.id.tvSendVerifyCode:
                doSendPhoneCodeApi();
                break;
            case R.id.tvCountryCode:
                onClickCountryCode();
                break;
        }
    }

    private void onClickCountryCode()
    {
        if(countryCodes==null || countryCodes.size()==0)
        {
            ToastUtils.showShort(getResources().getString(R.string.downloadingError));
            return;
        }

        if(dropDownWindowsOfCountry==null)
        {
            dropDownWindowsOfCountry=new DropDownWindowsOfCountry(this);
            dropDownWindowsOfCountry.setOutsideTouchable(true);
            dropDownWindowsOfCountry.setFocusable(true);
            dropDownWindowsOfCountry.setBackgroundDrawable(new ColorDrawable(0));
            dropDownWindowsOfCountry.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
            dropDownWindowsOfCountry.setOnClickItemListener(new DropDownWindowsOfCountry.OnClickItemListener() {
                @Override
                public void onClickItemListener(CountryCode countryCode) {
                    dropDownWindowsOfCountry.dismiss();
                    mBind.tvCountryCode.setText(countryCode.getAreaCode());
                    mBind.tvCountryCode.setTag(countryCode);
                }
            });
        }
        dropDownWindowsOfCountry.setData(countryCodes);

        if(!dropDownWindowsOfCountry.isShowing())
        {
            dropDownWindowsOfCountry.showAsDropDown(mBind.tvCountryCode,0,10);
        }
        else
        {
            dropDownWindowsOfCountry.dismiss();
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_phone_binding;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(R.string.phoneNumberVerify);

        countryCodes= SPManager.getCountryCode();
        FixImageSize.setImageSizeOnWidthWithSRC(mBind.iconTopPhoneBind,getScaleWidth(0.4f));
        FixImageSize.setImageSizeOnWidthWithSRC(mBind.icoTopPhoneMid,getScaleWidth(0.07f));
        mBind.tvSendVerifyCode.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mBind.tvSendVerifyCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        for (int i = 0; i <mBind.llTips.getChildCount() ; i++) {
            LinearLayout item=(LinearLayout) mBind.llTips.getChildAt(i);
            ImageView imageView=(ImageView) item.getChildAt(0);
            TextView text=(TextView) item.getChildAt(1);
            text.setTextColor(0xff646464);
            FixImageSize.setImageSizeOnWidthWithSRC(imageView,getScaleWidth(0.133f));
            SpannableString spannableString=new SpannableString(text.getText().toString());
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),0,spannableString.length()-5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(0xffB8B2C8),spannableString.length()-5,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setText(spannableString);
        }

        if(countryCodes==null || countryCodes.size()==0)
        {
            getCountryCode();
        }
    }


    public void doSendPhoneCodeApi() {
        String phoneNum=mBind.tvPhoneNum.getText().toString();
        String area=mBind.tvCountryCode.getText().toString();
        if(TextUtils.isEmpty(phoneNum))
        {
            ToastUtils.showShort(getString(R.string.tipsPhoneNumFormatWrong));
            return;
        }
        showLoadingDialog();
        RegisterEntity registerEntity=new RegisterEntity();
        registerEntity.setName(phoneNum);
        registerEntity.setType("2");
        registerEntity.setVersion(AppUtils.getAppVersionName());
        registerEntity.setArea(area);
        Api_Auth.ins().sendPhoneCode(registerEntity, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                handler.sendEmptyMessage(0);
            }
        });
    }


    public void guestBindPhone() {
        String phoneNum=mBind.tvPhoneNum.getText().toString();
        String verifyCode=mBind.tvVerifyCode.getText().toString();
        String area=mBind.tvCountryCode.getText().toString();
        if(TextUtils.isEmpty(phoneNum))
        {
            ToastUtils.showShort(getString(R.string.tipsPhoneNumFormatWrong));
            return;
        }
        if(TextUtils.isEmpty(verifyCode))
        {
            ToastUtils.showShort(getString(R.string.tipsVerifyCodeFormatWrong));
            return;
        }
        showLoadingDialog();

        Api_Auth.ins().guestBindPhone(area,phoneNum, verifyCode,new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                Log.e("DDDDD",data);
                hideLoadingDialog();
                if(code==200)
                {
                    setResult(ConstantValue.GUEST_BINDPHONE);
                    mBind.icoTopPhoneMid.setImageDrawable(getResources().getDrawable(R.mipmap.icon_phone_bind_success));
                    mBind.rlContentMain.getChildAt(0).setVisibility(View.GONE);
                    mBind.rlContentMain.getChildAt(1).setVisibility(View.GONE);
                    mBind.tvBoundTitle.setVisibility(View.VISIBLE);

                    StringBuilder sb=new StringBuilder();
                    sb.append(getResources().getString(R.string.phoneBound));
                    int startLength=sb.length();
                    sb.append(area).append("  ").append(phoneNum);

                    SpannableString spannableString=new SpannableString(sb.toString());
                    spannableString.setSpan(new ForegroundColorSpan(0xff1FC478),startLength,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBind.tvBoundTitle.setText(spannableString);

                    ToastUtils.showShort(getString(R.string.bindPhoneSuccess));
                }
            }
        });
    }


    public void getCountryCode() {

        if(countryCodes==null || countryCodes.size()==0)
        {
            showLoadingDialogWithNoBgBlack();
        }

        Api_Auth.ins().countryCodeList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                try {
                    hideLoadingDialog();
                    Type type = new TypeToken<List<CountryCode>>() {}.getType();
                    countryCodes=new Gson().fromJson(data,type);
                    if(countryCodes!=null && countryCodes.size()>0)
                    {
                        SPManager.setCountryCode(data);
                    }

                } catch (Exception e) {
                    ToastUtils.showShort(e.getMessage());
                }
            }
        });
    }
}
