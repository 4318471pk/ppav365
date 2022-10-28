package com.live.fox.dialog.temple;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.dialog.bottomDialog.EditNickNameDialog;
import com.live.fox.utils.SpanUtils;

public class EditNickNameConfirmDialog extends TempleDialog{

    public static EditNickNameConfirmDialog getInstance()
    {
        return new EditNickNameConfirmDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.gtCancel:

                break;
            case R.id.gtCommit:
                DialogFramentManager.getInstance().showDialog(getActivity().getSupportFragmentManager(), EditNickNameDialog.getInstance());
                break;
        }
        dismissAllowingStateLoss();
    }


    @Override
    public void initView(View view) {
        super.initView(view);

        getBind().tvTitle.setText(getStringWithoutContext(R.string.dialogTitle2));

        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(getStringWithoutContext(R.string.editNicknameShouldSpend)).setForegroundColor(0xff646464);
        spanUtils.append("60").setForegroundColor(0xffF42C2C);
        spanUtils.appendImage(getResources().getDrawable(R.mipmap.icon_diamond),SpanUtils.ALIGN_BASELINE);
        spanUtils.append(",");
        spanUtils.append(getStringWithoutContext(R.string.plzConfirm)).setForegroundColor(0xff646464);
        getBind().tvContent.setText(spanUtils.create());

        getBind().gtCancel.setText(getStringWithoutContext(R.string.cancel));
        getBind().gtCommit.setText(getStringWithoutContext(R.string.confirm));
    }
}
