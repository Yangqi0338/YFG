package com.base.sbc.module.basicsdatum.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProcessDatabaseSelectVO {
    private String id;
    private String code;
    @JsonProperty("name")
    private String processName;

}
