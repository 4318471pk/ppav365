package com.live.fox.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivitySearchAnchorBinding;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordFragment;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordModifyFragment;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordOperationListFragment;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordResetFragment;
import com.live.fox.utils.OnClickFrequentlyListener;

import java.util.ArrayList;
import java.util.List;

public class SearchAnchorActivity extends BaseBindingViewActivity {

    ActivitySearchAnchorBinding mBind;
    List<BaseBindingFragment> fragments;
    public static final String Status="Status";
    public static final int Normal=0;
    public static final int NoData=1;

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,SearchAnchorActivity.class));
    }

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivDelete:
                mBind.etSearch.setText("");
                showPreView();
                break;
            case R.id.gtvSearch:
                if(mBind.etSearch.getText().toString().length()>0)
                {
                    showResult();
                }
                else
                {
                    showNoResult();
                }
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_search_anchor;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        fragments=new ArrayList<>();
        fragments.add(PreviewSearchFragment.getInstance());
        fragments.add(HasResultFragment.getInstance());

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        for (int i = 0; i <fragments.size() ; i++) {
            fragmentTransaction.add(R.id.frameLayout,fragments.get(i));
        }

        for (int i = 0; i <fragments.size() ; i++) {
            fragmentTransaction.hide(fragments.get(i));
        }
        fragmentTransaction.commitAllowingStateLoss();
        showPreView();
    }

    public void showPreView()
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(0));
        fragmentTransaction.hide(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();

        Bundle bundle=new Bundle();
        bundle.putInt(Status,Normal);
        fragments.get(0).setArguments(bundle);
    }

    private void showResult()
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(1));
        fragmentTransaction.hide(fragments.get(0));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showNoResult()
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(0));
        fragmentTransaction.hide(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();

        Bundle bundle=new Bundle();
        bundle.putInt(Status,NoData);
        fragments.get(0).setArguments(bundle);
    }
}
