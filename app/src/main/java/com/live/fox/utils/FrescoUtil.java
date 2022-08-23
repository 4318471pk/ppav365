package com.live.fox.utils;

import android.content.Context;
import android.net.Uri;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;


/**
 * User: hqs
 * Date: 2017/1/16
 * Time: 16:32
 */

public class FrescoUtil {

    public static void init(final Context context) {

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setMaxCacheSize(50 * ByteConstants.MB)
                .setBaseDirectoryName("fresco")
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        return context.getCacheDir();
                    }
                })
                .build();

        DiskCacheConfig smallDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryName("fresco_small")
                .setMaxCacheSize(10 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(5 * ByteConstants.MB)
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        return context.getCacheDir();
                    }
                })
                .build();

        // 当内存紧张时采取的措施
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();

                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    // 清除内存缓存
                    Fresco.getImagePipeline().clearMemoryCaches();
                }
            }
        });


//        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(context, OkHttpUtil.getOkHttpClient())
//                .setMainDiskCacheConfig(diskCacheConfig)
//                .setSmallImageDiskCacheConfig(smallDiskCacheConfig)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry) // 报内存警告时的监听
//                .setBitmapsConfig(Bitmap.Config.RGB_565)
//                .setDownsampleEnabled(true)
//                .build();
//        Fresco.initialize(context, config);
    }

    public static void loadUrl(String url, SimpleDraweeView imageView) {
        loadUrl(url, imageView, -1, -1, null);
    }

    public static void loadUrl(String url, SimpleDraweeView imageView, BaseControllerListener<ImageInfo> baseControllerListener) {
        loadUrl(url, imageView, -1, -1, baseControllerListener);
    }

    public static void loadUrl(String url, SimpleDraweeView imageView, int width, int height) {
        loadUrl(url, imageView, width, height, null);
    }

    public static void loadUrl(String url, SimpleDraweeView imageView, int width, int height, BaseControllerListener<ImageInfo> baseControllerListener) {
        ImageRequestBuilder request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url));
        if (width != -1 && height != -1)
            request.setResizeOptions(new ResizeOptions(width, height));

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request.build())
                .setOldController(imageView.getController())
                .setControllerListener(baseControllerListener)
                .build();
        imageView.setController(controller);
    }


    public static void loadBitmap(Context context, String url, BaseBitmapDataSubscriber subscriber) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(subscriber, CallerThreadExecutor.getInstance());
    }
}
