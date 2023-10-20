/**
 * 
 */
package com.base.sbc.config.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author 
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 秒
	 */
	public static final String SECONDS = "SECONDS";
	/**
	 * 分钟
	 */
	public static final String MINUTES = "MINUTES";
	/**
	 * 小时
	 */
	public static final String HOURS  = "HOURS";
	/**
	 * 天
	 */
	public static final String DAYS  =  "DAYS";
	/** 一星期的天数 */
	public static final int WEEK_DAYS = 7;
	/** 一年的月份数 */
	public static final int YEAR_MONTHS = 12;
	/** 一天的小时数 */
	public static final int DAY_HOURS = 24;
	/** 一小时分钟数 */
	public static final int HOUR_MINUTES = 60;
	/** 一天分钟数 (24 * 60) */
	public static final int DAY_MINUTES = 1440;
	/** 一分钟的秒数 */
	public static final int MINUTE_SECONDS = 60;
	/** 一个小时的秒数 (60 * 60) */
	public static final int HOUR_SECONDS = 3600;
	/** 一天的秒数 (24 * 60 * 60) */
	public static final int DAY_SECONDS = 86400;
	/** 一秒的毫秒数 */
	public static final long SECOND_MILLISECONDS = 1000L;
	/** 一分钟的毫秒数（60 * 1000） */
	public static final long MINUTE_MILLISECONDS = 60000L;
	/** 一小时的毫秒数（60 * 60 * 1000） */
	public static final long HOUR_MILLISECONDS = 3600000L;
	/** 一天的毫秒数（24 * 60* 60* 1000） */
	public static final long DAY_MILLISECONDS = 86400000L;
	/** 星期一 */
	public static final int WEEK_1_MONDAY = 1;
	/** 星期二 */
	public static final int WEEK_2_TUESDAY = 2;
	/** 星期三 */
	public static final int WEEK_3_WEDNESDAY = 3;
	/** 星期四 */
	public static final int WEEK_4_THURSDAY = 4;
	/** 星期五 */
	public static final int WEEK_5_FRIDAY = 5;
	/** 星期六 */
	public static final int WEEK_6_SATURDAY = 6;
	/** 星期天 */
	public static final int WEEK_7_SUNDAY = 7;
	/** 一月 */
	public static final int MONTH_1_JANUARY = 1;
	/** 二月 */
	public static final int MONTH_2_FEBRUARY = 2;
	/** 三月 */
	public static final int MONTH_3_MARCH = 3;
	/** 四月 */
	public static final int MONTH_4_APRIL= 4;
	/** 五月 */
	public static final int MONTH_5_MAY = 5;
	/** 六月 */
	public static final int MONTH_6_JUNE = 6;
	/** 七月 */
	public static final int MONTH_7_JULY = 7;
	/** 八月 */
	public static final int MONTH_8_AUGUST = 8;
	/** 九月 */
	public static final int MONTH_9_SEPTEMBER = 9;
	/** 十月 */
	public static final int MONTH_10_OCTOBER = 10;
	/** 十一月 */
	public static final int MONTH_11_NOVEMBER = 11;
	/** 十二月 */
	public static final int MONTH_12_DECEMBER= 12;
	/** 显示到日期 */
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	/** 显示到小时 */
	public static final String FORMAT_HOUR = "yyyy-MM-dd HH";
	/** 显示到分 */
	public static final String FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
	/** 显示到秒 */
	public static final String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
	/** 显示到毫秒 */
	public static final String FORMAT_MILLISECOND = "yyyy-MM-dd HH:mm:ss:SSS";
	/** 显示到日期（数字格式） */
	public static final String FORMAT_NO_DATE = "yyyyMMdd";
	/** 显示到小时（数字格式） */
	public static final String FORMAT_NO_HOUR = "yyyyMMddHH";
	/** 显示到分（数字格式） */
	public static final String FORMAT_NO_MINUTE = "yyyyMMddHHmm";
	/** 显示到秒（数字格式） */
	public static final String FORMAT_NO_SECOND = "yyyyMMddHHmmss";
	/** 显示到毫秒（数字格式） */
	public static final String FORMAT_NO_MILLISECOND = "yyyyMMddHHmmssSSS";
	/** 时间格式化器集合 */
	private static final Map<String, SimpleDateFormat> STRING_SIMPLE_DATE_FORMAT_HASH_MAP = new HashMap<>();

	static {
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_DATE, new SimpleDateFormat(FORMAT_DATE));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_HOUR, new SimpleDateFormat(FORMAT_HOUR));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_MINUTE, new SimpleDateFormat(FORMAT_MINUTE));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_SECOND, new SimpleDateFormat(FORMAT_SECOND));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_MILLISECOND, new SimpleDateFormat(FORMAT_MILLISECOND));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_NO_DATE, new SimpleDateFormat(FORMAT_NO_DATE));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_NO_HOUR, new SimpleDateFormat(FORMAT_NO_HOUR));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_NO_MINUTE, new SimpleDateFormat(FORMAT_NO_MINUTE));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_NO_SECOND, new SimpleDateFormat(FORMAT_NO_SECOND));
		STRING_SIMPLE_DATE_FORMAT_HASH_MAP.put(FORMAT_NO_MILLISECOND, new SimpleDateFormat(FORMAT_NO_MILLISECOND));
	}

	/**
	 * 计算缓存过期时间
	 * @param dateString 设置缓存的时间
	 * @param dateType 缓存时间类型
	 * @param timeOut 缓存时间
	 * @return 缓存过期时间
	 */
	public static Date getDateByDateType(String dateString, String timeOut, String dateType){
		// 1、缓存过期时间转换
		Date date = parse(dateString, DateUtils.FORMAT_SECOND);
		// 2、时间计算类型 默认 秒
		long calculationType = SECOND_MILLISECONDS;
		// 3、根据缓存时间类型
		switch (dateType){
			// 分
			case MINUTES:
				calculationType =  MINUTE_MILLISECONDS;
				break;
			// 小时
			case HOURS:
				calculationType =  HOUR_MILLISECONDS;
				break;
			// 天
			case DAYS:
				calculationType =  DAY_MILLISECONDS;
				break;
			// 默认秒
			default:
                break;
		}
		// 4、拿到设置缓存时间和缓存时间做计算，得到缓存过期时间
		if (date != null) {
			date.setTime(date.getTime() + (Long.parseLong(timeOut) * calculationType));
		}
		return date;
	}
	/**
	 * 获取指定时间格式化器
	 *
	 * @param formatStyle 时间格式
	 * @return 时间格式化器
	 */
	private static SimpleDateFormat getSimpleDateFormat(String formatStyle) {
		SimpleDateFormat dateFormat = STRING_SIMPLE_DATE_FORMAT_HASH_MAP.get(formatStyle);
		if (Objects.nonNull(dateFormat)) {
			return dateFormat;
		}
		return new SimpleDateFormat(formatStyle);
	}

	/**
	 * 将字符串格式时间转化为 Date 格式时间
	 *
	 * @param dateString 字符串时间（如：2022-06-17 16:06:17）
	 * @return formatStyle 格式内容
	 * @return Date 格式时间
	 */
	public static Date parse(String dateString, String formatStyle) {
		String s = getString(dateString);
		if (s.isEmpty()) {
			return null;
		}
		try {
			return getSimpleDateFormat(formatStyle).parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取字符串有效内容
	 *
	 * @param s 字符串
	 * @return 有效内容
	 */
	private static String getString(String s) {
		return Objects.isNull(s) ? "" : s.trim();
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/***
	 * 将字符串转为Date
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	 public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/***
	 * 获取过去一年
	 */
	public static synchronized String getYearAgo(Date now) {

		  Calendar c = Calendar.getInstance();
		 //过去一年
		  c.setTime(now);
		  c.add(Calendar.YEAR, -1);
		  Date y = c.getTime();
		  String year = format.format(y);
		  return year;
	}
	
	/***
	 * 获取过去一个月
	 */
	public static synchronized String getMonthAgo(Date now) {

		  Calendar c = Calendar.getInstance();
		 //过去一年
		  c.setTime(now);
		  c.add(Calendar.MONTH, -1);
		  Date y = c.getTime();
		  String month = format.format(y);
		  return month;
	}
	
	/***
	 * 获取过去一周
	 */
	public static synchronized String getWeekAgo(Date now) {

		  Calendar c = Calendar.getInstance();
		 //过去一年
		  c.setTime(now);
		  c.add(Calendar.DATE, -7);
		  Date y = c.getTime();
		  String week = format.format(y);
		  return week;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static Long getDistanceOfTwoDate(Date before, Date after) {
		if (Objects.isNull(before)) {
			return 0L;
		}
		if (Objects.isNull(after)) {
			return 0L;
		}
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/***
	 * 比较两个时间的大小，第一个大于第二个返回true，其他返回false
	 * @param startDate
	 * @param after
	 * @return
	 */
	 public static boolean compareDate(Date startDate, Date after) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            if (startDate.getTime() > after.getTime()) {
                return true;
            } else if (startDate.getTime() < after.getTime()) {
                return false;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		System.out.println(getMonthAgo(new Date()));;
		System.out.println(getWeekAgo(new Date()));;
	}
}
