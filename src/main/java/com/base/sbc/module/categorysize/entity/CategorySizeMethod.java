/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.categorysize.entity;

import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类描述：品类尺寸量法 实体类
 * @address com.base.sbc.basedata.entity.CategorySizeMethod
 * @author youkehai
 * @email 717407966@qq.com
 * @date 创建时间：2021-5-6 16:34:43
 * @version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CategorySizeMethod extends BaseDataEntity<String> implements Cloneable{

	private static final long serialVersionUID = 1L;

	private List<String> sizeList;
	private List<String> standardList;

    /** 品类名称（对应CCM的品类名称） */
    private String categoryName;
    /** 部位名称 */
    private String partName;
    /** 量法 */
    private String method;
    /** 尺寸(对应CCM中的品类的尺寸) */
    private String size;
    /** 公差 */
    private String tolerance;
    /** 标准值 */
    private String standard;
    /** 说明 */
    private String tips;
    /** 图片 */
    private String image;
    /** 码差 */
    private String codeError;
    /** 基码 */
    private String baseSize;
}

