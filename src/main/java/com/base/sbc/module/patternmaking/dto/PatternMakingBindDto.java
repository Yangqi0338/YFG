package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：打版管理dto
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-29 15:24
 * @version 1.0
 */
@Data
@ApiModel("打版管理dto PatternMakingBindDto ")
public class PatternMakingBindDto {


    @ApiModelProperty(value = "款式设计id")
    private String styleId;

    private List<PMBindDetailDto> list;


    @Data
    public static class PMBindDetailDto{

        private String patternRoomId;

        private String supplierStyleNo;

    }

}
