/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * 类描述：资料包-工序工价 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackProcessPrice
 * @email your email
 * @date 创建时间：2023-7-11 17:44:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_process_price")
@ApiModel("资料包-工序工价 PackProcessPrice")
public class PackProcessPrice extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型
     */
    @ApiModelProperty(value = "资料包类型")
    private String packType;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private BigDecimal sort;
    /**
     * 工序编号
     */
    @ApiModelProperty(value = "工序编号")
    private String processSort;
    /**
     * 部位
     */
    @ApiModelProperty(value = "部位")
    private String part;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String processName;
    /**
     * 工序等级
     */
    @ApiModelProperty(value = "工序等级")
    private String level;
    /**
     * 工序说明
     */
    @ApiModelProperty(value = "工序说明")
    private String processSay;
    /**
     * 是否末节点:(0否，1是)
     */
    @ApiModelProperty(value = "是否末节点:(0否，1是)")
    private String endNode;
    /**
     * 标准工时(秒)
     */
    @ApiModelProperty(value = "标准工时(秒)")
    private BigDecimal processDate;
    /**
     * 标准工价(元)
     */
    @ApiModelProperty(value = "标准工价(元)")
    private BigDecimal processPrice;
    /**
     * 倍数
     */
    @ApiModelProperty(value = "倍数")
    private BigDecimal multiple;
    /**
     * 币种
     */
    @ApiModelProperty(value = "币种")
    private String currency;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

