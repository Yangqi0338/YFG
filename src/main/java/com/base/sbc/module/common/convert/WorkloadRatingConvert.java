package com.base.sbc.module.common.convert;

import com.base.sbc.module.common.vo.SelectOptionsChildrenVo;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
import com.base.sbc.module.workload.dto.WorkloadRatingItemDTO;
import com.base.sbc.module.workload.entity.WorkloadRatingDetail;
import com.base.sbc.module.workload.entity.WorkloadRatingItem;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigQO;
import com.base.sbc.module.workload.vo.WorkloadRatingItemQO;
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
public interface WorkloadRatingConvert {

    WorkloadRatingConvert INSTANCE = Mappers.getMapper(WorkloadRatingConvert.class);

    @Mappings({
            @Mapping(target = "itemName", source = "configName"),
            @Mapping(target = "id", source = "configId"),
    })
    WorkloadRatingConfigQO copy2ConfigQO(WorkloadRatingItemQO source);

    List<SelectOptionsVo> copy2OptionsVo(List<SelectOptionsChildrenVo> source);

    List<WorkloadRatingDetailDTO> copy2DTO(List<WorkloadRatingDetail> source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget WorkloadRatingItem target, WorkloadRatingItemDTO source);

    List<WorkloadRatingDetailDTO> copy2DetailDTO(List<WorkloadRatingDetail> source);
    WorkloadRatingDetail copy2Entity(WorkloadRatingDetailDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(@MappingTarget WorkloadRatingDetail target, WorkloadRatingDetailDTO source);

}
