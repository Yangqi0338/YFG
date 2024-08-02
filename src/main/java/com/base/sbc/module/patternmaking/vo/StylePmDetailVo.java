package com.base.sbc.module.patternmaking.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.style.vo.StyleVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：款式设计+打板指令明细vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingDetailVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 15:25
 */
@Data
@ApiModel("款式设计+打板指令明细 SampleDesignPmDetailVo ")
public class StylePmDetailVo extends StyleVo {

    @ApiModelProperty(value = "打版指令")
    private PatternMakingVo patternMaking;

    @ApiModelProperty(value = "版型库文件地址 由于技术部看不了设计部的东西 所以需要在打版任务中展示链接并下载")
    private String patternLibraryFileUrl;


    public String getDesignerName() {
        String designer = getDesigner();
        if (StrUtil.contains(designer, StrUtil.COMMA)) {
            return designer.split(",")[0];
        }
        return designer;
    }
}
