package com.base.sbc.open.dto;

import com.base.sbc.open.entity.BasicsdatumGarmentInspection;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BasicsdatumGarmentInspectionDto extends BasicsdatumGarmentInspection {


    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectDate;

    private List<BasicsdatumGarmentInspectionDetailDto> garmentInspectionDetailDtoList;
}
