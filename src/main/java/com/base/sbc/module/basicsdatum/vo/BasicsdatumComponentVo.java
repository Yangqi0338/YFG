/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：基础资料-部件 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumComponent
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-10 11:37:55
 * @version 1.0
 */
@Data

public class BasicsdatumComponentVo extends BaseDataEntity<String>   {

    private static final long serialVersionUID = 1L;
    private String id;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private String coding;
    /** 部件类别 */
    @ApiModelProperty(value = "部件类别"  )
    private String componentCategory;
    /** 工艺项目 */
    @ApiModelProperty(value = "工艺项目"  )
    private String technologyProject;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    private String description;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String image;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;



}
