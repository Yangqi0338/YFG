package com.base.sbc.module.style.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class PrincipalDesignerManageExcel {

    /**
     * 品牌
     */
    private String brand;
    /**
     * 品牌名称,多选
     */
    @Excel(name = "品牌名称")
    private String brandName;
    /**
     * 品类code
     */
    private String prodCategory;
    /**
     * 品类名称
     */
    @Excel(name = "品类名称")
    private String prodCategoryName;
    /**
     * 中类code
     */
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @Excel(name = "中类名称")
    private String prodCategory2ndName;
    /**
     * 设计师名称
     */
    @Excel(name = "设计师名称")
    private String designer;
    /**
     * 设计师id
     */
    private String designerId;

}
