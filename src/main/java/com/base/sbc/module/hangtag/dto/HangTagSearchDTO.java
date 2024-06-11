package com.base.sbc.module.hangtag.dto;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xhj
 * @Date 2023/6/26 17:53
 */
@Data
@ApiModel(value = "吊牌列表查询")
public class HangTagSearchDTO extends QueryFieldDto {

    /**
     * 款式
     */
    @ApiModelProperty(value = "款式")
    private String style;

    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /**
     * 成分
     */
    @ApiModelProperty(value = "成分")
    private String ingredient;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 确认时间
     */
    @ApiModelProperty(value = "确认时间")
    private String confirmDate;
    /**
     * 翻译确认时间
     */
    @ApiModelProperty(value = "翻译确认时间")
    private String translateConfirmDate;
    /**
     * 款号
     */
    @ApiModelProperty(value = "款号")
    private String styleNo;
    /**
     * 款号是否模糊查询 1是、0否
     */
    @ApiModelProperty(value = "款号是否模糊查询")
    private String likeQueryFlag;
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    private String companyCode;
    @ApiModelProperty(value = "查询类型：packBigGoods.标准资料包")
    private String selectType;

    @ApiModelProperty(value = "品名编码")
    private String  productCode;

    @ApiModelProperty(value = "品类编码")
    private String  prodCategory;


    private String[] productCodes;

    private String[]   prodCategorys;

    private String checkType;

    private String[] bulkStyleNos;

    private String[] designNos;

    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;

    private String[] bandNames;

    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;

    @ApiModelProperty(value = "导出标记")
    private String deriveFlag;

    @ApiModelProperty(value = "二检包装形式")
    private String secondPackagingFormCode;

    @ApiModelProperty(value = "包装形式")
    private String  packagingFormCode;

    @ApiModelProperty(value = "是否导图片")
    private String  imgFlag;

    @ApiModelProperty(value = "年份")
    private String  year;

    private String produceTypeName;

    /**
     * 工艺师
     */
    @ApiModelProperty(value = "工艺师")
    private String technologistName;

    /**
     * 下单员
     */
    @ApiModelProperty(value = "下单员")
    private String placeOrderStaffName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String placeOrderDate;
}
