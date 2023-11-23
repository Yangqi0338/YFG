package com.base.sbc.module.planningproject.service.impl;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.mapper.PlanningProjectPlankMapper;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.vo.PlanningProjectPlankVo;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.tablecolumn.vo.TableColumnVo;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Override
    public  Map<String,Object> ListByDto(PlanningProjectPlankPageDto dto) {
        Map<String,Object> hashMap =new HashMap<>();
        BaseQueryWrapper<PlanningProjectPlank> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tpp.planning_project_id",dto.getPlanningProjectId());
        if (StringUtils.isNotEmpty(dto.getPlanningBandCode())) {
            String[] split = dto.getPlanningBandCode().split(",");
            queryWrapper.in("tpp.band_code", Arrays.asList(split));
        }

        queryWrapper.notEmptyEq("tpp.bulk_style_no",dto.getPlanningBulkStyleNo());
        queryWrapper.orderBy(true,true,"tpp.band_code");

        List<PlanningProjectPlankVo> list = this.baseMapper.queryPage(queryWrapper);
        List<TableColumnVo> tableColumnVos =new ArrayList<>();
        Map<String, Integer> map =new HashMap<>();
        for (PlanningProjectPlankVo planningProjectPlankVo : list) {
            //获取所有波段,当作列
            Integer i = map.get(planningProjectPlankVo.getBandName());
            if (i == null) {
                map.put(planningProjectPlankVo.getBandName(), 1);
            }else {
                i++;
                map.put(planningProjectPlankVo.getBandName(), i);
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
        stylePicUtils.setStyleColorPic2(list, "pic");
        hashMap.put("list",list);
        hashMap.put("tableColumnVos",tableColumnVos);
        return hashMap;
    }
}
