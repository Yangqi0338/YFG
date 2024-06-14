package com.base.sbc.module.planning.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.excel.ExportPlanningExcel;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningChannelVo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.PlanningSeasonVo;
import com.base.sbc.module.planning.vo.YearSeasonBandVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 类描述：商品企划 相关接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.controller.PlanningController
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 13:47
 */
@RestController
@Api(tags = "商品企划 相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PlanningController extends BaseController {

    @Resource
    private PlanningSeasonService planningSeasonService;
    @Resource
    private PlanningCategoryItemService planningCategoryItemService;

    @Resource
    private PlanningCategoryItemMaterialService planningCategoryItemMaterialService;
    @Resource
    private PlanningChannelService planningChannelService;
    @Autowired
    private BandService bandService;
    @Autowired
    private CcmService ccmService;
    @Autowired
    private CcmFeignService ccmFeignService;
    @Autowired
    private AmcFeignService amcFeignService;
    @Autowired
    private MinioUtils minioUtils;
    private IdGen idGen = new IdGen();
    private final ReentrantLock lock = new ReentrantLock();
    @ApiOperation(value = "保存产品季", notes = "")
    @PostMapping
    public PlanningSeasonVo save(@Valid @RequestBody PlanningSeasonSaveDto dto) {
        // 校验名称重复
        planningSeasonService.checkNameRepeat(dto, getUserCompany());
        // 校验品牌，年份，季节重复
        planningSeasonService.checkBYSRepeat(dto, getUserCompany());
        //保存产品季
        PlanningSeason bean = planningSeasonService.savePlanningSeason(dto);
        return BeanUtil.copyProperties(bean, PlanningSeasonVo.class);
    }

    @ApiOperation(value = "查询产品季-通过季名称查询")
    @GetMapping("/getByName")
    public PlanningSeasonVo getByName(@NotNull(message = "名称不能为空") String name) {
        return planningSeasonService.getByName(name);
    }

    @ApiOperation(value = "查询产品季-分页查询")
    @GetMapping
    public PageInfo query(PlanningSeasonSearchDto dto) {
        PageInfo<PlanningSeasonVo> pageInfoVO = planningSeasonService.queryPlanningSeasonPageInfo(dto, getUserCompany());
        if (pageInfoVO != null) {
            return pageInfoVO;
        }
        return new PageInfo<>();
    }

    @ApiOperation(value = "产品季-查询年份品牌树(新)")
    @GetMapping("/queryYearBrandTree")
    public List<YearSeasonBandVo> queryYearBrandTree(YearSeasonBandVo vo) {
        return planningSeasonService.queryYearBrandTree(vo);
    }

    @ApiOperation(value = "新增渠道")
    @PostMapping("/channel")
    @DuplicationCheck
    public PlanningChannelVo save(@Valid @RequestBody PlanningChannelDto dto) {
        return planningChannelService.saveByDto(dto);
    }

    @ApiOperation(value = "渠道列表-分页查询")
    @GetMapping("/channel")
    public PageInfo<PlanningChannelVo> channelPageInfo(PlanningChannelSearchDto dto) {
        return planningChannelService.channelPageInfo(dto);
    }

    @ApiOperation(value = "删除渠道信息")
    @DeleteMapping("/channel")
    public boolean delChannel(@Valid @NotNull(message = "编号不能为空") String id) {
        if (planningChannelService.checkHasSub(id)) {
            throw new OtherException("存在坑位信息无法删除");
        }
        return planningChannelService.delChannel(id);
    }

    @ApiOperation(value = "坑位列表-左侧树")
    @GetMapping("/categoryTree")
    public List<BasicStructureTreeVo> categoryTree(String planningChannelId) {
        return planningCategoryItemService.categoryTree(planningChannelId);
    }

    @ApiOperation(value = "修改坑位信息")
    @PostMapping("/updateCategoryItem")
    public List<PlanningCategoryItemSaveDto> updateCategoryItem(@RequestBody List<PlanningCategoryItemSaveDto> item) {
        planningCategoryItemService.updateCategoryItem(item);
        return item;
    }

    @ApiOperation(value = "新建坑位(新)")
    @PostMapping("/addSeat")
    public boolean addSeat(@Validated @RequestBody AddSeatDto dto) {
        lock.lock();
        try {
            return planningCategoryItemService.addSeat(dto);
        } finally {
            lock.unlock();
        }

    }

    @ApiOperation(value = "撤回(新)")
    @GetMapping("/seat/revoke")
    public boolean seatRevoke(@Validated IdsDto idsDto) {
        return planningCategoryItemService.revoke(idsDto.getId());
    }

    @ApiOperation(value = "删除坑位信息(新)")
    @GetMapping("/seat/del")
    public boolean seatDel(@Validated IdsDto idsDto) {
        return planningCategoryItemService.del(idsDto.getId());
    }


    @ApiOperation(value = "坑位信息下发(坑位信息下发到产品季总览)")
    @PostMapping("/seatSend")
    public boolean seatSend(@Valid @RequestBody List<PlanningCategoryItemSaveDto> item) {
        return planningCategoryItemService.seatSend(item);

    }

    @ApiOperation(value = "查询坑位列表")
    @PostMapping("/seatList")
    public PageInfo<PlanningSeasonOverviewVo> seatList(@Valid @RequestBody ProductCategoryItemSearchDto dto) {
        if (dto == null) {
            dto = new ProductCategoryItemSearchDto();
        }
        dto.setOrderBy("c.status asc,c.id desc");
        PageInfo<PlanningSeasonOverviewVo> productCategoryItem = planningCategoryItemService.findProductCategoryItem(dto);
        minioUtils.setObjectUrlToList(productCategoryItem.getList(), "stylePic");
        return productCategoryItem;
    }

    @ApiOperation(value = "修改图片")
    @GetMapping("/updateStylePic")
    public boolean updateStylePic(@Valid @NotBlank(message = "id不能为空") String id, String stylePic) {
        return planningCategoryItemService.updateStylePic(id, stylePic);
    }

    @ApiOperation(value = "获取下一个流水(测试)")
    @GetMapping("/nextDesignNo")
    public String nextDesignNo() {
        GetMaxCodeRedis getMaxCode = new GetMaxCodeRedis(ccmService);
        Map<String, String> params = new HashMap<>(12);
        params.put("brand", "1");
        params.put("year", "2021");
        params.put("season", "A");
        params.put("category", "0");
        params.put("designCode", "LD");
        List<String> planningDesignNo1 = getMaxCode.genCode("PLANNING_DESIGN_NO", 10, params);
        return CollUtil.join(planningDesignNo1, ",");
    }

    @ApiOperation(value = "获取最大流水号")
    @PostMapping("/maxDesignNo")
    public String maxDesignNo(@RequestBody GetMaxCodeRedis data) {
        if (ObjectUtil.isEmpty(data.getValueMap())) {
            throw new OtherException("条件不能为空");
        }
        String styleMaxOldDesignNo = planningCategoryItemService.getStyleMaxOldDesignNo(data, getUserCompany());
        String maxDesignNo = planningCategoryItemService.getMaxDesignNo(data, getUserCompany());
        if (StrUtil.isNotBlank(styleMaxOldDesignNo)) {
            if (StrUtil.isBlank(maxDesignNo)) {
                return styleMaxOldDesignNo;
            } else {
                int max = NumberUtil.max(Integer.parseInt(styleMaxOldDesignNo), Integer.parseInt(maxDesignNo));
                return String.valueOf(max);
            }
        }
        return maxDesignNo;

    }


    @ApiOperation(value = "删除产品季")
    @DeleteMapping("/planningSeason")
    public boolean delPlanningSeason(@Valid @NotNull(message = "编号不能为空") String id) {
        if (planningSeasonService.checkPlanningSeasonHasSub(id)) {
            throw new OtherException("存在渠道企划无法删除");
        }
        return planningSeasonService.delPlanningSeason(id);
    }


    @ApiOperation(value = "保存关联的素材库")
    @PostMapping("/savePlanningCategoryItemMaterial")
    public boolean savePlanningCategoryItemMaterial(@RequestBody PlanningCategoryItemMaterialSaveDto dto) {
        return planningCategoryItemMaterialService.savePlanningCategoryItemMaterial(dto);
    }

    @ApiOperation(value = "查询产品季用户列表", notes = "可通过岗位查询")
    @GetMapping("/getTeamUserList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "planningSeasonId", value = "产品季id", paramType = "query", required = true),
            @ApiImplicitParam(name = "post", value = "操作人", paramType = "query"),
    })
    public List<UserCompany> getTeamUserList(@Valid @NotBlank(message = "产品季id不能为空") String planningSeasonId, String post) {
        return amcFeignService.getTeamUserListByPost(planningSeasonId, post);
    }


    @ApiOperation(value = "查询坑位信息的维度数据", notes = "")
    @GetMapping("/querySeatDimension")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "坑位信息id", paramType = "query", required = true),
            @ApiImplicitParam(name = "isSelected", value = "选择标识(非空只返回选择了的维度)", paramType = "query", required = true),
            @ApiImplicitParam(name = "categoryFlag", value = "用于是否查询品类", paramType = "query", required = true),
    })
    public List<FieldManagementVo> querySeatDimension(String id, String isSelected,String categoryFlag) {
        return planningCategoryItemService.querySeatDimension(id, isSelected,categoryFlag);
    }

    @ApiOperation(value = "通过年份获取产品季下拉列表", notes = "")
    @GetMapping("/getByYear")
    public ApiResult getByYear(@Valid @NotBlank(message = "年份不可为空") String year) {
        return selectSuccess(planningSeasonService.getByYear(year));
    }


    @ApiOperation(value = "导出坑位信息Excel模板")
    @GetMapping(value = "/exportPlanningExcel")
    public void exportPlanningExcel(HttpServletResponse response, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String season) {
        //查询字典月份
        List<BasicBaseDict> baseDictList = ccmFeignService.basicDictDependsByTypes(null, "C8_Month", "C8_Quarter", season);
        //查询字典系列
        List<BasicBaseDict> seriesList = ccmFeignService.getDictInfoToList("Series");

        QueryWrapper<Band> qc = new QueryWrapper<>();
        qc.eq("company_code", getUserCompany());
        qc.eq("status", BaseGlobal.STATUS_NORMAL);
        List<Band> bandList = bandService.list(qc);
//        Map<String, List<Band>> monthBandMap = bandList.stream().collect(Collectors.groupingBy(Band::getMonth));

        // 生成文件名称
        String strFileName = "坑位信息导入模板.xls";
        OutputStream objStream = null;
        try {
            objStream = response.getOutputStream();
            response.reset();
            // 设置文件名称
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(strFileName, "UTF-8"));
            // 文档对象
            ExportPlanningExcel excel = new ExportPlanningExcel();
            XSSFWorkbook objWb = excel.createWorkBook(bandList, baseDictList, seriesList);
            objWb.write(objStream);
            objStream.flush();
            objStream.close();
        } catch (Exception e) {
            logger.error("生成坑位信息导入模板异常：", e);
            e.printStackTrace();
        } finally {
            if (objStream != null) {
                try {
                    objStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "批量导入坑位信息", notes = "")
    @PostMapping("/importPlanningExcel")
    public ApiResult importPlanningExcel(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestParam("file") MultipartFile file,
                                         @RequestParam("planningChannelId") String planningChannelId) throws Exception {
        return planningCategoryItemService.importPlanningExcel(file, planningChannelId);
    }

    @ApiOperation(value = "渠道分类下拉框选择")
    @GetMapping("/channelClassifSelection")
    public ApiResult channelClassifSelection(String planningSeasonId) {
        return selectSuccess(planningChannelService.channelClassifSelection(planningSeasonId));
    }
}
