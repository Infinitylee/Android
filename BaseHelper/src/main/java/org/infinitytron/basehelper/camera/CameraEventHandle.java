/**
 * @fileName CameraEventHandle
 * @describe 摄像头事件接口
 * @author 李培铭
 * @time 2017-08-10
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.basehelper.camera;

public interface CameraEventHandle {

	void captureStillPictureResult(byte[] imageByteArray);
	void setUpCameraOutputsError();
	void cameraAccessExceptionError();
	void onConfigureFailedError();
	void createCameraPreviewSessionError();

}
