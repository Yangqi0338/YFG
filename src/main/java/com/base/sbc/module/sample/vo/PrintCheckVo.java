package com.base.sbc.module.sample.vo;

import com.base.sbc.module.fabricsummary.entity.FabricSummaryStyle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PrintCheckVo {

    @ApiModelProperty(value = "设计师未确认列表")
    private List<FabricSummaryStyle> designerNotlist;

    public PrintCheckVo(List<FabricSummaryStyle> designerNotlist) {
        this.designerNotlist = designerNotlist;
    }
}
