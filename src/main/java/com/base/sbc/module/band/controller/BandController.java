package com.base.sbc.module.band.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.band.dto.BandSaveDto;
import com.base.sbc.module.band.dto.BandStartStopDto;
import com.base.sbc.module.band.dto.QueryBandDto;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.band.service.BandService;
import com.base.sbc.module.band.vo.BandQueryReturnVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/18 17:54:12
 */
@RestController
@Api(tags = "波段管理相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/band", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BandController extends BaseController {
    @Resource
    private BandService bandService;

    /**
     * 新增波段
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "新增波段", notes = "id为空")
    public boolean add(@Valid @RequestBody BandSaveDto bandSaveDto) {
        Band band =new Band();
        QueryWrapper<Band> qc = new QueryWrapper<>();
        qc.eq("code", bandSaveDto.getCode());
        List<Band> bandList = bandService.list(qc);
        if(!CollectionUtils.isEmpty(bandList)){
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }
        BeanUtils.copyProperties(bandSaveDto,band);
        return bandService.save(band);
    }

    /**
     * 条件查询列表
     */
    @GetMapping("/listQuery")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "查询字段", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序", required = false, dataType = "String", paramType = "query")
    })
    public ApiResult listQuery(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, QueryBandDto queryBandDto) {
        QueryWrapper<Band> qc = new QueryWrapper<>();
        qc.eq("company_code", userCompany);
        qc.like(StringUtils.isNotBlank(queryBandDto.getBandName()),"band_name",queryBandDto.getBandName());
        qc.like(StringUtils.isNotBlank(queryBandDto.getCode()),"code",queryBandDto.getCode());
        qc.eq(StringUtils.isNotBlank(queryBandDto.getMonth()),"month",queryBandDto.getMonth());
        qc.eq(StringUtils.isNotBlank(queryBandDto.getSeason()),"season",queryBandDto.getSeason());
        if (!StringUtils.isEmpty(queryBandDto.getOrderBy())){
            qc.orderByAsc(queryBandDto.getOrderBy());
        }else {
            qc.orderByDesc("create_date");
        }
        if (queryBandDto.getPageNum() != 0 && queryBandDto.getPageSize() != 0) {
            com.github.pagehelper.Page<BandQueryReturnVo> basicLabelUseScopePage = PageHelper.startPage(queryBandDto.getPageNum(), queryBandDto.getPageSize());
            bandService.list(qc);
            PageInfo<BandQueryReturnVo> pages = basicLabelUseScopePage.toPageInfo();
            return ApiResult.success("success", pages);
        }
        return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value());

    }

    /**
     * 根据id查询
     */
    @GetMapping("/getById")
    @ApiImplicitParams({  @ApiImplicitParam(name = "id", value = "波段id", required = true, dataType = "String"), })
    @ApiOperation(value = "id查询波段", notes = "id必填")
    public ApiResult getById(String id) {
        Band band=  bandService.getById(id);
        BandQueryReturnVo bandQueryReturnVo=new BandQueryReturnVo();
        BeanUtils.copyProperties(band,bandQueryReturnVo);
        return selectSuccess(bandQueryReturnVo);
    }

    @ApiOperation(value = "/导入")
    @PostMapping("/bandImportExcel")
    public ApiResult bandImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
        return bandService.bandImportExcel(file);
    }

    @ApiOperation(value = "/导出")
    @GetMapping("/bandDeriveExcel")
    public void bandDeriveExcel(HttpServletResponse response) throws Exception {
        bandService.bandDeriveExcel(response);
    }
    /**
     * 批量删除
     */
    @Transactional(rollbackFor = {Exception.class})
    @DeleteMapping("delByIds")
    @ApiImplicitParams({  @ApiImplicitParam(name = "ids", value = "删除波段", required = true, dataType = "String[]"), })
    @ApiOperation(value = "删除波段", notes = "ids必填")
    public ApiResult delByIds(String[] ids) {
        return deleteSuccess(bandService.removeBatchByIds(Arrays.asList(ids)));
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "修改波段", notes = "必填")
    public ApiResult update(@Valid @RequestBody BandSaveDto bandSaveDto) {
        if (StringUtils.isEmpty(bandSaveDto.getId())){
            throw new OtherException("Id不能为空");
        }
        Band band=  bandService.getById(bandSaveDto.getId());
        if (band==null){
            throw new OtherException("查无数据");
        }
        BeanUtils.copyProperties(bandSaveDto,band);
        return updateSuccess(bandService.updateById(band));
    }
    /**
     * 启动 停止
     */
    @ApiOperation(value = "批量启用/停用波段", notes = "ids:波段ids(多个用逗号拼接), status:0启用1停用")
    @PostMapping("bandStartStop")
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult bandStartStop(@Valid @RequestBody BandStartStopDto bandStartStopDto) {
        UpdateWrapper<Band> updateWrapper =new UpdateWrapper<>();
        updateWrapper.in("id",Arrays.asList(bandStartStopDto.getIds()));
        updateWrapper.set("status",bandStartStopDto.getStatus());
        return deleteSuccess(bandService.update(null,updateWrapper));
    }

    @GetMapping("/queryBand")
    public ApiResult queryBand(BandSaveDto dto){
        QueryWrapper<Band> qc = new QueryWrapper<>();
        qc.eq("company_code", getUserCompany());
        qc.eq(StrUtil.isNotBlank(dto.getStatus()), "status", BaseGlobal.STATUS_NORMAL);
//        qc.eq(StrUtil.isNotBlank(dto.getParticularYear()), "particular_year", dto.getParticularYear());
        qc.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        qc.eq(StrUtil.isNotBlank(dto.getCode()), "code", dto.getCode());
        qc.eq(StrUtil.isNotBlank(dto.getMonth()), "month", dto.getMonth());
        List<Band> bandList = bandService.list(qc);
        return selectSuccess(bandList);
    }

    @GetMapping("/getNameByCode")
    @ApiOperation(value = "通过编码获取名称", notes = "")
    public String getNameByCode(@RequestParam(value = "name", required = true) @Valid @NotBlank(message = "波段名称不能为空") String name) {
        return bandService.getNameByCode(name);
    }
}
