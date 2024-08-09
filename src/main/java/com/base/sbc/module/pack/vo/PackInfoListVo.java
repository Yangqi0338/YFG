package com.base.sbc.module.pack.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

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


    @ApiModelProperty(value = "主数据id(款式设计id)")
    private String foreignId;


    @ApiModelProperty(value = "资料包类型")
    private String packType;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "款式id")
    private String styleId;
    @ApiModelProperty(value = "配色id")
    private String styleColorId;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "款式图")
    private String stylePic;
    @ApiModelProperty(value = "大货款图")
    private String styleColorPic;
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
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "样板号")
    private String patternNo;

    public String getStyle() {
        return Optional.ofNullable(designNo).orElse("") + Optional.ofNullable(styleName).orElse("");
    }

    @ApiModelProperty(value = "工艺说明文件信息(pdf)")
    private AttachmentVo techSpecFile;
    @ApiModelProperty(value = "工艺说明视频)")
    private AttachmentVo techSpecVideo;

    public String getStylePic() {
        if (StrUtil.isNotBlank(styleColorPic)) {
            return styleColorPic;
        }
        return stylePic;
    }

    @ApiModelProperty(value = "品牌")
    private String brand;

    /**
     * 工艺接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "工艺接收时间")
    private Date techReceiveDate;

    /**
     * 下单员部门
     */
    @ApiModelProperty(value = "下单员部门")
    private String orderDept;

    private String orderDeptId;

}
