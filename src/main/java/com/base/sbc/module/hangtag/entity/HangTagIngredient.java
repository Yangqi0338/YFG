package com.base.sbc.module.hangtag.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.BigDecimalJsonSerializer;
import com.base.sbc.config.JacksonHttpMessageConverter;
import com.base.sbc.config.annotation.DuplicateSql;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;

/**
 * 吊牌成分表实体类
 */
@Data
@TableName("t_hang_tag_ingredient")
public class HangTagIngredient extends BaseDataEntity<String> {

    /** 吊牌id */
    private String hangTagId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 成分名称 */
    private String ingredientName;

    /** 成分编码 */
    private String ingredientCode;

    /** 类型编码 */
    private String typeCode;

    /** 类型 */
    private String type;

    /** 百分比 */
//    @JsonSerialize(using = BigDecimalJsonSerializer.class)
    @JsonIgnore
    private BigDecimal percentage;

    @JsonIgnore
    @DuplicateSql(columnName = "percentage_str")
    private String percentageStr;

    @JsonProperty("percentage")
    public String getPercentageStr(){
        return StrUtil.isBlank(percentageStr) ? (percentage == null ? null : percentage.toString()) : percentageStr;
    }

    /** 成分备注 */
    private String descriptionRemarks;

    /** 成分备注编码 */
    private String descriptionRemarksCode;

    /** 是否换行 */
    private String wrapOrNot;

    /** 成分说明 */
    private String ingredientDescription;

    /** 成分英文 */
    private String descriptionRemarksCn;

    /** 成分法文 */
    private String descriptionRemarksFr;

    /** 成分说明编码 */
    private String ingredientDescriptionCode;

    /** 成分说明英文 */
    private String ingredientDescriptionCn;

    /** 成分说明法文 */
    private String ingredientDescriptionFr;
}
