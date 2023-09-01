package com.base.sbc.module.planning.vo;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("产品季品类树Vo ProductCategoryTreeVo")
public class ProductAllCategoryTreeVo {

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

    @ApiModelProperty(value = "是否有子节点")
    private List<BasicStructureTreeVo> children;
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
}
