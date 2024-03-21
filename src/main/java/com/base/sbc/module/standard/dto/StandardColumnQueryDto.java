package com.base.sbc.module.standard.dto;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StandardColumnQueryDto extends Page {

    @ApiModelProperty(value = "标准类型")
    private StandardColumnType type;

    @ApiModelProperty(value = "标准类型")
    private List<StandardColumnType> typeList;

    @ApiModelProperty(value = "标准使用模式")
    private StandardColumnModel noModel;

    @ApiModelProperty(value = "code列表")
    private List<String> codeList;

    @ApiModelProperty(value = "code列表")
    private YesOrNoEnum showFlag = YesOrNoEnum.YES;

}
