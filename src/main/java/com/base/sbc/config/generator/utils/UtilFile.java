package com.base.sbc.config.generator.utils;

import java.io.File;

import com.base.sbc.config.generator.entity.Params;

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
			file.mkdirs();
		}
	}

	public static void initDirName(Params params) {
		// 1.xml
		String xmlDir = params.getXmlosdir() + File.separatorChar + "mappings" + File.separatorChar
				+ params.getProject();
		createDir(xmlDir);
		// 2.entity
		String entityDir = params.getOsdir() + File.separatorChar + params.getProject() + File.separatorChar + "entity";
		createDir(entityDir);
		// 3.dao
		String daoDir = params.getOsdir() + File.separatorChar + params.getProject() + File.separatorChar + "mapper";
		createDir(daoDir);
		// 4.service
		String serviceDir = params.getOsdir() + File.separatorChar + params.getProject() + File.separatorChar
				+ "service";
		createDir(serviceDir);
		// 4.2serviceImpl
		String serviceImplDir = params.getOsdir() + File.separatorChar + params.getProject() + File.separatorChar
				+ "service"+File.separator+"impl";
		createDir(serviceImplDir);
		// 5.controller
		String controllerDir = params.getOsdir() + File.separatorChar + params.getProject() + File.separatorChar
				+ "controller";
		createDir(controllerDir);
	}
}
