package com.live.fox.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.QRCodeUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.device.DeviceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 分享
 */
public class MyShareDialog extends Dialog implements View.OnClickListener {

    Context context;
    private ImageView qrImageView; // 二维码
    private TextView saveImgView; // 保存图片
    private TextView copyUrlView; // 复制链接
    private Bitmap currentQrBp; // 当前的图片
    private ImageButton closeButton; // 关闭按钮

    public MyShareDialog(Context context) {
        super(context, R.style.SundayDialogs);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        setCanceledOnTouchOutside(true);
        setSubviews();

        saveImgView.setOnClickListener(v -> { // 保存图片
            saveQRCodeImage();
        });

        copyUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 复制链接
                copyToClipboard();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 关闭
                dismiss();
            }
        });

        handlerQRCodeImage();
    }

    @Override
    public void onClick(View arg0) {
        if (isShowing()) {
            dismiss();
        }
    }

    private void setSubviews() {
        qrImageView = findViewById(R.id.my_share_qr_id);
        saveImgView = findViewById(R.id.tv_save);
        copyUrlView = findViewById(R.id.tv_copy);
        closeButton = findViewById(R.id.my_share_close_id);
    }

    public void reloadQRCode(Bitmap bp) {
        if (bp == null) return;
        currentQrBp = bp;

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //已在主线程中，可以更新UI
                qrImageView.setImageBitmap(bp);
            }
        });
    }

    /**
     * 复制到剪切板
     */
    private void copyToClipboard() {
        String mShareUrl = SPManager.getShareUrl() + "?puid=" + DataCenter.getInstance().getUserInfo().getUser().getUid();
        String copyString = context.getString(R.string.copyNetwork) + mShareUrl;
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("", copyString);
        clip.setPrimaryClip(myClip);
        ToastUtils.showShort(context.getString(R.string.copySuccess));
    }

    /**
     * 保存图片
     */
    private void saveQRCodeImage() {
        if (currentQrBp == null) return;

        Bitmap backbm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Bitmap totalBp = addBackground(currentQrBp, backbm);
        saveBmp2Gallery(context, totalBp, "live"); // 保存
    }

    /**
     * @param bmp     獲取的bitmap數據
     * @param picName 自定義的圖片名
     */
    public static void saveBmp2Gallery(Context context, Bitmap bmp, String picName) {
        saveImageToGallery(bmp, picName);
        String fileName = null;
        //系統相冊目錄
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;


        // 聲明文件對象
        File file = null;
        // 聲明輸出流
        FileOutputStream outStream = null;
        try {
            // 如果有目標文件，直接獲得文件對象，否則創建一個以filename爲名稱的文件
            file = new File(galleryPath, picName + ".jpg");
            // 獲得文件相對路徑
            fileName = file.toString();
            // 獲得輸出流，如果文件中有內容，追加內容
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

        ToastUtils.showShort(context.getString(R.string.sharepic));
    }


    /**
     * 保存圖片到圖庫
     *
     * @param bmp
     */
    public static void saveImageToGallery(Bitmap bmp, String bitName) {
        // 首先保存圖片
        File appDir = new File(Environment.getExternalStorageDirectory(),
                "yingtan");
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


    // 生成二维码
    private void handlerQRCodeImage() {
        String mShareUrl = SPManager.getShareUrl() + "?puid=" + DataCenter.getInstance().getUserInfo().getUser().getUid();
        showThreadImage(context, mShareUrl, R.mipmap.ic_launcher);
    }

    public void showThreadImage(final Context mContext, final String text, final int centerPhoto) {
        final String filePath = getFileRoot(mContext) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";

        // 二維碼圖片較大時，生成圖片、保存文件的時間可能較長，因此放在新線程中
        new Thread(() -> {
            boolean success = QRCodeUtil.createQRImage(text, DeviceUtils.dp2px(context, 134),
                    DeviceUtils.dp2px(context, 138), BitmapFactory.decodeResource(mContext.getResources(), centerPhoto),
                    filePath);
            if (success) {
                Bitmap codebm = BitmapFactory.decodeFile(filePath);
                reloadQRCode(codebm);
            } else {
                if (getOwnerActivity() != null)
                    getOwnerActivity().runOnUiThread(() -> ToastUtils.showShort(context.getString(R.string.QRFailed)));
            }
        }).start();
    }

    /**
     * 文件存儲根目錄
     */
    private static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }


    /**
     * 給二維碼圖片加背景
     */
    public Bitmap addBackground(Bitmap foreground, Bitmap background) {
        //前面參數是二維碼圖，後面是背景大圖
        int bgWidth = background.getWidth(); // 背景
        int bgHeight = background.getHeight();

//        int fgWidth = foreground.getWidth(); // 二维码
//        int fgHeight = foreground.getHeight();
        int fgWidth = DeviceUtils.dp2px(context, 134); // 二维码
        int fgHeight = DeviceUtils.dp2px(context, 138);

        LogUtils.e(bgWidth + "," + bgHeight);
        LogUtils.e(DeviceUtils.dp2px(context, 60) + "," + DeviceUtils.dp2px(context, 40));
        Bitmap newmap = Bitmap
                .createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        //背景
        canvas.drawBitmap(background, 0, 0, null);
        //二维码
//        canvas.drawBitmap(foreground, (bgWidth - fgWidth)/2,
//                (bgHeight - fgHeight)/2, null);
        canvas.drawBitmap(foreground, DeviceUtils.dp2px(context, 87),
                DeviceUtils.dp2px(context, 227), null);
        canvas.save();//Canvas.ALL_SAVE_FLAG
        canvas.restore();

        return newmap;
    }
}