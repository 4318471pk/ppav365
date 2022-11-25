package com.live.fox.dialog.bottomDialog.livingRoomBlockAndMute;

import android.view.View;
import android.view.animation.Animation;

import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogBlockMuteListBinding;
import com.live.fox.dialog.bottomDialog.AudienceManagerDialog;
import com.live.fox.utils.ScreenUtils;

public class LivingRoomBlockAndMuteListDialog extends BaseBindingDialogFragment {

    DialogBlockMuteListBinding mBind;
    String liveId;

    public static LivingRoomBlockAndMuteListDialog getInstance(String liveId)
    {
        LivingRoomBlockAndMuteListDialog dialog=new LivingRoomBlockAndMuteListDialog();
        dialog.liveId=liveId;
        return dialog;
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.llContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.llContent,false);
                break;
            case R.id.ivClose:
                openAnotherDialog();
                break;
        }
    }


    private void openAnotherDialog()
    {
        startAnimate(mBind.llContent, false, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AudienceManagerDialog audienceManagerDialog = AudienceManagerDialog.getInstance(liveId);
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getParentFragmentManager(), audienceManagerDialog);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_block_mute_list;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        int screeHeight= ScreenUtils.getScreenHeight(getActivity());
        mBind.llContent.getLayoutParams().height=(int)(screeHeight*0.56f);
        int titles[]={R.string.black,R.string.muteSendingMessage};
        mBind.vpMain.setOffscreenPageLimit(1);
        mBind.vpMain.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getFragment(int position) {

                switch (position)
                {
                    case 0:
                        return BlockListFragment.getInstance(liveId);
                    case 1:
                        return MuteListFragment.getInstance(liveId);
                }

                return null;
            }

            @Override
            public String getTitle(int position) {
                return getStringWithoutContext(titles[position]);
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
        mBind.tabLayout.setGradient(0xffA800FF,0xffEA00FF);
        int screenWidth=ScreenUtils.getScreenWidth(getActivity());
        int dip95=ScreenUtils.dp2px(getActivity(),95);
        mBind.tabLayout.setTabWidthPX((screenWidth-dip95)/2);
        mBind.tabLayout.setViewPager(mBind.vpMain);

        view.setVisibility(View.VISIBLE);

        startAnimate(mBind.llContent,true);
    }

}
