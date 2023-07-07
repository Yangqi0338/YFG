package com.base.sbc.module.common.vo;

import cn.hutool.core.io.unit.DataSizeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 大小
     */
    @ApiModelProperty(value = "大小")
    private BigDecimal size;
    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    private String url;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "上传时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "上传人")
    private String createName;

    /**
     * 获取大小
     *
     * @return
     */
    public String getSizeDesc() {
        if (size == null) {
            return "";
        }
        return DataSizeUtil.format(size.toBigInteger().longValue());
    }
}
