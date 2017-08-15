package com.example.exampleproject.base.zxing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.exampleproject.R;
import com.example.exampleproject.base.activity.BaseActivity;
import com.example.exampleproject.base.zxing.camera.CameraManager;
import com.example.exampleproject.base.zxing.decode.AmbientLightManager;
import com.example.exampleproject.base.zxing.decode.BeepManager;
import com.example.exampleproject.base.zxing.decode.CaptureActivityHandler;
import com.example.exampleproject.base.zxing.decode.InactivityTimer;
import com.example.exampleproject.base.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * 二维码界面
 * 
 * @author chang
 * 
 */
public abstract class BaseCaptureActivity extends BaseActivity implements
		SurfaceHolder.Callback {

	private static final String TAG = BaseCaptureActivity.class.getSimpleName();
	private CameraManager cameraManager;//摄像头管理器
	private CaptureActivityHandler handler;//handler消息处理类
	private Result savedResultToShow;//扫描结果
	private ViewfinderView viewfinderView;//扫描视图
	private boolean hasSurface;//标识是否显示
	private Collection<BarcodeFormat> decodeFormats = null;
	private Map<DecodeHintType, ?> decodeHints;
	private String characterSet = "ISO-8859-1";//编码
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;//声音管理器
	private AmbientLightManager ambientLightManager;//亮度管理器

	/**
	 * 获取扫描视图
	 * 
	 * @return
	 */
	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	/**
	 * 获取handler消息处理对象
	 * 
	 * @return
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * 获取摄像头管理对象
	 * 
	 * @return
	 */
	public CameraManager getCameraManager() {
		return cameraManager;
	}

	/**
	 * 绘制扫描视图
	 */
	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_base_capture;
	}

	@Override
	protected void initWindow() {
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 拍照过程屏幕一直处于高亮
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);
	}

	@Override
	protected void initWidget() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderView);
		viewfinderView.setCameraManager(cameraManager);

		handler = null;

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
		}

		beepManager.updatePrefs();
		ambientLightManager.start(cameraManager);

		inactivityTimer.onResume();

	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 保存扫描图片
	 * 
	 * @param bitmap 扫描图片
	 * @param result 扫描结果
	 */
	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler,
						R.id.id_zxing_decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * 处理解码
	 * 
	 * @param rawResult 绘制解码后的结果
	 * @param barcode 图片
	 * @param scaleFactor 缩略图缩放值
	 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();

		boolean fromLiveScan = barcode != null;
		if (fromLiveScan) {
			beepManager.playBeepSoundAndVibrate();
			drawResultPoints(barcode, scaleFactor, rawResult);
		}

		String result = rawResult.getText();
		onScanResult(result,barcode);
	}

	/**
	 * 叠加一个二维突出关键特征的一维或点线条码
	 * 
	 * @param barcode 图片
	 * @param scaleFactor 缩略图缩放值
	 * @param rawResult 绘制解码后的结果
	 */
	private void drawResultPoints(Bitmap barcode, float scaleFactor,
			Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.color_c099cc00));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
							.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
				drawLine(canvas, paint, points[2], points[3], scaleFactor);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					if (point != null) {
						canvas.drawPoint(scaleFactor * point.getX(),
								scaleFactor * point.getY(), paint);
					}
				}
			}
		}
	}

	/**
	 * 绘制线条
	 * 
	 * @param canvas 画布
	 * @param paint 画笔
	 * @param a 点
	 * @param b 点
	 * @param scaleFactor 缩略图缩放值
	 */
	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b, float scaleFactor) {
		if (a != null && b != null) {
			canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(),
					scaleFactor * b.getX(), scaleFactor * b.getY(), paint);
		}
	}

	/**
	 * 初始化摄像头
	 * 
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
							"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						decodeHints, characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe.getMessage());
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, e.getMessage());
			displayFrameworkBugMessageAndExit();
		}
	}

	/**
	 * 显示错误信息
	 */
	private void displayFrameworkBugMessageAndExit() {
		Toast.makeText(BaseCaptureActivity.this,"抱歉，Android相机出现问题。您可能需要重启设备。", Toast.LENGTH_SHORT).show();
	}

	protected abstract void onScanResult(String result,Bitmap barcode);
}
