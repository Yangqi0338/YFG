package com.base.sbc.config.generator.utils;

/** 
 * 类描述：
 * @address com.celizi.base.common.generator.utils.UtilString
 * @author shenzhixiong  
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月14日 下午1:57:37 
 * @version 1.0  
 */
public class UtilString {

	public static final String VARCHAR = "VARCHAR";
	public static final String CHAR = "CHAR";
	public static final String TEXT = "TEXT";
	public static final String STRING = "String";
	public static final String DATETIME = "DATETIME";
	public static final String DATE = "DATE";
	public static final String DATE1 = "Date";
	public static final String INT = "INT";
	public static final String INT_UNSIGNED = "INT UNSIGNED";
	public static final String INTEGER = "Integer";
	public static final String MEDIUMBLOB = "MEDIUMBLOB";
	public static final String VARBINARY = "VARBINARY";
	public static final String BYTE_ARR = "byte[]";
	public static final String DECIMAL = "DECIMAL";
	public static final String BIG_DECIMAL = "BigDecimal";
	public static final String BIGINT = "BIGINT";
	public static final String TINYBLOB = "TINYBLOB";
	public static final String BLOB = "BLOB";

	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		if (null == str) {
			return null;
		} else if ("".equals(str)) {
			return str;
		}
		return Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}
	/**
	 * 将表中列名去下划线且下划线后首字母大写其他字母小写
	 * @param columnName 表中列名
	 * @return java类属性名
	 */
	public static String dbNameToVarName(String columnName) {
		if (columnName == null) {
			return null;
		}
		StringBuilder fieldName = new StringBuilder();
		boolean toUpper = false;
		for (int i = 0; i < columnName.length(); i++) {
			char ch = columnName.charAt(i);
			if (ch == '_') {
				toUpper = true;
			} else if (toUpper == true) {
				fieldName.append(Character.toUpperCase(ch));
				toUpper = false;
			} else {
				fieldName.append(Character.toLowerCase(ch));
			}
		}
		return fieldName.toString();
	}
	/**
	 * 将数据库类型转换成java类型
	 * @param columnType 数据库类型
	 * @return java类型
	 */
	public static String dbTypeToJavaType(String columnType) {
		String type = "";
		if (columnType == null || "".equals(columnType.trim())) {
			return null;
		}
		if (VARCHAR.equals(columnType) || CHAR.equals(columnType)
				|| TEXT.equals(columnType)) {
			type = STRING;
		} else if (DATE.equals(columnType) || DATETIME.equals(columnType)) {
			type = DATE1;
		} else if (INT.equals(columnType)
				|| INT_UNSIGNED.equals(columnType)) {
			type = INTEGER;
		} else if (MEDIUMBLOB.equals(columnType)
				|| VARBINARY.equals(columnType)) {
			type = BYTE_ARR;
		} else if (DECIMAL.equals(columnType)) {
			type = BIG_DECIMAL;
		} else if (BIGINT.equals(columnType)) {
			type = STRING;
		} else {
			System.out.println("未知的字段类型[" + columnType + "]");
			type = null;
		}
		return type;
	}
	/**
	 * 将数据库类型转换成mybatis配置文件类型
	 * @param sqlTypeName 数据库类型
	 * @return mybatis配置文件类型
	 */
	public static String mybatisType(String sqlTypeName) {
		String type = "";
		if (sqlTypeName == null || "".equals(sqlTypeName.trim())) {
			return null;
		}
		if (VARCHAR.equals(sqlTypeName) || TEXT.equals(sqlTypeName)) {
			type = VARCHAR;
		} else if (TINYBLOB.equals(sqlTypeName) || BLOB.equals(sqlTypeName)
				|| MEDIUMBLOB.equals(sqlTypeName)
				|| VARBINARY.equals(sqlTypeName)) {
			type = BLOB;
		} else if (CHAR.equals(sqlTypeName)) {
			type = CHAR;
		} else if (INT.equals(sqlTypeName) || INT_UNSIGNED.equals(sqlTypeName)) {
			type = "INTEGER";
		} else if (DATETIME.equals(sqlTypeName) || DATE.equals(sqlTypeName)) {
			type = "TIMESTAMP";
		} else if (DECIMAL.equals(sqlTypeName)) {
			type = DECIMAL;
		}  else if (BIGINT.equals(sqlTypeName)) {
			type = BIGINT;
		}else {
			System.out.println("未知的数据库类型[" + sqlTypeName + "]");
			type = null;
		}
		return type;
	}
	/*public static String getFileName(String tableName, String flag){
		String retName;
		if(){
			
		}
		switch(flag){
			case "po"  : retName = tableName + "_po"; break;
			case "vo"  : retName = tableName + "_vo"; break;
			default : retName = null;
		}
		return retName;
	}*/
}
