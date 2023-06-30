package com.base.sbc.open.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/18 15:22:32
 * @mail 247967116@qq.com
 */
@Data
public class SmpPostDto {
    @Excel(name = "id")
    private String id;
    /** 中心编号 */
    @Excel(name = "centercode")
    private String centerId;
    /** 公司编号 */
    @Excel(name = "compid")
    private String companyId;
    /** 岗位ID */
    @Excel(name = "jobcode")
    private String positionId;
    /** 岗位名称 */
    @Excel(name = "title")
    private String name;
    /** 中心名称 */
    @Excel(name = "centername")
    private String centerName;

    /** 岗位状态(1正常,0不正常) */
    @Excel(name = "poststatus")
    private String objStatus;
}
