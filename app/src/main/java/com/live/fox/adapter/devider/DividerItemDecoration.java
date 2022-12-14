package com.live.fox.adapter.devider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private List<String> tags;//列表数据源的tag集合
    private int divideHeight = 1;//分割线高度（px）

    private Paint mPaint;

    public DividerItemDecoration() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#e5e5e5"));
    }

    public DividerItemDecoration(int color) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();

        //只处理线性垂直类型的列表
        if ((manager instanceof LinearLayoutManager)
                && LinearLayoutManager.VERTICAL != ((LinearLayoutManager) manager).getOrientation()) {
            return;
        }

        int position = parent.getChildAdapterPosition(view);
//        if (!Utils.listIsEmpty(tags) && (position + 1) < tags.size() && tags.get(position).equals(tags.get(position + 1))) {
        //当前ItemView的data的tag和下一个ItemView的不相等，则为当前ItemView设置bottom 偏移量
        outRect.set(0, 0, 0, divideHeight);
//        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            //和getItemOffsets()里的条件判断类似
//            if (!Utils.listIsEmpty(tags) && (position + 1) < tags.size() && tags.get(position).equals(tags.get(position + 1))) {
            drawDivide(c, parent, view);
//            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    private void drawDivide(Canvas c, RecyclerView parent, View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth();
        int top = view.getBottom() + params.bottomMargin;
        int bottom = top + divideHeight;
        c.drawRect(left, top, right, bottom, mPaint);
    }


    public DividerItemDecoration setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public DividerItemDecoration setDevideColor(int divideColor) {
        mPaint.setColor(divideColor);
        return this;
    }

    public DividerItemDecoration setDevideColor(String divideColor) {
        mPaint.setColor(Color.parseColor(divideColor));
        return this;
    }

    public DividerItemDecoration setDevideHeight(int divideHeight) {
        this.divideHeight = divideHeight;
        return this;
    }

}
