package com.base.sbc.module.pack.vo;

import com.base.sbc.module.common.vo.AttachmentVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：资料包-基础信息列表Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackInfoLisVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-06 17:41
 */
@Data
@ApiModel("资料包-基础信息列表Vo PackInfoLisVo")
public class PackInfoListVo extends PackInfoStatusVo {


    @JsonIgnore
    @ApiModelProperty(value = "主数据id(款式设计id)")
    private String foreignId;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "款式id")
    private String styleId;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "款式图")
    private String stylePic;
    /**
     * 款式名称
     */
    @ApiModelProperty(value = "款式名称")
    private String styleName;


    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @ApiModelProperty(value = "生产模式")
    private String devtType;
    @ApiModelProperty(value = "生产模式名称")
    private String devtTypeName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "创建人")
    private String createName;

    @ApiModelProperty(value = "配色")
    private String color;
    @ApiModelProperty(value = "编号")
    private String code;


    public String getStyle() {
        return designNo + styleName;
    }

    @ApiModelProperty(value = "工艺说明文件信息")
    private AttachmentVo techSpecFile;

}
