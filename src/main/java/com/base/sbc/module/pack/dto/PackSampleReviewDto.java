package com.base.sbc.module.pack.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.SpringContextHolder;
import com.base.sbc.module.pack.entity.PackSampleReview;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 类描述：资料包-样衣评审 Dto
 *
 * @author lxl
 * @version 1.0
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 11:09:05
 */
@Data
@ApiModel("资料包-样衣评审 PackSampleReviewDto")
public class PackSampleReviewDto extends PackSampleReview {

    @ApiModelProperty(value = "主数据id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型为空")
    private String packType;

    private List<String> fabricTestFileUrlList;

    /* ----------------------------为了字符串和前端显示的url列表进行互相转换---------------------------- */

    public List<String> getFabricTestFileUrlList() {
        if (CollUtil.isNotEmpty(fabricTestFileUrlList)) return fabricTestFileUrlList;
        String fabricTestFileUrl = this.getSourceFabricTestFileUrl();
        if (StrUtil.isBlank(fabricTestFileUrl)) return new ArrayList<>();
        MinioUtils minioUtils = SpringContextHolder.getBean("minioUtils");
        return StrUtil.split(minioUtils.getObjectUrl(fabricTestFileUrl), COMMA);
    }

    @Override
    public String getFabricTestFileUrl() {
        if (StrUtil.isNotBlank(this.getSourceFabricTestFileUrl())) return this.getSourceFabricTestFileUrl();
        if (CollUtil.isEmpty(fabricTestFileUrlList)) return "";
        return String.join(COMMA, fabricTestFileUrlList);
    }
}
