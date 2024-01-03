package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumResearchProcessTemplate;
import lombok.Data;

import java.util.List;

@Data
public class BasicsdatumResearchProcessTemplateVo extends BasicsdatumResearchProcessTemplate {
    private List<BasicsdatumResearchProcessNodeVo> templateNodeList;

}
