/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.client.ccm.entity;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/** 
 * 类描述：字典表 实体类
 * @address com.base.sbc.companyconfig.entity.BasicBaseDict
 * @author pengjinsong
 * @email 2416962387@qq.com
 * @date 创建时间：2019-5-10 15:24:23
 * @version 1.0  
 */
@Data
public class BasicBaseDict extends BaseDataEntity<String> {
	private static final long serialVersionUID = 1L;
    /** 父级编号 */
    private String parentId;
    /** 所有父级编号 */
    private String parentIds;
    /** 标签名 */
    private String name;
    /** 数据值 */
    private String value;
    /** 类型 */
    private String type;
    /** 描述 */
    private String description;
    /** 排序（升序） */
    private BigDecimal sort;
    /** 状态 */
    private String status;
    /** 层级 */
    private String level;
    /** 是否子节点 */
    private String isLeaf;
    /** 是否初始值(1.是 0.否) */
    private String isInitial;

    @JsonIgnore
    @ApiModelProperty(value = "具体数据")
    public int getNameLength(){
        return StrUtil.length(this.getName());
    };

}

