package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.mapper.PlanningProjectPlankMapper;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import com.base.sbc.module.planningproject.vo.PlanningProjectPlankVo;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.base.sbc.module.tablecolumn.vo.TableColumnVo;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/11/17 9:45:19
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class PlanningProjectPlankServiceImpl extends BaseServiceImpl<PlanningProjectPlankMapper, PlanningProjectPlank> implements PlanningProjectPlankService {
    private final StyleColorService styleColorService;
    private final StylePicUtils stylePicUtils;
    private final StylePricingMapper stylePricingMapper;
    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;
    @Resource
    @Lazy
    private  PlanningProjectService planningProjectService ;
    @Override
    public  Map<String,Object> ListByDto(PlanningProjectPlankPageDto dto) {
        Map<String,Object> hashMap =new HashMap<>();
        BaseQueryWrapper<PlanningProjectPlank> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tppp.planning_project_id",dto.getPlanningProjectId());
        queryWrapper.notEmptyEq("tppd.prod_category_code",dto.getProdCategoryCode());
        queryWrapper.notEmptyEq("tppd.prod_category2nd_code",dto.getProdCategory2ndCode());
        queryWrapper.notEmptyEq("tppd.prod_category1st_code",dto.getProdCategory1stCode());
        if (StringUtils.isNotEmpty(dto.getPlanningBandCode())) {
            String[] split = dto.getPlanningBandCode().split(",");
            queryWrapper.in("tppp.band_code", Arrays.asList(split));
        }

        queryWrapper.notEmptyEq("tppp.bulk_style_no",dto.getPlanningBulkStyleNo());
        queryWrapper.orderBy(true,true,"tppp.band_code");

        List<PlanningProjectPlankVo> list = this.baseMapper.queryPage(queryWrapper);

        List<TableColumnVo> tableColumnVos =new ArrayList<>();
        Map<String, Integer> map =new TreeMap<>();
        for (PlanningProjectPlankVo planningProjectPlankVo : list) {
            //获取所有波段,当作列
            if (StringUtils.isNotEmpty(planningProjectPlankVo.getBandName())){
                Integer i = map.get(planningProjectPlankVo.getBandName());
                if (i == null) {
                    map.put(planningProjectPlankVo.getBandName(), 1);
                }else {
                    i++;
                    map.put(planningProjectPlankVo.getBandName(), i);
                }
            }

            if (StringUtils.isNotEmpty(planningProjectPlankVo.getStyleColorId())) {
                List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(planningProjectPlankVo.getStyleColorId());
                planningProjectPlankVo.setFieldManagementVos(fieldManagementVos);
            }
        }
        //生成表格列
        for (Map.Entry<String, Integer> stringStringEntry : map.entrySet()) {
            TableColumnVo tableColumnVo =new TableColumnVo();
            tableColumnVo.setTitle(stringStringEntry.getKey());
            tableColumnVo.setNum(String.valueOf(stringStringEntry.getValue()));
            tableColumnVos.add(tableColumnVo);
        }

        //匹配
        this.match(list);
        stylePicUtils.setStyleColorPic2(list, "pic");
        hashMap.put("list",list);
        hashMap.put("tableColumnVos",tableColumnVos);
        return hashMap;
    }

    /**
     * 坑位匹配
     *
     * @param list 坑位列表
     */
    @Override
    public void match(List<PlanningProjectPlankVo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        PlanningProject planningProject = planningProjectService.getById(list.get(0).getPlanningProjectId());
        //查询已经匹配的大货款号
        List<PlanningProjectPlank> projectPlanks = this.list(new QueryWrapper<PlanningProjectPlank>().select("bulk_style_no").isNotNull("bulk_style_no").last("and bulk_style_no != ''"));
        List<String>  bulkNos = projectPlanks.stream().map(PlanningProjectPlank::getBulkStyleNo).distinct().collect(Collectors.toList());

        //匹配实体
        for (PlanningProjectPlankVo planningProjectPlankVo : list) {
            // //如果已经匹配上了,就不再匹配
            if (!"0".equals(planningProjectPlankVo.getMatchingStyleStatus())){
                continue;
            }
            // 匹配规则 产品季  大类 品类 (中类有就匹配)  波段 第一维度  这5匹配
            BaseQueryWrapper<StyleColor> styleColorQueryWrapper = new BaseQueryWrapper<>();
            styleColorQueryWrapper.eq("ts.planning_season_id", planningProject.getSeasonId());
            styleColorQueryWrapper.eq("ts.prod_category1st", planningProjectPlankVo.getProdCategory1stCode());
            styleColorQueryWrapper.eq("1".equals(planningProjectPlankVo.getIsProdCategory2nd()), "ts.prod_category2nd", planningProjectPlankVo.getProdCategory2ndCode());
            styleColorQueryWrapper.eq("ts.prod_category", planningProjectPlankVo.getProdCategoryCode());
            styleColorQueryWrapper.eq("tsc.band_code", planningProjectPlankVo.getBandCode());
            styleColorQueryWrapper.eq("tsc.order_flag", "1");
            if (!bulkNos.isEmpty()){
                styleColorQueryWrapper.notIn("tsc.style_no", bulkNos);
            }

            List<StyleColorVo> styleColorList = stylePricingMapper.getByStyleList(styleColorQueryWrapper);
            if (styleColorList == null || styleColorList.isEmpty()) {
                continue;
            }
            //  初步匹配上,再去筛选维度
            for (StyleColorVo styleColorVo : styleColorList) {
                List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(styleColorVo.getId());
                for (FieldManagementVo fieldManagementVo : fieldManagementVos) {
                    if (fieldManagementVo.getFieldId().equals(planningProjectPlankVo.getDimensionId()) && fieldManagementVo.getVal().equals(planningProjectPlankVo.getDimensionValue())) {
                        // 说明匹配上了
                        planningProjectPlankVo.setBulkStyleNo(styleColorVo.getStyleNo());
                        planningProjectPlankVo.setMatchingStyleStatus("2");
                        planningProjectPlankVo.setPic(styleColorVo.getStyleColorPic());
                        planningProjectPlankVo.setBandCode(styleColorVo.getBandCode());
                        planningProjectPlankVo.setBandName(styleColorVo.getBandName());
                        planningProjectPlankVo.setStyleColorId(styleColorVo.getId());
                        BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColorVo.getColorCode()));
                        if (colourLibrary != null) {
                            planningProjectPlankVo.setColorSystem(colourLibrary.getColorType());
                        }

                        PlanningProjectPlank planningProjectPlank = new PlanningProjectPlank();
                        BeanUtil.copyProperties(planningProjectPlankVo, planningProjectPlank);
                        this.updateById(planningProjectPlank);
                        bulkNos.add(planningProjectPlankVo.getBulkStyleNo());
                        planningProject.setIsMatch("1");
                    }
                }
            }
        }

        List<PlanningProjectPlankVo> list1 =new ArrayList<>();
        //匹配虚拟
        for (PlanningProjectPlankVo planningProjectPlankVo : list) {

            // 匹配规则 产品季  大类 品类 (中类有就匹配)  波段 第一维度  这5匹配
            BaseQueryWrapper<StyleColor> styleColorQueryWrapper = new BaseQueryWrapper<>();
            styleColorQueryWrapper.eq("ts.planning_season_id", planningProject.getSeasonId());
            styleColorQueryWrapper.eq("ts.prod_category1st", planningProjectPlankVo.getProdCategory1stCode());
            styleColorQueryWrapper.eq("1".equals(planningProjectPlankVo.getIsProdCategory2nd()), "ts.prod_category2nd", planningProjectPlankVo.getProdCategory2ndCode());
            styleColorQueryWrapper.eq("ts.prod_category", planningProjectPlankVo.getProdCategoryCode());
            styleColorQueryWrapper.eq("tsc.band_code", planningProjectPlankVo.getBandCode());
            styleColorQueryWrapper.eq("tsc.order_flag", "1");
            if (!bulkNos.isEmpty()) {
                styleColorQueryWrapper.notIn("tsc.style_no", bulkNos);
            }

            List<StyleColorVo> styleColorList = stylePricingMapper.getByStyleList(styleColorQueryWrapper);
            if (styleColorList == null || styleColorList.isEmpty()) {
                continue;
            }
            //  初步匹配上,再去筛选维度
            for (StyleColorVo styleColorVo : styleColorList) {
                List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(styleColorVo.getId());

                for (FieldManagementVo fieldManagementVo : fieldManagementVos) {
                    if (fieldManagementVo.getFieldId().equals(planningProjectPlankVo.getDimensionId()) && fieldManagementVo.getVal().equals(planningProjectPlankVo.getDimensionValue())) {
                        // 说明匹配上了
                        PlanningProjectPlankVo planningProjectPlankVo1 = new PlanningProjectPlankVo();
                        BeanUtil.copyProperties(planningProjectPlankVo, planningProjectPlankVo1);
                        planningProjectPlankVo1.setBulkStyleNo(styleColorVo.getStyleNo());
                        planningProjectPlankVo1.setMatchingStyleStatus("2");
                        planningProjectPlankVo1.setPic(styleColorVo.getStyleColorPic());
                        planningProjectPlankVo1.setBandCode(styleColorVo.getBandCode());
                        planningProjectPlankVo1.setBandName(styleColorVo.getBandName());
                        planningProjectPlankVo1.setStyleColorId(styleColorVo.getId());
                        BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColorVo.getColorCode()));
                        if (colourLibrary != null) {
                            planningProjectPlankVo.setColorSystem(colourLibrary.getColorType());
                        }
                        planningProjectPlankVo.setId(null);
                        planningProjectPlankVo.setIsVirtual("1");
                        bulkNos.add(planningProjectPlankVo1.getBulkStyleNo());
                        list1.add(planningProjectPlankVo1);
                        planningProject.setIsMatch("1");
                    }
                }
            }
        }
        list.addAll(list1);
        planningProjectService.updateById(planningProject);
    }
}
