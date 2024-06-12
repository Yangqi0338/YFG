/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.annotation.ExtendField;
import com.base.sbc.config.common.base.BaseDataExtendEntity;
import com.base.sbc.module.replay.dto.ReplayConfigDetailDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：基础资料-复盘管理 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.entity.ReplayConfig
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-3 18:43:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_replay_config", autoResultMap = true)
@ApiModel("基础资料-复盘管理 ReplayConfig")
public class ReplayConfig extends BaseDataExtendEntity {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 品牌 */
    @ApiModelProperty(value = "品牌")
    @NotBlank(message = "品牌不能为空")
    private String brand;

    /** 品牌名 */
    @ApiModelProperty(value = "品牌名")
    @ExtendField
    private String brandName;

    /** 销售周期 */
    @ApiModelProperty(value = "销售周期")
    private String saleCycle;

    /** 销售季 */
    @ApiModelProperty(value = "销售季")
    @ExtendField
    @JsonIgnore
    private ReplayConfigDetailDTO saleSeason;

}
