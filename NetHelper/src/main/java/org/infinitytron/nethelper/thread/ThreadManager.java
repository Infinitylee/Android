/**
 * @fileName ThreadManager
 * @describe Thread线程助理类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper.thread;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager extends ThreadEntity {

	public static ThreadManager threadManager;

	private ThreadPoolExecutor threadPoolExecutor;

	public static ThreadManager getInstance() {
		if (threadManager == null) {
			threadManager = new ThreadManager();
		}
		return threadManager;
	}

	/**
	 * 构造函数
	 */
	public ThreadManager() {
		if (threadPoolExecutor == null) {
			threadPoolExecutor = new  ThreadPoolExecutor(corePoolSizeInt, maximumPoolSizeInt, keepAliveTimeLong, keepAliveTimeUnit, runnableTaskQueue);
		}
	}

	/**
	 * 启动一个线程,达到最大容量时抛出异常
	 * @param runnable 网络线程
	 */
	public void startThreadWithoutReturn(Runnable runnable) throws RejectedExecutionException {
		this.threadPoolExecutor.execute(runnable);
	}

	/**
	 * 启动一个线程并返回运行结果,达到最大容量时抛出异常
	 * @param runnable 网络线程
	 */
	public Future<?> startThreadWithReturn(Runnable runnable) throws RejectedExecutionException {
		return this.threadPoolExecutor.submit(runnable);
	}

	/**
	 * 关闭线程池,等带所有线程结束,不再接收新线程
	 */
	public void shutdownThreadPoolWait() {
		this.threadPoolExecutor.shutdown();
	}

	/**
	 * 关闭线程池,强制关闭所有线程,并且清空任务缓存队列,返回尚未执行的任务。
	 */
	public List<Runnable> shutdownThreadPoolNow() {
		return this.threadPoolExecutor.shutdownNow();
	}

	/**
	 * 动态设置核心线程池的大小
	 * @param corePoolSizeInt 核心线程池的大小
	 */
	public void setCorePoolSizeInt(int corePoolSizeInt) {
		this.threadPoolExecutor.setCorePoolSize(corePoolSizeInt);
	}

	/**
	 * 动态设置最大线程池的大小
	 * @param maximumPoolSizeInt 最大线程池的大小
	 */
	public void setMaximumPoolSizeInt(int maximumPoolSizeInt) {
		this.threadPoolExecutor.setMaximumPoolSize(maximumPoolSizeInt);
	}

	/**
	 * 动态设置空闲线程闲置时间
	 * @param keepAlineTime 空闲线程闲置时间
	 * @param keepAliveTimeUnit 空闲线程闲置时间单位
	 */
	public void setKeepAlineTime(long keepAlineTime, TimeUnit keepAliveTimeUnit) {
		this.threadPoolExecutor.setKeepAliveTime(keepAlineTime, keepAliveTimeUnit);
	}

	/**
	 * 获取线程池需要执行的任务数量
	 */
	public long getTaskCount() {
		return this.threadPoolExecutor.getTaskCount();
	}

	/**
	 * 获取线程池在运行过程中已完成的任务数量,小于或等于taskCount
	 */
	public long getCompletedTaskCount() {
		return this.threadPoolExecutor.getCompletedTaskCount();
	}

	/**
	 * 线程池曾经创建过的最大线程数量
	 */
	public long getLargestPoolSize() {
		return this.threadPoolExecutor.getLargestPoolSize();
	}

	/**
	 * 线程池的线程数量
	 */
	public long getPoolSize() {
		return this.threadPoolExecutor.getPoolSize();
	}

	/**
	 * 获取活动的线程数
	 */
	public long getActiveCount() {
		return this.threadPoolExecutor.getActiveCount();
	}

	/**
	 * 获取当前队列里面的线程对象
	 */
	public BlockingQueue<Runnable> getQueue() {
		return this.threadPoolExecutor.getQueue();
	}

	/**
	 * 移除线程队列中的对象
	 * @param runnable 线程对象
	 */
	public void removeThread(Runnable runnable) {
		this.threadPoolExecutor.remove(runnable);
	}
}