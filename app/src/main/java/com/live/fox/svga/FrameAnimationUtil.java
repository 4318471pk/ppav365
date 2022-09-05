package com.live.fox.svga;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.live.fox.utils.LogUtils;

import java.io.FileInputStream;
import java.util.List;


public class FrameAnimationUtil {

    private boolean mIsRepeat;

    private AnimationListener mAnimationListener;

    private final ImageView mImageView;

    private final int[] mFrameRess;
    private List<String> pathList;

    /**
     * 每帧动画的播放间隔数组
     */
    private int[] mDurations;

    /**
     * 每帧动画的播放间隔
     */
    private int mDuration;

    /**
     * 下一遍动画播放的延迟时间
     */
    private int mDelay;

    private final int mLastFrame;

    private boolean mNext;

    private boolean mPause;

    private int mCurrentSelect;

    private int mCurrentFrame;

    private static final int SELECTED_A = 1;

    private static final int SELECTED_B = 2;

    private static final int SELECTED_C = 3;

    private static final int SELECTED_D = 4;

    Context context;

    /**
     * @param iv       播放动画的控件
     * @param frameRes 播放的图片数组
     * @param duration 每帧动画的播放间隔(毫秒)
     * @param isRepeat 是否循环播放
     */
    public FrameAnimationUtil(ImageView iv, int[] frameRes, int duration, boolean isRepeat) {
        this.mImageView = iv;
        this.mFrameRess = frameRes;
        this.pathList = null;
        this.mDuration = duration;
        this.mLastFrame = frameRes.length - 1;
        this.mIsRepeat = isRepeat;
        play(0);
    }


    public FrameAnimationUtil(Context context, ImageView iv, List<String> pathList, int duration, boolean isRepeat) {
        this.context = context;
        this.mImageView = iv;
        this.mFrameRess = null;
        this.pathList = pathList;
        this.mDuration = duration;
        this.mLastFrame = pathList.size() - 1;
        this.mIsRepeat = isRepeat;
        play(0);
    }


    /**
     * @param iv        播放动画的控件
     * @param frameRess 播放的图片数组
     * @param durations 每帧动画的播放间隔(毫秒)
     * @param isRepeat  是否循环播放
     */
    public FrameAnimationUtil(ImageView iv, int[] frameRess, int[] durations, boolean isRepeat) {
        this.mImageView = iv;
        this.mFrameRess = frameRess;
        this.mDurations = durations;
        this.mLastFrame = frameRess.length - 1;
        this.mIsRepeat = isRepeat;
        playByDurations(0);
    }

    /**
     * 循环播放动画
     *
     * @param iv        播放动画的控件
     * @param frameRess 播放的图片数组
     * @param duration  每帧动画的播放间隔(毫秒)
     * @param delay     循环播放的时间间隔
     */
    public FrameAnimationUtil(ImageView iv, int[] frameRess, int duration, int delay) {
        this.mImageView = iv;
        this.mFrameRess = frameRess;
        this.mDuration = duration;
        this.mDelay = delay;
        this.mLastFrame = frameRess.length - 1;
        playAndDelay(0);
    }

    /**
     * 循环播放动画
     *
     * @param iv        播放动画的控件
     * @param frameRess 播放的图片数组
     * @param durations 每帧动画的播放间隔(毫秒)
     * @param delay     循环播放的时间间隔
     */
    public FrameAnimationUtil(ImageView iv, int[] frameRess, int[] durations, int delay) {
        this.mImageView = iv;
        this.mFrameRess = frameRess;
        this.mDurations = durations;
        this.mDelay = delay;
        this.mLastFrame = frameRess.length - 1;
        playByDurationsAndDelay(0);
    }

    private void play(final int playPos) {
        mImageView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mPause) {
                    if (mPause) {
                        mCurrentSelect = SELECTED_D;
                        mCurrentFrame = playPos;
                        return;
                    }
                    return;
                }
                if (0 == playPos) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationStart();
                    }
                }
                try {
                    if (mFrameRess == null && pathList != null) {
                        //播放本地图片
                        showImage(playPos, false);
                    } else {
                        //播放资源图片
                        showImage(playPos, true);
                    }
                } catch (Exception e){
                    LogUtils.e("index:"+playPos+", 文件播放错误");
                    mImageView.setImageBitmap(null);
                    mAnimationListener.onAnimationEnd();
                    return;
                }


                if (playPos == mLastFrame) {
                    if (mIsRepeat) {
                        if (mAnimationListener != null) {
                            mAnimationListener.onAnimationRepeat();
                        }
                        play(0);
                    } else {
                        if (mAnimationListener != null) {
                            mImageView.setImageBitmap(null);
                            mAnimationListener.onAnimationEnd();
                        }
                    }
                } else {
                    play(playPos + 1);
                }
            }
        }, mDuration);
    }

    //isFromRes->true 播放资源文件图片  false 播放本地文件图片
    public void showImage(int index, boolean isFromRes) throws Exception {
        if (isFromRes) {
            mImageView.setBackgroundResource(mFrameRess[index]);
        } else {
            FileInputStream fis = new FileInputStream(pathList.get(index));
            mImageView.setImageBitmap(BitmapFactory.decodeStream(fis));
        }
    }


    private void playByDurationsAndDelay(final int i) {
        mImageView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mPause) {   // 暂停和播放需求
                    mCurrentSelect = SELECTED_A;
                    mCurrentFrame = i;
                    return;
                }
                if (0 == i) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationStart();
                    }
                }
                mImageView.setBackgroundResource(mFrameRess[i]);
                if (i == mLastFrame) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationRepeat();
                    }
                    mNext = true;
                    playByDurationsAndDelay(0);
                } else {
                    playByDurationsAndDelay(i + 1);
                }
            }
        }, mNext && mDelay > 0 ? mDelay : mDurations[i]);

    }

    private void playAndDelay(final int i) {
        mImageView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mPause) {
                    if (mPause) {
                        mCurrentSelect = SELECTED_B;
                        mCurrentFrame = i;
                        return;
                    }
                    return;
                }
                mNext = false;
                if (0 == i) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationStart();
                    }
                }
                mImageView.setBackgroundResource(mFrameRess[i]);
                if (i == mLastFrame) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationRepeat();
                    }
                    mNext = true;
                    playAndDelay(0);
                } else {
                    playAndDelay(i + 1);
                }
            }
        }, mNext && mDelay > 0 ? mDelay : mDuration);

    }

    private void playByDurations(final int i) {
        mImageView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mPause) {
                    if (mPause) {
                        mCurrentSelect = SELECTED_C;
                        mCurrentFrame = i;
                        return;
                    }
                    return;
                }
                if (0 == i) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationStart();
                    }
                }
                mImageView.setBackgroundResource(mFrameRess[i]);
                if (i == mLastFrame) {
                    if (mIsRepeat) {
                        if (mAnimationListener != null) {
                            mAnimationListener.onAnimationRepeat();
                        }
                        playByDurations(0);
                    } else {
                        if (mAnimationListener != null) {
                            mAnimationListener.onAnimationEnd();
                        }
                    }
                } else {
                    playByDurations(i + 1);
                }
            }
        }, mDurations[i]);

    }


    public static interface AnimationListener {

        /**
         * <p>Notifies the start of the animation.</p>
         */
        void onAnimationStart();

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         */
        void onAnimationEnd();

        /**
         * <p>Notifies the repetition of the animation.</p>
         */
        void onAnimationRepeat();
    }

    /**
     * <p>Binds an animation listener to this animation. The animation listener
     * is notified of animation events such as the end of the animation or the
     * repetition of the animation.</p>
     *
     * @param listener the animation listener to be notified
     */
    public void setAnimationListener(AnimationListener listener) {
        this.mAnimationListener = listener;
    }

    public void release() {
        pauseAnimation();
    }

    public void pauseAnimation() {
        this.mPause = true;
    }

    public boolean isPause() {
        return this.mPause;
    }

    public void restartAnimation() {
        if (mPause) {
            mPause = false;
            switch (mCurrentSelect) {
                case SELECTED_A:
                    playByDurationsAndDelay(mCurrentFrame);
                    break;
                case SELECTED_B:
                    playAndDelay(mCurrentFrame);
                    break;
                case SELECTED_C:
                    playByDurations(mCurrentFrame);
                    break;
                case SELECTED_D:
                    play(mCurrentFrame);
                    break;
                default:
                    break;
            }
        }
    }

}
