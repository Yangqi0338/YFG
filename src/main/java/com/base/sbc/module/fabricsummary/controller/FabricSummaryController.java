package com.base.sbc.module.fabricsummary.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.ExcelExportTitleStyle;
import com.base.sbc.module.fabricsummary.entity.FabricSummaryPrintLog;
import com.base.sbc.module.fabricsummary.service.FabricSummaryService;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.sample.dto.FabricSummaryStyleMaterialDto;
import com.base.sbc.module.sample.dto.FabricSummaryV2Dto;
import com.base.sbc.module.sample.dto.PrintFabricSummaryLogDto;
import com.base.sbc.module.sample.vo.FabricStyleGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryGroupVo;
import com.base.sbc.module.sample.vo.FabricSummaryInfoExcel;
import com.base.sbc.module.sample.vo.FabricSummaryInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


@RestController
@Api(tags = "面料详单")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricSummary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricSummaryController {

    @Autowired
    private FabricSummaryService fabricSummaryService;
    @Autowired
    private PackBomService packBomService;

    @ApiOperation(value = "/面料汇总组创建/修改")
    @PostMapping("/fabricSummaryGroupSaveOrUpdate")
    public boolean  fabricSummaryGroupSaveOrUpdate(@RequestBody FabricStyleGroupVo fabricStyleGroupVo) {
        return fabricSummaryService.fabricSummaryGroupSaveOrUpdate(fabricStyleGroupVo);
    }

    @ApiOperation(value = "/面料汇总组删除")
    @DeleteMapping("/fabricSummaryGroup")
    public boolean  deleteFabricSummaryGroup( FabricStyleGroupVo fabricStyleGroupVo) {
        return fabricSummaryService.deleteFabricSummaryGroup(fabricStyleGroupVo);
    }

    @ApiOperation(value = "/面料汇总组列表")
    @GetMapping("/fabricSummaryGroup")
    public PageInfo<FabricSummaryGroupVo> fabricSummaryGroup(FabricSummaryStyleMaterialDto dto) {
        return fabricSummaryService.fabricSummaryGroup(dto);
    }

    @GetMapping("/selectFabricSummaryStyle")
    @ApiOperation(value = "选择面料款式列表")
    public PageInfo<FabricSummaryInfoVo> selectFabricSummaryStyle(FabricSummaryV2Dto dto) {
        return fabricSummaryService.selectFabricSummaryStyle(dto);
    }

    @PostMapping("/saveFabricSummary")
    @ApiOperation(value = "添加物料款式")
    public boolean saveFabricSummary(@RequestBody List<FabricSummaryInfoVo> dto) {
        return fabricSummaryService.saveFabricSummary(dto);
    }

    @PostMapping("/fabricSummarySync")
    @ApiOperation(value = "面料信息同步更新")
    public boolean fabricSummarySync(@RequestBody List<FabricSummaryV2Dto> dto) {
        return fabricSummaryService.fabricSummarySync(dto);
    }

    @ApiOperation(value = "/面料详单Excel导出")
        @GetMapping("/fabricSummaryExcel")
    @DuplicationCheck(type = 1,message = "服务已存在导出，请稍后...")
    public void fabricSummaryExcel(HttpServletResponse response , FabricSummaryV2Dto dto) throws Exception {
        packBomService.fabricSummaryExcelExport(response,dto);
    }

    @GetMapping("/printFabricSummaryLog")
    @ApiOperation(value = "面料汇总打印日志")
    public PageInfo<FabricSummaryPrintLog> printFabricSummaryLog(PrintFabricSummaryLogDto dto) {
        return  fabricSummaryService.printFabricSummaryLog(dto);
    }

    public static void main(String[] args) throws IOException {
        FabricSummaryInfoExcel fabricSummaryInfoExcel = new FabricSummaryInfoExcel();
        List<FabricSummaryInfoExcel> list = JSON.parseArray("", FabricSummaryInfoExcel.class);
        list.forEach(item ->{
            final String stylePic = item.getStylePic();
            final String imageUrl = item.getImageUrl();
            item.setStylePic1(HttpUtil.downloadBytes(stylePic));
            item.setImageUrl1(HttpUtil.downloadBytes(imageUrl));
        });

        ExportParams exportParams = new ExportParams("款式列表", "款式列表", ExcelType.HSSF);
        exportParams.setStyle(ExcelExportTitleStyle.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, FabricSummaryInfoExcel.class, list);
        PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), 1, 0, 8);
        // 指定写出的文件
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\29117\\Desktop\\test.xlsx");
        workbook.write(outputStream);
        outputStream.close();

    }


}
