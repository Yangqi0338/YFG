package com.base.sbc.module.planningproject.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningQueryDto;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.mapper.SeasonalPlanningMapper;
import com.base.sbc.module.planningproject.service.SeasonalPlanningService;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningVo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author 卞康
 * @date 2024-01-18 17:08:00
 * @mail 247967116@qq.com
 */
@Service
public class SeasonalPlanningServiceImpl extends BaseServiceImpl<SeasonalPlanningMapper,SeasonalPlanning> implements SeasonalPlanningService {
    @Override
    public void importExcel(MultipartFile file, SeasonalPlanningSaveDto seasonalPlanningSaveDto) throws IOException {
        List<HashMap<Integer, String>> hashMaps = EasyExcel.read(file.getInputStream()).headRowNumber(0).doReadAllSync();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < hashMaps.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            Set<Integer> keySet = hashMaps.get(i).keySet();
            for (Integer s : keySet) {
                jsonObject.put(String.valueOf(s), hashMaps.get(i).get(s) ==null ? "" : hashMaps.get(i).get(s));
            }
            TreeSet<Integer> integerTreeSet = new TreeSet<>();
            TreeSet<Integer>  orderTime = new TreeSet<>();
            TreeSet<Integer> listingTime = new TreeSet<>();
            TreeSet<Integer> styleCategories = new TreeSet<>();
            TreeSet<Integer>  middleClass = new TreeSet<>();
            switch (i) {
                case 0:
                    // 标题
                    String name = jsonObject.getString("0");
                    seasonalPlanningSaveDto.setName(name);
                    break;
                case 1:
                    // 上市波段
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        // if (i1 > 2 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                        //     System.out.println("上市波段:" + jsonObject.getString(s));
                        // }
                        integerTreeSet.add(i1);
                    }
                    break;
                case 2:
                    // 下单时间
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        // if (i1 > 2 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                        //     System.out.println("下单时间:" + jsonObject.getString(s));
                        // }
                        orderTime.add(i1);
                    }
                    break;
                case 3:
                    // 上市时间
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        // if (i1 > 2 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                        //     System.out.println("上市时间:" + jsonObject.getString(s));
                        // }
                        listingTime.add(i1);
                    }
                    break;
                case 4:
                    // 款式类别
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        // if (i1 > 2) {
                        //     System.out.println("样式类别:" + jsonObject.getString(s));
                        // }
                        styleCategories.add(i1);
                    }
                    break;
                default:
                    // 品类
                    for (String s : jsonObject.keySet()) {
                        int i1 = Integer.parseInt(s);
                        // if (i1 == 0 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                        //     // System.out.println("大类:" + jsonObject.getString(s));
                        // }
                        // if (i1 == 1 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                        //     System.out.println("品类:" + jsonObject.getString(s));
                        // }
                        // if (i1 == 2) {
                        //     System.out.println("中类:" + jsonObject.getString(s));
                        // }
                        // if (i1 > 2 && StringUtils.isNotBlank(jsonObject.getString(s))) {
                        //     System.out.println("数量:" + jsonObject.getString(s));
                        // }
                        middleClass.add(i1);
                    }
            }

            if (!integerTreeSet.isEmpty()){
                jsonObject.put(String.valueOf(integerTreeSet.last()+1), "");
                jsonObject.put(String.valueOf(integerTreeSet.last()+2), "合计");
            }
            if (!orderTime.isEmpty()){
                jsonObject.put(String.valueOf(orderTime.last()+1), "");
                jsonObject.put(String.valueOf(orderTime.last()+2), "-");
            }
            if (!listingTime.isEmpty()){
                jsonObject.put(String.valueOf(listingTime.last()+1), "");
                jsonObject.put(String.valueOf(listingTime.last()+2), "-");
            }
            if (!styleCategories.isEmpty()){
                jsonObject.put(String.valueOf(styleCategories.last()+1), "常规");
                jsonObject.put(String.valueOf(styleCategories.last()+2), "高奢");
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
        String dataJson = JSON.toJSONString(jsonArray,SerializerFeature.WriteMapNullValue);

        seasonalPlanningSaveDto.setDataJson(dataJson);

        //先去查有没有启用的，如果有就将状态设为停用
        QueryWrapper<SeasonalPlanning> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("company_code",seasonalPlanningSaveDto.getCompanyCode());
        queryWrapper.eq("status","0");
        queryWrapper.eq("season_id",seasonalPlanningSaveDto.getSeasonId());
        queryWrapper.eq("channel_code",seasonalPlanningSaveDto.getChannelCode());
        long l = this.count(queryWrapper);
        seasonalPlanningSaveDto.setStatus(l > 0 ? "1" : "0");
        this.save(seasonalPlanningSaveDto);
    }

    @Override
    public List<SeasonalPlanningVo> queryList(SeasonalPlanningQueryDto seasonalPlanningQueryDto) {
        BaseQueryWrapper<SeasonalPlanning> queryWrapper = this.buildQueryWrapper(seasonalPlanningQueryDto);
        return baseMapper.listByQueryWrapper(queryWrapper);
    }

    @Override
    public List<SeasonalPlanningVo> queryPage(SeasonalPlanningQueryDto seasonalPlanningQueryDto) {
        PageHelper.startPage(seasonalPlanningQueryDto);
        return this.queryList(seasonalPlanningQueryDto);
    }


    private BaseQueryWrapper<SeasonalPlanning> buildQueryWrapper(SeasonalPlanningQueryDto seasonalPlanningQueryDto) {
        BaseQueryWrapper<SeasonalPlanning> baseQueryWrapper = new BaseQueryWrapper<>();
        baseQueryWrapper.notEmptyEq("tsp.season_id",seasonalPlanningQueryDto.getSeasonId());
        baseQueryWrapper.notEmptyEq("tsp.channel_code",seasonalPlanningQueryDto.getChannelCode());
        baseQueryWrapper.orderByDesc("tsp.id");
        return baseQueryWrapper;
    }
}
