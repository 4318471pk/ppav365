package com.live.fox.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivitySearchAnchorBinding;
import com.live.fox.db.LocalWatchHistoryDao;
import com.live.fox.entity.SearchAnchorBean;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordFragment;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordModifyFragment;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordOperationListFragment;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordResetFragment;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ToastUtils;

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
//                if(mBind.etSearch.getText().toString().length()>0)
//                {
//                    showResult();
//                }
//                else
//                {
//                    showNoResult();
//                }
                searchAnchor(mBind.etSearch.getText().toString());
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_search_anchor;
    }

    @Override
    public void initView() {
        setWindowsFlag();
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

        mBind.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0)
                {
                    showPreView();
                }
            }
        });
        showPreView();
    }

    private void saveSearchHistory(String text)
    {
        String history= SPUtils.getInstance().getString(ConstantValue.searchHistory,"");
        if(!TextUtils.isEmpty(history)) {
            String strings[] = history.split(",");
            boolean isMoreThan50=strings.length>50;
            int count=strings.length>30?30:strings.length;
            StringBuilder sb=new StringBuilder();
            //超过50条就取前面的30条保存
            for (int i = 0; i < count; i++) {
                if(isMoreThan50)
                {
                    sb.append(",").append(strings[i]);
                }
                if(strings[i].equals(text))
                {
                    return;
                }
            }
            if(sb.length()>0)
            {
                history=sb.toString();
            }
        }
        StringBuilder sb=new StringBuilder();
        sb.append(",").append(text).append(history);
        SPUtils.getInstance().put(ConstantValue.searchHistory,sb.toString());
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

    private void showResult(List<SearchAnchorBean> list)
    {
        HasResultFragment hasResultFragment=(HasResultFragment)fragments.get(1);
        hasResultFragment.setData(list);

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


    public void searchAnchor(String content)
    {
        if(TextUtils.isEmpty(content))
        {
            return;
        }
        saveSearchHistory(content);
        mBind.gtvSearch.setEnabled(false);
        Api_Live.ins().searchAnchor(content, new JsonCallback<List<SearchAnchorBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<SearchAnchorBean> data) {
                if(isActivityOK())
                {
                    mBind.gtvSearch.setEnabled(true);
                    if(code==0)
                    {
                        if(data!=null && data.size()>0)
                        {
                            showResult(data);
                        }
                        else
                        {
                            showNoResult();
                        }
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }
}
