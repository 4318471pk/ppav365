package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityAgencySaveBinding;

public class AgencySaveActivity extends BaseBindingViewActivity {

    ActivityAgencySaveBinding mBind;


    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, AgencySaveActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_agency_save;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});
        setTipsText();
        mBind.tvRecord.setOnClickListener(view -> AgencySaveRecordActivity.startActivity(this));
    }


    private void setTipsText(){
        String all = getString(R.string.save_tips_2);
        SpannableString spannableString = new SpannableString(all);
        String txt = getString(R.string.agency_canter_my_card);
        if (all.contains(txt)) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color0F86FF));
            spannableString.setSpan(colorSpan, all.indexOf(txt) , all.indexOf(txt) + txt.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE );
            UnderlineSpan underlineSpan = new UnderlineSpan();
            spannableString.setSpan(underlineSpan, all.indexOf(txt), all.indexOf(txt)+ txt.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE );
        }

        mBind.tvTips.setText(spannableString);
        //mBind.tvTips.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
