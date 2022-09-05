package com.live.fox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.utils.ConvertUtils;
import com.live.fox.utils.StringUtils;


/**
 * @author : zlf
 * date    : 2019/4/17
 * github  : https://github.com/mamumu
 * blog    : https://www.jianshu.com/u/281e9668a5a6
 */
public class MMLoading extends Dialog {

    public MMLoading(Context context) {
        super(context);
    }

    public MMLoading(Context context, int themeResId) {
        super(context, themeResId);
    }

    public boolean isBgBlack = false;


    public static class Builder {

        private Context context;
        private String message;
        //        private boolean isShowMessage = true;
        private boolean isCancelable = false;
        private boolean isCancelOutside = false;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         * @param message
         * @return
         */

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

//        /**
//         * 设置是否显示提示信息
//         * @param isShowMessage
//         * @return
//         */
//        public Builder setShowMessage(boolean isShowMessage) {
//            this.isShowMessage = isShowMessage;
//            return this;
//        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以点击外部取消
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }



        public MMLoading create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_loading, null);
            MMLoading mmLoading = new MMLoading(context, R.style.LoadingDialog);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_ll);
            TextView msgText = (TextView) view.findViewById(R.id.tipTextView);
            if(StringUtils.isEmpty(message)){
                msgText.setVisibility(View.GONE);
                layout.setMinimumWidth(0);
                layout.setMinimumHeight(0);
            }else {
                msgText.setVisibility(View.VISIBLE);
                msgText.setText(message);
                layout.setMinimumWidth(ConvertUtils.dp2px(92));
                layout.setMinimumHeight(ConvertUtils.dp2px(88));
            }
            mmLoading.setContentView(view);
            mmLoading.setCancelable(isCancelable);
            mmLoading.setCanceledOnTouchOutside(isCancelOutside);


            //实现loading的透明度
//            WindowManager.LayoutParams lp=mmLoading.getWindow().getAttributes();
//            lp.alpha = 0.6f;
//            mmLoading.getWindow().setAttributes(lp);
            return mmLoading;
        }

    }


    public void setIsBgBlack(boolean isBgBlack){
        this.isBgBlack = isBgBlack;
    }


    public void setText(String text){
        ((TextView)findViewById(R.id.tipTextView)).setText(text);
    }


    @Override
    public void show() {
        if(!isBgBlack) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.dimAmount = 0f;
            getWindow().setAttributes(lp);
        }
        super.show();
    }
}