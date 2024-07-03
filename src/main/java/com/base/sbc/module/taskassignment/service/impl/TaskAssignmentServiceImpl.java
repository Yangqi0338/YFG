/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.GeneralFlagEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternmaking.dto.SetPatternDesignDto;
import com.base.sbc.module.patternmaking.dto.TechnologyCenterTaskSearchDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.TechnologyCenterTaskVo;
import com.base.sbc.module.planning.dto.ProductCategoryItemSearchDto;
import com.base.sbc.module.planning.dto.SeatSendDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.taskassignment.constants.ResultConstant;
import com.base.sbc.module.taskassignment.dto.QueryTaskAssignmentDTO;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentDTO;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import com.base.sbc.module.taskassignment.entity.TaskAssignmentRecords;
import com.base.sbc.module.taskassignment.enums.TriggerMenuEnum;
import com.base.sbc.module.taskassignment.mapper.TaskAssignmentMapper;
import com.base.sbc.module.taskassignment.service.TaskAssignmentRecordsService;
import com.base.sbc.module.taskassignment.service.TaskAssignmentService;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentRecordsVO;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：任务分配 service类
 *
 * @author XHTE
 * @create 2024/6/27
 */
@Service
@Slf4j
public class TaskAssignmentServiceImpl extends BaseServiceImpl<TaskAssignmentMapper, TaskAssignment> implements TaskAssignmentService {

    @Autowired
    @Lazy
    private PlanningCategoryItemService planningCategoryItemService;
    @Autowired
    @Lazy
    private StyleService styleService;
    @Autowired
    @Lazy
    private PatternMakingService patternMakingService;
    @Autowired
    @Lazy
    private TaskAssignmentRecordsService taskAssignmentRecordsService;
    @Autowired
    @Lazy
    private PlanningSeasonService planningSeasonService;
    @Autowired
    private CcmFeignService ccmFeignService;
    @Autowired
    private AmcFeignService amcFeignService;

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveTaskAssignment(TaskAssignmentDTO saveTaskAssignment) {
        if (ObjectUtil.isEmpty(saveTaskAssignment)) {
            throw new OtherException(ResultConstant.OPERATION_DATA_NOT_EMPTY);
        }
        // 查询是否存在相同数据，包括：品牌/虚拟部门/品类/人员/触发菜单
        taskAssignmentComparison(saveTaskAssignment, false);
        // 如果没有则保存
        return save(saveTaskAssignment);
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeTaskAssignment(String taskAssignmentId) {
        if (ObjectUtil.isEmpty(taskAssignmentId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        TaskAssignment taskAssignment = getById(taskAssignmentId);
        if (ObjectUtil.isEmpty(taskAssignment)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        if (taskAssignment.getEnableFlag().equals(GeneralFlagEnum.YES.getCode())) {
            throw new OtherException(ResultConstant.CANT_REMOVE_ENABLE_TASK);
        }
        return removeById(taskAssignment);
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTaskAssignment(TaskAssignmentDTO updateTaskAssignment) {
        if (ObjectUtil.isEmpty(updateTaskAssignment) || ObjectUtil.isEmpty(updateTaskAssignment.getId())) {
            throw new OtherException(ResultConstant.OPERATION_DATA_NOT_EMPTY);
        }
        // 查询原始数据是否存在
        TaskAssignment taskAssignment = getById(updateTaskAssignment.getId());
        if (ObjectUtil.isEmpty(taskAssignment)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        // 查询是否存在相同数据，包括：品牌/虚拟部门/品类/人员/触发菜单
        taskAssignmentComparison(updateTaskAssignment, true);
        return updateById(updateTaskAssignment);
    }

    @Override
    public PageInfo<TaskAssignmentVO> queryTaskAssignmentPage(QueryTaskAssignmentDTO queryTaskAssignment) {
        PageHelper.startPage(queryTaskAssignment.getPageNum(), queryTaskAssignment.getPageSize());
        BaseQueryWrapper<TaskAssignmentVO> taskAssignmentQueryWrapper = new BaseQueryWrapper<>();
        boolean flag = QueryGenerator.initQueryWrapperByMap(taskAssignmentQueryWrapper, queryTaskAssignment);
        List<TaskAssignmentVO> taskAssignmentList = baseMapper.queryTaskAssignmentPage(taskAssignmentQueryWrapper, queryTaskAssignment);
        if (flag) {
            return new PageInfo<>(taskAssignmentList);
        }
        if (ObjectUtil.isNotEmpty(taskAssignmentList)) {
            List<String> askAssignmentIdList
                    = taskAssignmentList.stream().map(TaskAssignmentVO::getId).collect(Collectors.toList());
            List<TaskAssignmentRecordsVO> taskAssignmentRecordsList = baseMapper.queryTaskAssignmentRecordsCount(askAssignmentIdList);
            Map<String, Integer> map = new HashMap<>();
            if (ObjectUtil.isNotEmpty(taskAssignmentRecordsList)) {
                map = taskAssignmentRecordsList
                        .stream().collect(
                                Collectors.toMap(TaskAssignmentRecordsVO::getTaskAssignmentId
                                        , TaskAssignmentRecordsVO::getCount));
            }
            for (TaskAssignmentVO taskAssignmentVO : taskAssignmentList) {
                Integer count = map.get(taskAssignmentVO.getId());
                taskAssignmentVO.setTriggerNum(ObjectUtil.isEmpty(count) ? 0 : count);
            }
        }
        return new PageInfo<>(taskAssignmentList);
    }

    @Override
    public TaskAssignmentVO queryTaskAssignmentDetail(String taskAssignmentId) {
        if (ObjectUtil.isEmpty(taskAssignmentId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        TaskAssignment taskAssignment = getById(taskAssignmentId);
        if (ObjectUtil.isEmpty(taskAssignment)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        return BeanUtil.copyProperties(taskAssignment, TaskAssignmentVO.class);
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean enableDisableTaskAssignment(TaskAssignmentDTO enableDisableTaskAssignment) {
        if (ObjectUtil.isEmpty(enableDisableTaskAssignment) || ObjectUtil.isEmpty(enableDisableTaskAssignment.getId())) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        if (ObjectUtil.isEmpty(GeneralFlagEnum.getValueByCode(enableDisableTaskAssignment.getEnableFlag()))) {
            throw new OtherException(ResultConstant.ENABLE_DISABLED_STATUS_ERROR);
        }
        TaskAssignment taskAssignment = getById(enableDisableTaskAssignment.getId());
        if (ObjectUtil.isEmpty(taskAssignment)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        taskAssignment.setEnableFlag(enableDisableTaskAssignment.getEnableFlag());
        return updateById(taskAssignment);
    }

    @Override
    public List<TaskAssignmentVO> queryTaskAssignmentFilterCriteria(TaskAssignmentDTO queryTaskAssignment) {
        LambdaQueryWrapper<TaskAssignment> taskAssignmentQueryWrapper = new LambdaQueryWrapper<>();
        switch (queryTaskAssignment.getType()) {
            case 1:
                // 品牌
                taskAssignmentQueryWrapper.like(TaskAssignment::getBrandName, queryTaskAssignment.getSearch());
                taskAssignmentQueryWrapper.select(TaskAssignment::getBrand, TaskAssignment::getBrandName);
                taskAssignmentQueryWrapper.groupBy(TaskAssignment::getBrandName);
                break;
            case 2:
                // 虚拟部门
                taskAssignmentQueryWrapper.like(TaskAssignment::getVirtualDeptName, queryTaskAssignment.getSearch());
                taskAssignmentQueryWrapper.select(TaskAssignment::getVirtualDeptId, TaskAssignment::getVirtualDeptName);
                taskAssignmentQueryWrapper.groupBy(TaskAssignment::getVirtualDeptName);
                break;
            case 3:
                // 大类
                taskAssignmentQueryWrapper.like(TaskAssignment::getProdCategory1stName, queryTaskAssignment.getSearch());
                taskAssignmentQueryWrapper.select(TaskAssignment::getProdCategory1st, TaskAssignment::getProdCategory1stName);
                taskAssignmentQueryWrapper.groupBy(TaskAssignment::getProdCategory1stName);
                break;
            case 4:
                // 品类
                taskAssignmentQueryWrapper.like(TaskAssignment::getProdCategoryName, queryTaskAssignment.getSearch());
                taskAssignmentQueryWrapper.select(TaskAssignment::getProdCategory, TaskAssignment::getProdCategoryName);
                taskAssignmentQueryWrapper.groupBy(TaskAssignment::getProdCategoryName);
                break;
            case 5:
                // 中类
                taskAssignmentQueryWrapper.like(TaskAssignment::getProdCategory2ndName, queryTaskAssignment.getSearch());
                taskAssignmentQueryWrapper.select(TaskAssignment::getProdCategory2nd, TaskAssignment::getProdCategory2ndName);
                taskAssignmentQueryWrapper.groupBy(TaskAssignment::getProdCategory2ndName);
                break;
            case 6:
                // 小类
                taskAssignmentQueryWrapper.like(TaskAssignment::getProdCategory3rdName, queryTaskAssignment.getSearch());
                taskAssignmentQueryWrapper.select(TaskAssignment::getProdCategory3rd, TaskAssignment::getProdCategory3rdName);
                taskAssignmentQueryWrapper.groupBy(TaskAssignment::getProdCategory3rdName);
                break;
            case 7:
                // 人员
                taskAssignmentQueryWrapper.like(TaskAssignment::getUserName, queryTaskAssignment.getSearch());
                taskAssignmentQueryWrapper.select(TaskAssignment::getUserId, TaskAssignment::getUserName);
                taskAssignmentQueryWrapper.groupBy(TaskAssignment::getUserName);
                break;
            default:
                throw new OtherException(ResultConstant.FILTER_TYPE_DOES_NOT_EXIST);

        }
        List<TaskAssignment> taskAssignmentList = list(taskAssignmentQueryWrapper);
        return BeanUtil.copyToList(taskAssignmentList, TaskAssignmentVO.class);
    }

    @Override
    public void runTaskAssignment(TaskAssignmentDTO queryTaskAssignment) {
        // 当前触发菜单
        String triggerMenu = queryTaskAssignment.getTriggerMenu();
        if (ObjectUtil.isEmpty(TriggerMenuEnum.checkValue(queryTaskAssignment.getTriggerMenu()))) {
            throw new OtherException(ResultConstant.TRIGGER_MENU_DOES_NOT_EXIST);
        }
        String brand = queryTaskAssignment.getBrand();
        String virtualDeptId = queryTaskAssignment.getVirtualDeptId();
        String prodCategory1st = queryTaskAssignment.getProdCategory1st();
        String prodCategory = queryTaskAssignment.getProdCategory();
        String prodCategory2nd = queryTaskAssignment.getProdCategory2nd();
        String prodCategory3rd = queryTaskAssignment.getProdCategory3rd();
        if (ObjectUtil.isEmpty(brand)
                || ObjectUtil.isEmpty(virtualDeptId)
                || ObjectUtil.isEmpty(prodCategory1st)
                || ObjectUtil.isEmpty(prodCategory)
                || ObjectUtil.isEmpty(prodCategory2nd)
                || ObjectUtil.isEmpty(prodCategory3rd)) {
            return;
        }
        // 根据「品牌/虚拟部门（发送部门）/大类/品类/中类/小类/触发菜单」找到合适的任务分配
        LambdaQueryWrapper<TaskAssignment> taskAssignmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        taskAssignmentLambdaQueryWrapper.eq(TaskAssignment::getBrand, brand)
                .eq(TaskAssignment::getVirtualDeptId, virtualDeptId)
                .eq(TaskAssignment::getProdCategory1st, prodCategory1st)
                .eq(TaskAssignment::getProdCategory, prodCategory)
                .eq(TaskAssignment::getProdCategory2nd, prodCategory2nd)
                .eq(TaskAssignment::getProdCategory3rd, prodCategory3rd)
                .like(TaskAssignment::getTriggerMenus, triggerMenu)
                .eq(TaskAssignment::getEnableFlag, GeneralFlagEnum.YES);
        TaskAssignment taskAssignment = getOne(taskAssignmentLambdaQueryWrapper);
        if (ObjectUtil.isNotEmpty(taskAssignment)) {
            if (triggerMenu.equals(TriggerMenuEnum.JSZXKB.getValue())) {
                // 技术中心看板
                TaskAssignmentRecords taskAssignmentRecords = technicalCenterKanbanTriggerOperation(queryTaskAssignment.getDataId(), taskAssignment);
                // 触发菜单增加记录
                if (taskAssignmentRecords != null) {
                    // 设置任务分配 ID
                    taskAssignmentRecords.setTaskAssignmentId(taskAssignment.getId());
                    // 设置触发菜单
                    taskAssignmentRecords.setTriggerMenu(triggerMenu);
                    taskAssignmentRecordsService.save(taskAssignmentRecords);
                }
            } else if (triggerMenu.equals(TriggerMenuEnum.CPJZL.getValue())) {
                TaskAssignmentRecords taskAssignmentRecords = productSeasonTriggerOperation(queryTaskAssignment.getDataId(), taskAssignment);
                // 触发菜单增加记录
                if (taskAssignmentRecords != null) {
                    // 设置任务分配 ID
                    taskAssignmentRecords.setTaskAssignmentId(taskAssignment.getId());
                    // 设置触发菜单
                    taskAssignmentRecords.setTriggerMenu(triggerMenu);
                    taskAssignmentRecordsService.save(taskAssignmentRecords);
                }
            } else {
                log.error("触发菜单「{}」不存在！", triggerMenu);
            }
        }
    }

    /**
     * 技术中心看板触发操作
     *
     * @param dataId         数据 ID
     * @param taskAssignment 任务分配信息
     */
    private TaskAssignmentRecords technicalCenterKanbanTriggerOperation(String dataId, TaskAssignment taskAssignment) {
        // 初始化触发记录数据
        TaskAssignmentRecords taskAssignmentRecords = new TaskAssignmentRecords();
        // 先查询打版指令数据
        PatternMaking patternMaking = patternMakingService.getById(dataId);
        if (ObjectUtil.isEmpty(patternMaking)) {
            return null;
        }
        Style style = styleService.getById(patternMaking.getStyleId());
        if (ObjectUtil.isEmpty(style)) {
            return null;
        }
        taskAssignmentRecords.setDesignNo(style.getDesignNo());
        taskAssignmentRecords.setPatternNo(patternMaking.getPatternNo());
        taskAssignmentRecords.setSampleType(patternMaking.getSampleType());
        taskAssignmentRecords.setSampleTypeName(patternMaking.getSampleTypeName());
        String planningSeasonId = patternMaking.getPlanningSeasonId();

        // 查询版师 判断版师是否符合产品季总览的使用
        List<UserCompany> userCompanyList = amcFeignService.getTeamUserListByPost(planningSeasonId, "版师");
        UserCompany userCompany = userCompanyList.stream().filter(Item -> Item.getUserId().equals(taskAssignment.getUserId())).findFirst().orElse(null);
        if (userCompany == null) {
            // 任务分配的用户不是当前产品季的版师
            PlanningSeason planningSeason = planningSeasonService.getById(planningSeasonId);
            String triggerResult = "当前任务分配人员「" + taskAssignment.getUserName() + "」不是产品季「" + planningSeason.getName() + "」下的版师";
            taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
            taskAssignmentRecords.setTriggerResult(triggerResult);
            log.error(triggerResult);
            return taskAssignmentRecords;
        }
        patternMaking.setPatternDesignId(taskAssignment.getUserId());
        patternMaking.setPatternDesignName(taskAssignment.getUserName());
        if (!patternMakingService.updateById(patternMaking)) {
            String triggerResult = "技术中心看板数据自动更新失败";
            taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
            taskAssignmentRecords.setTriggerResult(triggerResult);
            log.error("设计款「{}」/打版类型「{}」/样版号「{}」{}",
                    style.getDesignNo(),
                    patternMaking.getSampleTypeName(),
                    patternMaking.getPatternNo(),
                    triggerResult);
            return taskAssignmentRecords;
        }
        // 技术中心看板自动下发
        TechnologyCenterTaskSearchDto technologyCenterTaskSearchDto = new TechnologyCenterTaskSearchDto();
        technologyCenterTaskSearchDto.setPatternMakingId(dataId);
        PageInfo<TechnologyCenterTaskVo> technologyCenterTaskVoPageInfo = patternMakingService.technologyCenterTaskList(technologyCenterTaskSearchDto);
        // 没查询到
        if (ObjectUtil.isEmpty(technologyCenterTaskVoPageInfo) || ObjectUtil.isEmpty(technologyCenterTaskVoPageInfo.getList())) {
            String triggerResult = "技术中心看板数据自动更新数据成功，但自动下发失败，失败原因：下发数据不存在";
            taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
            taskAssignmentRecords.setTriggerResult(triggerResult);
            log.error("设计款「{}」/打版类型「{}」/样版号「{}」{}",
                    style.getDesignNo(),
                    patternMaking.getSampleTypeName(),
                    patternMaking.getPatternNo(),
                    triggerResult);
            return taskAssignmentRecords;
        }
        // 拿到产品季总览数据
        TechnologyCenterTaskVo technologyCenterTask = technologyCenterTaskVoPageInfo.getList().get(0);
        if ("0".equals(technologyCenterTask.getPrmSendStatus())) {
            // 如果没有下发再进行下发
            try {
                patternMakingService.prmSendBatch(CollUtil.newArrayList(BeanUtil.copyProperties(technologyCenterTask, SetPatternDesignDto.class)));
            } catch (Exception e) {
                String triggerResult = "技术中心看板数据自动更新数据成功，但自动下发失败，失败原因：" + (e.getMessage() == null ? e.toString() : e.getMessage());
                taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
                taskAssignmentRecords.setTriggerResult(triggerResult);
                log.error("设计款「{}」/打版类型「{}」/样版号「{}」{}",
                        style.getDesignNo(),
                        patternMaking.getSampleTypeName(),
                        patternMaking.getPatternNo(),
                        triggerResult);
                return taskAssignmentRecords;
            }
        }

        // 最后返回了 就是成功了
        taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.YES.getCode());
        taskAssignmentRecords.setTriggerResult("任务执行成功");
        return taskAssignmentRecords;
    }

    /**
     * 产品季总览触发操作
     *
     * @param dataId         数据 ID
     * @param taskAssignment 任务分配信息
     */
    private TaskAssignmentRecords productSeasonTriggerOperation(String dataId, TaskAssignment taskAssignment) {
        // 初始化触发记录数据
        TaskAssignmentRecords taskAssignmentRecords = new TaskAssignmentRecords();
        // 先查询产品季总览数据
        PlanningCategoryItem planningCategoryItem = planningCategoryItemService.getById(dataId);
        if (ObjectUtil.isEmpty(planningCategoryItem)) {
            return null;
        }
        taskAssignmentRecords.setDesignNo(planningCategoryItem.getDesignNo());
        String planningSeasonId = planningCategoryItem.getPlanningSeasonId();

        // 查询设计师 判断设计师是否符合产品季总览的使用
        List<UserCompany> userCompanyList = amcFeignService.getTeamUserListByPost(planningSeasonId, "设计师");
        UserCompany userCompany = userCompanyList.stream().filter(Item -> Item.getUserId().equals(taskAssignment.getUserId())).findFirst().orElse(null);
        if (userCompany == null) {
            // 任务分配的用户不是当前产品季的设计师
            PlanningSeason planningSeason = planningSeasonService.getById(planningSeasonId);
            String triggerResult = "当前任务分配人员「" + taskAssignment.getUserName() + "」不是产品季「" + planningSeason.getName() + "」下的设计师";
            taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
            taskAssignmentRecords.setTriggerResult(triggerResult);
            log.error(triggerResult);
            return taskAssignmentRecords;
        } else if (ObjectUtil.isEmpty(userCompany.getUserCode())) {
            // 任务分配的用户没有设计师代码
            String triggerResult = "当前任务分配人员「" + taskAssignment.getUserName() + "」未完善设计师代码";
            taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
            taskAssignmentRecords.setTriggerResult(triggerResult);
            log.error(triggerResult);
            return taskAssignmentRecords;
        }

        planningCategoryItem.setId(planningCategoryItem.getId());
        planningCategoryItem.setPlanningFinishDate(new Date());
        planningCategoryItem.setDemandFinishDate(new Date());
        planningCategoryItem.setTaskLevel("10");
        planningCategoryItem.setTaskLevelName("常规");
        planningCategoryItem.setDesignerId(taskAssignment.getUserId());
        planningCategoryItem.setDesigner(taskAssignment.getUserName() + "," + userCompany.getUserCode());
        if (!planningCategoryItemService.updateById(planningCategoryItem)) {
            String triggerResult = "产品季总览数据自动更新失败";
            taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
            taskAssignmentRecords.setTriggerResult(triggerResult);
            log.error("设计款「{}」{}", planningCategoryItem.getDesignNo(), triggerResult);
            return taskAssignmentRecords;
        }

        // 产品季总览自动下发
        // 模拟前端查询列表数据
        ProductCategoryItemSearchDto productCategoryItemSearchDto = new ProductCategoryItemSearchDto();
        productCategoryItemSearchDto.setProductCategoryItemId(dataId);
        PageInfo<PlanningSeasonOverviewVo> productCategoryItem = planningCategoryItemService.findProductCategoryItem(productCategoryItemSearchDto);
        // 没查询到
        if (ObjectUtil.isEmpty(productCategoryItem) || ObjectUtil.isEmpty(productCategoryItem.getList())) {
            String triggerResult = "产品季总览数据自动更新数据成功，但自动下发失败，失败原因：下发数据不存在";
            taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
            taskAssignmentRecords.setTriggerResult(triggerResult);
            log.error("设计款「{}」{}", planningCategoryItem.getDesignNo(), triggerResult);
            return taskAssignmentRecords;
        }

        // 拿到产品季总览数据 进行下发操作
        PlanningSeasonOverviewVo planningSeasonOverview = productCategoryItem.getList().get(0);
        if ("0".equals(planningSeasonOverview.getStatus())) {
            // 如果没有下发再进行下发
            try {
                planningCategoryItemService.send(CollUtil.newArrayList(BeanUtil.copyProperties(planningSeasonOverview, SeatSendDto.class)));
            } catch (Exception e) {
                String triggerResult = "产品季总览数据自动更新数据成功，但自动下发失败，失败原因：" + (e.getMessage() == null ? e.toString() : e.getMessage());
                taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.NO.getCode());
                taskAssignmentRecords.setTriggerResult(triggerResult);
                log.error("设计款「{}」{}", planningCategoryItem.getDesignNo(), triggerResult);
                return taskAssignmentRecords;
            }
        }
        // 最后返回了 就是成功了
        taskAssignmentRecords.setSuccessFlag(GeneralFlagEnum.YES.getCode());
        taskAssignmentRecords.setTriggerResult("任务执行成功");
        return taskAssignmentRecords;
    }

    /**
     * 比对任务分配是否存在相同的数据
     *
     * @param needComparisonTaskAssignment 需要比对的任务分配数据
     * @param excludeSelfFlag              排除自身状态 true-排除（编辑） false-不排除（新增）
     */
    private void taskAssignmentComparison(TaskAssignmentDTO needComparisonTaskAssignment, Boolean excludeSelfFlag) {
        LambdaQueryWrapper<TaskAssignment> taskAssignmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (excludeSelfFlag) {
            // 编辑的时候排除自身
            String id = needComparisonTaskAssignment.getId();
            taskAssignmentLambdaQueryWrapper.ne(TaskAssignment::getId, id);
        }
        taskAssignmentLambdaQueryWrapper.eq(TaskAssignment::getBrand, needComparisonTaskAssignment.getBrand()).eq(TaskAssignment::getVirtualDeptId, needComparisonTaskAssignment.getVirtualDeptId()).eq(TaskAssignment::getProdCategory1st, needComparisonTaskAssignment.getProdCategory1st()).eq(TaskAssignment::getProdCategory, needComparisonTaskAssignment.getProdCategory()).eq(TaskAssignment::getProdCategory2nd, needComparisonTaskAssignment.getProdCategory2nd()).eq(TaskAssignment::getProdCategory3rd, needComparisonTaskAssignment.getProdCategory3rd()).and(item -> {
            String triggerMenus = needComparisonTaskAssignment.getTriggerMenus();
            if (ObjectUtil.isEmpty(triggerMenus)) {
                throw new OtherException(ResultConstant.PLEASE_CHOOSE_TRIGGER_MENU);
            }
            String[] triggerMenuArray = triggerMenus.split(",");
            for (String triggerMenu : triggerMenuArray) {
                item.or().like(TaskAssignment::getTriggerMenus, triggerMenu);
            }
        });
        List<TaskAssignment> taskAssignmentList = list(taskAssignmentLambdaQueryWrapper);
        // 存在相同任务分配抛出异常
        if (ObjectUtil.isNotEmpty(taskAssignmentList)) {
            throw new OtherException(ResultConstant.TASK_ASSIGNMENT_EXIST);
        }
    }
}
