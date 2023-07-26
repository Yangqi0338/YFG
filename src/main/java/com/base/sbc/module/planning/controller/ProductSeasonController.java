package com.base.sbc.module.planning.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningBandService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.ProductCategoryTreeVo;
import com.base.sbc.module.planning.vo.YearSeasonVo;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    PlanningCategoryItemService planningCategoryItemService;
    @Resource
    SampleDesignService sampleDesignService;

    @ApiOperation(value = "查询产品季-分页查询")
    @GetMapping
    public PageInfo query(PlanningSeasonSearchDto dto) {
        return planningSeasonService.queryByPage(dto, getUserCompany());
    }

    @ApiOperation(value = "查询产品季-查询所有产品季下拉选择")
    @GetMapping("/getPlanningSeasonOptions")
    public List<SelectOptionsVo> getPlanningSeasonOptions(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        return planningSeasonService.getPlanningSeasonOptions(userCompany);
    }

    @ApiOperation(value = "查询年份季节")
    @GetMapping("/queryYs")
    public List<YearSeasonVo> queryYs() {
        List<YearSeasonVo> result = new ArrayList<>(16);
        List<PlanningSeason> planningSeasons = planningSeasonService.queryYs(getUserCompany());
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Year,C8_Quarter");
        if (CollUtil.isNotEmpty(planningSeasons)) {
            for (PlanningSeason planningSeason : planningSeasons) {
                YearSeasonVo yearSeasonVo = BeanUtil.copyProperties(planningSeason, YearSeasonVo.class);
                yearSeasonVo.setSeasonDesc(Optional.ofNullable(dictInfoToMap.get("C8_Quarter"))
                        .map(item->item.get(planningSeason.getSeason()))
                        .orElse(""));
                yearSeasonVo.setYearDesc(Optional.ofNullable(dictInfoToMap.get("C8_Year"))
                        .map(item->item.get(planningSeason.getYear()))
                        .orElse(""));
                result.add(yearSeasonVo);
            }
        }
        return result;
    }

    @ApiOperation(value = "按波段展开-分页查询")
    @GetMapping("/expandByBand")
    public PageInfo expandByBand(@Valid  ProductSeasonExpandByBandSearchDto dto){
        return planningBandService.expandByBand(dto,getUserCompany());
    }


    @ApiOperation(value = "按品类展开")
    @GetMapping("/expandByCategory")
    public List<BasicStructureTreeVo> expandByCategory(@Valid ProductSeasonExpandByCategorySearchDto dto){
        return planningCategoryItemService.expandByCategory(dto);
    }


    @ApiOperation(value = "查询坑位列表")
    @PostMapping("/findProductCategoryItem")
    public PageInfo<PlanningSeasonOverviewVo> findProductCategoryItem(@Valid @RequestBody ProductCategoryItemSearchDto dto) {
        dto.setStatus(BasicNumber.ONE.getNumber());
        dto.setOrderBy("c.status asc ,c.id desc ");
        return planningCategoryItemService.findProductCategoryItem(dto);
    }


    @ApiOperation(value = "设置任务等级")
    @PostMapping("/setTaskLevel")
    public boolean setTaskLevel(@Valid @RequestBody List<SetTaskLevelDto> dtoList){
        // 查询数据是否存在
        return planningCategoryItemService.setTaskLevel(dtoList);
    }


    @ApiOperation(value = "分配设计师")
    @PostMapping("/allocationDesign")
    public boolean allocationDesign(@Valid @RequestBody List<AllocationDesignDto> dtoList){
        return planningCategoryItemService.allocationDesign(dtoList);

    }

    /**
     * 获取企业和产品季的树形结构
     */
    @GetMapping("/getTree")
    public ApiResult getTree(){
        List<AdTree> list = planningSeasonService.getTree();
        return selectSuccess(list);
    }


    @ApiOperation(value = "坑位信息下发(产品季总览-下发到样衣设计)")
    @PostMapping("/send")
    public boolean send(@RequestBody List<PlanningCategoryItem> categoryItemList){
        if(CollUtil.isEmpty(categoryItemList)){
            throw  new OtherException("数据为空");
        }
         // 校验
        for (PlanningCategoryItem planningCategoryItem : categoryItemList) {
            if(StrUtil.hasBlank(planningCategoryItem.getDesigner(),planningCategoryItem.getDesignerId())){
                throw new OtherException("未分配设计师");
            }
            if (StrUtil.isBlank(planningCategoryItem.getTaskLevel())) {
                throw new OtherException("请设置任务等级");
            }
        }
        return planningCategoryItemService.send(categoryItemList);

    }

    @ApiOperation(value = "产品季总览-波段汇总统计图表")
    @GetMapping("/getBandChart")
    public List getBandChart(String month) {
        return sampleDesignService.getBandChart(month);
    }

    @ApiOperation(value = "产品季总览-品类汇总统计图表")
    @GetMapping("/getCategoryChart")
    public List getCategoryChart(String category) {
        return sampleDesignService.getCategoryChart(category);
    }

    @ApiOperation(value = "产品季总览-设计数据总览")
    @GetMapping("/getDesignDataOverview")
    public Map getDesignDataOverview(String time) {
        return sampleDesignService.getDesignDataOverview(time);
    }

    @ApiOperation(value = "产品季总览-修改坑位信息(局部跟新)")
    @PutMapping("/updateItem")
    public boolean updateItem(@RequestBody PlanningCategoryItem item) {
        return planningCategoryItemService.updateById(item);
    }

    @ApiOperation(value = "产品季总览-批量修改坑位信息")
    @PutMapping("/updateItemBatch")
    public boolean updateItemBatch(@Valid @RequestBody PlanningCategoryItemBatchUpdateDto dto) {
        return planningCategoryItemService.updateItemBatch(dto);
    }

    @ApiOperation(value = "产品季总览-获取坑位数据所有设计师")
    @GetMapping("/getAllDesigner")
    public List<SampleUserVo> getAllDesigner(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        return planningCategoryItemService.getAllDesigner(userCompany);
    }

    @ApiOperation(value = "获取产品季品类树")
    @GetMapping("/getProductCategoryTree")
    public List<ProductCategoryTreeVo> getProductCategoryTree(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        return planningSeasonService.getProductCategoryTree(userCompany);
    }




}
