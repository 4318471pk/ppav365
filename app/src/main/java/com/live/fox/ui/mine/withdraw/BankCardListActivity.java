package com.live.fox.ui.mine.withdraw;

import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.ActivityBankcardListBinding;
import com.live.fox.dialog.bottomdialog.AddNewBankCardDialog;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.device.ScreenUtils;

public class BankCardListActivity extends BaseBindingViewActivity {

    ActivityBankcardListBinding mBind;
    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_bankcard_list;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        setActivityTitle(getString(R.string.withdrawCards));

        Drawable drawable=getResources().getDrawable(R.mipmap.bg_empty_card);
        addEmptyCard(drawable);
        addBankCard();
        addTips(drawable.getIntrinsicHeight());

    }


    private void addEmptyCard(Drawable drawable)
    {
        ImageView imageView=new ImageView(this);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin=ScreenUtils.getDip2px(this,10);
        imageView.setLayoutParams(ll);
        imageView.setImageDrawable(drawable);
        imageView.setAdjustViewBounds(true);
        imageView.setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), AddNewBankCardDialog.getInstance());
            }
        });
        mBind.llCardList.addView(imageView);
    }

    private void addBankCard()
    {
        View view=getLayoutInflater().inflate(R.layout.item_bankcard,mBind.llCardList,false);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin=ScreenUtils.getDip2px(this,10);
        mBind.llCardList.addView(view,ll);
    }

    private void addTips(int viewHeight)
    {
        TextView textView=new TextView(this);
        textView.setTextColor(0xffB8B2C8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        LinearLayout.LayoutParams ll= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin=(viewHeight+ScreenUtils.getDip2px(this,10))*3+ScreenUtils.getDip2px(this,20);
        textView.setLayoutParams(ll);
        textView.setGravity(Gravity.CENTER);
        textView.setText(getResources().getString(R.string.max3Cards));
        mBind.rlMain.addView(textView);
    }
}
