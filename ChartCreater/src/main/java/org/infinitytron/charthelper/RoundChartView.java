/**
 * @fileName RoundChartView
 * @describe 圆形图表视图类
 * @author 李培铭
 * @time 2017-08-01
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.charthelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundChartView extends View {

	public List<Map<Integer, String>> dataWithStyleMapList = new ArrayList<>(); // 数据内容集合
	public Map<Integer, String> roundChartStyleMap = new HashMap<>(); // 圆形图表样式集合

	public void setDataWithStyleMapList(List<Map<Integer, String>> dataWithStyleMapList) {
		// 判断图标数据和样式完整性
		for (int i = 0; i < dataWithStyleMapList.size(); i++) {
			if (!dataWithStyleMapList.get(i).containsKey(ChartEntity.data)) {
				dataWithStyleMapList.get(i).put(ChartEntity.data, String.valueOf(0));
			}
			if (!dataWithStyleMapList.get(i).containsKey(ChartEntity.dataShowColor)) {
				dataWithStyleMapList.get(i).put(ChartEntity.dataShowColor, "#dedede");
			}
			if (!dataWithStyleMapList.get(i).containsKey(ChartEntity.dataShowText)) {
				dataWithStyleMapList.get(i).put(ChartEntity.dataShowText, "第" + i + "数据");
			}
		}
		this.dataWithStyleMapList = dataWithStyleMapList;
	}

	public void setRoundChartStyleMap(Map<Integer, String> roundChartStyleMap) {
		// 判断图标样式完整性
		if (!roundChartStyleMap.containsKey(ChartEntity.roundChartPadding)) {
			roundChartStyleMap.put(ChartEntity.roundChartPadding, "56");
		}
		if (!roundChartStyleMap.containsKey(ChartEntity.roundChartWidth)) {
			roundChartStyleMap.put(ChartEntity.roundChartWidth, "120");
		}
		if (!roundChartStyleMap.containsKey(ChartEntity.roundChartShowWidth)) {
			roundChartStyleMap.put(ChartEntity.roundChartShowWidth, "128");
		}
		if (!roundChartStyleMap.containsKey(ChartEntity.roundChartShowDataSize)) {
			roundChartStyleMap.put(ChartEntity.roundChartShowDataSize, "108");
		}
		if (!roundChartStyleMap.containsKey(ChartEntity.roundChartShowTextSize)) {
			roundChartStyleMap.put(ChartEntity.roundChartShowTextSize, "40");
		}
		if (!roundChartStyleMap.containsKey(ChartEntity.roundChartShowTextColor)) {
			roundChartStyleMap.put(ChartEntity.roundChartShowTextColor, "#8c5e5e5e");
		}
		if (!roundChartStyleMap.containsKey(ChartEntity.roundChartShowTextBackground)) {
			roundChartStyleMap.put(ChartEntity.roundChartShowTextBackground, "#ffffff");
		}
		this.roundChartStyleMap = roundChartStyleMap;
	}

	/**
	 * 构造该函数
	 */
	public RoundChartView(Context context) {
		super(context);
		// 初始化图表样式
		setRoundChartStyleMap(roundChartStyleMap);
	}

	public RoundChartView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private Map<Integer, Double> historyPartMap = new HashMap<>();
	private boolean isShowData = false;
	private int dataIndex = 0;

	/**
	 * 重绘图形
	 * @param canvas 画布
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// 获取当前控件高度
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		// 获取当前数据总量
		int dataCountValue = 0;
		for(Map<Integer, String> dataWithStyleMap : dataWithStyleMapList) {
			dataCountValue += Integer.parseInt(dataWithStyleMap.get(ChartEntity.data));
		}
		int left;
		int right;
		int top;
		int bottom;
		// 定义一个圆形区域(要居中)
		if (viewWidth >= viewHeight) { // 横屏
			left = (viewWidth - viewHeight) / 2;
			right = viewWidth - left;
			top = 0;
			bottom = viewHeight;
		} else { // 竖屏
			left = 0;
			right = viewWidth;
			top = (viewHeight - viewWidth) / 2;
			bottom = viewHeight - top;
		}
		RectF rectInside = new RectF(   left + Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartPadding)),
										top + Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartPadding)),
										right - Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartPadding)),
										bottom - Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartPadding)));
		// 初始化开始角度
		float currentAngle = 0;
		// 遍历绘制饼状图
		int count = 0;
		for (int i = 0; i < dataWithStyleMapList.size(); i++) {
			Map<Integer, String> dataWithStyleMap = dataWithStyleMapList.get(i);
			historyPartMap.put(count, (double) currentAngle);
			count++;
			// 如果数据需要展示
			if (isShowData && dataIndex == i) {
				RectF rectOutside = new RectF(left, top, right, bottom);
				canvas.drawArc( rectOutside, // 弧线所使用的矩形区域大小
								currentAngle,  // 开始角度
								Float.parseFloat(dataWithStyleMap.get(ChartEntity.data)) / dataCountValue * 360, // 扫过的角度
								true, // 是否使用中心(解决使用空心画笔出现角度不正确原因)
								new ChartPaint().Paint("#8c" + dataWithStyleMapList.get(dataIndex).get(ChartEntity.dataShowColor).substring(1, dataWithStyleMapList.get(dataIndex).get(ChartEntity.dataShowColor).length()), 3));
			}
			// 绘制
			canvas.drawArc( rectInside, // 弧线所使用的矩形区域大小
							currentAngle,  // 开始角度
							Float.parseFloat(dataWithStyleMap.get(ChartEntity.data)) / dataCountValue * 360, // 扫过的角度
							true, // 是否使用中心(解决使用空心画笔出现角度不正确原因)
							new ChartPaint().Paint(dataWithStyleMap.get(ChartEntity.dataShowColor), 3));
			// 角度增加
			currentAngle += Float.parseFloat(dataWithStyleMap.get(ChartEntity.data)) / dataCountValue * 360;
		}
		// 绘制数据展现中心
		canvas.drawCircle(  viewWidth / 2,
							viewHeight / 2,
							(viewWidth > viewHeight ? viewHeight : viewWidth) / 2 - Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartWidth)),
							new ChartPaint().Paint(roundChartStyleMap.get(ChartEntity.roundChartShowTextBackground), 3));
		// 绘制数据文字
		if (isShowData) {
			canvas.drawText(dataWithStyleMapList.get(dataIndex).get(ChartEntity.data),
							viewWidth / 2,
							viewHeight / 2,
							new ChartTextPaint().chartText(dataWithStyleMapList.get(dataIndex).get(ChartEntity.dataShowColor), Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartShowDataSize))));
			canvas.drawText(dataWithStyleMapList.get(dataIndex).get(ChartEntity.dataShowText) + "(" + Math.round(Float.parseFloat(dataWithStyleMapList.get(dataIndex).get(ChartEntity.data)) / dataCountValue * 10000) / 100  + "%)",
							viewWidth / 2,
							viewHeight / 2 + Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartShowDataSize)),
							new ChartTextPaint().chartText(roundChartStyleMap.get(ChartEntity.roundChartShowTextColor), Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartShowTextSize))));
		}
	}

	/**
	 * 触摸事件捕捉
	 * @param event 触摸事件
	 * @return 返回
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 计算点击距图中心的距离(x轴上)
				double xDifference;
				if ((double) event.getX() > (double) getWidth() / 2) {
					xDifference = (double) event.getX() - (double) getWidth() / 2;
				} else {
					xDifference = (double) getWidth() / 2 - (double) event.getX();
				}
				// 计算点击距图中心的距离(y轴上)
				double yDifference;
				if ((double) event.getY() > (double) (getHeight()) / 2) {
					yDifference = (double) event.getY() - (double) getHeight() / 2;
				} else {
					yDifference = (double) getHeight() / 2 - (double) event.getY();
				}
				// 计算点击距中心的半径
				double r = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));
				// 计算内圆半径
				int rInside = ((getWidth() > getHeight() ? getHeight() : getWidth()) / 2 - Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartWidth)));
				// 计算外圆半径
				int rOutSide = rInside + Integer.parseInt(roundChartStyleMap.get(ChartEntity.roundChartWidth));
				if (r > rInside && r < rOutSide) { // 如果点击区域在圆弧内
					// 判断点击区域(计算角度)
					double angle = 0;
					if ((int) event.getX() > getWidth() / 2 && (int) event.getY() < getHeight() / 2) { // 第一象限
						angle = 360 - Math.toDegrees(Math.atan(yDifference / xDifference));
					} else if ((int) event.getX() < getWidth() / 2 && (int) event.getY() < getHeight() / 2) { // 第二象限
						angle = 180 + Math.toDegrees(Math.atan(yDifference / xDifference));
					} else if ((int) event.getX() < getWidth() / 2 && (int) event.getY() > getHeight() / 2) { // 第三象限
						angle = 180 - Math.toDegrees(Math.atan(yDifference / xDifference));
					} else if ((int) event.getX() > getWidth() / 2 && (int) event.getY() > getHeight() / 2) { // 第四象限
						angle = Math.toDegrees(Math.atan(yDifference / xDifference));
					}
					// 遍历历史区域,找出点击的区域
					for (int i = 0; i < historyPartMap.size(); i++) {
						if (i != historyPartMap.size() - 1) {
							if (angle > historyPartMap.get(i) && angle < historyPartMap.get(i + 1)) {
								isShowData = true;
								dataIndex = i;
								invalidate();
							}
						} else {
							if (angle > historyPartMap.get(i) && angle < 360) {
								isShowData = true;
								dataIndex = i;
								invalidate();
							}
						}
					}
				} else {
					isShowData = false;
					dataIndex = 0;
					invalidate();
				}
				return true;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				break;
			default:
				break;
		}
		return false;
	}
}
