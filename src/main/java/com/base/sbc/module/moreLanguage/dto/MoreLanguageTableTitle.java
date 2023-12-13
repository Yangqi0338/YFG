package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Data
public class MoreLanguageTableTitle {

    private String code;

    private String text;

    private boolean isHidden = false;

    private Integer key;

    private Integer keyName;

}
