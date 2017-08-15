/**
 * @fileName SwitchButton
 * @describe 滑动开关
 * @author 李培铭
 * @time 2017-08-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.userinterface.SwitchButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.infinitytron.userinterface.R;

import java.util.Timer;
import java.util.TimerTask;

public class SwitchButton extends View {

	private boolean enable = true;
	private boolean isOn = false;
	private String closeUpFrameColor = "";
	private String closeUpCircleColor = "";
	private String turnOnFrameColor = "";
	private String turnOnCircleColor = "";
	private OnSwitchListener onSwitchListener = null;

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setOn(boolean on) {
		isOn = on;
	}

	public void setCloseUpFrameColor(String closeUpFrameColor) {
		closeUpFrameColor = closeUpFrameColor.contains("#") ? closeUpFrameColor.replace("#", "") : closeUpFrameColor;
		closeUpFrameColor = closeUpFrameColor.length() == 8 ? closeUpFrameColor.substring(2) : closeUpFrameColor;
		this.closeUpFrameColor = closeUpFrameColor;
	}

	public void setCloseUpCircleColor(String closeUpCircleColor) {
		closeUpCircleColor = closeUpCircleColor.contains("#") ? closeUpCircleColor.replace("#", "") : closeUpCircleColor;
		closeUpCircleColor = closeUpCircleColor.length() == 8 ? closeUpCircleColor.substring(2) : closeUpCircleColor;
		this.closeUpCircleColor = closeUpCircleColor;
	}

	public void setTurnOnFrameColor(String turnOnFrameColor) {
		turnOnFrameColor = turnOnFrameColor.contains("#") ? turnOnFrameColor.replace("#", "") : turnOnFrameColor;
		turnOnFrameColor = turnOnFrameColor.length() == 8 ? turnOnFrameColor.substring(2) : turnOnFrameColor;
		this.turnOnFrameColor = turnOnFrameColor;
	}

	public void setTurnOnCircleColor(String turnOnCircleColor) {
		turnOnCircleColor = turnOnCircleColor.contains("#") ? turnOnCircleColor.replace("#", "") : turnOnCircleColor;
		turnOnCircleColor = turnOnCircleColor.length() == 8 ? turnOnCircleColor.substring(2) : turnOnCircleColor;
		this.turnOnCircleColor = turnOnCircleColor;
	}

	public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
		this.onSwitchListener = onSwitchListener;
	}

	public SwitchButton(Context context) {
		super(context);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(attrs);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(attrs);
	}

	private void initView(AttributeSet attrs) {
		if (attrs != null && getContext() != null) {
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
			if (typedArray != null) {
				this.enable = typedArray.getBoolean(R.styleable.SwitchButton_enable, true);
				this.isOn = typedArray.getBoolean(R.styleable.SwitchButton_isOn, false);
				this.closeUpFrameColor = Integer.toHexString(typedArray.getColor(R.styleable.SwitchButton_closeUpFrameColor, Color.parseColor("#a7a7a7"))).substring(2);
				this.closeUpCircleColor = Integer.toHexString(typedArray.getColor(R.styleable.SwitchButton_closeUpCircleColor, Color.parseColor("#b9b9b9"))).substring(2);
				this.turnOnFrameColor = Integer.toHexString(typedArray.getColor(R.styleable.SwitchButton_turnOnFrameColor, Color.parseColor("#05d05d"))).substring(2);
				this.turnOnCircleColor = Integer.toHexString(typedArray.getColor(R.styleable.SwitchButton_turnOnCircleColor, Color.parseColor("#dedede"))).substring(2);
				typedArray.recycle();
			}
		}
	}

	// 记录x轴位移坐标
	private float moveX = 0;
	// 点击标记,用于判断手指是否点击到开关的小圆点
	private boolean canMove = false;
	// 记录拖动开始时候的坐标位置
	private PointF movePoint = new PointF();
	// 记录点击时的坐标位置
	private PointF touchPoint = new PointF();
	// 判断是否已经有小圆点记录
	private boolean hasLog = false;
	// 记录当前小圆点的起止位置
	private PointF startSwitchButtonCircle = new PointF();
	private PointF endSwitchButtonCircle = new PointF();
	// 记录当前小圆点的半径
	private float currentSwitchButtonCircleRadius;
	// 记录按下的时间
	private long touchTime = 0;
	// 动画计时器
	private Timer timer = null;


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
	}

	/**
	 * 重绘图形
	 * @param canvas 画布
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// 获取当前控件高度
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		// 定义半径
		int radius;
		// 定义基准
		int left;
		int right;
		int top;
		int bottom;
		// 设置基准
		if (viewWidth > viewHeight) {
			left = 8;
			right = viewWidth - 8;
			top = 8;
			bottom = viewHeight - 8;
			radius = (viewHeight - 16) / 2;
		} else {
			left = 8;
			right = viewWidth - 8;
			top = (viewHeight - ((viewWidth - 16) / 2)) / 2;
			bottom = viewHeight - (viewHeight - ((viewWidth - 16) / 2)) / 2;
			radius = (viewWidth - 16) / 4;
		}
		// 记录
		if (!hasLog) {
			hasLog = true;
			// 记录小圆点起点坐标
			startSwitchButtonCircle.set(left + radius + 1, top + radius + 0.5f);
			// 记录小圆点终点坐标
			endSwitchButtonCircle.set(right - radius - 1, top + radius + 0.5f);
			// 记录当前小圆点半径
			currentSwitchButtonCircleRadius = radius - 4;
			// 初始化设置
			if (!isOn) {
				moveX = 0;
			} else {
				moveX = endSwitchButtonCircle.x - startSwitchButtonCircle.x;
			}
		}
		// 行程颜色计算
		String increaseColor = Integer.toHexString((int) (255f * (moveX / (endSwitchButtonCircle.x - startSwitchButtonCircle.x))));
		String reduceColor = Integer.toHexString((int) (255f * (1f - moveX / (endSwitchButtonCircle.x - startSwitchButtonCircle.x))));
		increaseColor = increaseColor.length() == 1 ? "0" + increaseColor : increaseColor;
		reduceColor = reduceColor.length() == 1 ? "0" + reduceColor : reduceColor;
		RectF buttonRectF = new RectF(left, top, right, bottom);
		// 绘制开关外边框
		canvas.drawRoundRect(buttonRectF, radius, radius, new ButtonPaint().Paint("#" + reduceColor + closeUpFrameColor, 1, true));
		// 绘制开关内填充
		canvas.drawRoundRect(buttonRectF, radius, radius, new ButtonPaint().Paint("#" + increaseColor + turnOnFrameColor, 1, false));
		// 绘制中间小圆点
		canvas.drawCircle(left + radius + 1 + moveX, top + radius, radius - 4, new ButtonPaint().Paint("#" + increaseColor + turnOnCircleColor, 2, false));
		canvas.drawCircle(left + radius + 1 + moveX, top + radius, radius - 4, new ButtonPaint().Paint("#" + reduceColor + closeUpCircleColor, 2, false));

	}

	/**
	 * 触摸事件捕捉
	 * @param event 触摸事件
	 * @return 返回
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (enable) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 判断是否在开关的小圆点内
					float pointX;
					float pointY;
					PointF tempPointF;
					if (!isOn) {
						tempPointF = startSwitchButtonCircle;
					} else {
						tempPointF = endSwitchButtonCircle;
					}
					if (tempPointF.x < event.getX()) {
						pointX = event.getX() - tempPointF.x;
					} else {
						pointX = tempPointF.x - event.getX();
					}
					if (tempPointF.y < event.getY()) {
						pointY = event.getY() - tempPointF.y;
					} else {
						pointY = tempPointF.y - event.getY();
					}
					// 但点击点在小圆点内
					if (Math.sqrt(pointX * pointX + pointY * pointY) <= currentSwitchButtonCircleRadius + 20f) { // 考虑到手指触摸屏幕的误差,设置误差为10像素,以便在小尺寸的控件下有好的触摸反馈
						// 设置可移动标记
						canMove = true;
						// 记录当前点击位置
						movePoint.set(event.getX(), event.getY());
					}
					// 记录按下时间
					touchTime = System.currentTimeMillis();
					// 记录按下位置
					touchPoint.set(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_UP:
					// 判断触点消失时小圆点是否已经与边界接触
					if (moveX > (endSwitchButtonCircle.x - startSwitchButtonCircle.x) / 2) { // 如果小圆点行程已经过半,则认为已经打开
						// 设置标记并进行回调
						isOn = true;
						// 设置行程
						move(endSwitchButtonCircle.x - startSwitchButtonCircle.x);
					} else {
						// 设置标记并进行回调
						isOn = false;
						// 设置行程
						move(0);
					}
					// 记录触点抬起时间
					long leaveTime = System.currentTimeMillis();
					// 如果在1000ms内,且触点抬起时的位置变化偏差不大于10像素,则进行开关操作
					if (leaveTime - touchTime < 1000 && event.getX() < touchPoint.x + 5 && event.getX() > touchPoint.x - 5 && event.getY() < touchPoint.y + 5 && event.getY() > touchPoint.y - 5) {
						if (!isOn) {
							// 设置标记并进行回调
							isOn = true;
							// 设置行程
							move(endSwitchButtonCircle.x - startSwitchButtonCircle.x);
						} else {
							// 设置标记并进行回调
							isOn = false;
							// 设置行程
							move(0);
						}
					}
					// 设置可移动标记
					canMove = false;
					// 刷新界面
					invalidate();
					break;
				case MotionEvent.ACTION_MOVE:
					// 如果检测到移动,先判断是否可以进行移动
					if (canMove) {
						// 计算x轴的移动距离
						float moveDistance;
						if (!isOn) {
							moveDistance = event.getX() - movePoint.x;
						} else {
							moveDistance = movePoint.x - event.getX();
						}
						if (moveDistance > 0 && moveDistance < endSwitchButtonCircle.x - startSwitchButtonCircle.x) {
							// 设置行程
							if (!isOn) {
								moveX = moveDistance;
							} else {
								moveX = endSwitchButtonCircle.x - startSwitchButtonCircle.x - moveDistance;
							}
							// 刷新界面
							invalidate();
						}
					}
					break;
			}
		}
		return true;
	}

	/**
	 * 唯一动画
	 * @param endX 结束的x轴坐标
	 */
	private void move(final float endX) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (moveX <= endX) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (moveX < endX) {
						if (endX - moveX > getWidth() / 30) {
							moveX += getWidth() / 30;
						} else {
							moveX = endX;
						}
						postInvalidate();
					} else {
						//Log.i("TAG", "isOn" + isOn);
						if (onSwitchListener != null) {
							onSwitchListener.onSwith(true);
						}
						this.cancel();
					}
				}
			}, 0, 10);
		} else {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (moveX > endX) {
						if (moveX - endX > getWidth() / 30) {
							moveX -= getWidth() / 30;
						} else {
							moveX = endX;
						}
						postInvalidate();
					} else {
						//Log.i("TAG", "isOn" + isOn);
						if (onSwitchListener != null) {
							onSwitchListener.onSwith(false);
						}
						this.cancel();
					}
				}
			}, 0, 10);
		}
	}
}
