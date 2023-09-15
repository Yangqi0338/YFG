package com.base.sbc.module.style.vo;

import cn.hutool.core.util.IdUtil;
import com.base.sbc.config.common.annotation.UserAvatar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.style.vo.DemandOrderSkcVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-13 14:36
 */
@Data
@ApiModel("配色下单维度数据Vo   DemandOrderSkcVo ")
public class DemandOrderSkcVo {


    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;
    @ApiModelProperty(value = "款式设计id")
    private String styleId;

    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;
    @ApiModelProperty(value = "小类名称")
    private String prodCategory3rdName;

    @ApiModelProperty(value = "设计师名称")
    private String designer;
    @ApiModelProperty(value = "设计师id")
    private String designerId;
    @ApiModelProperty(value = "设计师头像")
    @UserAvatar("designerId")
    private String designerAvatar;

    @ApiModelProperty(value = "波段编码")
    private String bandCode;
    @ApiModelProperty(value = "波段名称")
    private String bandName;

    @ApiModelProperty(value = "匹配的波段编码")
    private String matchBandCode;
    @ApiModelProperty(value = "匹配的波段名称")
    private String matchBandName;

    @ApiModelProperty(value = "字段id")
    private String fieldId;
    @ApiModelProperty(value = "字段说明")
    private String fieldExplain;
    @ApiModelProperty(value = "字段名称")
    private String fieldName;
    @ApiModelProperty(value = "字段值")
    private String val;
    @ApiModelProperty(value = "字段值名称")
    private String valName;


    @ApiModelProperty(value = "匹配标志:z0坑位(未匹配),a1完美匹配(坑位),b2手动匹配(坑位),c3手动匹配(配色),d4:未匹配(配色)")
    private String matchFlag;

    @ApiModelProperty(value = "维度企划坑位id")
    private String planningDemandProportionSeatId;

    public String getUid() {
        return IdUtil.randomUUID();
    }
}
