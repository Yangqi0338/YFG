package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PrintCheckVo {

    @ApiModelProperty(value = "设计师未确认列表")
    private List<FabricSummaryInfoVo> designerNotlist;

    public PrintCheckVo(List<FabricSummaryInfoVo> designerNotlist) {
        this.designerNotlist = designerNotlist;
    }
}
