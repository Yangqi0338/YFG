package com.base.sbc.module.common.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.SpringContextHolder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：附件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.vo.AttachmentVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-12 16:11
 */
@Data
@ApiModel("附件 AttachmentVo ")
public class AttachmentVo {
    @ApiModelProperty(value = "附件id")
    private String id;
    @ApiModelProperty(value = "文件id")
    private String fileId;
    @ApiModelProperty(value = "名称")
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
    @JsonIgnore
    @ApiModelProperty(value = "附件类型")
    private String attachmentType;

    @ApiModelProperty(value = "外键id")
    private String  foreignId;


    public String getUrl() {
        MinioUtils minioUtils = SpringContextHolder.getBean("minioUtils");
        return minioUtils.getObjectUrl(getSourceUrl());
    }

    public String getSourceUrl() {
        return url;
    }

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

    @ApiModelProperty(value = "创建日期时间")
    public String getCreateDateTime() {
        return DateUtil.format(createDate, DatePattern.NORM_DATETIME_PATTERN);
    }
}
