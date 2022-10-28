package com.live.fox.dialog.temple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTempleRoundBtnsBinding;
import com.live.fox.utils.SpanUtils;

public class FreeRoomToPrepaidRoomDialog extends TempleDialog2 {


    public static FreeRoomToPrepaidRoomDialog getInstance()
    {
        return new FreeRoomToPrepaidRoomDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gtCancel:
                break;
            case R.id.gtCommit:
                break;
        }
        dismissAllowingStateLoss();
    }



    @Override
    public void initView(View view) {

        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(getResources().getString(R.string.dialogText1));
        Bitmap diamond = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_diamond);
        spanUtils.append("\n999").appendImage(diamond,SpanUtils.ALIGN_BASELINE);
        spanUtils.append(getResources().getString(R.string.dialogText2));
        mBind.tvContent.setText(spanUtils.create());

    }
}
