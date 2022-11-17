package com.live.fox.dialog.bottomDialog;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogAudienceAdminlistBinding;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import java.util.ArrayList;
import java.util.List;

public class AudienceAdminListDialog extends BaseBindingDialogFragment {

    DialogAudienceAdminlistBinding mBind;
    int viewHeight;

    public static AudienceAdminListDialog getInstance() {
        return new AudienceAdminListDialog();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                if (mBind.ivIntroduction.getVisibility() == View.GONE) {
                    mBind.llList.setVisibility(View.GONE);
                    mBind.rlEmptyDataView.setVisibility(View.VISIBLE);
                    mBind.tvTips.setVisibility(View.GONE);
                    mBind.tvAmountOfAdmin.setVisibility(View.VISIBLE);
                    mBind.tvTitle.setText(getStringWithoutContext(R.string.setAudienceAdmin));
                    mBind.ivIntroduction.setVisibility(View.VISIBLE);
                } else {
                    showAnotherDialog(R.id.ivClose);
                }
                break;
            case R.id.ivIntroduction:
                mBind.tvTitle.setText(getStringWithoutContext(R.string.dialogTitleAudienceList));
                mBind.ivIntroduction.setVisibility(View.GONE);
                mBind.llList.setVisibility(View.GONE);
                mBind.rlEmptyDataView.setVisibility(View.GONE);
                mBind.tvTips.setVisibility(View.VISIBLE);
                mBind.tvAmountOfAdmin.setVisibility(View.GONE);
                break;
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.llContent, false);
                break;
        }
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.llContent,false);
        return true;
    }

    private void showAnotherDialog(int id) {
        startAnimate(mBind.llContent, false, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (id) {
                    case R.id.ivClose:
                        AudienceManagerDialog audienceManagerDialog = AudienceManagerDialog.getInstance();
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getParentFragmentManager(), audienceManagerDialog);
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_audience_adminlist;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        int screenHeight = ScreenUtils.getScreenHeight(getActivity());
        viewHeight = (int) (screenHeight * 0.56f);
        mBind.llContent.getLayoutParams().height = viewHeight;

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("");
        }
        addItem(list);
        view.setVisibility(View.VISIBLE);

        startAnimate(mBind.llContent, true);
    }

    private void addItem(List list) {
        if (list == null || list.size() == 0) {
            mBind.llList.setVisibility(View.GONE);
            mBind.rlEmptyDataView.setVisibility(View.VISIBLE);
            return;
        } else {
            mBind.llList.removeAllViews();
            mBind.llList.setVisibility(View.VISIBLE);
            mBind.rlEmptyDataView.setVisibility(View.GONE);
        }

        int itemHeight=(int)(viewHeight*0.64f/4);
        for (int i = 0; i < list.size(); i++) {
            View view = View.inflate(getActivity(), R.layout.item_audience_admin_list, null);
            mBind.llList.addView(view,ViewGroup.LayoutParams.MATCH_PARENT,itemHeight);
            SpanUtils spanUtils = new SpanUtils();
            spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(48, getActivity()));

            TextView tvRemove = view.findViewById(R.id.tvRemove);
            RankProfileView rpv = view.findViewById(R.id.rpv);
            TextView tvNickName = view.findViewById(R.id.tvNickName);
            TextView tvIcons = view.findViewById(R.id.tvIcons);

            tvNickName.setText("名字");
            tvIcons.setText(spanUtils.create());
            rpv.setIndex(RankProfileView.NONE, 48 % 7,false);

            if (list.size() < 5) {
                TextView textView = new TextView(getActivity());
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.topMargin = ScreenUtils.dp2px(getActivity(), 10);
                textView.setLayoutParams(ll);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                textView.setTextColor(0xffA3A3A3);
                textView.setGravity(Gravity.CENTER);
                textView.setText(getStringWithoutContext(R.string.noMoreData));

                mBind.llList.addView(textView);
            }

        }

    }
}
