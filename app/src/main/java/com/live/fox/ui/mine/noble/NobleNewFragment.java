package com.live.fox.ui.mine.noble;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.adapter.NobleEquityAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentNobleBinding;
import com.live.fox.dialog.CommonDialog;
import com.live.fox.entity.NobleEquityBean;
import com.live.fox.ui.mine.RechargeActivity;

import java.util.ArrayList;
import java.util.List;

public class NobleNewFragment extends BaseBindingFragment {

    FragmentNobleBinding mBind;
    NobleEquityAdapter nobleEquityAdapter;
    private CommonDialog commonDialog;

    List<NobleEquityBean> mList = new ArrayList<>();
    int level;


    public static NobleNewFragment newInstance(int level) {
        NobleNewFragment fragment = new NobleNewFragment();
        Bundle args = new Bundle();
        args.putInt("level", level);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_noble;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        commonDialog = new CommonDialog();
        level = getArguments().getInt("level");
        showNoble();
        setData();
        nobleEquityAdapter = new NobleEquityAdapter(this.getActivity(), mList);
        nobleEquityAdapter.setType(level);
        mBind.gv.setAdapter(nobleEquityAdapter);

        mBind.layoutOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setPopCharge();
                String tips =  String.format(getString(R.string.confirm_buy_noble),
                        10000+"", getString(R.string.noble_2) + "." + getNoble());
                showDialog(getString(R.string.dialog_words), tips, false);

               // showDialog(getString(R.string.dialogTitle2), getString(R.string.goto_charge_diamond), true);
            }
        });


    }

    private void showDialog(String content, String tips, boolean isCharge) {
        commonDialog.setDialogContent(
                content,
                tips,
                getString(R.string.button_cancel),
                getString(R.string.button_confirm),
                view -> commonDialog.dismiss(),
                view -> {
                    commonDialog.dismiss();
                    if (isCharge) { //充值
                        RechargeActivity.startActivity(requireActivity());
                    } else { //购买

                    }

                });
        commonDialog.show(this.getActivity().getSupportFragmentManager(), "noble dialog");
    }

    private void setPopCharge(){
        View popupView = getLayoutInflater().inflate(R.layout.pop_noble_buy,null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(null);
        TextView tvTitle = popupView.findViewById(R.id.tv_title);
        String string = "<font color='#ffffff'> " +getResources().getString(R.string.con_you_buy)+ "</font>" +
                "<font color='#E2B361'> " +getResources().getString(R.string.noble_2) + "." + getNoble() + "</font>" +
                "<font color='#ffffff'> " +getResources().getString(R.string.tab_change_success)+ "</font>";
        tvTitle.setText(Html.fromHtml(string));

        TextView tvTime = popupView.findViewById(R.id.tv_time);
        setRootAlpha(0.35f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                setRootAlpha(1f);
            }
        });

        popupWindow.showAtLocation(mBind.getRoot(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished / 1000 + "s");
            }
            public void onFinish() {
                popupWindow.dismiss();
            }
        }.start();


    }

    private void setRootAlpha(float al){
        WindowManager.LayoutParams lp=this.getActivity().getWindow().getAttributes();
        lp.alpha= al;
        this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getActivity().getWindow().setAttributes(lp);
    }



    private void showNoble(){
        if (level == NobleActivity.NANJUE) {

        } else if (level == NobleActivity.ZIJUE || level == NobleActivity.BOJUE) {
            mBind.tvPower.setText("7/13");
            if (level == NobleActivity.ZIJUE) {
                mBind.tvNoble.setText(getString(R.string.zijue));
                mBind.layout.setBackground(getResources().getDrawable(R.mipmap.zijue_p));
            } else {
                mBind.tvNoble.setText(getString(R.string.bojue));
                mBind.layout.setBackground(getResources().getDrawable(R.mipmap.bojue_p));
            }
        } else if (level == NobleActivity.HOUJUE) {
            mBind.tvPower.setText("8/13");
            mBind.tvNoble.setText(getString(R.string.houjue));
            mBind.layout.setBackground(getResources().getDrawable(R.mipmap.houjue_p));
        } else if (level == NobleActivity.GONGJUE) {
            mBind.tvPower.setText("11/13");
            mBind.tvNoble.setText(getString(R.string.gongjue));
            mBind.layout.setBackground(getResources().getDrawable(R.mipmap.gongjue_p));
        } else if (level == NobleActivity.QINWANG) {
            mBind.tvPower.setText("12/13");
            mBind.tvNoble.setText(getString(R.string.qinwang));
            mBind.layout.setBackground(getResources().getDrawable(R.mipmap.qinwang_p));
        } else if (level == NobleActivity.KING) {
            mBind.tvPower.setText("13/13");
            mBind.tvNoble.setText(getString(R.string.king));
            mBind.layout.setBackground(getResources().getDrawable(R.mipmap.guowang_p));
        }

    }

    private String getNoble(){
        if (level == NobleActivity.NANJUE) {
            return getString(R.string.nanjue);
        } else if (level == NobleActivity.ZIJUE ) {
            return getString(R.string.zijue);
        }  else if (level == NobleActivity.BOJUE ) {
            return getString(R.string.zijue);
        } else if (level == NobleActivity.HOUJUE) {
            return getString(R.string.houjue);
        } else if (level == NobleActivity.GONGJUE) {
            return getString(R.string.gongjue);
        } else if (level == NobleActivity.QINWANG) {
            return getString(R.string.qinwang);
        }
        return getString(R.string.king);
    }


    private void setData(){
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_1), getString(R.string.noble_equity_tips_1),
                getResources().getDrawable(R.mipmap.equity1)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_2), getString(R.string.noble_equity_tips_2),
                getResources().getDrawable(R.mipmap.equity2)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_3), getString(R.string.noble_equity_tips_3),
                getResources().getDrawable(R.mipmap.equity3)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_4), getString(R.string.noble_equity_tips_4),
                getResources().getDrawable(R.mipmap.equity4)));

        NobleEquityBean bean = new NobleEquityBean(getString(R.string.noble_equity_title_5), getString(R.string.noble_equity_tips_5),
                getResources().getDrawable(R.mipmap.zi_kuang));
        bean.setShowPhoto(true);
        if(level == NobleActivity.ZIJUE) {
            bean.setImg(getResources().getDrawable(R.mipmap.zi_kuang));
        } else if(level == NobleActivity.BOJUE) {
            bean.setImg(getResources().getDrawable(R.mipmap.bo_kuagn));
        } else if(level == NobleActivity.HOUJUE) {
            bean.setImg(getResources().getDrawable(R.mipmap.hou_kuang));
        } else if(level == NobleActivity.GONGJUE) {
            bean.setImg(getResources().getDrawable(R.mipmap.gong_kuang));
        } else if(level == NobleActivity.QINWANG) {
            bean.setImg(getResources().getDrawable(R.mipmap.qin_kuang));
        } else if(level == NobleActivity.KING) {
            bean.setImg(getResources().getDrawable(R.mipmap.wang_kuang));
        }
        mList.add(bean);



        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_6), getString(R.string.noble_equity_tips_6),
                getResources().getDrawable(R.mipmap.equity6), getShow(5)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_7), getString(R.string.noble_equity_tips_7),
                getResources().getDrawable(R.mipmap.equity7), getShow(6)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_8), getString(R.string.noble_equity_tips_8),
                getResources().getDrawable(R.mipmap.equity8), getShow(7)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_9), getString(R.string.noble_equity_tips_9),
                getResources().getDrawable(R.mipmap.equity9), getShow(8)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_10), getString(R.string.noble_equity_tips_10),
                getResources().getDrawable(R.mipmap.equity10), getShow(9)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_11), getString(R.string.noble_equity_tips_11),
                getResources().getDrawable(R.mipmap.equity5), getShow(10)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_12), getString(R.string.noble_equity_tips_12),
                getResources().getDrawable(R.mipmap.equity11), getShow(11)));
        mList.add(new NobleEquityBean(getString(R.string.noble_equity_title_13), getString(R.string.noble_equity_tips_13),
                getResources().getDrawable(R.mipmap.equity12), getShow(12)));

    }

    private boolean getShow(int pos) {
        if (level == NobleActivity.NANJUE) {
            if (pos > 4) {
                return false;
            } else {
                return true;
            }
        } else if (level == NobleActivity.ZIJUE || level == NobleActivity.BOJUE) {
            if (pos > 6) {
                return false;
            } else {
                return true;
            }
        } else if (level == NobleActivity.HOUJUE) {
            if (pos > 7) {
                return false;
            } else {
                return true;
            }
        } else if (level == NobleActivity.GONGJUE) {
            if (pos > 10) {
                return false;
            } else {
                return true;
            }
        } else if (level == NobleActivity.QINWANG) {
            if (pos > 11) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }



}
