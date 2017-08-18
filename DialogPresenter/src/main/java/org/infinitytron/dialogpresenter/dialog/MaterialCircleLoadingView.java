/**
 * @fileName MaterialCircleLoadingView
 * @describe 加载动画视图
 * @author 李培铭
 * @time 2017-08-17
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.dialogpresenter.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.infinitytron.dialogpresenter.R;

public class MaterialCircleLoadingView extends View {

	// 显示模式
	private int mode = normal;
	// 外边框粗细
	private int frameWidth = 8;
	// 加载模式边框粗细
	private int loadingWidth = 8;
	// 加载完成时勾的粗细
	private int finishWidth = 8;
	// 加载错误时感叹号的粗细
	private int errorWidth = 8;
	// 外边框颜色
	private String frameColor = "";
	// 加载颜色
	private String loadingColor = "";
	// 加载完成颜色
	private String finishColor = "";
	// 错误颜色
	private String errorColor = "";
	// 显示模式监听器
	private MaterialCircleLoadingViewListener materialCircleLoadingViewListener = null;

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public void setLoadingWidth(int loadingWidth) {
		this.loadingWidth = loadingWidth;
	}

	public void setFinishWidth(int finishWidth) {
		this.finishWidth = finishWidth;
	}

	public void setErrorWidth(int errorWidth) {
		this.errorWidth = errorWidth;
	}

	public void setFrameColor(String frameColor) {
		frameColor = frameColor.contains("#") ? frameColor.replace("#", "") : frameColor;
		frameColor = frameColor.length() == 8 ? frameColor.substring(2) : frameColor;
		this.frameColor = frameColor;
	}

	public void setLoadingColor(String loadingColor) {
		loadingColor = loadingColor.contains("#") ? loadingColor.replace("#", "") : loadingColor;
		loadingColor = loadingColor.length() == 8 ? loadingColor.substring(2) : loadingColor;
		this.loadingColor = loadingColor;
	}

	public void setFinishColor(String finishColor) {
		finishColor = finishColor.contains("#") ? finishColor.replace("#", "") : finishColor;
		finishColor = finishColor.length() == 8 ? finishColor.substring(2) : finishColor;
		this.finishColor = finishColor;
	}

	public void setErrorColor(String errorColor) {
		errorColor = errorColor.contains("#") ? errorColor.replace("#", "") : errorColor;
		errorColor = errorColor.length() == 8 ? errorColor.substring(2) : errorColor;
		this.errorColor = errorColor;
	}

	public void setMaterialCircleLoadingViewListener(MaterialCircleLoadingViewListener materialCircleLoadingViewListener) {
		this.materialCircleLoadingViewListener = materialCircleLoadingViewListener;
	}

	public MaterialCircleLoadingView(Context context) {
		super(context);
		// 初始化控件
		init();
	}

	public MaterialCircleLoadingView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		// 初始化视图
		initView(attrs);
		// 初始化控件
		init();
	}

	public MaterialCircleLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 初始化视图
		initView(attrs);
		// 初始化控件
		init();
	}

	/**
	 * 初始化视图
	 */
	private void initView(AttributeSet attrs) {
		if (attrs != null && getContext() != null) {
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
			if (typedArray != null) {
				this.mode = typedArray.getInteger(R.styleable.SwitchButton_mode, 1);
				this.frameWidth = typedArray.getInteger(R.styleable.SwitchButton_frameWidth, 8);
				this.loadingWidth = typedArray.getInteger(R.styleable.SwitchButton_loadingWidth, 8);
				this.finishWidth = typedArray.getInteger(R.styleable.SwitchButton_finishWidth, 8);
				this.errorWidth = typedArray.getInteger(R.styleable.SwitchButton_errorWidth, 8);
				this.frameColor = Integer.toHexString(typedArray.getColor(R.styleable.SwitchButton_frameColor, Color.parseColor("#d6d6d6"))).substring(2);
				this.loadingColor = Integer.toHexString(typedArray.getColor(R.styleable.SwitchButton_loadingColor, Color.parseColor("#1999fa"))).substring(2);
				this.finishColor = Integer.toHexString(typedArray.getColor(R.styleable.SwitchButton_finishColor, Color.parseColor("#05e666"))).substring(2);
				this.errorColor = Integer.toHexString(typedArray.getColor(R.styleable.SwitchButton_errorolor, Color.parseColor("#fb4848"))).substring(2);
				typedArray.recycle();
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		circlePaint = new DialogLoadingPaint().Paint("#" + frameColor, frameWidth, true);
		changingCirclePaint = new DialogLoadingPaint().Paint("#" + loadingColor, loadingWidth, true);
		finishPaint = new DialogLoadingPaint().Paint("#" + finishColor, finishWidth, false);
		errorPaint = new DialogLoadingPaint().Paint("#" + errorColor, errorWidth, false);
		rectF = new RectF();
	}

	// 无模式
	private static final int normal = 0;
	// 加载模式
	private static final int loading = 1;
	// 完成模式
	private static final int finish = 2;
	// 错误模式
	private static final int error = 3;
	// 圆弧轨道画笔
	private Paint circlePaint;
	// 变化圆弧画笔
	private Paint changingCirclePaint;
	// 完成画笔
	private Paint finishPaint;
	// 警告画笔
	private Paint errorPaint;
	// 用来放置圆弧
	private RectF rectF = null;
	// 加载时的旋转角度
	private static final int loadingRotateDelta = 4;
	// 加载时的当前角度
	private long loadingCurrentAngle = 0;
	// 加载时的最小角度
	private long loadingMinAngle = 0;
	// 加载时的开始角度
	private long loadingStartAngle = 0;
	// 加载时的结束角度
	private long loadingEndAngle = 120;
	// 完成时的画笔实时位置
	private PointF currentPointF = new PointF(0, 0);
	// 完成时的画笔进度
	private int finishOnDelta = 4;
	private int finishTwDelta = 4;

	/**
	 * 绘制
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取当前控件高度
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		// 获取放置圆弧所用边框
		int left;
		int right;
		int top;
		int bottom;
		if (viewWidth > viewHeight) {
			left = viewWidth / 2 - viewHeight / 2 + 32;
			right = viewWidth / 2 + viewHeight / 2 - 32;
			top = 32;
			bottom = viewHeight - 32;

		} else {
			left = 32;
			right = viewWidth - 32;
			top = viewHeight / 2 - viewWidth / 2 + 32;
			bottom = viewHeight / 2 + viewWidth / 2 - 32;
		}
		rectF.set(left, top, right, bottom);
		// 绘制圆形旋转轨道
		canvas.drawArc(rectF, 0, 360, false, circlePaint);
		// 根据模式进行绘制
		if (mode == loading) { // 如果标记说明继续绘制或者当前为加载模式,则绘制圆形旋转变弧长动画
			// 开始时,如果弧度开始角度和最小角度一致
			if (loadingStartAngle == loadingMinAngle) {
				//弧度的结束角度减8(loadingEndAngle += 6;正转)
				loadingEndAngle -= 8;
			}
			// 如果弧度的结束角度超过规定的最小角度 或 开始角度小于最小角度(loadingEndAngle >= 300 || loadingStartAngle > loadingMinAngle;正转)
			if (loadingEndAngle <= -320 || loadingStartAngle < loadingMinAngle) {
				// 弧度开始角度减8(loadingStartAngle += 6;正转)
				loadingStartAngle -= 8;
				// 如果弧度结束角度小于-20(loadingEndAngle > 20;正转)
				if (loadingEndAngle < -20) {
					// 弧度结束角度加8(loadingEndAngle -= 6;正转)
					loadingEndAngle += 8;
				}
			}
			// 如果弧度开始角度大于最小角度加320(loadingStartAngle >  loadingMinAngle + 300;正转)
			if (loadingStartAngle <  loadingMinAngle - 320) {
				// 最小角度等于开始角度
				loadingMinAngle = loadingStartAngle;
				// 弧度开始角度等于最小角度
				loadingStartAngle = loadingMinAngle;
				// 弧度结束角度为20(loadingEndAngle = -20;正转)
				loadingEndAngle = -20;
				// 设置动画完成回调
				if (materialCircleLoadingViewListener != null) {
					materialCircleLoadingViewListener.onModeDisplayFinish(loading);
				}
			}
			// 旋转canvas(loadingCurrentAngle += loadingRotateDelta;正转)
			canvas.rotate(loadingCurrentAngle -= loadingRotateDelta, viewWidth / 2, viewHeight / 2);
			// 绘制弧形
			canvas.drawArc(rectF, loadingStartAngle, loadingEndAngle, false, changingCirclePaint);
			// 绘制视图
			invalidate();
		} else if (mode == finish) { // 如果当前为加载完成模式,则绘制圆形结束画勾动画
			// 设置步骤标记
			boolean canNext = false;
			// 确定勾的半径
			int tickRadius = (viewWidth / 2 - left) / 2;
			// 取得勾的起点,中点,终点
			int tickStartX = viewWidth / 2 - tickRadius;
			int tickStartY = viewHeight / 2;
			int tickCenterX = viewWidth / 2;
			int tickCenterY = viewHeight / 2 + tickRadius;
			int tickEndX = viewWidth / 2 + tickRadius;
			int tickEndY = viewHeight / 2 - tickRadius;
			// 画勾的第一条线
			if (currentPointF.x < tickCenterX) { // 如果当前所绘的坐标比中点小,则继续绘制
				// 绘制线条
				canvas.drawLine(tickStartX, tickStartY, tickStartX + finishOnDelta, tickStartY + finishOnDelta, finishPaint);
				// 设置绘制后的终点
				currentPointF.set(tickStartX + finishOnDelta, tickStartY + finishOnDelta);
				// 设置所画线的长度
				finishOnDelta += (right - left) / 24;
				// 重绘图像
				invalidate();
			} else {
				// 设置第二步标记为可进行
				canNext = true;
				// 画出第一步的最终线条
				canvas.drawLine(tickStartX, tickStartY, tickCenterX, tickCenterY, finishPaint);
			}
			// 画勾的第二条线
			if (canNext && currentPointF.x < tickEndX) { // 如果当前所绘的坐标比终点小,则继续绘制
				// 绘制线条
				canvas.drawLine(tickCenterX, tickCenterY, tickCenterX + finishTwDelta, tickCenterY - finishTwDelta * 2, finishPaint);
				// 设置绘制后的终点
				currentPointF.set(tickCenterX + finishTwDelta, tickCenterY - finishTwDelta * 2);
				// 设置所画线的长度
				finishTwDelta += (right - left) / 24;
				// 重绘图像
				invalidate();
			} else if (canNext) {
				// 画出第一步的最终线条
				canvas.drawLine(tickCenterX, tickCenterY, tickEndX, tickEndY, finishPaint);
				currentPointF.set(0, 0);
				// 设置动画完成回调
				if (materialCircleLoadingViewListener != null) {
					materialCircleLoadingViewListener.onModeDisplayFinish(finish);
				}
			}
		} else if (mode == error) { // 如果当前为加载失败模式,则绘制感叹号动画
			// 设置步骤标记
			boolean canNext = false;
			// 确定感叹号的半径
			int exclamatoryMarkRadius = (viewWidth / 2 - left) / 2;
			// 取得感叹号的起点,终点,起点,终点
			int exclamatoryMarkFirstStartY = viewHeight / 2 - exclamatoryMarkRadius;
			int exclamatoryMarkFirstEndY = viewHeight/ 2 + exclamatoryMarkRadius / 2 - (right - left) / 16;
			int exclamatoryMarkLastStartY = viewHeight / 2 + exclamatoryMarkRadius / 2 + (right - left) / 16;
			int exclamatoryMarkLastEndY = viewWidth / 2 + exclamatoryMarkRadius;
			// 画竖的第一条线
			if (currentPointF.y < exclamatoryMarkFirstEndY) {
				// 绘制线条
				canvas.drawLine(viewWidth / 2, exclamatoryMarkFirstStartY, viewWidth / 2, exclamatoryMarkFirstStartY + finishTwDelta, errorPaint);
				// 设置绘制后的终点
				currentPointF.set(viewWidth / 2, exclamatoryMarkFirstStartY + finishOnDelta);
				// 设置所画线的长度
				finishOnDelta += (right - left) / 24;
				// 重绘图像
				invalidate();
			} else {
				// 设置第二步标记为可进行
				canNext = true;
				// 画出第一步的最终线条
				canvas.drawLine(viewWidth / 2, exclamatoryMarkFirstStartY, viewWidth / 2, exclamatoryMarkFirstEndY, errorPaint);
			}
			// 画勾的第二条线
			if (canNext && currentPointF.y < exclamatoryMarkLastEndY) { // 如果当前所绘的坐标比终点小,则继续绘制
				// 绘制线条
				canvas.drawLine(viewWidth / 2, exclamatoryMarkLastStartY, viewWidth / 2, exclamatoryMarkLastStartY + finishTwDelta, errorPaint);
				// 设置绘制后的终点
				currentPointF.set(viewWidth / 2, exclamatoryMarkLastStartY + finishTwDelta);
				// 设置所画线的长度
				finishTwDelta += (right - left) / 24;
				// 重绘图像
				invalidate();
			} else if (canNext) {
				// 画出第一步的最终线条
				canvas.drawLine(viewWidth / 2, exclamatoryMarkLastStartY, viewWidth / 2, exclamatoryMarkLastEndY, errorPaint);
				currentPointF.set(0, 0);
				// 设置动画完成回调
				if (materialCircleLoadingViewListener != null) {
					materialCircleLoadingViewListener.onModeDisplayFinish(error);
				}
			}
		}
	}
}
