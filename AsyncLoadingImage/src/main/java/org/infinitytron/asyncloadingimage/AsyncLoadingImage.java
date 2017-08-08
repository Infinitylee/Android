/**
 * @fileName FileHelper
 * @describe 文件助理类
 * @author 李培铭
 * @time 2017-07-29
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.asyncloadingimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import org.infinitytron.basehelper.FileHelper;
import org.infinitytron.basehelper.StringHelper;
import org.infinitytron.nethelper.NetEventHandle;
import org.infinitytron.nethelper.NetHelper;
import org.infinitytron.nethelper.network.tcp.http.get.GetHelper;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsyncLoadingImage {

	private static AsyncLoadingImage asyncLoadingImage;

	private HashMap<String, GetHelper> runnableMap = new HashMap<>(); // 进程历史纪录
	private Map<String, SoftReference<Bitmap>> imageCachesMap = new HashMap<>(); // 软引用图片缓存记录

	/**
	 * 静态实例化引导
	 * @return asyncLoadingImage
	 */
	public static AsyncLoadingImage getInstance() {
		if (asyncLoadingImage == null) {
			asyncLoadingImage = new AsyncLoadingImage();
		}
		return asyncLoadingImage;
	}

	/**
	 * 图片异步下载
	 * @param activity 活动
	 * @param getHelper 请求对象
	 * @param imageView imageView控件
	 * @param pathString 文件存储路径
	 */
	public void asyncLoading(final Activity activity, final GetHelper getHelper, final ImageView imageView, final String pathString) {
		// 从图片缓存记录中获取相应缓存对象
		SoftReference<Bitmap> tempBitmap = imageCachesMap.get(getHelper.getSendAddress());
		// 实例化软引用位图缓存对象
		Bitmap softReferenceBitmap = null;
		// (1)先从软引用中取数据
		if (tempBitmap != null) { // 如果软引用图片对象不为空,则获取图片
			softReferenceBitmap = tempBitmap.get();
		}
		// 实例化图片名字符串对象
		String imageNameString = "";
		if (getHelper.getSendAddress() != null) { // 如果请求地址不为空,这获取地址上的图片名
			imageNameString = StringHelper.getInstance().md5(getHelper.getSendAddress());
		}
		// (2)如果从图片缓存记录中获取不到相应缓存对象,则根据文件存放地址和文件名获取图片的位图对象
		Bitmap bitmap = FileHelper.getInstance().getBitmapFromFile(activity, pathString, imageNameString);
		// 条件分析
		if (softReferenceBitmap != null && imageView != null && getHelper.getSendAddress() != null && getHelper.getSendAddress().equals(imageView.getTag(R.id.asyncLoadingImage).toString())) { // 判断从软引用中获取的相关数据是否为空
			// 当视图标记相同时,为imageView设置图像
			imageView.setImageBitmap(softReferenceBitmap);
		} else if (bitmap != null && imageView != null && getHelper.getSendAddress() != null && getHelper.getSendAddress().equals(imageView.getTag(R.id.asyncLoadingImage).toString())) { // 判断从文件中获取的相关数据是否为空
			imageView.setImageBitmap(bitmap);
		} else if (imageView != null && getHelper.getSendAddress() != null && needCreateNewRunnable(imageView)) { // 如果文件中获取不到图像文件,此时根据imageView的tag,即url去判断该url对应的Runnable是否已经在执行,如果在执行,本次操作不创建新的线程,否则创建新的线程
			// 给请求线程设置回调函数
			getHelper.setNetEventHandle(new NetEventHandle() {
				@Override
				public void onSend() { }
				@Override
				public void onReceive(Map<String, List<String>> receiveHeader, byte[] receiveMessage) {
					// 把接收过来的字节数组转化成位图
					Bitmap bitmap = BitmapFactory.decodeByteArray(receiveMessage, 0, receiveMessage.length);
					if (!FileHelper.getInstance().saveImage(activity, bitmap, 100, pathString, StringHelper.getInstance().md5(getHelper.getSendAddress()))) { // 把位图储存成图片文件
						FileHelper.getInstance().removeBitmapFromFile(activity, pathString, StringHelper.getInstance().md5(getHelper.getSendAddress()));
					}
					// 把位图放入软引用图片缓存记录
					imageCachesMap.put(getHelper.getSendAddress(), new SoftReference<>(Bitmap.createScaledBitmap(bitmap, 100, 100, true)));
					// 设置图片
					if (getHelper.getSendAddress().equals(imageView.getTag(R.id.asyncLoadingImage).toString())) {
						imageView.setImageBitmap(bitmap);
					}
					// 将对应的url对应的任务从进程历史纪录中删除
					runnableMap.remove(getHelper.getSendAddress());
				}
				@Override
				public void onTimeout() {
					runnableMap.remove(getHelper.getSendAddress());
				}
				@Override
				public void error(Integer errorCode) {
					runnableMap.remove(getHelper.getSendAddress());
				}
			});
			// 提交请求
			NetHelper.getInstance().startNetworkWithoutReturn(getHelper);
			// 将对应的url对应的任务存起来
			runnableMap.put(getHelper.getSendAddress(), getHelper);
		}
	}

	/**
	 * 判断是否需要重新创建线程下载图片,如果需要,返回值为true。
	 * @param imageView imageView控件
	 * @return Boolean
	 */
	private boolean needCreateNewRunnable(ImageView imageView){
		boolean isNeedCreateNewTask = true;
		if (imageView != null) {
			String currentRunnableUrlString = imageView.getTag(R.id.asyncLoadingImage).toString();
			if (runnableMap != null && runnableMap.get(currentRunnableUrlString) != null) {
				isNeedCreateNewTask = false;
			}
		}
		return isNeedCreateNewTask;
	}

}
