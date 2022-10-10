package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityMsgDetailBinding;

/*
* 消息中心-消息详情
* */
public class MsgDetailActivity extends BaseBindingViewActivity {

    ActivityMsgDetailBinding mBind;
    private boolean isSys = true;


    public static void startActivity(Context context, boolean isSys) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MsgDetailActivity.class);
        i.putExtra("isSys", isSys);
        context.startActivity(i);

    }


    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        isSys =  this.getIntent().getBooleanExtra("isSys", isSys);
        mBind.iv.setImageDrawable(isSys? getResources().getDrawable(R.mipmap.announcement) : getResources().getDrawable(R.mipmap.email));

    }


}
