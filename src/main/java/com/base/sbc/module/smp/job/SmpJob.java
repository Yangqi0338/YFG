package com.base.sbc.module.smp.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.constant.SmpProperties;
import com.base.sbc.config.enums.business.PushRespStatus;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailOrderStatusEnum;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.pushrecords.entity.PushRecords;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/8/15 15:24:07
 * @mail 247967116@qq.com
 */
@Component
@RequiredArgsConstructor
public class SmpJob {
    private final PushRecordsService pushRecordsService;
    private final OrderBookDetailService orderBookDetailService;


    /**
     * 每十五秒检测数据
     */
    @Scheduled(cron = "1/15 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void putInProduction(){
        LambdaQueryWrapper<OrderBookDetail> qw = new LambdaQueryWrapper<OrderBookDetail>()
                .eq(OrderBookDetail::getOrderSendStatus, PushRespStatus.PROCESS)
                .eq(OrderBookDetail::getOrderStatus, OrderBookDetailOrderStatusEnum.PRODUCTION_IN);
        boolean exists = orderBookDetailService.exists(qw.last(" and 1 = 1"));
        if (exists) {
            List<OrderBookDetail> list = orderBookDetailService.list(qw.select(OrderBookDetail::getId));
            orderBookDetailService.handlePlaceAnProduction(list, new ArrayList<>());
        }
    }

    /**
     * 每十五秒检测数据
     */
    @Scheduled(cron = "1/5 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void cancelProduction(){
        LambdaQueryWrapper<OrderBookDetail> qw = new BaseLambdaQueryWrapper<OrderBookDetail>()
                .notNull(OrderBookDetail::getOrderNo)
                .eq(OrderBookDetail::getOrderStatus, OrderBookDetailOrderStatusEnum.ORDER)
                ;
        boolean exists = orderBookDetailService.exists(qw.last(" and 1 = 1"));
        if (exists) {
            List<OrderBookDetail> list = orderBookDetailService.list(qw.select(OrderBookDetail::getOrderNo, OrderBookDetail::getId));
            orderBookDetailService.handlePlaceAnCancelProduction(list, new ArrayList<>());
        }
    }


    /**
     * TODO 要根据配置进行开不同的配置顺序
     * 每三分钟重新推下游推送
     */
    @Scheduled(cron = "0 3 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void rePush() {
        List<PushRecords> pushRecordsList = pushRecordsService.existHandlePushRecord();
        List<PushRecords> updatePushRecordsList = new ArrayList<>();
        List<String> rePushRecordsList = new ArrayList<>();
        for (PushRecords pushRecords : pushRecordsList) {
            String url = pushRecords.getPushAddress();
            Integer pushCount = pushRecords.getPushCount();
            for (SmpProperties.SystemModuleEnum systemEnum : SmpProperties.SystemModuleEnum.values()) {
                if (url.startsWith(systemEnum.getUrl())) {
                    if (systemEnum.getRetryNum() <= pushCount) {
                        pushRecords.setPushStatus(PushRespStatus.FAILURE);
                        pushRecords.setResponseMessage("处理超时,请重试");
                        pushRecords.setResponseStatusCode("501");
                        updatePushRecordsList.add(pushRecords);
                    }else {
                        rePushRecordsList.add(pushRecords.getId());
                    }
                    break;
                }
            }
        }
        if (CollUtil.isNotEmpty(updatePushRecordsList)) {
            pushRecordsService.saveOrUpdateBatch(updatePushRecordsList);
        }
        if (CollUtil.isNotEmpty(rePushRecordsList)) {
            rePushRecordsList.forEach(pushRecordsService::rePush);
        }
    }
}
