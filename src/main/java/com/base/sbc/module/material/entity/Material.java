package com.base.sbc.module.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 素材
 *
 * @author 卞康
 * @date 2023/4/3 11:03:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_material")
public class Material extends BaseDataEntity<String> {
    @ApiModelProperty(value = "素材编码")
    private String materialCode;

    @ApiModelProperty(value = "文件信息")
    private String fileInfo;

    @ApiModelProperty(value = "素材分类id")
    private String materialCategoryId;

    @ApiModelProperty(value = "素材分类名称")
    private String materialCategoryName;

    @ApiModelProperty(value = "素材细分类")
    private String materialSubcategory;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商编号")
    private String supplierCode;

    @ApiModelProperty(value = "供应商色号")
    private String supplierColor;

    @ApiModelProperty(value = "成分")
    private String component;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "市场")
    private String market;

    @ApiModelProperty(value = "市场等级")
    private String marketLevel;

    @ApiModelProperty(value = "知名度")
    private String fame;

    @ApiModelProperty(value = "品类名称")
    private String categoryName;

    @ApiModelProperty(value = "品类id")
    private String categoryId;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "季节")
    private String season;

    @ApiModelProperty(value = "来源地")
    private String sourcePlace;

    @ApiModelProperty(value = "来源人")
    private String sourcePerson;

    @ApiModelProperty(value = "来源部门")
    private String sourceDepartment;

    @ApiModelProperty(value = "采集时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date collectionTime;

    @ApiModelProperty(value = "素材缺点")
    private String materialDefect;

    @ApiModelProperty(value = "风险评估")
    private String riskAssessment;

    @ApiModelProperty(value = "审核状态（0：未提交，1：待审核，2：审核通过，3：审核不通过）")
    private String status;

    @ApiModelProperty(value = "图片地址")
    private String picUrl;

    @ApiModelProperty(value = "图片格式")
    private String picFormat;

    @ApiModelProperty(value = "图片尺寸")
    private String picSize;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "款号")
    private String styleNo;

    @ApiModelProperty(value = "版型名称")
    private String shapeName;

    @ApiModelProperty(value = "维度系数代码")
    private String dimensionCode;

    @ApiModelProperty(value = "维度数值")
    private String dimensionValue;

    @ApiModelProperty(value = "长度数值")
    private String lengthValue;

    @ApiModelProperty(value = "长度数值（后中）")
    private String lengthValuePM;

    @ApiModelProperty(value = "参考部位")
    private String referencePosition;

    @ApiModelProperty(value = "优质版型: 1:优质，0：")
    private String highQualityShape;

    @ApiModelProperty(value = "场合搭配code")
    private String occasionCode;

    @ApiModelProperty(value = "场合搭配")
    private String occasionName;

    @ApiModelProperty(value = "颜色搭配code")
    private String colorMatchCode;

    @ApiModelProperty(value = "颜色搭配")
    private String colorMatchName;

    @ApiModelProperty(value = "图案种类")
    private String patternTypes;

    @ApiModelProperty(value = "运用手法")
    private String useSkill;

    @ApiModelProperty(value = "购买人")
    private String purchaser;

}
