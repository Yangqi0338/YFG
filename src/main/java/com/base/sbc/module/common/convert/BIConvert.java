package com.base.sbc.module.common.convert;

import com.base.sbc.module.smp.dto.GoodsSluggishSalesDTO;
import com.base.sbc.module.smp.entity.GoodsSluggishSales;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
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
public interface BIConvert {

    BIConvert INSTANCE = Mappers.getMapper(BIConvert.class);

    List<GoodsSluggishSalesDTO> copyList2DTO(List<GoodsSluggishSales> source);

    GoodsSluggishSalesDTO copy2DTO(GoodsSluggishSales source);


}
