package com.live.fox.dialog.bottomDialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogSetroomTypeBinding;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.Strings;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetRoomTypeDialog extends BaseBindingDialogFragment {

    final String PricePerHour="PricePerHour";
    final String PricePerShow="PricePerShow";
    final String RoomType="RoomType";

    DialogSetroomTypeBinding mBind;
    List<RadioButton> radioButtons = new ArrayList<>();
    boolean hasSwitchButton = false;
    OnSelectRoomTypeListener onSelectRoomTypeListener;
    String liveId;
    int type[]={1,2,0};

    public static SetRoomTypeDialog getInstance(boolean hasSwitchButton,String liveId) {
        SetRoomTypeDialog dialog = new SetRoomTypeDialog();
        dialog.hasSwitchButton = hasSwitchButton;
        dialog.liveId=liveId;
        return dialog;
    }

    public void setOnSelectRoomTypeListener(OnSelectRoomTypeListener onSelectRoomTypeListener) {
        this.onSelectRoomTypeListener = onSelectRoomTypeListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(onSelectRoomTypeListener!=null)
        {
            int index=-1;
            int price=0;
            for (int j = 0; j < radioButtons.size(); j++) {
                if(radioButtons.get(j).isChecked())
                {
                    index=j;
                }
            }

            if(index==0 || index==1)
            {
                price=Integer.valueOf(getPriceEditText(index).getText().toString());
            }

            SPUtils.getInstance().put(PricePerShow,getPriceEditText(1).getText().toString());
            SPUtils.getInstance().put(PricePerHour,getPriceEditText(0).getText().toString());
            SPUtils.getInstance().put(RoomType,String.valueOf(type[index]));
            onSelectRoomTypeListener.onSelect(liveId,type[index],price);
        }
    }

    private EditText getPriceEditText(int index)
    {
        LinearLayout linearLayout=(LinearLayout) mBind.llRadioList.getChildAt(index);
        LinearLayout content=(LinearLayout) linearLayout.getChildAt(1);
        EditText editText=(EditText) content.getChildAt(1);
        return editText;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

//        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.rllContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.rllContent, false);
                break;
            case R.id.gtvConfirmSwitch:

                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_setroom_type;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);

        int screeWidth = ScreenUtils.getScreenWidth(getActivity());

        mBind.gtvConfirmSwitch.setVisibility(hasSwitchButton ? View.VISIBLE : View.GONE);
        startAnimate(mBind.rllContent, true);

        for (int i = 0; i < mBind.llRadioList.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) mBind.llRadioList.getChildAt(i);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            ll.leftMargin = (int) (screeWidth * 0.1f);
            linearLayout.setLayoutParams(ll);

            RadioButton radioButton = (RadioButton) linearLayout.getChildAt(0);
            LinearLayout child = (LinearLayout) linearLayout.getChildAt(1);
            LinearLayout.LayoutParams llChild = (LinearLayout.LayoutParams) child.getLayoutParams();
            llChild.leftMargin = (int) (screeWidth * 0.08f);
            child.setLayoutParams(llChild);

            radioButtons.add(radioButton);
            linearLayout.setTag(i);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();
                    for (int j = 0; j < radioButtons.size(); j++) {
                        radioButtons.get(j).setChecked(index == j);
                    }
                }
            });
        }

        radioButtons.get(0).setChecked(true);

        String pph=SPUtils.getInstance().getString(PricePerHour,"");
        String pps=SPUtils.getInstance().getString(PricePerShow,"");
        String roomType=SPUtils.getInstance().getString(RoomType,"");
        if(Strings.isDigitOnly(pph))
        {
            getPriceEditText(0).setText(pph);
        }

        if(Strings.isDigitOnly(pps))
        {
            getPriceEditText(1).setText(pps);
        }

        if(Strings.isDigitOnly(roomType))
        {
            for (int j = 0; j < radioButtons.size(); j++) {
                radioButtons.get(j).setChecked(false);
            }
            radioButtons.get(Integer.valueOf(roomType)).setChecked(true);
        }

    }

    public interface OnSelectRoomTypeListener {
        void onSelect(String liveId,int type,int price);
    }
}
