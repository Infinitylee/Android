/**
 * @fileName NetEntity
 * @describe 网络请求工具类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper;

public class NetEntity {

    public static String TAG = "NetHelper";

    public static int newSocketError = 1; // 实例化Socket失败
    public static int getIpAddressError = 2; // 获取ip地址失败
    public static int sendFileIsNotFound = 3; // 要发送的内容为空
    public static int sendOrReceiveUdpMessageError = 4; // 发送Udp消息失败
    public static int sendOrReceiveGetMessageError = 5; // 发送或接收Post消息失败
    public static int sendOrReceivePostMessageError = 6; // 发送或接收Post消息失败
    public static int closeInOrOutBufferedError = 7; // 关闭输出流或输入流失败
    public static int putJSONDataError = 8; // 把要发送的美容存入JSON对象失败
}
