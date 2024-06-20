package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：分配人员 dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.AssignmentUserDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-20 12:48
 */
@Data
@ApiModel("分配人员dto AssignmentUserDto ")
public class AssignmentUserDto {

    @ApiModelProperty(value = "打版id")
    @NotBlank(message = "数据id为空")
    private String id;

    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    @NotBlank(message = "车缝工为空")
    private String stitcher;
    /**
     * 车缝工id
     */
    @ApiModelProperty(value = "车缝工id")
    @NotBlank(message = "车缝工id为空")
    private String stitcherId;

    @ApiModelProperty(value = "样衣组长确认齐套")
    @NotBlank(message = "样衣组长确认齐套")
    private String sglKitting;
    @ApiModelProperty(value = "样衣条码")
    private String sampleBarCode;

    @ApiModelProperty(value = "分配车缝工备注")
    private String stitcherRemark;

    @ApiModelProperty(value = "齐套原因")
    private String kittingReason;

    @ApiModelProperty(value = "齐套原因名称")
    private String kittingReasonName;
}
