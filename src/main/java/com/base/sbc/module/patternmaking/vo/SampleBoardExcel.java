package com.base.sbc.module.patternmaking.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.BaseGlobal;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 样衣看板导出实体类
 */
@Data
public class SampleBoardExcel {

    @ApiModelProperty(value = "款图")
    @Excel(name = "款图", type = 2,  imageType = 2)
    private  byte[]  pic;

    private String  stylePic;
    /**
     * 产品季
     */
    @ApiModelProperty(value = "产品季")
    @Excel(name = "季节")
    private String productSeason;

    @ApiModelProperty(value = "设计款号")
    @Excel(name = "款式")
    private String designNo;

    @Excel(name = "波段")
    private String bandName;

    @Excel(name = "生产类型")
    private String devtTypeName;

    @ApiModelProperty(value = "样板号")
    @Excel(name = "样板号")
    private String patternNo;

    @ApiModelProperty(value = "样衣条码")
    @Excel(name = "样衣条码")
    private String sampleBarCode;

    @ApiModelProperty(value = "确认收到样衣时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "设计收到时间" , exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date receiveSampleDate;

    @ApiModelProperty(value = "打版类型")
    @Excel(name = "打版类型")
    private String sampleTypeName;

    @ApiModelProperty(value = "打版难度")
    @Excel(name = "打版难度")
    private String patDiffName;

    @ApiModelProperty(value = "打样顺序")
    @Excel(name = "打样顺序")
    private String patSeqName;

    /**
     * 打样设计师
     */
    @ApiModelProperty(value = "打样设计师")
    @Excel(name = "打样设计师")
    private String patternDesignerName;

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
    @ApiModelProperty(value = "纸样需求完成日期")
    @Excel(name = "纸样需求完成日期",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date patternReqDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "样衣需求完成日期")
    @Excel(name = "样衣需求完成日期",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date demandFinishDate;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "技术收到日期")
    @Excel(name = "技术收到日期",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date jssdrq;

    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    private String shortageRemarks;

    /**
     * 改版意见
     */
    @ApiModelProperty(value = "改版意见")
    @Excel(name = "改版意见",width = 20)
    private String revisionComments;

    /**
     * 改版原因
     */
    @ApiModelProperty(value = "改版原因")
    @Excel(name = "改版原因")
    private String revisionReason;

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
    @Excel(name = "需求数",type = 10)
    private BigDecimal requirementNum;

    /**
     * 是否参考样衣
     */
    @ApiModelProperty(value = "是否参考样衣")
    @Excel(name = "是否参考样衣",type = 11)
    private String referSample;



    @ApiModelProperty(value = "纸样完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "纸样完成时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date zywcsj;

    @Excel(name = "延迟打版原因")
    private String suspendRemarks;

    /**
     * 版师工作量评分
     */
    @ApiModelProperty(value = "版师工作量评分")
    @Excel(name = "版师工作量评分",type = 10)
    private BigDecimal patternMakingScore;

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

    @ApiModelProperty(value = "裁剪完成数量")
    @Excel(name = "裁剪完成数量",type = 10)
    private BigDecimal cutterFinishNum;

    /**
     * 样衣组长确认齐套
     */
    @ApiModelProperty(value = "样衣组长确认齐套")
    @Excel(name = "样衣组长确认齐套", replace = {"是_1", "否_0"})
    private String sglKitting;

    @ApiModelProperty(value = "备注")
    private String stitcherRemark;

    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    @Excel(name = "车缝工名称")
    private String stitcher;

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

    /**
     * 设计工艺员名称
     */
    @ApiModelProperty(value = "设计工艺员名称")
    @Excel(name = "工艺员")
    private String technicianName;

    /**
     * 打样工艺员名称
     */
    @ApiModelProperty(value = "打样工艺员名称")
    @Excel(name = "打样工艺员")
    private String patternTechnicianName;

    @ApiModelProperty(value = "设计师名称"  )
    @Excel(name = "设计师")
    private String designer;

    @ApiModelProperty(value = "跟款设计师")
    @Excel(name = "跟款设计师")
    private String merchDesignName;

    @ApiModelProperty(value = "改款设计师")
    @Excel(name = "改款设计师")
    private String revisedDesignName;

    @ApiModelProperty(value = "审版设计师")
    @Excel(name = "审版设计师")
    private String      reviewedDesignName;

    @Excel(name = "品类")
    private String prodCategoryName;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    @Excel(name = "中类")
    private String prodCategory2ndName;

    /**
     * 小类名称
     */
    @ApiModelProperty(value = "小类名称")
    @Excel(name = "小类")
    private String prodCategory3rdName;


    @ApiModelProperty(value = "纸样完成数量")
    @Excel(name = "纸样完成数量",type = 10)
    private BigDecimal patternFinishNum;

    @ApiModelProperty(value = "尺码")
    @Excel(name = "尺码")
    private String  defaultSize;


    @Excel(name = "创建指令时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date      pmCreateDate;

    @Excel(name = "部件编码")
    private String  patternParts;

    @Excel(name = "完成件数",type = 10)
    private BigDecimal sampleFinishNum;

    @Excel(name = "样衣工作量评分",type = 10)
    private BigDecimal sampleMakingScore;

    @Excel(name = "样衣工质量评分",type = 10)
    private BigDecimal sampleMakingQualityScore;



    /**
     * 二次加工(0否，1是)
     */
    @ApiModelProperty(value = "二次加工(0否，1是)")
    @Excel(name = "二次加工", replace = { "_null"})
    private String secondProcessing;

    @ApiModelProperty(value = "打版任务当前状态")
    @Excel(name = "状态")
    private String pmStatus;

    @Excel(name = "下发给样衣组长时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date  prmSendDate;

    @Excel(name = "下发给样衣组长状态", replace = {"已发送_1", "未发送_0"})
    private String prmSendStatus;

    @Excel(name = "放码师")
    private String gradingName;

    @Excel(name = "放码时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date  gradingDate;








    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;

    @ApiModelProperty(value = "品牌")
    private String brandName;

    @ApiModelProperty(value = "年份")
    private String yearName;

    private String  breakOffPattern;

    private String  breakOffSample;

    public String getProductSeason() {
        return getSeason();
    }

    public String  getPmStatus() {
        return StrUtil.equals(getBreakOffPattern(), BaseGlobal.YES)?"打板中断":
             StrUtil.equals(getBreakOffSample(), BaseGlobal.YES)?"样衣中断":"启用";
    }

}
