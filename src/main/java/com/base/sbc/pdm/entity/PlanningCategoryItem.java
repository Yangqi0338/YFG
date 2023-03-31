/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;
/**
 * 类描述：企划-坑位信息 实体类
 * @address com.base.sbc.pdm.entity.PlanningCategoryItem
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:47:40
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanningCategoryItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    /** 波段企划id */
    @ApiModelProperty(value = "波段企划id"  )
    private String planningBandId;
    /** 品类信息id */
    @ApiModelProperty(value = "品类信息id"  )
    private String planningCategoryId;
    /** 款式图 */
    @ApiModelProperty(value = "款式图"  )
    private String stylePic;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designNo;
    /** 品类id(小类) */
    @ApiModelProperty(value = "品类id(小类)"  )
    private String categoryId;
    /** 品类名称路径:(中类/小类) */
    @ApiModelProperty(value = "品类名称路径:(中类/小类)"  )
    private String categoryName;
    /** 品类id路径:(中类/小类) */
    @ApiModelProperty(value = "品类id路径:(中类/小类)"  )
    private String categoryIds;
    /** 价格带 */
    @ApiModelProperty(value = "价格带"  )
    private String price;
    /** 关联历史款 */
    @ApiModelProperty(value = "关联历史款"  )
    private String hisDesignNo;
    /*******************************************getset方法区************************************/

}

