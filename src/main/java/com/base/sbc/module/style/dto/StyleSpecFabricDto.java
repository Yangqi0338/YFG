/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.dto;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：款式BOM指定面料表 实体类
 * @address com.base.sbc.module.style.entity.StyleSpecFabric
 * @author your name
 * @email your email
 * @date 创建时间：2023-12-25 16:04:37
 * @version 1.0
 */
@Data
public class StyleSpecFabricDto extends BaseDataEntity<String> {

    /** 配色表t_style_color 主键id */
    private String styleColorId;
    /** 指定面料厂家 */
    @NotBlank(message = "指定面料厂家必填")
    private String manufacturerFabric;
    /** 厂家编号 */
    @NotBlank(message = "厂家编号必填")
    private String manufacturerNumber;
    /** 成分 */
    @NotBlank(message = "成分必填")
    private String ingredient;
}

