package com.live.fox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;

import org.jetbrains.annotations.NotNull;

public class GradientEditTextView extends androidx.appcompat.widget.AppCompatEditText {

    private final static String TAG = GradientEditTextView.class.getSimpleName();
    private int[] mColors;
    private int solidColor=0;
    private int mAngle = 0;
    private GradientEditTextView.DIRECTION mDIRECTION;
    GradientDrawable mIndicatorDrawable=new GradientDrawable();
    float radius=10f;
    private int strokeColor=0;
    private float strokeWidth=0;
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

    public GradientEditTextView(Context context) {
        super(context);
        init(context, null);
    }

    public GradientEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GradientEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet) {
        final TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GradientEditTextView);
        try {
            int colorArrayResourceId = typedArray.getResourceId(R.styleable.GradientEditTextView_gt_color_list, 0);
            if (colorArrayResourceId != 0) {
                mColors = getResources().getIntArray(colorArrayResourceId);
            }
            if (typedArray.hasValue(R.styleable.GradientEditTextView_gt_gradient_direction)) {
                int value = typedArray.getInt(R.styleable.GradientEditTextView_gt_gradient_direction, 0);
                mDIRECTION = GradientEditTextView.DIRECTION.values()[value];
            }

            if (typedArray.hasValue(R.styleable.GradientEditTextView_gt_gradient_angle)) {
                mAngle = typedArray.getInt(R.styleable.GradientEditTextView_gt_gradient_angle, 0);
            }

            if (typedArray.hasValue(R.styleable.GradientEditTextView_gt_radius_dp)) {
                radius = typedArray.getDimension(R.styleable.GradientEditTextView_gt_radius_dp, 0);
            }

            if (typedArray.hasValue(R.styleable.GradientEditTextView_gt_stroke_color)) {
                strokeColor = typedArray.getColor(R.styleable.GradientEditTextView_gt_stroke_color, 0);
            }

            if (typedArray.hasValue(R.styleable.GradientEditTextView_gt_stroke_width)) {
                strokeWidth = typedArray.getDimension(R.styleable.GradientEditTextView_gt_stroke_width, 0);
            }

            if (typedArray.hasValue(R.styleable.GradientEditTextView_gt_solidNoGradient)) {
                solidColor = typedArray.getColor(R.styleable.GradientEditTextView_gt_solidNoGradient, 0);
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
                    break;
                case RIGHT:
                    orientation=GradientDrawable.Orientation.RIGHT_LEFT;
                    break;
                case BOTTOM:
                    orientation=GradientDrawable.Orientation.BOTTOM_TOP;
                    break;
                case LEFT:
                    orientation=GradientDrawable.Orientation.LEFT_RIGHT;
                    break;
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

    public void setColors(int[] mColors) {
        this.mColors = mColors;
        invalidate();
    }

    public void setGradientBackground(int[] mColors,float radius) {
        mIndicatorDrawable=new GradientDrawable();
        this.radius = radius;
        this.mColors = mColors;
        solidColor=0;
        strokeColor=0;
        strokeWidth=0;
        invalidate();
    }

    public void setSolidBackground(int solidColor,int radius) {
        mIndicatorDrawable=new GradientDrawable();
        this.solidColor = solidColor;
        this.radius = radius;
        mColors=null;
        strokeColor=0;
        strokeWidth=0;
        invalidate();
    }

    public void setStokeBackground(int strokeColor,int radius,int strokeWidth) {
        mIndicatorDrawable=new GradientDrawable();
        this.solidColor = 0;
        this.radius = radius;
        mColors=null;
        this.strokeColor=strokeColor;
        this.strokeWidth=strokeWidth;
        invalidate();
    }

    public void setStokeWithSolidBackground(int strokeColor,int strokeWidth,int solidColor,int radius) {
        mIndicatorDrawable=new GradientDrawable();
        this.solidColor = solidColor;
        this.radius = radius;
        mColors=null;
        this.strokeColor=strokeColor;
        this.strokeWidth=strokeWidth;
        invalidate();
    }


    public void setOrientation(GradientEditTextView.DIRECTION direction) {
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

        if(mColors!=null )
        {
            mIndicatorDrawable.setBounds(0,0,getWidth(),getHeight());
            mIndicatorDrawable.setColors(mColors);
            mIndicatorDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            mIndicatorDrawable.setOrientation(orientation);
            mIndicatorDrawable.setCornerRadius(radius);
            mIndicatorDrawable.draw(canvas);

            super.onDraw(canvas);
            return;
        }

        if(strokeColor!=0 && strokeWidth>0 && solidColor!=0)
        {
            mIndicatorDrawable.setBounds(0,0,getWidth(),getHeight());
            mIndicatorDrawable.setColor(solidColor);
            mIndicatorDrawable.setCornerRadius(radius);
            mIndicatorDrawable.draw(canvas);

            GradientDrawable mIndicatorDrawable2=new GradientDrawable();
            mIndicatorDrawable2.setStroke((int)strokeWidth,strokeColor);
            mIndicatorDrawable2.setBounds(0,0,getWidth(),getHeight());
            mIndicatorDrawable2.setCornerRadius(radius);
            mIndicatorDrawable2.draw(canvas);

            super.onDraw(canvas);
            return;
        }

        if(solidColor!=0)
        {
            mIndicatorDrawable.setBounds(0,0,getWidth(),getHeight());
            mIndicatorDrawable.setColor(solidColor);
            mIndicatorDrawable.setCornerRadius(radius);
            mIndicatorDrawable.draw(canvas);

            super.onDraw(canvas);
            return;
        }

        if(strokeColor!=0 && strokeWidth>0)
        {
            mIndicatorDrawable.setBounds(0,0,getWidth(),getHeight());
            mIndicatorDrawable.setStroke((int)strokeWidth,strokeColor);
            mIndicatorDrawable.setCornerRadius(radius);
            mIndicatorDrawable.draw(canvas);

            super.onDraw(canvas);
            return;
        }

    }
}
