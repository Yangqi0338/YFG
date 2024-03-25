package com.base.sbc.module.sample.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.base.sbc.module.patternmaking.vo.NodeStatusVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 导出 产前样看板
 */
@Data
public class PreProductionSampleTaskVoExcel {

    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开")
    @Excel(name = "SCM下发状态", replace = {"重新打开_3","发送失败_2","发送成功_1", "未发送_0"})
    private String scmSendFlag;

    @ApiModelProperty(value = "设计款号")
    @Excel(name = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "大货款号")
    @Excel(name = "大货款号")
    private String styleNo;

    @ApiModelProperty(value = "款式图")
    private String stylePic;

    @Excel(name = "款图", type = 2,  imageType = 2)
    private  byte[]  pic;

    @Excel(name = "样衣图", type = 2)
    private String   samplePic;

    @ApiModelProperty(value = "配色")
    @Excel(name = "配色")
    private String color;

    @ApiModelProperty(value = "品类")
    @Excel(name = "品类")
    private String prodCategoryName;

    /**
     * 工艺师名称
     */
    @ApiModelProperty(value = "工艺师名称")
    @Excel(name = "工艺师名称")
    private String technologistName;

    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
    @Excel(name = "放码师名称")
    private String gradingName;

    /**
     * 放码日期
     */
    @ApiModelProperty(value = "放码日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "放码日期",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date gradingDate;

    /**
     * 裁剪工
     */
    @ApiModelProperty(value = "裁剪工")
    @Excel(name = "裁剪工")
    private String cutterName;


    /**
     * 裁剪开始时间
     */
    @ApiModelProperty(value = "裁剪开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "裁剪开始时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date cutterStartTime;


    /**
     * 裁剪开始时间
     */
    @ApiModelProperty(value = "裁剪结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "裁剪结束时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date cutterEndTime;

    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    @Excel(name = "车缝工名称")
    private String stitcher;

    /**
     * 车缝开始时间
     */
    @ApiModelProperty(value = "车缝开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "车缝开始时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date stitchStartTime;

    /**
     * 车缝结束时间
     */
    @ApiModelProperty(value = "车缝结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "车缝结束时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date stitchEndTime;

    /**
     * 样衣工作量评分
     */
    @ApiModelProperty(value = "样衣工作量评分")
    @Excel(name = "样衣工作量评分", type = 10)
    private BigDecimal sampleMakingScore;

    /**
     * 样衣质量评分
     */
    @ApiModelProperty(value = "样衣质量评分")
    @Excel(name = "样衣质量评分", type = 10)
    private BigDecimal sampleQualityScore;

    /**
     * 样衣条码
     */
    @ApiModelProperty(value = "样衣条码")
    @Excel(name = "样衣条码")
    private String sampleBarCode;

    /**
     * 样衣是否完成:(0否，1是)
     */
    @ApiModelProperty(value = "样衣是否完成:(0否，1是)")
    @Excel(name = "样衣是否完成", replace = {"是_1", "否_0"})
    private String sampleCompleteFlag;

    /**
     * 工艺单完成日期
     */
    @ApiModelProperty(value = "工艺单完成日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "工艺单完成日期",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date processCompletionDate;

    /**
     * 启用状态:1启用,0未启用
     */
    @ApiModelProperty(value = "启用状态:1启用,0未启用")
    @Excel(name = "启用状态", replace = {"启用_1", "未启用_0"})
    private String enableFlag;

    /**
     * 更新者名称
     */
    @Excel(name = "修改人")
    private String updateName;

    /**
     * 版房
     */
    @ApiModelProperty(value = "版房")
    @Excel(name = "版房")
    private String patternRoom;

    @ApiModelProperty(value = "是否是正确产前样：0否,1是")
    @Excel(name = "是否是正确产前样", replace = {"是_1", "否_0"})
    private String correctSampleFlag;

    /**
     * 需求数
     */
    @ApiModelProperty(value = "需求数")
    @Excel(name = "需求数")
    private BigDecimal requirementNum;

    @ApiModelProperty(value = "打样顺序")
    @Excel(name = "打样顺序")
    private String patSeqName;

    @ApiModelProperty(value = "品牌名称")
    @Excel(name = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "年份")
    @Excel(name = "年份")
    private String yearName;

    @ApiModelProperty(value = "季节")
    @Excel(name = "季节")
    private String seasonName;

    @ApiModelProperty(value = "月份")
    @Excel(name = "月份")
    private String monthName;


    @ApiModelProperty(value = "波段")
    @Excel(name = "波段")
    private String bandName;

    /**
     * 后技术备注说明
     */
    @ApiModelProperty(value = "后技术备注说明")
    @Excel(name = "后技术备注说明")
    private String techRemarks;

    /**
     * 是否齐套:0未齐套，1已齐套
     */
    @ApiModelProperty(value = "是否齐套:0未齐套，1已齐套")
    @Excel(name = "是否齐套", replace = {"已齐套_1", "未齐套_0"})
    private String kitting;

    /**
     * 正确样是否接收:(0未接收,1已接收)
     */
    @ApiModelProperty(value = "正确样是否接收:(0未接收,1已接收)")
    @Excel(name = "正确样是否接收", replace = {"已接收_1", "未接收_0"})
    private String correctSampleReceivedFlag;

    /**
     * 样衣是否接收:(0未接收,1已接收)
     */
    @ApiModelProperty(value = "样衣是否接收:(0未接收,1已接收)")
    @Excel(name = "样衣是否接收", replace = {"已接收_1", "未接收_0"})
    private String receiveSample;

    @ApiModelProperty(value = "版师名称")
    @Excel(name = "版师名称")
    private String patternDesignName;


    @ApiModelProperty(value = "设计下明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "设计下明细单时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date designDetailTime;

    @ApiModelProperty(value = "计控接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "计控接收明细单时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planningReceiveTime;

    @ApiModelProperty(value = "工艺接收明细单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "工艺接收明细单时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date techReceiveTime;

    @ApiModelProperty(value = "技术下工艺部正确样")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "技术下工艺部正确样",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date processDepartmentDate;

    /**
     * 查版日期
     */
    @ApiModelProperty(value = "查版日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "设计查版日期",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sampleChaBanData;

    /**
     * 工艺部接收正确样时间
     */
    @ApiModelProperty(value = "工艺部接收正确样时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "工艺部接收正确样时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date techReceiveDate;

    /**
     * 工艺部接收正确样时间
     */
    @ApiModelProperty(value = "面料检测单日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "面料检测单日期",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date materialCheckDate;

    /**
     * 面辅料信息
     */
    @ApiModelProperty(value = "面辅料信息")
    @Excel(name = "面辅料信息")
    private String materialInfo;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人")
    private String createName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "创建时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ApiModelProperty(value = "节点状态")
    private Map<String, NodeStatusVo> nodeStatus;

    /*样衣完成时间*/
    public Date getCutterStartTime() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("产前样衣任务-裁剪开始")).map(NodeStatusVo::getStartDate).orElse(null);
    }

    /*样衣完成时间*/
    public Date getCutterEndTime() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("产前样衣任务-裁剪完成")).map(NodeStatusVo::getStartDate).orElse(null);
    }

    /*样衣完成时间*/
    public Date getStitchStartTime() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("产前样衣任务-车缝进行中")).map(NodeStatusVo::getStartDate).orElse(null);
    }


    /*样衣完成时间*/
    public Date getStitchEndTime() {
        if (nodeStatus == null) {
            return null;
        }
        return Optional.ofNullable(nodeStatus.get("产前样衣任务-车缝完成")).map(NodeStatusVo::getStartDate).orElse(null);
    }





}
