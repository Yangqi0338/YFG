package com.base.sbc.module.smp.dto;

import com.base.sbc.module.smp.base.SmpBaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 卞康
 * @date 2023/5/10 13:16:10
 * @mail 247967116@qq.com
 * 工艺单
 */
@Data
public class SmpProcessSheetDto extends SmpBaseDto {
    /** 大货款号 */
    private String bulkNumber;
    /** 工艺制单PDF路径 */
    private String pdfUrl;
    /**
     * 修改人
     */
    private String modifiedPerson;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifiedTime;
    /**
     * 同步id
     */
    private String syncId;
    /**
     * plmid
     */
    private String plmId;

    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)")
    private String packType;
}
