package com.base.sbc.module.sample.vo;

import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.sample.entity.FabricBasicInformation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/*返回面料信息*/
@Data
public class FabricInformationVo extends FabricBasicInformation {

//    private String id;

    @ApiModelProperty(value = "图片地址"  )
    private List<String> imageUrlList;

    @ApiModelProperty(value = "图片地址"  )
    private List<AttachmentVo>  attachmentVoList;

    private String imageUrl1;

    private String imageUrl2;

    private String imageUrl3;

    private String imageUrl4;

    private String imageUrl5;

}
