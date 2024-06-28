package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.module.patternmaking.entity.PatternMaking;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

/**
 * 类描述： 打版管理 vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-29 13:44
 */
@Data
@ApiModel("打版管理vo PatternMakingVo ")
public class PatternMakingListVo extends PatternMaking {
    @ApiModelProperty(value = "尺码")
    private String productSizes;
    @ApiModelProperty(value = "默认尺码")
    private String defaultSize;

    private String isUpdate;
    /**
     * 打版难度
     */
    @ApiModelProperty(value = "打版指令打版难度")
    private String patDiff;
    @ApiModelProperty(value = "打版指令打版难度")
    private String patDiffName;
    /**
     * 打版难度
     */
    @ApiModelProperty(value = "样衣打版难度")
    private String sdPatDiff;
    @ApiModelProperty(value = "样衣打版难度")
    private String sdPatDiffName;
    @Override
    public String getPatDiff() {
        return Optional.ofNullable(patDiff).orElse(sdPatDiff);
    }

}
