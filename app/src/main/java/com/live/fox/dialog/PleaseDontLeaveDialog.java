package com.live.fox.dialog;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogPlzDontLeaveBinding;
import com.live.fox.utils.device.ScreenUtils;

public class PleaseDontLeaveDialog extends BaseBindingDialogFragment {

    DialogPlzDontLeaveBinding mBind;
    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_plz_dont_leave;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        int screenWidth= ScreenUtils.getScreenWidth(getActivity());
        mBind.llContent.getLayoutParams().width=(int)(screenWidth*0.75f);

    }

}
