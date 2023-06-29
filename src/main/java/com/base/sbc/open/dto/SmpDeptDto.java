package com.base.sbc.open.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/18 15:15:00
 * @mail 247967116@qq.com
 */
@Data
public class SmpDeptDto {

    @Excel(name = "id")
    private String id;
    /** 公司ID */
    @Excel(name = "compid")
    private Integer companyId;
    /** 部门ID */
    @Excel(name = "depcode")
    private String departmentId;
    /** 嘉扬部门ID */
    @Excel(name = "depid")
    private String depId;
    /** 部门名称 */
    @Excel(name = "title")
    private String name;
    /** 钉钉部门ID */
    @Excel(name = "dd_depid")
    private Integer ddDepId;
    /** 钉钉部门父ID */
    @Excel(name = "dd_adminid")
    private Integer ddAdminId;
    /** 父类ID */
    @Excel(name = "parent_code")
    private String parentId;
    /** 部门组别 */
    @Excel(name = "depgroup")
    private String depGroup;
    /** 部门级别 */
    @Excel(name = "depgrade")
    private Integer depGrade;
    /** 部门负责人工号 */
    @Excel(name = "incharge_badge")
    private String inChargeBadge;
    /** 是否叶子节点  0不是,1是 */
    @Excel(name = "isleaf")
    private Integer isLeaf;
    /** 状态,1正常,0不正常 */
    @Excel(name = "depstatus")
    private Integer objStatus;

}
