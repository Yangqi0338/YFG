package com.base.sbc.module.operalog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.*;

/**
 * @author 卞康
 * @date 2023/6/19 9:54:22
 * @mail 247967116@qq.com
 * 操作日志实体类
 */
@Data
@TableName("t_opera_log")
public class OperaLogEntity extends BaseDataEntity<String> {

    /**
     * 单据id
     */
    private String documentId;

    /**
     * 单据名称字段名
     */
    @TableField(exist = false)
    private String documentNameField;

    /**
     * 单据名称
     */
    private String documentName;

    /**
     * 单据编码字段名
     */
    @TableField(exist = false)
    private String documentCodeField;
    /**
     * 单据编码
     */
    private String documentCode;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 模块名称
     */
    private String name;
    /**
     * 内容变更
     */
    private String content;

    /**
     * 变更json
     */
    private String jsonContent;

    /**
     * 路径
     */
    private String path;
    /**
     * 单据名称字段名
     */
    @TableField(exist = false)
    private String pathField;

    /**
     * 父id
     */
    private String parentId;
    /**
     * 单据名称字段名
     */
    @TableField(exist = false)
    private String parentIdField;

}
