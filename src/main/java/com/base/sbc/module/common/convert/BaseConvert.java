package com.base.sbc.module.common.convert;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.base.BaseDataExtendEntity;
import com.github.pagehelper.PageInfo;
import org.mapstruct.BeforeMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * {@code 描述：通用MapStruct计算转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BaseConvert {

    BaseConvert INSTANCE = Mappers.getMapper(BaseConvert.class);

    @IterableMapping(qualifiedByName = "obj2Map")
    List<Map<String, Object>> toListMap(List<?> source);

    Map<String, Object> toMap(Map<String, Object> source);

    @Named("obj2Map")
    default Map<String, Object> obj2Map(Object source) {
        return BeanUtil.beanToMap(source);
    }

    PageInfo copy(PageInfo<?> source);
    default <T> PageInfo<T> copy(PageInfo<?> source, List<T> list) {
        source.setList(null);
        PageInfo<T> pageInfo = copy(source);
        pageInfo.setList(list);
        return pageInfo;
    };

    BaseDataExtendEntity copy(BaseDataExtendEntity source);

    @BeforeMapping
    default void beforeBaseDataExtendEntity(BaseDataExtendEntity source) {
        source.build();
    }
}
