package com.live.fox.dialog.temple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTempleRoundBtnsBinding;
import com.live.fox.utils.SpanUtils;

public class TempleDialog2 extends BaseBindingDialogFragment {

    public DialogTempleRoundBtnsBinding mBind;
    OnCreateDialogListener onCreateDialogListener;

    public static TempleDialog2 getInstance()
    {
        return new TempleDialog2();
    }

    public void setOnCreateDialogListener(OnCreateDialogListener onCreateDialogListener) {
        this.onCreateDialogListener = onCreateDialogListener;
    }

    @Override
    public void onClickView(View view) {
        onClick(view);
    }

    public void onClick(View view){
        if (view == mBind.gtCancel) {
            if(onCreateDialogListener!=null)
            {
                onCreateDialogListener.clickCancel(this);
            }
        } else if (view  == mBind.gtCommit) {
            onCreateDialogListener.clickOk(this);
        }
        else if(view==mBind.ivClose)
        {
            if(onCreateDialogListener!=null)
            {
                onCreateDialogListener.clickClose(this);
            }
        }
    }

    @Override
    public int onCreateLayoutId() {
        mIsKeyCanback=false;
        mIsOutCanback=false;
        return R.layout.dialog_temple_round_btns;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        if(onCreateDialogListener!=null)
        {
            onCreateDialogListener.onCreate(this);
        }

    }

    public interface OnCreateDialogListener
    {
        void onCreate(TempleDialog2 dialog);
        void clickCancel(TempleDialog2 dialog);
        void clickOk(TempleDialog2 dialog);
        void clickClose(TempleDialog2 dialog);
    }
}
