package com.base.sbc.config.utils;

import com.base.sbc.config.constant.Constants;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


/**
 *  由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 *  确的浮点数运算，包括加减乘除和四舍五入。
 * @author Fred
 * @data 创建时间:2020/2/3
 */
public class BigDecimalUtil {

	/**
	 * 默认除法运算精度
	 */
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 这个类不能实例化
	 */
	private BigDecimalUtil() {

	}

	/**
	 * 提供精确的加法运算。
	 * @param v1  被加数
	 * @param v2  加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.add(b2).doubleValue();
	}
	
	
	
	/**
	 * 提供精确的加法运算。
	 * @param v1  被加数
	 * @param v2  加数
	 * @return 两个参数的和
	 */
	public static int add(Integer v1, Integer v2) {
		if(v1 == null) {
			v1 = 0;
		}
		if(v2 == null){
			v2 = 0;
		}
		BigDecimal b1 = new BigDecimal(Integer.toString(v1));
		BigDecimal b2 = new BigDecimal(Integer.toString(v2));
		return b1.add(b2).intValue();
	}



	/** 判断值是否相等
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static boolean valueEqual(BigDecimal v1, BigDecimal v2){
		if(v1 == null){
			v1 = BigDecimal.ZERO;
		}if(v2 == null){
			v2 = BigDecimal.ZERO;
		}
		return v1.compareTo(v2) ==0;
	}
	/**
	 * 判断是否为空
	 * @param v1  被加数
	 * @param v2  加数
	 * @return 两个参数的和
	 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
		if(v1 == null){
			v1 = BigDecimal.ZERO;
		}
		if(v2 == null){
			v2 = BigDecimal.ZERO;
		}
		return v1.add(v2);
	}

	/**
	 * 判断是否为空
	 * @param v1  被加数
	 * @param v2  加数
	 * @return 两个参数的和
	 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2, int scale) {
		if(v1 == null){
			v1 = BigDecimal.ZERO;
		}
		if(v2 == null){
			v2 = BigDecimal.ZERO;
		}
		return v1.add(v2).setScale(scale, RoundingMode.HALF_UP);
	}

	/**
	 * 获得绝对值
	 * @param bigDecimal
	 * @return
	 */
	public static BigDecimal abs(BigDecimal bigDecimal) {
		if(bigDecimal == null){
			bigDecimal = BigDecimal.ZERO;
		}
		if(!biggerThenZero(bigDecimal)){
			bigDecimal = mul(bigDecimal, new BigDecimal(-1));
		}
		return bigDecimal;
	}
	/**
	 * 判断是否为空
	 * @values values  被加数
	 * @return 和
	 */
	public static BigDecimal add( BigDecimal ... values ) {
		BigDecimal total = BigDecimal.ZERO;
		for( BigDecimal temp : values ) {
			if( temp == null) {
				temp = BigDecimal.ZERO;
			}
			total = total.add(temp);
		}
		return total;
	}

	/**
	 * 判断是否为空
	 * @param v1  被减数
	 * @param v2  减数
	 * @return 两个参数的差
	 */
	public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
		if(v1 == null){
			v1 = BigDecimal.ZERO;
		}
		if(v2 == null){
			v2 = BigDecimal.ZERO;
		}
		return v1.subtract(v2);
	}
	
	/**
	 * 提供非负数的减法
	 * @param v1  被减数
	 * @param v2  减数
	 * @return 两个参数的差
	 */
	public static BigDecimal subNonnegative(BigDecimal v1, BigDecimal v2) {
		if(v1 == null){
			v1 = BigDecimal.ZERO;
		}
		if(v2 == null){
			v2 = BigDecimal.ZERO;
		}
		BigDecimal result = v1.subtract(v2);
		return result.compareTo(BigDecimal.ZERO)>0?result:BigDecimal.ZERO;
	}
	
	/** 
	 * @Title: 两数相除 
	 * @param v1: 被除数
	 * @param v2: 除数
	 * @return BigDecimal    返回类型 
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2){
		if(v1 == null){
			v1 = BigDecimal.ZERO;
		}
		if(v2 == null){
			v2 = BigDecimal.ZERO;
		}
		if(v1.equals(BigDecimal.ZERO)){
			return BigDecimal.ZERO;
		}
		if(v2.equals(BigDecimal.ZERO)){
			return BigDecimal.ZERO;
		}
		return div(v1, v2, DEF_DIV_SCALE);
	}
	
	/**
	 * 提供精确的减法运算。
	 * @param v1  被减数
	 * @param v2  减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.subtract(b2).doubleValue();
	}
	
	/**
	 * 提供精确的乘法运算。
	 * @param v1  被乘数
	 * @param v2  乘数
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
		if(v1 == null || v2 == null){
			return BigDecimal.ZERO;
		}
		return v1.multiply(v2);
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(Integer scale,BigDecimal v, BigDecimal... values) {
		BigDecimal total = Objects.isNull(v) ? BigDecimal.ZERO : v;
		for (BigDecimal temp : values) {
			total = total.multiply(Objects.isNull(temp) ? BigDecimal.ZERO : temp).setScale(scale, RoundingMode.HALF_UP);
		}
		return total;
	}

	/**
	 * 提供精确的乘法运算。
	 * @param v1  被乘数
	 * @param v2  乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.multiply(b2).doubleValue();
	}
	
	/**
	 * 转换成objValue
	 * @param objValue
	 * @return BigDecimal
	 */
	public static BigDecimal convertBigDecimal(Object objValue) {
		if(objValue == null){
			return BigDecimal.ZERO; 
		}
		return new BigDecimal(objValue.toString());
	}
	/**
	 * 转换为String
	 * @param bigDecimal
	 * @return
	 */
	public static String convertString(BigDecimal bigDecimal){
		if(bigDecimal == null){
			return BigDecimal.ZERO.toString();
		}
		return bigDecimal.toString();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 小数点以后10位，以后的数字四舍五入。
	 * @param v1  被除数
	 * @param v2  除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。
	 * @param v1  被除数
	 * @param v2  除数
	 * @param scale  表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
			"The scale must be a positive integer or zero");
		}
		BigDecimal b1 =  BigDecimal.valueOf(v1);
		BigDecimal b2 =  BigDecimal.valueOf(v2);
		return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。
	 * @param v1  被除数
	 * @param v2  除数
	 * @param scale  表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */

	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
			"The scale must be a positive integer or zero");
		}
		if(v1 == null){
			v1 = BigDecimal.ZERO;
		}
		if(v2 == null || (BigDecimal.ZERO).compareTo(v2) == 0 ){
			return BigDecimal.ZERO;
		}
		return v1.divide(v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * @param v  需要四舍五入的数字
	 * @param scale  小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
			"The scale must be a positive integer or zero");
		}
		BigDecimal b =  BigDecimal.valueOf(v);
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
	}
	
	/** 万变成分
	 * @param money
	 * @return
	 */
	public static long tenThoundsToCent(String money){
		//去掉逗号
		if(money.indexOf(Constants.COMMA) > 0){
			money = StringUtils.remove(money, Constants.COMMA);
		}
		BigDecimal moneyd = BigDecimal.valueOf(Double.parseDouble(money));
		moneyd = moneyd.multiply(BigDecimal.valueOf(1000000));
		return moneyd.longValue();
	}
	
	
	/**
	 * @param money
	 * @return
	 */
	public static double centTotenThounds(String money){
		Double moneyd = Double.valueOf(money);
		return BigDecimalUtil.div(moneyd, 1000000d,6);
	}
	
	/**
	 * @param money
	 * @return
	 */
	public static boolean equalZero(BigDecimal money){
		if(null == money){
			return true;
		}
		return money.compareTo(BigDecimal.ZERO) == 0;
	}
	
	/**
	 * @param money
	 * @return
	 */
	public static boolean biggerThenZero(BigDecimal money){
		if(null == money){
			return false;
		}
		return money.compareTo(BigDecimal.ZERO) > 0;
	}
	
	/**
	 * @param money
	 * @return
	 */
	public static boolean biggerEqThenZero(BigDecimal money){
		if(null == money){
			return false;
		}
		return money.compareTo(BigDecimal.ZERO) >= 0;
	}
	
	/**
	 * @param money
	 * @return
	 */
	public static boolean smallerThenZero(BigDecimal money){
		if(null == money){
			return false;
		}
		return money.compareTo(BigDecimal.ZERO) < 0;
	}
	
	/**
	 * @param money
	 * @return
	 */
	public static boolean notEqualZero(BigDecimal money){
		if(null == money){
			return false;
		}
		return money.compareTo(BigDecimal.ZERO) != 0;
	}
	
	public static BigDecimal valueOf(String value){
		return StringUtils.isEmpty(value)?BigDecimal.ZERO:new BigDecimal(value);
	}
	
	
	/**
	 * 比较两个BigDecimal中的d1是否比d2大
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean biggerThen (BigDecimal d1,BigDecimal d2){
		if(d1 == null){
			d1 = BigDecimal.ZERO;
		}
		if(d2 == null){
			d2 = BigDecimal.ZERO;
		}
		return d1.compareTo(d2) > 0;
	}

	/**
	 * 计算两个BigDecimal的比值
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal dividePercentage (BigDecimal d1,BigDecimal d2){
		return dividePercentage(d1, d2, 2, RoundingMode.HALF_UP);
	}

	/**
	 * 计算两个BigDecimal的比值
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal dividePercentage (BigDecimal d1,BigDecimal d2, int scale, RoundingMode roundingMode){
		if(!biggerThenZero(d1)){
			return BigDecimal.ZERO;
		}
		if(!biggerThenZero(d2)){
			return BigDecimal.ZERO;
		}
		return d1.multiply(new BigDecimal(100)).divide(d2, scale, roundingMode);
	}

	/**
	 * 计算两个Double的比值
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Double dividePercentage (Double d1,Double d2){
		return dividePercentage(BigDecimal.valueOf(d1), BigDecimal.valueOf(d2)).doubleValue();
	}
}

