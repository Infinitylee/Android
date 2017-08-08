/**
 * @fileName GetHelper
 * @describe TCP通信服务Http中get助理类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper.network.tcp.http.get;

import android.os.Handler;
import android.os.Message;

import org.infinitytron.nethelper.NetHelper;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GetHelper extends GetEntity implements Runnable{

	private Boolean canReceiveBoolean = true;

	private Map<String, List<String>> receiveHeader;
	private byte[] receiveMessage;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 此处可以更新UI
			netEventHandle.onReceive(receiveHeader, receiveMessage);
		}
	};

	/**
	 * get消息线程
	 */
	public void run() {
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
		try {
			// 处理要提交的表单
			// 形式:
			// keyOn=valueOn&keyTw=valueTw&keyTh=valueTh
			String sendMessageString;
			if (sendTextMap.size() != 0) {
				// 生成衔接头
				sendMessageString = "?";
				for (Map.Entry<String, String> entry : sendTextMap.entrySet()) {
					sendMessageString += entry.getKey() + "=" + entry.getValue() + "&";
				}
				// 去掉字符串最后一个字符
				sendMessageString = sendMessageString.substring(0, sendMessageString.length() - 1);

			} else {
				sendMessageString = "";
			}
			URL realUrl = new URL(sendAddress + sendMessageString);
			// 打开和URL之间的连接
			URLConnection URLConnection = realUrl.openConnection();
			// 设置通用的请求属性
			URLConnection.setRequestProperty("accept", sendHeaderAccept);
			URLConnection.setRequestProperty("accept-encoding", sendHeaderAcceptEncoding);
			URLConnection.setRequestProperty("accept-language", sendHeaderAcceptLanguage);
			URLConnection.setRequestProperty("connection", sendHeaderConnection);
			if (!sendHeaderReferer.isEmpty()) {
				URLConnection.setRequestProperty("referer", sendHeaderReferer);
			}
			if (!sendHeaderHost.isEmpty()) {
				URLConnection.setRequestProperty("host", sendHeaderHost);
			}
			if (sendHeaderCookieMap.size() != 0) {
				String tempCookieString = "";
				for (Map.Entry<String, String> entry : sendHeaderCookieMap.entrySet()) {
					tempCookieString += entry.getKey() + "=" + entry.getValue() + ";";
				}
				URLConnection.setRequestProperty("cookie", tempCookieString);
			}
			URLConnection.setRequestProperty("user-agent", sendHeaderUserAgent);
			// 建立实际的连接
			URLConnection.connect();
			// 消息发送回调
			netEventHandle.onSend();
			// 定义byteArrayOutputStream字节数组输出流以获取响应内容
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			// byteArray用于存放循环读取的临时数据
			byte[] tempByteArray = new byte[receiveCachePart];
			int count;
			// 循环读取内容
			while ((count = URLConnection.getInputStream().read(tempByteArray, 0, receiveCachePart)) > 0) {
				byteArrayOutputStream.write(tempByteArray, 0, count);
			}
			// 判断是否在超时后接收到消息
			if (canReceiveBoolean) {
				// 取消超时回调
				timer.cancel();
				receiveHeader = URLConnection.getHeaderFields();
				receiveMessage = byteArrayOutputStream.toByteArray();
				handler.obtainMessage(1).sendToTarget();
			}
		} catch (Exception e) { // 失败回调错误码
			e.printStackTrace();
			netEventHandle.error(NetHelper.sendOrReceiveGetMessageError);
		}
	}
}
