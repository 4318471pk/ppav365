package com.live.fox.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 getXY             : 获取View的x和y坐标
 getWidth          : 获取控件的宽度 在控件显示后调用才会得到准确的值
 getHeight         : 获取控件的高度 在控件显示后调用才会得到准确的值
 setLayout         : 改变控件的位置
 */
public class ViewUtils {

	//得到View的x.y坐标 如果用的频繁的话建议复制进代码中 去掉static
	public static int[] getXY(View view) {
		int[] xyPoint = new int[2];
		//获取点击按钮的坐标
		view.getLocationOnScreen(xyPoint);
		return xyPoint;
	}

	//获取控件的宽度 在控件显示后调用才可以得到值
	public static int getWidth(View view) {
		return view.getWidth();
	}

	//获取控件的高度 在控件显示后调用才可以得到值
	public static int getHeight(View view) {
		return view.getHeight();
	}


	//改变控件的位置
	public void setLayout(View view, int x, int y, int w, int h) {
		view.layout(x, y, w+x, h+y);
	}

	public static void setVisiableWithAlphaAnim(final View view){
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setDuration(600);
		animation.setFillAfter(true);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.VISIBLE);
				view.clearAnimation();
			}
		});
		view.setAnimation(animation);
		animation.start();
	}

	public static void setGoneWithAlphaAnim(final View view){
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(360);
		animation.setFillAfter(true);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.GONE);
				view.clearAnimation();
			}
		});
		view.setAnimation(animation);
		animation.start();
	}


	public static void setInvisibleWithAlphaAnim(final View view){
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(600);
		animation.setFillAfter(true);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.INVISIBLE);
				view.clearAnimation();
			}
		});
		view.setAnimation(animation);
		animation.start();
	}

}
