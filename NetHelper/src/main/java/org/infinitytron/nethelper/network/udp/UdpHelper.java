/**
 * @fileName UdpHelper
 * @describe UDP通信服务助理类
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper.network.udp;

import android.os.Handler;
import android.os.Message;

import org.infinitytron.nethelper.NetHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class UdpHelper extends UdpEntity implements Runnable {

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
            // 实例化udpScoket
            DatagramSocket datagramSocket = new DatagramSocket();
            // 发送地址
            InetAddress local = null;
            try {
                // 获取地址
                local = InetAddress.getByName(sendAddress);
            } catch (UnknownHostException e) { // 失败回调错误码
                netEventHandle.error(NetHelper.getIpAddressError);
            }
            // 发送数据
            try {
                // 获取字节数组
                byte[] messageByteArray = sendMessage.getBytes();
                // 获取消息长度
                int msgLengthInt = sendMessage.length();
                // 打包消息
                DatagramPacket datagramPacket = new DatagramPacket(messageByteArray, msgLengthInt, local, sendPort);
                // 发送消息
                datagramSocket.send(datagramPacket);
                // 消息发送回调
                netEventHandle.onSend();
                // 实例化字节数组
                byte[] byteArray = new byte[receiveCachePart];
                // 以指定字节数组创建准备接收数据的DatagramPacket对象
                DatagramPacket inPacket = new DatagramPacket(byteArray, byteArray.length);
                // 读取Socket中的数据，读到的数据放入inPacket封装的数组里
                datagramSocket.receive(inPacket);
                // 判断inPacket.getData()和inBuff是否是同一个数组
                //System.out.println(inBuffer == inPacket.getData());
                // 发送消息到主线程中
                receiveMessage = byteArray;
                if (canReceiveBoolean) {
                    // 取消超时回调
                    timer.cancel();
                    handler.obtainMessage(1).sendToTarget();
                }
                // 关闭连接
                datagramSocket.close();
            } catch (IOException e) {
                netEventHandle.error(NetHelper.sendOrReceiveUdpMessageError);
            }
        } catch (SocketException e) {
            netEventHandle.error(NetHelper.newSocketError);
        }
    }
}
