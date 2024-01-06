package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Data
public class MoreLanguageTableTitle {

    private String code;

    private String text;

    /**
     * 0是不隐藏
     * 1是隐藏
     * 2是excel去除
     * 3是web去除
     * */
    private String hidden;

    private String handler;

    private Integer key;

    private Integer keyName;

    private Double excelWidth;

    public boolean hiddenRemove(){
        return MoreLanguageTableContext.MoreLanguageTableParamEnum.IN_EXCEL.isTrue() ? isExcelRemove() : isWebRemove();
    }

    public boolean isExcelHidden(){
        return "1".equals(hidden);
    }

    public boolean isExcelRemove(){
        return "2".equals(hidden);
    }

    public boolean isWebRemove(){
        return "1".equals(hidden) || "3".equals(hidden);
    }

}
