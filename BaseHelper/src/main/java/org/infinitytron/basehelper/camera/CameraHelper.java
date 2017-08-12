/**
 * @fileName CameraHelper
 * @describe 摄像头助理类
 * @author 李培铭
 * @time 2017-08-10
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.basehelper.camera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraHelper {

	// 活动
	private Activity activity;
	// 摄像头事件回调
	private CameraEventHandle cameraEventHandle;
	// 横竖屏状态存放
	private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
	static {
		ORIENTATIONS.append(Surface.ROTATION_0, 90);
		ORIENTATIONS.append(Surface.ROTATION_90, 0);
		ORIENTATIONS.append(Surface.ROTATION_180, 270);
		ORIENTATIONS.append(Surface.ROTATION_270, 180);
	}
	// 内容流显示控件
	private AutoFitTextureView textureView;
	// 摄像头ID(0为后置摄像头,1为前置摄像头)
	private String cameraId = "0";
	// 摄像头设备对象
	private CameraDevice cameraDevice;
	// 预览尺寸
	private Size previewSize;
	// 预览拍照请求构建器
	private CaptureRequest.Builder captureRequestBuilder;
	// 预览拍照请求
	private CaptureRequest captureRequest;
	// 预览,拍照时,通过该类的实例来创建Session
	private CameraCaptureSession cameraCaptureSession;
	// 照片读取
	private ImageReader imageReader;
	// 显示内容流的控件对象监听器
	private final TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture texture , int width, int height) {
			// 当TextureView可用时,打开摄像头
			openCamera(width, height);
		}
		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture texture , int width, int height) {
			// 确定显示方向
			configureTransform(width, height);
		}
		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) { return true; }
		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture texture){}
	};
	// 摄像头设备对象状态接口回调
	private final CameraDevice.StateCallback stateCallback = new CameraDevice. StateCallback() {
		/**
		 * 摄像头被打开时激发该方法
		 */
		@Override
		public void onOpened(@NonNull CameraDevice cameraDevice) {
			CameraHelper.this.cameraDevice = cameraDevice;
			// 开始预览
			createCameraPreviewSession();
		}
		/**
		 * 摄像头断开连接时激发该方法
		 */
		@Override
		public void onDisconnected(@NonNull CameraDevice cameraDevice) {
			cameraDevice.close();
			CameraHelper.this.cameraDevice = null;
		}
		/**
		 * 打开摄像头出现错误时激发该方法
		 */
		@Override
		public void onError(@NonNull CameraDevice cameraDevice, int error) {
			cameraDevice.close();
			CameraHelper.this.cameraDevice = null;
		}
	};

	public CameraHelper(Activity activity, AutoFitTextureView textureView, CameraEventHandle cameraEventHandle) {
		this.activity = activity;
		this.textureView = textureView;
		this.cameraEventHandle = cameraEventHandle;
	}

	public void actionOpenCamera() {
		if (textureView.isAvailable()) {
			// 开启摄像头
			openCamera(textureView.getWidth(), textureView.getHeight());
		} else {
			// 为该组件设置监听器
			textureView.setSurfaceTextureListener(surfaceTextureListener);
		}
	}

	public void actionCloseCamera() {
		// 关闭摄像头
		closeCamera();
	}

	public void actionCaptureStillPicture() {
		// 拍照
		captureStillPicture();
	}

	/**
	 * 打开摄像头
	 * @param width 预览控件宽度
	 * @param height 预览控件高度
	 */
	private void openCamera(int width, int height) {
		// 设置摄像头输出
		setUpCameraOutputs(width, height);
		// 确定显示方向(解决onPause后方向显示不正确问题)
		configureTransform(textureView.getWidth(), textureView.getHeight());
		// 获取摄像头管理器
		CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
		try {
			// 打开摄像头
			manager.openCamera(cameraId, stateCallback, null);
		} catch (CameraAccessException | SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭摄像头
	 */
	private void closeCamera() {
		if (cameraCaptureSession != null) {
			cameraCaptureSession.close();
			cameraCaptureSession = null;
		}
		if (cameraDevice != null) {
			cameraDevice.close();
			cameraDevice = null;
		}
		if (imageReader != null) {
			imageReader.close();
			imageReader = null;
		}
	}

	/**
	 * 设置摄像头输出
	 * @param width 预览控件宽度
	 * @param height 预览控件高度
	 */
	private void setUpCameraOutputs(int width, int height) {
		// 获取摄像头管理器
		CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
		try {
			// 获取指定摄像头的特性
			CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
			// 获取摄像头支持的配置属性
			StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
			if (map != null) {
				// 获取摄像头支持的最大尺寸
				Size largestSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
				// 创建一个ImageReader对象,用于获取摄像头的图像数据
				imageReader = ImageReader.newInstance(largestSize.getWidth(), largestSize.getHeight(), ImageFormat.JPEG, 1);
				imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
					/**
					 * 当照片数据可用时激发该方法
					 */
					@Override
					public void onImageAvailable(ImageReader reader) {
						// 获取捕获的照片数据
						Image image = reader.acquireNextImage();
						ByteBuffer buffer = image.getPlanes()[0].getBuffer();
						byte[] bytes = new byte[buffer.remaining()];
						buffer.get(bytes);
						// 将图像内容转化为byte数组进行回调
						cameraEventHandle.captureStillPictureResult(bytes);
						image.close();
					}
				}, null);
				// 获取最佳的预览尺寸
				previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, largestSize);
				// 根据选中的预览尺寸来调整内容流显示控件(TextureView)的长宽比
				int orientation = activity.getResources().getConfiguration().orientation;
				Log.e("width", previewSize.getWidth() + "");
				Log.e("height", previewSize.getHeight() + "");
				if (orientation == Configuration.ORIENTATION_LANDSCAPE) { // 如果是竖屏
					textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
				} else { // 横屏
					textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
				}
			}
		} catch (CameraAccessException | NullPointerException e) {
			e.printStackTrace();
			cameraEventHandle.setUpCameraOutputsError();
			Log.e("TAG", "对摄像头输出设置时出现错误");
		}
	}

	/**
	 * 获取最佳的预览尺寸
	 * @param choicesSizeArray 摄像头支持的尺寸列表
	 * @param width 预览控件宽度
	 * @param height 预览控件高度
	 * @param aspectRatioSize 摄像头支持的最大尺寸
	 * @return 最佳的预览尺寸
	 */
	private static Size chooseOptimalSize(Size[] choicesSizeArray , int width, int height, Size aspectRatioSize) {
		// 实例化列表
		List<Size> bigEnoughSizeList = new ArrayList<>();
		int tempWidth = aspectRatioSize.getWidth();
		int tempHeight = aspectRatioSize.getHeight();
		// 遍历收集 大过预览Surface分辨率的摄像头支持分辨率 并存到列表中
		for (Size size : choicesSizeArray) {
			if (size.getHeight() == size.getWidth() * tempHeight / tempWidth && size.getWidth() >= width && size.getHeight() >= height) {
				bigEnoughSizeList.add(size);
			}
		}
		// 如果找到多个预览尺寸,获取其中面积最小的
		if (bigEnoughSizeList.size() > 0) {
			return Collections.min(bigEnoughSizeList, new CompareSizesByArea());
		} else {
			Log.e("TAG", "找不到合适的预览尺寸！！！");
			return choicesSizeArray[0];
		}
	}

	/**
	 * 屏幕旋转后重新设置比例和预览方向
	 * @param viewWidth 控件宽度
	 * @param viewHeight 控件高度
	 */
	private void configureTransform(int viewWidth, int viewHeight) {
		if (textureView == null) {
			return;
		}
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		Matrix matrix = new Matrix();
		RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
		RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
		float centerX = viewRect.centerX();
		float centerY = viewRect.centerY();
		if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
			bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
			matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
			float scale = Math.max((float) viewHeight / previewSize.getHeight(), (float) viewWidth / previewSize.getWidth());
			matrix.postScale(scale, scale, centerX, centerY);
			matrix.postRotate(90 * (rotation - 2), centerX, centerY);
		} else if (Surface.ROTATION_180 == rotation) {
			matrix.postRotate(180, centerX, centerY);
		}
		textureView.setTransform(matrix);
	}

	/**
	 * 开始预览
	 */
	private void createCameraPreviewSession() {
		try {
			// 获取内容流显示控件的隐性显示对象
			SurfaceTexture texture = textureView.getSurfaceTexture();
			// 声明对象不为空
			assert texture != null;
			// 设置隐性显示对象的宽度和高度
			texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
			// 实例化显性显示对象
			Surface surface = new Surface(texture);
			// 创建作为预览的 预览拍照请求构建器
			captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			// 将 内容流显示控件 的surface作为预览拍照请求构建器的目标(目的是把图片流输出到内容流显示控件中,与拍照输出到imageReader的Surface相反)
			captureRequestBuilder.addTarget(surface);
			// 创建CameraCaptureSession,该对象负责管理处理预览请求和拍照请求
			cameraDevice.createCaptureSession(Arrays.asList(surface , imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
				@Override
				public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
					// 如果摄像头为null,直接结束方法
					if (null == cameraDevice) {
						return;
					}
					// 当摄像头已经准备好时,开始显示预览
					CameraHelper.this.cameraCaptureSession = cameraCaptureSession;
					try {
						// 设置自动对焦模式
						captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
						// 设置自动曝光模式
						captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
						// 由请求构建器生成请求对象
						captureRequest = captureRequestBuilder.build();
						// 设置预览时连续捕获图像数据,调用setRepeatingRequest函数开始显示相机预览
						CameraHelper.this.cameraCaptureSession.setRepeatingRequest(captureRequest, null, null);
					} catch (CameraAccessException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
					cameraEventHandle.onConfigureFailedError();
					Log.e("TAG", "开始预览时配置失败");
				}
			}, null);
		} catch (CameraAccessException e) {
			e.printStackTrace();
			cameraEventHandle.createCameraPreviewSessionError();
			Log.e("TAG", "预览时出现错误");
		}
	}

	/**
	 * 进行拍照
	 */
	private void captureStillPicture() {
		try {
			if (cameraDevice == null) {
				return;
			}
			// 创建作为拍照的 预览拍照请求构建器
			final CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
			// 将imageReader的surface作为 预览拍照请求构建器 的目标
			captureRequestBuilder.addTarget(imageReader.getSurface());
			// 设置自动对焦模式
			captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
			// 设置自动曝光模式
			captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
			// 获取设备方向
			int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
			// 根据设备方向计算设置照片的方向
			captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION , ORIENTATIONS.get(rotation));
			// 停止连续取景(预览)
			cameraCaptureSession.stopRepeating();
			// 捕获静态图像(拍照)
			cameraCaptureSession.capture(captureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
				// 拍照完成时激发该方法
				@Override
				public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
					super.onCaptureCompleted(session, request, result);
					try {
						// 重设自动对焦模式
						CameraHelper.this.captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
						// 设置自动曝光模式
						CameraHelper.this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
						// 由请求构建器生成请求对象
						captureRequest = CameraHelper.this.captureRequestBuilder.build();
						// 调用setRepeatingRequest函数开始显示相机预览,即打开连续取景模式
						cameraCaptureSession.setRepeatingRequest(captureRequest, null, null);
					} catch (CameraAccessException e) {
						e.printStackTrace();
					}
				}
			}, null);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 为Size定义一个比较器Comparator
	 */
	private static class CompareSizesByArea implements Comparator<Size> {
		@Override
		public int compare(Size lhs, Size rhs) {
			// 强转为long保证不会发生溢出
			return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
		}
	}
}
