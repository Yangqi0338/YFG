/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.replay.dto;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：基础资料-复盘评分-详情Vo 实体类
 *
 * @author KC
 * @version 1.0
 * @address com.base.sbc.module.replay.vo.ReplayRatingDetailVo
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@ApiModel("基础资料-复盘评分-详情 ReplayRatingDetailVo")
public class ReplayRatingDetailDTO extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 复盘评分id */
    @ApiModelProperty(value = "复盘评分id")
    private String replayRatingId;
    /** 评分类型 0评分 1后续改善 */
    @ApiModelProperty(value = "评分类型 0评分 1后续改善")
    private String type;
    /** 维度类型 0默认 1版型 2面料 3颜色 */
    @ApiModelProperty(value = "维度类型 0默认 1版型 2面料 3颜色")
    private String dimensionType;
    /** 描述 */
    @ApiModelProperty(value = "描述")
    private String description;
    /** 评级 */
    @ApiModelProperty(value = "评级")
    private Integer level;
    /** 文件id */
    @ApiModelProperty(value = "文件id")
    private String fileId;
    /** 文件格式 */
    @ApiModelProperty(value = "文件格式")
    private String fileType;
    /** 跟进人id */
    @ApiModelProperty(value = "跟进人id")
    private String followUserId;
    /** 责任人id */
    @ApiModelProperty(value = "责任人id")
    private String userId;
    /** 扩展字段 (typeName|dimensionTypeName|fileUrl|followUserName|userName) */
    @ApiModelProperty(value = "扩展字段 (typeName|dimensionTypeName|fileUrl|followUserName|userName)")
    private String extend;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
