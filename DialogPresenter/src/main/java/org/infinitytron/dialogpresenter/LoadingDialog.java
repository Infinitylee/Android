/**
 * @fileName LoadingDialog
 * @describe 加载对话框
 * @author 李培铭
 * @time 2017-08-18
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.dialogpresenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.infinitytron.basehelper.ImageHelper;
import org.infinitytron.basehelper.SystemHelper;
import org.infinitytron.dialogpresenter.dialog.MaterialCircleLoadingView;

public class LoadingDialog {

	private static LoadingDialog loadingDialog = null;
	private Dialog dialog;

	public static LoadingDialog getInstance() {
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog();
		}
		return loadingDialog;
	}

	private LoadingDialog() { }

	/**
	 * 显示加载Dialog
	 * @param activity 活动
	 * @param mode 模式
	 * @param tip 提示信息
	 * @param isPreventBackKey 设置是否响应返回键或外屏触摸事件
	 */
	public void display(final Activity activity, int mode, String tip, boolean isPreventBackKey) {
		// 实例化一个dialog
		dialog = new Dialog(activity, R.style.loadingDialogStyle);
		// 设置窗口模式
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置关联视图
		dialog.setContentView(R.layout.loading_dialog);
		// 设置是否响应返回键或外屏触摸事件
		if (isPreventBackKey) {
			// 设置不响应外屏触摸事件
			dialog.setCanceledOnTouchOutside(false);
			// 设置不响应返回键
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					return true;
				}
			});
		}
		// 设置当前模式
		((MaterialCircleLoadingView) dialog.findViewById(R.id.materialCircleLoadingView)).setMode(mode);
		// 设置信息提示
		((TextView) dialog.findViewById(R.id.tipTextView)).setText(tip);
		// 获取窗口
		final Window window = dialog.getWindow();
		if (window != null) {
			window.getDecorView().post(new Runnable() {
				@Override
				public void run() {
					// 获取对话框当前的参数值
					WindowManager.LayoutParams layoutParams = window.getAttributes();
					// 设置dialog宽度
					layoutParams.width = SystemHelper.getInstance().dpToPx(activity, 140);
					layoutParams.height = SystemHelper.getInstance().dpToPx(activity, 168);
					// 进行赋值
					window.setAttributes(layoutParams);
					// 获取屏幕截图
					Bitmap bitmap = SystemHelper.getInstance().shortcut(activity);
					// 获取状态栏高度
					Rect rect = SystemHelper.getInstance().getStateBarHeight(activity);
					// 获取屏幕长和高
					Point size = SystemHelper.getInstance().getSystemDisplayInfo(activity);
					// 去掉标题栏
					bitmap = Bitmap.createBitmap(bitmap, 0, rect.top, size.x, size.y - rect.top);
					// 对图像进行裁剪
					int left = size.x / 2 - layoutParams.width / 2;
					int top = (size.y - rect.top) / 2 - layoutParams.height / 2;
					bitmap = Bitmap.createBitmap(bitmap, left, top, layoutParams.width, layoutParams.height);
					// 对图像进行模糊
					bitmap = ImageHelper.getInstance().gaussAmbiguity(bitmap, 64, true);
					// 圆角处理
					bitmap = ImageHelper.getInstance().getRoundedCornerBitmap(bitmap, SystemHelper.getInstance().dpToPx(activity, 8));
					window.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), bitmap));
				}
			});
			// 设置视图居中
			window.setGravity(Gravity.CENTER);
			// 设置Dialog出现和小时的动画效果
			window.setWindowAnimations(R.style.loadingDialogAnimationsStyle);
		}
		dialog.show();
	}

	/**
	 * 设置模式
	 * @param mode 模式(1-加载, 2-完成, 3-错误)
	 * @param tip 提示信息
	 */
	public void setMode(int mode, String tip) {
		if (dialog != null) { // 如果存在Dialog对象
			// 设置当前模式
			((MaterialCircleLoadingView) dialog.findViewById(R.id.materialCircleLoadingView)).setMode(mode);
			// 设置信息提示
			((TextView) dialog.findViewById(R.id.tipTextView)).setText(tip);
		}
	}

	/**
	 * 关闭加载Dialog
	 */
	public void close() {
		if (dialog != null) { // 如果存在Dialog对象
			// 隐藏dialog
			dialog.cancel();
			dialog = null;
		}
	}
}
