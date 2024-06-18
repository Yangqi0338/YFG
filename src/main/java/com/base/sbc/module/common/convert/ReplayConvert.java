package com.base.sbc.module.common.convert;

import com.base.sbc.module.replay.dto.ReplayConfigDTO;
import com.base.sbc.module.replay.dto.ReplayRatingSaveDTO;
import com.base.sbc.module.replay.entity.ReplayConfig;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.base.sbc.module.replay.entity.ReplayRatingDetail;
import com.github.pagehelper.PageInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * {@code 描述：吊牌模块通用MapStruct转化}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/1/9
 */
@Mapper(uses = {BaseConvert.class}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReplayConvert {

    ReplayConvert INSTANCE = Mappers.getMapper(ReplayConvert.class);

    List<ReplayConfigDTO> copyList2DTO(List<ReplayConfig> source);

    ReplayConfigDTO copy2DTO(ReplayConfig source);

    ReplayConfig copy2Entity(ReplayConfigDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayConfig target, ReplayConfigDTO source);

    PageInfo<ReplayConfigDTO> copy2DTO(PageInfo<ReplayConfig> source);

    ReplayRating copy2Entity(ReplayRatingSaveDTO replayRatingSaveDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingDetail target, ReplayRatingDetail source);

}
