package com.base.sbc.module.sample.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：产前样-任务分配Dto PreTaskAssignmentDto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.PreTaskAssignmentDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 17:51
 */
@Data
@ApiModel("产前样-任务分配Dto PreTaskAssignmentDto")
public class PreTaskAssignmentDto {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 工艺师名称
     */
    @ApiModelProperty(value = "工艺师名称")
//    @NotBlank(message = "工艺师不能为空")
    private String technologistName;
    /**
     * 工艺师id
     */
    @ApiModelProperty(value = "工艺师id")
//    @NotBlank(message = "工艺师不能为空")
    private String technologistId;
    /**
     * 放码师id
     */
    @ApiModelProperty(value = "放码师id")
//    @NotBlank(message = "放码师不能为空")
    private String gradingId;
    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
//    @NotBlank(message = "放码师不能为空")
    private String gradingName;
    /**
     * 裁剪工
     */
    @ApiModelProperty(value = "裁剪工")
    @NotBlank(message = "裁剪工不能为空")
    private String cutterName;
    /**
     * 裁剪工id
     */
    @ApiModelProperty(value = "裁剪工id")
    @NotBlank(message = "裁剪工不能为空")
    private String cutterId;
    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    //@NotBlank(message = "车缝工不能为空")
    private String stitcher;
    /**
     * 车缝工id
     */
    @ApiModelProperty(value = "车缝工id")
    //@NotBlank(message = "车缝工不能为空")
    private String stitcherId;

    private String sampleBarCode;

}
