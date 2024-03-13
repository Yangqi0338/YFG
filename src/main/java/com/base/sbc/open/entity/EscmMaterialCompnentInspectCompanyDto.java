package com.base.sbc.open.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("t_escm_material_component_inspect_company")
public class EscmMaterialCompnentInspectCompanyDto extends BaseDataEntity<String> {
    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 年份
     */
    private String year;

    /**
     * 到货日期
     */
    private Date arriveDate;
    /**
     * 送检日期
     */
    private String checkDate;

    /**
     * 检测项目
     */
    private String sendInspectContent;

    /**
     * 款号
     */
    private String styleNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 检测单位
     */
    private String companyFullName;

    /**
     * 委托商
     */
    private String supplierFullName;

    /**
     * 质检单位成分
     */
    private String quanlityInspectContent;

    /**
     * 预计出报告时间
     */
    private String predictReportTime;

    /**
     * 实际出报告时间
     */
    private String actualReportTime;

    /**
     * 几组成分
     */
    private String compnentNum;

    /**
     * 成分有效期（剩余天数）
     */
    private Integer validityTime ;

    /**
     * 是否到期
     */
    private String validityStatus ;

    /**
     * 供应商名称
     */
    private String supplierName ;

    /**
     * 供应商等级
     */
    private String supplierLevel ;

    /**
     * 供应商成分
     */
    private String supplierComposition ;


    /**
     * 面料编号
     */
    private String materialsNo ;


   /**
     * 物料名称
     */
    private String materialsName ;

    /**
     * 制单人
     */
    private String makerByName ;


    @TableField(exist = false)
    private List<EscmMaterialCompnentInspectContent> detailList;

    @TableField(exist = false)
    private String inspectCompanyId;

    /*年份集合*/
    @TableField(exist = false)
    private List<EscmMaterialCompnentInspectCompanyDto> companyDtoList;

}
