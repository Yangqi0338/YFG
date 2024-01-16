package com.base.sbc.module.operalog.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 卞康
 * @date 2023/6/19 9:54:22
 * @mail 247967116@qq.com
 * 操作日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperaLogJsonDto {

    /**
     * 修改字段
     */
    private String name;

    /**
     * 修改前内容
     */
    private String oldStr;

    /**
     * 修改后内容
     */
    private String newStr;

}
