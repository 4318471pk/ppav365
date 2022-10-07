package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;

public class ContributionRankActivity extends BaseBindingViewActivity {

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,ContributionRankActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_contribution_rank;
    }

    @Override
    public void initView() {

    }


}
