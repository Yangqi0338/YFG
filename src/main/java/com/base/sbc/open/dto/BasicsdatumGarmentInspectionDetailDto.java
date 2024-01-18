package com.base.sbc.open.dto;

import com.base.sbc.open.entity.BasicsdatumGarmentInspectionDetail;
import lombok.Data;

import java.util.List;

@Data
public class BasicsdatumGarmentInspectionDetailDto extends BasicsdatumGarmentInspectionDetail {
    /**
     * 附件报告集合
     */
    private List<String> attachmentUrlList;
}
