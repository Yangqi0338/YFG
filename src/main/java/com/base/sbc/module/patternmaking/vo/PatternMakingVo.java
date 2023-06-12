package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * 类描述：打版管理Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-12 14:47
 */
@Data
@ApiModel("打版管理Vo PatternMakingVo")
public class PatternMakingVo extends PatternMaking {


    @ApiModelProperty(value = "纸样文件")
    private List<AttachmentVo> attachmentList;

    @ApiModelProperty(value = "节点信息list")
    private List<NodeStatus> nodeStatusList;
    @ApiModelProperty(value = "节点信息Map")
    private Map<String, NodeStatus> nodeStatus;
}
