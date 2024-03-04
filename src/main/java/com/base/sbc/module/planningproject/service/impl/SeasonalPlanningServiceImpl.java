package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.orderbook.dto.OrderBookDetailQueryDto;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningQueryDto;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.mapper.SeasonalPlanningMapper;
import com.base.sbc.module.planningproject.service.SeasonalPlanningDetailsService;
import com.base.sbc.module.planningproject.service.SeasonalPlanningService;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningVo;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-18 17:08:00
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class SeasonalPlanningServiceImpl extends BaseServiceImpl<SeasonalPlanningMapper, SeasonalPlanning> implements SeasonalPlanningService {
    private final CcmFeignService ccmFeignService;
    private final SeasonalPlanningDetailsService seasonalPlanningDetailsService;
    private final OrderBookDetailService orderBookDetailService;

    @Override
    @Transactional
    public void importExcel(MultipartFile file, SeasonalPlanningSaveDto seasonalPlanningSaveDto) throws IOException {
        List<HashMap<Integer, String>> hashMaps = EasyExcel.read(file.getInputStream()).headRowNumber(0).doReadAllSync();

        JSONArray jsonArray = new JSONArray();
        List<BasicBaseDict> c8Band = ccmFeignService.getDictInfoToList("C8_Band");

        List<String> bandNames = new ArrayList<>();
        List<String> orders = new ArrayList<>();
        List<String> markets = new ArrayList<>();
        List<String> styleCategorieList = new ArrayList<>();
        List<String> sums = new ArrayList<>();
        List<HashMap<String, String>> hashMapList = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < hashMaps.size(); i++) {

            JSONObject jsonObject = new JSONObject();
            Set<Integer> keySet = hashMaps.get(i).keySet();
            for (Integer s : keySet) {
                jsonObject.put(String.valueOf(s), hashMaps.get(i).get(s) == null ? "" : hashMaps.get(i).get(s));
            }
            TreeSet<Integer> integerTreeSet = new TreeSet<>();
            TreeSet<Integer> orderTime = new TreeSet<>();
            TreeSet<Integer> listingTime = new TreeSet<>();
            TreeSet<Integer> styleCategories = new TreeSet<>();
            TreeSet<Integer> middleClassSet = new TreeSet<>();
            switch (i) {
                case 0:
                    // 标题
                    String name = jsonObject.getString("0");
                    seasonalPlanningSaveDto.setName(name);
                    break;
                case 1:
                    // 上市波段
                    int s =0;
                    //防止顺序错乱,不能用jsonObject.keySet()
                    while (jsonObject.containsKey(String.valueOf(s))){
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            // System.out.println("上市波段:" + jsonObject.getString(s));
                            bandNames.add(jsonObject.getString(String.valueOf(s)));
                            bandNames.add(jsonObject.getString(String.valueOf(s)));
                        }
                        integerTreeSet.add(s);
                        s++;
                    }
                    break;
                case 2:
                     s =0;
                    // 下单时间
                    while (jsonObject.containsKey(String.valueOf(s))){
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            orders.add(jsonObject.getString(String.valueOf(s)));
                            orders.add(jsonObject.getString(String.valueOf(s)));
                        }
                        orderTime.add(s);
                        s++;
                    }
                    break;
                case 3:
                    // 上市时间
                     s =0;
                    while (jsonObject.containsKey(String.valueOf(s))){
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            markets.add(jsonObject.getString(String.valueOf(s)));
                            markets.add(jsonObject.getString(String.valueOf(s)));
                        }
                        listingTime.add(s);
                        s++;
                    }
                    break;
                case 4:
                    // 款式类别
                     s=0;
                    while (jsonObject.containsKey(String.valueOf(s))){
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            styleCategorieList.add(jsonObject.getString(String.valueOf(s)));
                        }
                        styleCategories.add(s);
                        s++;
                    }
                    break;
                default:
                    List<BasicCategoryDot> categoryDotList = new ArrayList<>();
                    // int sum = 0;
                    s=0;

                    while (jsonObject.containsKey(String.valueOf(s))){

                        if (s == 0 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            // System.out.println("大类:" + jsonObject.getString(s));

                            List<BasicCategoryDot> list = ccmFeignService.getCategorySByNameAndLevel("品类", jsonObject.getString(String.valueOf(s)), "0");
                            if (list != null && !list.isEmpty()) {
                                categoryDotList.add(list.get(0));
                            }else {
                                throw new RuntimeException("大类:"+jsonObject.getString(String.valueOf(s))+"不存在");
                            }
                        }
                        if (s == 1 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            // System.out.println("品类:" + jsonObject.getString(s));
                            List<BasicCategoryDot> list = ccmFeignService.getCategorySByNameAndLevel("品类", jsonObject.getString(String.valueOf(s)), "1");
                            if (list != null && !list.isEmpty()) {
                                categoryDotList.add(list.get(0));
                            }else {
                                throw new RuntimeException("品类:"+jsonObject.getString(String.valueOf(s))+"不存在");
                            }

                        }
                        if (s == 2) {
                            // System.out.println("中类:" + jsonObject.getString(s));
                            List<BasicCategoryDot> list =new ArrayList<>();
                            if (StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))){
                                list  = ccmFeignService.getCategorySByNameAndLevel("品类", jsonObject.getString(String.valueOf(s)), "2");

                            }
                            if (list != null && !list.isEmpty()) {
                                categoryDotList.add(list.get(0));
                            }else {
                                if (StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))){
                                    throw new RuntimeException("中类:"+jsonObject.getString(String.valueOf(s))+"不存在");

                                }
                            }
                        }
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            // System.out.println("数量:" + jsonObject.getString(s));
                            // sum += Integer.parseInt(jsonObject.getString(s));
                            String s1 = jsonObject.getString(String.valueOf(s));

                            int i1 = 0;
                            try {
                                i1 = Integer.parseInt(s1);
                            } catch (NumberFormatException e) {
                                throw new RuntimeException("输入的数字不规范");
                            }
                            if (i1<0){
                                throw new RuntimeException("数量不能小于0");
                            }
                            sums.add(s1);
                        }
                        middleClassSet.add(s);
                        s++;
                    }

                    // System.out.println("SKC数量"+sum);


                    for (BasicCategoryDot basicCategoryDot : categoryDotList) {
                        // 每次都清掉中类信息
                        hashMap.put("中类名称", "");
                        hashMap.put("中类编码", "");
                        Integer level = basicCategoryDot.getLevel();
                        if (level == 0) {
                            hashMap.put("大类名称", basicCategoryDot.getName());
                            hashMap.put("大类编码", basicCategoryDot.getValue());
                        }
                        if (level == 1) {
                            hashMap.put("品类名称", basicCategoryDot.getName());
                            hashMap.put("品类编码", basicCategoryDot.getValue());
                        }
                        if (level == 2) {
                            hashMap.put("中类名称", basicCategoryDot.getName());
                            hashMap.put("中类编码", basicCategoryDot.getValue());
                        }
                    }
                    // System.out.println(hashMap);
                    HashMap<String, String> hashMap1 = new HashMap<>(hashMap);
                    hashMapList.add(hashMap1);
            }

            if (!integerTreeSet.isEmpty()) {
                jsonObject.put(String.valueOf(integerTreeSet.last() + 1), "合计");
                jsonObject.put(String.valueOf(integerTreeSet.last() + 2), "");
            }
            if (!orderTime.isEmpty()) {
                jsonObject.put(String.valueOf(orderTime.last() + 1), "-");
                jsonObject.put(String.valueOf(orderTime.last() + 2), "");
            }
            if (!listingTime.isEmpty()) {
                jsonObject.put(String.valueOf(listingTime.last() + 1), "-");
                jsonObject.put(String.valueOf(listingTime.last() + 2), "");
            }
            if (!styleCategories.isEmpty()) {
                jsonObject.put(String.valueOf(styleCategories.last() + 1), "常规");
                jsonObject.put(String.valueOf(styleCategories.last() + 2), "高奢");
            }
            if (!middleClassSet.isEmpty()) {
                int sum = 0;
                int sum1 = 0;
                for (String s : jsonObject.keySet()) {
                    int i1 = Integer.parseInt(s);
                    if (i1 > 2) {
                        // System.out.println(jsonObject.getString(s));
                        if (i1 % 2 == 0) {
                            sum1 += Integer.parseInt(jsonObject.getString(s));
                        } else {
                            sum += Integer.parseInt(jsonObject.getString(s));
                        }
                    }
                }

                jsonObject.put(String.valueOf(middleClassSet.last() + 1), sum);
                jsonObject.put(String.valueOf(middleClassSet.last() + 2), sum1);
            }
            jsonArray.add(jsonObject);
        }
        // System.out.println(band);
        // System.out.println(order);
        // System.out.println(market);

        // 竖排合计
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("2", "合计");
        for (int i = 0; i < jsonArray.size(); i++) {

            if (i > 4) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                for (String s : jsonObject.keySet()) {
                    int i1 = Integer.parseInt(s);
                    if (i1 > 2) {
                        int sum;
                        if (jsonObject2.getInteger(s) != null) {
                            sum = jsonObject.getInteger(s) + jsonObject2.getInteger(s);
                        } else {
                            sum = jsonObject.getInteger(s);
                        }
                        jsonObject2.put(s, sum);
                    }
                }

            }
        }
        jsonArray.add(jsonObject2);
        String dataJson = JSON.toJSONString(jsonArray, SerializerFeature.WriteMapNullValue);

        seasonalPlanningSaveDto.setDataJson(dataJson);

        // 先去查有没有启用的，如果有就将状态设为停用
        QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", seasonalPlanningSaveDto.getCompanyCode());
        queryWrapper.eq("status", "0");
        queryWrapper.eq("season_id", seasonalPlanningSaveDto.getSeasonId());
        queryWrapper.eq("channel_code", seasonalPlanningSaveDto.getChannelCode());
        long l = this.count(queryWrapper);
        seasonalPlanningSaveDto.setStatus(l > 0 ? "1" : "0");
        this.save(seasonalPlanningSaveDto);


        List<SeasonalPlanningDetails> detailsList = new ArrayList<>();
        // System.out.println(hashMapList);
        //如果品类相同,则只存一个,并且将中类内容用逗号分开相加
        List<HashMap<String, String> > hashMapList1 = new ArrayList<>();
        HashMap<String, HashMap<String,String>> hashMap1 =new HashMap<>();
        for (HashMap<String, String> hashMap2 : hashMapList) {
            String name =hashMap2.get("大类名称")+","+hashMap2.get("品类名称");
            if (hashMap1.containsKey(name)) {
                HashMap<String, String> stringStringHashMap = hashMap1.get(name);
                String prodCategoryCode2Name = stringStringHashMap.get("中类名称")+","+hashMap2.get("中类名称");
                String prodCategoryCode2Code = stringStringHashMap.get("中类编码")+","+hashMap2.get("中类编码");
                hashMap2.put("中类名称",prodCategoryCode2Name);
                hashMap2.put("中类编码",prodCategoryCode2Code);
                hashMap1.put(name,hashMap2);
            }else {
                hashMap1.put(name,hashMap2);
            }

        }

        for (String s : hashMap1.keySet()) {
            hashMapList1.add(hashMap1.get(s));
        }
        List<BasicStructureTreeVo> basicStructureTreeVos = ccmFeignService.basicStructureTreeByCode("品类", null, "0,1,2");
        Map<String, BasicStructureTreeVo> map = basicStructureTreeVos.stream().collect(Collectors.toMap(BasicStructureTreeVo::getValue, basicStructureTreeVo -> basicStructureTreeVo));
        for (HashMap<String, String> stringStringHashMap : hashMapList1) {
            SeasonalPlanningDetails seasonalPlanningDetails = new SeasonalPlanningDetails();
            BasicStructureTreeVo basicStructureTreeVo = map.get(stringStringHashMap.get("大类编码"));
            if (basicStructureTreeVo==null){
                throw new RuntimeException("大类"+stringStringHashMap.get("大类名称")+"不存在");
            }
            if (basicStructureTreeVo.getChildren() ==null || basicStructureTreeVo.getChildren().isEmpty()){
                throw new RuntimeException(stringStringHashMap.get("大类名称")+"下的品类"+ stringStringHashMap.get("品类名称")+"不存在");
            }

            Map<String, BasicStructureTreeVo> map1 = basicStructureTreeVo.getChildren().stream().collect(Collectors.toMap(BasicStructureTreeVo::getValue, basicStructureTreeVo1 -> basicStructureTreeVo1));
            BasicStructureTreeVo basicStructureTreeVo1 = map1.get(stringStringHashMap.get("品类编码"));
            if (basicStructureTreeVo1==null){
                throw new RuntimeException(stringStringHashMap.get("大类名称")+"下的品类"+ stringStringHashMap.get("品类名称")+"不存在");
            }

            if (StringUtils.isNotBlank(stringStringHashMap.get("中类名称"))){
                if (basicStructureTreeVo1.getChildren() ==null || basicStructureTreeVo1.getChildren().isEmpty()){
                    throw new RuntimeException(stringStringHashMap.get("中类名称")+"下的中类"+ stringStringHashMap.get("中类名称")+"不存在");
                }

                Map<String, BasicStructureTreeVo> map2 = basicStructureTreeVo1.getChildren().stream().collect(Collectors.toMap(BasicStructureTreeVo::getValue, basicStructureTreeVo2 -> basicStructureTreeVo2));

                for (String s : stringStringHashMap.get("中类编码").split(",")) {
                    BasicStructureTreeVo basicStructureTreeVo2 = map2.get(s);
                    if (basicStructureTreeVo2==null){
                        throw new RuntimeException(stringStringHashMap.get("品类名称")+"下的中类"+ stringStringHashMap.get("中类名称")+"不存在");
                    }
                }

            }

            seasonalPlanningDetails.setProdCategory1stCode(stringStringHashMap.get("大类编码"));
            seasonalPlanningDetails.setProdCategory1stName(stringStringHashMap.get("大类名称"));
            seasonalPlanningDetails.setProdCategory2ndCode(stringStringHashMap.get("中类编码"));
            seasonalPlanningDetails.setProdCategory2ndName(stringStringHashMap.get("中类名称"));
            seasonalPlanningDetails.setProdCategoryCode(stringStringHashMap.get("品类编码"));
            seasonalPlanningDetails.setProdCategoryName(stringStringHashMap.get("品类名称"));
            seasonalPlanningDetails.setBandName(String.join(",", bandNames));
            seasonalPlanningDetails.setStyleCategory(String.join(",", styleCategorieList));
            List<String> bandCodes = new ArrayList<>();
            Map<String, BasicBaseDict> dictMap = c8Band.stream().collect(Collectors.toMap(BasicBaseDict::getName, band -> band));
            for (String bandName : bandNames) {
                BasicBaseDict basicBaseDict = dictMap.get(bandName);
                if (basicBaseDict == null) {
                    throw new RuntimeException("找不到"+bandName+"的编码");
                }
                bandCodes.add(basicBaseDict.getValue());
            }

            seasonalPlanningDetails.setBandCode(String.join(",", bandCodes));
            seasonalPlanningDetails.setSeasonalPlanningId(seasonalPlanningSaveDto.getId());
            seasonalPlanningDetails.setSeasonalPlanningName(seasonalPlanningSaveDto.getName());
            seasonalPlanningDetails.setSkcCount(String.join(",",sums));
            seasonalPlanningDetails.setLaunchTime(String.join(",", markets));
            seasonalPlanningDetails.setOrderTime(String.join(",", orders));

            detailsList.add(seasonalPlanningDetails);
        }
        seasonalPlanningDetailsService.saveBatch(detailsList);
        // seasonalPlanningDetails.setSkcCount(sum);
        // seasonalPlanningDetails.setSeasonalPlanningId();

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

    @Override
    public SeasonalPlanningVo getDetailById(String id) {
        QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tsp.id",id);
        List<SeasonalPlanningVo> list = baseMapper.listByQueryWrapper(queryWrapper);
        SeasonalPlanningVo seasonalPlanningVo = list.get(0);
        String dataJson = this.addTotal(seasonalPlanningVo.getDataJson());
        seasonalPlanningVo.setDataJson(dataJson);
        List<SeasonalPlanningDetails> detailsList = seasonalPlanningDetailsService.listByField("seasonal_planning_id", seasonalPlanningVo.getId());

        //拆分成中类维度
        List<SeasonalPlanningDetails> list1 =new ArrayList<>();
        List<SeasonalPlanningDetails> list2 =new ArrayList<>();
        List<SeasonalPlanningDetails> list3 =new ArrayList<>();
        for (SeasonalPlanningDetails seasonalPlanningDetails : detailsList) {
            String prodCategory2ndCode = seasonalPlanningDetails.getProdCategory2ndCode();
            if (StringUtils.isNotBlank(prodCategory2ndCode)&& prodCategory2ndCode.split(",").length>1) {
                String[] split = prodCategory2ndCode.split(",");
                for (int i = 0; i < split.length; i++) {
                    SeasonalPlanningDetails seasonalPlanningDetails1 =new SeasonalPlanningDetails();
                    BeanUtil.copyProperties(seasonalPlanningDetails,seasonalPlanningDetails1);
                    seasonalPlanningDetails1.setProdCategory2ndCode(split[i]);
                    seasonalPlanningDetails1.setProdCategory2ndName(seasonalPlanningDetails.getProdCategory2ndName().split(",")[i]);
                    list1.add(seasonalPlanningDetails1);
                }
            } else  {
                list1.add(seasonalPlanningDetails);
            }
        }
        //拆分成波段维度
        for (SeasonalPlanningDetails seasonalPlanningDetails : list1) {
            String bandCode = seasonalPlanningDetails.getBandCode();
            if(StringUtils.isNotBlank(bandCode) && bandCode.split(",").length>1) {
                String[] split = bandCode.split(",");
                //去重
                // List<String> arrayList =  Arrays.stream(split).distinct().collect(Collectors.toList());
                List<String> arrayList = Arrays.asList(split);
                for (int i = 0; i < arrayList.size(); i++) {
                    SeasonalPlanningDetails seasonalPlanningDetails1 = new SeasonalPlanningDetails();
                    BeanUtil.copyProperties(seasonalPlanningDetails, seasonalPlanningDetails1);
                    seasonalPlanningDetails1.setBandCode(arrayList.get(i));
                    seasonalPlanningDetails1.setBandName(seasonalPlanningDetails.getBandName().split(",")[i]);
                    list2.add(seasonalPlanningDetails1);
                }
            }
        }

        // System.out.println(list2);
        //查询订货本下单的数据
        for (int i = 0; i < list2.size(); i++) {
            SeasonalPlanningDetails seasonalPlanningDetails = list2.get(i);
            String prodCategory2ndCode = seasonalPlanningDetails.getProdCategory2ndCode();
            String prodCategory1stCode = seasonalPlanningDetails.getProdCategory1stCode();
            String prodCategoryCode = seasonalPlanningDetails.getProdCategoryCode();
            String bandCode = seasonalPlanningDetails.getBandCode();

            OrderBookDetailQueryDto dto = new OrderBookDetailQueryDto();
            // dto.setBandCode(bandCode);
            dto.setPlanningSeasonId(seasonalPlanningVo.getSeasonId());
            dto.setCategoryCode(prodCategoryCode);
            dto.setProdCategory1st(prodCategory1stCode);
            if (StringUtils.isNotBlank(prodCategory2ndCode)){
                dto.setProdCategory2ndCode(prodCategory2ndCode);
            }
            dto.setIsOrder("1");
            BaseQueryWrapper<OrderBookDetail> bookDetailBaseQueryWrapper = orderBookDetailService.buildQueryWrapper(dto);
            bookDetailBaseQueryWrapper.orderByDesc("commissioning_date");
            if (i%2==0){
                bookDetailBaseQueryWrapper.like("tsc.style_no","Q");
                seasonalPlanningDetails.setStyleCategory("高奢");
            }else {
                seasonalPlanningDetails.setStyleCategory("常规");
                bookDetailBaseQueryWrapper.notLike("tsc.style_no","Q");
            }

            seasonalPlanningDetails.setSkcCount("0");
            seasonalPlanningDetails.setOrderTime("");
            seasonalPlanningDetails.setLaunchTime("");
            List<OrderBookDetailVo> bookDetailVos = orderBookDetailService.querylist(bookDetailBaseQueryWrapper, null);
            int z=0;
            Map<String,Integer> map = new HashMap<>();
            for (OrderBookDetailVo bookDetailVo : bookDetailVos) {
                if (bookDetailVo.getBandCode().equals(bandCode)) {
                    z++;
                    if (bookDetailVo.getCommissioningDate() != null) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String format = simpleDateFormat.format(bookDetailVo.getCommissioningDate());
                        seasonalPlanningDetails.setOrderTime(format);
                        seasonalPlanningDetails.setLaunchTime(format);
                    }
                }else {
                    String band = bookDetailVo.getBandCode()+"_"+bookDetailVo.getBandName();
                    map.merge(band, 1, Integer::sum);
                }

            }
            for (String band : map.keySet()) {
                if (StringUtils.isNotBlank(band)){
                    String[] split = band.split("_");
                    if (split.length>1){
                        SeasonalPlanningDetails seasonalPlanningDetails1 = new SeasonalPlanningDetails();
                        BeanUtil.copyProperties(seasonalPlanningDetails, seasonalPlanningDetails1);
                        seasonalPlanningDetails1.setBandCode(split[0]);
                        seasonalPlanningDetails1.setBandName(split[1]);
                        seasonalPlanningDetails1.setSkcCount(String.valueOf(map.get(band)));
                        list3.add(seasonalPlanningDetails1);
                    }
                }
            }

            seasonalPlanningDetails.setSkcCount(String.valueOf(z));

        }
        list2.addAll(list3);
        seasonalPlanningVo.setSeasonalPlanningDetailsList(list2);
        return seasonalPlanningVo;
    }


    private BaseQueryWrapper<SeasonalPlanning> buildQueryWrapper(SeasonalPlanningQueryDto seasonalPlanningQueryDto) {
        BaseQueryWrapper<SeasonalPlanning> baseQueryWrapper = new BaseQueryWrapper<>();
        baseQueryWrapper.notEmptyEq("tsp.season_id", seasonalPlanningQueryDto.getSeasonId());
        baseQueryWrapper.notEmptyEq("tsp.channel_code", seasonalPlanningQueryDto.getChannelCode());
        baseQueryWrapper.orderByDesc("tsp.id");
        return baseQueryWrapper;
    }

    /**
     * 增加合计
     */
    private String addTotal(String dataJson) {
        JSONArray jsonArray = JSON.parseArray(dataJson);
        jsonArray.remove(jsonArray.size()-1);
        JSONArray jsonArray1 =new JSONArray();

        JSONObject object =new JSONObject();
        boolean b =false;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (i<=4){
                jsonArray1.add(jsonObject);
                continue;
            }

            if (StringUtils.isNotBlank(jsonObject.getString("1"))){
                if (b){
                    object.put("2","合计");
                    jsonArray1.add(object);
                    object=new JSONObject();
                }
                b=true;
            }

            for (String s : jsonObject.keySet()) {
                int si= Integer.parseInt(s);
                if (si>2){
                    Integer integer = jsonObject.getInteger(s);
                    Integer integer1 = object.getInteger(s);
                    if (integer1==null){
                        object.put(s, integer);
                    } else {
                        object.put(s, integer + integer1);
                    }
                }
            }

            jsonArray1.add(jsonObject);
            if (i== (jsonArray.size())-1){
                object.put("2","合计");
                jsonArray1.add(object);
                object=new JSONObject();
            }
        }

        return jsonArray1.toJSONString();
    }
}
