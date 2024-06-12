package com.base.sbc.module.common.convert;

import com.base.sbc.module.replay.dto.ReplayConfigDTO;
import com.base.sbc.module.replay.entity.ReplayConfig;
import com.github.pagehelper.PageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
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

    void copy(@MappingTarget ReplayConfig target, ReplayConfigDTO source);

    PageInfo<ReplayConfigDTO> copy2DTO(PageInfo<ReplayConfig> source);

}
