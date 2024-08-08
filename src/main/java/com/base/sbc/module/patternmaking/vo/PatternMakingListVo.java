package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

/**
 * 类描述： 打版管理 vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-29 13:44
 */
@Data
@ApiModel("打版管理vo PatternMakingVo ")
public class PatternMakingListVo extends PatternMaking {
    @ApiModelProperty(value = "尺码")
    private String productSizes;
    @ApiModelProperty(value = "默认尺码")
    private String defaultSize;

    private String isUpdate;
    /**
     * 打版难度
     */
    @ApiModelProperty(value = "打版指令打版难度")
    private String patDiff;
    @ApiModelProperty(value = "打版指令打版难度")
    private String patDiffName;
    /**
     * 打版难度
     */
    @ApiModelProperty(value = "样衣打版难度")
    private String sdPatDiff;
    @ApiModelProperty(value = "样衣打版难度")
    private String sdPatDiffName;

    /** 打版图片文件url */
    @ApiModelProperty(value = "打版图片文件url"  )
    private String samplePicUrl;

    private String samplePicUrl1;

    private String samplePicUrl2;

    private String samplePicUrl3;

    private String samplePicUrl4;

    /** 打版视频文件url */
    @ApiModelProperty(value = "打版视频文件url"  )
    private String sampleVideoUrl;

    public String getSamplePicUrl() {
        if (StringUtils.isEmpty(samplePicUrl)){
            this.samplePicUrl = getSamplePicFileId();
        }
        return samplePicUrl;
    }

    public String getSamplePicUrl1() {
        if (StringUtils.isEmpty(samplePicUrl1)){
            this.samplePicUrl1 = getSamplePicFileId1();
        }
        return samplePicUrl1;
    }

    public String getSamplePicUrl2() {
        if (StringUtils.isEmpty(samplePicUrl2)){
            this.samplePicUrl2 = getSamplePicFileId2();
        }
        return samplePicUrl2;
    }

    public String getSamplePicUrl3() {
        if (StringUtils.isEmpty(samplePicUrl3)){
            this.samplePicUrl3 = getSamplePicFileId3();
        }
        return samplePicUrl3;
    }

    public String getSamplePicUrl4() {
        if (StringUtils.isEmpty(samplePicUrl4)){
            this.samplePicUrl4 = getSamplePicFileId4();
        }
        return samplePicUrl4;
    }

    public String getSampleVideoUrl() {
        if (StringUtils.isEmpty(sampleVideoUrl)){
            this.sampleVideoUrl =  getSampleVideoFileId();
        }
        return sampleVideoUrl;
    }

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    private String supplierAbbreviation;
    @Override
    public String getPatDiff() {
        return Optional.ofNullable(patDiff).orElse(sdPatDiff);
    }

}
