/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：工艺信息 dto
 * @address com.base.sbc.module.sample.dto.TechnologyDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:43
 * @version 1.0
 */
@Data
@ApiModel("工艺信息 TechnologyDto")
public class TechnologyDto  {

    @ApiModelProperty(value = "id")
    private String id;
    /** 关联id */
    @ApiModelProperty(value = "关联id"  )
    private String fId;
    /** 衣长分类 */
    @ApiModelProperty(value = "衣长分类"  )
    private String lengthRange;
    /** 门襟 */
    @ApiModelProperty(value = "门襟"  )
    private String menJing;
    /** 领型 */
    @ApiModelProperty(value = "领型"  )
    private String lingXing;
    /** 胸围 */
    @ApiModelProperty(value = "胸围"  )
    private String xiongWei;
    /** 花型 */
    @ApiModelProperty(value = "花型"  )
    private String printting;
    /** 腰型 */
    @ApiModelProperty(value = "腰型"  )
    private String yaoXing;
    /** 款式工艺 */
    @ApiModelProperty(value = "款式工艺"  )
    private String productSpecs;
    /** 肩宽 */
    @ApiModelProperty(value = "肩宽"  )
    private String jianKuan;
    /** 袖长 */
    @ApiModelProperty(value = "袖长"  )
    private String xiuChang;
    /** 毛纱针法 */
    @ApiModelProperty(value = "毛纱针法"  )
    private String yarnNeedle;
    /** 袖型 */
    @ApiModelProperty(value = "袖型"  )
    private String xiuXing;
    /** 毛纱针型 */
    @ApiModelProperty(value = "毛纱针型"  )
    private String yarnNeedleType;
    /** 材质 */
    @ApiModelProperty(value = "材质"  )
    private String caiZhi;
    /** 衣长(CM) */
    @ApiModelProperty(value = "衣长(CM)"  )
    private String coatLength;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    private String keZhong;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
}

