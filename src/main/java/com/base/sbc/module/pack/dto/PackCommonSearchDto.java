package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-公共筛选条件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackCommonSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:09
 */
@Data
@ApiModel("资料包-公共筛选条件 PackCommonSearchDto")
public class PackCommonSearchDto {

    public PackCommonSearchDto() {
    }

    public PackCommonSearchDto(String foreignId, String packType) {
        this.foreignId = foreignId;
        this.packType = packType;
    }

    @ApiModelProperty(value = "主数据id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")
    @NotBlank(message = "资料包类型为空")
    private String packType;

    @ApiModelProperty(value = "资料包类型:packDesign:颜色编码")
    private String colorCode;

    @ApiModelProperty(value = "版本id")
    private String bomVersionId;

    /**
     * 是否勾选 0-否 1-是 是就代表按照勾选的来计算，否或者空就是不按照勾选的 计算所有的
     */
    @ApiModelProperty(value = "是否勾选 0-否 1-是 是就代表按照勾选的来计算，否或者空就是不按照勾选的 计算所有的")
    private String checkType;
}
