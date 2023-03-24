/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package ${javapackage}.${project}.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：${title} 实体类
 * @address ${javapackage}.${project}.entity.${className}
 * @author ${author}
 * @email ${email}
 * @date 创建时间：${.now}
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ${className} extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    <#list columns as propertyName>
    <#if propertyName != 'id'& propertyName != 'companyCode'
    & propertyName != 'createId'& propertyName != 'createName'& propertyName != 'createDate'
    & propertyName != 'updateId'& propertyName != 'updateName'& propertyName != 'updateDate'
    & propertyName != 'delFlag'& propertyName != 'remarks'>
    /** ${remarks[propertyName_index]} */
    private ${columnTypes[propertyName_index]} ${propertyName};
    </#if>
    </#list>
    /*******************************************getset方法区************************************/

}
