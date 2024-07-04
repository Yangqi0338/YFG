/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述： 实体类
 * @address com.base.sbc.module.patternmaking.entity.PatternMakingBarCode
 * @author your name
 * @email your email
 * @date 创建时间：2024-7-3 17:06:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pattern_making_bar_code")
@ApiModel(" PatternMakingBarCode")
public class PatternMakingBarCode extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
    /** 样衣看板id */
    @ApiModelProperty(value = "样衣看板id"  )
    private String headId;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String img;
    /** 状态,0:未确认;1:通过;2:不通过 */
    @ApiModelProperty(value = "状态,0:未确认;1:通过;2:不通过"  )
    private String status;
    /** 意见 */
    @ApiModelProperty(value = "意见"  )
    private String suggestion;
    /** 意见图片0 */
    @ApiModelProperty(value = "意见图片0"  )
    private String suggestionImg;
    /** 意见视频 */
    @ApiModelProperty(value = "意见视频"  )
    private String suggestionVideo;
    /** 坑位 */
    @ApiModelProperty(value = "坑位"  )
    private Integer pitSite;
    /** 样衣条码 */
    @ApiModelProperty(value = "样衣条码"  )
    private String barCode;
    /** 意见图片1 */
    @ApiModelProperty(value = "意见图片1"  )
    private String suggestionImg1;
    /** 意见图片2 */
    @ApiModelProperty(value = "意见图片2"  )
    private String suggestionImg2;
    /** 意见图片3 */
    @ApiModelProperty(value = "意见图片3"  )
    private String suggestionImg3;
    /** 意见图片4 */
    @ApiModelProperty(value = "意见图片4"  )
    private String suggestionImg4;
}
