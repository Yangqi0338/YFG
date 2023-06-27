package com.base.sbc.config.generator.utils;

import com.base.sbc.config.generator.entity.Params;
import com.base.sbc.config.generator.entity.Tables;

import java.io.File;
import java.util.Optional;

/** 
 * 类描述：
 * @address com.celizi.base.common.generator.utils.UtilFile
 * @author shenzhixiong  
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月14日 下午1:57:37 
 * @version 1.0  
 */
public class UtilFile {
	public static void createDir(String path) {
		if (null != path && !"".equals(path)) {
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}

		}
	}

	public static void initDirName(Params params, Tables table) {
		String project= Optional.ofNullable(table.getProject()).orElse(params.getProject());
		// 1.xml
		String xmlDir = params.getXmlosdir() + File.separatorChar + "mappings" + File.separatorChar
				+ project;
		createDir(xmlDir);
		// 2.entity
		String entityDir = params.getOsdir() + File.separatorChar + project + File.separatorChar + "entity";
		createDir(entityDir);
		// 3.dao
		String daoDir = params.getOsdir() + File.separatorChar + project + File.separatorChar + "mapper";
		createDir(daoDir);
		// 4.service
		String serviceDir = params.getOsdir() + File.separatorChar + project + File.separatorChar
				+ "service";
		createDir(serviceDir);
		// 4.2serviceImpl
		String serviceImplDir = params.getOsdir() + File.separatorChar + project + File.separatorChar
				+ "service"+File.separator+"impl";
		createDir(serviceImplDir);
		// 5.controller
		String controllerDir = params.getOsdir() + File.separatorChar + project + File.separatorChar
				+ "controller";
		createDir(controllerDir);
	}
}
