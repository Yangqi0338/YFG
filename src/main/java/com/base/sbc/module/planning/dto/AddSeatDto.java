package com.base.sbc.module.planning.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

/**
 * 类描述：商品企划-添加坑位信息
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.dto.AddSeatDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-24 09:55
 */
@Data
@ApiModel("商品企划-添加坑位信息AddSeatDto")
public class AddSeatDto {

    @ApiModelProperty(value = "渠道编号", example = "689467740238381056", required = true)
    @NotBlank(message = "坑位编号不能为空")
    private String planningChannelId;

    @ApiModelProperty(value = "大类code")
    @NotBlank(message = "大类不能为空")
    private String prodCategory1st;
    @ApiModelProperty(value = "品类code")
    @NotBlank(message = "品类不能为空")
    private String prodCategory;

    @ApiModelProperty(value = "大类名称")
    @NotBlank(message = "大类不能为空")
    private String prodCategory1stName;
    @ApiModelProperty(value = "品类名称")
    @NotBlank(message = "品类不能为空")
    private String prodCategoryName;

    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称"  )
    private String prodCategory3rdName;
    /** 中类code */
    @ApiModelProperty(value = "中类code"  )
    private String prodCategory2nd;
    /** 小类code */
    @ApiModelProperty(value = "小类code"  )
    private String prodCategory3rd;
    @ApiModelProperty(value = "坑位数量")
    @Range(min = 1, max = 50)
    private Integer count;
}
