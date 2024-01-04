/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：研发总进度节点 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessNode
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-2 17:47:09
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_research_process_node")
@ApiModel("研发总进度节点 BasicsdatumResearchProcessNode")
public class BasicsdatumResearchProcessNode extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 研发总进度模板id */
    @ApiModelProperty(value = "研发总进度模板id"  )
    private String templateId;
    /** 节点编码 */
    @ApiModelProperty(value = "节点编码"  )
    private String code;
    /** 日期类型(0:工作日，1：自然日) */
    @ApiModelProperty(value = "日期类型(0:工作日，1：自然日)"  )
    private Integer dateType;
    /** 天数 */
    @ApiModelProperty(value = "天数"  )
    private Integer numberDay;
    /** 顺延星期几 */
    @ApiModelProperty(value = "顺延星期几"  )
    private Integer weekDay;
    /** 排序 */
    @ApiModelProperty(value = "排序"  )
    private Integer sort;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
