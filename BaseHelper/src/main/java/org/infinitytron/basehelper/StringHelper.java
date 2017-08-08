/**
 * @fileName StringHelper
 * @describe 字符串助理类
 * @author 李培铭
 * @time 2017-07-25
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.basehelper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringHelper {

	private static StringHelper stringHelper;

	/**
	 * 静态实例化引导
	 * @return stringHelper
	 */
	public static StringHelper getInstance() {
		if (stringHelper == null) {
			stringHelper = new StringHelper();
		}
		return stringHelper;
	}

	/**
	 * 从字符串中取得图片名
	 * @param url 地址
	 * @return 图片名
	 */
	public String getImageNameByUrl(String url) {
		String imageNameString = "";
		if (url != null ) {
			imageNameString = url.substring(url.lastIndexOf("/") + 1);
		}
		return imageNameString;
	}

	/**
	 * 字符串是否以某字符串开头
	 * @param haystack 干草堆
	 * @param needle 针
	 * @return Boolean
	 */
	public Boolean isStareWithNeedle(String haystack, String needle) {
		return haystack.startsWith(needle);
	}

	/**
	 * 字符串是否以某字符串结尾
	 * @param haystack 干草堆
	 * @param needle 针
	 * @return Boolean
	 */
	public Boolean isEndWithNeedle(String haystack, String needle) {
		return haystack.endsWith(needle);
	}

	/**
	 * 字符串是否含有中文符
	 * @param haystack 干草堆
	 * @return Boolean
	 */
	public boolean isContainChinese(String haystack) {
		char [] chr = haystack.toCharArray();
		for(int i = 0; i < haystack.length(); i++) {
			if(checkChinese(chr[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean checkChinese(char chr) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(chr);
		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
					|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION // GENERAL_PUNCTUATION 判断中文的“号
					|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
					|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS; // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
	}

	/**
	 * 判断是否含有指定字符串
	 * @param haystack 干草堆
	 * @param needle 针
	 * @return boolean
	 */
	public boolean isContainNeedle(String haystack, String needle) {
		return haystack.contains(needle);
	}

	/**
	 * 去掉字符串后几位
	 * @param haystack 干草堆
	 * @param index 索引
	 * @return String
	 */
	public String delectIndexOfString(String haystack, int index) {
		return haystack.substring(index, haystack.length());
	}

	/**
	 * 返回字符串第一次出现指定字符的索引
	 * @param  haystack 干草堆
	 * @param  needle 针
	 * @return int
	 */
	public int startIndexOfNeedle(String haystack, String needle) {
		return haystack.indexOf(needle);
	}

	/**
	 * 代替字符
	 * @param  haystack 干草堆
	 * @param  needle 针
	 * @param  replace 替换
	 * @return String 字符串
	 */
	public String replaceString(String haystack, String needle, String replace) {
		return haystack.replace(needle, replace);
	}

	/**
	 * 字符串转md5
	 * @param url 请求地址
	 * @return md5
	 */
	public String md5(String url) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(url.getBytes("UTF-8"));
			byte[] encryption = md5.digest();
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					stringBuilder.append("0").append(Integer.toHexString(0xff & encryption[i]));
				} else {
					stringBuilder.append(Integer.toHexString(0xff & encryption[i]));
				}
			}
			return stringBuilder.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
