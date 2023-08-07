package com.base.sbc.config.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 成分处理工具类
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年8月4日
 */
public class IngredientUtils {

//	@Data
//	static class Ingredient {
//		private String name;
//		private String ratio;
//		private String say;
//	}

	/*
	 *     
	 */

	public static void main(String[] args) {
		String s1 = "65%聚醋纤维 30%粘纤 5%银丝";
		String s2 = "66%聚醋纤维（说明），32%粘纤，2%氨纶";
		String s3 = "7 3 人棉 2 3锦纶4氨纶(说明)";
		String s4 = "棉72锦 28";
		String s5 = "65羊毛 35聚醋纤维";
		String s6 = "62 羊毛 （羊毛啊） 38  聚醋纤维( 纤维）";
		String s7 = "獭兔毛领";

		System.out.println(format(s1));
		System.out.println(format(s2));
		System.out.println(format(s3));
		System.out.println(format(s4));
		System.out.println(format(s5));
		System.out.println(format(s6));
		System.out.println(format(s7));
//		List<Ingredient> formatToList = formatToList(s6);
//		System.out.println(JsonUtils.beanToJson(formatToList));

	}

//	public static List<Ingredient> formatToList(String str) {
//		String format = format(str);
//		String[] strs = format.split(",");
//		List<Ingredient> list = new ArrayList<>();
//		for (int i = 0; i < strs.length; i++) {
//			String ingredients = strs[i];
//			Ingredient in = new Ingredient();
//			in.setNum(ingredients.split("%")[0]);
//			String nameSay = ingredients.split("%")[1];
//			int kidx = nameSay.indexOf("(");
//			String name = nameSay;
//			String say = "";
//			if (kidx != -1) {
//				name = nameSay.substring(0, kidx);
//				say = nameSay.substring(kidx + 1, nameSay.length() - 1);
//			}
//			in.setName(name);
//			in.setSay(say);
//			list.add(in);
//		}
//		return list;
//	}

	/**
	 * 格式化成分
	 * 
	 * @param str
	 * @return
	 */
	public static String format(String str) {
		// 清理
		str = clearOtherChar(str);
		// 转换
		str = numberChineseSort(str);
		boolean isNumber = false;
		String s = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			boolean digit = Character.isDigit(c);
			if (i != 0 && isNumber == digit) {
				s += c;
			} else {
				s += (isNumber ? "%" : ",") + String.valueOf(c);
			}
			isNumber = digit;
		}
		return s.length() > 0 ? s.substring(1) : str;
	}

	/**
	 * 转换 中文+数字+中文+数字 为 数字+中文+数字+中文
	 * 
	 * @param str
	 * @return
	 */
	public static String numberChineseSort(String str) {
		String result = "";
		List<String> list = new ArrayList<>();
		boolean isNumber = false;
		String s = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			boolean digit = Character.isDigit(c);
			if (digit && i == 0) {
				return str;
			}

			if (isNumber == digit) {
				s += c;
				if (i == str.length() - 1) {
					list.add(s);
				}
			} else {
				list.add(s);
				s = String.valueOf(c);
			}
			isNumber = digit;
		}
		if (list.size() == 1) {
			return "100" + str;
		}
		// 数字与中文互换
		for (int i = 0; i < list.size(); i++) {
			if (i % 2 != 0) {
				result += list.get(i) + list.get(i - 1);
			}
		}
		return result;
	}

	/**
	 * 清理剔除百分号和空格逗号等
	 * 
	 * @param str
	 * @return
	 */
	public static String clearOtherChar(String str) {
		str = str.replaceAll("%", "");
		str = str.replaceAll("\\s+", "");
		str = str.replaceAll("，", "");
		str = str.replaceAll(",", "");
		str = str.replaceAll("、", "");
		str = str.replaceAll("\\+", "");
		str = str.replaceAll("/", "");
		str = str.replaceAll("\"", "");
		str = str.replaceAll("（", "(");
		str = str.replaceAll("）", ")");
		return str;
	}

}
