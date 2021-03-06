/**
 * @fileName ChartPaint
 * @describe 图表画笔类
 * @author 李培铭
 * @time 2017-08-01
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.charthelper;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IdRes;

public class ChartPaint {

	public Paint Paint(String paintColor, int paintWidth) {
		// 设置画笔
		Paint paint = new Paint();
		// 设置一个画笔笔刷颜色为黄色
		paint.setColor(Color.parseColor(paintColor));
		// 设置结合处形状(Miter锐角, Round圆弧, Bevel直线)
		paint.setStrokeJoin(Paint.Join.ROUND);
		// 设置线帽子(BUTT无线帽, ROUND圆形线帽, SQUARE方形线帽)
		paint.setStrokeCap(Paint.Cap.ROUND);
		// 设置线宽
		paint.setStrokeWidth(paintWidth);
		// 设置消除齿距
		paint.setAntiAlias(true);
		// 设置空心效果
		/*if (strokeWidth != 0) {
			paint.setStrokeWidth(40);
			paint.setStyle(Paint.Style.STROKE);
		}*/
		return paint;
	}
}
