package com.base.sbc.pdm.dao;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.pdm.entity.Material;
import com.base.sbc.pdm.entity.MaterialDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 卞康
 * @date 2023/3/24 17:48:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialAllDto extends MaterialDetails {
    private static final long serialVersionUID = 1L;


    /**
     * 所在部门
     */
    private String deptName;

    /**
     * 图片地址
     */
    private String pictureUrl;
    /**
     * 图片格式
     */
    private String pictureFormat;
    /**
     * 图片大小
     */
    private String pictureSize;
    /**
     * 素材文件名
     */
    private String materialName;
    /**
     * 素材编码
     */
    private String materialCode;
    /**
     * 所属素材库
     */
    private String materialLibrary;
    /** 所属素材库名称 */
    private String libraryName;

    /**
     * 审核状态（0：未审核，1：审核通过，2：审核不通过）
     */
    private String status;
    /**
     * 所属分类id
     */
    private String materialType;
    /** 所属分类名称 */
    private String typeName;
    /**
     * 年份
     */
    private String particularYear;
    /**
     * 月份
     */
    private String month;
    /**
     * 来源地
     */
    private String source;
    /**
     * 季节
     */
    private String season;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 品牌规模
     */
    private String brandScale;
    /**
     * 缺点
     */
    private String drawback;
    /**
     * 风险评估
     */
    private String assess;

    public Material toMaterial() {
        Material material = new Material();
        BeanUtil.copyProperties(this, material);
        return material;
    }

    public MaterialDetails toMaterialDetails() {
        MaterialDetails details = new MaterialDetails();
        BeanUtil.copyProperties(this, details);
        return details;
    }
}
