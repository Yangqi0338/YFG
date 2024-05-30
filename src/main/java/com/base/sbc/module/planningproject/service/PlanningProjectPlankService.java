package com.base.sbc.module.planningproject.service;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.vo.FieldDisplayVo;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.vo.PlanningProjectPlankVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/11/17 9:44:54
 * @mail 247967116@qq.com
 */
public interface PlanningProjectPlankService extends BaseService<PlanningProjectPlank> {
    Map<String,Object> ListByDto(PlanningProjectPlankPageDto dto);

    /**
     * 坑位匹配
     */
    void match( List<PlanningProjectPlankVo> list);

    /**
     * 根据大货款号取消匹配
     */
    void unMatchByBulkStyleNo(String bulkStyleNo);

    List<FieldDisplayVo> getDimensionFieldCard(DimensionLabelsSearchDto dto);

    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    void delById(PlanningProjectPlank plank);

    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    void saveData(@RequestBody PlanningProjectPlank planningProjectPlank);

    void updateCategoryAndSeasonPlanning(PlanningProjectPlank planningProjectPlank,
                                         PlanningProjectDimension planningProjectDimension,
                                         long pitPositionCount,
                                         Integer type
    );
}
