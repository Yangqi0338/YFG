package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.base.sbc.config.enums.business.ProcessDatabaseType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 卞康
 * @date 2023/6/5 9:46:03
 * @mail 247967116@qq.com
 * 工艺资料库导入实体类
 */
@Data
public class ProcessDatabaseExcelDto {

    /**工艺项目(名称)*/
    @Excel(name = "工艺项目")
    private String processName;

    /**编码*/
    @Excel(name = "编码")
    private String code;
    /**品牌*/
    @Excel(name = "品牌")
    private String brandName;

    private String brandId;

    private String processType;
    /**工艺类型*/
    @Excel(name = "工艺类型")
    private String processTypeName;

    private String  component;

    @Excel(name = "部件类别")
    private String componentName;
    /**工艺要求*/
    @Excel(name = "工艺要求")
    private String processRequire;
    /**大类*/
    @Excel(name = "大类")
    private String broadCategory;
    /**中类*/
    @Excel(name = "中类")
    private String middleCategory;
    /**小类*/
    @Excel(name = "小类")
    private String smallCategory;

    /**部件类别*/
//    @Excel(name = "部件类别")
    private String componentCategory;

    /**描述*/
    @Excel(name = "描述")
    private String description;

    /**品类名称*/
    @Excel(name = "品类")
    private String categoryName;

    /**品类id*/
    private String categoryId;

    /** 图片 */
    @Excel(name = "图片",type = 2)
    private String picture;

    /**状态*/
    @Excel(name = "可用的",replace={"false_1","true_0"})
    private String status;

    /**类别 1：部件库，2：基础工艺，3：外辅工艺，4：裁剪工艺，5：注意事项，6：整烫包装，7：模板部件*/
    private ProcessDatabaseType type;


    /** 更新者名称  */
    @Excel(name = "修改者")
    private String updateName;


    /**  创建者名称 */
    @Excel(name = "创建人")
    private String createName;
    @Excel(name = "工价")
    private BigDecimal price;
}
