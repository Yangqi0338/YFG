package com.base.sbc.module.patternmaking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：打版管理-工作记录保存
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingWorkLogSaveDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 19:59
 */
@Data
@ApiModel("打版管理-工作记录保存dto PatternMakingWorkLogSaveDto ")
public class PatternMakingWorkLogSaveDto {
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 打板id
     */
    @ApiModelProperty(value = "打板id")
    @NotBlank(message = "打板id不能为空")
    private String patternMakingId;
    /**
     * 用户类型:裁剪工、车缝工
     */
    @ApiModelProperty(value = "用户类型:裁剪工、车缝工")
    @NotBlank(message = "用户类型不能为空")
    private String userType;
    /**
     * 登记类型:修改、小样、其他
     */
    @ApiModelProperty(value = "登记类型:修改、小样、其他")
    @NotBlank(message = "登记类型不能为空")
    private String logType;
    /**
     * 记录
     */
    @Length(min = 3, max = 200, message = "长度3-200")
    @ApiModelProperty(value = "记录")
    private String remarks;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "数据类型:0打版管理,1产前样任务")
    private String dataType;

}
