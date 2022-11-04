package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityMylevelBinding;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;

import java.util.HashMap;

public class MyLevelActivity extends BaseBindingViewActivity {

    ActivityMylevelBinding mBind;

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,MyLevelActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_mylevel;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(getResources().getString(R.string.my_level));
        int screenWidth= ScreenUtils.getScreenWidth(this);
        mBind.rlmain.setVisibility(View.INVISIBLE);
        FixImageSize.setImageSizeOnWidthWithSRC(mBind.ivBG, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {
                RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) mBind.tvLv.getLayoutParams();
                rl.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                rl.topMargin=(int)(height*0.238f);
                mBind.tvLv.setLayoutParams(rl);

                RelativeLayout.LayoutParams rlProgress=(RelativeLayout.LayoutParams)mBind.llProgress.getLayoutParams();
                rlProgress.topMargin=(int)(height*0.35f);
                rlProgress.width=(int)(width*0.8f);
                rlProgress.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                mBind.llProgress.setLayoutParams(rlProgress);

                String ex=getResources().getString(R.string.experienceValueNeeds);
                ex=String.format(ex,"50");
                mBind.tvExperienceNeeds.setText(ex);

                mBind.rlmain.setVisibility(View.VISIBLE);
                setProgress(0.5f);
            }
        });

        getData();

    }

    //persent 0.0f to 1.0f
    private void setProgress(float persent)
    {
        int screenWidth= ScreenUtils.getScreenWidth(this);
        int progressWidth=(int)(screenWidth*0.8f);
        mBind.floatingPoint.measure(0,0);
        int halfWidth=mBind.floatingPoint.getMeasuredWidth()/2;
        float point=progressWidth*persent-halfWidth;

        LinearLayout.LayoutParams rlProgress=(LinearLayout.LayoutParams)mBind.floatingPoint.getLayoutParams();
        rlProgress.leftMargin=(int)point;
        mBind.floatingPoint.setLayoutParams(rlProgress);

        mBind.lpv.setProgress(persent);

    }

    private void getData(){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        Api_Order.ins().getAssets(new JsonCallback<UserAssetsBean>() {
            @Override
            public void onSuccess(int code, String msg, UserAssetsBean data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    mBind.tvLv.setText("LV." + data.getUserLevel());
                    mBind.floatingPoint.setText(data.getUserExp()+ "");
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }


}
