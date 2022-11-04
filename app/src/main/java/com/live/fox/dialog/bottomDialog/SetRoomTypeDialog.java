package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogSetroomTypeBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetRoomTypeDialog extends BaseBindingDialogFragment {

    DialogSetroomTypeBinding mBind;
    List<TextView> textViews;
    boolean hasSwitchButton=false;
    OnSelectRoomTypeListener onSelectRoomTypeListener;

    public static SetRoomTypeDialog  getInstance(boolean hasSwitchButton)
    {
        SetRoomTypeDialog dialog=new SetRoomTypeDialog();
        dialog.hasSwitchButton=hasSwitchButton;
        return dialog;
    }

    public void setOnSelectRoomTypeListener(OnSelectRoomTypeListener onSelectRoomTypeListener) {
        this.onSelectRoomTypeListener = onSelectRoomTypeListener;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

//        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        setWindowsFlag();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                startAnimate(mBind.rllContent,false);
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
        mBind=getViewDataBinding();
        mBind.setClick(this);

        mBind.gtvConfirmSwitch.setVisibility(hasSwitchButton?View.VISIBLE:View.GONE);
        startAnimate(mBind.rllContent,true);

        for (int i = 0; i < mBind.llRadioList.getChildCount(); i++) {
          LinearLayout linearLayout=(LinearLayout) mBind.llRadioList.getChildAt(i);
          TextView textView=(TextView) linearLayout.getChildAt(0);
          textViews.add((TextView) linearLayout.getChildAt(0));
          textView.setTag(i);
          textView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  int index=(int)view.getTag();
                  for (int j = 0; j < textViews.size(); j++) {
                      textViews.get(j).setSelected(index==j);
                  }
              }
          });
        }
    }

    public interface OnSelectRoomTypeListener
    {
        void onSelect(int position);
    }
}
