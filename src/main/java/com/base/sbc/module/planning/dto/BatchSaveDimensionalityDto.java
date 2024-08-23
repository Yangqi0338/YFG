package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

/*编辑维度标签*/
@Data
@ApiModel("企划维度-编辑维度标签 BatchSaveDimensionalityDto")
public class BatchSaveDimensionalityDto {

    private List<UpdateDimensionalityDto> tree;

    private List<UpdateDimensionalityDto> field;

    private String type;
}
