package com.base.sbc.config.generator.entity;

/** 
 * 类描述：
 * @address com.celizi.base.common.generator.entity.Params
 * @author shenzhixiong  
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月14日 下午1:57:21 
 * @version 1.0  
 */
public class Params {
	public String osdir;
	public String javapackage;
	public String author;
	public String project;
	public String email;
	public String xmlosdir;
	
	public String getXmlosdir() {
		return xmlosdir;
	}
	public void setXmlosdir(String xmlosdir) {
		this.xmlosdir = xmlosdir;
	}
	public String getOsdir() {
		return osdir;
	}
	public void setOsdir(String osdir) {
		this.osdir = osdir;
	}
	public String getJavapackage() {
		return javapackage;
	}
	public void setJavapackage(String javapackage) {
		this.javapackage = javapackage;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
