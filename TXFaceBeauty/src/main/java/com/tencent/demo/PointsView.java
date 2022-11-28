package com.tencent.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.List;

public class PointsView extends View {

    private Paint paint;

    public PointsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
    }

    private List<Float> points;
    public void setPoints(List<Float> points){
        this.points = points;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (points == null) {
            return;
        }
        for (int i =0;i<points.size()-1;i+=2){
            canvas.drawCircle(points.get(i),points.get(i+1),4,paint);
        }
    }
}
