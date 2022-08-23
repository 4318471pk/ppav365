package com.live.fox.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.utils.StringUtils;


/**
 * 提醒弹框
 */
public class AppTipDialog extends DialogFragment {

    TextView tvTitle;
    TextView tvTitle2;
    TextView tvContent;
    TextView tvSure;

    boolean isShow;

    String title="";
    String title2="";
    String content="";

    public static AppTipDialog newInstance(String title, String title2, String content) {
        AppTipDialog fragment = new AppTipDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("title2", title2);
        bundle.putString("content", content);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogDefault);
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            title2 = bundle.getString("title2");
            content = bundle.getString("content");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_apptip, container, false);
    }

    //如果需要修改大小 修改显示动画 重写此方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isShow = true;

        initView(view);
    }


    public void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle2 = view.findViewById(R.id.tv_title2);
        tvContent = view.findViewById(R.id.tv_content);
        tvSure = view.findViewById(R.id.tv_sure);
        tvTitle.getPaint().setFakeBoldText(true);
        tvTitle2.getPaint().setFakeBoldText(true);
        tvContent.getPaint().setFakeBoldText(true);
        tvSure.getPaint().setFakeBoldText(true);

        tvTitle.setText(title);
        tvContent.setText(content);
        tvTitle2.setVisibility(StringUtils.isEmpty(title2) ? View.GONE: View.VISIBLE);
        if(!StringUtils.isEmpty(title2)){
            tvTitle2.setText(title2);
        }

        view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnSureClick!=null){
                    btnSureClick.onClick();
                }
                dismiss();
            }
        });
    }





    @Override
    public void dismiss() {
        isShow = false;
        super.dismiss();
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        this.btnSureClick = btnClick;
    }

    public interface OnBtnSureClick {
        void onClick();
    }

}
