/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：核价模板
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:33
 */
@Data
@ApiModel("核价模板")
public class PricingTemplateVO extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 模板编码
     */
    @ApiModelProperty(value = "模板编码")
    private String templateCode;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;
    /**
     * 状态(0)启用,(1)停用
     */
    @ApiModelProperty(value = "状态(0)启用,(1)停用")
    private String status;
    /**
     * 是否默认0.否,1.是
     */
    @ApiModelProperty(value = "是否默认0.否,1.是")
    private String defaultFlag;

    /** 模板名称 */
    @ApiModelProperty(value = "模板名称"  )
    private String devtType;

    /** 生产类型名称 */
    @ApiModelProperty(value = "生产类型名称"  )
    private String devtTypeName;

    /**
     * 核价模板明细
     */
    private List<PricingTemplateItemVO> pricingTemplateItems;

    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;

    /** 品牌名 */
    @ApiModelProperty(value = "品牌名"  )
    private String brandName;

    /**
     * 编辑权限
     */
    private Integer isEdit = 0;
}
