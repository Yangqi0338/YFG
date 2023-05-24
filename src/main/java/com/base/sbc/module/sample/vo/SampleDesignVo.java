package com.base.sbc.module.sample.vo;


import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.fieldManagement.vo.FieldManagementVo;
import com.base.sbc.module.sample.entity.SampleDesign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣明细
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.SampleVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-11 11:48
 */
@Data
@ApiModel("样衣设计明细返回 SampleVo ")
public class SampleDesignVo extends SampleDesign {

    @ApiModelProperty(value = "工艺信息")
    private List<FieldManagementVo> technologyInfo;

    @ApiModelProperty(value = "附件")
    private List<AttachmentVo> attachmentList;
    @ApiModelProperty(value = "款式图片信息")
    private List<AttachmentVo> stylePicList;
    @ApiModelProperty(value = "关联的素材库")
    private List<MaterialVo> materialList;
}
