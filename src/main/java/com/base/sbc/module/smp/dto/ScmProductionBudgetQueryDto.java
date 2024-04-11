package com.base.sbc.module.smp.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScmProductionBudgetQueryDto {

    private List<String> brandList;
    private List<String> yearList;
    private List<String> seasonList;

}