/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：产前样 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.PreProductionSample
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-11 16:02:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pre_production_sample")
@ApiModel("产前样 PreProductionSample")
public class PreProductionSample extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 资料包id
     */
    @ApiModelProperty(value = "资料包id")
    private String packInfoId;
    /**
     * 款式设计id
     */
    @ApiModelProperty(value = "款式设计id")
    private String styleId;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 坑位信息id
     */
    @ApiModelProperty(value = "坑位信息id")
    private String planningCategoryItemId;
    /**
     * 大类id
     */
    @ApiModelProperty(value = "大类id")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 中类id
     */
    @ApiModelProperty(value = "中类id")
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    /**
     * 小类
     */
    @ApiModelProperty(value = "小类")
    private String prodCategory3rd;
    /**
     * 小类名称
     */
    @ApiModelProperty(value = "小类名称")
    private String prodCategory3rdName;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 年份名称
     */
    @ApiModelProperty(value = "年份名称")
    private String yearName;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 季节名称
     */
    @ApiModelProperty(value = "季节名称")
    private String seasonName;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    /**
     * 波段(编码)
     */
    @ApiModelProperty(value = "波段(编码)")
    private String bandCode;
    /**
     * 波段(名称)
     */
    @ApiModelProperty(value = "波段(名称)")
    private String bandName;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String month;
    /**
     * 月份名称
     */
    @ApiModelProperty(value = "月份名称")
    private String monthName;
    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String stylePic;
    /**
     * 款式配色id
     */
    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;
    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    private String color;
    /**
     * 旧设计款号
     */
    @ApiModelProperty(value = "旧设计款号")
    private String hisDesignNo;
    /**
     * 款式名称
     */
    @ApiModelProperty(value = "款式名称")
    private String styleName;
    /**
     * 后技术备注说明
     */
    @ApiModelProperty(value = "后技术备注说明")
    private String techRemarks;
    /**
     * 是否齐套:0未齐套，1已齐套
     */
    @ApiModelProperty(value = "是否齐套:0未齐套，1已齐套")
    private String kitting;
    /**
     * 正确样是否接收:(0未接收,1已接收)
     */
    @ApiModelProperty(value = "正确样是否接收:(0未接收,1已接收)")
    private String correctSampleReceivedFlag;
    /**
     * 样衣是否接收:(0未接收,1已接收)
     */
    @ApiModelProperty(value = "样衣是否接收:(0未接收,1已接收)")
    private String receiveSample;
    /**
     * 设计下明细单时间
     */
    @ApiModelProperty(value = "设计下明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designDetailTime;
    /**
     * 计控接收明细单时间
     */
    @ApiModelProperty(value = "计控接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planningReceiveTime;
    /**
     * 工艺接收明细单时间
     */
    @ApiModelProperty(value = "工艺接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date techReceiveTime;
    /**
     * 大货接收明细单时间
     */
    @ApiModelProperty(value = "大货接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productionReceiveTime;
    /**
     * 品控接收明细单时间
     */
    @ApiModelProperty(value = "品控接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qcReceiveTime;
    /**
     * 面辅料信息
     */
    @ApiModelProperty(value = "面辅料信息")
    private String materialInfo;
    /**
     * 版师id
     */
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;
    /**
     * 版师名称
     */
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    /**
     * 设计师名称
     */
    @ApiModelProperty(value = "设计师名称")
    private String designer;
    /**
     * 设计师id
     */
    @ApiModelProperty(value = "设计师id")
    private String designerId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

