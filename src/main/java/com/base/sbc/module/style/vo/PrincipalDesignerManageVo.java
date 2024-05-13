/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：负责设计师配置表Vo 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.vo.PrincipalDesignerManageVo
 * @email your email
 * @date 创建时间：2024-3-18 16:27:53
 */
@Data
@ApiModel("负责设计师配置表 PrincipalDesignerManageVo")
public class PrincipalDesignerManageVo extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 品牌名称,多选
     */
    @ApiModelProperty(value = "品牌名称,多选")
    private String brandName;
    /**
     * 品类code
     */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    /**
     * 设计师名称
     */
    @ApiModelProperty(value = "设计师名称")
    private String designer;
    /**
     * 设计师id
     */
    @ApiModelProperty(value = "设计师id")
    private String designerId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
