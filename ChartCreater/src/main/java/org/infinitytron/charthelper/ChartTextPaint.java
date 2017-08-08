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
