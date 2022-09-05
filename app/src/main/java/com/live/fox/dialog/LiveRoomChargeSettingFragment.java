package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.utils.LogUtils;


/**
 * 房间收费模式设置
 */
public class LiveRoomChargeSettingFragment extends DialogFragment {

    RelativeLayout roomtype1Layout;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;

    int roomTyep;

    boolean isShow = true;


    public static LiveRoomChargeSettingFragment newInstance(int roomTyep) {
        LiveRoomChargeSettingFragment fragment = new LiveRoomChargeSettingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("roomTyep", roomTyep);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            roomTyep = bundle.getInt("roomTyep", 0);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.preview_roomtypeset);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        initView(dialog);

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);

        return dialog;
    }


    private void initView(Dialog dialog) {
        isShow = true;
        roomtype1Layout = dialog.findViewById(R.id.layout_roomtype1);
        rb1 = dialog.findViewById(R.id.rb_type1);
        rb2 = dialog.findViewById(R.id.rb_type2);
        rb3 = dialog.findViewById(R.id.rb_type3);
        rb4 = dialog.findViewById(R.id.rb_type4);

        showPage(1);
        setSelRb(roomTyep);

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(btnClick!=null){
                    btnClick.onClick(view.getId());
                }
            }
        });
        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(btnClick!=null){
                    btnClick.onClick(view.getId());
                }
            }
        });
        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(btnClick!=null){
                    btnClick.onClick(view.getId());
                }
            }
        });
        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(btnClick!=null){
                    btnClick.onClick(view.getId());
                }
            }
        });
    }


    public void showPage(int pageType){
        roomtype1Layout.setVisibility(View.GONE);
        switch (pageType) {
            case 1:
                roomtype1Layout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setSelRb(int sel){
        switch (sel) {
            case 0:
                rb1.setChecked(true);
                break;
            case 1:
                rb2.setChecked(true);
                break;
            case 2:
                rb3.setChecked(true);
                break;
            case 3:
                rb4.setChecked(true);
                break;
        }
    }


    public boolean isShow() {
        return isShow;
    }



    @Override
    public void dismiss() {
        LogUtils.e("dismiss");
        isShow = false;
        super.dismiss();
    }

    OnBtnClick btnClick;

    public void setBtnClick(OnBtnClick btnClick) {
        this.btnClick = btnClick;
    }


    public interface OnBtnClick {
        void onClick(int id);
    }

}
