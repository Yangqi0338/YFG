/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.vo;

import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 面料开发申请保存
 */
@Data
@ApiModel("面料开发申请")
public class FabricDevApplyVO {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;
    @ApiModelProperty(value = "修改人")
    private String updateName;
    @ApiModelProperty(value = "创建人")
    private String createName;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 开发申请单号
     */
    @ApiModelProperty(value = "开发申请单号")
    private String devApplyCode;
    /**
     * 分配状态:1.待分配、2.进行中、3.已完成
     */
    @ApiModelProperty(value = "分配状态:1.待分配、2.进行中、3.已完成")
    private String allocationStatus;
    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id")
    private String materialId;
    /**
     * 基本信息
     */
    @ApiModelProperty(value = "基本信息")
    private FabricDevBasicInfoVO fabricDevBasicInfo;

    /**
     * 物料信息
     */
    @ApiModelProperty(value = "物料信息")
    private BasicsdatumMaterialVo basicsdatumMaterial;


}
