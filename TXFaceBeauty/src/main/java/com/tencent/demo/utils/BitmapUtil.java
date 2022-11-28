package com.tencent.demo.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
    public static void saveBitmap(Bitmap bitmap, String filePath, int compress) {
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        File parent = f.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(CompressFormat.JPEG, compress, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap scaleBitmap(Bitmap source, float scale, boolean needRecycle) {
        Bitmap resizedBmp = null;
        if (source != null && !source.isRecycled()) {
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            try {
                resizedBmp = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
                if (resizedBmp != source && needRecycle) {
                    source.recycle();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                resizedBmp = source;
            }
        }
        return resizedBmp;
    }
}
