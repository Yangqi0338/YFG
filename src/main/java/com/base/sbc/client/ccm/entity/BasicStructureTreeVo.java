package com.base.sbc.client.ccm.entity;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("结构管理-类目树查询 BasicStructureTreeVo")
public class BasicStructureTreeVo {

    @ApiModelProperty(value = "编号", example = "691374423193681920")
    private String id;

    private String ids;

    @ApiModelProperty(value = "名称", example = "上衣")
    private String name;
    @ApiModelProperty(value = "编码", example = "sy")
    private String value;

    @ApiModelProperty(value = "父类id", example = "680014765321355265")
    private String parentId;

    @ApiModelProperty(value = "父类ID集合(逗号隔开)", example = "691335048628011008,691335458646392832")
    private String parentIds;
    @ApiModelProperty(value = "类目id", example = "691240876940197888")
    private String structureId;
    @ApiModelProperty(value = "状态:启用(0),停用(1)", example = "1")
    private String status;
    @ApiModelProperty(value = "级别", example = "0")
    private Integer level;
    @ApiModelProperty(value = "排序", example = "`0`")
    private Integer sort;

    @ApiModelProperty(value = "子节点")
    private List<BasicStructureTreeVo> children;

    public String getIds() {
        return IdUtil.getSnowflake().nextIdStr();
    }
}
