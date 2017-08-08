/**
 * @fileName TcpEntity
 * @describe TCP通信服务工具类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper.network.tcp;

import org.infinitytron.nethelper.NetEventHandle;

public class TcpEntity {

	String sendAddress = "192.168.1.147"; // 发送的地址
	Integer sendPort = 1900; // 发送的端口
	byte[] sendMessage; // 要发送的消息的字节数组

	Integer receiveTimeout = 10000; // 发送后无响应超时时间
	Integer receiveCachePart = 1024; // 接受数据缓存大小

	NetEventHandle netEventHandle; // 事件处理接口对象

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public Integer getSendPort() {
		return sendPort;
	}

	public void setSendPort(Integer sendPort) {
		this.sendPort = sendPort;
	}

	public byte[] getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(byte[] sendMessage) {
		this.sendMessage = sendMessage;
	}

	public Integer getReceiveTimeout() {
		return receiveTimeout;
	}

	public void setReceiveTimeout(Integer receiveTimeout) {
		this.receiveTimeout = receiveTimeout;
	}

	public Integer getReceiveCachePart() {
		return receiveCachePart;
	}

	public void setReceiveCachePart(Integer receiveCachePart) {
		this.receiveCachePart = receiveCachePart;
	}

	public NetEventHandle getNetEventHandle() {
		return netEventHandle;
	}

	public void setNetEventHandle(NetEventHandle netEventHandle) {
		this.netEventHandle = netEventHandle;
	}
}
