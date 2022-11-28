package com.live.fox.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EdgeEffect;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.EdgeEffectCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.live.fox.R;
import com.live.fox.utils.LogUtils;
import com.live.fox.view.overscroll.RecyclerViewBouncy;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class LivingRecycleView extends RecyclerView {

    private ViewPager2 viewPager;
    OnTouchViewUpListener onTouchViewUpListener;

    public LivingRecycleView(@NonNull @NotNull Context context) {
        super(context);
    }

    public LivingRecycleView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LivingRecycleView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setOnTouchViewUpListener(OnTouchViewUpListener onTouchViewUpListener) {
        this.onTouchViewUpListener = onTouchViewUpListener;
    }

    public void setViewPager(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //重点 不让其他父控件处理
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(e);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        if(viewPager==null)
//        {
//            return super.onTouchEvent(e);
//        }
//        viewPager.setUserInputEnabled(false);
//
//        Log.e("onTouchEvent",e.getAction()+" ");
//        switch (e.getAction())
//        {
////            case MotionEvent.ACTION_DOWN:
////            case MotionEvent.ACTION_MOVE:
////                viewPager.setUserInputEnabled(false);
////                break;
//            case MotionEvent.ACTION_UP:
////                viewPager.setUserInputEnabled(true);
//                if(onTouchViewUpListener!=null)
//                {
//                    onTouchViewUpListener.onTouch();
//                }
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//
//
//        return super.onTouchEvent(e);
//    }


    @Override
    protected float getTopFadingEdgeStrength() {
        return super.getTopFadingEdgeStrength();
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        return 0;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

//        EdgeEffect mTopGlow = null;
//        try {
//            Field topGlow = getClass().getDeclaredField("mTopGlow");
//
//            if (topGlow != null) {
//                topGlow.setAccessible(true);
//                mTopGlow = (EdgeEffect) topGlow.get(this);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (mTopGlow != null) {
//            mTopGlow.setSize(0, 0);
//            mTopGlow.finish();
//
//        }
    }

    public interface OnTouchViewUpListener
    {
        void onTouch();
    }


    public void setEdgeColor(@ColorInt int color) {
        //自定义模糊阴影效果 极限回弹颜色
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                Class<?> c = Class.forName("android.widget.ScrollView");
                @SuppressLint("SoonBlockedPrivateApi") Field acEdgeGlowTop = c.getDeclaredField("mEdgeGlowTop");
                @SuppressLint("SoonBlockedPrivateApi") Field acEdgeGlowBottom = c.getDeclaredField("mEdgeGlowBottom");
                acEdgeGlowTop.setAccessible(true);
                acEdgeGlowBottom.setAccessible(true);
                EdgeEffect edgeEffect = new EdgeEffect(getContext());
                edgeEffect.setColor(color);
                acEdgeGlowTop.set(this, edgeEffect);
                acEdgeGlowBottom.set(this, edgeEffect);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            }
        } else {
            this.setEdgeEffectColor(color);
        }
    }
    public void setEdgeEffectColor(int edgeEffectColor) {
        // 通过该方法修改颜色
        ((ContextWrapperEdgeEffect) getContext()).setEdgeEffectColor(edgeEffectColor);

    }


    /**
     * 自定义滑动到顶部或者底部阴影颜色的ContextWrapper
     */
    public static class ContextWrapperEdgeEffect extends ContextWrapper {

        public static final String TAG = ContextWrapperEdgeEffect.class.getSimpleName();

        private ResourcesEdgeEffect mResourcesEdgeEffect;
        private int mColor;
        private Drawable mEdgeDrawable;
        private Drawable mGlowDrawable;

        public ContextWrapperEdgeEffect(Context context) {
            this(context, 0);
        }

        public ContextWrapperEdgeEffect(Context context, int color) {
            super(context);
            mColor = color;
            Resources resources = context.getResources();
            mResourcesEdgeEffect = new ResourcesEdgeEffect(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        }

        public void setEdgeEffectColor(int color) {
            mColor = color;
            if (mEdgeDrawable != null) mEdgeDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            if (mGlowDrawable != null) mGlowDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }

        @Override
        public Resources getResources() {
            return mResourcesEdgeEffect;
        }

        private class ResourcesEdgeEffect extends Resources {
            private int overscroll_edge = getPlatformDrawableId("overscroll_edge");
            private int overscroll_glow = getPlatformDrawableId("overscroll_glow");

            public ResourcesEdgeEffect(AssetManager assets, DisplayMetrics metrics, Configuration config) {
                //super(metrics, localConfiguration);
                super(assets, metrics, config);
            }

            private int getPlatformDrawableId(String name) {
                try {
                    int i = ((Integer) Class.forName("com.android.internal.R$drawable").getField(name).get(null)).intValue();
                    return i;
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, "Cannot find internal resource class");
                    return 0;
                } catch (NoSuchFieldException e1) {
                    Log.e(TAG, "Internal resource id does not exist: " + name);
                    return 0;
                } catch (IllegalArgumentException e2) {
                    Log.e(TAG, "Cannot access internal resource id: " + name);
                    return 0;
                } catch (IllegalAccessException e3) {
                    Log.e(TAG, "Cannot access internal resource id: " + name);
                }
                return 0;
            }

            @Override
            public Drawable getDrawable(int resId) throws Resources.NotFoundException {
                Drawable ret = null;
                if (resId == this.overscroll_edge) {
                    mEdgeDrawable = ContextWrapperEdgeEffect.this.getBaseContext().getResources().getDrawable(R.drawable.ic_arrow_solid);
//                    mEdgeDrawable = ContextWrapperEdgeEffect.this.getBaseContext().getResources().getDrawable(R.drawable.community_overscroll_edge);
                    ret = mEdgeDrawable;
                } else if (resId == this.overscroll_glow) {
//                    mGlowDrawable = ContextWrapperEdgeEffect.this.getBaseContext().getResources().getDrawable(R.drawable.community_overscroll_glow);
                    mGlowDrawable = ContextWrapperEdgeEffect.this.getBaseContext().getResources().getDrawable(R.drawable.ic_arrow_solid);
                    ret = mGlowDrawable;
                } else return super.getDrawable(resId);

                if (ret != null) {
                    ret.setColorFilter(mColor, PorterDuff.Mode.MULTIPLY);
                }

                return ret;
            }
        }
    }

}
