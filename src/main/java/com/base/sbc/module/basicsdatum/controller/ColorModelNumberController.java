package com.base.sbc.module.basicsdatum.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.annotation.DataIsolation;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberDto;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberExcelDto;
import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.basicsdatum.service.ColorModelNumberService;
import com.base.sbc.module.basicsdatum.vo.ColorModelNumberBaseSelectVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/6/26 10:23
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "基础资料-色号和色型")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/colorModelNumber", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ColorModelNumberController extends BaseController {

    private final ColorModelNumberService colorModelNumberService;

    /**
     * 查询列表
     *
     * @param colorModelNumberDto 请求参数
     * @return 列表
     */
    @GetMapping("/queryList")
    @ApiOperation(value = "查询列表")
    public ApiResult queryList(ColorModelNumberDto colorModelNumberDto) {
        BaseQueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyLike("file_name",colorModelNumberDto.getFileName());
        queryWrapper.notEmptyLike("name",colorModelNumberDto.getName());
        queryWrapper.notEmptyLike("code",colorModelNumberDto.getCode());
        queryWrapper.notEmptyLike("status",colorModelNumberDto.getStatus());
        queryWrapper.notEmptyLike("mat2nd_category_name",colorModelNumberDto.getMat2ndCategoryName());
        queryWrapper.between("create_date",colorModelNumberDto.getCreateDate());
        PageHelper.startPage(colorModelNumberDto);
        List<ColorModelNumber> list = colorModelNumberService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }

    /**
     * 单个新增或者修改
     */
    @PostMapping("/save")
    @ApiOperation(value = "单个新增或者修改")
    public ApiResult save(@RequestBody ColorModelNumber colorModelNumber) {
        return updateSuccess(colorModelNumberService.saveColorModelNumber(colorModelNumber));
    }


    /**
     * 批量修改
     */
//    @PutMapping("/updateList")
//    public ApiResult updateList(List<ColorModelNumber> colorModelNumberList) {
//        colorModelNumberService.updateBatchById(colorModelNumberList);
//        return updateSuccess("操作成功");
//    }

    /**
     * 启用或者停用
     */
    @PutMapping("/startStop")
    @ApiOperation(value = "启用或者停用")
    public ApiResult startStop(@RequestBody ColorModelNumberDto colorModelNumberDto) {
        UpdateWrapper<ColorModelNumber> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", colorModelNumberDto.getStatus());
        updateWrapper.in("id", Arrays.asList(colorModelNumberDto.getIds().split(",")));
        colorModelNumberService.update(updateWrapper);
        return updateSuccess("操作成功");
    }

    /**
     * 根据Ids删除
     */
    @PutMapping("/detByIds")
    @ApiOperation(value = "根据数组删除")
    public ApiResult detByIds(String[] ids) {
        return deleteSuccess(colorModelNumberService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导入
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        Boolean b = colorModelNumberService.importExcel(file);
        return insertSuccess(b);
    }


    /**
     * 导出
     */
    @ApiOperation(value = "导出Excel")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response,ColorModelNumberDto colorModelNumberDto) throws Exception {
        QueryWrapper<ColorModelNumber> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("file_name",colorModelNumberDto.getFileName());
        List<ColorModelNumberExcelDto> list = BeanUtil.copyToList(colorModelNumberService.list(queryWrapper), ColorModelNumberExcelDto.class);
        ExcelUtils.exportExcel(list,  ColorModelNumberExcelDto.class, "色号和色型.xlsx",new ExportParams() ,response);
    }

    /**
     * 查询色号和型号拉下框组件基本信息
     *
     * @param distCode
     * @param fileName
     * @return
     */
    @ApiOperation(value = "查询色号和型号拉下框组件基本信息")
    @GetMapping("/getByDistCode")
    public ApiResult getByDistCode(@Valid @NotBlank(message = "依赖编码不可为空") String distCode, @NotBlank(message = "类型不可为空") String fileName) {
        return selectSuccess(colorModelNumberService.getByDistCode(distCode, fileName, super.getUserCompany()));
    }
}
