package com.base.sbc.module.planning.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("产品季品类树Vo ProductCategoryTreeVo")
public class ProductCategoryTreeVo {

    private String id;
    private String ids;
    @ApiModelProperty(value = "seat坑位,style,款式")
    private String dataForm = "style";
    /**
     * 企划名称
     */
    @ApiModelProperty(value = "企划名称")
    private String name;
    @ApiModelProperty(value = "季节编码")
    private String season;
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;
    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty(value = "渠道企划id")
    private String planningChannelId;
    @ApiModelProperty(value = "渠道")
    private String channel;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;

    /**
     * 年
     */
    @ApiModelProperty(value = "年")
    private String year;
    /**
     * 字节点数量
     */
    @ApiModelProperty(value = "字节点数量"  )
    private String childCount;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;

    @ApiModelProperty(value = "是否有子节点")
    private Boolean children;
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;

    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 大类id
     */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;

    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;

    public String getLabel() {
        if (level == 0) {
            return name;
        } else if (level == 1) {
            return prodCategory1stName;
        } else if (level == 2) {
            return prodCategoryName;
        }
        return "";
    }

    public String getValue() {
        List<String> val = CollUtil.newArrayList(name, prodCategory1stName, prodCategoryName);
        CollUtil.removeBlank(val);
        return CollUtil.join(val, "/");
    }

    public String getUid() {
        return IdUtil.getSnowflake().nextIdStr();
    }

    @ApiModelProperty(value = "业务对象编码(用于数据权限)")
    private String businessType;

    @ApiModelProperty(value = "是否展示未来款2099：1:展示，0：不展示")
    private String futureStyleStatus = "0";
    @ApiModelProperty(value = "产品季")
    private Object childrens;
}
