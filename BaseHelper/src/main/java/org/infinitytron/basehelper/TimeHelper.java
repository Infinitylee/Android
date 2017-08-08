/**
 * @fileName TimeHelper
 * @describe 时间助理类
 * @author 李培铭
 * @time 2017-07-25
 * @copyRight ©2017 by InfinityTron.李培铭
 */
package org.infinitytron.basehelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeHelper {

	private Calendar calendar;
	private Date dateNowDate;
	private SimpleDateFormat simpleDateFormat;

	/**
	 * 构造函数,实例化对象
	 */
	@SuppressWarnings("SimpleDateFormat")
	public TimeHelper() {
		// 实例化日历对象
		calendar = Calendar.getInstance(Locale.CHINA);
		// 设置时区
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		// 解决周日会出现 并到下一周的情况
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		// 设置一周的第一天是星期一
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		// 实例化时间格式对象
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	}

	/**
	 * 计算天气时间差
	 * @param beforeTime 第一时间
	 * @param afterTime 第二时间
	 * @return long 时差(天)
	 */
	public long calculateTimeWithDate(String beforeTime, String afterTime) {
		try {
			//格式化当前时间并转化为字符串并转化为时间对象
			Date beforeDate = simpleDateFormat.parse(simpleDateFormat.format(beforeTime));
			Date afterDate = simpleDateFormat.parse(simpleDateFormat.format(beforeTime));
			// 时间相减得到微秒级时间差
			long timeDifferenceLong = afterDate.getTime() - beforeDate.getTime();
			// 得到天数
			return timeDifferenceLong / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			return (long) 0;
		}
	}

	/**
	 * 计算天气时间差
	 * @param beforeTime 第一时间
	 * @param afterTime 第二时间
	 * @return long 时差(时)
	 */
	public long calculateTimeWithHour(String beforeTime, String afterTime) {
		try {
			//格式化当前时间并转化为字符串并转化为时间对象
			Date beforeDate = simpleDateFormat.parse(simpleDateFormat.format(beforeTime));
			Date afterDate = simpleDateFormat.parse(simpleDateFormat.format(beforeTime));
			// 时间相减得到微秒级时间差
			long timeDifferenceLong = afterDate.getTime() - beforeDate.getTime();
			// 得到天数
			long days = timeDifferenceLong / (1000 * 60 * 60 * 24);
			// 得到并返回小时
			return (timeDifferenceLong - days * (1000 * 60 * 60 * 24)) / (1000* 60 * 60);
		} catch (ParseException e) {
			return (long) 0;
		}
	}

	/**
	 * 计算天气时间差
	 * @param beforeTime 第一时间
	 * @param afterTime 第二时间
	 * @return long 时差(分)
	 */
	public Long calculateTimeWithSecond(String beforeTime, String afterTime) {
		try {
			//格式化当前时间并转化为字符串并转化为时间对象
			Date beforeDate = simpleDateFormat.parse(simpleDateFormat.format(beforeTime));
			Date afterDate = simpleDateFormat.parse(simpleDateFormat.format(beforeTime));
			// 时间相减得到微秒级时间差
			long timeDifferenceLong = afterDate.getTime() - beforeDate.getTime();
			// 得到天数
			long days = timeDifferenceLong / (1000 * 60 * 60 * 24);
			// 得到并返回小时
			long hours = (timeDifferenceLong - days * (1000 * 60 * 60 * 24)) / (1000* 60 * 60);
			// 得到分钟
			return (timeDifferenceLong - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
		} catch (ParseException e) {
			return (long) 0;
		}
	}

	/**
	 * 计算周天差
	 * @return int 天数
	 */
	public int caculateDayToMonday() {
		// 获取周几
		int weekDate = calendar.get(Calendar.DAY_OF_WEEK);
		// 计算周天差
		if (weekDate == 1) {
			return 0;
		} else {
			return 1 - weekDate;
		}
	}

	/**
	 * 获取当前年
	 * @return int 年
	 */
	public int getYear() {
		//返回月份
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取当前月份
	 * @return int 月
	 */
	public int getMonth() {
		//返回月份
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前日期
	 * @return int 日期
	 */
	public int getMonthOfDay() {
		//返回月份
		return calendar.get(Calendar.DAY_OF_MONTH) + 1;
	}

	/**
	 * 获取当前周几
	 * @return int 周几
	 */
	public int getWeekOfDay() {
		//返回周几
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取当前小时
	 * @return int 小时
	 */
	public int getHour() {
		//返回小时
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前分
	 * @return int 分
	 */
	public int getMinute() {
		//返回分
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 获取当前秒
	 * @return int 秒
	 */
	public int getSecond() {
		//返回秒
		return calendar.get(Calendar.SECOND);
	}
}
