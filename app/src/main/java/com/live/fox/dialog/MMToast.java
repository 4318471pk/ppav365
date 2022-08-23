package com.live.fox.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.live.fox.R;


/**
 * @author : zlf
 * date    : 2019-04-18
 * github  : https://github.com/mamumu
 * blog    : https://www.jianshu.com/u/281e9668a5a6
 */
public class MMToast extends Toast {

    public MMToast(Context context) {
        super(context);
    }

    public static class Builder {
        private final Context context;
        private String message;
        private boolean isSuccess = true;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置toast的内容
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 是否是显示成功的toast，true:成功 false:失败
         *
         * @param isSuccess
         * @return
         */
        public Builder setSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
            return this;
        }

        public MMToast create(int type) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_toast, null);
            MMToast mmToast = new MMToast(context);
            TextView msgText = view.findViewById(R.id.t_text);
            ImageView imageView = view.findViewById(R.id.t_image);
            if (!TextUtils.isEmpty(message)) {
                msgText.setText(message);
            }

            if (isSuccess) {
                imageView.setImageResource(R.drawable.ic_toast_success);
            } else {
                imageView.setImageResource(R.drawable.ic_toast_failure);
            }

            mmToast.setView(view);

            mmToast.setDuration(type);
            mmToast.setGravity(Gravity.CENTER, 0, 0);
            return mmToast;
        }
    }


}