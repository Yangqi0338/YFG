package com.base.sbc.module.smp.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 吊牌确认日期
 */
@Data
public class SaleProductIntoDto extends Page implements Serializable {

    private static final long serialVersionUID = -927874174170828211L;

    /**
     * 大货款号 like
     */
    private String bulkStyleNo;

    /**
     * 大货款号 in
     */
    private List<String> bulkStyleNoList;

    /**
     * 渠道
     */
    private List<String> channelList = Arrays.stream(OrderBookChannelType.values()).map(OrderBookChannelType::getText).collect(Collectors.toList());
}
