package com.base.sbc.client.ccm.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("结构管理-筛选dto")
public class BasicStructureSearchDto {
    private String codes;
    private String categoryIds;
    private String structureCode;
}
