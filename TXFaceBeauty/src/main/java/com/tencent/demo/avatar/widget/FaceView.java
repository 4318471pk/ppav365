package com.tencent.demo.avatar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.tencent.demo.R;

public class FaceView extends LinearLayout {

    private float ovalWidth = -1;
    private float ovalHeight = -1;
    private float ovalTop = -1;
    private Paint bgPaint = new Paint();
    private Paint ovalPaint = new Paint();
    private Paint ovalLinePaint = new Paint();

    public FaceView(Context context) {
        this(context, null);
    }

    public FaceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.TRANSPARENT);
        this.initPaint();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FaceView);
            ovalTop = typedArray.getDimension(R.styleable.FaceView_oval_top, -1);
            ovalWidth = typedArray.getDimension(R.styleable.FaceView_oval_width, -1);
            ovalHeight = typedArray.getDimension(R.styleable.FaceView_ovalHeight, -1);
        }
    }


    private void initPaint() {
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor("#CC1C2225"));
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeWidth(10);

        ovalPaint.setAlpha(0);
        ovalPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        ovalLinePaint.setColor(Color.parseColor("#FF0085CF"));
        ovalLinePaint.setStyle(Paint.Style.STROKE);
        ovalLinePaint.setStrokeWidth(10);
        ovalLinePaint.setStrokeCap(Paint.Cap.ROUND);
        ovalLinePaint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 20));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (ovalWidth == -1 || ovalHeight == -1) {
            ovalWidth = getWidth() / 3 * 2;
            ovalHeight = ovalWidth + 100;
        }
        if (ovalTop == -1) {
            ovalTop = getHeight() / 5;
        }

        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRect(rect, bgPaint);


        int ovalLeft = (int) ((getWidth() - ovalWidth) / 2);
        RectF ovalRectF = new RectF(ovalLeft, ovalTop, ovalLeft + ovalWidth, ovalTop + ovalHeight);
        canvas.drawOval(ovalRectF, ovalPaint);

        canvas.drawOval(ovalRectF, ovalLinePaint);
    }
}
