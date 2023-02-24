package com.base.sbc.config.generator.entity;

/** 
 * 类描述：
 * @address com.celizi.base.common.generator.entity.Tables
 * @author shenzhixiong  
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月14日 下午1:57:21 
 * @version 1.0  
 */
public class Tables {
	public static final String GENXML = "1";
	public static final String GENENTITY = "2";
	public static final String GENDAO = "3";
	public static final String GENSERVICE = "4";
	public static final String GENCONTROLLER = "5";
	
	/** 表名 */
	public String tableName;
	/** 1,2,3,4,5   xml,entity,dao,service,controller  */
	public String genType;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getGenType() {
		return genType;
	}
	public void setGenType(String genType) {
		this.genType = genType;
	}
}
