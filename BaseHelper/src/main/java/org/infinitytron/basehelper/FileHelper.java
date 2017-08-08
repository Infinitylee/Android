/**
 * @fileName FileHelper
 * @describe 文件助理类
 * @author 李培铭
 * @time 2017-07-25
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.basehelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FileHelper {

	private static FileHelper fileHelper;

	/**
	 * 静态实例化引导
	 * @return fileHelper
	 */
	public static FileHelper getInstance() {
		if (fileHelper == null) {
			fileHelper = new FileHelper();
		}
		return fileHelper;
	}

	/**
	 * 判断是否有sdcard
	 * @return Boolean
	 */
	public boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * 获取储存路径
	 * @param activity 活动
	 * @return 路径
	 */
	public String getSDCardPath(Activity activity) {
		String sdCardPathString;
		if (hasSDCard()) { // 如果存在sd卡,则返回sd卡路径
			sdCardPathString = Environment.getExternalStorageDirectory().getPath();
		} else { // 如果不存在sd卡,则返回/data/data/<application package>/files路径
			sdCardPathString = activity.getFilesDir().toString();
		}
		return sdCardPathString;
	}

	/**
	 * 在sd卡创建文件夹
	 * @param folderName 文件名(example:/Android/data/PthyemLeeHelper/)
	 * @return Boolean
	 */
	public Boolean createSDCardDir(Activity activity, String folderName) {
		if(hasSDCard()) {
			// 得到一个路径,内容是sdcard的文件夹路径和名字
			try {
				String pathString = getSDCardPath(activity) + folderName;
				File folder = new File(pathString);
				folder.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pathString = getSDCardPath(activity) + folderName;
			File folder = new File(pathString);
			// 若不存在,创建目录
			return !folder.exists() && folder.mkdirs();
		}
		return false;
	}

	/**
	 * 在sd卡删除文件夹
	 * @param folderName 文件名(example:/Android/data/PthyemLeeHelper/)(ps:此方法只适用末级文件夹遍历删除)
	 * @return Boolean
	 */
	public Boolean delectSDCardDir(Activity activity, String folderName) {
		if(hasSDCard()) {
			//得到一个路径，内容是sdcard的文件夹路径和名字
			String pathString = getSDCardPath(activity) + folderName;
			File folder = new File(pathString);
			if(folder.isDirectory()){
				File[] childFiles = folder.listFiles();
				if (childFiles != null) {
					for (File file : childFiles) {
						if (!file.delete()) {
							break;
						}
					}
				}
				return folder.delete();
			}
			return false;
		}
		return false;
	}

	/**
	 * 在sd卡删除文件
	 * @param fileName 文件名(example:/Android/data/PthyemLeeHelper/PthyemLee.png)
	 * @return Boolean
	 */
	public Boolean delectFile(Activity activity, String fileName) {
		if(hasSDCard()) {
			//得到一个路径,内容是sdcard的文件路径和名字
			String filePathString = getSDCardPath(activity) + fileName;
			File file = new File(filePathString);
			return file.exists() && file.delete();
		}
		return false;
	}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径(sd卡相对路径example:/Android/data/PthyemLeeOld.png)
	 * @param newPath String 复制后路径(sd卡相对路径example:/Android/data/PthyemLeeNew.png)
	 */
	public boolean copyFile(String oldPath, String newPath) {
		try {
			int byteRead;
			File oldFile = new File(oldPath);
			if (oldFile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream outStream = new FileOutputStream(newPath); // 预载新文件
				byte[] buffer = new byte[1024];
				while ((byteRead = inStream.read(buffer)) > 0) {
					outStream.write(buffer, 0, byteRead);
				}
				inStream.close();
				outStream.close();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 储存用户图片
	 * @param bitmap 位图文件
	 * @param quality 图片质量
	 * @param savePath 文件路径(example:/Android/data/PthyemLeeHelper/)
	 * @param fileName 文件名(example:PthyemLee.png)
	 */
	public boolean saveImage(Activity activity, Bitmap bitmap, int quality, String savePath, String fileName) {
		String folderPath = savePath;
		savePath = getSDCardPath(activity) + savePath;
		File file = new File(savePath + fileName);
		if (!file.exists()) {
			createSDCardDir(activity, folderPath);
		}
		try {
			if (file.createNewFile()) {
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
				fos.flush();
				fos.close();
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 从文件中获取图片
	 * @param activity 活动
	 * @param savePath 图片路径(example:/Android/data/PthyemLeeHelper/)
	 * @param imageName 图片名字(example:PthyemLee.png)
	 * @return Bitmap
	 */
	public Bitmap getBitmapFromFile(Activity activity, String savePath, String imageName){
		Bitmap bitmap = null;
		if (imageName != null) {
			try {
				String realPath = getSDCardPath(activity) + savePath;
				File file = new File(realPath, imageName);
				if (file.exists()) {
					bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
				}
			} catch (Exception e) {
				bitmap = null;
			}
		}
		return bitmap;
	}

	/**
	 * 删除图片
	 * @param activity 活动
	 * @param savePath 图片路径(example:/Android/data/PthyemLeeHelper/)
	 * @param imageName 图片名(example:PthyemLee.png)
	 */
	public boolean removeBitmapFromFile(Activity activity, String savePath, String imageName){
		try {
			if (hasSDCard()) {
				savePath = getSDCardPath(activity) + savePath;
				File file = new File(savePath, imageName);
				return file.delete();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取文件夹大小
	 * @param activity 活动
	 * @param folderName 文件夹路径(example:/Android/data/PthyemLeeHelper)
	 * @param format 返回格式(example:"00.00")
	 * @return double
	 */
	public double getFolderSize(Activity activity, String folderName, String format) {
		double folderSizeDouble = Double.valueOf(getFormatSizeMb(doGetFolderSize(activity, folderName)));
		NumberFormat numberFormat = new DecimalFormat(format);
		return Double.parseDouble(numberFormat.format(folderSizeDouble));
	}

	/**
	 * 获取文件夹容量大小
	 * @param  folderName 文件夹路径(example:/Android/data/PthyemLeeHelper)
	 * @return size/1048576 文件大小,失败则返回0
	 */
	public long doGetFolderSize(Activity activity, String folderName) {
		long size = 0;
		File file = new File(getSDCardPath(activity) + folderName);
		if (file.exists()) {
			try {
				java.io.File[] fileList = file.listFiles();
				for (File files : fileList) {
					if (files.isDirectory()) {
						size = size + doGetFolderSize(activity, folderName);
					} else {
						size = size + files.length();
					}
				}
				return size;
			} catch (Exception e) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 格式化单位(自动识别)
	 * @param size 容量大小
	 * @return String容量
	 */
	public String getFormatSizeAuto(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte(s)";
		}
		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}
		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2  = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}
		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

	/**
	 * 格式化单位(KB)
	 * @param size 容量大小
	 * @return String 容量大小Kb
	 */
	public String getFormatSizeKb(double size) {
		double kiloByte = size / 1024;
		BigDecimal result  = new BigDecimal(Double.toString(kiloByte));
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	/**
	 * 格式化单位(MB)
	 * @param size 容量大小
	 * @return String 容量大小MB
	 */
	public String getFormatSizeMb(double size) {
		double kiloByte = size / 1024;
		double megaByte = kiloByte / 1024;
		BigDecimal result  = new BigDecimal(Double.toString(megaByte));
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	/**
	 * 格式化单位(GB)
	 * @param size 容量大小
	 * @return String 容量大小GB
	 */
	public String getFormatSizeGb(double size) {
		double kiloByte = size / 1024;
		double megaByte = kiloByte / 1024;
		double gigaByte = megaByte / 1024;
		BigDecimal result  = new BigDecimal(Double.toString(gigaByte));
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	/**
	 * 格式化单位(TB)
	 * @param size 容量大小
	 * @return String 容量大小TB
	 */
	public String getFormatSizeTb(double size) {
		double kiloByte = size / 1024;
		double megaByte = kiloByte / 1024;
		double gigaByte = megaByte / 1024;
		double teraBytes = gigaByte / 1024;
		BigDecimal result  = new BigDecimal(Double.toString(teraBytes));
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
	}
}
