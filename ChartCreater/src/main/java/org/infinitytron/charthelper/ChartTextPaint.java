/**
 * @fileName ChartTextPaint
 * @describe 图表文字画笔类
 * @author 李培铭
 * @time 2017-08-01
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.charthelper;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

public class ChartTextPaint {

	public TextPaint chartText(String textColor, int textSize) {
		TextPaint textPaint = new TextPaint();
		textPaint.setColor(Color.parseColor(textColor));
		textPaint.setTextSize(textSize);
		textPaint.setAntiAlias(true);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setTextAlign(Paint.Align.CENTER);
		return textPaint;
	}
}
