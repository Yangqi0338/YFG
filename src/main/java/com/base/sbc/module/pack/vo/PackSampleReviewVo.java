package com.base.sbc.module.pack.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.SpringContextHolder;
import com.base.sbc.module.pack.entity.PackSampleReview;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 类描述：资料包-样衣评审 Vo
 *
 * @author lxl
 * @version 1.0
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 11:09:05
 */
@Data
@ApiModel("资料包-样衣评审 PackSampleReviewVo")
public class PackSampleReviewVo extends PackSampleReview {

    public List<String> getFabricTestFileUrlList() {
        String fabricTestFileUrl = this.getSourceFabricTestFileUrl();
        if (StrUtil.isBlank(fabricTestFileUrl)) return new ArrayList<>();
        MinioUtils minioUtils = SpringContextHolder.getBean("minioUtils");
        return StrUtil.split(minioUtils.getObjectUrl(fabricTestFileUrl), COMMA);
    }

}
