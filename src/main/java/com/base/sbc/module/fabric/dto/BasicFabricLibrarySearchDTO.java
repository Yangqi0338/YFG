package com.base.sbc.module.fabric.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xhj
 * @Date 2023/6/26 17:53
 */
@Data
@ApiModel(value = "面料开发查询")
public class BasicFabricLibrarySearchDTO extends Page {
    @ApiModelProperty("来源1.新增、2.其他")
    private String source;
    @ApiModelProperty("审核状态：0.未提交、1.待审核、2.审核中、3.审核通过、4.审核失败")
    private String approveStatus;
    @ApiModelProperty("是否转至物料档案 0.否、1.是")
    private String toMaterialFlag;
    @ApiModelProperty("是否物料档案接受 0.否、1.是")
    private String materialAcceptFlag;
    @ApiModelProperty("分类id")
    private String categoryId;

    private String companyCode;

}
