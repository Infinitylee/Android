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
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

class RoundChartView extends AppCompatImageView {

	private Paint paint;

	public RoundChartView(Context context) {
		super(context);
		paint = new Paint();
		// 设置一个画笔笔刷颜色为黄色
		paint.setColor(Color.YELLOW);
		// 设置结合处形状(Miter锐角, Round圆弧, Bevel直线)
		paint.setStrokeJoin(Paint.Join.ROUND);
		// 设置线帽子(BUTT无线帽, ROUND圆形线帽, SQUARE方形线帽)
		paint.setStrokeCap(Paint.Cap.ROUND);
		// 设置线宽
		paint.setStrokeWidth(3);
	}

	public RoundChartView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 重绘图形
	 * @param canvas 画布
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// 绘制圆形
		/*canvas.drawCircle(100, 100, 90, paint);*/

		// 绘制弧线区域
		/*// 定义一个矩形区域
		RectF rect = new RectF(0, 0, 300, 300);
		canvas.drawArc(rect, // 弧线所使用的矩形区域大小
				0,  // 开始角度
				90, // 扫过的角度
				false, // 是否使用中心
				paint);*/

		// 填满画布颜色
		/*canvas.drawColor(Color.BLUE);*/

		// 画出直线
		/*canvas.drawLine(10, 10, 100, 100, paint);*/

		// 画出椭圆
		// 定义一个矩形区域
		/*RectF oval = new RectF(0, 0, 200, 300);
		// 矩形区域内切椭圆
		canvas.drawOval(oval, paint);*/

		// 画出矩形
		/*RectF rect = new RectF(50, 50, 200, 200);
		canvas.drawRect(rect, paint);*/

		// 画出圆角矩形
		/*RectF rect = new RectF(50, 50, 200, 200);
		canvas.drawRoundRect(rect,
				30, // x轴的半径
				30, // y轴的半径
				paint);*/

		// 画出不规则图形
		/*Path path = new Path(); // 定义一条路径
		path.moveTo(10, 10); // 移动到坐标10,10
		path.lineTo(50, 60);
		path.lineTo(200,80);
		path.lineTo(10, 10);
		canvas.drawPath(path, paint);*/

		// 画出文字
		/*Path path = new Path(); // 定义一条路径
		path.moveTo(10, 10); // 移动到 坐标10,10
		path.lineTo(50, 60);
		path.lineTo(200,80);
		path.lineTo(10, 10);
		canvas.drawTextOnPath("Android777开发者博客", path, 0, 0, paint);*/
	}

	/**
	 * 触摸事件捕捉
	 * @param event 触摸事件
	 * @return 返回
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		invalidate(); // 重新绘制区域
		return false;
	}
}
