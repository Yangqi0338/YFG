//package com.base.sbc.open.controller;
//
//import com.base.sbc.config.common.ApiResult;
//import com.base.sbc.config.common.base.BaseController;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author 卞康
// * @date 2023/7/24 10:06:19
// * @mail 247967116@qq.com
// */
//@RestController
//@RequiredArgsConstructor
//@RequestMapping(value = BaseController.OPEN_URL + "/bi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//public class BiController extends BaseController{
//    //private final BiService biService;
//    //@GetMapping("/sizeChart")2
//    //public ApiResult sizeChart(){
//    //    //List<BiSizeChart> biSizeCharts = biService.sizeChart();
//    //    return selectSuccess(biSizeCharts);
//    //}
//
//    /**
//     * 款式主数据
//     */
//    @GetMapping("/styleChart")
//    public ApiResult styleChart(){
//
//        return selectSuccess(null);
//    }
//
//    /**
//     * 样衣信息
//     */
//    @GetMapping("/sampleChart")
//    public ApiResult sampleChart(){
//
//        return selectSuccess(null);
//    }
//
//    /**
//     * 订货本信息
//     */
//    @GetMapping("/orderBookChart")
//    public ApiResult orderBookChart(){
//
//        return selectSuccess(null);
//    }
//}
