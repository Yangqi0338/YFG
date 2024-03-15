package com.base.sbc.module.style.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class LatestCommissioningDateExcel {

    /** 品牌 */
    private String brand;
    /** 品牌名称,多选 */
    @Excel(name = "品牌名称"  )
    private String brandName;
    /** 年份 */
    private String year;
    /** 年份名称 */
    @Excel(name = "年份名称"  )
    private String yearName;
    /** 波段(编码) */
    private String bandCode;
    /** 波段名称 */
    @Excel(name = "波段名称"  )
    private String bandName;
    /** 最晚投产日期 */
    @Excel(name = "最晚投产日期",format = "yyyy-MM-dd")
    private Date latestCommissioningDate;

}
