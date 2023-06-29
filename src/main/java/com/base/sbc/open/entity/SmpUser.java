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
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 类描述：SMP用户表 实体类
 * @address com.base.sbc.company.entity.SmpUser
 * @author 卞康
 * @email 247967116@qq.com
 * @date 创建时间：2023-5-31 16:26:40
 * @version 1.0
 */
@Data
public class SmpUser extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    /** 发送状态(1:新增, 2:修改) */
    private String type;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 部门编号 */
    private String department;
    /** 组别 */
    private String depGroup;
    /** 员工星级 */
    private String empCustom5;
    /** 企业Id */
    private String companyId;
    /** 部门名称 */
    private String depTitle;
    /** 岗位编号 */
    private String positioId;
    /** 人事EID */
    private String eid;
    /** 工号 */
    private String userId;
    /** 入职日期 */
    private String entryDate;
    /** 姓名 */
    private String name;
    /** 手机号 */
    private String mobile;
    /** 用户状态(1:正常, 0:离职) */
    private String userStatus;
    /** 年龄 */
    private String age;
    /** 性别 */
    private String sex;
    /** 英文名字 */
    private String enName;
    /** 居住所在省 */
    private String nativePlaceProvince;
    /** 居住所在市 */
    private String nativePlaceCity;
    /** 出生日期 */
    private String birthday;
    /** 民族 */
    private String nation;
    /** 身份证号 */
    private String cardId;
    /** 婚姻状态 */
    private String marriageStatus;
    /** 政治面貌 */
    private String politicalStatus;
    /** 入党日期 */
    private String joinPartyDate;
    /** 户口所在省 */
    private String registeredResidenceProvince;
    /** 户口所在市 */
    private String registeredResidenceCity;
    /** 户口类型 */
    private String registeredResidenceType;
    /** 学历 */
    private String education;
    /** 健康状态 */
    private String health;
    /** 学位 */
    private String degree;
    /** 毕业学校 */
    private String school;
    /** 专业 */
    private String subject;
    /** 毕业日期 */
    private String graduatingDate;
    /** 电子邮件地址 */
    private String email;
    /** 联系手机 */
    private String contactMobile;
    /** QQ号 */
    private String qq;
    /** 微信号 */
    private String wechat;
    /** 家庭住址 */
    private String liveAddress;
    /** 员工状态(3:作废,其他正常) */
    private String objStatus;
    /** 钉钉部门ID */
    private String ddDepId;
    /** 钉钉上级部门ID */
    private String ddAdminId;
    /** 部门ID */
    private String depId;
    /** 离职日期 */
    private String leaveDate;
    /** 开始工作日期 */
    private String workBeginDate;
    /** 银行名称 */
    private String bankName;
    /** 银行账号 */
    private String bankCardOut;
    /** 银行支行名称 */
    private String bandAddr;
    /** 户口地址 */
    private String residentAddress;
    /** 户口类型 */
    private String resident;
    /** 社保类型 */
    private String insuranceType;
    /** 参保日期 */
    private String canBaoDate;
    /** 停保日期 */
    private String stopSocialDate;
    /** 最后工作日期 */
    private String workLastDate;
    /** 离职原因 */
    private String leaveReason;
    /** 岗位类型 */
    private String jobStatus;
    /** 员工状态 */
    private String staffStatus;
    /** OA同步日期 */
    private String oaSyncDate;

}

