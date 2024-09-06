package com.base.sbc.module.httplog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 卞康
 * @date 2023/5/16 18:49:25
 * @mail 247967116@qq.com
 *
 * riz
 */
@Data
@TableName("t_http_log")
public class HttpLog implements Serializable {

    //请求数据
    /** 请求开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**请求类型 (1: 发出的请求, 2: 收到的请求)*/
//    private Integer type;

    /** IP地址 */
    private String ip;

    /** 物理地址 */
    private String address;

    /** 接口地址 */
    private String url;

    /** 方法类型 */
    private String method;

    /**请求类型*/
    private String reqContentType;

    /*** 请求头*/
    private String reqHeaders;

    /*** 请求参数*/
    private String reqQuery;

    /** 请求body */
    private String reqBody;

    /** 用户编码 */
//    private String userCode;

    /** 团队编码 */
//    private String teamCode;

    /** 线程Id */
    private String threadId;

    /** 文档名称 */
    private String reqName;

    /** 权限编码 */
//    private String authCode;

    /** 菜单权限名称 */
//    private String authName;

    /** sql 记录 */
//    private String sqlLog;

    //响应数据
    /** 持续时间ms */
//    private Long intervalNum;
    private Short intervalNum;

    /** 响应数据 */
    private String respBody;

    /** 是否异常(0正常1异常) */
    private Integer exceptionFlag;

    /** 异常信息 */
//    private String throwableException;

    ///** 请求的cookie */
    //private Map<String, String> cookieMap;

    /** 请求sql */
//    @TableField(exist = false)
//    private List<SqlDataInfo> sqlDataInfoList;

    /** 业务具体日志 */
    //private Map<String, Object> businessDataMap;


    /***响应状态码*/
    private Integer statusCode;

    /***响应头*/
//    private String respHeaders;

    /***备注*/
//    private String remarks;



    @TableField(exist = false)
    private static final long serialVersionUID = 7022181519896948997L;

    /** 更新者名称  */
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    protected String updateName;

    /**  更新者id */
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    protected String updateId;

    /** 更新日期 */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    protected Date updateDate;

    /** 创建日期 */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @TableField(fill = FieldFill.INSERT)
//    protected Date createDate;

    /**  创建者名称 */
    @TableField(fill = FieldFill.INSERT)
    protected String createName;

    /** 创建者id */
    @TableField(fill = FieldFill.INSERT)
    protected String createId;

    /**物理删除*/
//    protected String delFlag;

    /** 实体主键 */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 公司编码 */
    @TableField(fill = FieldFill.INSERT)
    private String companyCode;

    @Override
    public String toString() {
        return "\n" +
                "开始时间: %s | 地址: %s | 请求名: %s | 方法类型:%s | 线程id: %s | 状态码: %s | 请求人: %s\n" +
                "请求头: %s\n" +
                "请求Body: %s\n" +
                "返回Body: %s\n" +
                "其他扩展信息: [ip:%s | 物理地址:%s | 请求类型:%s | 持续时间:%s | 是否异常:%s | 状态码:%s | 创建人id: %s]\n";
    }
}


