package com.live.fox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.live.fox.R;
import com.live.fox.utils.device.ScreenUtils;

public class GradientTextView extends AppCompatTextView {

    private final static String TAG = GradientTextView.class.getSimpleName();
    private int[] mColors;
    private int solidColor=-1;
    private int mAngle = 0;
    private DIRECTION mDIRECTION;
    GradientDrawable mIndicatorDrawable=new GradientDrawable();
    float radius=10f;
    private int strokeColor=-1;
    private float strokeWidth=-1;
    GradientDrawable.Orientation orientation=GradientDrawable.Orientation.LEFT_RIGHT;

    public enum DIRECTION {
        LEFT(0),
        TOP(90),
        RIGHT(180),
        BOTTOM(270);

        int angle;

        DIRECTION(int angle) {
            this.angle = angle;
        }
    }

    public GradientTextView(Context context) {
        super(context);
        init(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet) {
        final TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GradientTextView);
        try {
            int colorArrayResourceId = typedArray.getResourceId(R.styleable.GradientTextView_gt_color_list, 0);
            if (colorArrayResourceId != 0) {
                mColors = getResources().getIntArray(colorArrayResourceId);
            }
            if (typedArray.hasValue(R.styleable.GradientTextView_gt_gradient_direction)) {
                int value = typedArray.getInt(R.styleable.GradientTextView_gt_gradient_direction, 0);
                mDIRECTION = DIRECTION.values()[value];
            }

            if (typedArray.hasValue(R.styleable.GradientTextView_gt_gradient_angle)) {
                mAngle = typedArray.getInt(R.styleable.GradientTextView_gt_gradient_angle, 0);
            }

            if (typedArray.hasValue(R.styleable.GradientTextView_gt_radius_dp)) {
                radius = ScreenUtils.getDip2px(context,typedArray.getDimension(R.styleable.GradientTextView_gt_radius_dp, 0));
            }

            if (typedArray.hasValue(R.styleable.GradientTextView_gt_stroke_color)) {
                strokeColor = typedArray.getColor(R.styleable.GradientTextView_gt_stroke_color, -1);
            }

            if (typedArray.hasValue(R.styleable.GradientTextView_gt_stroke_width)) {
                strokeWidth = typedArray.getDimension(R.styleable.GradientTextView_gt_stroke_width, -1f);
            }

            if (typedArray.hasValue(R.styleable.GradientTextView_gt_solidNoGradient)) {
                solidColor = typedArray.getColor(R.styleable.GradientTextView_gt_solidNoGradient, -1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }

        if (mDIRECTION != null) {
            switch (mDIRECTION) {
                case TOP:
                    orientation=GradientDrawable.Orientation.TOP_BOTTOM;
                case RIGHT:
                    orientation=GradientDrawable.Orientation.RIGHT_LEFT;
                case BOTTOM:
                    orientation=GradientDrawable.Orientation.BOTTOM_TOP;
                case LEFT:
                default:
                    orientation=GradientDrawable.Orientation.LEFT_RIGHT;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //if colors haven't been set, skip this
        if (mColors != null) {

        }
    }


    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    public void setOrientation(DIRECTION direction) {
        if(direction!=null)
        {
                switch (mDIRECTION) {
                    case TOP:
                        orientation=GradientDrawable.Orientation.TOP_BOTTOM;
                    case RIGHT:
                        orientation=GradientDrawable.Orientation.RIGHT_LEFT;
                    case BOTTOM:
                        orientation=GradientDrawable.Orientation.BOTTOM_TOP;
                    case LEFT:
                    default:
                        orientation=GradientDrawable.Orientation.LEFT_RIGHT;
            }
            invalidate();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(solidColor!=-1)
        {
            mIndicatorDrawable.setBounds(0,0,getWidth(),getHeight());
            mIndicatorDrawable.setColor(solidColor);
            mIndicatorDrawable.setCornerRadius(radius);
            mIndicatorDrawable.draw(canvas);
        }

        if(mColors!=null )
        {
            mIndicatorDrawable.setBounds(0,0,getWidth(),getHeight());
            mIndicatorDrawable.setColors(mColors);
            mIndicatorDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            mIndicatorDrawable.setOrientation(orientation);
            mIndicatorDrawable.setCornerRadius(radius);
            mIndicatorDrawable.draw(canvas);
        }

        if(strokeColor!=-1 && strokeWidth>0)
        {
            mIndicatorDrawable.setBounds(0,0,getWidth(),getHeight());
            mIndicatorDrawable.setStroke((int)strokeWidth,strokeColor);
            mIndicatorDrawable.setCornerRadius(radius);
            mIndicatorDrawable.draw(canvas);
        }

        super.onDraw(canvas);

    }
}
