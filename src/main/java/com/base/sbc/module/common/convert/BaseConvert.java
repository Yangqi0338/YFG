package com.base.sbc.module.common.convert;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.SmpProperties;
import com.base.sbc.config.constant.UrlConfig;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
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

    List<Map<String, Object>> toListMap(List<?> source);

    default Map<String, Object> toMap(Object source) {
        return BeanUtil.beanToMap(source);
    }

    PageInfo copy(PageInfo<?> source);
    default <T> PageInfo<T> copy(PageInfo<?> source, List<T> list) {
        source.setList(null);
        PageInfo<T> pageInfo = copy(source);
        pageInfo.setList(list);
        return pageInfo;
    };
}
