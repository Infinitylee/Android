/**
 * @fileName SwipeBackActivity
 * @describe 滑动返回Activity
 * @author 李培铭
 * @time 2017-08-16
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.swipebackframe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

public class SwipeBackActivity extends AppCompatActivity implements BaseSlidingPaneLayout.PanelSlideListener {

	// 软引用图片缓存记录
	private static Bitmap bitmap = null;
	// 用来记录屏幕尺寸
	private Point size = new Point();
	// SlidingPaneLayout对象
	private BaseSlidingPaneLayout baseSlidingPaneLayout;
	// 用来存放当前activity布局所用到的界面的线性布局
	private FrameLayout frameLayout;
	// 用来展现上一个activity界面图像的ImageView控件
	private ImageView beforeActivityImageView;
	// 用来展现阴影图片的ImageView控件
	private ImageView shadowImageView;
	// 默认x轴的位移量
	private int defaultTranslationX = 100;
	// 用来显示阴影图像的ImageView控件宽度
	private int shadowWidthInt = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			// 实例化SlidingPaneLayout对象
			baseSlidingPaneLayout = new BaseSlidingPaneLayout(this);
			// 通过反射获取SlidingPaneLayout类里的mOverhangSize字段
			Field mOverhangSizeField = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
			// 设置访问性为可访问
			mOverhangSizeField.setAccessible(true);
			// 对私有变量mOverhangSize进行赋值,值为为0
			mOverhangSizeField.set(baseSlidingPaneLayout, 0);
			// 设置监听器
			baseSlidingPaneLayout.setPanelSlideListener(this);
			// 设置滑动时当前页面颜色变化为透明,即主界面颜色不变灰
			baseSlidingPaneLayout.setSliderFadeColor(Color.parseColor("#00000000"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		defaultTranslationX = dpToPx(defaultTranslationX);
		// 把用来显示阴影图像的ImageView控件的宽度由dp单位转为px单位
		shadowWidthInt = dpToPx(shadowWidthInt);

		// 实例化用来展现上一个activity界面图像的布局
		FrameLayout beforeActivityImageShowFrameLayout = new FrameLayout(this);
		// 实例化用来展现上一个activity界面图像的ImageView控件
		beforeActivityImageView = new ImageView(this);
		// 设置边距大小
		beforeActivityImageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		// 把转载上一个activity界面图像的ImageView控件添加到布局里
		beforeActivityImageShowFrameLayout.addView(beforeActivityImageView, 0);

		// 获取屏幕长和高
		getWindowManager().getDefaultDisplay().getSize(size);

		// 实例化用来放置当前activity界面的布局
		LinearLayout currentActivityLayout = new LinearLayout(this);
		// 设置布局方向为横向
		currentActivityLayout.setOrientation(LinearLayout.HORIZONTAL);
		// 设置背景颜色为透明
		currentActivityLayout.setBackgroundColor(Color.parseColor("#00000000"));
		// 设置边距大小
		currentActivityLayout.setLayoutParams(new ViewGroup.LayoutParams(size.x + shadowWidthInt, ViewGroup.LayoutParams.MATCH_PARENT));
		// 实例化一个线性布局,用来存放当前activity布局所用到的界面
		frameLayout = new FrameLayout(this);
		// 设置布局的背景颜色
		frameLayout.setBackgroundColor(Color.parseColor("#ffffff"));
		// 设置边距大小
		frameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

		// 实例化用来展现阴影图片的ImageView控件
		shadowImageView = new ImageView(this);
		// 给图像设置阴影图片
		shadowImageView.setBackgroundResource(R.drawable.shadow);
		// 设置边距大小
		shadowImageView.setLayoutParams(new LinearLayout.LayoutParams(shadowWidthInt, LinearLayout.LayoutParams.MATCH_PARENT));

		// 横向布局从左到右依次添加,先添加阴影图片
		currentActivityLayout.addView(shadowImageView);
		// 横向布局从左到右依次添加,再添加用来存放当前activity布局所用到的界面
		currentActivityLayout.addView(frameLayout);
		// 设置布局位置偏移,以隐藏阴影布局所占控件
		currentActivityLayout.setTranslationX(-shadowWidthInt);

		// 添加两个视图控件,先添加用来显示上一个activity界面图片的布局
		baseSlidingPaneLayout.addView(beforeActivityImageShowFrameLayout, 0);
		// 添加两个视图控件,后添加用来显示阴影和当前activity界面的布局
		baseSlidingPaneLayout.addView(currentActivityLayout, 1);
	}

	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	@Override
	public void setContentView(View view) {
		setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		try {
			// 设置用来显示上一activity界面图片的ImageView控件的布局
			beforeActivityImageView.setScaleType(ImageView.ScaleType.FIT_XY);
			// 设置用来显示上一activity界面图片的ImageView控件的图片
			beforeActivityImageView.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(baseSlidingPaneLayout, params);
		// 先移除历史界面,避免布局出现错误
		frameLayout.removeAllViews();
		// 添加当前activity界面到当前线性布局中
		frameLayout.addView(view, params);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 当监听到滑动时
	 * @param panel 视图
	 * @param slideOffset 滑动距离占屏幕的百分比
	 */
	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		// 给 用来展示上一activity界面图片的ImageView控件 添加滑动效果
		beforeActivityImageView.setTranslationX(slideOffset * defaultTranslationX - defaultTranslationX);
		shadowImageView.setAlpha(slideOffset < 0.8 ? 1 : (1.5f - slideOffset));

	}

	@Override
	public void onPanelOpened(View panel) {
		finish();
		this.overridePendingTransition(0, 0);
	}

	@Override
	public void onPanelClosed(View panel) { }

	/**
	 * 启动视觉差返回Activity
	 */
	public void startSwipeBackActivty(Activity activity, Class<?> activityClass) {
		Intent intent = new Intent(activity, activityClass);
		// 执行启动视觉差返回Activity
		startSwipeBackActivty(activity, intent, false);
	}

	/**
	 * 执行启动视觉差返回Activity
	 */
	private void startSwipeBackActivty(Activity activity, Intent intent, boolean isFullScreen) {
		// 执行启动前先保存本activity界面的图片
		screenshots(activity, isFullScreen);
		// 启动另一个activity
		activity.startActivity(intent);
		// 启动切换动画
		activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	/**
	 * 截屏并保存
	 */
	private void screenshots(Activity activity, boolean isFullScreen) {
		try {
			// View是你需要截图的View
			View decorView = activity.getWindow().getDecorView();
			decorView.setDrawingCacheEnabled(true);
			decorView.buildDrawingCache();
			bitmap = decorView.getDrawingCache();
			// 获取状态栏高度
			Rect frame = new Rect();
			activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			// 获取屏幕长和高
			activity.getWindowManager().getDefaultDisplay().getSize(size);
			// 去掉标题栏
			if (isFullScreen) {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, size.x, size.y);
			} else {
				bitmap = Bitmap.createBitmap(bitmap, 0, frame.top, size.x, size.y - frame.top);
			}
			decorView.destroyDrawingCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * dp转px
	 */
	private int dpToPx(float dpValue) {
		return (int) (dpValue * getResources().getDisplayMetrics().density + 0.5f);
	}
}
