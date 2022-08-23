package com.live.fox.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;


public class SoundPlayUtil {
    private static MediaPlayer mPlayer;
    private static boolean isPause;

    public static void playSound(Context context, int resId, final OnFinishListener onFinishListener) {
        try {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.reset();
                    mPlayer.release();
                }
                mPlayer = null;
            }
            mPlayer = MediaPlayer.create(context, resId);
            mPlayer.setLooping(false);
            mPlayer.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    mPlayer.reset();
                    mPlayer.release();
                    return false;
                }
            });

            mPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            mPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayer.reset();
                    mPlayer.release();
                    mPlayer = null;
                    if (onFinishListener != null) {
                        onFinishListener.onFinish();
                    }
                }
            });
            mPlayer.start();

        } catch (Exception e) {
            release();
        }
    }

    public static void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            isPause = true;
        }
    }

    public static void resume() {
        if (mPlayer != null && isPause) {
            mPlayer.start();
        }
    }

    public static void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public static void release() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.release();
            mPlayer = null;
        }
    }


    public interface OnFinishListener {
        void onFinish();
    }
}
