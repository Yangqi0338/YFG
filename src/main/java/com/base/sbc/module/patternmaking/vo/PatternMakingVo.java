package com.base.sbc.module.patternmaking.vo;

import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * 类描述：打版管理Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-12 14:47
 */
@Data
@ApiModel("打版管理Vo PatternMakingVo")
public class PatternMakingVo extends PatternMaking {

    @ApiModelProperty(value = "裁剪工头像")
    @UserAvatar("cutterId")
    private String cutterAvatar;
    @ApiModelProperty(value = "车缝工头像")
    @UserAvatar("stitcherId")
    private String stitcherAvatar;

    @ApiModelProperty(value = "版师头像")
    @UserAvatar("patternDesignId")
    private String patternDesignAvatar;

    @ApiModelProperty(value = "纸样文件")
    private List<AttachmentVo> attachmentList;

    @ApiModelProperty(value = "节点信息list")
    private List<NodeStatusVo> nodeStatusList;
    @ApiModelProperty(value = "节点信息Map")
    private Map<String, NodeStatusVo> nodeStatus;

    /** 打版图片文件url */
    @ApiModelProperty(value = "打版图片文件url"  )
    private String samplePicUrl;

    /** 打版视频文件url */
    @ApiModelProperty(value = "打版视频文件url"  )
    private String sampleVideoUrl;

    public String getSamplePicUrl() {
        if (StringUtils.isEmpty(samplePicUrl)){
            this.samplePicUrl = getSamplePic();
        }
        return samplePicUrl;
    }

    public String getSampleVideoUrl() {
        if (StringUtils.isEmpty(sampleVideoUrl)){
            this.sampleVideoUrl =  getSampleVideoFileId();
        }
        return sampleVideoUrl;
    }
}
