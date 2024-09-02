package com.base.sbc.module.common.convert;

import com.base.sbc.module.pack.dto.PackSampleReviewDto;
import com.base.sbc.module.pack.entity.PackSampleReview;
import com.base.sbc.module.pack.vo.PackSampleReviewVo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * {@code 描述：吊牌模块通用MapStruct转化}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/1/9
 */
@Mapper(uses = {BaseConvert.class}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PackConvert {
    PackConvert INSTANCE = Mappers.getMapper(PackConvert.class);

    PackSampleReview copy2Entity(PackSampleReviewDto source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy2Entity(PackSampleReviewDto source, @MappingTarget PackSampleReview review);

    PackSampleReviewVo copy2Vo(PackSampleReview source);

}
