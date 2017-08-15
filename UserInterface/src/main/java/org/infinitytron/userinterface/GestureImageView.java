/**
 * @fileName GestureImageView
 * @describe 支持手势的ImageView
 * @author 李培铭
 * @time 2017-08-13
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.userinterface;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

public class GestureImageView extends AppCompatImageView {

	// ImageView宽度
	private int imageViewWidth;
	// ImageView高度
	private int imageViewHeight;
	// 图片宽度
	private int imageWidth;
	// 图片高度
	private int imageHeight;
	// 图片最大缩放级别
	private float imageMaxScale = 10.0f;
	// 最小缩放级别
	private float imageMinScale = 1.0f;
	// 记录拖拉图片移动的矩阵
	private Matrix matrix = new Matrix();
	// 记录图片要进行拖拉时候的矩阵
	private Matrix currentMatrix = new Matrix();
	// 记录第一次点击的时间,以支持双击放大
	private long firstTouchTime = 0;
	// 双击的间隔
	private static final int intervalTime = 250;
	// 第一次点击的坐标
	private PointF firstPointF;
	// 记录拖拉照片模式或放大缩小照片模式,初始值为0
	private int mode = 0;
	// 拖拉照片模式
	private static final int MODE_DRAG = 1;
	// 放大缩小照片模式
	private static final int MODE_ZOOM = 2;
	// 用于记录开始时候的坐标位置
	private PointF startPoint = new PointF();
	// 两触点之间的距离
	private float startDis;
	// 两触点的中间点
	private PointF midPoint;

	public GestureImageView(Context context) {
		super(context);
		initView();
	}

	public GestureImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public GestureImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		this.setScaleType(ScaleType.FIT_CENTER);
		// 获取ImageView的宽高
		getImageViewWidthAndHeight();
		// 获得图片内在宽高
		getIntrinsicWidthHeight();
	}

	/**
	 * 获取ImageView的宽高
	 */
	private void getImageViewWidthAndHeight() {
		ViewTreeObserver vto2 = getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			public void onGlobalLayout() {
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				imageViewWidth = getWidth();
				imageViewHeight = getHeight();
			}
		});
	}

	/**
	 * 获得图片内在宽高
	 */
	private void getIntrinsicWidthHeight() {
		Drawable drawable = this.getDrawable();
		// 初始化bitmap的宽高
		imageHeight = drawable.getIntrinsicHeight();
		imageWidth = drawable.getIntrinsicWidth();
	}

	/**
	 * 重写触摸事件
	 * @param event 触摸事件
	 * @return boolean
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) { // 单点事件和多点事件处理
			case MotionEvent.ACTION_DOWN:
				// 设置拖拽模式
				mode = MODE_DRAG;
				// 记录ImageView当前的矩阵
				currentMatrix.set(getImageMatrix());
				// 记录当前点击位置
				startPoint.set(event.getX(), event.getY());
				// 设置要变化的矩阵
				matrix.set(currentMatrix);
				// 自适应设置imageView
				makeImageViewFit();
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == MODE_DRAG) { // 拖拉图片
					// 计算x轴的移动距离
					float dx = event.getX() - startPoint.x;
					// 计算y轴的移动距离
					float dy = event.getY() - startPoint.y;
					// 设置将要操作的矩阵为当前矩阵
					matrix.set(currentMatrix);
					float[] values = new float[9];
					matrix.getValues(values);
					// 比较矩阵,确保不超出边界
					dx = checkDxBound(values, dx);
					dy = checkDyBound(values, dy);
					// 进行矩阵位移
					matrix.postTranslate(dx, dy);
				} else if (mode == MODE_ZOOM) { // 放大缩小图片
					// 计算两个触点之间的距离
					float distance = distance(event);
					if (distance > 10f) { // 如果距离之间的像素大于10
						// 得到缩放倍数
						float scale = distance / startDis;
						// 设置将要操作的矩阵为当前矩阵
						matrix.set(currentMatrix);
						float[] values = new float[9];
						matrix.getValues(values);
						// 检验scale,使图像缩放后不会超出最大倍数
						scale = checkFitScale(scale, values);
						// 进行矩阵缩放
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				setDoubleTouchEvent(event);
			case MotionEvent.ACTION_POINTER_UP:
				mode = 0;
				float[] values = new float[9];
				matrix.getValues(values);
				// 居中图像
				makeImgCenter(values);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				mode = MODE_ZOOM;
				// 计算两个手指间的距离
				startDis = distance(event);
				// 计算两个手指间的中间点
				if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
					midPoint = mid(event);
					// 记录当前ImageView的缩放倍数
					currentMatrix.set(getImageMatrix());
				}
				break;
		}
		setImageMatrix(matrix);
		return true;
	}

	/**
	 * 计算两个触点之间的距离
	 */
	private float distance(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		// 使用勾股定理返回两点之间的距离
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * 计算两个触点之间的中间点
	 */
	private PointF mid(MotionEvent event) {
		float midX = (event.getX(1) + event.getX(0)) / 2;
		float midY = (event.getY(1) + event.getY(0)) / 2;
		return new PointF(midX, midY);
	}

	/**
	 * 和当前矩阵对比,检验dx,使图像移动后不会超出ImageView边界
	 */
	private float checkDxBound(float[] values, float dx) {
		float width = imageViewWidth;
		if (imageWidth * values[Matrix.MSCALE_X] < width)
			return 0;
		if (values[Matrix.MTRANS_X] + dx > 0)
			dx = -values[Matrix.MTRANS_X];
		else if (values[Matrix.MTRANS_X] + dx < -(imageWidth * values[Matrix.MSCALE_X] - width))
			dx = -(imageWidth * values[Matrix.MSCALE_X] - width) - values[Matrix.MTRANS_X];
		return dx;
	}

	/**
	 * 和当前矩阵对比,检验dy,使图像移动后不会超出ImageView边界
	 */
	private float checkDyBound(float[] values, float dy) {

		float height = imageViewHeight;
		if (imageHeight * values[Matrix.MSCALE_Y] < height)
			return 0;
		if (values[Matrix.MTRANS_Y] + dy > 0)
			dy = -values[Matrix.MTRANS_Y];
		else if (values[Matrix.MTRANS_Y] + dy < -(imageHeight
				* values[Matrix.MSCALE_Y] - height))
			dy = -(imageHeight * values[Matrix.MSCALE_Y] - height)
					- values[Matrix.MTRANS_Y];
		return dy;
	}

	/**
	 * 检验scale,使图像缩放后不会超出最大倍数
	 */
	private float checkFitScale(float scale, float[] values) {
		if (scale * values[Matrix.MSCALE_X] > imageMaxScale)
			scale = imageMaxScale / values[Matrix.MSCALE_X];
		if (scale * values[Matrix.MSCALE_X] < imageMinScale)
			scale = imageMinScale / values[Matrix.MSCALE_X];
		return scale;
	}

	/**
	 * 居中图片
	 */
	private void makeImgCenter(float[] values) {
		// 缩放后图片的宽高
		float zoomY = imageHeight * values[Matrix.MSCALE_Y];
		float zoomX = imageWidth * values[Matrix.MSCALE_X];
		// 图片左上角Y坐标
		float leftY = values[Matrix.MTRANS_Y];
		// 图片左上角X坐标
		float leftX = values[Matrix.MTRANS_X];
		// 图片右下角Y坐标
		float rightY = leftY + zoomY;
		// 图片右下角X坐标
		float rightX = leftX + zoomX;
		// 使图片垂直居中
		if (zoomY < imageViewHeight) {
			float marY = (imageViewHeight - zoomY) / 2.0f;
			matrix.postTranslate(0, marY - leftY);
		}
		// 使图片水平居中
		if (zoomX < imageViewWidth) {
			float marX = (imageViewWidth - zoomX) / 2.0f;
			matrix.postTranslate(marX - leftX, 0);
		}
		// 使图片缩放后上下不留白(即当缩放后图片的大小大于imageView的大小,但是上面或下面留出一点空白的话,将图片移动占满空白处)
		if (zoomY >= imageViewHeight) {
			if (leftY > 0) {// 判断图片上面留白
				matrix.postTranslate(0, -leftY);
			}
			if (rightY < imageViewHeight) {// 判断图片下面留白
				matrix.postTranslate(0, imageViewHeight - rightY);
			}
		}
		// 使图片缩放后左右不留白
		if (zoomX >= imageViewWidth) {
			if (leftX > 0) {// 判断图片左边留白
				matrix.postTranslate(-leftX, 0);
			}
			if (rightX < imageViewWidth) {// 判断图片右边不留白
				matrix.postTranslate(imageViewWidth - rightX, 0);
			}
		}
	}

	/**
	 * 让imageView显示最适合的宽高比例
	 */
	private void makeImageViewFit() {
		if (getScaleType() != ScaleType.MATRIX) {
			setScaleType(ScaleType.MATRIX);
			matrix.postScale(1.0f, 1.0f, imageViewWidth / 2, imageViewHeight / 2);
		}
	}

	/**
	 * 双击事件处理
	 */
	private void setDoubleTouchEvent(MotionEvent event) {
		float values[] = new float[9];
		matrix.getValues(values);
		// 存储当前时间
		long currentTime = System.currentTimeMillis();
		// 判断两次点击间距时间是否符合
		if (currentTime - firstTouchTime >= intervalTime) {
			firstTouchTime = currentTime;
			firstPointF = new PointF(event.getX(), event.getY());
		} else {
			// 判断两次点击之间的距离是否小于30f
			if (Math.abs(event.getX() - firstPointF.x) < 30f && Math.abs(event.getY() - firstPointF.y) < 30f) {
				// 判断当前缩放比例与最大最小的比例
				if (values[Matrix.MSCALE_X] < imageMaxScale) {
					matrix.postScale(imageMaxScale / values[Matrix.MSCALE_X], imageMaxScale / values[Matrix.MSCALE_X], event.getX(), event.getY());
				} else {
					matrix.postScale(imageMinScale / values[Matrix.MSCALE_X], imageMinScale / values[Matrix.MSCALE_X], event.getX(), event.getY());
				}
			}
		}
	}
}
