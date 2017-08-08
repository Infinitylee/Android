/**
 * @fileName NetHelper
 * @describe 网络请求助理类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper;

import org.infinitytron.nethelper.thread.ThreadManager;

import java.util.concurrent.Future;

public class NetHelper extends NetEntity {

	private static NetHelper netHelper;

	public static NetHelper getInstance() {
		if (netHelper == null) {
			netHelper = new NetHelper();
		}
		return netHelper;
	}

	/**
	 * 加入一个网络请求
	 */
	public void startNetworkWithoutReturn(Runnable runnable) {
		ThreadManager.getInstance().startThreadWithoutReturn(runnable);
	}

	/**
	 * 加入一个网络请求并返回一个future
	 */
	public Future<?> startNetworkWithReturn(Runnable runnable) {
		return ThreadManager.getInstance().startThreadWithReturn(runnable);
	}

	/**
	 * 移除一个未被开始的网络请求
	 */
	public void removeNetworkUnstart(Runnable runnable) {
		ThreadManager.getInstance().removeThread(runnable);
	}

	/**
	 * 等所有线程完成时停止线程池
	 */
	public void stopNetHelperAllFinish() {
		ThreadManager.getInstance().shutdownThreadPoolWait();
	}

	/**
	 * 立即停止线程池,并清除所有队列
	 */
	public void stopNetHelperNow() {
		ThreadManager.getInstance().shutdownThreadPoolNow();
	}
}
