package com.base.sbc.pdm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/3/24 11:26:59
 */
@Data
@ApiModel("素材新增DTO MaterialAddDto")
public class MaterialAddDto {
    /** 图片地址 */
    @ApiModelProperty(value = "图片地址" ,required = true,example = "url地址")
    private String pictureUrl;
    /** 图片格式 */
    @ApiModelProperty(value = "图片格式" ,required = true,example = ".png")
    private String pictureFormat;
    /** 图片大小 */
    @ApiModelProperty(value = "图片大小" ,required = true,example = "1080*1920")
    private String pictureSize;
    /** 文件名 */
    @ApiModelProperty(value = "素材文件名称" ,required = true,example = "素材文件名称")
    private String fileName;
    /** 所属素材库 */
    @ApiModelProperty(value = "所属素材库" ,required = true,example = "素材文件名称")

    private String materialLibrary;
    /** 审核状态（0：未审核，1：审核通过，2：审核不通过） */
    private String status;
    /** 所属分类 */
    private String materialType;
    /** 缺点 */
    private String drawback;
    /** 风险评估 */
    private String assess;
}
