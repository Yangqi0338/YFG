package com.base.sbc.module.task.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author 卞康
 * @data 2023/3/6 17:20
 */
@Data
@TableName("t_form")
public class Form {
    /**
     * 表单主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 表单名称
     */
    private String name;

    /**
     * 表单内容
     */
    private String content;

    /**
     * 企业编码
     */
    private String companyCode;
    /**
     * 编码
     */
    private String code;

    /**
     * 创建时间
     */
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @TableField(fill = FieldFill.INSERT) //创建时自动填充
    private Date createTime;

    /**
     * 创建者ID
     */
    private String createId;

    /**
     * 创建者名称
     */
    private String createName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)//创建与修改时自动填充
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    /**
     * 修改者ID
     */
    private String updateId;

    /**
     * 修改者名称
     */
    private String updateName;

    /**
     * 删除标记(0正常，1删除)
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;


    /**
     * 当前表单关联部署id
     */
    @TableField(exist = false)
    private String deployId;

}
