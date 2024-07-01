package com.base.sbc.module.patternmaking.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：打版-样衣进度列表搜索条件Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingCommonPageSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-12 11:26
 */
@Data
@ApiModel("打版管理通用分页筛选类Dto PatternMakingCommonPageSearchDto ")
public class PatternMakingCommonPageSearchDto extends QueryFieldDto {
    @ApiModelProperty(value = "关键字筛选", example = "1")
    private String search;
    @ApiModelProperty(value = "年份", example = "2022")
    private String year;

    @ApiModelProperty(value = "季节", example = "S")
    @TableField(value = "CONCAT(s.year_name,s.season_name,s.brand_name)")
    private String season;

    @ApiModelProperty(value = "月份", example = "1")
    private String month;
    @ApiModelProperty(value = "波段", example = "1")
    @TableField(value = "band_code")
    private String bandCode;

    @ApiModelProperty(value = "设计师id", example = "1223333122223333333")
    private String designerId;
    @ApiModelProperty(value = "设计师ids", example = "1223333122223333333")
    private String designerIds;

    @ApiModelProperty(value = "版师id", example = "1223333122223333333")
    private String patternDesignId;

    @ApiModelProperty(value = "状态", example = "打版中")
    private String status;
    @ApiModelProperty(value = "产品季id", example = "1223333122223333333")
    private String planningSeasonId;

    @ApiModelProperty(value = "导出标记")
    private String deriveflag;


    //private String patternNo;

    //private String sampleType;

    //private String technicianKittingDate;

    //private String bfzgxfsj;

    //private String bsjssj;

    private String zysj;

    private String cjsj;

    private String cfsj;

    //private String yywcsj;
    @TableField(value = "sample_bar_code")
    private String sampleBarCode;
    //模板部件
    @TableField(value = "pattern_parts", property = "replace")
    private String patternParts;

    @ApiModelProperty(value = "创建指令时间")
    @TableField(value = "p.create_date", property = "date")
    private String pmCreateDate;
    @ApiModelProperty(value = "紧急程度")
    @TableField(value = "urgency")
    private String urgency;

    @ApiModelProperty(value = "打样工艺员")
    @TableField(value = "pattern_technician_name")
    private String patternTechnicianName;

    @ApiModelProperty(value = "打样工艺员id")
    private String patternTechnician;

    /*
     * 0 启用
     * 样衣没中断
     * 打板没中断
     * 1样衣中断
     * 2打板中断
     * */
    @ApiModelProperty(value = "打版状态")
    @TableField(value = " (CASE WHEN p.break_off_Pattern = '1' THEN '打板中断' WHEN p.break_off_sample = '1' THEN '样衣中断' ELSE '启用' END)")
    private String pmStatus;

    /*
     * 0 启用
     * 样衣没中断
     * 打板没中断
     * 1样衣中断
     * 2打板中断
     * */
    @ApiModelProperty(value = "状态")
    private String  nePmStatus;


    @TableField(value = "(CASE WHEN p.prm_send_status = 0 THEN '未发送' ELSE '已发送' END)")
    private String prmSendStatus;

    private String breakOffPattern;
    @ApiModelProperty(value = "图片标记")
    private String imgFlag;

    @ApiModelProperty(value = "品类")
    private String prodCategory;

    @ApiModelProperty(value = "波段")
    @TableField(value = "band_name")
    private String bandName;

    @ApiModelProperty(value = "打样设计师")
    @TableField(value = "pattern_designer_name")
    private String patternDesignerName;

    @ApiModelProperty(value = "设计收到时间")
    @TableField(value = "receive_sample_date", property = "date")
    private String receiveSampleDate;

    @ApiModelProperty(value = "设计款号")
    @TableField(value = "design_no")
    private String designNo;

    @ApiModelProperty(value = "大货款号")
    private String styleNo;

    @ApiModelProperty(value = "设计收到时间判断为空标记")
    private String sampleNullFlag;


    //添加字段进行模糊查询
    /**
     * 生产类型
     */
    @TableField(value = "devt_type_name")
    private String devtTypeName;//	生产类型
    @TableField(value = "style_pic")
    private String stylePic;//	款图
    @TableField(value = "sample_type")
    private String sampleType;//	打版类型
    @TableField(value = "p.pat_diff_name")
    private String patDiffName;//	打版难度
    @TableField(value = "p.pat_seq_name")
    private String patSeqName;//	打版顺序
    @TableField(value = "(CASE WHEN p.technician_kitting = 0 THEN '否' ELSE '是' END)")
    private String technicianKitting;//	设计确认是否齐套
    @TableField(value = "technician_kitting_date" , property = "date")
    private String technicianKittingDate;//	设计确认是否齐套时间
    @TableField(value = "pattern_req_date" , property = "date")
    private String patternReqDate;//	纸样需求完成日期
    @TableField(value = "p.demand_finish_date" , property = "date")
    private String demandFinishDate;//	样衣需求完成日期
    //@TableField(value = "jssdrq")
    private String jssdrq;//	技术收到日期
    @TableField(value = "shortage_remarks")
    private String shortageRemarks;//	备注
    @TableField(value = "revision_comments")
    private String revisionComments;//	改版意见
    //private String season;//	季节
    @TableField(value = "revision_reason")
    private String revisionReason;//	改版原因
    //@TableField(value = "bfzgxfsj")
    private String bfzgxfsj;//	版房主管下发时间
    @TableField(value = "s.pattern_design_name")
    private String patternDesignName;//	版师
    //@TableField(value = "bsjssj")
    private String bsjssj;//	版师接收时间
    @TableField(value = "requirement_num")
    private String requirementNum;//	需求数量
    //@TableField(value = "zywcsj")
    private String zywcsj;//	纸样完成时间
    @TableField(value = "suspend_remarks")
    private String suspendRemarks;//	延迟打版原因
    @TableField(value = "pattern_making_score")
    private String patternMakingScore;//	版师工作量评分
    @TableField(value = "cutter_name")
    private String cutterName;//	裁剪工
    //@TableField(value = "cjkssj")
    private String cjkssj;//	裁剪开始时间
    //private String designNo;//	款号
    //@TableField(value = "cjwcsj")
    private String cjwcsj;//	裁剪完成时间
    @TableField(value = "cutter_finish_num")
    private String cutterFinishNum;//	裁剪完成件数
    @TableField(value = "(CASE WHEN p.sgl_kitting = 0 THEN '否' ELSE '是' END)")
    private String sglKitting;//	样衣组长确认齐套
    @TableField(value = "stitcher_remark")
    private String stitcherRemark;//	备注
    @TableField(value = "stitcher")
    private String stitcher;//	车缝工名称
    //@TableField(value = "cfkssj")
    private String cfkssj;//	车缝开始时间
    //@TableField(value = "cfwcsj")
    private String cfwcsj;//	车缝完成时间
    //@TableField(value = "yywcsj")
    private String yywcsj;//	样衣完成时间
    @TableField(value = "technician_name")
    private String technicianName;//	工艺员
    //private String patternTechnicianName;//	打样工艺员
    @TableField(value = "sample_pic")
    private String samplePic;//	样衣图
    @TableField(value = "(CASE WHEN s.designer != '' THEN SUBSTRING_INDEX(s.designer, ',', -1) ELSE '' END)")
    private String designer;//	设计师
    //private String patternDesignerName;//	打样设计师
    @TableField(value = "merch_design_name")
    private String merchDesignName;//	跟款设计师
    @TableField(value = "revised_design_name")
    private String revisedDesignName;//	改款设计师
    @TableField(value = "reviewed_design_name")
    private String reviewedDesignName;//	审版设计师
    @TableField(value = "prod_category_name")
    private String prodCategoryName;//	品类
    @TableField(value = "prod_category2nd_name")
    private String prodCategory2ndName;//	中类
    @TableField(value = "prod_category3rd_name")
    private String prodCategory3rdName;//	小类
    @TableField(value = "pattern_finish_num")
    private String patternFinishNum;//	纸样完成件数
    @TableField(value = "default_size")
    private String defaultSize;//	尺码
    @TableField(value = "urgency_name",condition = "urgency")
    private String urgencyName;//	紧急程度
    //private String pmCreateDate;//	创建指令时间
    //private String patternParts;//	部件编码
    @TableField(value = "sample_finish_num")
    private String sampleFinishNum;//	完成件数
    @TableField(value = "pattern_making_quality_score")
    private String patternMakingQualityScore;//	版师质量评分
    @TableField(value = "sample_making_score")
    private String sampleMakingScore;//	样衣工作量评分
    @TableField(value = "sample_making_quality_score")
    private String sampleMakingQualityScore;//	样衣工质量评分
    @TableField(value = "second_processing")
    private String secondProcessing;//	二次加工
    //private String pmStatus;//	状态
    @TableField(value = "prm_send_date" , property = "date")
    private String prmSendDate;//	下发给样衣组长时间
    //private String prmSendStatus;//	下发给样衣组长状态
    @TableField(value = "pattern_no")
    private String patternNo;//	样版号 c
    @TableField(value = "grading_name")
    private String gradingName;//	放码师
    @TableField(value = "grading_date", property = "date")
    private String gradingDate;//	放码时间
    //private String bandName;//	波段
    //private String sampleBarCode;//	样衣码
    //private String receiveSampleDate;//	设计收到时间
    //添加字段进行模糊查询
    // 生产类型
    private String devtType;
    //列头全量匹配
    private String columnHeard;
    //列头全量匹配
}
