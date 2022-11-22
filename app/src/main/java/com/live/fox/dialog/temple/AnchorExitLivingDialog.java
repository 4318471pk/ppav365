package com.live.fox.dialog.temple;

import android.content.Intent;
import android.view.View;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.openLiving.AnchorLivingFinishActivity;
import com.live.fox.ui.openLiving.OpenLivingActivity;
import com.live.fox.ui.openLiving.StartLivingFragment;
import com.live.fox.utils.SpanUtils;

public class AnchorExitLivingDialog extends TempleDialog2{



    public static AnchorExitLivingDialog getInstance()
    {
        AnchorExitLivingDialog dialog=new AnchorExitLivingDialog();
        return dialog;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.gtCancel:
                dismissAllowingStateLoss();
                break;
            case R.id.gtCommit:
                dismissAllowingStateLoss();
                if(getActivity() instanceof OpenLivingActivity)
                {

                    OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
                    openLivingActivity.onAnchorExitLiving();

                    AnchorLivingFinishActivity.startActivity(openLivingActivity,openLivingActivity.liveId,false);
                }
                break;
        }

    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mBind.tvTitle.setText(getStringWithoutContext(R.string.dialog_words));
        mBind.gtCancel.setText(getStringWithoutContext(R.string.cancel));
        mBind.gtCommit.setText(getStringWithoutContext(R.string.confirm));


        if(getParentFragment()!=null && (getParentFragment() instanceof StartLivingFragment))
        {
            StartLivingFragment startLivingFragment=(StartLivingFragment)getParentFragment();
            if(Integer.valueOf(startLivingFragment.getOnlineAudAmount())>0)
            {
                SpanUtils spanUtils=new SpanUtils();
                spanUtils.append(startLivingFragment.mBind.gtvOnlineAmount.getText().toString()).setForegroundColor(0xffF42C2C);
                spanUtils.append(getStringWithoutContext(R.string.offLivingContent2)).setForegroundColor(0xffffffff);
                mBind.tvContent.setText(spanUtils.create());
            }
            else
            {
                mBind.tvContent.setText(getStringWithoutContext(R.string.offLivingContent));
            }
        }
    }

}
