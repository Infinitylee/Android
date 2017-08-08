/**
 * @fileName ThreadEntity
 * @describe Thread线程工具类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper.thread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class ThreadEntity {

	public int corePoolSizeInt = 0; // 核心线程池的大小
	public int maximumPoolSizeInt = Integer.MAX_VALUE; // 最大线程池的大小
	public long keepAliveTimeLong = 60L; // 保持线程实例空闲时间
	public TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS; // 保持线程实例空闲时间单位
	public SynchronousQueue<Runnable> runnableTaskQueue = new SynchronousQueue<>(); // 一个不存储元素的阻塞队列

	public int getCorePoolSizeInt() {
		return corePoolSizeInt;
	}

	public void setCorePoolSizeInt(int corePoolSizeInt) {
		this.corePoolSizeInt = corePoolSizeInt;
	}

	public int getMaximumPoolSizeInt() {
		return maximumPoolSizeInt;
	}

	public void setMaximumPoolSizeInt(int maximumPoolSizeInt) {
		this.maximumPoolSizeInt = maximumPoolSizeInt;
	}

	public long getKeepAliveTimeLong() {
		return keepAliveTimeLong;
	}

	public void setKeepAliveTimeLong(long keepAliveTimeLong) {
		this.keepAliveTimeLong = keepAliveTimeLong;
	}

	public TimeUnit getKeepAliveTimeUnit() {
		return keepAliveTimeUnit;
	}

	public void setKeepAliveTimeUnit(TimeUnit keepAliveTimeUnit) {
		this.keepAliveTimeUnit = keepAliveTimeUnit;
	}

	public SynchronousQueue<Runnable> getRunnableTaskQueue() {
		return runnableTaskQueue;
	}

	public void setRunnableTaskQueue(SynchronousQueue<Runnable> runnableTaskQueue) {
		this.runnableTaskQueue = runnableTaskQueue;
	}
}
