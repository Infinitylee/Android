/**
 * @fileName PostHelper
 * @describe TCP通信服务Http中post助理类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper.network.tcp.http.post;

import android.os.Handler;
import android.os.Message;

import org.infinitytron.nethelper.NetHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class PostHelper extends PostEntity implements Runnable {

	private Boolean canReceiveBoolean = true;
	private String sendMessageString = "";

	private String boundaryString = UUID.randomUUID().toString().replace("-", "");

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
	 * post消息线程
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
		// 输出流(要提交表单内容)
		DataOutputStream out = null;
		try {
			URL realUrl = new URL(sendAddress);
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
			switch (sendMethod) {
				case "x-www-form-urlencoded":
					URLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
					break;
				case "json":
					URLConnection.setRequestProperty("Content-type", "application/json;charset=utf-8");
					break;
				case "form-data":
					URLConnection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundaryString);
					break;
				default:
					break;
			}
			// 发送POST请求打开输入输出流,并设置无缓存
			URLConnection.setDoOutput(true);
			URLConnection.setDoInput(true);
			URLConnection.setUseCaches(false);
			// 获取URLConnection对象对应的输出流以提交表单内容
			out = new DataOutputStream(URLConnection.getOutputStream());
			// 处理表单
			switch (sendMethod) {
				// (1)形式: keyOn=valueOn&keyTw=valueTw&keyTh=valueTh
				case "x-www-form-urlencoded":
					if (sendTextMap.size() != 0) {
						for (Map.Entry<String, String> entry : sendTextMap.entrySet()) {
							sendMessageString += entry.getKey() + "=" + entry.getValue() + "&";
						}
						// 去掉字符串最后一个字符
						sendMessageString = sendMessageString.substring(0, sendMessageString.length() - 1);
					} else {
						sendMessageString = "";
					}
					// 发送请求参数
					out.write(sendMessageString.getBytes());
					break;
				// (2)形式: {"keyOn":"valueOn","keyTw":"valueTw","keyTh":"valueTh"}
				case "json":
					if (sendTextMap.size() != 0) {
						JSONObject sendMessageJSONObject = new JSONObject();
						for (Map.Entry<String, String> entry : sendTextMap.entrySet()) {
							try {
								sendMessageJSONObject.put(entry.getKey(), entry.getValue());
							} catch (JSONException e) { // 失败回调错误码
								netEventHandle.error(NetHelper.putJSONDataError);
							}
						}
						// 生成字符串
						sendMessageString = sendMessageJSONObject.toString();
					} else {
						sendMessageString = "{}";
					}
					// 发送请求参数
					out.write(sendMessageString.getBytes());
					break;
				// (3)形式: --UUID32UUID32UUID32UUID32UUID32UUID32\r\n
				//         Content-Disposition: form-data; name="keyOn"\r\n
				//         \r\n
				//         valueOn\r\n
				//         --UUID32UUID32UUID32UUID32UUID32UUID32\r\n
				//         Content-Disposition: form-data; name="keyTw"; filename="fileName.xxx"\r\n
				//         \r\n
				//         bytebytebytebytebytebytebytebytebytebytebytebytebyte...\r\n
				//         --UUID32UUID32UUID32UUID32UUID32UUID32--\r\n
				case "form-data":
					// 初始化变量
					String endString = "\r\n";
					String twoHyphensString = "--";
					// 写入标题
					out.write((twoHyphensString + boundaryString + endString).getBytes());
					out.write(("Content-Disposition: form-data; name= \"title\"" + endString).getBytes());
					out.write(endString.getBytes());
					out.write(("NetHelper By Pthyem Lee" + endString).getBytes());
					// 处理文本消息内容
					for (Map.Entry<String, String> entry : sendTextMap.entrySet()) {
						out.write((twoHyphensString + boundaryString + endString).getBytes());
						out.write(("Content-Disposition:form-data;name=\"" + entry.getKey() + "\"" + endString).getBytes());
						out.write(endString.getBytes());
						out.write((entry.getValue() + endString).getBytes());
					}
					// 处理文件消息内容
					for (Map.Entry<String, File> entry : sendFileMap.entrySet()) {
						// 编写内容
						out.write((twoHyphensString + boundaryString + endString).getBytes());
						out.write(("Content-Disposition:form-data;name=\"" + entry.getKey() + "\";filename=\"" + entry.getValue().getName() + "\"" + endString).getBytes());
						out.write((endString).getBytes());
						// 预处理,将File文件内容读取并以字节数组记录
						try {
							// 读取文件内容到文件输入流中
							FileInputStream fileInputStream = new FileInputStream(entry.getValue());
							// 实例化字节数组输出流
							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
							byte[] byteArray = new byte[1024];
							for (int length; (length = fileInputStream.read(byteArray)) != -1;) {
								byteArrayOutputStream.write(byteArray, 0, length);
							}
							fileInputStream.close();
							byteArrayOutputStream.close();
							out.write(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length);
							out.write(endString.getBytes());
						} catch (IOException e) { // 失败回调错误码
							netEventHandle.error(NetHelper.sendFileIsNotFound);
						}
					}
					//消息主体最后一行
					out.write((twoHyphensString + boundaryString + twoHyphensString + endString).getBytes());
					break;
				default:
					break;
			}
			// flush输出流的缓冲
			out.flush();
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
			netEventHandle.error(NetHelper.sendOrReceivePostMessageError);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) { // 失败回调错误码
				netEventHandle.error(NetHelper.closeInOrOutBufferedError);
			}
		}
	}
}
