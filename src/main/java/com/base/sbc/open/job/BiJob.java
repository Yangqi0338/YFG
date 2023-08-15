package com.base.sbc.open.job;

import com.base.sbc.open.service.BiSizeChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 卞康
 * @date 2023/8/15 15:24:07
 * @mail 247967116@qq.com
 */
@Component
@RequiredArgsConstructor
public class BiJob {
    private final BiSizeChartService biSizeChartService;


    /**
     * 尺寸表数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0 * * * * ?")
    public void sizeChart(){
        biSizeChartService.sizeChart();
    }
}
