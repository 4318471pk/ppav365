package com.live.fox.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.live.fox.utils.utilconstants.ImageHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 图片工具类，提供一些应用在图片上的工具方法。
 *
 * @author guolin
 * @since 17/4/20
 */
public class ImageUtil {

    private static String TAG = "ImageUtil";

    /**
     * 获取传入图片的类型。
     *
     * @param imagePath 图片的路径
     * @return 以枚举格式返回图片的类型，如果传入的图片格式有问题，或者图片不存在，一律返回null。
     */
    public static ImageHeaderParser.ImageType getImageType(String imagePath) {
        File file = null;
        FileInputStream fis = null;
        try {
            file = new File(imagePath);
            fis = new FileInputStream(file);
            return new ImageHeaderParser(fis).getType();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            IOUtils.closeIO(fis);
        }
        return null;
    }


    /**
     * 判断传入的图片是否是一张GIF图片。
     *
     * @param imagePath 图片的路径。
     * @return 如果是GIF图返回true，否则返回false。
     */
    public static boolean isGif(String imagePath) {
        ImageHeaderParser.ImageType imageType = getImageType(imagePath);
        return imageType != null && imageType == ImageHeaderParser.ImageType.GIF;
    }

    /**
     * 判断GIF图片的格式是否完全正确。
     *
     * @param imagePath 图片的路径。
     * @return GIF图片的格式完全正确返回true，否则返回false。
     */
    public static boolean isGifValid(String imagePath) {
        try {
            GifHeader gifHeader = (new GifHeaderParser()).setData(getGifBytes(imagePath)).parseHeader();
            return gifHeader.getStatus() == GifDecoder.STATUS_OK;
        } catch (Exception var4) {
            return false;
        }
    }


    /**
     * 获取传入图片的大小。
     *
     * @param imagePath 图片的路径
     * @return 返回图片的大小，以字节为单位，如果图片不存在则返回0。
     */
    public static long getImageSize(String imagePath) {
        File file = new File(imagePath);
        return file.exists() ? file.length() : 0L;
    }


    /**
     * 将指定图片路径插入到系统的相册当中。
     *
     * @return 插入到相册之后图片对应的Uri。
     */
    public final Uri insertImageToSystem(Context context, String imagePath) {
        Uri uri = null;
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, imagePath);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/gif");
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA+"=?", new String[]{imagePath}, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                uri = Uri.parse("content://media/external/images/media/" + id);
                Log.e(TAG, "image id is " + id);
            } else {
                uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return uri;
    }


    private static byte[] getGifBytes(String imagePath) {
        FileInputStream fis = null;
        int bufferSize = 16384;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bufferSize);
        try {
            File file = new File(imagePath);
            fis = new FileInputStream(file);
            byte[] data = new byte[bufferSize];
            int bytes = fis.read(data);
            while (bytes >= 0) {
                buffer.write(data, 0, bytes);
                bytes = fis.read(data);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            IOUtils.closeIO(fis);
        }

        return new byte[buffer.size()];
    }



}
