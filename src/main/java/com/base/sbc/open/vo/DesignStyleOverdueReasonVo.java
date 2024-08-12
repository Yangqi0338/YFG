package com.base.sbc.open.vo;

import lombok.Data;

@Data
public class DesignStyleOverdueReasonVo {
    /**大货款号*/
    private String styleNo;

    /**
     * 设计下面料详单逾期原因
     */
    private String sendMainFabricOverdueReason;

    /**
     * 设计下明细单逾期原因
     */
    private String designDetailOverdueReason;

    /**
     * 设计下正确样逾期原因
     */
    private String designCorrectOverdueReason;
}
