/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package ${javapackage}.${project}.entity;
<#if hasBigDecimal >
import java.math.BigDecimal;
</#if>
<#if hasDate >
import java.util.Date;
</#if>
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("${tableName}")
@ApiModel("${title} ${className}")
public class ${className} extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    <#list columns as propertyName>
    <#if propertyName != 'id'& propertyName != 'companyCode'
    & propertyName != 'createId'& propertyName != 'createName'& propertyName != 'createDate'
    & propertyName != 'updateId'& propertyName != 'updateName'& propertyName != 'updateDate'
    & propertyName != 'delFlag' >
    /** ${remarks[propertyName_index]} */
    @ApiModelProperty(value = "${remarks[propertyName_index]}"  )
    private ${columnTypes[propertyName_index]} ${propertyName};
    </#if>
    </#list>
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
