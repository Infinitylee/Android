/**
 * @fileName IntegerHelper
 * @describe 整形助理类
 * @author 李培铭
 * @time 2017-07-25
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.basehelper;

import android.app.Activity;

public class IntegerHelper {

	private static IntegerHelper integerHelper;

	/**
	 * 静态实例化引导
	 * @return integerHelper
	 */
	public static IntegerHelper getInstance() {
		if (integerHelper == null) {
			integerHelper = new IntegerHelper();
		}
		return integerHelper;
	}

	/**
	 * 判断是否含有指定整形数
	 * @param haystack 干草堆
	 * @param needle 针
	 * @return Boolean
	 */
	private boolean isHaystackHasNeedle(int[] haystack, int needle) {
		for (int i : haystack) {
			if (i == needle) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据手机的分辨率从dp单位转成为px(像素)
	 * @param activity 活动
	 * @param dpValue dp值
	 */
	private int dpToPx(Activity activity, float dpValue) {
		final float scale = activity.getResources().getDisplayMetrics().density;
		return (int)(dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从px(像素)单位转成为dp
	 * @param activity 活动
	 * @param pxValue px值
	 */
	private int pxToDp(Activity activity, float pxValue) {
		final float scale = activity.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
}
