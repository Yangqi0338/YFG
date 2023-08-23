package com.base.sbc.module.fabric.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 面料开发配置信息列表
 */
@Data
@ApiModel("面料开发配置信息列表")
public class FabricDevConfigInfoListVO {

    private String id;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    /**
     * 开发名称
     */
    @ApiModelProperty(value = "开发名称")
    private String devName;
    /**
     * 开发编码
     */
    @ApiModelProperty(value = "开发编码")
    private String devCode;

    /**
     * 更新者名称
     */
    @ApiModelProperty(value = "更新者名称")
    private String updateName;

    /**
     * 更新者id
     */
    @ApiModelProperty(value = "更新者id")
    private String updateId;

    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新日期")
    private Date updateDate;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    /**
     * 创建者名称
     */
    @ApiModelProperty(value = "创建者名称")
    private String createName;

    /**
     * 创建者id
     */
    @ApiModelProperty(value = "创建者id")
    private String createId;
}
