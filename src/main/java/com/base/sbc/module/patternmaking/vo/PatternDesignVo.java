package com.base.sbc.module.patternmaking.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashMap;

/**
 * 类描述：版师信息 PatternDesignVo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternDesignVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-01 11:07
 */
@Data
@ApiModel("版师信息 PatternDesignVo ")
public class PatternDesignVo {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户名称")
    private String aliasUserName;

    @ApiModelProperty(value = "用户头像")
    private String aliasUserAvatar;

    @ApiModelProperty(value = "用户手机")
    private String userPhone;

    @ApiModelProperty(value = "用户部门")
    private String deptName;
    @ApiModelProperty(value = "打版类型数量")
    private LinkedHashMap<String, Long> sampleTypeCount;
}
