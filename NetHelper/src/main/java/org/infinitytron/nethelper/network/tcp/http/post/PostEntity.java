/**
 * @fileName PostEntity
 * @describe TCP通信服务Http中post工具类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper.network.tcp.http.post;

import org.infinitytron.nethelper.NetEventHandle;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PostEntity {

    String sendAddress = "192.168.1.147"; // 发送的地址
    String sendMethod = "x-www-form-urlencoded"; // 发送方法(支持方法: application/x-www-form-urlencoded, application/json, multipart/form-data)

    String sendHeaderAccept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    String sendHeaderAcceptEncoding = "gzip, deflate";
    String sendHeaderAcceptLanguage = "zh-CN,zh;q=0.8,zh-TW;q=0.6,en;q=0.4";
    String sendHeaderConnection = "keep-alive";
    String sendHeaderReferer = "";
    String sendHeaderHost = "";
    String sendHeaderUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
    Map<String, String> sendHeaderCookieMap = new HashMap<>(); // 发送cookie消息集合
    Map<String, String> sendTextMap = new HashMap<>(); // 发送文字消息集合
    Map<String, File> sendFileMap = new HashMap<>(); // 发送文件消息集合

    Integer receiveTimeout = 10000; // 发送后无响应超时时间
    Integer receiveCachePart = 1024; // 接受数据缓存大小

    NetEventHandle netEventHandle; // 事件处理接口对象

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendMethod() {
        return sendMethod;
    }

    public void setSendMethod(String sendMethod) {
        this.sendMethod = sendMethod;
    }

    public String getSendHeaderAccept() {
        return sendHeaderAccept;
    }

    public void setSendHeaderAccept(String sendHeaderAccept) {
        this.sendHeaderAccept = sendHeaderAccept;
    }

    public String getSendHeaderAcceptEncoding() {
        return sendHeaderAcceptEncoding;
    }

    public void setSendHeaderAcceptEncoding(String sendHeaderAcceptEncoding) {
        this.sendHeaderAcceptEncoding = sendHeaderAcceptEncoding;
    }

    public String getSendHeaderAcceptLanguage() {
        return sendHeaderAcceptLanguage;
    }

    public void setSendHeaderAcceptLanguage(String sendHeaderAcceptLanguage) {
        this.sendHeaderAcceptLanguage = sendHeaderAcceptLanguage;
    }

    public String getSendHeaderConnection() {
        return sendHeaderConnection;
    }

    public void setSendHeaderConnection(String sendHeaderConnection) {
        this.sendHeaderConnection = sendHeaderConnection;
    }

    public String getSendHeaderReferer() {
        return sendHeaderReferer;
    }

    public void setSendHeaderReferer(String sendHeaderReferer) {
        this.sendHeaderReferer = sendHeaderReferer;
    }

    public String getSendHeaderHost() {
        return sendHeaderHost;
    }

    public void setSendHeaderHost(String sendHeaderHost) {
        this.sendHeaderHost = sendHeaderHost;
    }

    public String getSendHeaderUserAgent() {
        return sendHeaderUserAgent;
    }

    public void setSendHeaderUserAgent(String sendHeaderUserAgent) {
        this.sendHeaderUserAgent = sendHeaderUserAgent;
    }

    public Map<String, String> getSendHeaderCookieMap() {
        return sendHeaderCookieMap;
    }

    public void setSendHeaderCookieMap(Map<String, String> sendHeaderCookieMap) {
        this.sendHeaderCookieMap = sendHeaderCookieMap;
    }

    public Map<String, String> getSendTextMap() {
        return sendTextMap;
    }

    public void setSendTextMap(Map<String, String> sendTextMap) {
        this.sendTextMap = sendTextMap;
    }

    public Map<String, File> getSendFileMap() {
        return sendFileMap;
    }

    public void setSendFileMap(Map<String, File> sendFileMap) {
        this.sendFileMap = sendFileMap;
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
