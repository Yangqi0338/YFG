package com.base.sbc.module.pack.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.CopyItemsVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-10-28 20:44
 */

@Data
@ApiModel("资料包-引用-返回")
public class CopyItemsVo {

    /**
     * 是否引用历史记录(0否，1是)
     */
    @ApiModelProperty(value = "是否是迁移历史数据")
    private String historicalData;
    /**
     * bom是否引用历史记录(0否，1是)
     */
    @ApiModelProperty(value = "bom是否引用历史记录(0否，1是)")
    private String bomRhdFlag;
    /**
     * bom引用时间
     */
    @ApiModelProperty(value = "bom引用时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bomRhdDate;
    @ApiModelProperty(value = "BOM引用长度")
    private long bomCount;
    /**
     * bom引用人
     */
    @ApiModelProperty(value = "bom引用人")
    private String bomRhdUser;
    /**
     * size是否引用历史记录(0否，1是)
     */
    @ApiModelProperty(value = "size是否引用历史记录(0否，1是)")
    private String sizeRhdFlag;
    /**
     * size引用时间
     */
    @ApiModelProperty(value = "size引用时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sizeRhdDate;
    /**
     * 尺码引用人
     */
    @ApiModelProperty(value = "尺码引用人")
    private String sizeRhdUser;

    @ApiModelProperty(value = "Size引用长度")
    private long sizeCount;
    /**
     * 工艺是否引用历史记录(0否，1是)
     */
    @ApiModelProperty(value = "工艺是否引用历史记录(0否，1是)")
    private String techRhdFlag;
    /**
     * 工艺引用时间
     */
    @ApiModelProperty(value = "工艺引用时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date techRhdDate;
    /**
     * 工艺引用人
     */
    @ApiModelProperty(value = "工艺引用人")
    private String techRhdUser;

    @ApiModelProperty(value = "工艺引用长度")
    private long techCount;
}
