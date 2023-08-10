package com.base.sbc.module.fabric.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("开发申请保存")
public class FabricDevApplySaveDTO {
    @ApiModelProperty(value = "开发申请id")
    private String id;
    /**
     * 要求到料日期
     */
    @ApiModelProperty(value = "要求到料日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requiredArrivalDate;

    @ApiModelProperty(value = "面料开发基本信息保存")
    private FabricDevBasicInfoSaveDTO fabricDevBasicInfoSave;

    @ApiModelProperty(value = "面料开发物料信息保存")
    private FabricDevMaterialInfoSaveDTO fabricDevMaterialInfoSave;

    @ApiModelProperty(value = "面料开发其他信息保存")
    private FabricDevOtherInfoSaveDTO fabricDevOtherInfoSave;

    @ApiModelProperty(value = "面料开发颜色信息保存")
    private List<FabricDevColorInfoSaveDTO> fabricDevColorInfoSaves;

    @ApiModelProperty(value = "面料开发其他信息保存")
    private List<FabricDevSupplerInfoSaveDTO> fabricDevSupplerInfoSaves;

}
