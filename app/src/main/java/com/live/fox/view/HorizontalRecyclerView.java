package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.utils.ScreenUtils;

public class HorizontalRecyclerView extends RecyclerView {

    Context context;

    public HorizontalRecyclerView(Context context) {
        super(context);
        this.context= context;
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context= context;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();

        int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
        int firstVisibleView = linearLayoutManager.findFirstVisibleItemPosition();
        View firstView = linearLayoutManager.findViewByPosition(firstVisibleView);
        View lastView = linearLayoutManager.findViewByPosition(lastVisibleView);


        int leftMargin = (ScreenUtils.getScreenWidth(context) - lastView.getWidth()) / 2;
        int rightMargin = (ScreenUtils.getScreenWidth(context) - firstView.getWidth()) / 2 + firstView.getWidth();
        int leftEdge = lastView.getLeft();
        int rightEdge = firstView.getRight();
        int scrollDistanceLeft = leftEdge - leftMargin;
        int scrollDistanceRight = rightMargin - rightEdge;

        if (velocityX > 0) {
            smoothScrollBy(scrollDistanceLeft, 0); // 向右滑动
            if (horizontalRecyclerViewScroll != null) {
                horizontalRecyclerViewScroll.scrollRight();
            }
        } else {
            smoothScrollBy(-scrollDistanceRight, 0); // 向左
            if (horizontalRecyclerViewScroll != null) {
                horizontalRecyclerViewScroll.scrollLeft();
            }
        }

        return true;
    }

    public interface HorizontalRecyclerViewScroll{
        public void scrollRight();
        public void scrollLeft();
    }

    private HorizontalRecyclerViewScroll horizontalRecyclerViewScroll;

    public void setHorizontalRecyclerViewScroll(HorizontalRecyclerViewScroll horizontalRecyclerViewScroll){
        this.horizontalRecyclerViewScroll = horizontalRecyclerViewScroll;
    }

}
