package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundLinearLayout;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.TuiGuangPageAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityPromoMaterialBinding;
import com.live.fox.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class PromoMaterialActivity extends BaseBindingViewActivity {

    final int code = 00001;
    ActivityPromoMaterialBinding mBind;

    String livingScale = "0";
    String gameScale = "0";


    //适配器
    //private FancyCoverFlowSampleAdapter adapter = null;

    //private TuiguangAdapter adapter;
    private TuiGuangPageAdapter adapter;
    private List<Boolean> list = new ArrayList<>();
    int nowPos = 0;

    CircleAdapter circleAdapter;

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, PromoMaterialActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void onClickView(View view) {
        if (view == mBind.tvChangeScale) {
            AgencyMoneyPlanActivity.startActivity(this, false, livingScale, gameScale, code);

        } else if (view == mBind.tvCopyLink) {

        } else if (view == mBind.tvSaveQrcode) {
            //ImageUtil.saveBmp2Gallery(this, new Bitmap(), "");
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_promo_material;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        mBind.setClick(this);
        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});
        setTipsText(livingScale, gameScale);

        initLayout();

    }

    private void setTipsText(String str1, String str2){
        String s = String.format(getString(R.string.promo_tips), str1+"%", str2 + "%");
        mBind.tvTips.setText(s);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code) {
            livingScale = data.getStringExtra("living");
            gameScale = data.getStringExtra("game");
            setTipsText(livingScale, gameScale);
        }
    }


    private void initLayout() {

        initData();
        adapter = new TuiGuangPageAdapter(this,list);
        mBind.vp.setOffscreenPageLimit(2);
        mBind.vp.setPageTransformer(false,new ZoomOutPageTransformer());
        mBind.vp.setAdapter(adapter);
        mBind.vp.setCurrentItem(Integer.MAX_VALUE/2-(Integer.MAX_VALUE/2%list.size()));

        mBind.vp.setPageMargin(-100);

        mBind.viewPg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mBind.vp.dispatchTouchEvent(event);
            }
        });

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBind.vp.getLayoutParams();
        lp.width = ScreenUtils.getScreenWidth(PromoMaterialActivity.this)
                - ScreenUtils.dp2px(PromoMaterialActivity.this,50);


        circleAdapter = new CircleAdapter(list);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.rcCircle.setLayoutManager(layoutManager2);
        mBind.rcCircle.setAdapter(circleAdapter);

        mBind.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int newPos=position%(list.size());//对应数据中的位置
                for (int i = 0; i < list.size(); i++) {
                    if (i == newPos) {
                        list.set(i, true);
                    } else{
                        list.set(i, false);
                    }

                }
                circleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//
//        ViewTreeObserver vto = mBind.viewPg.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                mBind.viewPg.getViewTreeObserver().removeOnPreDrawListener(this);
//                int height = mBind.viewPg.getMeasuredHeight();
//                int width = mBind.viewPg.getMeasuredWidth();
//                LogUtils.e("高度：" + height + "  "  + width);
//
//
//                return true;
//            }
//        });
//



    }

    private void initData() {
        for (int i = 0; i < 9; i++) {
            if (i == 0) {
                list.add(true);
            } else {
                list.add(false);
            }
        }





    }

    /**
     * FancyCoverFlow属性使用
     *
     * // 未选中的透明度
     * this.fancyCoverFlow.setUnselectedAlpha(0.0f);
     * // 未选中的饱和度
     * this.fancyCoverFlow.setUnselectedSaturation(0.0f);
     * // 未选中的比例
     * this.fancyCoverFlow.setUnselectedScale(0.8f);
     * // child间距
     * this.fancyCoverFlow.setSpacing(-60);
     * // 旋转度数
     * this.fancyCoverFlow.setMaxRotation(0);
     * // 非选中的重心偏移,负的向上
     * this.fancyCoverFlow.setScaleDownGravity(-1f);
     * // 作用距离
     * this.fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
     */

    public LinearSnapHelper getSnapHelper() {
        return new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                return targetPosition;
            }
        };
    }


    public class Scalltransformer implements ViewPager.PageTransformer {
        private float MINSCALE=0.8f;//最小缩放值

        /**
         * position取值特点：
         * 假设页面从0～1，则：
         * 第一个页面position变化为[0,-1]
         * 第二个页面position变化为[1,0]
         *
         * @param view
         * @param v
         */
        @Override
        public void transformPage(@NonNull View view, float v) {

            float scale;//view  应缩放的值
            if(v>1||v<-1){
                scale=MINSCALE;
            }else if(v<0){
                scale=MINSCALE+(1+v)*(1-MINSCALE);
            }else{
                scale=MINSCALE+(1-v)*(1-MINSCALE);
            }
            view.setScaleY(scale);
            view.setScaleX(scale);
            view.setAlpha(1);
        }
    }


    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        public static final float MIN_SCALE = 0.8f;//原图片缩小0.8倍
        private static final float MIN_ALPHA = 0.6f;//透明度

        public void transformPage(View page, float position) {
            if (position < -1) {//[-Infinity,-1)左边露出半个的page
                page.setAlpha(MIN_ALPHA);//设置page的透明度
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            } else if (position <= 1) {
                if (position < 0) {//(0,-1] 第一页向左移动
                    if (position < -0.2f)//最大缩小到0.8倍
                        position = -0.2f;
                    page.setAlpha(1f + position*2);
                    page.setScaleY(1f + position);
                    page.setScaleX(1f + position);
                } else {//[1,0] 第二页向左移动 成currentItem
                    if (position > 0.2)
                        position = 0.2f;
                    page.setAlpha(1f -position*2);
                    page.setScaleY(1f - position);
                    page.setScaleX(1f - position);
                }
            } else {//(1,+Infinity]右边露出半个的page
                page.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            }
        }
    }

    private class CircleAdapter extends BaseQuickAdapter<Boolean, BaseViewHolder> {


        public CircleAdapter(List data) {
            super(R.layout.item_circle, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Boolean data) {
            RoundLinearLayout view = helper.getView(R.id.view);
            if (data) {
                view.getDelegate().setBackgroundColor(mContext.getResources().getColor(R.color.colorA800FF));
            } else {
                view.getDelegate().setBackgroundColor(mContext.getResources().getColor(R.color.colorD2CDE0));
            }

        }


    }
}