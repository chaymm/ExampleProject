package com.example.exampleproject.app.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Display;
import android.view.View;

public class ImageUtils {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 3;
	public static final int BOTTOM = 4;

	/** */
	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/** */
	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/** */
	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/** */
	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		// bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		bitmapDrawable = new BitmapDrawable(null, toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	/**
	 * 读取路径中的图片，然后将其转化为缩放后的bitmap
	 * 
	 * @param path
	 */
	public static void saveBefore(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
		options.inJustDecodeBounds = false;
		// 计算缩放比
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = 2; // 图片长宽各缩小二分之一
		// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		System.out.println(w + " " + h);
		// savePNG_After(bitmap,path);
		saveJPGE_After(bitmap, path);
	}

	/**
	 * 保存图片为PNG
	 * 
	 * @param bitmap
	 * @param name
	 */
	public static void savePNG_After(Bitmap bitmap, String name) {
		File file = new File(name);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存图片为JPEG
	 * 
	 * @param bitmap
	 * @param path
	 */
	public static void saveJPGE_After(Bitmap bitmap, String path) {
		File file = new File(path);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 水印
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 图片合成
	 * 
	 * @return
	 */
	public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}
		Bitmap newBitmap = bitmaps[0];
		// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
		for (int i = 1; i < bitmaps.length; i++) {
			newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
		}
		return newBitmap;
	}

	private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second,
			int direction) {
		if (first == null) {
			return null;
		}
		if (second == null) {
			return first;
		}
		int fw = first.getWidth();
		int fh = first.getHeight();
		int sw = second.getWidth();
		int sh = second.getHeight();
		Bitmap newBitmap = null;
		if (direction == LEFT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, sw, 0, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == RIGHT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, fw, 0, null);
		} else if (direction == TOP) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, sh, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == BOTTOM) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, 0, fh, null);
		}
		return newBitmap;
	}

	/**
	 * 将Bitmap转换成指定大小
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}

	/**
	 * Drawable 转 Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmapByBD(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		return bitmapDrawable.getBitmap();
	}

	/**
	 * Bitmap 转 Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
		// Drawable drawable = new BitmapDrawable(bitmap);
		Drawable drawable = new BitmapDrawable(null, bitmap);
		return drawable;
	}

	/**
	 * byte[] 转 bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap bytesToBimap(byte[] b) {
		if (b.length == 0) {
			return null;
		} else {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
	}

	/**
	 * bitmap 转 byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 40, baos);
		return baos.toByteArray();
	}

	/**
	 * 将Bitmap转换成字符串
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.PNG, 100, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);
	}

	/**
	 * 将字符串转换成Bitmap类型
	 * 
	 * @param st
	 * @return
	 */
	public static Bitmap convertStringToIcon(String st) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(st, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 使图片背景变灰
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static Bitmap getGrayBitmap(Context context, int id) {
		Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(),
				id);
		Bitmap mGrayBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), Config.ALPHA_8);
		Canvas mCanvas = new Canvas(mGrayBitmap);
		Paint mPaint = new Paint();
		// 创建颜色变换矩阵
		ColorMatrix mColorMatrix = new ColorMatrix();
		// 设置灰度影响范围
		mColorMatrix.setSaturation(0);
		// 创建颜色过滤矩阵
		ColorMatrixColorFilter mColorFilter = new ColorMatrixColorFilter(
				mColorMatrix);
		// 设置画笔的颜色过滤矩阵
		mPaint.setColorFilter(mColorFilter);
		mPaint.setAlpha(150);
		// 使用处理后的画笔绘制图像
		mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
		return mGrayBitmap;
	}

	/**
	 * 缩放本地图片
	 * 
	 * @param f
	 * @return
	 */
	public static Bitmap getFitSizePicture(File f) {
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		if (f.length() < 51200) { // 0-50k
			opts.inSampleSize = 1;
		} else if (f.length() < 307200) { // 50-300k
			opts.inSampleSize = 2;
		} else {
			opts.inSampleSize = 4;
		} 

		try {
			resizeBmp = BitmapFactory.decodeFile(f.getPath(), opts);
		} catch (Exception err) {
			err.printStackTrace();
		}

		return resizeBmp;
	}
	
	public static Bitmap getFitSizePicture(byte[] data) {
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		if (data.length < 150000) { // 0-150k
			opts.inSampleSize = 1;
		} else if (data.length < 350000) { // 150-350k
			opts.inSampleSize = 2;
		} else {
			opts.inSampleSize = 3;
		}

		try {
			resizeBmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		} catch (Exception err) {
			err.printStackTrace();
		}

		return resizeBmp;
	}

	/**
	 * 读取文件到byte[]
	 * 
	 * @param filename
	 *            文件名
	 * @param byteOffset
	 *            字节数组开始位置
	 * @param byteCount
	 *            读取字节数组个数
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filename, int byteOffset,
			int byteCount) throws IOException {

		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException(filename);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer, byteOffset, byteCount)) != -1) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * 截取指定字节数组
	 * 
	 * @param src
	 *            源字节数组
	 * @param begin
	 *            起始下标
	 * @param count
	 *            截取字节个数
	 * @return 新的字节数组
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}

	/**
	 * 计算jpg格式图片的大小
	 * 
	 * @param fileName
	 *            文件名
	 */
	public static String calJPGPictureSize(String fileName) {
		String size = "0x0";
		BytesTransUtil bytesTransUtil = BytesTransUtil.getInstance();
		try {
			byte[] d = toByteArray(fileName, 0, 209);
			short word = bytesTransUtil.getShort(subBytes(d, 0x15, 0x1),
					bytesTransUtil.testCPU());
			if (word == 0xdb) {
				word = bytesTransUtil.getShort(subBytes(d, 0x5a, 0x1),
						bytesTransUtil.testCPU());
				if (word == 0xdb) {
					d = subBytes(d, 0xa3, 0x4);
					return jpgImageSizeWithExactData(d);
				} else {
					d = subBytes(d, 0x5e, 0x4);
					return jpgImageSizeWithExactData(d);
				}
			} else {
				return size;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 获取图片宽度和高度
	 * 
	 * @param data
	 *            字节数据
	 * @return
	 */
	public static String jpgImageSizeWithExactData(byte[] data) {
		short w1 = 0, w2 = 0;
		BytesTransUtil bytesTransUtil = BytesTransUtil.getInstance();
		w1 = bytesTransUtil.getShort(subBytes(data, 2, 1),
				bytesTransUtil.testCPU());
		w2 = bytesTransUtil.getShort(subBytes(data, 3, 1),
				bytesTransUtil.testCPU());
		short w = (short) ((w1 << 8) + w2);
		// System.out.println("w:" + w);

		short h1 = 0, h2 = 0;
		h1 = bytesTransUtil.getShort(subBytes(data, 0, 1),
				bytesTransUtil.testCPU());
		h2 = bytesTransUtil.getShort(subBytes(data, 1, 1),
				bytesTransUtil.testCPU());
		short h = (short) ((h1 << 8) + h2);
		// System.out.println("h:" + h);

		return new String(w + "x" + h);
	}

	/**
	 * 截屏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap screenShot(Activity activity) {
		// 获取windows中最顶层的view
		View view = activity.getWindow().getDecorView();
		view.buildDrawingCache();
		// 获取状态栏高度
		Rect rect = new Rect();
		view.getWindowVisibleDisplayFrame(rect);
		int statusBarHeights = rect.top;
		Display display = activity.getWindowManager().getDefaultDisplay();
		// 获取屏幕宽和高
		int widths = display.getWidth();
		int heights = display.getHeight();
		// 允许当前窗口保存缓存信息
		view.setDrawingCacheEnabled(true);
		// 去掉状态栏
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
				statusBarHeights, widths, heights - statusBarHeights);
		// 销毁缓存信息
		view.destroyDrawingCache();
		return bmp;
	}

}
