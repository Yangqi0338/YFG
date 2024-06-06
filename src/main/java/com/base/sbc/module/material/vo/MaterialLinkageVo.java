package com.base.sbc.module.material.vo;

import lombok.Data;

import java.util.List;

@Data
public class MaterialLinkageVo {

    private String group;

    private List<MaterialChildren> children;




}
