package com.base.sbc.module.common.convert;

import org.mapstruct.Mapper;

/**
 * {@code 描述：通用MapStruct计算转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */

public interface ConvertContext {

    HangTagConvert HANG_TAG_CV = HangTagConvert.INSTANCE;
    BaseConvert BASE_CV = BaseConvert.INSTANCE;
    MoreLanguageConvert MORE_LANGUAGE_CV = MoreLanguageConvert.INSTANCE;
    OpenConvert OPEN_CV = OpenConvert.INSTANCE;
    OrderBookConvert ORDER_BOOK_CV = OrderBookConvert.INSTANCE;

}
