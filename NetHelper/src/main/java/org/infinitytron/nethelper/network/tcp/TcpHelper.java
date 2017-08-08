/**
 * @fileName TcpHelper
 * @describe TCP通信服务服务类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper.network.tcp;

import android.os.Handler;
import android.os.Message;

import org.infinitytron.nethelper.NetHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class TcpHelper extends TcpEntity implements Runnable {

	private byte[] receiveMessage;

	private Boolean canReceiveBoolean = true;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			super.handleMessage(message);
			if (message.what == 1) {
				// 将接收到的内容转换成字符串后输出到接口进行回调
				netEventHandle.onReceive(null, receiveMessage);
			}
		}
	};

	public void run(){
		try {
			// 启动计时器计算超时时间并回调
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// 设置不可以接收消息,阻止回调
					canReceiveBoolean = false;
					// 消息超时回调
					netEventHandle.onTimeout();
				}
			}, receiveTimeout);
			// 发送数据
			// 实例化socket对象
			Socket socket = new Socket(sendAddress, sendPort);
			// 获取Socket的OutputStream对象用于发送数据。
			OutputStream outputStream = socket.getOutputStream();
			// 把数据写入到输出流中
			outputStream.write(sendMessage, 0, sendMessage.length);
			// 发送读取的数据到服务端
			outputStream.flush();
			// 消息发送回调
			netEventHandle.onSend();
			// 定义byteArrayOutputStream字节数组输出流以获取响应内容
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			// byteArray用于存放循环读取的临时数据
			byte[] tempByteArray = new byte[receiveCachePart];
			int count;
			// 循环读取内容
			while ((count = socket.getInputStream().read(tempByteArray, 0, receiveCachePart)) > 0) {
				byteArrayOutputStream.write(tempByteArray, 0, count);
			}
			// 发送消息到主线程中
			receiveMessage = byteArrayOutputStream.toByteArray();
			if (canReceiveBoolean) {
				// 取消超时回调
				timer.cancel();
				handler.obtainMessage(1).sendToTarget();
			}
			// 关闭连接
			outputStream.close();
			byteArrayOutputStream.close();
			socket.close();
		} catch (IOException e) {
			netEventHandle.error(NetHelper.newSocketError);
		}
	}
}
