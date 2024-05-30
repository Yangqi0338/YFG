package com.base.sbc.module.sample.dto;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrintFabricSummaryLogDto extends Page {

    @ApiModelProperty(value = "面料汇总id"  )
    private String fabricSummaryId;

    /**  创建者名称 */
    @ApiModelProperty(value = "创建者名称"  )
    private String createName;
}
