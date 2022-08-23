package com.live.fox.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.live.fox.R;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.glide2transformation.BorderTransformation;
import com.live.fox.utils.glide2transformation.CircleWithBorderTransformation;
import com.live.fox.utils.glide2transformation.RoundedWithBoardTransformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;

/**
 * Glide 4.4版本的工具类
 * 配置为：
 * compile 'com.github.bumptech.glide:glide:4.4.0'
 * annotationProcessor 'com.github.bumptech.glide:compiler:4.4.0'
 * compile 'jp.wasabeef:glide-transformations:3.0.1' //Glide-transformations图片变换
 * 如果低于27则配置为
 * compile 'com.github.bumptech.glide:glide:4.4.0'
 * compile 'jp.wasabeef:glide-transformations:3.0.1' //Glide-transformations图片变换
 */

public class GlideUtils {

    /**
     * 默认预加载图、错误图
     * 如果实际图片没有填满整个控件，建议预加载图设置成 R.color.transparent
     */
    public static int defaultPlaceImg;
    public static int defaultErrorImg;


    /************************** Glide常用方法********************************************************************************/

    /**
     * 加载图片->无预览图&错误图
     */
    public static void loadImage(Context context, Object path, ImageView imageView) {
        loadImage(context, path, R.color.transparent, R.color.transparent, true, imageView);
    }

    @SafeVarargs
    public static void loadImage(Context context, Object path, ImageView imageView, Transformation<Bitmap>... transformations) {
        loadImage(context, path, R.color.transparent, R.color.transparent, false, imageView, transformations);
    }

    /**
     * 加载图片->默认预览图&错误图
     */
    public static void loadDefaultImage(Context context, Object path, ImageView imageView) {
        loadImage(context, path, defaultPlaceImg, defaultErrorImg, true, imageView);
    }

    /**
     * 加载图片->最终显示成 圆形图
     * 注：圆形图不要在方法里设置预加载图
     */
    public static void loadDefaultCircleImage(Context context, Object path, ImageView imageView) {
        loadCircleImage(context, path, defaultPlaceImg, defaultErrorImg, imageView);
    }

    /**
     * 加载图片->最终显示成 圆形带边框的图
     * 注：圆形图不要在方法里设置预加载图
     */
    public static void loadDefaultCircleRingImage(Context context, Object path, ImageView imageView) {
        loadCircleRingImage(context, path, 1, Color.GREEN, defaultPlaceImg, defaultErrorImg, imageView);
    }

    /**
     * 加载图片->最终显示成 圆角图
     */
    public static void loadDefaultRoundedImage(Context context, Object path, ImageView imageView) {
        loadRoundedImage(context, DeviceUtils.dp2px(context, 8), path, defaultPlaceImg, defaultErrorImg, imageView);
    }


    private static boolean isUrl(String path) {
        return path.startsWith("http:") || path.startsWith("https:");
    }

    /**
     * 加载图片->最终显示成 圆角带边框图
     */
    public static void loadDefaultRoundedRingImage(Context context, Object path, ImageView imageView) {
        loadRoundedRingImage(context, path, 16, Color.GREEN, defaultPlaceImg, defaultErrorImg, imageView);
    }

    /**
     * 加载图片->最终显示成 圆角带边框图ni d
     */
    public static void loadDefaultdRingImage(Context context, Object path, ImageView imageView) {
        loadRingImage(context, path, 1, Color.GREEN, defaultPlaceImg, defaultErrorImg, imageView);
    }


    /**
     * 加载图片+高斯模糊(不能达到完全模糊)
     */
    public static void loadDefaultMohuImage(Context context, Object path, ImageView imageView) {
        loadImageWithMohuTrans(context, path, 25, defaultPlaceImg, defaultErrorImg, imageView);
    }


    /************************** 常用方法 End********************************************************************************/


    /**
     * 判断 context 是否能用
     */
    @SuppressLint("NewApi")
    private static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            return activity.isDestroyed() || activity.isFinishing();
        }
        return false;
    }


    @SafeVarargs
    public static void loadImage(Context context, Object path, int placeholderImg, int errorImg, ImageView imageView, Transformation<Bitmap>... transformations) {
        loadImage(context, path, placeholderImg, errorImg, true, imageView, transformations);
    }

    /**
     * 加载图片->最终显示都调用此方法
     * 圆形图片：      new CircleCrop() 注：圆形需要配合circleimageview使用
     * 圆形带边框图片： new CircleWithBorderTransformation(borderWidth, borderColor)
     * 圆角图片：      new RoundedCorners(roundingRadius)
     * 圆角带边框图片： new RoundedWithBoardTransformation(roundingRadius, borderColor)
     * 高斯模糊效果：   new BlurTransformation(context, mohuValue)
     * 图片灰度效果：   new GrayscaleTransformation()
     * 图片加边框：     new BorderTransformation(1, Color.BLUE)
     */
    @SafeVarargs
    public static void loadImage(Context context, Object path, int placeholderImg, int errorImg, boolean isFade, ImageView imageView, Transformation<Bitmap>... transformations) {
        if ((isValidContextForGlide(context))) return;
        RequestOptions options;
        if (placeholderImg > 0) {
            options = new RequestOptions().placeholder(placeholderImg).error(errorImg);
        } else {
            options = new RequestOptions();
        }

        if (transformations != null && transformations.length > 0) {
            options.transform(transformations);
        }

        if (isFade) {
            Glide.with(context)
                    .load(replaceDomain(path))
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(replaceDomain(path))
                    .apply(options)
                    .into(imageView);
        }
    }

    //替换短视频 社区 图片、视频的域名
    public static Object replaceDomain(Object path) {
        if (path == null)
            return "";
        if (path instanceof Integer) {
            return path;
        }

        String url = "";
        try {
            url = path.toString();
        } catch (Exception e) {
            return "";
        }

        if (StringUtils.isEmpty(url))
            return "";

        String domain = SPUtils.getInstance("domain").getString("domain", "");
        if (StringUtils.isEmpty(domain)) {
            domain = "www.baudu.com";
        }

        Uri mUri = Uri.parse(url);

        String startStr = url.startsWith("http://") ? "http://" : "https://";
        url = url.replace(mUri.getScheme() + "://" + mUri.getAuthority(), startStr + domain);
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return url;
        }
        return "";
    }


    /**
     * 圆形图
     * 注：圆形图不要在方法里设置预加载图
     */
    public static void loadCircleImage(Context context, Object path, int placeholderImg, int errorImg, ImageView imageView) {
        if ((isValidContextForGlide(context))) return;
        loadImage(context, path, placeholderImg, errorImg, true, imageView, new CircleCrop());
    }

    /**
     * 圆形+边框
     * 注：圆形图不要在方法里设置预加载图
     */
    public static void loadCircleRingImage(Context context, Object path, int borderWidth, int borderColor, int placeholderImg, int errorImg, ImageView imageView) {
//        if((isValidContextForGlide(context))) return;
//        RequestOptions options = new RequestOptions().placeholder(placeholderImg).error(errorImg)
//                .transforms(new CircleWithBorderTransformation(borderWidth, borderColor));
//        Glide.with(context)
//                .load(path)
//                .apply(options)
//                .into(imageView);
        loadImage(context, path, placeholderImg, errorImg, true, imageView, new CircleWithBorderTransformation(borderWidth, borderColor));
    }

    /**
     * 圆形+1px边框
     * 注：圆形图不要在方法里设置预加载图
     */
    public static void loadCircleOnePxRingImage(Context context, Object path, int borderColor, int placeholderImg, int errorImg, ImageView imageView) {
        loadImage(context, path, placeholderImg, errorImg, true, imageView, new CircleWithBorderTransformation(DeviceUtils.px2dp(context, 1), borderColor));
    }

    /**
     * 圆角图
     */
    public static void loadRoundedImage(Context context, int radius, Object path, int placeholderImg, int errorImg, ImageView imageView) {
//        if((isValidContextForGlide(context))) return;
//        RequestOptions options = null;
//        if(placeholderImg>0){
//            options = new RequestOptions().placeholder(placeholderImg).error(errorImg)
//                    .transforms(new CenterCrop(), new RoundedCorners(radius));
//        }else {
//            options = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(radius));
//        }
//
//        Glide.with(context)
//                .load(path)
//                .apply(options)
//                .into(imageView);

        loadImage(context, path, placeholderImg, errorImg, true, imageView, new CenterCrop(), new RoundedCorners(radius));
    }


    /**
     * 圆角+边框
     */
    public static void loadRoundedRingImage(Context context, Object path, int roundingRadius, int borderColor, int placeholderImg, int errorImg, ImageView imageView) {
//        if((isValidContextForGlide(context))) return;
//        RequestOptions options = new RequestOptions().transforms(new RoundedWithBoardTransformation(roundingRadius, borderColor));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
        loadImage(context, path, placeholderImg, errorImg, true, imageView, new RoundedWithBoardTransformation(roundingRadius, borderColor));
    }

    /**
     * 原图片+边框
     */
    public static void loadRingImage(Context context, Object path, int borderWidth, int borderColor, int placeholderImg, int errorImg, ImageView imageView) {
//        if((isValidContextForGlide(context))) return;
//        RequestOptions options = new RequestOptions().transforms(new RoundedWithBoardTransformation(roundingRadius, borderColor));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
        loadImage(context, path, placeholderImg, errorImg, true, imageView, new BorderTransformation(borderWidth, borderColor));
    }


    /**
     * 加载图片+高斯模糊(不能达到完全模糊)
     */
    public static void loadImageWithMohuTrans(Context context, Object path, int mohuValue, int placeholderImg, int errorImg, ImageView imageView) {
        // radius: 离散半径/模糊度 默认25 取值越大越模糊
        // 推荐值:  25(ImageView小看不清 ImageView大时看的清 推荐小图时使用此值) 200(ImageView全屏展示时也看不清 推荐ImageView全屏使用时用此值)
//        RequestOptions options = new RequestOptions().placeholder(placeholderImage).error(errorImage).transforms(new BlurTransformation(mohuValue));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
        loadImage(context, path, placeholderImg, errorImg, true, imageView, new BlurTransformation(mohuValue));
    }


    /**
     * 加载图片->最终显示成 灰度图
     */
    public static void loadGrayImage(Context context, Object path, int placeholderImg, int errorImg, ImageView imageView) {
//        RequestOptions options = new RequestOptions().transforms(new GrayscaleTransformation());
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
        loadImage(context, path, placeholderImg, errorImg, true, imageView, new CenterCrop(), new GrayscaleTransformation());
    }

    /**
     * 加载图片->改变显示的图片大小（原控件的大小不会变,只会改变显示图片的大小）
     * 注：失败、没有效果
     */
    public static void loadImageWithSize(Context context, Object path, int width, int height, int placeholderImg, int errorImg, ImageView imageView) {
        RequestOptions options = new RequestOptions().placeholder(placeholderImg).error(errorImg).override(width, height);
        Glide.with(context).load(path).apply(options).into(imageView);
    }


    /**
     * 加载图片 并在图上盖一层颜色模板 颜色格式 类似0x7900CCCC
     */
    public static void loadImageWithColorTrans(Context context, Object path, int color, ImageView imageView) {
        // Color ：蒙层颜色值
        RequestOptions options = new RequestOptions().transforms(new ColorFilterTransformation(color));
        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }


    /**
     * 最终会显示 原图+遮罩图形状 适用一些不规则的图形
     */
    public static void loadImageWithMaskshapeTrans(Context context, Object path, int maskResourceId, ImageView imageView) {
        RequestOptions options = new RequestOptions().transforms(new MultiTransformation<Bitmap>(new CenterCrop(), new MaskTransformation(maskResourceId)));
        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }


    /**
     * 跳过内存缓存 直接读取硬盘的图片
     */
    public static void loadImageCache(Context context, Object path, ImageView imageView) {
        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    //设置下载优先级
//    public static void loadImagePriority(Context context, Object path, ImageView imageView, ImageView imageView) {
//        RequestOptions options = new RequestOptions().priority(Priority.NORMAL);
//        Glide.with(context).load(path).apply(options).into(imageView);
//    }


    /**
     * 显示图片 并设置设置缓存策略
     */
    public static void loadImageDiskCache(Context context, Object path, DiskCacheStrategy strategy, ImageView imageView) {
        RequestOptions options = new RequestOptions().diskCacheStrategy(strategy);
        Glide.with(context).load(path).apply(options).into(imageView);
    }


    /**
     * 设置缩略图支持 会先加载缩略图
     */
    public static void loadImageThumbnail(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).thumbnail(0.1f).into(imageView);
    }

    /**
     * 设置动态转换
     */
    public static void loadImageWithCrossFade(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    /**
     * 设置动态GIF加载方式
     */
    public static void loadImageByGif(Context context, Object path, ImageView imageView) {
        Glide.with(context).asGif().load(path).into(imageView);
    }

    /**
     * 设置静态GIF加载方式
     */
    public static void loadImageByBitmap(Context context, Object path, ImageView imageView) {
        Glide.with(context).asBitmap().load(path).into(imageView);
    }

    /**
     * 设置监听请求接口
     */
    public static void loadImageListener(Context context, Object path, ImageView imageView, RequestListener<Drawable> requstlistener) {
        Glide.with(context).load(path).listener(requstlistener).into(imageView);
    }


    /**
     * 设置要加载的内容,项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排
     */
    public static void loadImageContent(Context context, Object path, SimpleTarget<Drawable> simpleTarget) {
        Glide.with(context).load(path).transition(DrawableTransitionOptions.withCrossFade()).into(simpleTarget);
    }

    /**
     * 清除本地缓存
     */
    public static void clearDiskCache(final Context context) {
        //理磁盘缓存 需要在子线程中执行
        if (isValidContextForGlide(context)) return;
        Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    public void accept(Integer s) throws Exception {
                        Glide.get(context).clearDiskCache();
                    }
                });
    }


    /**
     * 清除内存缓存
     */
    public static void clearMemory(final Context context) {
        if (isValidContextForGlide(context)) return;
        // 必须在UI线程中调用
        Glide.get(context).clearMemory();

    }


    /**
     * 下载图片，并在媒体库中显示
     * (记得调用这个方法前需要做权限处理)
     *
     * @param context
     * @param imgUrl
     */
    @SuppressLint("CheckResult")
    public static void downloadImageToGallery(final Context context, final String imgUrl) {
//        String extension = MimeTypeMap.getFileExtensionFromUrl(imgUrl);
//        Observable.create((ObservableOnSubscribe<File>)
//                emitter -> {
//                    // Glide提供了一个download() 接口来获取缓存的图片文件，
//                    // 但是前提必须要设置diskCacheStrategy方法的缓存策略为
//                    // DiskCacheStrategy.ALL或者DiskCacheStrategy.SOURCE，
//                    // 还有download()方法需要在子线程里进行
//                    File file = Glide.with(context).download(imgUrl).submit().get();
//                    String fileParentPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/easyGlide/Image";
//                    File appDir = new File(fileParentPath);
//                    if (!appDir.exists()) {
//                        appDir.mkdirs();
//                    } //获得原文件流
//                    FileInputStream fis = new FileInputStream(file);
//                    //保存的文件名
//                    String fileName = "easyGlide_" + System.currentTimeMillis() + "." + extension;
//                    //目标文件
//                    File targetFile = new File(appDir, fileName);
//                    //输出文件流
//                    FileOutputStream fos = new FileOutputStream(targetFile);
//                    // 缓冲数组
//                    byte[] b = new byte[1024 * 8];
//                    while (fis.read(b) != -1) {
//                        fos.write(b);
//                    }
//                    fos.flush();
//                    fis.close();
//                    fos.close();
//                    //扫描媒体库
//                    String mimeTypes = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//                    MediaScannerConnection.scanFile(context, new String[]{targetFile.getAbsolutePath()},
//                            new String[]{mimeTypes}, null);
//                    emitter.onNext(targetFile);
//                }).subscribeOn(Schedulers.io())
//                //发送事件在io线程
//                .observeOn(AndroidSchedulers.mainThread())
//                //最后切换主线程提示结果
//                .subscribe(file -> Toast.makeText(context, "保存图片成功", Toast.LENGTH_SHORT).show(),
//                        throwable -> Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show());
    }

    /**
     * 保存图片到图库
     *
     * @param bmp
     */
    public static void saveImage2Gallery(Bitmap bmp, String bitName) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(),
                "xhr");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String fileName = bitName + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param bmp     获取的bitmap数据
     * @param picName 自定义的图片名
     */
    public static void saveBmp2Gallery(Context context, Bitmap bmp, String picName) {
        saveImage2Gallery(bmp, picName);
        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;


        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;
        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".jpg");
            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        ToastUtils.showShort("图片保存成功");

    }


    //最终会显示 原图的卡通效果 (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
    public static void loadImageWithToonFilterTrans(Context context, Object path, ImageView imageView) {
        // threshold ：阀值（单参构造器 - 默认0.2F）影响色块边界的描边效果
        // quantizationLevels ：量化等级（单参构造器 - 默认10.0F）影响色块色彩
        RequestOptions options = new RequestOptions().transforms(new ToonFilterTransformation(0.2F, 10F));
        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    //最终会显示 原图的乌墨色效果(类似原图的暖色调效果) (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
    public static void loadImageWithSepiaFilterTrans(Context context, Object path, ImageView imageView) {
        // intensity 渲染强度（单参构造器 - 默认1.0F）
        RequestOptions options = new RequestOptions().transforms(new SepiaFilterTransformation(1.0F));
        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    //    //最终会显示 原图的对比度效果 (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
//    public static void loadImageWithContrastFilterTrans(Context context, Object path, ImageView imageView) {
//        // contrast 对比度 （单参构造器 - 默认1.0F）
//        RequestOptions options = new RequestOptions().transforms(new ContrastFilterTransformation(3F));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
//    }
//
//    //最终会显示 原图的反转效果 (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
//    public static void loadImageWithInvertFilterTrans(Context context, Object path, ImageView imageView) {
//        RequestOptions options = new RequestOptions().transforms(new InvertFilterTransformation());
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
//    }
//
//    //最终会显示 原图的像素化效果(马赛克效果) (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
//    public static void loadImageWithPixelationFilterTrans(Context context, Object path, ImageView imageView) {
//        // pixel 像素值（单参构造器 - 默认10F）数值越大，绘制出的像素点越大，图像越失真
//        RequestOptions options = new RequestOptions().transforms(new PixelationFilterTransformation(20F));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
//    }
//
//    //最终会显示 原图的素描效果 (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
//    public static void loadImageWithSketchFilterTrans(Context context, Object path, ImageView imageView) {
//        RequestOptions options = new RequestOptions().transforms(new SketchFilterTransformation());
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
//    }
//
//    //最终会显示 原图围绕中心的旋转效果 (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
//    public static void loadImageWithSwirlFilterTrans(Context context, Object path, ImageView imageView) {
//        // radius 旋转半径[0.0F，1.0F] （单参构造器 - 默认0.5F）
//        // angle 角度[0.0F,无穷大）（单参构造器 - 默认1.0F）视图表现为旋转圈数
//        // center 旋转中心点 （单参构造器 - 默认new PointF(0.5F,0.5F)）
//        RequestOptions options = new RequestOptions().transforms(new SwirlFilterTransformation(1.0F, 0.4F, new PointF(0.5F, 0.5F)));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
//    }
//
//    //最终会显示 原图的高亮效果 (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
//    public static void loadImageWithBrightnessFilterTrans(Context context, Object path, ImageView imageView) {
//        // brightness 光亮强度[-1F,1F]（单参构造器 - 默认0.0F）小于-1F纯黑色,大于1F纯白色
//        RequestOptions options = new RequestOptions().transforms(new BrightnessFilterTransformation(0.5F));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
//    }
//
//    //最终会显示 原图的水彩画效果 (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
//    public static void loadImageWithKuwaharaFilterTrans(Context context, Object path, ImageView imageView) {
//        // radius 半径 （单参构造器 - 默认25）
//        RequestOptions options = new RequestOptions().transforms(new KuwaharaFilterTransformation(10));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
//    }
//
//    //最终会显示 原图+边界至中间全黑至无黑的装饰图效果 (需要先导入GPUImage库 compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.3.0')
//    public static void loadImageWithVignetteFilterTrans(Context context, Object path, ImageView imageView) {
//        RequestOptions options = new RequestOptions().transforms(new VignetteFilterTransformation(new PointF(0.5F, 0.5F), new float[]{0.0F, 0.0F, 0.0F}, 0.0F, 0.5F));
//        Glide.with(context).load(path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
//    }
    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public static void releaseViewResouce(View view) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getBackground();
        view.setBackgroundResource(0);
        bitmapDrawable.setCallback(null);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }

    public static void releaseTextViewResouce(TextView view) {
        Spanned spanned = (Spanned) view.getText();
//        view.setText(null);
        spanned = null;
    }
}