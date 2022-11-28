package com.tencent.demo.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.tencent.demo.camera.camerax.GLCameraXView;
import com.tencent.demo.camera.gltexture.GLTextureView;
import com.tencent.demo.camera.gltexture.NoCameraGLTextureView;

public class DraggableViewGroup extends FrameLayout {

    private ViewDragHelper mDragHelper = null;

    public DraggableViewGroup(Context context) {
        super(context);
        this.initHelper();

    }

    public DraggableViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initHelper();
    }

    public DraggableViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initHelper();
    }


    private void initHelper() {
        mDragHelper = ViewDragHelper.create(this, 50.0f, new DragHelperCallback());
    }

    //事件拦截处理，基本是固定的格式
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    //事件处理，格式也是固定的
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return true;
    }

    //创建DragHelper的Callback类
    class DragHelperCallback extends ViewDragHelper.Callback {

        private View mDragView = null;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            mDragView = child;
            return child instanceof GLTextureView || child instanceof GLCameraXView ||child instanceof NoCameraGLTextureView;
        }


        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int leftBound = getPaddingLeft();
            int rightBound = getWidth() - mDragView.getWidth();
            int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            return newLeft;
        }

        //同样要实现纵向移动要实现这个方法
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int topBound = getPaddingTop();  //padding顶部的距离
            //父框体的宽度减去视图本身的宽度，得到可以移动的最大宽度
            int bottomBound = getHeight() - mDragView.getHeight();
            //先取出padding顶部和距离顶部距离的最大值
            //然后让这个最大值和这个视图可以移动的距离做比较，取出最小值，得到视图实际可以移动的距离
            int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }
    }

}
