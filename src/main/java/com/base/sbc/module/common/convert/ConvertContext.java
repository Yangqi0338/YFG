package com.base.sbc.module.common.convert;

import org.mapstruct.Mapper;

/**
 * {@code 描述：通用MapStruct计算转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */

public interface ConvertContext {

    HangTagConvert HANG_TAG_CONVERT = HangTagConvert.INSTANCE;
    BaseConvert BASE_CONVERT = BaseConvert.INSTANCE;
    MoreLanguageConvert MORE_LANGUAGE_CONVERT = MoreLanguageConvert.INSTANCE;

}
