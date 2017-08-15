/**
 * @fileName CircleImageView
 * @describe 圆形的的ImageView
 * @author 李培铭
 * @time 2017-08-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.userinterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CircleImageView extends AppCompatImageView {

	public CircleImageView(Context context) {
		super(context);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 绘制
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		// 判断是否有图片
		if (drawable == null) {
			return;
		}
		// 判断控件是否已经绘制
		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		if (!(drawable instanceof BitmapDrawable)) {
			return;
		}
		// 判断获取的bitmap是否为空
		if (((BitmapDrawable) drawable).getBitmap() == null) {
			return;
		}
		// 新的bitmap对象
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
		// 获取宽度
		int width = getWidth();
		// 绘制圆形ImageView
		canvas.drawBitmap(getCroppedBitmap(bitmap, width), 0, 0, null);
	}

	/**
	 * 初始Bitmap对象的缩放裁剪过程
	 * @param bitmap 初始Bitmap对象
	 * @param radius 圆形图片直径大小
	 * @return 返回一个圆形的缩放裁剪过后的Bitmap对象
	 */
	public static Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
		Bitmap tempBitmap;
		// 比较初始Bitmap宽高和给定的圆形直径,判断是否需要缩放裁剪Bitmap对象
		if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
			tempBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
		} else {
			tempBitmap = bitmap;
		}
		Bitmap outputBitmap = Bitmap.createBitmap(tempBitmap.getWidth(), tempBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(outputBitmap);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, tempBitmap.getWidth(), tempBitmap.getHeight());
		// 设置消除齿距
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(Color.parseColor("#000000"));
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(tempBitmap.getWidth() / 2 + 0.7f, tempBitmap.getHeight() / 2 + 0.7f, tempBitmap.getWidth() / 2 + 0.1f, paint);
		// 设置两张图片的相交模式,在这里就是上面绘制的Circle和下面绘制的Bitmap
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(tempBitmap, rect, rect, paint);
		return outputBitmap;
	}
}
