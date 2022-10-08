package com.live.fox.ui.mine.depositAndWithdrawHistory;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.DepositHistoryAdapter;
import com.live.fox.adapter.WithdrawHistoryAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentWithdrawHistoryBinding;
import com.live.fox.entity.DepositeHistoryBean;
import com.live.fox.entity.WithdrawHistoryBean;
import com.live.fox.utils.device.ScreenUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WithdrawHistoryFragment extends BaseBindingFragment {

    FragmentWithdrawHistoryBinding mBind;
    WithdrawHistoryAdapter withdrawHistoryAdapter;
    private int titles[]=new int[]{R.string.withdrawTime,R.string.amountOfMoney,R.string.withdrawType2,R.string.depositStatus};
    List<WithdrawHistoryBean> list;

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_withdraw_history;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        list=new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            WithdrawHistoryBean bean=new WithdrawHistoryBean();
            bean.setAmountOfMoney(new BigDecimal("21.23"));
            bean.setStatusStr("成功");
            bean.setWithdrawTime(System.currentTimeMillis());
            bean.setWithdrawWay("支付宝");
            list.add(bean);
        }
        withdrawHistoryAdapter=new WithdrawHistoryAdapter(getActivity(),list);
        withdrawHistoryAdapter.addHeaderView(makeHeader());
        mBind.rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        mBind.rvMain.setAdapter(withdrawHistoryAdapter);
    }

    private View makeHeader()
    {

        float ratio[]=new float[]{0.2f,0.3f,0.3f,0.2f};

        int height= ScreenUtils.getDip2px(getContext(),38);
        int width=ScreenUtils.getScreenWidth(getContext())
                -ScreenUtils.getDip2px(getContext(),10);

        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(width,height));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(0xffffffff);

        for (int i = 0; i <titles.length ; i++) {
            TextView textView=new TextView(getContext());
            int itemWidth=(int)(width*ratio[i]);
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(itemWidth,height);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            textView.setTextColor(0xff665275);
            textView.setText(titles[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(ll);
            linearLayout.addView(textView);
        }

        return linearLayout;
    }
}
