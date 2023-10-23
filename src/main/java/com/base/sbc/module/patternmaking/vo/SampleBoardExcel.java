package com.base.sbc.module.patternmaking.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 样衣看板导出实体类
 */
@Data
public class SampleBoardExcel {


    /**
     * 产品季
     */
    @ApiModelProperty(value = "产品季")
    @Excel(name = "季节")
    private String productSeason;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;

    @ApiModelProperty(value = "品牌")
    private String brandName;

    @ApiModelProperty(value = "年份")
    private String yearName;

    @ApiModelProperty(value = "样板号")
    @Excel(name = "样板号")
    private String patternNo;

    /**
     * 改版原因
     */
    @ApiModelProperty(value = "改版原因")
    @Excel(name = "改版原因")
    private String revisionReason;
    /**
     * 改版意见
     */
    @ApiModelProperty(value = "改版意见")
    @Excel(name = "改版意见")
    private String revisionComments;

    @ApiModelProperty(value = "款图")
    @Excel(name = "款图", type = 2, imageType = 1)
    private String stylePic;

    @ApiModelProperty(value = "设计款号")
    @Excel(name = "款式")
    private String designNo;

    @ApiModelProperty(value = "样衣条码")
    @Excel(name = "样衣条码")
    private String sampleBarCode;

    @ApiModelProperty(value = "打版类型")
    @Excel(name = "打版类型")
    private String sampleType;

    @ApiModelProperty(value = "打版难度")
    @Excel(name = "打版难度")
    private String patDiffName;

    @ApiModelProperty(value = "打样顺序")
    @Excel(name = "打样顺序")
    private String patSeqName;

    @ApiModelProperty(value = "工艺员确认齐套")
    @Excel(name = "工艺员确认齐套", replace = {"是_1", "否_0"})
    private String technicianKitting;

    /**
     * 工艺员确认齐套时间
     */
    @ApiModelProperty(value = "工艺员确认齐套时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "设计确认齐套时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date technicianKittingDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "版房主管下发时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date bfzgxfsj;

    /**
     * 版师名称
     */
    @ApiModelProperty(value = "版师名称")
    @Excel(name = "版师")
    private String patternDesignName;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "版师接收时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date bsjssj;


    @ApiModelProperty(value = "需求数")
    @Excel(name = "需求数")
    private BigDecimal requirementNum;

    @ApiModelProperty(value = "纸样完成数量")
    @Excel(name = "纸样完成数量")
    private BigDecimal patternFinishNum;

    @ApiModelProperty(value = "纸样完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "纸样完成时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date zywcsj;

    @ApiModelProperty(value = "裁剪工")
    @Excel(name = "裁剪工")
    private String cutterName;

    @ApiModelProperty(value = "裁剪开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "裁剪开始时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date cjkssj;

    @ApiModelProperty(value = "裁剪完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "裁剪完成时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date cjwcsj;


    @ApiModelProperty(value = "车缝开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "车缝开始时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date cfkssj;


    @ApiModelProperty(value = "车缝完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "车缝完成时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date cfwcsj;


    @ApiModelProperty(value = "样衣完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "样衣完成时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date yywcsj;

    @ApiModelProperty(value = "裁剪完成数量")
    @Excel(name = "裁剪完成数量")
    private BigDecimal cutterFinishNum;
    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    @Excel(name = "车缝工名称")
    private String stitcher;

    /**
     * 版师工作量评分
     */
    @ApiModelProperty(value = "版师工作量评分")
    @Excel(name = "版师工作量评分")
    private BigDecimal patternMakingScore;

    /**
     * 版师质量评分
     */
    @ApiModelProperty(value = "版师质量评分")
    @Excel(name = "版师工作量评分")
    private BigDecimal patternMakingQualityScore;
    /**
     * 样衣工工作量评分
     */
    @ApiModelProperty(value = "样衣工工作量评分")
    @Excel(name = "样衣工工作量评分")
    private BigDecimal sampleMakingScore;
    /**
     * 样衣工质量评分
     */
    @ApiModelProperty(value = "样衣工质量评分")
    @Excel(name = "样衣工质量评分")
    private BigDecimal sampleMakingQualityScore;

    /**
     * 二次加工(0否，1是)
     */
    @ApiModelProperty(value = "二次加工(0否，1是)")
    @Excel(name = "二次加工", replace = {"是_1", "否_0"})
    private String secondProcessing;

    @ApiModelProperty(value = "确认收到样衣时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "确认收到样衣时间" ,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date receiveSampleDate;

    @ApiModelProperty(value = "节点状态")
    private Map<String, NodeStatusVo> nodeStatus;

    public String getProductSeason() {
        return getYearName() + " " + getSeason() + " " + getBrandName();
    }

    /*版房主管下发时间*/
    public Date getBfzgxfsj() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("技术中心-版房主管下发")).map(NodeStatusVo::getStartDate).orElse(null);
    }

    /*版师接收时间*/
    public Date getBsjssj() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("打版任务-已接收")).map(NodeStatusVo::getStartDate).orElse(null);
    }

    /*纸样完成时间*/
    public Date getZywcsj() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("打版任务-打版完成")).map(NodeStatusVo::getStartDate).orElse(null);
    }

    /*裁剪开始时间*/

    public Date getCjkssj() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("样衣任务-裁剪开始")).map(NodeStatusVo::getStartDate).orElse(null);
    }

    /*裁剪完成时间*/
    public Date getCjwcsj() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("样衣任务-裁剪完成")).map(NodeStatusVo::getStartDate).orElse(null);
    }

    /*车缝开始时间*/
    public Date getCfkssj() {
        if (nodeStatus == null) {
            return null;
        }
        return  Optional.ofNullable(nodeStatus.get("样衣任务-车缝进行中")).map(NodeStatusVo::getStartDate).orElse(null);
    }

/*车缝完成时间*/
    public Date getCfwcsj() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("样衣任务-车缝完成")).map(NodeStatusVo::getStartDate).orElse(null);
    }
    /*样衣完成时间*/
    public Date getYywcsj() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("样衣任务-样衣完成")).map(NodeStatusVo::getStartDate).orElse(null);
    }
}
