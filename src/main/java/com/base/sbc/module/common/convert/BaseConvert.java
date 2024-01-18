package com.base.sbc.module.common.convert;

import cn.hutool.core.bean.BeanUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * {@code 描述：通用MapStruct计算转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Mapper
public interface BaseConvert {

    BaseConvert INSTANCE = Mappers.getMapper(BaseConvert.class);

    List<Map<String, Object>> toListMap(List<?> object);

    default Map<String, Object> toMap(Object object) {
        return BeanUtil.beanToMap(object );
    }
}
