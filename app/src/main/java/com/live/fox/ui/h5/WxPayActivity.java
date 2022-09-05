package com.live.fox.ui.h5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.DensityUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class WxPayActivity extends BaseHeadActivity {


    String qrcodeUrl;
    long money;
    boolean isAliPay;

    Bitmap mBitmap;

    /**
     * 啓動設置界面的統一接口
     *
     * @param context
     */
    public static void start(Context context, boolean isAliPay, String qrcodeUrl, long money) {
        Constant.isAppInsideClick=true;
        Intent intent = new Intent(context, WxPayActivity.class);
        intent.putExtra("isAliPay", isAliPay);
        intent.putExtra("qrcodeUrl", qrcodeUrl);
        intent.putExtra("money", money);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initData(getIntent());
        setView();
    }

    public void initData(Intent intent) {
        if (intent != null) {
            isAliPay = intent.getBooleanExtra("isAliPay", true);
            qrcodeUrl = intent.getStringExtra("qrcodeUrl");
            money = intent.getLongExtra("money", 0);
        }
    }


    public void setView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        String title = isAliPay ? getString(R.string.alipay) : getString(R.string.wechartPay);
        setHead(title, true, true);
        setBackgroundColor(isAliPay ? R.color.colorBlue : R.color.weixin);

        findViewById(R.id.ll_root).setBackgroundResource(isAliPay ? R.color.colorBlue : R.color.weixin);

        String tip = isAliPay ? getString(R.string.alipic) : getString(R.string.wechartpic);
        ((TextView) findViewById(R.id.tv_tip)).setText(tip);

        ((TextView) findViewById(R.id.tv_money)).setText(money + "");
        //二维码的大小
        int qrcodeSize = DensityUtils.dp2px(WxPayActivity.this, 200);

        //展示二维码
        ImageView iv = findViewById(R.id.iv_wx_qrcode);
        LogUtils.e(qrcodeUrl);
        showLoadingDialog();
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(qrcodeUrl, DeviceUtils.dp2px(WxPayActivity.this, 200));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                hideLoadingDialog();
                if (bitmap != null) {
                    mBitmap = bitmap;
                    iv.setImageBitmap(bitmap);
                } else {
                    ToastUtils.showShort(getString(R.string.ewmWrong));
                    finish();
                }
            }
        }.execute();


        //保存二维码至相册
        findViewById(R.id.tv_savetoxc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBmp2Gallery(WxPayActivity.this, mBitmap, "pay");
            }
        });

    }


    /**
     * @param bmp     獲取的bitmap數據
     * @param picName 自定義的圖片名
     */
    public void saveBmp2Gallery(Context context, Bitmap bmp, String picName) {
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
        ToastUtils.showShort(getString(R.string.ewmSave));


    }


    //生成二维码 size单位px
//    public void createQRcodeImage(String content, int size) {
//        try {
//            //判断URL合法性
//            if (content == null || "".equals(content) || content.length() < 1) {
//                ToastUtils.showShort("二维码格式不正确");
//                return null;
//            }
//            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            //图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, w, h, hints);
//            int[] pixels = new int[w * h];
//            //下面这里按照二维码的算法，逐个生成二维码的图片，
//            //两个for循环是图片横列扫描的结果
//            for (int y = 0; y < h; y++) {
//                for (int x = 0; x < w; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * w + x] = 0xff000000;
//                    } else {
//                        pixels[y * w + x] = 0xffffffff;
//                    }
//                }
//            }
//            //生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
//            //显示到我们的ImageView上面
//            return bitmap;
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
