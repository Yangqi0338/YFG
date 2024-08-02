package com.base.sbc.module.common.vo;


import cn.hutool.core.collection.CollUtil;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类描述：下拉选项vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.vo.SelectOptionsVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-25 14:11
 */
@Data
@ApiModel("下拉选择Vo ")
@AllArgsConstructor
public class SelectOptionsChildrenVo {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "label")
    private String label;
    @ApiModelProperty(value = "value")
    private String value;
    @ApiModelProperty(value = "value")
    private List<SelectOptionsChildrenVo> children;

    public SelectOptionsChildrenVo(String id, String label, String value) {
        this.id = id;
        this.label = label;
        this.value = value;
    }

    public SelectOptionsChildrenVo setChildrenByStructure(List<BasicCategoryDot> children, String prefix) {
        if (CollUtil.isNotEmpty(children)) {
            this.setChildren(children.stream().filter(Objects::nonNull).map(it -> {
                String value = prefix + "/" + it.getValue();
                SelectOptionsChildrenVo childrenVo = new SelectOptionsChildrenVo(it.getId(), it.getName(), value);
                childrenVo.setChildrenByStructure(it.getChildren(), value);
                return childrenVo;
            }).collect(Collectors.toList()));
        }
        return this;
    }
}
