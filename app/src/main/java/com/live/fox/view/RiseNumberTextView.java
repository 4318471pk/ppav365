package com.live.fox.view;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.utils.NumberUtils;

import java.lang.ref.WeakReference;


@SuppressLint("AppCompatCustomView")
public class RiseNumberTextView extends RelativeLayout {
	/**
	 * 默认轮播时间间隔
	 */
	private final static int SLEEP_TIME = 200;

	/**
	 * 默认动画执行时间
	 */
	private final static int ANIM_DURATION = 200;
	private boolean mSingleLine;
	private int mTextColor;
	private int mTextSize;
	/**
	 * 滚动方向
	 */
	private int scrollOrientation;


	private TextView mTvContentTop;

	private TextView mTvContentBottom;

	/**
	 * 是否运行轮播图
	 */
	protected boolean mIsRun;

	/**
	 * 自动轮播使用的handler
	 */
	private Handler mHandler;

	private int heightSize;
	private int minRandomNum=10000;
	private int maxRandomNum=99999;
	private String stopString;
	public RiseNumberTextView(Context context) {
		super(context);

		init(context);
	}

	public RiseNumberTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerticalScrollTextView);
		scrollOrientation = array.getInt(R.styleable.VerticalScrollTextView_vst_scrollOrientation, 0);
		mTextSize = array.getDimensionPixelSize(R.styleable.VerticalScrollTextView_vst_textSize,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
						12, getResources().getDisplayMetrics()));
		mTextColor = array.getColor(R.styleable.VerticalScrollTextView_vst_textColor, Color.BLACK);
		mSingleLine = array.getBoolean(R.styleable.VerticalScrollTextView_vst_singleLine, true);
		array.recycle();

		init(context);
	}

	private void init(Context context) {
		initTextView(context);
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams
				(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.CENTER_VERTICAL);

		addView(mTvContentTop, lp1);
		addView(mTvContentBottom, lp1);

		mHandler = new SliderScrollHandler(this);
	}
//	public void setBottomTextColor(int myTextColor){
//		mTvContentBottom.setTextColor(myTextColor);
//	}
	public void setTBTextColor(int myTextColor){
		mTvContentTop.setTextColor(myTextColor);
		mTvContentBottom.setTextColor(myTextColor);
	}

	private void initTextView(Context context) {
		mTvContentTop = new TextView(context);
		mTvContentTop.setTextColor(mTextColor);
		mTvContentTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
		mTvContentTop.setSingleLine(mSingleLine);

		mTvContentBottom = new TextView(context);
		mTvContentBottom.setTextColor(mTextColor);
		mTvContentBottom.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
		mTvContentBottom.setSingleLine(mSingleLine);
		if (mSingleLine) {
			mTvContentTop.setEllipsize(TextUtils.TruncateAt.END);

			mTvContentBottom.setEllipsize(TextUtils.TruncateAt.END);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		heightSize = getMeasuredHeight();
	}

	/**
	 * 滚动
	 */
	private void autoSlider() {
		mTvContentTop.setText(NumberUtils.getRandom(minRandomNum, maxRandomNum)+"");
		startTopAnim();
		mTvContentBottom.setText(NumberUtils.getRandom(minRandomNum, maxRandomNum)+"");
		startBottomAnim();
	}

	private void startTopAnim() {
		int value = -heightSize;  //默认朝上
		if (scrollOrientation != 0) {
			//朝下
			value = heightSize;
		}

		ObjectAnimator.ofFloat(mTvContentTop, "translationY", 0F, value)
				.setDuration(ANIM_DURATION)
				.start();
	}

	private void startBottomAnim() {
		int value = heightSize;  //默认朝上
		if (scrollOrientation != 0) {
			//朝下
			value = -heightSize;
		}

		ObjectAnimator.ofFloat(mTvContentBottom, "translationY", value, 0F)
				.setDuration(ANIM_DURATION)
				.start();
	}

	/**
	 * 轮播滚动Handler
	 */
	private class SliderScrollHandler extends Handler {
		private WeakReference<RiseNumberTextView> mSliderView;

		SliderScrollHandler(RiseNumberTextView sliderView) {
			mSliderView = new WeakReference<>(sliderView);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					RiseNumberTextView sliderView = mSliderView.get();
					if (sliderView != null && mSliderView.get().mIsRun) {
						sliderView.autoSlider();
						//重复
						this.sendEmptyMessageDelayed(0, SLEEP_TIME);
					}
					break;
				case 1:
					mIsRun = false;
					this.removeCallbacksAndMessages(null);
					mTvContentBottom.setTextColor(Color.parseColor("#E7408F"));
					mTvContentBottom.setText(stopString);
//					mTvContentTop.setTextColor(Color.parseColor("#E7408F"));
//					mTvContentTop.setText(stopString);
					break;
				case 2:
					mIsRun = false;
					this.removeCallbacksAndMessages(null);
//					mTvContentBottom.setTextColor(Color.parseColor("#E7408F"));
//					mTvContentBottom.setText(stopString);
					mTvContentTop.setTextColor(Color.parseColor("#E7408F"));
					mTvContentTop.setText(stopString);
					break;
			}
		}
	}


	/**
	 * Description: 开始轮播
	 * @param i
	 * @param i1
	 */
	public void startPlay(int i, int i1) {
		if (mHandler != null) {
			minRandomNum=i;
			maxRandomNum=i1;
			mIsRun = true;
			mHandler.removeCallbacksAndMessages(null);
			mHandler.sendEmptyMessageDelayed(0, 0);
		}
	}

	/**
	 * Description: 暂停轮播
     * @param s
     */
	public void stopPlay(String s,int millision,int whatNum) {
		if (mHandler != null) {
			stopString=s;
			mHandler.sendEmptyMessageDelayed(whatNum,millision);
		}
	}

}
