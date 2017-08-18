/**
 * @fileName SystemHelper
 * @describe 系统助理类
 * @author 李培铭
 * @time 2017-07-25
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.basehelper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class SystemHelper {

	private static SystemHelper systemHelper;
	/**
	 * 静态实例化引导
	 * @return ReadWriteUtil
	 */
	public static SystemHelper getInstance() {
		if (systemHelper == null) {
			systemHelper = new SystemHelper();
		}
		return systemHelper;
	}

	/**
	 * 检查是否联网
	 * @param activity 活动
	 */
	public boolean checkInternet(Activity activity) {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return info != null && info.isAvailable();
	}

	/**
	 * 震动调用
	 * @param timeInt 振动时间
	 * @param Activity 活动
	 */
	public void vibration(int timeInt, Context Activity){
		Vibrator vibrator = (Vibrator) Activity.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(timeInt);

	}

	/**
	 * 获取系统SDK版本
	 * @return int 系统版本
	 */
	public int getSystemBuildApi() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 获取屏幕像素
	 * @param activity 活动
	 * @return 数组(0为宽,1为高)
	 */
	public Point getSystemDisplayInfo(Activity activity) {
		// 获取应用窗口
		Window window = activity.getWindow();
		// 获取用用窗口管理器
		WindowManager windowManager = window.getWindowManager();
		// 获取屏幕宽和高
		Display windowManagerDisplay = windowManager.getDefaultDisplay();
		// 实例化point对象
		Point pointSize = new Point();
		// 获取数据
		windowManagerDisplay.getSize(pointSize);
		// 返回数据
		return pointSize;
	}

	/**
	 * 获取系统状态栏高度
	 * @param activity 活动
	 * @return int高度
	 */
	public Rect getStateBarHeight(Activity activity) {
		Rect rect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		return rect;
	}

	/**
	 * 屏幕截图
	 * @return 位图信息
	 */
	public Bitmap shortcut(Activity activity) {
		// 获取屏幕图像
		final View decorView = activity.getWindow().getDecorView();
		decorView.setDrawingCacheEnabled(true);
		decorView.buildDrawingCache();
		return decorView.getDrawingCache();
	}

	/**
	 * 判断服务是否正在运行,是包名+服务的类名(例如:org.infinitytron.temp.TempService)
	 * @param activity 活动
	 * @param serviceName 服务进程名称
	 * @return true代表正在运行,false代表服务没有正在运行
	 */
	public boolean isServiceWork(Activity activity, String serviceName) {
		boolean isServiceWorkBoolean = false;
		ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(40);
		if (list.size() <= 0) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			String isWorkingServiceNameString = list.get(i).service.getClassName();
			if (isWorkingServiceNameString.equals(serviceName)) {
				isServiceWorkBoolean = true;
				break;
			}
		}
		return isServiceWorkBoolean;
	}

	/**
	 * 检查权限
	 * @param activity 活动
	 * @param permission 权限名称字符串数组
	 * @return boolean 权限是否被
	 */
	public boolean checkPermission(Activity activity, String[] permission) {
		boolean isPermissionBoolean = true;
		for (String permissionString : permission) {
			if (ContextCompat.checkSelfPermission(activity, permissionString) != PackageManager.PERMISSION_GRANTED) {
				isPermissionBoolean  = false;
				break;
			}
		}
		return isPermissionBoolean;
	}

	/**
	 * 请求权限
	 * @param activity 活动
	 * @param permission 权限名称
	 */
	public void requirePermission(Activity activity, String[] permission) {
		ActivityCompat.requestPermissions(activity, permission, 0);
	}

	/**
	 * 获取IMEI
	 * @param activity 活动
	 * @return IMEI设备id
	 */
	public String getPhoneIMEI(Activity activity) {
		TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * dp转px
	 */
	public int dpToPx(Activity activity, float dpValue) {
		return (int) (dpValue * activity.getResources().getDisplayMetrics().density + 0.5f);
	}
}
