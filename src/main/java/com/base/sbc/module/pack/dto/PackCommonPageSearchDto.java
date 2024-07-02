package com.base.sbc.module.pack.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

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
@ApiModel("资料包-公共分页筛选条件 PackCommonSearchDto")
public class PackCommonPageSearchDto extends Page {

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

    @ApiModelProperty(value = "不能使用(0否,1是")
    private String unusableFlag;

    @ApiModelProperty(value = "主数据id集合")
    private List<String> foreignIdList;

    @ApiModelProperty(value = "版本id集合")
    private List<String> bomVersionIdList;





}
