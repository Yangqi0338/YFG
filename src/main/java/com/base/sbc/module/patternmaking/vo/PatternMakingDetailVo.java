package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.sample.vo.SampleDesignVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：打板管理明细vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingDetailVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 15:25
 */
@Data
@ApiModel("打版管理明细vo PatternMakingDetailVo ")
public class PatternMakingDetailVo extends PatternMaking {

    @ApiModelProperty(value = "样衣设计信息")
    private SampleDesignVo sampleDesign;


    @ApiModelProperty(value = "附件")
    private List<AttachmentVo> attachmentList;


}
