package com.live.fox.ui.openLiving;

import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.FragmentPreparingLivingBinding;
import com.live.fox.dialog.bottomDialog.EditLivingGameTypeDialog;
import com.live.fox.dialog.bottomDialog.EditProfileImageDialog;
import com.live.fox.dialog.bottomDialog.SetLocationDialog;
import com.live.fox.utils.device.ScreenUtils;

public class PreparingLivingFragment extends BaseBindingFragment {

    FragmentPreparingLivingBinding mBind;
    int iconArray[]={R.mipmap.icon_cameral_change,R.mipmap.icon_beaty_effect,
            R.mipmap.icon_gametype,R.mipmap.icon_advantage,R.mipmap.icon_roomtype};

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.llBotView:
                hideKeyBoard(getView());
                break;
            case R.id.ivClose:
                getActivity().finish();
                break;
            case R.id.gtvStartLiving:

                break;
            case R.id.tvLocation:
                SetLocationDialog setLocationDialog= SetLocationDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),setLocationDialog);
                break;
            case R.id.ivRoomPic:
                EditProfileImageDialog dialog= EditProfileImageDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),dialog);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_preparing_living;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        int screenWidth= ScreenUtils.getScreenWidth(getContext());
        int screenHeight=ScreenUtils.getScreenHeight(getContext());

        view.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) mBind.rlContent.getLayoutParams();
        rl.width=(int)(screenWidth*0.8f);
        rl.leftMargin=(int)(screenWidth*0.1f);
        rl.rightMargin=(int)(screenWidth*0.1f);
        mBind.rlContent.setLayoutParams(rl);

        RelativeLayout.LayoutParams rlBot=(RelativeLayout.LayoutParams) mBind.llBotView.getLayoutParams();
        rlBot.width=(int)(screenWidth*0.8f);
        rlBot.leftMargin=(int)(screenWidth*0.1f);
        rlBot.rightMargin=(int)(screenWidth*0.1f);
        mBind.llBotView.setLayoutParams(rlBot);

        RelativeLayout.LayoutParams rlButtons=(RelativeLayout.LayoutParams) mBind.llButtons.getLayoutParams();
        int botMargin=(int)(screenHeight*0.141f);
        rlButtons.bottomMargin=botMargin+ScreenUtils.getDip2px(getContext(),70);
        mBind.llButtons.setLayoutParams(rlButtons);

        String buttonTitles[]=getResources().getStringArray(R.array.startLivingTitles);

        int padding=ScreenUtils.getDip2px(getContext(),5);
        for (int i = 0; i < buttonTitles.length; i++) {
            TextView textView=new TextView(getActivity());
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.weight=1;
            textView.setLayoutParams(ll);
            textView.setTextColor(0xffffffff);
            textView.setText(buttonTitles[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setCompoundDrawablePadding(padding*2);
            Drawable drawable=getResources().getDrawable(iconArray[i]);
            textView.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
            mBind.llButtons.addView(textView);
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index=(int)view.getTag();
                    switch (index)
                    {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            EditLivingGameTypeDialog editLivingGameTypeDialog= EditLivingGameTypeDialog.getInstance();
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),editLivingGameTypeDialog);
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                    }
                }
            });
        }

        view.setVisibility(View.VISIBLE);
    }


}
