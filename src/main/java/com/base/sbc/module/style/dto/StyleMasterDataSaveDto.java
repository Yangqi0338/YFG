package com.base.sbc.module.style.dto;

import com.base.sbc.module.formType.entity.FieldVal;
import com.base.sbc.module.sample.dto.SampleAttachmentDto;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleMasterData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：款式主数据保存修改
 * @address com.base.sbc.module.style.entity.StyleMasterData
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-9-7 13:57:43
 * @version 1.0
 */
@Data
@ApiModel("款式主数据保存修改 StyleMasterDataSaveDto")
public class StyleMasterDataSaveDto extends StyleMasterData {


    @ApiModelProperty(value = "工艺信息")
    private List<FieldVal> technologyInfo;

    @ApiModelProperty(value = "款式图片信息")
    private List<SampleAttachmentDto> stylePicList;

    @ApiModelProperty(value = "附件信息")
    private List<SampleAttachmentDto> attachmentList;

    @ApiModelProperty(value = "关联的素材库")
    private List<MaterialVo> materialList;


}
