package com.live.fox.svga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.live.fox.BuildConfig;
import com.live.fox.R;
import com.live.fox.utils.FileUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.opensource.svgaplayer.utils.SVGARect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 座驾和礼物
 */
public class ShowBigGiftFragment extends Fragment {

    /**
     * 礼物文件下载后存储位置
     */
    public static String DEFAULT_GIFT_DOWNLOAD_DIR = FileUtils.getExternalCardPath() +
            File.separator + BuildConfig.AppFlavor + File.separator;
    protected View rootView;

    List<GiftBean> giftbeanList = new ArrayList<>();

    //禮物是否在顯示
    boolean isAllGiftPlayEnd = true;

    ImageView ivShowbiggift;
    SVGAImageView mSVGAImageView;

    int screenHeight;
    int bottomHeight;

    public static ShowBigGiftFragment newInstance() {
        return new ShowBigGiftFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_big_gift, container, false);
        setView(rootView);
        return rootView;
    }

    private void setView(View view) {
        ivShowbiggift = view.findViewById(R.id.iv_show_big_gift);
        mSVGAImageView = view.findViewById(R.id.fragment_big_gift_svga);

        isAllGiftPlayEnd = true;

        screenHeight = ScreenUtils.getScreenHeight(requireActivity());
        bottomHeight = DeviceUtils.dp2px(requireActivity(), 200);

        mSVGAImageView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
                LogUtils.e("mSVGAImageViewCallback:" + "onPause");
            }

            @Override
            public void onFinished() {
                LogUtils.e("mSVGAImageViewCallback:" + "onFinished");
                playEnd();
            }

            @Override
            public void onRepeat() {
                LogUtils.e("mSVGAImageViewCallback:" + "onRepeat");
            }

            @Override
            public void onStep(int i, double v) {
                if (0 == i)
                    LogUtils.e("mSVGAImageViewCallback:" + "onStep");
            }
        });
    }


    public void showBigGiftEffect(int gid, int playCount, int giftType, String resourceUrl) {
        LogUtils.e("giftType:" + giftType + "," + resourceUrl);
        GiftBean gift = new GiftBean(gid, playCount, giftType, resourceUrl);
        giftbeanList.add(gift);
        LogUtils.e("isAllGiftPlayEnd:" + isAllGiftPlayEnd);
        if (isAllGiftPlayEnd && isAdded()) {
            showGift();
        }
    }

    private void showGift() {
        isAllGiftPlayEnd = false;
        GiftBean giftBean = giftbeanList.get(0);
        if (giftBean == null || StringUtils.isEmpty(giftBean.getResourceUrl())) return;
        String path = DEFAULT_GIFT_DOWNLOAD_DIR + ".tx/" + giftBean.getGid() + "/";
        if (!giftBean.getResourceUrl().endsWith(".zip")) {//svga
            path = path + giftBean.getGid();
            LogUtils.e("path:" + path);
            decodeSvga(new File(path));
        } else {
            //gif
            path = path + "b_";
            File fisrstFile = new File(path + "1.png");
            if (fisrstFile.exists()) {
                showFrameGif(getCompletePathList(path, giftBean));
            } else {
                playEnd();
                LogUtils.e("礼物文件不存在:" + fisrstFile.getAbsolutePath().toString());
            }
        }
    }

    public void playEnd() {
        if (giftbeanList.size() > 0) {
            giftbeanList.remove(0);
        }
        if (giftbeanList.size() > 0) {
            showGift();
        } else {
            isAllGiftPlayEnd = true;
        }
    }

    SVGAParser mSVGAParser;
    SVGAParser.ParseCompletion mParseCompletionCallback;

    /**
     * 播放svga
     */
    FileInputStream fileInputStream;

    private void decodeSvga(File file) {
        if (mSVGAParser == null) {
            mSVGAParser = new SVGAParser(requireActivity());
        }
        fileInputStream = null;
        if (mParseCompletionCallback == null) {
            mParseCompletionCallback = new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                    playSVGA(svgaVideoEntity);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError() {
                    LogUtils.e("礼物解析出错，请联系管理员");
                }
            };
        }
        try {
            fileInputStream = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fileInputStream);
            mSVGAParser.decodeFromInputStream(bis, file.getAbsolutePath(), mParseCompletionCallback, true, null, null);
        } catch (Exception e) {
            if (giftbeanList.size() > 0) {
                giftbeanList.remove(0);
            }
            isAllGiftPlayEnd = true;
            LogUtils.e("礼物解析出错，请联系管理员");
        }
    }

    /**
     * 播放svga
     */
    private void playSVGA(SVGAVideoEntity svgaVideoEntity) {
        if (mSVGAImageView != null) {
            SVGARect rect = svgaVideoEntity.getVideoSize();
            resizeSvgaImageView(rect.getWidth(), rect.getHeight());
            mSVGAImageView.setVideoItem(svgaVideoEntity);
//            mSvgaPlayTime = System.currentTimeMillis();
            mSVGAImageView.startAnimation();
//            if (mTempGifGiftBean != null) {
//                mGifGiftTip.setText(mTempGifGiftBean.getUserNiceName() + "  " + mSendString + mTempGifGiftBean.getGiftName());
//                mGifGiftTipGroup.setAlpha(1f);
//                mGifGiftTipShowAnimator.start();
//            }
        }
    }

    /**
     * 调整mSVGAImageView的大小
     */
    private void resizeSvgaImageView(double w, double h) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSVGAImageView.getLayoutParams();
        LogUtils.e("123123");
        params.height = screenHeight;
        params.topMargin = 0;
        mSVGAImageView.setLayoutParams(params);
        if (!giftbeanList.isEmpty() && giftbeanList.size() > 0) {
            LogUtils.e("type: " + giftbeanList.get(0).getType());
            LogUtils.e("View: " + mSVGAImageView.getWidth() + "," + mSVGAImageView.getHeight());
            LogUtils.e("SVGA: " + w + "," + h);
            if (giftbeanList.get(0).getType() == 1) { //0全屏 1座驾
                int resizeHeiht = (int) (mSVGAImageView.getWidth() * h / w);
                LogUtils.e("topMargin: " + params.topMargin);
                params.height = resizeHeiht;
                params.topMargin = screenHeight - resizeHeiht - bottomHeight;
                LogUtils.e("SVGA: " + params.topMargin);
                mSVGAImageView.setLayoutParams(params);
                return;
            }
            LogUtils.e("View AutoSize: " + params.width + "," + params.height);
        }
    }

    public void clearGiftShow() {
        giftbeanList.clear();
        mSVGAImageView.stopAnimation();
        mSVGAImageView.clearAnimation();
    }

    /**
     * 得到本地禮物的具体地址集合
     *
     * @return
     */
    public List<String> getCompletePathList(String startPath, GiftBean giftBean) {
        List<String> paths = new ArrayList<>();
        for (int i = 1; i <= giftBean.getBimgs(); i++) {
            String githPath = startPath + i + ".png";
            paths.add(githPath);
        }
        if (paths.size() == 0) {
            LogUtils.e("禮物還未下載");
//            Toast.makeText(getActivity(), "禮物還未下載", Toast.LENGTH_SHORT).show();
        }
        LogUtils.e("添加礼物路径 " + giftBean.getGid() + ", " + paths.size());
        return paths;
    }


    private void showFrameGif(List<String> paths) {
        // 每70ms一幀 循環播放動畫
        final FrameAnimationUtil frameAnimation = new FrameAnimationUtil(getActivity(), ivShowbiggift, paths, 30, false);
        frameAnimation.setAnimationListener(new FrameAnimationUtil.AnimationListener() {
            @Override
            public void onAnimationStart() {
                LogUtils.e("start");

            }

            @Override
            public void onAnimationEnd() {
                LogUtils.e("end");
                playEnd();
            }

            @Override
            public void onAnimationRepeat() {
                LogUtils.e("repeat");
            }
        });
    }

}

