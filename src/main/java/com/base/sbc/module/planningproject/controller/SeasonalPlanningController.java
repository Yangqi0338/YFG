package com.base.sbc.module.planningproject.controller;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.service.SeasonalPlanningService;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningQueryDto;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningVo;
import com.github.pagehelper.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author 卞康
 * @date 2024-01-18 17:08:27
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "季节企划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/seasonalPlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class SeasonalPlanningController extends BaseController {
    private final SeasonalPlanningService seasonalPlanningService;

    /**
     * 导入季节企划
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    @DuplicationCheck(type = 1,time = 20)
    public ApiResult importExcel(MultipartFile file,  SeasonalPlanningSaveDto seasonalPlanningSaveDto) throws Exception {
        seasonalPlanningSaveDto.setCompanyCode(this.getUserCompany());
        return seasonalPlanningService.importSeasonalPlanningExcel(file,seasonalPlanningSaveDto);
    }

    @ApiOperation(value = "导出季节企划")
    @GetMapping(value = "/exportExcel")
    public void exportExcel(HttpServletResponse response, String id) {
        String fileName = "季节企划模板";
        SeasonalPlanningDetails seasonalPlanningDetails = new SeasonalPlanningDetails();
        seasonalPlanningDetails.setId(id);
        Map<Integer, Map<Integer, String>> dataMap = seasonalPlanningService.getSeasonalPlanningDetails(seasonalPlanningDetails);
        // 创建工作薄
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<CellRangeAddress> mergedRegions = new ArrayList<>();

        // 创建居中对齐的样式
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 填充数据
        int row1 = 6;
        String row1Name = "";
        int row2 = 6;
        String row2Name = "";
        int row3 = 6;
        String row3Name = "";
        for (Integer rowKey : dataMap.keySet()) {
            Row rowData = sheet.createRow(rowKey-1);
            Map<Integer, String> cellData = dataMap.get(rowKey);
            int cellLine = 0;
            String cellVal = "";
            Integer startIndex = 0;
            Integer maxSize = cellData.size();
            for (Integer cellKey : cellData.keySet()) {
                String value = cellData.get(cellKey);
                Cell cell = rowData.createCell(cellLine);
                cell.setCellValue(value);
                cell.setCellStyle(style);
                // 第一行
                if (1 == rowKey) {
                    if ("".equals(cellVal)) {
                        cellVal = value;
                    } else {
                        if (!cellVal.equals(value)) {
                            mergedRegions.add(new CellRangeAddress(0, 0, startIndex, cellKey - 2));
                            startIndex = cellKey - 1;
                            cellVal = value;
                        } else if (maxSize.equals(cellKey)) {
                            mergedRegions.add(new CellRangeAddress(0, 0, startIndex, cellKey-1));
                        }
                    }
                }
                // 2，3，4 行
                if (2 == rowKey) {
                    if ("".equals(cellVal)) {
                        cellVal = value;
                    } else {
                        if (!cellVal.equals(value)) {
                            mergedRegions.add(new CellRangeAddress(1, 1, startIndex, cellKey - 2));
                            mergedRegions.add(new CellRangeAddress(2, 2, startIndex, cellKey - 2));
                            mergedRegions.add(new CellRangeAddress(3, 3, startIndex, cellKey - 2));
                            startIndex = cellKey - 1;
                            cellVal = value;
                        } else if (maxSize.equals(cellKey)) {
                            mergedRegions.add(new CellRangeAddress(1, 1, startIndex, cellKey - 1));
                            mergedRegions.add(new CellRangeAddress(2, 2, startIndex, cellKey - 1));
                            mergedRegions.add(new CellRangeAddress(3, 3, startIndex, cellKey - 1));
                        }

                    }
                }
                if (5 == rowKey) {
                    mergedRegions.add(new CellRangeAddress(4, 4, 0, 2));
                }

                if (rowKey > 5) {
                    if (cellKey == 1) {
                        String name = cellData.get(cellKey);
                        if ("".equals(row1Name)) {
                            row1Name = name;
                        } else {
                            if (!row1Name.equals(name)) {
                                if ((row1 -1) != (rowKey - 2)) {
                                    mergedRegions.add(new CellRangeAddress(row1 - 1, rowKey - 2, 0, 0));
                                }
                                row1 = rowKey;
                                row1Name = name;
                            }
                        }
                    }
                    if (cellKey == 2) {
                        String name = cellData.get(cellKey);
                        if ("".equals(row2Name)) {
                            row2Name = name;
                        } else {
                            if (!row2Name.equals(name)) {
                                if ((row2 - 1) != (rowKey - 2)) {
                                    mergedRegions.add(new CellRangeAddress(row2 - 1, rowKey - 2, 1, 1));
                                }
                                row2 = rowKey;
                                row2Name = name;
                            }
                        }
                    }
                    if (cellKey == 3) {
                        String name = cellData.get(cellKey);
                        if ("".equals(row3Name)) {
                            row3Name = name;
                        } else {
                            if (!row3Name.equals(name)) {
                                if ((row3 - 1) != (rowKey - 2)) {
                                    mergedRegions.add(new CellRangeAddress(row3 - 1, rowKey - 2, 2, 2));
                                }
                                row3 = rowKey;
                                row3Name = name;
                            }
                        }
                    }
                }
                cellLine++;
            }
        }

        // 处理单元格合并
            // 记录需要合并的单元格范围
           /* mergedRegions.add(new CellRangeAddress(0, 0, 0, 2));
            mergedRegions.add(new CellRangeAddress(0, 0, 3, 12));
            mergedRegions.add(new CellRangeAddress(0, 0, 13, 22));
            mergedRegions.add(new CellRangeAddress(0, 0, 23, 32));
        mergedRegions.add(new CellRangeAddress(1, 1, 0, 2));
        mergedRegions.add(new CellRangeAddress(2, 2, 0, 2));
        mergedRegions.add(new CellRangeAddress(3, 3, 0, 2));
        mergedRegions.add(new CellRangeAddress(4, 4, 0, 2));
*/
        // 应用合并单元格
        for (CellRangeAddress mergedRegion : mergedRegions) {
            sheet.addMergedRegion(mergedRegion);
        }

        try (OutputStream out = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            response.setHeader("Content-Length", String.valueOf(baos.size()));
            out.write(baos.toByteArray());
        } catch (Exception e) {
            try {
                throw new Exception(e.getMessage());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    /**
     * 季节企划详情
     */
    @ApiOperation(value = "季节企划详情")
    @PostMapping("/getSeasonalPlanningDetails")
    @DuplicationCheck(type = 1,time = 20)
    public ApiResult getSeasonalPlanningDetails(@RequestBody SeasonalPlanningDetails seasonalPlanningDetails) throws Exception {
        ApiResult apiResult = new ApiResult<>();
        apiResult.setData(seasonalPlanningService.getSeasonalPlanningDetails(seasonalPlanningDetails));
        apiResult.setSuccess(true);
        return apiResult;
    }

    /**
     * 根据条件查询
     */
    @ApiOperation(value = "根据条件查询")
    @GetMapping("/queryList")
    public ApiResult queryList(SeasonalPlanningQueryDto seasonalPlanningQueryDto){
        List<SeasonalPlanningVo> list = seasonalPlanningService.queryList(seasonalPlanningQueryDto);
        return selectSuccess(list);
    }

    /**
     * 根据id查询详情
     */
    @ApiOperation(value = "根据id查询详情")
    @GetMapping("/getDetailById")
    public ApiResult getDetailById(@RequestParam String id){
        SeasonalPlanningVo seasonalPlanningVo = seasonalPlanningService.getDetailById(id);
        return selectSuccess(seasonalPlanningVo);
    }

    /**
     * 启用停用
     */
    @ApiOperation(value = "启用停用")
    @PostMapping("/updateStatus")
    public ApiResult updateStatus(@RequestBody BaseDto baseDto){
        seasonalPlanningService.updateStatus(baseDto);
        return updateSuccess("更新成功");
    }

    /**
     * 删除季节企划
     */
    @ApiOperation(value = "删除季节企划")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(RemoveDto removeDto){
        seasonalPlanningService.delFlag(removeDto);
        return deleteSuccess("删除成功");
    }
}
