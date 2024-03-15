package com.base.sbc.module.planning.vo;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.annotation.FieldDisplay;
import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 类描述：PlanningSummaryDetailVo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.PlanningSummaryDetailVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 10:45
 */
@Data
@ApiModel("企划汇总明细数据 PlanningSummaryDetailVo")
public class PlanningSummaryDetailVo {

    @ApiModelProperty(value = "坑位信息id", example = "719579649834745856")
    private String id;
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    @ApiModelProperty(value = "渠道id"  )
    private String planningChannelId;
    @ApiModelProperty(value = "设计款号", example = "5CA232731")
    private String designNo;

    @ApiModelProperty(value = "大货款号", example = "5CA232731")
    private String styleNo;

    @ApiModelProperty(value = "关联历史款号", example = "5CA232731")
    private String hisDesignNo;

    @ApiModelProperty(value = "款号", example = "5CA232731")
    @FieldDisplay(value = "款号", display = true)
    private String finalNo;

    @ApiModelProperty(value = "波段", example = "2C")
    @FieldDisplay(value = "波段", display = true)
    private String bandName;

    @ApiModelProperty(value = "波段(编码)"  )
    private String bandCode;
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;
    @ApiModelProperty(value = "大类code"  )
    private String prodCategory1st;
    /** 品类code */
    @ApiModelProperty(value = "品类code"  )
    private String prodCategory;
    /** 中类code */
    @ApiModelProperty(value = "中类code"  )
    private String prodCategory2nd;
    /** 小类code */
    @ApiModelProperty(value = "小类code"  )
    private String prodCategory3rd;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String prodCategory1stName;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String prodCategoryName;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String prodCategory2ndName;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称"  )
    private String prodCategory3rdName;

    @ApiModelProperty(value = "图片", example = "http://sdr.saas123.com/static/img/defaultUser.png")
    private String stylePic;

    @ApiModelProperty(value = "价格带", example = "100-200")
    @FieldDisplay(value = "价格带", display = true)
    private String price;

    @ApiModelProperty(value = "设计师名称")
    private String designer;

    @ApiModelProperty(value = "设计师id")
    private String designerId;

    @ApiModelProperty(value = "设计师头像")
    @UserAvatar("designerId")
    private String designerAvatar;

    @ApiModelProperty(value = "销量")
    private BigDecimal salesVolume;

    @ApiModelProperty(value = "产销")
    private BigDecimal productionMarketing;

    @ApiModelProperty(value = "衣长")
    private BigDecimal clothesLength;

    @ApiModelProperty(value = "色系")
    @FieldDisplay(value = "色系")
    private String colorSystem;

    @ApiModelProperty(value = "面料")
    @FieldDisplay(value = "面料", display = true)
    private String fabric;

    @ApiModelProperty(value = "克重")
    @FieldDisplay(value = "克重", display = true)
    private String gramWeight;

    @ApiModelProperty(value = "应季节")
    @FieldDisplay(value = "应季节")
    private String seasonal;

    @ApiModelProperty(value = "延续点")
    @FieldDisplay(value = "延续点")
    private String continuationPoint;

    public String getDesignerName() {
        if (StrUtil.isNotBlank(designer)) {
            return designer.split(StrUtil.COMMA)[0];
        }
        return "";
    }

    public String getFinalNo() {
        return Opt.ofBlankAble(styleNo).orElse(designNo);
    }
    @ApiModelProperty(value = "维度信息")
    private Map<String,String> dimension;

    @ApiModelProperty(value = "维度信息列表")
    private List<FieldVal> fieldValList;
}
