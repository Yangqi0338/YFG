package com.base.sbc.open.dto;

import com.base.sbc.open.entity.BasicsdatumGarmentInspection;
import lombok.Data;

import java.util.List;

@Data
public class BasicsdatumGarmentInspectionDto extends BasicsdatumGarmentInspection {
    private List<BasicsdatumGarmentInspectionDetailDto> garmentInspectionDetailDtoList;
}
