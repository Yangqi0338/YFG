/******************************************************************************
 * Copyright (C) 2018 celizi.com 
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.generator.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.base.sbc.config.generator.entity.DataSource;
import com.base.sbc.config.generator.entity.Params;
import com.base.sbc.config.generator.entity.Tables;

/** 
 * 类描述：
 * @address com.celizi.base.common.generator.utils.UtilXML
 * @author shenzhixiong  
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月14日 下午1:57:37 
 * @version 1.0  
 */
public class UtilXml {
	public static Params params = new Params();
	public static DataSource dataSource = new DataSource();
	public static List<Tables> tableList = new ArrayList<Tables>();

	@SuppressWarnings("unchecked")
	public static void parseXml(String xmlResource){
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(xmlResource));
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			for(Element element : elements){
				if("params".equals(element.getName())){
					List<Element> paramElements = element.elements();
					for(Element param : paramElements){
						if("osdir".equals(param.getName())){
							params.setOsdir(param.attributeValue("value"));
						}else if("xmlosdir".equals(param.getName())){
							params.setXmlosdir(param.attributeValue("value"));
						}else if("javapackage".equals(param.getName())){
							params.setJavapackage(param.attributeValue("value"));
						}else if("project".equals(param.getName())){
							params.setProject(param.attributeValue("value"));
						}
					}
				}else if("dataSource".equals(element.getName())){
					List<Element> ele = element.elements();
					for(Element e : ele){
						if("driver".equals(e.getName())){
							dataSource.setDriver(e.attributeValue("value"));
						}else if("url".equals(e.getName())){
							dataSource.setUrl(e.attributeValue("value"));
						}else if("username".equals(e.getName())){
							dataSource.setUsername(e.attributeValue("value"));
						}else if("password".equals(e.getName())){
							dataSource.setPassword(e.attributeValue("value"));
						}
					}
				}else if("author".equals(element.getName())){
					List<Element> paramElements = element.elements();
					for(Element param : paramElements){
						if("name".equals(param.getName())){
							params.setAuthor(param.attributeValue("value"));
						}else if("email".equals(param.getName())){
							params.setEmail(param.attributeValue("value"));
						}
					}
				}else if("tables".equals(element.getName())){
					List<Element> tables = element.elements();
					for(Element table : tables){
						if("table".equals(table.getName())){
							//yes 全部生成数据库中的所有表
							if("yes".equalsIgnoreCase(table.attributeValue("value"))){
								List<String> tableNames = UtilDb.getTableNames();
								for (String tableName : tableNames) {
									Tables t = new Tables();
									t.setTableName(tableName);
									t.setGenType(table.attributeValue("genType"));
									tableList.add(t);
								}
								break;
								//no 只生成config.xml配置的表
							} else if ("no".equalsIgnoreCase(table.attributeValue("value"))) {
								continue;
							}
							Tables t = new Tables();
							t.setTableName(table.attributeValue("value"));
							t.setGenType(table.attributeValue("genType"));
							tableList.add(t);
						}
					}
				}
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	public static Params getParams() {
		return params;
	}
	public static void setParams(Params params) {
		UtilXml.params = params;
	}
	public static DataSource getDataSource() {
		return dataSource;
	}
	public static void setDataSource(DataSource dataSource) {
		UtilXml.dataSource = dataSource;
	}
	public static List<Tables> getTableList() {
		return tableList;
	}
	public static void setTableList(List<Tables> tableList) {
		UtilXml.tableList = tableList;
	}
}
