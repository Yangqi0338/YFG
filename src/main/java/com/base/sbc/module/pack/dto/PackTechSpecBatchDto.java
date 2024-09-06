package com.base.sbc.module.pack.dto;

import com.base.sbc.module.pack.entity.PackTechPackaging;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class PackTechSpecBatchDto {

    @Valid
    private List<PackTechSpecDto> packTechSpecDtos;


    private PackTechPackaging packTechPackaging;

}
