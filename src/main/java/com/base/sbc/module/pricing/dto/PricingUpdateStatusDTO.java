package com.base.sbc.module.pricing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 修改状态DTO
 *
 * @Author xhj
 * @Date 2023/6/16 15:56
 */
@Data
@ApiModel("修改状态DTO")
public class PricingUpdateStatusDTO {
    /**
     * 业务id
     */
    @ApiModelProperty("业务id")
    private String id;
    /**
     * 业务id集合
     */
    @ApiModelProperty("业务id集合")
    private List<String> ids;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @NotBlank(message = "状态不可为空")
    private String status;
}
