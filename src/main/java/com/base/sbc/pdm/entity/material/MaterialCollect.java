/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.entity.material;

import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：素材收藏表 实体类
 * @date 创建时间：2023-3-24 9:44:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialCollect extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 用户id */
    private String userId;
    /** 素材id */
    private String materialId;
    /*******************************************getset方法区************************************/

}

