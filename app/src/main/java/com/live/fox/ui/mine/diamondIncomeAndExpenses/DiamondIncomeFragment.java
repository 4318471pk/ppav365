package com.live.fox.ui.mine.diamondIncomeAndExpenses;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.DiamondExpensesAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.FragmentDiamondIncomeBinding;
import com.live.fox.dialog.bottomDialog.TimePickerDialog;
import com.live.fox.entity.DiamondIncomeAndExpenseBean;
import com.live.fox.utils.device.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class DiamondIncomeFragment extends BaseBindingFragment {


    FragmentDiamondIncomeBinding mBind;
    DiamondExpensesAdapter adapter;

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.llTimeSelector:
                TimePickerDialog timePickerDialog=TimePickerDialog.getInstance(getStringWithoutContext(R.string.selectDate));
                timePickerDialog.setOnSelectedListener(new TimePickerDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int year, int month, int date, long time) {
                        StringBuilder sb=new StringBuilder();
                        sb.append(year).append("-");
                        sb.append(month).append("-");
                        sb.append(date);
                        mBind.tvTime.setText(sb.toString());
                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), timePickerDialog);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_diamond_income;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        List<DiamondIncomeAndExpenseBean> list=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DiamondIncomeAndExpenseBean bean=new DiamondIncomeAndExpenseBean();
            bean.setAmountOfDiamond(System.currentTimeMillis());
            bean.setNickname("我就是我不一样的果");
            bean.setAmountOfDiamond(9999999l);
            bean.setType("按时收费");
            list.add(bean);
        }
        adapter=new DiamondExpensesAdapter(getContext(),list);
        adapter.addHeaderView(makeHeader());
        mBind.rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        mBind.rvMain.setAdapter(adapter);

    }

    private View makeHeader()
    {
        float ratio[]=new float[]{0.2f,0.3f,0.3f,0.2f};
        int textResource[]=new int[]{R.string.time,R.string.customerNickname,
                R.string.diamond,R.string.expenseType};

        int height= ScreenUtils.getDip2px(getContext(),38);
        int width=ScreenUtils.getScreenWidth(getContext())-ScreenUtils.getDip2px(getContext(),10);

        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(width,height));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(0xffffffff);

        for (int i = 0; i <textResource.length ; i++) {
            TextView textView=new TextView(getContext());
            int itemWidth=(int)(width*ratio[i]);
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(itemWidth,height);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            textView.setTextColor(0xff665275);
            textView.setText(textResource[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(ll);
            linearLayout.addView(textView);
        }

        return linearLayout;
    }
}
