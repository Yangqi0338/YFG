package com.base.sbc.module.sample.vo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("设计档案左侧树 vo DesignDocTreeVo ")
public class DesignDocTreeVo {

    @ApiModelProperty(value = "显示值"  )
    private String label;
    @ApiModelProperty(value = "值"  )
    private String value;
    @ApiModelProperty(value = "级别", example = "0")
    private Integer level;
    @ApiModelProperty(value = "年份")
    private String year;
    @ApiModelProperty(value = "季节")
    private String season;
    @ApiModelProperty(value = "波段")
    private String bandCode;
    @ApiModelProperty(value = "品类id路径:(大类/品类/中类/小类)")
    private String categoryIds;

    @ApiModelProperty(value = "大类id")
    private String prodCategory1st;
    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private String prodCategory;
    @ApiModelProperty(value = "是否有子节点")
    private Boolean children;

    public String getValue() {
        List<String> val = CollUtil.newArrayList(year, season, bandCode, prodCategory1st, prodCategory);
        CollUtil.removeBlank(val);
        return CollUtil.join(val, "/");
    }
}
