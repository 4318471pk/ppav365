package com.live.fox.dialog.temple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.live.fox.R;
import com.live.fox.utils.SpanUtils;

public class GoAndOnlookerDialog extends TempleDialog2 {


    public static GoAndOnlookerDialog getInstance()
    {
        return new GoAndOnlookerDialog();
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

        mBind.tvTitle.setVisibility(View.GONE);
        mBind.tvContent.setText(getResources().getString(R.string.goToAnotherRoom));

    }



}
