package com.base.sbc.module.basicsdatum.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BasicsdatumCoefficientTemplateExcelVo {

    /** 模板名称 */
    @ApiModelProperty(value = "模板名称"  )
    @Excel(name = "模板名称")
    private String name;

    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    @Excel(name = "季节")
    private String seasonName;

    /** 渠道名称 */
    @ApiModelProperty(value = "渠道名称"  )
    @Excel(name = "渠道")
    private String channelName;

    @Excel(name = "修改人")
    private String updateName;



    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "修改时间",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;



}
