/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.material.entity;

import java.util.List;

import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：素材库 实体类
 * @address com.base.sbc.module.material.entity.Material
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-24 16:26:15
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Material extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    /** 所在部门 */
    private String deptName;
    /**
     * 收藏id
     */
    public String collectId;

    /**
     * 所有标签
     */
    public List<MaterialLabel> labels;

    /**
     * 所有尺码
     */
    private List<MaterialSize> sizes;

    /**
     * 所有颜色
     */
    private List<MaterialColor> colors;

    /**
     * ids 查询的id集合
     */
    public List<String> ids;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 图片地址 */
    private String pictureUrl;
    /** 图片格式 */
    private String pictureFormat;
    /** 图片大小 */
    private String pictureSize;
    /** 素材文件名 */
    private String materialName;
    /** 素材编码 */
    private String materialCode;
    /** 所属素材库id */
    private String materialLibrary;
    /** 所属素材库名称 */
    private String libraryName;
    /** 审核状态（0：未提交，1：待审核，2：审核通过，3：审核不通过） */
    private String status;
    /** 所属分类id */
    private String materialType;
    /** 所属分类名称 */
    private String typeName;
    /** 年份 */
    private String particularYear;
    /** 月份 */
    private String month;
    /** 来源地 */
    private String source;
    /** 季节 */
    private String season;
    /** 品牌 */
    private String brand;
    /** 品牌规模 */
    private String brandScale;
    /** 缺点 */
    private String drawback;
    /** 风险评估 */
    private String assess;
    /*******************************************getset方法区************************************/

}

