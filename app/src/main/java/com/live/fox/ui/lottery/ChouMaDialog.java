package com.live.fox.ui.lottery;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogChoumaBinding;
import com.live.fox.entity.SelectLotteryBean;
import com.live.fox.ui.lottery.adapter.ChouMaAdapter;
import com.live.fox.utils.ToastUtils;


import java.util.ArrayList;
import java.util.List;

public class ChouMaDialog extends BaseBindingDialogFragment {

    DialogChoumaBinding mBind;

    ChouMaAdapter chouMaAdapter;
    List<SelectLotteryBean> chouMaList = new ArrayList<>();

    InputChouMaDialog inputChouMaDialog;

    public static ChouMaDialog newInstance() {
        Bundle args = new Bundle();
        ChouMaDialog fragment = new ChouMaDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen_2);
    }

    @Override
    public void onClickView(View view) {
        if (view == mBind.rlMain) {
            dismissAllowingStateLoss();
        } else if (view == mBind.tvConfirm) {
            boolean isSelect = false;
            String money = "";
            int imgRes = 0;
            for (int i = 0 ;i < chouMaList.size(); i++) {
                if (chouMaList.get(i).isSelect()) {
                    isSelect = true;
                    money = chouMaList.get(i).getName();
                    imgRes = i;
                    break;
                }
            }
            if (!isSelect) {
                ToastUtils.showShort(getResources().getString(R.string.please_selcet_chouma));
                return;
            }
            if(TextUtils.isEmpty(money)) {
                ToastUtils.showShort(getResources().getString(R.string.please_selcet_chouma));
                return;
            }
            chouMaSelectSuc.select(money, getImgRes(imgRes), imgRes == 7 ? true :false);
            dismissAllowingStateLoss();
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_chouma;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);
        addData();

        GridLayoutManager grid = new GridLayoutManager(getActivity(), 4);
        mBind.rcChouMa.setLayoutManager(grid);

        chouMaAdapter = new ChouMaAdapter(chouMaList);
        mBind.rcChouMa.setAdapter(chouMaAdapter);

        chouMaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0 ;i < chouMaList.size(); i++) {
                    chouMaList.get(i).setSelect(false);
                }
                chouMaList.get(position).setSelect(true);
                chouMaAdapter.notifyDataSetChanged();
                if (position == 7) { //自定义筹码
                    if (inputChouMaDialog == null) {
                        inputChouMaDialog = InputChouMaDialog.newInstance(
                                getResources().getString(R.string.please_input_score), true, "");
                    }
                    inputChouMaDialog.show(getActivity().getSupportFragmentManager(), "input chouma dialog");
                    inputChouMaDialog.setChouMaInputListener(new InputChouMaDialog.ChouMaInputListener() {
                        @Override
                        public void confirm(String money, boolean isChouma) {
                            if (isChouma) {
                                if (chouMaList.size() > 7) {
                                    chouMaList.get(7).setName(money);
                                    chouMaAdapter.notifyItemChanged(7);
                                }
                            }
                        }
                    });
                }
            }
        });
    }



    private void addData(){
        chouMaList.clear();
        chouMaList.add(new SelectLotteryBean("2")); chouMaList.add(new SelectLotteryBean("5"));
        chouMaList.add(new SelectLotteryBean("10")); chouMaList.add(new SelectLotteryBean("50"));
        chouMaList.add(new SelectLotteryBean("100")); chouMaList.add(new SelectLotteryBean("500"));
        chouMaList.add(new SelectLotteryBean("1000"));
        SelectLotteryBean bean = new SelectLotteryBean();
        bean.setZdy(true);
        chouMaList.add(bean);
    }

    public Drawable getImgRes(int pos) {
        if (pos == 0) {
            return getResources().getDrawable(R.mipmap.chouma2);
        } else if (pos == 1) {
            return getResources().getDrawable(R.mipmap.chouma5);
        } else if (pos == 2) {
            return getResources().getDrawable(R.mipmap.chouma10);
        } else if (pos == 3) {
            return getResources().getDrawable(R.mipmap.chouma50);
        } else if (pos == 4) {
            return getResources().getDrawable(R.mipmap.chouma100);
        }else if (pos == 5) {
            return getResources().getDrawable(R.mipmap.chouma500);
        } else if (pos == 6) {
            return getResources().getDrawable(R.mipmap.chouma1000);
        }
        return getResources().getDrawable(R.mipmap.chouma_zdy);
    }

    public interface ChouMaSelectSuc{
        public void select(String money, Drawable img, boolean isSdy);
    }

    private ChouMaSelectSuc chouMaSelectSuc;

    public void setSelectChouMaSuc(ChouMaSelectSuc suc){
        this.chouMaSelectSuc = suc;
    }
}
