package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.module.style.entity.Style;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 类描述：样衣看板
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.SampleBoardVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-15 10:12
 */
@Data
@ApiModel("样衣看板vo SampleBoardVo ")
public class SampleBoardVo extends Style {
    @ApiModelProperty(value = "数据id")
    private String id;
    @ApiModelProperty(value = "打版id")
    private String patternMakingId;
    @ApiModelProperty(value = "款式设计id")
    private String styleId;

    /**
     * 大类code
     */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    /**
     * 品类code
     */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    /**
     * 小类code
     */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;
    /**
     * 小类名称
     */
    @ApiModelProperty(value = "小类名称")
    private String prodCategory3rdName;

    @ApiModelProperty(value = "款图")
    private String stylePic;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "品牌")
    private String brandName;
    @ApiModelProperty(value = "模板部件")
    private String patternParts;

    @ApiModelProperty(value = "年份")
    private String year;
    @ApiModelProperty(value = "年份")
    private String yearName;
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

    @ApiModelProperty(value = "款式风格")
    private String style;

    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "样板号")
    private String patternNo;

    @ApiModelProperty(value = "款式(大货款号)")
    private String styleNo;


    @ApiModelProperty(value = "打版类型")
    private String sampleType;

    @ApiModelProperty(value = "打版难度")
    private String patDiff;

    @ApiModelProperty(value = "打版难度")
    private String patDiffName;

    @ApiModelProperty(value = "打样顺序")
    private String patSeq;

    @ApiModelProperty(value = "打样顺序")
    private String patSeqName;

    @ApiModelProperty(value = "工艺员确认齐套")
    private String technicianKitting;
    /**
     * 工艺员确认齐套时间
     */
    @ApiModelProperty(value = "工艺员确认齐套时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technicianKittingDate;
    /**
     * 样衣组长确认齐套
     */
    @ApiModelProperty(value = "样衣组长确认齐套")
    private String sglKitting;
    /**
     * 样衣组长确认齐套时间
     */
    @ApiModelProperty(value = "样衣组长确认齐套时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sglKittingDate;

    @ApiModelProperty(value = "创建指令时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date pmCreateDate;

    /**
     * 版师名称
     */
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    /**
     * 版师id
     */
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;

    @ApiModelProperty(value = "需求数")
    private BigDecimal requirementNum;

    @ApiModelProperty(value = "纸样完成数量")
    private BigDecimal patternFinishNum;

    @ApiModelProperty(value = "裁剪工")
    private String cutterName;
    @ApiModelProperty(value = "裁剪工id")
    private String cutterId;
    @ApiModelProperty(value = "裁剪完成数量")
    private BigDecimal cutterFinishNum;

    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    private String stitcher;
    /**
     * 车缝工id
     */
    @ApiModelProperty(value = "车缝工id")
    private String stitcherId;


    /**
     * 样衣工工作量评分
     */
    @ApiModelProperty(value = "样衣工工作量评分")
    private BigDecimal sampleMakingScore;
    /**
     * 样衣工质量评分
     */
    @ApiModelProperty(value = "样衣工质量评分")
    private BigDecimal sampleMakingQualityScore;
    /**
     * 版师工作量评分
     */
    @ApiModelProperty(value = "版师工作量评分")
    private BigDecimal patternMakingScore;
    /**
     * 版师质量评分
     */
    @ApiModelProperty(value = "版师质量评分")
    private BigDecimal patternMakingQualityScore;

    /**
     * 打板备注
     */
    @ApiModelProperty(value = "打板备注")
    private String shortageRemarks;

    /**
     * 改版原因
     */
    @ApiModelProperty(value = "改版原因")
    private String revisionReason;
    /**
     * 改版意见
     */
    @ApiModelProperty(value = "改版意见")
    private String revisionComments;

    /**
     * 二次加工(0否，1是)
     */
    @ApiModelProperty(value = "二次加工(0否，1是)")
    private String secondProcessing;

    @ApiModelProperty(value = "确认收到样衣时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveSampleDate;
    /**
     * 确认收到样衣(0否,1是)
     */
    @ApiModelProperty(value = "确认收到样衣(0否,1是)")
    private String receiveSample;
    @ApiModelProperty(value = "节点状态")
    private Map<String, NodeStatusVo> nodeStatus;

    @ApiModelProperty(value = "样衣条码")
    private String sampleBarCode;

    @ApiModelProperty(value = "样衣图")
    private String samplePic;
    @ApiModelProperty(value = "挂起备注", example = "")
    private String suspendRemarks;


    @ApiModelProperty(value = "中断打版(0正常，1中断)")
    private String breakOffPattern;
    @ApiModelProperty(value = "中断样衣(0正常，1中断)")
    private String breakOffSample;
    @ApiModelProperty(value = "样衣完成状态:(0未完成,1完成)")
    private String sampleCompleteFlag;
    @ApiModelProperty(value = "版房主管下发状态:(0未下发，1已下发)")
    private String prmSendStatus;
    @ApiModelProperty(value = "版房主管下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date prmSendDate;
    @ApiModelProperty(value = "设计下发状态:(0未下发，1已下发)")
    private String designSendStatus;
    @ApiModelProperty(value = "设计下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designSendDate;
    @ApiModelProperty(value = "打版状态:0待接收,1已接收,2进行中,3完成")
    private String patternStatus;
    @ApiModelProperty(value = "裁剪状态:0待接收,1已接收,2进行中,3完成")
    private String cuttingStatus;
    @ApiModelProperty(value = "车缝状态:0待接收,1已接收,2进行中,3完成")
    private String sewingStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "纸样需求完成日期")
    private Date patternReqDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "样衣需求完成日期")
    private Date demandFinishDate;
    /**
     * 放码师id
     */
    @ApiModelProperty(value = "放码师id")
    private String gradingId;
    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
    private String gradingName;

    /**
     * 打样工艺员id
     */
    @ApiModelProperty(value = "打样工艺员id")
    private String patternTechnicianId;

    /**
     * 打样工艺员名称
     */
    @ApiModelProperty(value = "打样工艺员名称")
    private String patternTechnicianName;

    /**
     * 放码时间
     */
    @ApiModelProperty(value = "放码时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gradingDate;

    /**
     * 完成数量
     */
    @ApiModelProperty(value = "完成数量")
    private BigDecimal sampleFinishNum;
    @ApiModelProperty(value = "分配车缝工备注")
    private String stitcherRemark;


    @ApiModelProperty(value = "紧急程度")
    private String urgency;

    @ApiModelProperty(value = "紧急程度名称")
    private String urgencyName;

    @ApiModelProperty(value = "当前节点")
    private String node;

    @ApiModelProperty(value = "状态")
    private String  pmStatus;


    @ApiModelProperty(value = "当前状态")
    private String pStatus;

    private String patternDesignerName;

    /**
     * 是否参考样衣
     */
    @ApiModelProperty(value = "是否参考样衣")
    private String referSample;
    /**
     * 齐套否 原因
     */
    private String kittingReason;
    /**
     * 齐套否 原因名称
     */
    private String kittingReasonName;

    public String getId() {
        return patternMakingId;
    }

    /**
     * 列头筛选数量
     */
    private Integer groupCount;

    @ApiModelProperty(value = "供应商Id")
    private String supplierId;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    private String supplierAbbreviation;
    private String img;
    private String barCode;
    private String code;
}
