package com.base.sbc.module.common.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@code 描述：通用MapStruct计算转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Component
public class ConvertContext {

    public static HangTagConvert HANG_TAG_CV = HangTagConvert.INSTANCE;
    public static BaseConvert BASE_CV = BaseConvert.INSTANCE;
    public static MoreLanguageConvert MORE_LANGUAGE_CV = MoreLanguageConvert.INSTANCE;
    public static OpenConvert OPEN_CV = OpenConvert.INSTANCE;
    public static OrderBookConvert ORDER_BOOK_CV = OrderBookConvert.INSTANCE;
    public static ReplayConvert REPLAY_CV = ReplayConvert.INSTANCE;
    public static BIConvert BI_CV = BIConvert.INSTANCE;
    public static WorkloadRatingConvert WORKLOAD_CV = WorkloadRatingConvert.INSTANCE;
    public static PackConvert PACK_CV = PackConvert.INSTANCE;

    @Autowired(required = false)
    public void setHangTagCv(HangTagConvert hangTagCv) {
        HANG_TAG_CV = hangTagCv;
    }

    @Autowired(required = false)
    public void setBaseCv(BaseConvert baseCv) {
        BASE_CV = baseCv;
    }

    @Autowired(required = false)
    public void setMoreLanguageCv(MoreLanguageConvert moreLanguageCv) {
        MORE_LANGUAGE_CV = moreLanguageCv;
    }

    @Autowired(required = false)
    public void setOpenCv(OpenConvert openCv) {
        OPEN_CV = openCv;
    }

    @Autowired(required = false)
    public void setOrderBookCv(OrderBookConvert orderBookCv) {
        ORDER_BOOK_CV = orderBookCv;
    }

    @Autowired(required = false)
    public void setReplayCv(ReplayConvert replayCv) {
        REPLAY_CV = replayCv;
    }

    @Autowired(required = false)
    public void setBiCv(BIConvert biCv) {
        BI_CV = biCv;
    }

    @Autowired(required = false)
    public void setWorkloadCv(WorkloadRatingConvert workloadCv) {
        WORKLOAD_CV = workloadCv;
    }

    @Autowired(required = false)
    public void setPackCv(PackConvert packCv) {
        PACK_CV = packCv;
    }
}
