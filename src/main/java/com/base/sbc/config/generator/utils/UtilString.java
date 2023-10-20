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
		String type;
		if (columnType == null || columnType.trim().isEmpty()) {
			return null;
		}
        switch (columnType) {
            case VARCHAR:
            case CHAR:
            case TEXT:
                type = STRING;
                break;
            case DATE:
            case DATETIME:
                type = DATE1;
                break;
            case INT:
            case INT_UNSIGNED:
                type = INTEGER;
                break;
            case MEDIUMBLOB:
            case VARBINARY:
                type = BYTE_ARR;
                break;
            case DECIMAL:
                type = BIG_DECIMAL;
                break;
            case BIGINT:
                type = STRING;
                break;
            default:
                System.out.println("未知的字段类型[" + columnType + "]");
                type = null;
                break;
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
        switch (sqlTypeName) {
            case VARCHAR:
            case TEXT:
                type = VARCHAR;
                break;
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case VARBINARY:
                type = BLOB;
                break;
            case CHAR:
                type = CHAR;
                break;
            case INT:
            case INT_UNSIGNED:
                type = "INTEGER";
                break;
            case DATETIME:
            case DATE:
                type = "TIMESTAMP";
                break;
            case DECIMAL:
                type = DECIMAL;
                break;
            case BIGINT:
                type = BIGINT;
                break;
            default:
                System.out.println("未知的数据库类型[" + sqlTypeName + "]");
                type = null;
                break;
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
