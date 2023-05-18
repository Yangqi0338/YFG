package com.base.sbc.open.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/18 15:15:00
 * @mail 247967116@qq.com
 */
@Data
public class SmpDeptDto {
    /** 公司编号 */
    private String companyId;
    /** 部门编号 */
    private String departmentId;
    /** 部门名称 */
    private String name;
    /** 组别 */
    private String depGroup;
    /** 发送状态(1:新增, 2:修改) */
    private String type;
    /** 上级部门编号 */
    private String parentId;
    /** 品牌 */
    private String brand;
    /** 店长 */
    private String shopOwner;
    /** 大区经理 */
    private String admin1;
    /** 区经主管 */
    private String admin2;
    /** 区域经理 */
    private String director3;
    /** 运营经理 */
    private String director5;
    /** HRBP */
    private String depEmp;
    /** 总部人事 */
    private String headquartersHr;
    /** 钉钉部门ID */
    private String ddDepId;
    /** 钉钉上级部门ID */
    private String ddAdminId;
    /** 飞书部门ID */
    private String feishuDepId;
    /** 飞书上级部门ID */
    private String feishuAdminId;
    /** 部门层级 */
    private String depGrade;
    /** 部门编号 */
    private String depNum;
    /** 部门ID */
    private String depId;
    /** standard_id */
    private String standardId;
    /** 部门状态(3:作废,其他正常) */
    private String objStatus;
}
