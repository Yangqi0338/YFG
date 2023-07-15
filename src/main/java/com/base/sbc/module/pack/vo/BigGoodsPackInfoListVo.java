package com.base.sbc.module.pack.vo;

import com.base.sbc.module.planning.utils.PlanningUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：标准资料包分页查询
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.BigGoodsPackInfoListVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-12 11:26
 */
@Data
@ApiModel("标准资料包-列表返回Vo BigGoodsPackInfoListVo")
public class BigGoodsPackInfoListVo {

    @ApiModelProperty(value = "id")
    private String id;

    @JsonIgnore
    @ApiModelProperty(value = "主数据id(样衣设计id)")
    private String foreignId;

    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "款式名称")
    private String styleName;

    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)")
    private String categoryName;

    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @ApiModelProperty(value = "配色")
    private String color;
    @ApiModelProperty(value = "生产模式")
    private String devtType;

    @ApiModelProperty(value = "款式图")
    private String stylePic;

    @ApiModelProperty(value = "设计转后技术确认:(0未确认,1已确认)")
    private String designTechConfirm;

    @ApiModelProperty(value = "设计转后技术确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designTechConfirmDate;

    @ApiModelProperty(value = "大货制单员确认:(0未确认,1已确认)")
    private String bulkOrderClerkConfirm;

    @ApiModelProperty(value = "大货制单员确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bulkOrderClerkConfirmDate;

    @ApiModelProperty(value = "大货工艺员确认:(0未确认,1已确认)")
    private String bulkProdTechConfirm;

    @ApiModelProperty(value = "大货工艺员确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bulkProdTechConfirmDate;

    @ApiModelProperty(value = "后技术确认:(0未确认,1已确认)")
    private String postTechConfirm;

    @ApiModelProperty(value = "后技术确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postTechConfirmDate;

    @ApiModelProperty(value = "品控部确认:(0未确认,1已确认)")
    private String qcConfirm;

    @ApiModelProperty(value = "品控部确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qcConfirmDate;

    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发")
    private String scmSendFlag;

    @ApiModelProperty(value = "反审状态：待审核(1)、审核通过(2)、被驳回(-1)")
    private String reverseConfirmStatus;


    @ApiModelProperty(value = "bom状态:(0样品,1大货)")
    private String bomStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createName;


    @ApiModelProperty(value = "创建人Id")
    private String createId;

    public String getProdCategoryName() {
        return PlanningUtils.getProdCategoryName(categoryName);
    }

    public String getStyle() {
        return designNo + styleName;
    }

    public String getSampleDesignId() {
        return foreignId;
    }
}
