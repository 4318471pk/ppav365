package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

//解决设置了imeOptions之后不能多行显示的问题
public class MyEditText extends EditText {

	public MyEditText(Context context) {
		super(context);
	}
	
	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		//这里调用父类方法来初始化必要部分
		    InputConnection connection = super.onCreateInputConnection(outAttrs);
		if (connection == null) return null;
		//移除EditorInfo.IME_FLAG_NO_ENTER_ACTION标志位
		    outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
		    return connection;
		}


}
