package com.base.sbc.module.planningproject.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author 卞康
 * @date 2024-01-18 17:08:27
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "季节企划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/seasonalPlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class SeasonalPlanningController extends BaseController {

    /**
     * 导入季节企划
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<HashMap<Integer, String>> hashMaps = EasyExcel.read(file.getInputStream()).headRowNumber(0).doReadAllSync();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < hashMaps.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            Set<Integer> keySet = hashMaps.get(i).keySet();
            for (Integer s : keySet) {
                jsonObject.put(String.valueOf(s), hashMaps.get(i).get(s));
            }
            TreeSet<Integer> integerTreeSet = new TreeSet<>();
            TreeSet<Integer>  orderTime = new TreeSet<>();
            TreeSet<Integer> listingTime = new TreeSet<>();
            TreeSet<Integer> styleCategories = new TreeSet<>();
            TreeSet<Integer>  middleClass = new TreeSet<>();
            switch (i) {
                case 0:
                    // 标题
                    String string = jsonObject.getString("0");
                    System.out.println("标题:" + string);
                    break;
                case 1:
                    // 上市波段
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        if (i1 > 2 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                            System.out.println("上市波段:" + jsonObject.getString(s));
                        }
                        integerTreeSet.add(i1);
                    }
                    break;
                case 2:
                    // 下单时间
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        if (i1 > 2 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                            System.out.println("下单时间:" + jsonObject.getString(s));
                        }
                        orderTime.add(i1);
                    }
                    break;
                case 3:
                    // 上市时间
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        if (i1 > 2 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                            System.out.println("上市时间:" + jsonObject.getString(s));
                        }
                        listingTime.add(i1);
                    }
                    break;
                case 4:
                    // 款式类别
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        if (i1 > 2) {
                            System.out.println("样式类别:" + jsonObject.getString(s));
                        }
                        styleCategories.add(i1);
                    }
                    break;
                default:
                    // 品类
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        if (i1 == 0 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                            System.out.println("大类:" + jsonObject.getString(s));
                        }
                        if (i1 == 1 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                            System.out.println("品类:" + jsonObject.getString(s));
                        }
                        if (i1 == 2) {
                            System.out.println("中类:" + jsonObject.getString(s));
                        }
                        if (i1 > 2 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                            System.out.println("数量:" + jsonObject.getString(s));
                        }
                        middleClass.add(i1);
                    }
            }

            if (!integerTreeSet.isEmpty()){
                System.out.println("last:" + integerTreeSet.last());
                jsonObject.put(String.valueOf(integerTreeSet.last()+2), "合计");
                System.out.println(jsonObject);
            }
            if (!orderTime.isEmpty()){
                System.out.println("last:" + orderTime.last());
                jsonObject.put(String.valueOf(orderTime.last()+2), "-");
                System.out.println(jsonObject);
            }
            if (!listingTime.isEmpty()){
                System.out.println("last:" + listingTime.last());
                jsonObject.put(String.valueOf(listingTime.last()+2), "-");
                System.out.println(jsonObject);
            }
            if (!styleCategories.isEmpty()){
                System.out.println("last:" + styleCategories.last());
                jsonObject.put(String.valueOf(styleCategories.last()+1), "常规");
                jsonObject.put(String.valueOf(styleCategories.last()+2), "高奢");
                System.out.println(jsonObject);
            }
            if (!middleClass.isEmpty()){
                int sum = 0;
                int sum1 = 0;
                for (String s : jsonObject.keySet()) {
                    int i1 = Integer.parseInt(s);
                    if (i1 > 2) {
                        if (i1 % 2 == 0){
                            sum1 += Integer.parseInt(jsonObject.getString(s));
                        }else {
                            sum += Integer.parseInt(jsonObject.getString(s));
                        }
                    }
                }

                jsonObject.put(String.valueOf(middleClass.last()+1), sum);
                jsonObject.put(String.valueOf(middleClass.last()+2), sum1);
            }
            jsonArray.add(jsonObject);
        }

        //竖排合计
        JSONObject jsonObject2 =new JSONObject();
        jsonObject2.put("2","合计");
        for (int i = 0; i < jsonArray.size(); i++) {

            if (i>4){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                for (String s : jsonObject.keySet()) {
                    int i1 = Integer.parseInt(s);
                    if (i1>2){
                        int sum;
                        if (jsonObject2.getInteger(s)!= null) {
                            sum=jsonObject.getInteger(s) +jsonObject2.getInteger(s);
                        }else {
                            sum = jsonObject.getInteger(s);
                        }
                        jsonObject2.put(s, sum);
                    }
                }

            }
        }
        jsonArray.add(jsonObject2);
        System.out.println(jsonArray);
        String string = jsonArray.toString();
        return null;
    }
}
