package com.base.sbc.module.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：附件
 * @address com.base.sbc.module.common.vo.AttachmentVo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-12 16:11
 * @version 1.0
 */
@Data
@ApiModel("附件 AttachmentVo ")
public class AttachmentVo {
    @ApiModelProperty(value = "附件id"  )
    private String id;
    @ApiModelProperty(value = "文件id"  )
    private String fileId;
    @ApiModelProperty(value = "名称"  )
    private String name;
    /** 类型 */
    @ApiModelProperty(value = "类型"  )
    private String type;
    /** 大小 */
    @ApiModelProperty(value = "大小"  )
    private BigDecimal size;
    /** 文件地址 */
    @ApiModelProperty(value = "文件地址"  )
    private String url;
}
