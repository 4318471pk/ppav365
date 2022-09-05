package com.live.fox.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.view.ActionMode;
import android.view.Menu;

public class PastWebView extends WebView {

    public PastWebView(Context context) {
        super(context);
    }

    public PastWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionMode actionMode = super.startActionMode(callback);
        return resolveActionMode(actionMode);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        ActionMode actionMode = super.startActionMode(callback, type);
        return resolveActionMode(actionMode);
    }

    private ActionMode resolveActionMode(ActionMode actionMode) {
        if (actionMode != null) {
            final Menu menu = actionMode.getMenu();
            menu.clear();
        }
        return actionMode;
    }

//    @SuppressLint("NewApi")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
//            onCancelPendingInputEvents();
//            cancelLongPress();
//            cancelPendingInputEvents();
//            cancelDragAndDrop();
//        }
//        return super.onTouchEvent(event);
//    }
}

