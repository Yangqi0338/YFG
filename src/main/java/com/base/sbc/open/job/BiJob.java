//package com.base.sbc.open.job;
//
//import com.base.sbc.open.service.BiColorwayService;
//import com.base.sbc.open.service.BiSampleService;
//import com.base.sbc.open.service.BiSizeChartService;
//import com.base.sbc.open.service.BiStyleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * @author 卞康
// * @date 2023/8/15 15:24:07
// * @mail 247967116@qq.com
// */
//@Component
//@RequiredArgsConstructor
//public class BiJob {
//    private final BiSizeChartService biSizeChartService;
//    private final BiColorwayService biColorwayService;
//    private final BiSampleService biSampleService;
//    private final BiStyleService biStyleService;
//
//    /**
//     * 尺寸表数据
//     */
//    //@Scheduled(cron = "0 0 1 * * ?")
//    //@Scheduled(cron = "0 * * * * ?")
//    public void sizeChart(){
//        biSizeChartService.sizeChart();
//    }
//
//
//
//    /**
//     * 配色表数据
//     */
//    //@Scheduled(cron = "0 0 1 * * ?")
//    //@Scheduled(cron = "0 * * * * ?")
//    public void colorway(){
//        biColorwayService.colorway();
//    }
//
//
//    /**
//     * 样衣看板.产前样
//     */
//    //@Scheduled(cron = "0 0 1 * * ?")
//    //@Scheduled(cron = "0 * * * * ?")
//    public void style(){
//        biStyleService.style();
//    }
//
//
//    /**
//     * 款式设计
//     */
//    //@Scheduled(cron = "0 0 1 * * ?")
//    //@Scheduled(cron = "0 * * * * ?")
//    public void sample(){
//        biSampleService.sample();
//
//    }
//
//    /**
//     * 投产数据
//     */
//    //@Scheduled(cron = "0 0 1 * * ?")
//    public void putInProduction(){
//        biSampleService.sample();
//    }
//
//}
