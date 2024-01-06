package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVerifyResultVO {

    private String sheetName;

    private Integer rowIndex;

    private Map<String, String> errorMessageList;

}

