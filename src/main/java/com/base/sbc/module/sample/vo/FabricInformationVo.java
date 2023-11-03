package com.base.sbc.module.sample.vo;

import com.base.sbc.module.sample.entity.FabricBasicInformation;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/*返回面料信息*/
@Data
public class FabricInformationVo extends FabricBasicInformation {

//    private String id;

    @ApiModelProperty(value = "图片地址"  )
    private List<String> imageUrlList;

}
