package com.base.sbc.module.sample.vo;


import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：样衣设计分页返回
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.SamplePageVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-10 11:54
 */
@Data
@ApiModel("样衣分页返回 SamplePageVo ")
public class SampleDesignPageVo {
    @ApiModelProperty(value = "编号")
    private String id;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String month;
    /**
     * 波段(编码)
     */
    @ApiModelProperty(value = "波段(编码)")
    private String bandCode;

    @ApiModelProperty(value = "波段(名称)")
    private String bandName;

    @ApiModelProperty(value = "设计师名称")
    private String designer;
    /**
     * 设计师id
     */
    @ApiModelProperty(value = "设计师id")
    private String designerId;

    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    /**
     * 旧设计款号
     */
    @ApiModelProperty(value = "旧设计款号")
    private String hisDesignNo;

    @ApiModelProperty(value = "状态:0未开款，1已开款，2已下发打板")
    private String status;

    @ApiModelProperty(value = "审核状态：草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)", example = "0")
    private String confirmStatus;
    @ApiModelProperty(value = "款式图" )
    private String stylePic;
    @ApiModelProperty(value = "设计师头像", example = "https://sjkj-demo.oss-cn-shenzhen.aliyuncs.com/null/userHead/09/02/8361ea39-21d2-4944-b150-d2e69bb68254.png")
    private String aliasUserAvatar;
    @ApiModelProperty(value = "款式名称" )
    private String styleName;
    @ApiModelProperty(value = "款式类型" )
    private String styleType;
    @ApiModelProperty(value = "生成类型" )
    private String devtType;
    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)"  )
    private String categoryName;
    /** 品类id路径:(大类/品类/中类/小类) */
    @ApiModelProperty(value = "品类id路径:(大类/品类/中类/小类)"  )
    private String categoryIds;
    /*创建人*/
    private String createName;
    /*款式配色*/
    private List<SampleStyleColorVo> sampleStyleColorVoList;
    /*轮廓*/
    private String silhouette;

    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;

    @ApiModelProperty(value = "波段企划id")
    private String planningBandId;
    /**
     * 品类信息id
     */
    @ApiModelProperty(value = "品类信息id")
    private String planningCategoryId;
    /**
     * 坑位信息id
     */
    @ApiModelProperty(value = "坑位信息id")
    private String planningCategoryItemId;

    @ApiModelProperty(value = "是否齐套:0未齐套，1已齐套")
    private String kitting;

    @ApiModelProperty(value = "任务等级:普通,紧急,非常紧急")
    private String taskLevel;

    @ApiModelProperty(value = "计划完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planningFinishDate;

    /**
     * 生产模式名称
     */
    @ApiModelProperty(value = "生产模式名称")
    private String devtTypeName;
    /**
     * 开发分类名称
     */
    @ApiModelProperty(value = "开发分类名称")
    private String devClassName;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "相差天数")
    public String getRemainingTime() {
        if (planningFinishDate == null) {
            return "";
        }
        Date now = new Date();
        String abs = DateUtil.compare(getPlanningFinishDate(), now) < 0 ? "-" : "";
        return abs + DateUtil.betweenDay(getPlanningFinishDate(), now, false);

    }
}
