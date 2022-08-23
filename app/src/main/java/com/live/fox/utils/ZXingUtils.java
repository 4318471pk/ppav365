package com.live.fox.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;



/** 
* 生成条形码和二维码的工具
*/
public class ZXingUtils {

//	/**
//	 * 图片与二维码合并成新的图片
//	 */
//	public static Bitmap createQRImageMerge(final Context context, Bitmap bitmapBg) {
//
//		int userId = LoginSession.getLoginSession().getIdx();
//		long timestamp = System.currentTimeMillis();
//		String token = MD5.encrypt(("/h5/active/invitenew" + timestamp + userId + BuildConfig.VERSION_NAME + "1811b681-2322-lda1-b3d6k876")).toLowerCase();
//		String url = Constants.SERVER + Constants.RED_PACKECT + "?useridx=" + userId + "&token=" + token + "&timestamp=" + timestamp + "&appversion=" + BuildConfig.VERSION_NAME;
//		Bitmap bitmap1;
//		if (bitmapBg == null) {
//			bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.red_bg);
//		} else {
//			bitmap1 = bitmapBg;
//		}
//		int w = (int) (bitmap1.getWidth() * 0.32f);
//		Bitmap bitmap = ZXingUtils.createQRImage(url, w, w);
//		Bitmap bitmap2 = bitmap;
//		Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Config.RGB_565);
//		Canvas canvas = new Canvas(bitmap3);
//		canvas.drawBitmap(bitmap1, new Matrix(), null);
//		float wDifference = 0.3347f;
//		float hDifference = 0.7864f;
//		canvas.drawBitmap(bitmap2, (int) (bitmap1.getWidth() * wDifference), (int) (bitmap1.getHeight() * hDifference), null);
//		return bitmap3;
//	}
//
//	/**
//	 * 提现成功后分享（二维码合成图）
//	 */
//	public static Bitmap createQRImageMerge(final Context context, Bitmap bitmapTop, float money) {
//		/***************生成二维码********************/
//		int userId = LoginSession.getLoginSession().getIdx();
//		long timestamp = System.currentTimeMillis();
//		String token = MD5.encrypt(("/h5/active/invitenew" + timestamp + userId + BuildConfig.VERSION_NAME + "1811b681-2322-lda1-b3d6k876")).toLowerCase();
//		String url = Constants.SERVER + Constants.RED_PACKECT + "?useridx=" + userId + "&token=" + token + "&timestamp=" + timestamp + "&appversion=" + BuildConfig.VERSION_NAME;
//		Bitmap bitmap1;
//		//二维码背景图
//		bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shar_h5_bg);
//		int w = (int) (bitmap1.getWidth() * 0.32f);
//		Bitmap bitmap = ZXingUtils.createQRImage(url, w, w);
//		Bitmap bitmap2 = bitmap;
//		Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Config.RGB_565);
//		Canvas canvas = new Canvas(bitmap3);
//		canvas.drawBitmap(bitmap1, new Matrix(), null);
//		float wDifference = 0.336f;
//		float hDifference = 0.7677f;
//		canvas.drawBitmap(bitmap2, (int) (bitmap1.getWidth() * wDifference), (int) (bitmap1.getHeight() * hDifference), null);
//
//		//用户图像
//		if (bitmapTop == null) {
//			bitmapTop = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_profile);
//		}
//		float wPhoto = 0.3907f;
//		float hPhoto = 0.3471f;
//		canvas.drawBitmap(getPhoto(bitmapTop, 168, 168), (int) (bitmap1.getWidth() * wPhoto), (int) (bitmap1.getHeight() * hPhoto), null);
//
//		//绘画文字
//		String name = LoginSession.getLoginSession().getLoginUser().getNickname();
//		String nameMessage1 = context.getResources().getString(R.string.congratulation);
//		String nameMessage2 = context.getResources().getString(R.string.withdraw_name2);
//		String nameMessage = nameMessage1 + name + nameMessage2;
//		Paint paint1 = new Paint();
//		paint1.setTextSize(32);
//		paint1.setColor(Color.parseColor("#6c280b"));
//		Paint paint2 = new Paint();
//		paint2.setTextSize(36);
//		paint2.setColor(Color.parseColor("#ff0000"));
//		float nameMessageWdith = paint1.measureText(nameMessage);
//
//		float hNameMessage = 0.5247f;
//		canvas.drawText(nameMessage1 + " ", (bitmap1.getWidth() / 2 - nameMessageWdith / 2) / bitmap1.getWidth() * bitmap1.getWidth(), hNameMessage * bitmap1.getHeight(), paint1);
//		canvas.drawText(name, bitmap1.getWidth() / 2 - nameMessageWdith / 2 + paint1.measureText(nameMessage1), hNameMessage * bitmap1.getHeight(), paint2);
//		canvas.drawText(" " + nameMessage2, bitmap1.getWidth() / 2 - nameMessageWdith / 2 + paint1.measureText(nameMessage1) + paint1.measureText(name), hNameMessage * bitmap1.getHeight(), paint1);
//
//		String moneyMessage1 = context.getResources().getString(R.string.withdraw_money1);
//		String moneyMessage2 = context.getResources().getString(R.string.withdraw_money2);
//		String moneyMessage = moneyMessage1 + money + moneyMessage2;
//		float hMoneyMessage = 0.5922f;
//		float moneyMessageWdith = paint1.measureText(moneyMessage);
//		canvas.drawText(moneyMessage1 + " ", bitmap1.getWidth() / 2 - moneyMessageWdith / 2 - 10, hMoneyMessage * bitmap1.getHeight(), paint1);
//		canvas.drawText(String.valueOf(money), bitmap1.getWidth() / 2 - moneyMessageWdith / 2 + paint1.measureText(moneyMessage1) - 10, hMoneyMessage * bitmap1.getHeight(), paint2);
//		canvas.drawText(" " + moneyMessage2, bitmap1.getWidth() / 2 - moneyMessageWdith / 2 + paint1.measureText(moneyMessage1) + paint1.measureText(String.valueOf(money)) - 10, hMoneyMessage * bitmap1.getHeight(), paint1);
//
//		return bitmap3;
//	}

	/**
	 * 绘制圆角图片
	 */
	public static Bitmap getPhoto(Bitmap bitmap, int w, int h) {
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		//绘制图像
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		final Rect rect = new Rect(0, 0, w, h);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(w / 2, h / 2, h / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

//	/**
//	 * 生成二维码 要转换的地址或字符串,可以是中文
//	 */
//	public static Bitmap createQRImage(String url, final int width, final int height) {
//		try {
//			// 判断URL合法性
//			if (url == null || "".equals(url) || url.length() < 1) {
//				return null;
//			}
//			Hashtable<EncodeHintType, String> hints = new Hashtable<>();
//			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//			// 图像数据转换，使用了矩阵转换
//			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
//					BarcodeFormat.QR_CODE, width, height, hints);
//			int[] pixels = new int[width * height];
//			// 下面这里按照二维码的算法，逐个生成二维码的图片，
//			// 两个for循环是图片横列扫描的结果
//			for (int y = 0; y < height; y++) {
//				for (int x = 0; x < width; x++) {
//					if (bitMatrix.get(x, y)) {
//						pixels[y * width + x] = 0xff000000;
//					} else {
//						//pixels[y * width + x] = 0xffffffff;
//					}
//				}
//			}
//			// 生成二维码图片的格式，使用ARGB_8888
//			Bitmap bitmap = Bitmap.createBitmap(width, height,
//					Config.ARGB_8888);
//			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//			return bitmap;
//		} catch (WriterException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 生成条形码
//	 *
//	 * @param context
//	 * @param contents
//	 *            需要生成的内容
//	 * @param desiredWidth
//	 *            生成条形码的宽带
//	 * @param desiredHeight
//	 *            生成条形码的高度
//	 * @param displayCode
//	 *            是否在条形码下方显示内容
//	 * @return
//	 */
//	public static Bitmap creatBarcode(Context context, String contents,
//									  int desiredWidth, int desiredHeight, boolean displayCode) {
//		Bitmap ruseltBitmap = null;
//		/**
//		 * 图片两端所保留的空白的宽度
//		 */
//		int marginW = 20;
//		/**
//		 * 条形码的编码类型
//		 */
//		BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
//
//		if (displayCode) {
//			Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
//					desiredWidth, desiredHeight);
//			Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth + 2
//					* marginW, desiredHeight, context);
//			ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
//					0, desiredHeight));
//		} else {
//			ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
//					desiredWidth, desiredHeight);
//		}
//
//		return ruseltBitmap;
//	}
//
//	/**
//	 * 生成条形码的Bitmap
//	 *
//	 * @param contents
//	 *            需要生成的内容
//	 * @param format
//	 *            编码格式
//	 * @param desiredWidth
//	 * @param desiredHeight
//	 * @return
//	 * @throws WriterException
//	 */
//	protected static Bitmap encodeAsBitmap(String contents,
//										   BarcodeFormat format, int desiredWidth, int desiredHeight) {
//		final int WHITE = 0xFFFFFFFF;
//		final int BLACK = 0xFF000000;
//
//		MultiFormatWriter writer = new MultiFormatWriter();
//		BitMatrix result = null;
//		try {
//			result = writer.encode(contents, format, desiredWidth,
//					desiredHeight, null);
//		} catch (WriterException e) {
//			e.printStackTrace();
//		}
//
//		int width = result.getWidth();
//		int height = result.getHeight();
//		int[] pixels = new int[width * height];
//		// All are 0, or black, by default
//		for (int y = 0; y < height; y++) {
//			int offset = y * width;
//			for (int x = 0; x < width; x++) {
//				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//			}
//		}
//
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Config.ARGB_8888);
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}

	/**
	 * 生成显示编码的Bitmap
	 */
	protected static Bitmap creatCodeBitmap(String contents, int width,
											int height, Context context) {
		TextView tv = new TextView(context);
		LayoutParams layoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(layoutParams);
		tv.setText(contents);
		tv.setHeight(height);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setWidth(width);
		tv.setDrawingCacheEnabled(true);
		tv.setTextColor(Color.BLACK);
		tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

		tv.buildDrawingCache();
		Bitmap bitmapCode = tv.getDrawingCache();
		return bitmapCode;
	}


}
