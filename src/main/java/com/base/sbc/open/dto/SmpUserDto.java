package com.base.sbc.open.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;



/**
 * @author 卞康
 * @date 2023/5/18 15:24:09
 * @mail 247967116@qq.com
 */
@Data
public class SmpUserDto{
    /**
     * 生日
     */
    @Excel(name = "birthday")
    private String birthday;

    /**
     * id
     */
    @Excel(name = "id")
    private String id;

    /**
     * 身份证号码
     */
    @Excel(name = "certno")
    private String cardId;

    /**
     * 公司ID
     */
    @Excel(name = "compid")
    private String companyId;

    /**
     * 手机号
     */
    @Excel(name = "mobile")
    private String mobile;

    /**
     * 部门ID
     */
    @Excel(name = "depcode")
    private String department;

    /**
     * 部门名称
     */
    @Excel(name = "deptitle")
    private String depTitle;

    /**
     * 部门组别
     */
    @Excel(name = "depgroup")
    private String depGroup;

    /**
     * 姓名
     */
    @Excel(name = "name")
    private String name;

    /**
     * 民族
     */
    @Excel(name = "nation")
    private String nation;

    /**
     * 岗位ID
     */
    @Excel(name = "jobcode")
    private String positioId;

    /**
     * 性别 1男 2女
     */
    @Excel(name = "gender")
    private String sex;

    /**
     * 工号
     */
    @Excel(name = "badge")
    private String userId;

    /**
     * 人事ID,钉钉ID
     */
    @Excel(name = "eid")
    private String eid;

    /**
     * 状态,1正常,0不正常
     */
    @Excel(name = "empstatus")
    private String empstatus;

    @Excel(name = "pwd")
    private String pwd;

    @Excel(name = "u_type")
    private String u_type;

    @Excel(name = "login_type")
    private String login_type;

    @Excel(name = "login_def")
    private String login_def;

    @Excel(name = "email")
    private String email;

    @Excel(name = "post")
    private String post;

    @Excel(name = "address")
    private String liveAddress;

    @Excel(name = "create_dt")
    private String createDate;

    @Excel(name = "modify_dt")
    private String updateDate;

    @Excel(name = "last_login")
    private String last_login;

    @Excel(name = "nickname")
    private String nickname;

    @Excel(name = "nick_portrait")
    private String nick_portrait;

    @Excel(name = "is_valid")
    private String is_valid;

    /**
     * 外埠钉钉ID,对应EID
     */
    @Excel(name = "dd_id")
    private String dd_id;

    /**
     * 员工星级
     */
    @Excel(name = "empcustom5")
    private String empCustom5;

    /**
     * 年龄
     */
    @Excel(name = "age")
    private String age;

    /**
     * 开始工作日期
     */
    @Excel(name = "WorkBeginDate")
    private String workBeginDate;

    /**
     * 银行名称
     */
    @Excel(name = "bankname")
    private String bankName;

    /**
     * 银行卡号
     */
    @Excel(name = "bankcardout")
    private String bankCardOut;

    /**
     * 开户行
     */
    @Excel(name = "bandaddr")
    private String bandAddr;

    /**
     * 户籍地
     */
    @Excel(name = "residentAddress")
    private String residentAddress;

    /**
     * 户口性质
     */
    @Excel(name = "Resident")
    private String resident;

    /**
     * 入职日期
     */
    @Excel(name = "entry_date")
    private String entryDate;

    /**
     * 社保类型
     */
    @Excel(name = "InsuranceType")
    private String insuranceType;

    /**
     * 社保起缴年月
     */
    @Excel(name = "canbaodate")
    private String canBaoDate;

    /**
     * 离职日期
     */
    @Excel(name = "LeaveDate")
    private String leaveDate;

    /**
     * 停保日期
     */
    @Excel(name = "stopsocialdate")
    private String stopSocialDate;

    /**
     * 最后工作日期
     */
    @Excel(name = "WorkLastDate")
    private String workLastDate;

    /**
     * 离职原因
     */
    @Excel(name = "LeaveReason")
    private String leaveReason;

    /**
     * 在岗状态
     */
    @Excel(name = "JobStatus")
    private String jobStatus;

    /**
     * 员工在职离职状态,1:在职,2:离职
     */
    @Excel(name = "staffStatus")
    private String staffStatus;

    @Excel(name = "highlevel")
    private String education;

    /**
     * OA同步日期
     */
    @Excel(name = "oa_sync_date")
    private String oaSyncDate;
}
