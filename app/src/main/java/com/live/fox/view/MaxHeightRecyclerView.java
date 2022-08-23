package com.live.fox.view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.live.fox.R;

/*****************************************************************
 * Created by  on 2020\1\20 0020 下午
 * desciption:
 *****************************************************************/
public class MaxHeightRecyclerView extends RecyclerView {
    /**
     * 默认最大高度
     **/
    private int maxHeight = 350;

    public MaxHeightRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecycler);
        maxHeight = arr.getLayoutDimension(R.styleable.MaxHeightRecycler_maxHeight, maxHeight);
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
