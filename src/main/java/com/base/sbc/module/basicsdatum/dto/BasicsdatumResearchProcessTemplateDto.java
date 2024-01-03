package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

import java.util.List;

@Data
public class BasicsdatumResearchProcessTemplateDto extends Page {
    /**模板id*/
    private String id;
    /** 模板名称 */
    private String templateName;
    /** 品牌code */
    private String brandCode;
    /** 品牌名称 */
    private String brandName;
    /** 生产类型 */
    private String productType;
    /** 生产类型名称 */
    private String productName;
    /** 推送方式 0:代表正推 ， 1 代表倒推*/
    private String type;
    /** 节点信息 */
    private List<BasicsdatumResearchProcessNodeDto> nodeDtoList;
}
