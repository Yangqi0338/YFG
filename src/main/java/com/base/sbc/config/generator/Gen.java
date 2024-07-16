/******************************************************************************
 * Copyright (C) 2018 celizi.com
 * All Rights Reserved.
 * 本软件为网址：celizi.com 开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.generator;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.generator.entity.Params;
import com.base.sbc.config.generator.entity.Tables;
import com.base.sbc.config.generator.utils.UtilFile;
import com.base.sbc.config.generator.utils.UtilFreemarker;
import com.base.sbc.config.generator.utils.UtilString;
import com.base.sbc.config.generator.utils.UtilXml;
import com.base.sbc.config.utils.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 类描述：代码生成 总控制枢纽
 * 需要先设置config.xml对应 数据库表 和 项目的模块名称
 * @address com.celizi.base.common.generator.Gen
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月14日 下午2:09:25
 * @version 1.0
 */
public class Gen {
	/** 配置文件名称 */
	public static final String CONFIGXMLNAME = "config.xml";

	public static void main(String[] args) {
		//配置文件路径
		String mavenDir =  (new File(Objects.requireNonNull(Gen.class.getResource("")).getPath()) +"\\").replaceAll("\\\\", "\\\\\\\\").replaceAll("target", "src").replaceAll("classes", "main\\\\\\\\java");
		// 多返回了包名
		String configFilePath = mavenDir+CONFIGXMLNAME;
		// 1.创建目录
		UtilXml.parseXml(configFilePath);
		Params params = UtilXml.params;

		// 2.生成文件
		List<Tables> tables = UtilXml.tableList;
		for (Tables table : tables) {
			UtilFile.initDirName(params,table);
			String project=Optional.ofNullable(table.getProject()).orElse(params.getProject());
			//去掉前缀（T_  或者  sys_ 等   T_User变成User  sys_user 变成User  T_Bill_Log 变成 BillLog）
			String javaClassName = UtilString.capitalize(UtilString.dbNameToVarName(table.getTableName().substring(table.getTableName().indexOf("_"))));
			String javaPath = params.getOsdir() + File.separatorChar + project + File.separatorChar;
			Map<Object, Object> map = UtilFreemarker.getTableInfo(table.getTableName());
			map.put("tableName", table.getTableName());
			map.put("author", params.getAuthor());
			map.put("email", params.getEmail());
			map.put("project", project);
			map.put("className", javaClassName);
			map.put("smallClassName", toLowerCaseFirstOne(javaClassName));
			map.put("voClassName", javaClassName);
			map.put("javapackage", params.getJavapackage());
			List<String> columnTypes = (List<String>)map.get("columnTypes");
			map.put("hasDate",CollUtil.contains(columnTypes,"Date"));
			map.put("hasBigDecimal",CollUtil.contains(columnTypes,"BigDecimal"));
			map.put("hasInteger",CollUtil.contains(columnTypes,"Integer"));

			//不存在  或者  包含1
			if(StringUtils.isBlank(table.getGenType())|| table.getGenType().contains(Tables.GENXML)) {
				// 1.xml
				String xmlName =  params.getXmlosdir()+ File.separatorChar +"mappings"+ File.separatorChar +project+ File.separatorChar + javaClassName + "Mapper.xml";
				System.out.println("输出文件："+map.get("title")+"的mapper.xml件");
				UtilFreemarker.generateFile(xmlName, "ftl/xml.ftl", map);
			}
			//不存在  或者  包含1
			if(StringUtils.isBlank(table.getGenType())|| table.getGenType().contains(Tables.GENENTITY)) {
				//2.entity
				String entityName = javaPath + "entity" + File.separatorChar + javaClassName + ".java";
				System.out.println("输出文件："+map.get("title")+"的实体类");
				UtilFreemarker.generateFile(entityName, "ftl/entity.ftl", map);
				String entityNameDto = javaPath + "dto" + File.separatorChar + javaClassName + "QueryDto.java";
				System.out.println("输出文件："+map.get("title")+"的实体类dto");
				UtilFreemarker.generateFile(entityNameDto, "ftl/dto.ftl", map);
				String entityNameVo = javaPath + "vo" + File.separatorChar + javaClassName + "Vo.java";
				System.out.println("输出文件："+map.get("title")+"的实体类Vo");
				UtilFreemarker.generateFile(entityNameVo, "ftl/vo.ftl", map);
			}
			//不存在  或者  包含3
			if(StringUtils.isBlank(table.getGenType())|| table.getGenType().contains(Tables.GENDAO)) {
				//3.dao
				String daoName = javaPath +  "mapper" + File.separatorChar + javaClassName + "Mapper.java";
				System.out.println("输出文件："+map.get("title")+"的dao类");
				UtilFreemarker.generateFile(daoName,  "ftl/dao.ftl", map);
			}
			//不存在  或者  包含4
			if(StringUtils.isBlank(table.getGenType())|| table.getGenType().contains(Tables.GENSERVICE)) {
				//4.service
				String serviceName = javaPath +  "service" + File.separatorChar + javaClassName + "Service.java";
				System.out.println("输出文件："+map.get("title")+"的service类");
				UtilFreemarker.generateFile(serviceName,  "ftl/service.ftl", map);

				//4.service2
				String serviceImplName = javaPath +  "service" + File.separatorChar+"impl"+ File.separatorChar+ javaClassName + "ServiceImpl.java";
				System.out.println("输出文件："+map.get("title")+"的serviceImpl类");
				UtilFreemarker.generateFile(serviceImplName,  "ftl/serviceImpl.ftl", map);
			}
			//不存在  或者  包含5
			if(StringUtils.isBlank(table.getGenType())|| table.getGenType().contains(Tables.GENCONTROLLER)) {
				//5.controller
				String controllerName = javaPath +  "controller" + File.separatorChar + javaClassName + "Controller.java";
				System.out.println("输出文件："+map.get("title")+"的控制器类");
				UtilFreemarker.generateFile(controllerName,  "ftl/controller.ftl", map);
			}
		}
	}


	/**
	 * 首字母 小写
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s){
		if(Character.isLowerCase(s.charAt(0))){
		    return s;
		} else {
			return Character.toLowerCase(s.charAt(0)) + s.substring(1);
		}
	}
}
