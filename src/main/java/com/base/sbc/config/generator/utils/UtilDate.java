package com.base.sbc.config.generator.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * 类描述：
 * @address com.celizi.base.common.generator.utils.UtilDate
 * @author shenzhixiong  
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月14日 下午1:57:37 
 * @version 1.0  
 */
public class UtilDate {
	public static String getToday(){
		String result = "";
		Date date = new Date();
		result = format(date);
		return result;
	}
	public static String format(Date date){
		String format = "yyyyMMdd";
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		return fmt.format(date);
	}
}
