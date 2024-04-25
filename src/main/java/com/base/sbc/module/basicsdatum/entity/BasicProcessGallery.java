package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-03-11 11:08:53
 * @mail 247967116@qq.com
 * 基础工艺图库
 */
@Data
@TableName("t_basic_process_gallery")
public class BasicProcessGallery extends BaseDataEntity<String> {
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 类型编码
     */
    @ApiModelProperty(value = "类型编码")
    private String typeCode;

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String typeName;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;

    /**
     * 文件Id
     */
    @ApiModelProperty(value = "文件Id")
    private String fileId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private  String remarks;
}
