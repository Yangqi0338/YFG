/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：成衣成分送检 实体类
 * @address com.base.sbc.open.entity.BasicsdatumGarmentInspection
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-17 15:18:54
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_garment_inspection")
@ApiModel("成衣成分送检 BasicsdatumGarmentInspection")
public class BasicsdatumGarmentInspection extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 送检日期 */
    @ApiModelProperty(value = "送检日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inspectDate;
    /** 大货款号 */
    @ApiModelProperty(value = "大货款号"  )
    private String styleNo;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
