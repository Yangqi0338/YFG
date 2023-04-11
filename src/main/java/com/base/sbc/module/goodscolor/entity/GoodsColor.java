/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.goodscolor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：物料颜色 实体类
 * @address com.base.sbc.baseData.entity.GoodsColor
 * @author gcc
 * @email gcc@bestgcc.cn
 * @date 创建时间：2021-4-25 9:05:01
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_goods_color")
public class GoodsColor extends BaseDataEntity<String>{

	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

	/**
	 * 保存基础信息
	 * @param userId
	 * @param userName
	 * @param delFlag
	 */
	public void setEntityBasicInfo(String userId, String userName, String delFlag) {
		Date now = new Date();
		this.createId = userId;
		this.createName = userName;
		this.createDate = now;
		this.updateId = userId;
		this.updateName = userName;
		this.updateDate = now;
		if(delFlag == null || "".equals(delFlag)){
			this.delFlag = "0";
		}else {
			this.delFlag = delFlag;
		}
	}
	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 排序 */
    private Integer sort;
    /** 状态(0正常,1停用) */
    private String status;
    /** 颜色分类id */
    private String colorTypeId;
    /** 颜色十六进制 */
    private String colorHex;
    /** 颜色RGB */
    private String colorRgb;
    /** 颜色编码 */
    private String colorCode;
    /** 颜色 */
    private String color;
    /** 颜色分类 */
    private String colorClassify;
    /** pantone色号 */
    private String pantoneCode;
    /** pantone名称 */
    private String pantoneName;
}
