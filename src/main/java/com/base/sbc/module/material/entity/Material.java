package com.base.sbc.module.material.entity;

import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;

/**
 * 素材
 *
 * @author 卞康
 * @date 2023/4/3 11:03:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Material extends BaseDataEntity<String> {

    /**
     * 文件信息
     */
    private String fileInfo;

    /**
     * 素材分类id
     */
    private String materialCategoryId;

    /**
     * 素材分类名称
     */
    private String materialCategoryName;

    /**
     * 素材细分类
     */
    private String materialSubcategory;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 供应商色号
     */
    private String supplierColor;

    /**
     * 成分
     */
    private String component;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 市场
     */
    private String market;

    /**
     * 市场等级
     */
    private String marketLevel;

    /**
     * 知名度
     */
    private String fame;

    /**
     * 品类名称
     */
    private String categoryName;

    /**
     * 品类id
     */
    private Integer categoryId;

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 季节
     */
    private String season;

    /**
     * 来源地
     */
    private String sourcePlace;

    /**
     * 来源人
     */
    private String sourcePerson;

    /**
     * 来源部门
     */
    private String sourceDepartment;

    /**
     * 采集时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date collectionTime;

    /**
     * 素材缺点
     */
    private String materialDefect;

    /**
     * 风险评估
     */
    private String riskAssessment;

    /**
     * 审核状态（0：未提交，1：待审核，2：审核通过，3：审核不通过）
     */
    private String status;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 图片尺寸
     */
    private String picSize;

    /**
     * 备注
     */
    private String remarks;
}
