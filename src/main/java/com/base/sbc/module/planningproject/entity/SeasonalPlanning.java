package com.base.sbc.module.planningproject.entity;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 季节企划
 * @author 卞康
 * @date 2024-01-18 17:06:03
 * @mail 247967116@qq.com
 */
@Data
public class SeasonalPlanning extends BaseDataEntity<String> {
    @ApiModelProperty(value = "名称")
    private String name;
}
