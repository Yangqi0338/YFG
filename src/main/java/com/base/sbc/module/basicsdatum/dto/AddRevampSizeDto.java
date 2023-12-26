package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增修改尺码Dto类
 * */
@Data
public class AddRevampSizeDto {

    private String id;
    private String extSizeCode;
    /** 吊牌打印 显示尺码标识(0显示，1隐藏)*/
    @ApiModelProperty(value = "吊牌打印 显示尺码标识(0显示，1隐藏)"  )
    private String hangTagShowSizeStatus;
    /** 吊牌显示 */
    @ApiModelProperty(value = "吊牌显示"  )
    private String hangtags;
    /** 号型 */
    @ApiModelProperty(value = "号型"  )
    private String model;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String modelType;
    /*号型类型编码*/
    private String modelTypeCode;
    /** Internal Size */
    @ApiModelProperty(value = "Internal Size"  )
    private String internalSize;
    /** External Size */
    @ApiModelProperty(value = "External Size"  )
    private String externalSize;
    /** 代码 */
    @ApiModelProperty(value = "代码"  )
    private String code;
    /** 排序 */
    @ApiModelProperty(value = "排序"  )
    private String sort;
    /** 发送状态(0未发送,1已发送) */
    @ApiModelProperty(value = "发送状态(0未发送,1已发送)"  )
    private String sendStatus;
    /** 数据状态(0新建,1更改) */
    @ApiModelProperty(value = "数据状态(0新建,1更改)"  )
    private String dataStatus;
    /** Dimension Type */
    @ApiModelProperty(value = "Dimension Type"  )
    private String dimensionType;
    /** 翻译名称 */
    @ApiModelProperty(value = "翻译名称"  )
    private String translateName;
    /** 欧码 */
    @ApiModelProperty(value = "欧码"  )
    private String europeanSize;
    /** 显示尺码标识 */
    @ApiModelProperty(value = "显示尺码标识"  )
    private String showSizeStatus;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 编码（笛莎） */
    @ApiModelProperty(value = "编码（笛莎）"  )
    private String realCode;
}
