package com.base.sbc.module.common.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * {@code 描述：通用MapStruct spring 转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Mapper(uses = {BaseConvert.class}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BaseSpringConvert {
}
