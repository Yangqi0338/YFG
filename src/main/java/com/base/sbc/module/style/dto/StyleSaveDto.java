package com.base.sbc.module.style.dto;

import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.sample.dto.SampleAttachmentDto;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.style.entity.Style;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SampleDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:41
 */
@Data
@ApiModel("款式设计保存修改 SampleDto")
public class StyleSaveDto extends Style {


    @ApiModelProperty(value = "工艺信息")
    private List<FieldVal> technologyInfo;

    @ApiModelProperty(value = "款式图片信息")
    private List<SampleAttachmentDto> stylePicList;

    @ApiModelProperty(value = "附件信息")
    private List<SampleAttachmentDto> attachmentList;

    @ApiModelProperty(value = "附件信息")
    private List<SampleAttachmentDto> attachmentList1;

    @ApiModelProperty(value = "关联的素材库")
    private List<MaterialVo> materialList;

    @ApiModelProperty(value = "打标类型")
    private String markingType;

    @ApiModelProperty(value = "款式配色Id")
    private String styleColorId;

    /**
     * 下单阶段-审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "下单阶段-审批状态:草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)"  )
    private String orderAuditStatus;
    /**
     * 下单阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)
     */
    @ApiModelProperty(value = "下单阶段-打标状态:未打标(0)、部分打标(1)、全部打标(2)"  )
    private String orderMarkingStatus;

}
