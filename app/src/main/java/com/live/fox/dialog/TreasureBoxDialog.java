package com.live.fox.dialog;

import android.view.View;
import android.widget.RelativeLayout;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTreasureBoxBinding;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.ScreenUtils;

//包含礼物特权背包
public class TreasureBoxDialog extends BaseBindingDialogFragment {

    DialogTreasureBoxBinding mBind;

    public static TreasureBoxDialog getInstance()
    {
        return new TreasureBoxDialog();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                startAnimate(mBind.rrlContent,false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_treasure_box;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        int screenWidth= ScreenUtils.getScreenWidth(getActivity());
        int screenHeight=ScreenUtils.getScreenHeight(getActivity());
        FixImageSize.setImageSizeOnWidthWithSRC(mBind.ivFirstTimeTopUp, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {
                RelativeLayout.LayoutParams rlContent=(RelativeLayout.LayoutParams)mBind.rrlContent.getLayoutParams();
                rlContent.topMargin=height/2;
                rlContent.height=(int)(screenHeight*0.69f);
                mBind.rrlContent.setLayoutParams(rlContent);

                view.setVisibility(View.VISIBLE);
            }
        });

        startAnimate(mBind.rrlContent,true);
    }
}
