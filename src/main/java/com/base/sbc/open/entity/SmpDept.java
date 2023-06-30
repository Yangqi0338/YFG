/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 类描述：SMP部门表 实体类
 * @address com.base.sbc.company.entity.SmpDept
 * @author 卞康
 * @email 247967116@qq.com
 * @date 创建时间：2023-6-28 17:44:44
 * @version 1.0
 */
@Data
public class SmpDept extends BaseDataEntity<String> {
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

    /** 部门负责人工号 */
    private String inChargeBadge;

    /** 是否叶子节点  0不是,1是 */
    private Integer isLeaf;
}

