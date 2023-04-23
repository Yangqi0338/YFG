package com.base.sbc.module.planning.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.vo.UserInfoVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningBandService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningBandSummaryInfoVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：
 * @address com.base.sbc.module.planning.controller.ProductSeasonController
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 13:47
 * @version 1.0
 */
@RestController
@Api(tags = "产品季总览相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/productSeason", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProductSeasonController extends BaseController {

    @Resource
    private PlanningSeasonService planningSeasonService;
    @Resource
    private PlanningBandService planningBandService;

    @Resource
    private CcmFeignService ccmFeignService;
    @Resource
    private AmcFeignService amcFeignService;
    @Resource
    PlanningCategoryItemService planningCategoryItemService;
    @ApiOperation(value = "查询产品季-分页查询")
    @GetMapping
    public PageInfo query(PlanningSeasonSearchDto dto){
        QueryWrapper qc = new QueryWrapper();
        qc.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qc.eq(COMPANY_CODE, getUserCompany());
        qc.like(StrUtil.isNotBlank(dto.getSearch()), "name",dto.getSearch());
        dto.setOrderBy("create_date desc ");
        Page<PlanningSeason> objects = PageHelper.startPage(dto);
        planningSeasonService.selectProductSeason(qc);
        PageInfo<PlanningSeason> planningSeasonPageInfo = objects.toPageInfo();
       return planningSeasonPageInfo;
    }

    @ApiOperation(value = "按波段展开-分页查询")
    @GetMapping("/expandByBand")
    public PageInfo expandByBand(@Valid  ProductSeasonExpandByBandSearchDto dto){
        QueryWrapper bqw = new QueryWrapper();
        bqw.eq("del_flag", BaseEntity.DEL_FLAG_NORMAL);
        bqw.eq(COMPANY_CODE, getUserCompany());
        bqw.eq("status", BaseGlobal.STOCK_STATUS_CHECKED);
        bqw.eq("planning_season_id",dto.getPlanningSeasonId());
        dto.setOrderBy("create_date desc ");
        Page<PlanningBand> objects = PageHelper.startPage(dto);
        planningBandService.list(bqw);
        PageInfo<PlanningBand> planningSeasonPageInfo = objects.toPageInfo();
        return planningSeasonPageInfo;
    }
    @ApiOperation(value = "按品类展开")
    @GetMapping("/expandByCategory")
    public List<BasicStructureTreeVo> expandByCategory(@Valid ProductSeasonExpandByCategorySearchDto dto){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("b.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qw.eq("c.del_flag", BaseEntity.DEL_FLAG_NORMAL);
        qw.eq("b.status", BaseGlobal.STOCK_STATUS_CHECKED);
        qw.eq("b.planning_season_id",dto.getPlanningSeasonId());
        qw.eq("c.planning_season_id",dto.getPlanningSeasonId());

        List<String> categoryIds=planningCategoryItemService.selectCategoryIdsByBand(qw);
        if(CollUtil.isEmpty(categoryIds)){
            return null;
        }
        return ccmFeignService.findStructureTreeByCategoryIds(CollUtil.join(categoryIds,","));
    }
    @ApiOperation(value = "查询坑位列表")
    @PostMapping("/findProductCategoryItem")
    public ApiResult findProductCategoryItem(@Valid @RequestBody ProductCategoryItemSearchDto dto){
        if(CollUtil.isEmpty(dto.getCategoryIds())&&StrUtil.isBlank(dto.getPlanningBandId())){
            throw  new OtherException("品类和波段必须有一个不为空");
        }
        QueryWrapper qw = new QueryWrapper();
        // 设计款号
        qw.like(StrUtil.isNotBlank(dto.getSearch()),"design_no",dto.getSearch());
        //产品季
        qw.eq("planning_season_id",dto.getPlanningSeasonId());
        // 品类
        qw.in(CollUtil.isNotEmpty(dto.getCategoryIds()),"category_id",dto.getCategoryIds());
        // 波段企划
        qw.eq(StrUtil.isNotBlank(dto.getPlanningBandId()),"planning_band_id",dto.getPlanningBandId());
        // 设计师
        qw.in(CollUtil.isNotEmpty(dto.getDesignerIds()),"designer_id",dto.getDesignerIds());
        // 任务等级
        qw.in(CollUtil.isNotEmpty(dto.getTaskLevels()),"task_level",dto.getTaskLevels());

        Page<PlanningCategoryItem> objects = PageHelper.startPage(dto);
        planningCategoryItemService.list(qw);
        PageInfo<PlanningCategoryItem> pageInfo = objects.toPageInfo();
        //查询汇总信息
        PlanningBandSummaryInfoVo summaryInfo=new PlanningBandSummaryInfoVo();
        summaryInfo.setPlanRequirementNum(pageInfo.getTotal());
        qw.select("count(id) as count","status");
        qw.groupBy("status");
        List<Map<String, Object>> statusCountList = planningCategoryItemService.listMaps(qw);
        if(ObjectUtil.isNotEmpty(statusCountList)){
            Map<Object, Object> statusCountMap = statusCountList.stream().collect(Collectors.toMap(k -> k.get("status"), v -> v.get("count"), (a, b) -> a));
            summaryInfo.setDesignCompletionNum(MapUtil.getInt(statusCountMap,"2",0));
            summaryInfo.setPlanReceiveNum(MapUtil.getInt(statusCountMap,"1",0));
        }
        //获取参与人信息
        List<PlanningCategoryItem> planningBandList = pageInfo.getList();
        if(CollUtil.isNotEmpty(planningBandList)){
            List<String> userIds=new ArrayList<>(12);
            List<UserInfoVo> userInfoVos=new ArrayList<>(12);
            for (PlanningCategoryItem planningCategoryItem : planningBandList) {
                if(StrUtil.isNotBlank(planningCategoryItem.getDesignerId())){
                    userIds.add(planningCategoryItem.getDesignerId());
                    UserInfoVo userInfoVo = new UserInfoVo();
                    userInfoVo.setId(planningCategoryItem.getDesignerId());
                    userInfoVo.setName(planningCategoryItem.getDesigner());
                    userInfoVos.add(userInfoVo);
                }
            }
            //获取头像
            if(CollUtil.isNotEmpty(userIds)){
                Map<String, String> userAvatar = amcFeignService.getUserAvatar(CollUtil.join(userIds, ","));
                userInfoVos.forEach(item->{
                    item.setAvatar(userAvatar.get(item.getId()));
                });
                summaryInfo.setUserList(userInfoVos);
            }
        }
        Map<String,Object> attr=new HashMap<>(4);
        attr.put("summaryInfo",summaryInfo);
        return ApiResult.success("SUCCESS_OK",pageInfo,attr);
    }

    @ApiOperation(value = "设置任务等级")
    @PostMapping("/setTaskLevel")
    public boolean setTaskLevel(@Valid @RequestBody List<SetTaskLevelDto> dtoList){
        // 查询数据是否存在
        Map<String, SetTaskLevelDto> dtoMap = dtoList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
        List<PlanningCategoryItem> planningCategoryItems = planningCategoryItemService.listByIds(dtoMap.keySet());
        if(dtoList.size()!=planningCategoryItems.size()){
            throw  new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        for (PlanningCategoryItem planningCategoryItem : planningCategoryItems) {
            planningCategoryItem.setTaskLevel(dtoMap.get(planningCategoryItem.getId()).getTaskLevel());
        }
        return planningCategoryItemService.updateBatchById(planningCategoryItems);
    }

    @ApiOperation(value = "分配设计师")
    @PostMapping("/allocationDesign")
    public boolean allocationDesign(@Valid @RequestBody List<AllocationDesignDto> dtoList){
        // 查询数据是否存在
        Map<String, AllocationDesignDto> dtoMap = dtoList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v, (a, b) -> b));
        List<PlanningCategoryItem> planningCategoryItems = planningCategoryItemService.listByIds(dtoMap.keySet());
        if(dtoList.size()!=planningCategoryItems.size()){
            throw  new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        for (PlanningCategoryItem planningCategoryItem : planningCategoryItems) {
            AllocationDesignDto allocationDesignDto = dtoMap.get(planningCategoryItem.getId());
            String newDesigner = allocationDesignDto.getDesigner();
            if(!newDesigner.contains(",")){
                throw new OtherException("设计师名称格式为:名称,代码");
            }
            String newDesignerCode=newDesigner.split(",")[1];
            String oldDesigner=planningCategoryItem.getDesigner();
            //重新设置款号
            String designNo=planningCategoryItem.getDesignNo();
            //如果还没设置设计师 款号= 款号+设计师代码
            if(StrUtil.isBlank(oldDesigner)){
                planningCategoryItem.setDesignNo(designNo+newDesignerCode);
            }else{
                //如果已经设置了设计师 款号=款号+新设计师代码
                String oldDesignCode=oldDesigner.split(",")[1];
                // 获取原始款号
                designNo= StrUtil.sub(designNo,0,designNo.length()-oldDesignCode.length());
                planningCategoryItem.setDesignNo(designNo+newDesignerCode);
            }
            BeanUtil.copyProperties(allocationDesignDto,planningCategoryItem);
        }
        return planningCategoryItemService.updateBatchById(planningCategoryItems);
    }

}
