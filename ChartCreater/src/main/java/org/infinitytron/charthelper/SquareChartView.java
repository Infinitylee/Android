/**
 * @fileName SquareChartView
 * @describe 方形图表视图类
 * @author 李培铭
 * @time 2017-08-01
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.charthelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SquareChartView extends View {

	public List<Map<Integer, String>> xyMapList = new ArrayList<>(); // xy下标内容集合列表(1x轴, 2y轴)
	public List<Map<Integer, String>> xyStyleMapList = new ArrayList<>(); // xy轴和下标展现的样式集合列表(1x轴, 2y轴)
	public List<Map<Integer, Integer>> dataMapList = new ArrayList<>(); // 数据内容集合列表
	public List<Map<Integer, String>> dataStyleMapList = new ArrayList<>(); // 数据展现的样式集合列表
	public Map<Integer, String> squareChartStyleMap = new HashMap<>(); // 方形图表样式集合

	/**
	 * 设置xy轴下标内容
	 */
	public void setXyMapList(List<Map<Integer, String>> xyMapList) {
		// 检测xy轴的下标内容数据完整性
		if (xyMapList.contains(0) && xyMapList.get(0).size() == 0) {
			for (int i = 0; i < 10; i++) {
				xyMapList.get(0).put(i, String.valueOf(i));
			}
		} else {
			Map<Integer, String> xMap = new HashMap<>();
			for (int i = 0; i < 10; i++) {
				xMap.put(i, String.valueOf(i));
			}
			xyMapList.add(xMap);
		}
		if (xyMapList.contains(1) && xyMapList.get(1).size() == 0) {
			for (int i = 0; i < 10; i++) {
				xyMapList.get(1).put(i, String.valueOf(i));
			}
		} else {
			Map<Integer, String> yMap = new HashMap<>();
			for (int i = 0; i < 10; i++) {
				yMap.put(i, String.valueOf(i));
			}
			xyMapList.add(yMap);
		}
		this.xyMapList = xyMapList;
	}

	/**
	 * 设置xy轴样式集合列表
	 */
	public void setXyStyleMapList(List<Map<Integer, String>> xyStyleMapList) {
		// 判断xy轴和下标展现的样式集合列表(1x轴, 2y轴)数据完整性
		if (xyStyleMapList.contains(0)) {
			if (!xyStyleMapList.get(0).containsKey(ChartEntity.xMarginBottom)) {
				xyStyleMapList.get(0).put(ChartEntity.xMarginBottom, "100");
			}
			if (!xyStyleMapList.get(0).containsKey(ChartEntity.xTextSize)) {
				xyStyleMapList.get(0).put(ChartEntity.xTextSize, "24");
			}
			if (!xyStyleMapList.get(0).containsKey(ChartEntity.xTextColor)) {
				xyStyleMapList.get(0).put(ChartEntity.xTextColor, "#5e5e5e");
			}
			if (!xyStyleMapList.get(0).containsKey(ChartEntity.xTextMarginTopLine)) {
				xyStyleMapList.get(0).put(ChartEntity.xTextMarginTopLine, "30");
			}
			if (!xyStyleMapList.get(0).containsKey(ChartEntity.xLineWidth)) {
				xyStyleMapList.get(0).put(ChartEntity.xLineWidth, "3");
			}
			if (!xyStyleMapList.get(0).containsKey(ChartEntity.xLineColor)) {
				xyStyleMapList.get(0).put(ChartEntity.xLineColor, "#dedede");
			}
		} else {
			Map<Integer, String> xMapStyle = new HashMap<>();
			xMapStyle.put(ChartEntity.xMarginBottom, "100");
			xMapStyle.put(ChartEntity.xTextSize, "24");
			xMapStyle.put(ChartEntity.xTextColor, "#5e5e5e");
			xMapStyle.put(ChartEntity.xTextMarginTopLine, "30");
			xMapStyle.put(ChartEntity.xLineWidth, "3");
			xMapStyle.put(ChartEntity.xLineColor, "#dedede");
			xyStyleMapList.add(xMapStyle);
		}
		if (xyStyleMapList.contains(1)) {
			if (!xyStyleMapList.get(1).containsKey(ChartEntity.yMarginLeft)) {
				xyStyleMapList.get(1).put(ChartEntity.yMarginLeft, "60");
			}
			if (!xyStyleMapList.get(1).containsKey(ChartEntity.yTextSize)) {
				xyStyleMapList.get(1).put(ChartEntity.yTextSize, "16");
			}
			if (!xyStyleMapList.get(1).containsKey(ChartEntity.yTextColor)) {
				xyStyleMapList.get(1).put(ChartEntity.yTextColor, "#5e5e5e");
			}
			if (!xyStyleMapList.get(1).containsKey(ChartEntity.yTextMarginRightLine)) {
				xyStyleMapList.get(1).put(ChartEntity.yTextMarginRightLine, "30");
			}
			if (!xyStyleMapList.get(1).containsKey(ChartEntity.yLineWidth)) {
				xyStyleMapList.get(1).put(ChartEntity.yLineWidth, "3");
			}
			if (!xyStyleMapList.get(1).containsKey(ChartEntity.yLineColor)) {
				xyStyleMapList.get(1).put(ChartEntity.yLineColor, "#dedede");
			}
		} else {
			Map<Integer, String> yMapStyle = new HashMap<>();
			yMapStyle.put(ChartEntity.yMarginLeft, "60");
			yMapStyle.put(ChartEntity.yTextSize, "16");
			yMapStyle.put(ChartEntity.yTextColor, "#5e5e5e");
			yMapStyle.put(ChartEntity.yTextMarginRightLine, "30");
			yMapStyle.put(ChartEntity.yLineWidth, "3");
			yMapStyle.put(ChartEntity.yLineColor, "#dedede");
			xyStyleMapList.add(yMapStyle);
		}
		this.xyStyleMapList = xyStyleMapList;
	}

	/**
	 * 设置数据集合列表
	 */
	public void setDataMapList(List<Map<Integer, Integer>> dataMapList) {
		this.dataMapList = dataMapList;
	}

	/**
	 * 设置数据样式集合列表
	 */
	public void setDataStyleMapList(List<Map<Integer, String>> dataStyleMapList) {
		this.dataStyleMapList = dataStyleMapList;
	}

	/**
	 * 设置图表样式集合
	 */
	public void setSquareChartStyleMap(Map<Integer, String> squareChartStyleMap) {
		// 判断图表样式集合列表数据完整性
		if (!squareChartStyleMap.containsKey(ChartEntity.chartSimpleTextSize)) {
			squareChartStyleMap.put(ChartEntity.chartSimpleTextSize, "24");
		}
		if (!squareChartStyleMap.containsKey(ChartEntity.chartSimpleTextColor)) {
			squareChartStyleMap.put(ChartEntity.chartSimpleTextColor, "#ffffff");
		}
		if (!squareChartStyleMap.containsKey(ChartEntity.chartSimpleLayoutWidth)) {
			squareChartStyleMap.put(ChartEntity.chartSimpleLayoutWidth, "50");
		}
		if (!squareChartStyleMap.containsKey(ChartEntity.chartSimpleLayoutHeight)) {
			squareChartStyleMap.put(ChartEntity.chartSimpleLayoutHeight, "50");
		}
		if (!squareChartStyleMap.containsKey(ChartEntity.chartSimpleLayoutBackground)) {
			squareChartStyleMap.put(ChartEntity.chartSimpleLayoutBackground, "#8c5e5e5e");
		}
		this.squareChartStyleMap = squareChartStyleMap;
	}

	/**
	 * 构造该函数
	 */
	public SquareChartView(Context context) {
		super(context);
		// 初始化图表样式
		setSquareChartStyleMap(squareChartStyleMap);
		// 初始化xy轴的下标内容
		setXyMapList(xyMapList);
		// 初始化xy轴样式列表
		setXyStyleMapList(xyStyleMapList);
	}

	public SquareChartView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private List<Map<String, Integer>> historyPointAllMapList = new ArrayList<>();
	private int x = 0;
	private int y = 0;
	private int data = 0;

	/**
	 * 重绘图形
 	 * @param canvas 画布
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// 判断 数据展示样式完整性 和 数据组数是否和数据展示样式组数一致
		if (dataMapList.size() > dataStyleMapList.size()) { // 如果数据样式比数据组数小,则添加默认数据样式
			// 检测已有的数据样式完整性
			for (int i = 0; i < dataStyleMapList.size(); i++) {
				Map<Integer, String> dataStyleMap = dataStyleMapList.get(i);
				if (dataStyleMap.get(ChartEntity.dataLineColor) == null) {
					dataStyleMap.put(ChartEntity.dataLineColor, "#dedede");
				}
				if (dataStyleMap.get(ChartEntity.dataLineWidth) == null) {
					dataStyleMap.put(ChartEntity.dataLineWidth, "3");
				}
				if (dataStyleMap.get(ChartEntity.dataPointColor) == null) {
					dataStyleMap.put(ChartEntity.dataPointColor, "#8c5e5e5e");
				}
				if (dataStyleMap.get(ChartEntity.dataPointRadius) == null) {
					dataStyleMap.put(ChartEntity.dataPointRadius, "5");
				}
			}
			// 添加缺省的数据样式
			int styleDifferenceInt = dataMapList.size() - dataStyleMapList.size();
			for (int i = 0; i < styleDifferenceInt; i++) {
				Map<Integer, String> dataStyle = new HashMap<>();
				dataStyle.put(ChartEntity.dataLineColor, "#dedede");
				dataStyle.put(ChartEntity.dataLineWidth, "3");
				dataStyle.put(ChartEntity.dataPointColor, "#8c5e5e5e");
				dataStyle.put(ChartEntity.dataPointRadius, "5");
				dataStyleMapList.add(dataStyle);
			}
		}
		// 获取样式
		Map<Integer, String> xStyleMap = xyStyleMapList.get(0);
		Map<Integer, String> yStyleMap = xyStyleMapList.get(1);
		// 设置画笔
		Paint xPaintLine = new ChartPaint().Paint(xStyleMap.get(ChartEntity.xLineColor), Integer.parseInt(xStyleMap.get(ChartEntity.xLineWidth)));
		Paint yPaintLine = new ChartPaint().Paint(yStyleMap.get(ChartEntity.yLineColor), Integer.parseInt(yStyleMap.get(ChartEntity.yLineWidth)));
		// 获取当前控件高度
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		// 绘制x轴和y轴
		canvas.drawLine(Integer.parseInt(yStyleMap.get(ChartEntity.yMarginLeft)),
				viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)),
				viewWidth - Integer.parseInt(yStyleMap.get(ChartEntity.yMarginLeft)),
				viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)),
				xPaintLine);
		canvas.drawLine(Integer.parseInt(yStyleMap.get(ChartEntity.yMarginLeft)),
				viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)),
				Integer.parseInt(yStyleMap.get(ChartEntity.yMarginLeft)),
				Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)),
				yPaintLine);
		// 绘制x轴和y轴下标
		// 根据x轴和y轴下标内容计算间距
		int xInterval = (viewWidth - Integer.parseInt(yStyleMap.get(ChartEntity.yMarginLeft)) * 2) / xyMapList.get(0).size();
		int yInterval = (viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)) * 2) / xyMapList.get(1).size();
		for (int i = 0; i < xyMapList.get(0).size(); i++) {
			canvas.drawText(xyMapList.get(0).get(i),
					Integer.parseInt(yStyleMap.get(ChartEntity.yMarginLeft)) + xInterval * i,
					viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)) + Integer.parseInt(xStyleMap.get(ChartEntity.xTextMarginTopLine)),
					new ChartTextPaint().chartText(xStyleMap.get(ChartEntity.xTextColor), Integer.parseInt(xStyleMap.get(ChartEntity.xTextSize))));
		}
		for (int i = 0; i < xyMapList.get(1).size(); i++) {
			canvas.drawText(xyMapList.get(1).get(i),
					Integer.parseInt(yStyleMap.get(ChartEntity.yMarginLeft)) - Integer.parseInt(yStyleMap.get(ChartEntity.yTextMarginRightLine)),
					viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)) - yInterval * (i),
					new ChartTextPaint().chartText(xStyleMap.get(ChartEntity.xTextColor), Integer.parseInt(xStyleMap.get(ChartEntity.xTextSize))));
		}
		// 绘制数据
		for (int i = 0; i < dataMapList.size(); i++) {
			Map<Integer, Integer> dataMap = dataMapList.get(i);
			int count;
			if (dataMap.size() > xyMapList.get(0).size()) {
				count = xyMapList.get(0).size();
			} else {
				count = dataMap.size();
			}
			List<Map<String, Integer>> historyPointList = new ArrayList<>();
			for (int j = 0; j < count; j++) {
				int x = Integer.parseInt(yStyleMap.get(ChartEntity.yMarginLeft)) + xInterval * j;
				int y;
				int ylineHeight = viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)) * 2;
				int ydataDifference = Integer.parseInt(xyMapList.get(1).get(xyMapList.get(1).size() - 1)) - Integer.parseInt(xyMapList.get(1).get(0));
				int unit = (ylineHeight - yInterval) / ydataDifference;
				if (ylineHeight - yInterval >= unit * dataMap.get(j)) {
					y = Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)) + ylineHeight - unit * dataMap.get(j);
				} else {
					y = Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)) + yInterval;
				}
				// 把点信息记录到列表中
				Map<String, Integer> historyPoint = new HashMap<>();
				historyPoint.put("x", x);
				historyPoint.put("y", y);
				historyPoint.put("data", dataMap.get(j));
				historyPointList.add(historyPoint);
				// 把点信息记录到全局列表中,以区分响应事件
				this.historyPointAllMapList.add(historyPoint);
			}
			// 设置折线画笔
			Paint dataLinePaint = new ChartPaint().Paint(dataStyleMapList.get(i).get(ChartEntity.dataLineColor), Integer.parseInt(dataStyleMapList.get(i).get(ChartEntity.dataLineWidth)));
			// 绘制折线
			Path path = new Path(); // 定义一条路径
			for (int j = 0; j < historyPointList.size(); j++) {
				if (j == 0) {
					path.moveTo(historyPointList.get(j).get("x"), historyPointList.get(j).get("y"));
				} else {
					// 计算控制点一和控制点二
					int xControllerOnInt;
					int yControllerOnInt;
					int xControllerTwInt;
					int yControllerTwInt;
					if (historyPointList.get(j).get("x") > historyPointList.get(j - 1).get("x")) { // 如果斜率为正
						xControllerOnInt = historyPointList.get(j - 1).get("x") + xInterval / 2;
						yControllerOnInt = historyPointList.get(j - 1).get("y") + 3;
						xControllerTwInt = historyPointList.get(j).get("x") - xInterval / 2;
						yControllerTwInt = historyPointList.get(j).get("y") - 3;
					} else {
						xControllerOnInt = historyPointList.get(j - 1).get("x") + xInterval / 2;
						yControllerOnInt = historyPointList.get(j - 1).get("y") - 3;
						xControllerTwInt = historyPointList.get(j).get("x") - xInterval / 2;
						yControllerTwInt = historyPointList.get(j).get("y") + 3;
					}
					// 绘制曲线
					path.cubicTo(xControllerOnInt, yControllerOnInt, xControllerTwInt, yControllerTwInt, historyPointList.get(j).get("x"), historyPointList.get(j).get("y"));
				}
			}
			path.lineTo(historyPointList.get(historyPointList.size() - 1).get("x"), viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)));
			path.lineTo(historyPointList.get(0).get("x"), viewHeight - Integer.parseInt(xStyleMap.get(ChartEntity.xMarginBottom)));
			path.lineTo(historyPointList.get(0).get("x"), historyPointList.get(0).get("y"));
			canvas.drawPath(path, dataLinePaint);
			// 设置点画笔
			Paint dataPointPaint = new ChartPaint().Paint(dataStyleMapList.get(i).get(ChartEntity.dataPointColor), Integer.parseInt(dataStyleMapList.get(i).get(ChartEntity.dataLineWidth)));
			// 绘制点
			for (int j = 0; j < historyPointList.size(); j++) {
				canvas.drawCircle(historyPointList.get(j).get("x"), historyPointList.get(j).get("y"), Integer.parseInt(dataStyleMapList.get(i).get(ChartEntity.dataPointRadius)), dataPointPaint);
			}
		}
		// 绘制信息提示框
		if (x != 0 && y != 0) {
			// 绘制外边框
			RectF rect = new RectF(x - Integer.parseInt(squareChartStyleMap.get(ChartEntity.chartSimpleLayoutWidth)) / 2, y - 25 - Integer.parseInt(squareChartStyleMap.get(ChartEntity.chartSimpleLayoutHeight)), x + Integer.parseInt(squareChartStyleMap.get(ChartEntity.chartSimpleLayoutWidth)) / 2, y - 25);
			canvas.drawRoundRect(   rect,
									5, // x轴的半径
									5, // y轴的半径
									new ChartPaint().Paint(squareChartStyleMap.get(ChartEntity.chartSimpleLayoutBackground), 16));
			// 填充信息
			canvas.drawText(String.valueOf(data),
							x,
							y - 40,
							new ChartTextPaint().chartText(squareChartStyleMap.get(ChartEntity.chartSimpleTextColor), Integer.parseInt(squareChartStyleMap.get(ChartEntity.chartSimpleTextSize))));
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
				for (int i = 0; i < this.historyPointAllMapList.size(); i++) {
					int x = historyPointAllMapList.get(i).get("x");
					int y = historyPointAllMapList.get(i).get("y");
					if (x <= (int) event.getX() + 20 && x >= (int) event.getX() - 20 && y <= (int) event.getY() + 20 && y >= (int) event.getY() - 20) {
						this.x = x;
						this.y = y;
						this.data = historyPointAllMapList.get(i).get("data");
						invalidate();
						return true;
					}
				}
				this.x = 0;
				this.y = 0;
				invalidate();
				return true;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				break;
			default:
				break;
		}
		return super.onTouchEvent(event);
	}
}
