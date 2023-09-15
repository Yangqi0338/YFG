package com.base.sbc.module.nodestatus.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：打版-样衣进度列表搜索条件Dto
 *
 * @author lxf
 * @version 1.0
 * @address com.base.sbc.module.NodestatusPageSearchDto.dto.NodestatusPageSearchDto
 * @email 123456789@126.com
 * @date 创建时间：2023-09-12 11:26
 */
@Data
@ApiModel("研发总进度分页筛选类Dto NodestatusPageSearchDto ")
public class NodestatusPageSearchDto extends Page {
    @ApiModelProperty(value = "关键字筛选", example = "1")
    private String search;
    @ApiModelProperty(value = "品类", example = "秋裤")
    private String categoryName;

    @ApiModelProperty(value = "紧急状态", example = "普通")
    private String taskLevelName;

    @ApiModelProperty(value = "生产类型", example = "1")
    private String devtTypeName;

    @ApiModelProperty(value = "设计师id", example = "1223333122223333333")
    private String designerId;

    @ApiModelProperty(value = "工艺员id", example = "1223333122223333333")
    private String patternDesignId;

    @ApiModelProperty(value = "当前节点", example = "打版中")
    private String nodeName;
    private String companyCode;
}
