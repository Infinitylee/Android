/**
 * @fileName NetEventHandle
 * @describe 网络请求时间接口
 * @author 李培铭
 * @time 2017-07-15
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.nethelper;

import java.util.List;
import java.util.Map;

public interface NetEventHandle {
	void onSend();
	void onReceive(Map<String, List<String>> receiveHeader, byte[] receiveMessage);
	void onTimeout();
	void error(Integer errorCode);
}
