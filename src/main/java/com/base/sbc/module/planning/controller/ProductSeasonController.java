package com.base.sbc.module.planning.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.vo.SelectOptionsVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.ProductCategoryTreeVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    private CcmFeignService ccmFeignService;
    @Resource
    PlanningCategoryItemService planningCategoryItemService;
    @Resource
    StyleService styleService;


    @ApiOperation(value = "查询产品季-查询所有产品季下拉选择")
    @GetMapping("/getPlanningSeasonOptions")
    public List<SelectOptionsVo> getPlanningSeasonOptions(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String businessType,ProductCategoryTreeVo vo) {
        return planningSeasonService.getPlanningSeasonOptions(userCompany, businessType,vo);
    }



    @ApiOperation(value = "查询坑位列表")
    @GetMapping("/findProductCategoryItem")
    public PageInfo<PlanningSeasonOverviewVo> findProductCategoryItem(@Valid ProductCategoryItemSearchDto dto) {
        if (StrUtil.isBlank(dto.getStatus())) {
            dto.setStatus("1,2");
        }
        dto.setOrderBy("c.status asc ,c.send_date desc ");
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

    @ApiOperation(value = "设置系列")
    @PostMapping("/setSeries")
    public boolean setSeries(@Valid @RequestBody List<SetSeriesDto> dtoList){
        // 查询数据是否存在
        return planningCategoryItemService.setSeries(dtoList);
    }

    /**
     * 获取企业和产品季的树形结构
     */
    @GetMapping("/getTree")
    public ApiResult getTree(){
        List<AdTree> list = planningSeasonService.getTree();
        return selectSuccess(list);
    }


    @ApiOperation(value = "坑位信息下发(产品季总览-下发到款式设计)")
    @PostMapping("/send")
    public boolean send(@RequestBody List<SeatSendDto> categoryItemList){
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
        return styleService.getBandChart(month);
    }

    @ApiOperation(value = "产品季总览-品类汇总统计图表")
    @GetMapping("/getCategoryChart")
    public List getCategoryChart(String category) {
        return styleService.getCategoryChart(category);
    }

    @ApiOperation(value = "产品季总览-设计数据总览")
    @GetMapping("/getDesignDataOverview")
    public Map getDesignDataOverview(String time) {
        return styleService.getDesignDataOverview(time);
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

    @ApiOperation(value = "产品季总览-获取坑位数据所有工艺员")
    @GetMapping("/getPatternTechnician")
    public List<SampleUserVo> getPatternTechnician(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany) {
        return planningCategoryItemService.getPatternTechnician(userCompany);
    }

    @ApiOperation(value = "获取产品季品类树")
    @GetMapping("/getProductCategoryTree")
    public List<ProductCategoryTreeVo> getProductCategoryTree(ProductCategoryTreeVo vo) {
        return styleService.getProductCategoryTree(vo);
    }

    @ApiOperation(value = "获取产品季品类树(新)")
    @GetMapping("/getProductCategoryTreeNew")
    public List<ProductCategoryTreeVo> getProductCategoryTreeNew(ProductCategoryTreeVo vo) {
        return styleService.getProductCategoryTreeNew(vo);
    }

    @ApiOperation(value = "获取产品季全品类")
    @GetMapping("/getProductAllCategory")
    public List getProductAllCategory(ProductCategoryTreeVo vo) {
        return styleService.getProductAllCategory(vo);
    }

}
