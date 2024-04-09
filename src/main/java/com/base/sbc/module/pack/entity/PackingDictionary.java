package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_packaging_dictionary")
@ApiModel("资料包-工艺说明-包装方式和体积重量字典 PackTechPackaging")
public class PackingDictionary extends BaseDataEntity<String> {
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String id;

    /**
     * 包装形式
     */
    @ApiModelProperty(value = "包装形式")
    private String packagingForm;
    /**
     * 包装形式名称
     */
    @ApiModelProperty(value = "包装形式名称")
    private String packagingFormName;
    /**
     * 包装袋标准
     */
    private String parentId;
    /**
     * 包装袋标准名称
     */
    private String name;

    /**
     * 长
     */
    @ApiModelProperty(value = "长")
    private BigDecimal volumeLength;
    /**
     * 宽
     */
    @ApiModelProperty(value = "宽")
    private BigDecimal volumeWidth;
    /**
     *高
     */
    @ApiModelProperty(value = "高")
    private BigDecimal volumeHeight;
}
