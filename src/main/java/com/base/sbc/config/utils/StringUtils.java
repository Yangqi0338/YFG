/**
 * 
 */
package com.base.sbc.config.utils;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.exception.OtherException;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * 
 * @author
 * @version 2013-05-22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	// private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

	private static final char SEPARATOR = '_';
	private static final char D = ',';
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * 获取订单编号
	 * @param string  前缀
	 * @return
	 */
	public synchronized static String getBillCode(String string) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date newDate = new Date();
		// String类型的当前时间戳
		String stringTime = String.valueOf(newDate.getTime());
		return string+sdf.format(newDate) + stringTime.substring(stringTime.length()-4,stringTime.length());
	}

	/**
	 * 将Sting数组 转换为List<String>
	 * 
	 * @param strs
	 * @return
	 */
	public static List<String> convertStrsToList(String[] strs) {
		List<String> list = Lists.newArrayList();
		if (strs != null && strs.length > 0) {
			list.addAll(Arrays.asList(strs));
		}
		return list;
	}

	/**
	 * 将数组转换为'1','2','3','4'
	 * 
	 * @param strs
	 * @return
	 */
	public static String convertStrsToStr(String[] strs) {
		if (strs != null && strs.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (String string : strs) {
				sb.append("'").append(string.trim()).append("'").append(D);
			}
			return sb.substring(0, sb.length() - 1);
		}
		return "'-1'";
	}

	/**
	 * 将list<String> 转换为1,2,3,4,5
	 * 
	 * @param list
	 * @return
	 */
	public static String convertListToString(List<String> list) {
		if (list != null && !list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String string : list) {
				sb.append(string).append(D);
			}
			return sb.substring(0,sb.length()-1);
		}
		return "";
	}

	/**
	 * 将Set<String> 转换为1,2,3,4,5
	 *
	 * @param list
	 * @return
	 */
	public static String convertSetToString(Set<String> list) {
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String string : list) {
				sb.append(string).append(D);
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * 将1,2,3,a,b,c 转换为List<String>
	 * 
	 * @param str
	 * @return
	 */
	public static List<String> convertList(String str) {
		List<String> list = Lists.newArrayList();
		if (isNotBlank(str)) {
			String[] s = str.split(String.valueOf(D));
			for (String string : s) {
				list.add(string);
			}
		}
		return list;
	}
	public static List<String> convertList(String str,boolean isNull) {
		List<String> list = Lists.newArrayList();
		if (isNotBlank(str)) {
			String[] s = str.split(String.valueOf(D));
			for (String string : s) {
				if(isNull && StringUtils.isNotBlank(string)){
					list.add(string);
				}else{
					list.add(null);
				}
			}
		}
		return list;
	}
	/**
	 * 将1,2,3,a,b,c 转换为 "1","2","3","a","b","c"
	 * 
	 * @param str
	 * @return
	 */
	public static String convertStr(String str) {
		if (isNotBlank(str)) {
			StringBuilder sb = new StringBuilder();
			String[] s = str.split(String.valueOf(D));
			for (String string : s) {
				sb.append("'").append(string).append("'").append(D);
			}
			return sb.substring(0, sb.length() - 1);
		}
		return "";
	}

	/**
	 * 转换为字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] getBytes(String str) {
		if (str != null) {
			try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 转换为Boolean类型 'true', 'on', 'y', 't', 'yes' or '1' (case insensitive) will
	 * return true. Otherwise, false is returned.
	 */
	public static Boolean toBoolean(final Object val) {
		if (val == null) {
			return false;
		}
		return BooleanUtils.toBoolean(val.toString()) || "1".equals(val.toString());
	}

	/**
	 * 转换为字节数组
	 * @return
	 */
	public static String toString(byte[] bytes) {
		try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
	}

	/**
	 * 如果对象为空，则使用defaultVal值 see: ObjectUtils.toString(obj, defaultVal)
	 * 
	 * @param obj
	 * @param defaultVal
	 * @return
	 */
	public static String toString(final Object obj, final String defaultVal) {
		return obj == null ? defaultVal : obj.toString();
	}

	/**
	 * 是否包含字符串
	 * 
	 * @param str
	 *            验证字符串
	 * @param strs
	 *            字符串组
	 * @return 包含返回true
	 */
	public static boolean inString(String str, String... strs) {
		if (str != null) {
			for (String s : strs) {
				if (str.equals(trim(s))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}
	/**
	 * 替换html的内容 &nbsp; 转换空格
	 * @return
	 */
	public static String replaceHtmlCode(String s) {
		if (isBlank(s)) {
			return null;
		}
		s = StringEscapeUtils.unescapeHtml4(s);
		return s;
	}


	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * 
	 * @param html
	 * @return
	 */
	public static String replaceMobileHtml(String html) {
		if (html == null) {
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * 
	 * @param txt
	 * @return
	 */
	/*
	 * public static String toHtml(String txt){ if (txt == null){ return ""; }
	 * return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t",
	 * "&nbsp; &nbsp; "); }
	 */

	/**
	 * 缩略字符串（不区分中英文字符）
	 * 
	 * @param str
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
	public static String abbr2(String param, int length) {
		if (param == null) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		int n = 0;
		char temp;
		// 是不是HTML代码
		boolean isCode = false;
		// 是不是HTML特殊字符,如&nbsp;
		boolean isHtml = false;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHtml = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHtml) {
				isHtml = false;
			}
			try {
				if (!isCode && !isHtml) {
					n += String.valueOf(temp).getBytes("GBK").length;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (n <= length - 3) {
				result.append(temp);
			} else {
				result.append("...");
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String tempResult = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
		// 去掉不需要结素标记的HTML标记
		tempResult = tempResult.replaceAll(
				"</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
				"");
		// 去掉成对的HTML标记
		tempResult = tempResult.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
		// 用正则表达式取出标记
		
		Matcher m = p.matcher(tempResult);
		List<String> endHtml = Lists.newArrayList();
		while (m.find()) {
			endHtml.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHtml.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHtml.get(i));
			result.append(">");
		}
		return result.toString();
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 获得i18n字符串
	 */
	/*
	 * public static String getMessage(String code, Object[] args) { //
	 * LocaleResolver localLocaleResolver = (LocaleResolver)
	 * SpringContextHolder.getBean(LocaleResolver.class); // Locale localLocale =
	 * localLocaleResolver.resolveLocale(request); // return
	 * SpringContextHolder.getApplicationContext().getMessage(code, args,
	 * localLocale); HttpServletRequest request =
	 * ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).
	 * getRequest(); Locale locale = RequestContextUtils.getLocale(request);
	 * 
	 * logger.info("Locale:" + locale.getLanguage()); // String msg1 =
	 * messageSource.getMessage(code, null, Locale.CHINA); // logger.info("Msg1:" +
	 * msg1);
	 * 
	 * String msg2 = messageSource.getMessage(code, args, locale);
	 * logger.info("Msg2:" + msg2); return msg2; }
	 */

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		} else if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld"
	 *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 *         toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCamelCase(String s) {
		if (s == null) {
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == SEPARATOR) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld"
	 *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 *         toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCapitalizeCamelCase(String s) {
		if (s == null) {
			return null;
		}
		s = toCamelCase(s);
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld"
	 *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 *         toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toUnderScoreCase(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i > 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 转换为JS获取对象值，生成三目运算返回结果
	 * 
	 * @param objectString
	 *            对象串 例如：row.user.id
	 *            返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
	 */
	public static String jsGetVal(String objectString) {
		StringBuilder result = new StringBuilder();
		StringBuilder val = new StringBuilder();
		String[] vals = split(objectString, ".");
        for (String s : vals) {
            val.append(".").append(s);
            result.append("!").append(val.substring(1)).append("?'':");
        }
		result.append(val.substring(1));
		return result.toString();
	}

	/**
	 * 方法描述 ming
	 * @param categoryName 品类名称路径:(大类/品类/中类/小类)
	 * @param index 0大类，1品类，2中类，3小类
	 * @param typeIndex 0label,1 value
	 * @return
	 */
	public static String getCategory(String categoryName,int index,int typeIndex){
		if(StringUtils.isBlank(categoryName)){
			return  "";
		}
	   String[] strings = categoryName.split("/");
		return strings[index].split(",")[typeIndex] ;
	}


	/**
	 * 多个值使用ACT_RE_PROCDEF查询
	 *
	 * 返回
	 * FIND_IN_SET('0', category_id)
	 * or
	 * FIND_IN_SET('3', category_id)
	 * or
	 * FIND_IN_SET('9', category_id)
	 */
	public static String findInSet(String strings,String field) {
		StringBuilder s = new StringBuilder();
		if(isNotBlank(strings)){
			String[] strings1 = strings.split(",");
			for (int i = 0; i < strings1.length; i++) {
			    	s.append(" FIND_IN_SET('").append(strings1[i]).append("', ").append(field).append(") ");
				if(i != strings1.length-1){
					s.append(" or ");
				}
			}
		}
		return s.toString();
	}

	/**
	 * rgb转16进制
	 * @param rgbValue
	 * @return
	 */
	public static String rgbToHex(String rgbValue) {
		int[] rgbComponents = extractRGB(rgbValue);

		String redHex = Integer.toHexString(rgbComponents[0]);
		String greenHex = Integer.toHexString(rgbComponents[1]);
		String blueHex = Integer.toHexString(rgbComponents[2]);
		// 确保十六进制字符串长度为两位
		redHex = padLeft(redHex);
		greenHex = padLeft(greenHex);
		blueHex = padLeft(blueHex);
		return "#" + redHex + greenHex + blueHex;
	}

	public static String padLeft(String str) {
		StringBuilder sb = new StringBuilder();
		while (sb.length() + str.length() < 2) {
			sb.append('0');
		}
		sb.append(str);
		return sb.toString();
	}

	public static int[] extractRGB(String rgbValue) {
		String[] components = rgbValue.replaceAll("[^\\d,]", "").split(",");
		int red = Integer.parseInt(components[0].trim());
		int green = Integer.parseInt(components[1].trim());
		int blue = Integer.parseInt(components[2].trim());
		return new int[]{red, green, blue};
	}

	/**
	 * 地址获取图片名称
	 * @param imageUrl
	 * @return
	 */
	public static String getImageNameWithoutExtension(String imageUrl) {
		int lastIndexOfSlash = imageUrl.lastIndexOf('/');
		int lastIndexOfDot = imageUrl.lastIndexOf('.');
		if (lastIndexOfSlash == -1 || lastIndexOfDot == -1 || lastIndexOfDot < lastIndexOfSlash) {
			return "";
		}
		return imageUrl.substring(lastIndexOfSlash + 1, lastIndexOfDot);
	}

	/**
	 * 去掉空格等
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (isNotBlank(str)) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;

	}

	public static String keepStrByType(String str, MatchStrType... types) {
		return keepStrByType(str, null, types);
	}

	/**
	 * 仅保留类型里的字符串
	 * @param str
	 * @return
	 */
	public static String keepStrByType(String str, String warningMsg, MatchStrType... types) {
		char[] sourceArray = str.toCharArray();
		StringBuilder resultBuilder = new StringBuilder();
		for (char c : sourceArray) {
			for (MatchStrType type : types) {
				// 开始结束为null 获取子类型
				List<MatchStrType> childrenTypeList = type.getChildrenType();
				if (type.startIndex != null && type.endIndex != null) {
					childrenTypeList = Collections.singletonList(type);
				}
				for (MatchStrType childrenType : childrenTypeList) {
					Pair<Integer, Integer> range = childrenType.getRange();
                    if (range.getKey() <= (int) c && range.getValue() >= (int) c) {
						resultBuilder.append(c);
						break;
					}
				}
			}
		}
		String result = resultBuilder.toString();
		if (StrUtil.isNotBlank(warningMsg) && result.length() != str.length()) {
			throw new OtherException(warningMsg);
		}
		return result;
	}

	public static void main(String[] args) {
		String s = keepStrByType("我59  9X8\r\t030LXF\nS-2 ", "",MatchStrType.LETTER, MatchStrType.NUMBER, MatchStrType.BARRE);
		System.out.println(s);
	}

	@Getter
	@AllArgsConstructor
	public enum MatchStrType {
		LETTER_UPPER(65,90),
		LETTER_LOWER(97,122),
		LETTER(LETTER_UPPER, LETTER_LOWER),
		NUMBER(48,57),
		BARRE(45,45),
		UNDERLINE(95,95)
		;

		private final Integer startIndex;
		private final Integer endIndex;
		private final List<MatchStrType> childrenType;

		MatchStrType(int startIndex, int endIndex) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.childrenType = new ArrayList<>();
		}

		MatchStrType(MatchStrType... childrenType) {
			this.startIndex = null;
			this.endIndex = null;
			this.childrenType = Arrays.asList(childrenType);
		}

		public Pair<Integer,Integer> getRange(){
			return Pair.of(this.startIndex, this.endIndex);
		}
	}


}
