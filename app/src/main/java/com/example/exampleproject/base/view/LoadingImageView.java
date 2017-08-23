package com.example.exampleproject.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 带有进度条加载的ImageView
 * Created by ran on 2017/4/10.
 */

public class LoadingImageView extends AppCompatImageView {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 画笔
     */
    private Paint paint;

    /**
     * 打判定图形路径及空的路径
     */
    private Path hookPath;
    private Path forkPath;
    private Path forkDotPath;

    /**
     * 圆形图形路径及空的路径
     */
    private Path circlePath;

    private Rect rect;


    /**
     * 上传进度条进度
     */
    private int progress;

    /**
     * 进度条的最大值和最小值
     */
    private int progressMax = 100;
    private int progressMin = 0;

    /**
     * 控件的宽和高
     */
    private int mWidth;
    private int mHeight;

    /**
     * 完成时的圆圈半径
     */
    private int mCircleRadius;

    /**
     * 颜色布、文本颜色、完成圆和勾图形颜色
     */
    private String BACKGROUND_COLOR = "#70000000";
    private String TEXT_COLOR = "#FFFFFF";
    private String FINISHCIRCLE_COLOR = "#4C9BDC";
    private String FAILEDCIRCLE_COLOR = "#FFC41518";
    private String TICK_COLOR = "#FFFFFF";

    /**
     * 进度条状态种类
     */
    private enum Status{
        //不可视，加载中，完成,上传失败
        INVISIBLE,LOADING,FINISH,FAILED
    }

    /**
     * 当前状态
     */
    private Status currentStatus = Status.INVISIBLE;

    /**
     * handler标记，
     */
    private int INVISIBLE_FLAG = 0;

    /**
     *  0-->加载不可视
     *  1-->完成后显示1.5秒判定图形
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    currentStatus = Status.INVISIBLE;
                    postInvalidate();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public LoadingImageView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public LoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        this.mContext = context;
        init();
    }

    public LoadingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }



    /**
     * 初始化
     */
    public void init(){
        //初始化画笔
        paint = new Paint();
        circlePath = new Path();
        hookPath = new Path();
        forkPath = new Path();
        forkDotPath = new Path();
        rect = new Rect();

        //初始进度条进度为最小
        progress = progressMin;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
//        if (mWidth >= mHeight){
//            mWidth = mHeight;
//        }else{
//            mHeight = mWidth;
//        }
        //获得半径
        mCircleRadius = mHeight / 6;
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (currentStatus){
            case INVISIBLE:
                //清空画布,只是让画布重新绘制就行了
                break;
            case LOADING:
                loadingProgress(canvas);
                break;
//            case FINISH:
//                uploadFinish(canvas);
//                break;
//            case FAILED:
//                uploadFailed(canvas);
//                break;
        }
    }

    /**
     * 加载半透明进度条
     * @param canvas
     *              画布
     */
    private void loadingProgress(Canvas canvas){
        // 消除锯齿
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //将图片上方蒙一层半透明的颜色布
        paint.setColor(Color.parseColor(BACKGROUND_COLOR));
        canvas.drawRect(0, 0, getWidth(), getHeight()- getHeight() * progress
                / 100, paint);
        //设置图片为全透明
        paint.setColor(Color.parseColor("#00000000"));
        canvas.drawRect(0, getHeight() - getHeight() * progress / 100,
                getWidth(), getHeight(), paint);
        //进度文字
        paint.setTextSize(50);
        paint.setColor(Color.parseColor(TEXT_COLOR));
        paint.setStrokeWidth(5);
        // 确定文字的宽度
        paint.getTextBounds("100%", 0, "100%".length(), rect);
        canvas.drawText(progress + "%", getWidth() / 2 - rect.width() / 2,
                getHeight() / 2, paint);
    }

    /**
     * 上传成功图片完成图形
     * @param canvas
     *              画布
     */
    private void uploadFinish(Canvas canvas){
        paint.setColor(Color.parseColor(FINISHCIRCLE_COLOR));
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setStrokeWidth(10);
        //画圆
        circlePath.addCircle(getWidth() / 2,getHeight() / 2,mCircleRadius,Path.Direction.CW);
        canvas.drawPath(circlePath,paint);
        //画象征完成的绿色勾
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(TICK_COLOR));
        paint.setStrokeWidth(12);
        hookPath.moveTo(mWidth / 3 + (mCircleRadius * 5) / 12,mHeight / 3 + (mCircleRadius * 4) / 3);
        hookPath.lineTo(mWidth / 3 + (mCircleRadius * 8) / 9,mHeight / 3 + (mCircleRadius * 5) / 3);
        hookPath.lineTo(mWidth / 3 + (mCircleRadius * 7) / 4,mHeight / 3 + (mCircleRadius * 1) / 2);
        canvas.drawPath(hookPath, paint);
    }
    /**
     * 上传失败图片完成图形
     * @param canvas
     *              画布
     */
    private void uploadFailed(Canvas canvas){
        paint.setColor(Color.parseColor(FAILEDCIRCLE_COLOR));
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setStrokeWidth(10);
        //画圆
        circlePath.addCircle(getWidth() / 2,getHeight() / 2,mCircleRadius,Path.Direction.CW);
        canvas.drawPath(circlePath,paint);
        //画象征上传失败的红色感叹号
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(TICK_COLOR));
        paint.setStrokeWidth(12);
        forkPath.moveTo(mWidth / 2,mHeight / 3 + mCircleRadius / 3);
        forkPath.lineTo(mWidth / 2,mHeight / 3 + (mCircleRadius * 4) / 3);
        canvas.drawPath(forkPath, paint);
        forkDotPath.moveTo(mWidth / 2,mHeight / 3 + (mCircleRadius * 14) / 9);
        forkDotPath.lineTo(mWidth / 2,mHeight / 3 + (mCircleRadius * 16) / 9);
        canvas.drawPath(forkDotPath, paint);
    }


    /**
     * 读取进度
     * @param progress
     *              进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        //判断是否已完成
        if (progress == progressMin){
            handler.sendEmptyMessageDelayed(INVISIBLE_FLAG,0);
        }else{
            currentStatus = Status.LOADING;
        }
        postInvalidate();
    }

    /**
     * 上传成功
     */
    public void setUploadSuccess(){
        currentStatus = Status.FINISH;
        postInvalidate();
    }

    /**
     * 上传失败
     */
    public void setUploadFailed(){
        currentStatus = Status.FAILED;
        postInvalidate();
    }

    /**
     * 清除
     */
    public void clean(){
        currentStatus = Status.INVISIBLE;
        postInvalidate();
    }



    /**
     * 背景颜色布
     * @param BackgroundColor
     */
    public void setBackgroundColor(String BackgroundColor) {
        this.BACKGROUND_COLOR = BackgroundColor;
    }

    /**
     * 文本颜色
     * @param TextColor
     */
    public void setTextColor(String TextColor) {
        this.TEXT_COLOR = TextColor;
    }

    /**
     * 上传成功圆图形颜色
     * @param FinishCircleColor
     */
    public void setFinishCircleColor(String FinishCircleColor) {
        this.FINISHCIRCLE_COLOR = FinishCircleColor;
    }

    /**
     * 上传成功和上传失败的勾图形颜色
     * @param tickColor
     */
    public void setTickColor(String tickColor) {
        this.TICK_COLOR = tickColor;
    }

}
