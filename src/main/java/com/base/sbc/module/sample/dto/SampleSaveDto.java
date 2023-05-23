package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.sample.vo.TechnologyInfoDto;
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
@ApiModel("样衣保存修改 SampleDto")
public class SampleSaveDto extends Sample {


    @ApiModelProperty(value = "工艺信息")
    private List<TechnologyInfoDto> technologyInfo;

    @ApiModelProperty(value = "款式图片信息")
    private List<SampleAttachmentDto> stylePicList;

    @ApiModelProperty(value = "附件信息")
    private List<SampleAttachmentDto> attachmentList;

    @ApiModelProperty(value = "关联的素材库")
    private List<MaterialVo> materialList;

}
