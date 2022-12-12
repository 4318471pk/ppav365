package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityMylevelBinding;
import com.live.fox.entity.User;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Order;
import com.live.fox.server.Api_User;
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
        setWindowsFlag();
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

                mBind.rlmain.setVisibility(View.VISIBLE);

            }
        });

        User user=DataCenter.getInstance().getUserInfo().getUser();
        mBind.tvLv.setText("LV." + user.getUserLevel());

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
        Api_User.ins().getUserInfo(-1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                    if(isFinishing() || isDestroyed())
                    {
                        return;
                    }

                User user=DataCenter.getInstance().getUserInfo().getUser();
                mBind.tvLv.setText("LV." + user.getUserLevel());
                mBind.floatingPoint.setText(user.getUserExp()+ "");
                String ex=getResources().getString(R.string.experienceValueNeeds);
                mBind.tvExperienceNeeds.setText(String.format(ex,user.getNeedExp()+""));

                if(user.getUserExp()>0 && user.getNeedExp()>0)
                {
                    float progress=1.0f*user.getUserExp()/(user.getUserExp()+user.getNeedExp());
                    setProgress(progress);
                }
            }
        });

//        Api_Order.ins().getAssets(new JsonCallback<UserAssetsBean>() {
//            @Override
//            public void onSuccess(int code, String msg, UserAssetsBean data) {
//                if(isFinishing() || isDestroyed())
//                {
//                    return;
//                }
//                hideLoadingDialog();
//                if (code == 0 ) {
//                    mBind.tvLv.setText("LV." + data.getUserLevel());
//                    mBind.floatingPoint.setText(data.getUserExp()+ "");
//                } else {
//                    ToastUtils.showShort(msg);
//                }
//            }
//        }, commonParams);
    }


}
