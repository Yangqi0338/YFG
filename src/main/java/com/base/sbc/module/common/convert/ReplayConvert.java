package com.base.sbc.module.common.convert;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.replay.dto.ReplayConfigDTO;
import com.base.sbc.module.replay.dto.ReplayConfigDetailDTO;
import com.base.sbc.module.replay.dto.ReplayRatingDetailDTO;
import com.base.sbc.module.replay.dto.ReplayRatingFabricDTO;
import com.base.sbc.module.replay.dto.ReplayRatingPatternDTO;
import com.base.sbc.module.replay.dto.ReplayRatingSaveDTO;
import com.base.sbc.module.replay.dto.ReplayRatingStyleDTO;
import com.base.sbc.module.replay.entity.ReplayConfig;
import com.base.sbc.module.replay.entity.ReplayRating;
import com.base.sbc.module.replay.entity.ReplayRatingDetail;
import com.base.sbc.module.replay.vo.ReplayRatingVO;
import com.base.sbc.module.replay.vo.ReplayRatingYearVO;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.github.pagehelper.PageInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
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
@Mapper(uses = {BaseConvert.class}, imports = {BaseConvert.class}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReplayConvert {

    ReplayConvert INSTANCE = Mappers.getMapper(ReplayConvert.class);

    List<ReplayConfigDTO> copyList2DTO(List<ReplayConfig> source);

    ReplayConfigDTO copy2DTO(ReplayConfig source);

    ReplayConfig copy2Entity(ReplayConfigDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayConfig target, ReplayConfigDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReplayConfigDetailDTO copy(ReplayConfigDetailDTO source);

    PageInfo<ReplayConfigDTO> copy2DTO(PageInfo<ReplayConfig> source);

    ReplayRating copy2Entity(ReplayRatingSaveDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy2Entity(@MappingTarget ReplayRating replayRating, ReplayRatingSaveDTO source);

    ReplayRatingDetailDTO copy2DTO(ReplayRatingDetail source);

    List<ReplayRatingDetailDTO> copy2DetailDTOList(List<ReplayRatingDetail> source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingDetail target, ReplayRatingDetail source);

    @Mappings({
            @Mapping(target = "styleId", source = "id"),
            @Mapping(target = "id", ignore = true),
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingStyleDTO target, Style style);

    @Mappings({
            @Mapping(target = "styleColorId", source = "id"),
            @Mapping(target = "bulkStyleNo", source = "styleNo"),
            @Mapping(target = "id", ignore = true),
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingStyleDTO target, StyleColor styleColor);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingStyleDTO target, ReplayRating replayRating);

    @Mappings({
            @Mapping(target = "patternLibraryId", source = "id"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "patternLibraryItemPattern", ignore = true),
            @Mapping(target = "patternLibraryItemLength", ignore = true),
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingPatternDTO target, PatternLibrary patternLibrary);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingPatternDTO target, ReplayRating replayRating);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingYearVO target, ReplayRatingVO source);

    @Mappings({
            @Mapping(target = "materialId", source = "id"),
            @Mapping(target = "translateCode", source = "widthGroup"),
            @Mapping(target = "translate", source = "widthGroupName"),
            @Mapping(target = "unitCode", source = "stockUnitCode"),
            @Mapping(target = "id", ignore = true),
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingFabricDTO target, BasicsdatumMaterial source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget ReplayRatingFabricDTO target, ReplayRating source);

}
