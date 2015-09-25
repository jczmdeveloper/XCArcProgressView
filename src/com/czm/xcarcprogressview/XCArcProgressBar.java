package com.czm.xcarcprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;


/**
 * 开口圆环类型的进度条：带进度百分比显示的进度条，线程安全的View，可直接在线程中更新进度
 * @author caizhiming
 *
 */
@SuppressWarnings("deprecation")
public class XCArcProgressBar extends View{

	
	
	private Paint paint;//画笔对象的引用
	private int textColor;//中间进度百分比字符串的颜色
	private float textSize ;//中间进度百分比字符串的字体
	private int max;//最大进度
	private int progress;//当前进度
	private boolean isDisplayText;//是否显示中间百分比进度字符串
	private String title;//标题
	private Bitmap bmpTemp = null;
	private int degrees;
	
	public XCArcProgressBar(Context context){
		this(context, null);
	}
	public XCArcProgressBar(Context context,AttributeSet attrs){
		this(context,attrs,0);
	}
	public XCArcProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		
		
		degrees =  0;
		paint  =  new Paint();
		//从attrs.xml中获取自定义属性和默认值
		TypedArray typedArray  = context.obtainStyledAttributes(attrs, R.styleable.XCRoundProgressBar);
		textColor  =typedArray.getColor(R.styleable.XCRoundProgressBar_textColor, Color.RED);
		textSize = typedArray.getDimension(R.styleable.XCRoundProgressBar_textSize, 15);
		max = typedArray.getInteger(R.styleable.XCRoundProgressBar_max, 100);
		isDisplayText  =typedArray.getBoolean(R.styleable.XCRoundProgressBar_textIsDisplayable, true);
		typedArray.recycle();
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		int centerX = getWidth() / 2;// 获取中心点X坐标
		int centerY = getHeight() / 2;// 获取中心点Y坐标

		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas can = new Canvas(bitmap);
		// 绘制底部背景图
		bmpTemp = Utils.decodeCustomRes(getContext(), R.drawable.arc_bg);
		float dstWidth = (float) width;
		float dstHeight = (float) height;
		int srcWidth = bmpTemp.getWidth();
		int srcHeight = bmpTemp.getHeight();

		can.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));// 抗锯齿

		Bitmap bmpBg = Bitmap.createScaledBitmap(bmpTemp, width, height, true);
		can.drawBitmap(bmpBg, 0, 0, null);

		// 绘制进度前景图
		Matrix matrixProgress = new Matrix();
		matrixProgress.postScale(dstWidth / srcWidth, dstHeight / srcWidth);
		bmpTemp = Utils.decodeCustomRes(getContext(), R.drawable.arc_progress);

		Bitmap bmpProgress = Bitmap.createBitmap(bmpTemp, 0, 0, srcWidth,
				srcHeight, matrixProgress, true);
		degrees = progress * 270 / max - 270;
		//遮罩处理前景图和背景图
		can.save();
		can.rotate(degrees, centerX, centerY);
		paint.setAntiAlias(true);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
		can.drawBitmap(bmpProgress, 0, 0, paint);
		can.restore();
		
		if ((-degrees) >= 85) {
			int posX = 0;
			int posY = 0;
			if ((-degrees) >= 270) {
				posX = 0;
				posY = 0;
			} else if ((-degrees) >= 225) {
				posX = centerX / 2;
				posY = 0;
			} else if ((-degrees) >= 180) {
				posX = centerX;
				posY = 0;
			} else if ((-degrees) >= 135) {
				posX = centerX;
				posY = 0;
			} else if ((-degrees) >= 85) {
				posX = centerX;
				posY = centerY;
			}
			
			if ((-degrees) >= 225) {

				can.save();
				Bitmap dst = bitmap
						.createBitmap(bitmap, 0, 0, centerX, centerX);
				paint.setAntiAlias(true);
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
				Bitmap src = bmpBg.createBitmap(bmpBg, 0, 0, centerX, centerX);
				can.drawBitmap(src, 0, 0, paint);
				can.restore();

				can.save();
				dst = bitmap.createBitmap(bitmap, centerX, 0, centerX, height);
				paint.setAntiAlias(true);
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
				src = bmpBg.createBitmap(bmpBg, centerX, 0, centerX, height);
				can.drawBitmap(src, centerX, 0, paint);
				can.restore();

			} else {
				can.save();
				Bitmap dst = bitmap.createBitmap(bitmap, posX, posY, width
						- posX, height - posY);
				paint.setAntiAlias(true);
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
				Bitmap src = bmpBg.createBitmap(bmpBg, posX, posY,
						width - posX, height - posY);
				can.drawBitmap(src, posX, posY, paint);
				can.restore();
			}
		}
		//绘制遮罩层位图
		canvas.drawBitmap(bitmap, 0, 0, null);
		
		// 画中间进度百分比字符串
		paint.reset();
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		int percent = (int) (((float) progress / (float) max) * 100);// 计算百分比
		float textWidth = paint.measureText(percent + "%");// 测量字体宽度，需要居中显示

		if (isDisplayText && percent != 0) {
			canvas.drawText(percent + "%", centerX - textWidth / 2, centerX
					+ textSize / 2 - 25, paint);
		}
		//画底部开口处标题文字
		paint.setTextSize(textSize/2);
		textWidth = paint.measureText(title);
		canvas.drawText(title, centerX-textWidth/2, height-textSize/2, paint);
	}
	
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public int getTextColor() {
		return textColor;
	}
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	public float getTextSize() {
		return textSize;
	}
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	public synchronized int getMax() {
		return max;
	}
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max must more than 0");
		}
		this.max = max;
	}
	public synchronized int getProgress() {
		return progress;
	}
	/**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @author caizhiming
     */ 
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress must more than 0");
		}
		if(progress > max){
			this.progress = progress;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}
	}
	public boolean isDisplayText() {
		return isDisplayText;
	}
	public void setDisplayText(boolean isDisplayText) {
		this.isDisplayText = isDisplayText;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}

	
}
