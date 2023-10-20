package com.base.sbc.config.utils;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

@Component
public class RedisCodeGenUtils {
	public static final Logger logger = LoggerFactory.getLogger(RedisCodeGenUtils.class);

	/**
	 * 日期格式
	 */
	public enum DateFormat{
		yy,
		yyyy,
		yyyyMMdd,
		yyyyMMddHH,
		yyMMddHH,
		yyMMdd,
		MMdd,
		dd,
		nil
	}
	public static final String BEGIN_NUM="001";
	/**单例锁Map*/
	public static final HashMap<String, Object> LOCK_MAP = new HashMap<>();

	@Resource
	private RedisUtils redisUtils;

	/**
	 * 适用与动态前缀长度
	 * 计算逻辑：拆分 prefix , 000
	 *
	 * @param prefix CHD
	 * @param isCheckOver   是否检查超出9999，true：不允许超出
	 * @return 返回 前缀+000
	 */
	public String getCode_000( String companyCode,String businessFlag , String prefix, Boolean isNeedPrefix,Boolean isCheckOver) {
		// 前缀不为空
		if (StrUtil.isBlank(prefix)) {
			throw new RuntimeException("the prefix is not null and empty");
		}
		String key = businessFlag+":"+prefix+":"+companyCode;
		long incr = redisUtils.incr(key, 1);
		// 最低位数 是兼容 当最后三位数大于 9999 时 也是可以正常生成 code
		// 业务需要， 大于 9999 报错：该编号超出系统允许最大值，请联系管理员尚捷技术人员
		if (incr > 999 && isCheckOver) {
			throw new RuntimeException("该编号超出系统允许最大值，请联系管理员尚捷技术人员");
		}
		if (incr < 10) {
			// 个位数 前面补2个0
			return (isNeedPrefix?prefix:"") + "00" + incr;
		} else if (incr < 100) {
			// 十位数 前面补1个0
			return (isNeedPrefix?prefix:"") + "0" + incr;
		} else {
			// 大于百位或者千位以上 直接返回
			return (isNeedPrefix?prefix:"") + incr;
		}
	}

	/**
	 * 适用与动态前缀长度
	 * 计算逻辑：拆分 prefix , 0000
	 *
	 * @param prefix CHD
	 * @param isCheckOver   是否检查超出9999，true：不允许超出
	 * @return 返回 前缀+0000
	 */
	public String getCode_0000( String companyCode,String businessFlag , String prefix, Boolean isNeedPrefix,Boolean isCheckOver) {
		// 前缀不为空
		if (StrUtil.isBlank(prefix)) {
			throw new RuntimeException("the prefix is not null and empty");
		}
		String key = businessFlag+":"+prefix+":"+companyCode;
		long incr = redisUtils.incr(key, 1);
		// 最低位数 是兼容 当最后三位数大于 9999 时 也是可以正常生成 code
		// 业务需要， 大于 9999 报错：该编号超出系统允许最大值，请联系管理员尚捷技术人员
		if (incr > 9999 && isCheckOver) {
			throw new RuntimeException("该编号超出系统允许最大值，请联系管理员尚捷技术人员");
		}
		if (incr < 10) {
			// 个位数 前面补2个0
			return (isNeedPrefix?prefix:"") + "000" + incr;
		} else if (incr < 100) {
			// 十位数 前面补1个0
			return (isNeedPrefix?prefix:"") + "00" + incr;
		} else if (incr < 1000) {
			// 十位数 前面补1个0
			return (isNeedPrefix?prefix:"") + "0" + incr;
		} else {
			// 大于百位或者千位以上 直接返回
			return (isNeedPrefix?prefix:"") + incr;
		}
	}

	/**
	 * 计算逻辑：年 , 000
	 *
	 * @param prefix CHD
	 * @return 返回 前缀+年+000
	 */
	public String getCode_yyyy000(String companyCode, String businessFlag ,String prefix, Boolean isNeedPrefix,Boolean isCheckOver) {
		// 前缀不为空
		if (StrUtil.isBlank(prefix)) {
			throw new RuntimeException("the prefix is not null and empty");
		}
		// 获取今天的年月日
		String code = DateUtils.formatDateTime(new Date());
		String current_year =
				code.substring(0, 4) ; // 年后4位
		String key = businessFlag+":"+prefix+":"+companyCode+":"+current_year;
		long incr = redisUtils.incr(key, 1);
		if(incr == 1){
			// 加上30s容错
			redisUtils.expire(key, 366*24*60*60 + 30);
		}
		// 最低位数 是兼容 当最后三位数大于 999 时 也是可以正常生成 code
		// 业务需要， 大于 999 报错：该编号超出系统允许最大值，请联系管理员尚捷技术人员
		if (incr > 9999 && isCheckOver) {
			throw new RuntimeException("该编号超出系统允许最大值，请联系管理员尚捷技术人员");
		}
		if (incr < 10) {
			// 个位数 前面补2个0
			return (isNeedPrefix?prefix:"")+current_year+"000" + incr;
		} else if (incr < 100) {
			// 十位数 前面补1个0
			return (isNeedPrefix?prefix:"")+current_year+"00" + incr;
		} else if (incr < 1000) {
			return (isNeedPrefix?prefix:"")+current_year+"0"+incr;
		}else {
			// 大于百位或者千位以上 直接返回
			return (isNeedPrefix?prefix:"")+current_year+incr;
		}
	}

	/**
	 * 计算逻辑：年月日 , 000
	 *
	 * @param prefix CHD
	 * @return 返回 前缀+年月日+000
	 */
	public String getCode_YYYYMMDD000(String companyCode,String businessFlag , String prefix, Boolean isNeedPrefix,Boolean isCheckOver) {
		// 前缀不为空
		if (StrUtil.isBlank(prefix)) {
			throw new RuntimeException("the prefix is not null and empty");
		}
		// 获取今天的年月日
		Date date = new Date();
		String current_year_month_day = changeDate_YYYYMMDD(date);
		String key = changeKeyToRedis1(date,companyCode,prefix,businessFlag);
		long incr = redisUtils.incr(key, 1);
		if(incr == 1){
			// 加上30s容错
			redisUtils.expire(key, 60*60*24+30);
		}
		// 最低位数 是兼容 当最后三位数大于 999 时 也是可以正常生成 code
		// 业务需要， 大于 999 报错：该编号超出系统允许最大值，请联系管理员尚捷技术人员
		if (incr > 999 && isCheckOver) {
			throw new RuntimeException("该编号超出系统允许最大值，请联系管理员尚捷技术人员");
		}
		if (incr < 10) {
			// 个位数 前面补2个0
			return  (isNeedPrefix?prefix:"")+ current_year_month_day+"00" + incr;
		} else if (incr < 100) {
			// 十位数 前面补1个0
			return (isNeedPrefix?prefix:"")+current_year_month_day+"0" + incr;
		} else {
			// 大于百位或者千位以上 直接返回
			return (isNeedPrefix?prefix:"")+current_year_month_day+incr;
		}
	}

	/**
	 * 计算逻辑：年月日时 , 000
	 *
	 * @param prefix CHD
	 * @return 返回 前缀+年月日时+000
	 */
	public String getCode_YYMMDDHH000(String companyCode, String businessFlag ,String prefix, Boolean isNeedPrefix,Boolean isCheckOver) {
		// 前缀不为空
		if (StrUtil.isBlank(prefix)) {
			throw new RuntimeException("the prefix is not null and empty");
		}
		// 获取今天的年月日
		String code = DateUtils.formatDateTime(new Date());
		String current_year_month_day_hour =
				code.substring(2, 4) + // 年后2位
						code.substring(5, 7) + // 月份2位
						code.substring(8, 10) + // 几号2位
						code.substring(11, 13); // 小时2位
		String key = businessFlag+":"+prefix+":"+companyCode+":"+current_year_month_day_hour;
		long incr = redisUtils.incr(key, 1);
		if(incr == 1){
			// 加上30s容错
			redisUtils.expire(key, 60*60 + 30);
		}
		// 最低位数 是兼容 当最后三位数大于 999 时 也是可以正常生成 code
		// 业务需要， 大于 999 报错：该编号超出系统允许最大值，请联系管理员尚捷技术人员
		if (incr > 999 && isCheckOver) {
			throw new RuntimeException("该编号超出系统允许最大值，请联系管理员尚捷技术人员");
		}
		if (incr < 10) {
			// 个位数 前面补2个0
			return (isNeedPrefix?prefix:"")+current_year_month_day_hour+"00" + incr;
		} else if (incr < 100) {
			// 十位数 前面补1个0
			return (isNeedPrefix?prefix:"")+current_year_month_day_hour+"0" + incr;
		} else {
			// 大于百位或者千位以上 直接返回
			return (isNeedPrefix?prefix:"")+current_year_month_day_hour+incr;
		}
	}

	/**
	 * 计算逻辑：年月日时 , 000
	 *
	 * @param prefix CHD
	 * @return 返回 前缀+年月日时+000
	 */
	public String getCode_YYMMDD000(String companyCode, String businessFlag ,String prefix,Boolean isNeedPrefix, Boolean isCheckOver) {
		// 前缀不为空
		if (StrUtil.isBlank(prefix)) {
			throw new RuntimeException("the prefix is not null and empty");
		}
		// 获取今天的年月日
		Date date = new Date();
		String current_year_month_day = changeDate_YYYYMMDD(date);
		String key = changeKeyToRedis1(date,companyCode,prefix,businessFlag);
		long incr = redisUtils.incr(key, 1);
		if(incr == 1){
			// 加上30s容错
			redisUtils.expire(key, 60*60*24 + 30);
		}
		// 最低位数 是兼容 当最后三位数大于 999 时 也是可以正常生成 code
		// 业务需要， 大于 999 报错：该编号超出系统允许最大值，请联系管理员尚捷技术人员
		if (incr > 999 && isCheckOver) {
			throw new RuntimeException("该编号超出系统允许最大值，请联系管理员尚捷技术人员");
		}
		if (incr < 10) {
			// 个位数 前面补2个0
			return  (isNeedPrefix?prefix:"")+current_year_month_day+"00" + incr;
		} else if (incr < 100) {
			// 十位数 前面补1个0
			return (isNeedPrefix?prefix:"")+current_year_month_day+"0" + incr;
		} else {
			// 大于百位或者千位以上 直接返回
			return  (isNeedPrefix?prefix:"")+current_year_month_day+ incr;
		}
	}

	public String getCode_YYMMDD(String companyCode, String businessFlag ,String prefix,Boolean isNeedPrefix, Boolean isCheckOver, int zeroLen) {
		// 前缀不为空
		if (StrUtil.isBlank(prefix)) {
			throw new RuntimeException("the prefix is not null and empty");
		}
		// 获取今天的年月日
		Date date = new Date();
		String current_year_month_day = changeDate_YYMMDD(date);
		String key = changeKeyToRedis1(date,companyCode,prefix,businessFlag);
		long incr = redisUtils.incr(key, 1);
		if(incr == 1){
			// 加上30s容错
			redisUtils.expire(key, 60*60*24 + 30);
		}
		// 最低位数 是兼容 当最后三位数大于 999 时 也是可以正常生成 code
		// 业务需要， 大于 999 报错：该编号超出系统允许最大值，请联系管理员尚捷技术人员
		double max = Math.pow(zeroLen, 10);
		if (incr >= max && isCheckOver) {
			throw new RuntimeException("该编号超出系统允许最大值，请联系管理员尚捷技术人员");
		}
		return  (isNeedPrefix?prefix:"")+current_year_month_day+ String.format("%05d", incr);
	}

	/**
	 * 获取redis/其他数据源code 计算逻辑：前缀？+年月日+流水号
	 * @param date 日期
	 * @param dateFormat 日期格式（可为空）
	 * @param companyCode 企业编码
	 * @param prefix 前缀（为空则不需要）
	 * @param businessFlag 业务标记
	 * @param reLockSecond 等待时间（秒）
	 * @param getData 除redis外的数据来源（如mysql）,传入函数处理
	 * @return
	 */
	public String getCode_DATA(Date date,DateFormat dateFormat, String companyCode, String prefix, String businessFlag, Integer reLockSecond, MultiParamLambda getData){
		//1.校验是否有date参数，无则用当前时间
		if(date == null){
			date = new Date();
		}
		String dateStr = checkAndFormat(dateFormat,date);

		//2.组装key
		String key = changeKeyToRedis(dateStr, companyCode, prefix, businessFlag);

		//3.校验redis是否存在数据
		Object o = redisUtils.get(key);
		if (o != null){
			//3.1返回redis单号
			String result = packageReturn(key, 1, prefix, dateStr);
			if (redisUtils.getExpire(key) < 30){
				expireFormat(dateFormat,key);
			}
			return result;
		}else{
			//3.2是否能查询其他数据源
			Boolean canSelect = isSyncBusiness(key);
			try {
				if (canSelect){
					//3.3查询其他数据源并返回
					int num = getData.getSerialNum(date,companyCode);
					String result = packageReturn(key, num + 1, prefix, dateStr);
					expireFormat(dateFormat,key);
					return  result;
				}else{
					//3.4进入等待
					boolean isExist = true;
					int reLockSecondMills = reLockSecond*100;
					int waitTimes = 0;
					while (isExist){
						if (LOCK_MAP.containsKey(key)){
							waitTimes+=1;
							//3.5线程先睡眠10毫米
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
								throw new RuntimeException("sleep 10 mills Exception");
							}
							//3.6超时直接返回错误
							if(waitTimes > reLockSecondMills){
								throw new RuntimeException("getCode Timeout Exception");
							}
						}else{
							//3.7结束当前等待
							isExist = false;
						}
					}
					//3.8递归回去获取单号
					return getCode_DATA(date,dateFormat,companyCode,prefix,businessFlag,reLockSecond,getData);
				}
			}finally {
				//4.释放资源
				if (canSelect){
					LOCK_MAP.remove(key);
				}
			}
		}
	}

	/**
	 * 校验日期格式并转换
	 * @param dateFormat
	 * @param date
	 * @return
	 */
	private String checkAndFormat(DateFormat dateFormat,Date date){
		switch (dateFormat){
			case yy:
				return changeDate_YY(date);
			case yyyy:
				return changeDate_YYYY(date);
			case yyyyMMdd:
				return changeDate_YYYYMMDD(date);
			case yyyyMMddHH:
				return changeDate_YYYYMMDDHH(date);
			case yyMMddHH:
				return changeDate_YYMMDDHH(date);
			case yyMMdd:
				return changeDate_YYMMDD(date);
			case MMdd:
				return changeDate_MMDD(date);
			case dd:
				return changeDate_DD(date);
			case nil:
				return "";
			default:
				return "";
		}
	}

	/**
	 * 校验lockMap中是否有key，有则返回true
	 * @param key
	 * @return
	 */
	private synchronized Boolean isSyncBusiness(String key){
		if (LOCK_MAP.containsKey(key)){
			return false;
		}else {
			LOCK_MAP.put(key, key);
			return true;
		}
	}

	/**
	 * 组装返回
	 * @param key redis-key
	 * @param num 自增因子
	 * @param prefix 前缀
	 * @param dateStr 日期
	 * @return
	 */
	private String packageReturn(String key,long num,String prefix, String dateStr){
		long incr = redisUtils.incr(key, num);
		return StringUtils.isBlank(prefix) ? dateStr + String.format("%03d",incr) : prefix + dateStr + String.format("%03d",incr);
	}


	/**
	 * 根据日期格式设置过期时间
	 * @param dateFormat
	 * @param key
	 * @return
	 */
	private void expireFormat(DateFormat dateFormat, String key){
		switch (dateFormat){
			case yy:
			case yyyy:
				redisUtils.expire(key, 366 * 60 * 60 * 24 + 30);
				break;
			case yyyyMMdd:
			case MMdd:
			case dd:
			case yyMMdd:
				redisUtils.expire(key, 60 * 60 * 24 + 30);
				break;
			case yyyyMMddHH:
			case yyMMddHH:
				redisUtils.expire(key, 60 * 60 + 30);
				break;
			default:
				redisUtils.expire(key, 60 * 60 * 24 * 31 + 30);
		}
	}

	/**
	 * 组装redis - key （业务标记：前缀：企业编码：日期）
	 * @param userCompany 企业编码
	 * @param prefix 前缀
	 * @param businessFlag 业务标记
	 * @return
	 */
	private String changeKeyToRedis(String dateStr,String userCompany,String prefix,String businessFlag){
		return businessFlag+":"+prefix+":"+userCompany+":"+dateStr;
	}

	/**
	 * 组装redis - key （业务标记：前缀：企业编码：日期）
	 * @param date 日期
	 * @param userCompany 企业编码
	 * @param prefix 前缀
	 * @param businessFlag 业务标记
	 * @return
	 */
	private String changeKeyToRedis1(Date date,String userCompany,String prefix,String businessFlag){
		String current_year_month_day = changeDate_YYMMDD(date);
		return businessFlag+":"+prefix+":"+userCompany+":"+current_year_month_day;
	}

	/**
	 * 日期转换为年月日（yy）
	 * @param date 日期
	 * @return
	 */
	private String changeDate_YY(Date date){
		String code = DateUtils.formatDateTime(date);
		return code.substring(2, 4);
	}

	/**
	 * 日期转换为年月日（yyyy）
	 * @param date 日期
	 * @return
	 */
	private String changeDate_YYYY(Date date){
		String code = DateUtils.formatDateTime(date);
		return code.substring(0, 4);
	}

	/**
	 * 日期转换为年月日（yyyyMMddHH）
	 * @param date 日期
	 * @return
	 */
	private String changeDate_YYYYMMDDHH(Date date){
		String code = DateUtils.formatDateTime(date);
		return code.substring(0, 4) + code.substring(5, 7) + code.substring(8, 10) + code.substring(11, 13);
	}

	/**
	 * 日期转换为年月日（yyMMddHH）
	 * @param date 日期
	 * @return
	 */
	private String changeDate_YYMMDDHH(Date date){
		String code = DateUtils.formatDateTime(date);
		return code.substring(2, 4) + code.substring(5, 7) + code.substring(8, 10) + code.substring(11, 13);
	}

	/**
	 * 日期转换为年月日（yyyyMMdd）
	 * @param date 日期
	 * @return
	 */
	private String changeDate_YYYYMMDD(Date date){
		String code = DateUtils.formatDateTime(date);
		return code.substring(0, 4) + code.substring(5, 7) + code.substring(8, 10);
	}

	/**
	 * 日期转换为年月日（yyMMdd）
	 * @param date 日期
	 * @return
	 */
	private String changeDate_YYMMDD(Date date){
		String code = DateUtils.formatDateTime(date);
		return code.substring(2, 4) + code.substring(5, 7) + code.substring(8, 10);
	}

	/**
	 * 日期转换为年月日（MMdd）
	 * @param date 日期
	 * @return
	 */
	private String changeDate_MMDD(Date date){
		String code = DateUtils.formatDateTime(date);
		return code.substring(5, 7) + code.substring(8, 10);
	}

	/**
	 * 日期转换为年月日（dd）
	 * @param date 日期
	 * @return
	 */
	private String changeDate_DD(Date date){
		String code = DateUtils.formatDateTime(date);
		return code.substring(8, 10);
	}



	/**
	 * @author Liu YuBin
	 * @version 1.0
	 * @date 2023/2/28 11:11
	 */
	@FunctionalInterface
	public interface MultiParamLambda {
		/**
		 * 获取流水号
		 * @return
		 */
		Integer getSerialNum(Date data, String userCompany);
	}


	/**
	 * 按天生成编码
	 *
	 * @param prefix      编码前缀
	 * @param companyCode 企业编码
	 * @param dateFormat  日期格式
	 * @param bizType     标识
	 * @return
	 */
	public String genCodeByDay(String prefix, String companyCode, String dateFormat, String bizType) {
		String dateStr = DateUtils.getDate(dateFormat);
		long count = redisUtils.incr(bizType + ":" + companyCode + ":" + dateStr, 1);
		try {
			LocalDate localDate = LocalDate.now().minusDays(1);
			redisUtils.del(bizType + ":" + companyCode + ":" + localDate.format(DateTimeFormatter.ofPattern(dateFormat)));
		} catch (Exception e) {
			logger.error("RedisCodeGenUtils#countByDay del throws", e);
		}
		return prefix + dateStr + String.format("%04d", count);
	}

	/**
	 * 计算逻辑：月日 , 00
	 *
	 * @param prefix CHD
	 * @return 返回 前缀+月日+00
	 */
	public String getCode_MMDD00(String companyCode, String businessFlag ,String prefix, Boolean isNeedPrefix,Boolean isCheckOver) {
		// 获取今天的年月日
		String code = DateUtils.formatDateTime(new Date());
		String current_year_month_day_hour = code.substring(5, 7) + // 月份2位
						code.substring(8, 10);  // 几号2位
		String key = businessFlag+":"+prefix+":"+companyCode+":"+current_year_month_day_hour;
		long incr = redisUtils.incr(key, 1);
		if(incr == 1){
			// 加上30s容错
			redisUtils.expire(key, 60*60 + 30);
		}
		// 最低位数 是兼容 当最后三位数大于 99 时 也是可以正常生成 code
		// 业务需要， 大于 99 报错：该编号超出系统允许最大值，请联系管理员尚捷技术人员
		if (incr > 99 && isCheckOver) {
			throw new RuntimeException("该编号超出系统允许最大值，请联系管理员尚捷技术人员");
		}
		if (incr < 100) {
			// 十位数 前面补1个0
			return (isNeedPrefix?prefix:"")+current_year_month_day_hour+"0" + incr;
		} else {
			// 大于百位或者千位以上 直接返回
			return (isNeedPrefix?prefix:"")+current_year_month_day_hour+incr;
		}
	}
}

