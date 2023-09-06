/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.base.sbc.client.ccm.entity.BasicUnitConfig;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.utils.ImportExcel;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMeasurement;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMeasurementService;
import com.base.sbc.module.common.dto.IdsDto;
import com.base.sbc.module.pack.dto.*;
import com.base.sbc.module.pack.entity.PackSize;
import com.base.sbc.module.pack.entity.PackSizeDetail;
import com.base.sbc.module.pack.excel.PackBomSizeExcel;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pack.service.PackSizeConfigService;
import com.base.sbc.module.pack.service.PackSizeDetailService;
import com.base.sbc.module.pack.service.PackSizeService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackSizeConfigVo;
import com.base.sbc.module.pack.vo.PackSizeVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：资料包-尺寸表 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.web.PackSizeController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-1 10:14:51
 */
@RestController
@Api(tags = "资料包-尺寸表")
@RequestMapping(value = BaseController.SAAS_URL + "/packSize", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PackSizeController extends BaseController{

    @Autowired
    private PackSizeService packSizeService;
    @Autowired
    private PackSizeConfigService packSizeConfigService;
    @Autowired
    private PackInfoStatusService packInfoStatusService;
    @Autowired
    private BasicsdatumMeasurementService measurementService;
    @Autowired
    private PackSizeDetailService packSizeDetailService;
    @Autowired
    private CcmFeignService ccmFeignService;

    @ApiOperation(value = "分页查询")
    @GetMapping
    public PageInfo<PackSizeVo> page(@Valid PackCommonPageSearchDto dto) {
        return packSizeService.pageInfo(dto);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id,多个逗号分开", required = true, dataType = "String", paramType = "query"),
    })
    public boolean del(@Valid @NotBlank(message = "id不能为空") String id) {
        return packSizeService.delByIds(id);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存单个")
    @OperaLog(value = "尺寸表", operationType = OperationType.INSERT_UPDATE, pathSpEL = PackUtils.pathSqEL, parentIdSpEl = "#p0.foreignId", service = PackSizeService.class)
    public PackSizeVo save(@Valid @RequestBody PackSizeDto dto) {
        return packSizeService.saveByDto(dto);
    }

    @PostMapping("/saveBatch")
    @ApiOperation(value = "保存全部")
    public boolean save(@Valid PackCommonSearchDto commonDto, @RequestBody List<PackSizeDto> dtoList) {
        return packSizeService.saveBatchByDto(commonDto, dtoList);
    }



    @ApiOperation(value = "锁定")
    @GetMapping("/lock")
    public boolean lock(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.lockSize(dto.getForeignId(), dto.getPackType());
    }

    @ApiOperation(value = "解锁")
    @GetMapping("/unlock")
    public boolean unlock(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.unlockSize(dto.getForeignId(), dto.getPackType());
    }

    @ApiOperation(value = "提交审批")
    @GetMapping("/startApproval")
    public boolean startReverseApproval(@Valid PackCommonSearchDto dto) {
        return packInfoStatusService.startApprovalForSize(dto.getForeignId(), dto.getPackType());
    }

    @ApiIgnore
    @PostMapping("/approval")
    public boolean approval(@RequestBody AnswerDto dto) {
        return packInfoStatusService.approvalForSize(dto);
    }


    @GetMapping("/sort")
    @ApiOperation(value = "移动排序")
    public boolean sort(@Validated IdsDto dto) {
        return packSizeService.sort(dto.getId(), "sort");
    }

    @GetMapping("/config")
    @ApiOperation(value = "获取尺寸表配置")
    public PackSizeConfigVo getConfig(@Valid PackCommonSearchDto dto) {
        return packSizeConfigService.getConfig(dto.getForeignId(), dto.getPackType());
    }

    @PostMapping("/saveConfig")
    @ApiOperation(value = "保存尺寸表配置")
    public PackSizeConfigVo saveConfig(@RequestBody PackSizeConfigDto dto) {
        return packSizeConfigService.saveConfig(dto);
    }

    @GetMapping("/configList")
    @ApiOperation(value = "尺寸表配置列表分页查询")
    public PageInfo<PackSizeConfigVo> configList(PackSizeConfigSearchDto dto) {
        return packSizeConfigService.pageInfo(dto);
    }

    @GetMapping("/references")
    @ApiOperation(value = "引用")
    public boolean references(PackSizeConfigReferencesDto dto) {
        return packSizeService.references(dto);
    }

    @ApiOperation(value = "导出资料包尺寸表Excel模板")
    @GetMapping(value = "/exportPackBomSizeExcel")
    public void exportPackBomSizeExcel(HttpServletResponse response, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, String foreignId, String packType, String ifWashing) {
        //查询尺寸配置信息
        PackSizeConfigVo packSizeConfigVo = packSizeConfigService.getConfig(foreignId, packType);
        //查询部位信息
        List<BasicsdatumMeasurement> measurementList = measurementService.getAllMeasurement(userCompany);
        //查询单位信息
        List<BasicUnitConfig> basicUnitConfigList = ccmFeignService.getAllUnitConfigList( null);

        // 生成文件名称
        String strFileName = "尺寸表导入模板.xlsx";
        OutputStream objStream = null;
        try {
            objStream = response.getOutputStream();
            response.reset();
            // 设置文件名称
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(strFileName, "UTF-8"));
            // 文档对象
            PackBomSizeExcel excel = new PackBomSizeExcel();
            XSSFWorkbook objWb = excel.createWorkBook(packSizeConfigVo.getProductSizes(), ifWashing, measurementList, basicUnitConfigList);
            objWb.write(objStream);
            objStream.flush();
            objStream.close();
        } catch (Exception e) {
            logger.error("生成尺寸表导入模板异常：", e);
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

    @ApiOperation(value = "批量导入尺寸信息", notes = "")
    @PostMapping("/importPackBomSizeExcel")
    public ApiResult importPackBomSizeExcel(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestParam("file") MultipartFile file,
                                         @RequestParam("foreignId") String foreignId, @RequestParam("packType") String packType,
                                         @RequestParam("ifWashing") String ifWashing) throws Exception {
        //查询尺寸配置信息
        PackSizeConfigVo packSizeConfigVo = packSizeConfigService.getConfig(foreignId, packType);
        //查询部位信息
        List<BasicsdatumMeasurement> measurementList = measurementService.getAllMeasurement(companyCode);
        Map<String, BasicsdatumMeasurement> basicsdatumMeasurementMap = measurementList.stream().collect(Collectors.toMap(BasicsdatumMeasurement::getMeasurement, item -> item, (item1, item2) -> item1));

        List<String> sizeList = StringUtils.convertList(packSizeConfigVo.getProductSizes());

        int criticalFactor = StringUtils.equals(ifWashing, "1") ? 3 : 2;
        IdGen idGen = new IdGen();
        List<PackSize> packSizeList = new ArrayList<>();
        List<PackSizeDetail> packSizeDetailList = new ArrayList<>();
        ImportExcel excel;
        try {
            excel = new ImportExcel(file, 1, 0);
            for (int i = excel.getDataRowNum(); i < excel.getLastDataRowNum() + 1; i++) {
                Row row = excel.getRow(i);
                if (StringUtils.isBlank(excel.getCellValue(row, 0).toString())) {
                    break;
                }
                String id = idGen.nextIdStr();
                PackSize packSize = new PackSize();
                packSize.insertInit();
                packSize.setId(id);
                packSize.setCompanyCode(companyCode);
                packSize.setForeignId(foreignId);
                packSize.setPackType(packType);
                packSize.setRowType("0");
                packSize.setDelFlag("0");

                //尺码明细表
                Map<String, PackSizeDetail> packSizeDetailMap = new HashMap<>();
                //各尺码明细的数量
                Map<String, String> packSizeDetailNumMap = new HashMap<>();
                for (int k = 0; k < excel.getLastCellNum(); k++) {
                    Object val = excel.getCellValue(row, k);
                    if (val != null) {
                        String s = String.valueOf(val);
                        if (k == 0) {
                            // 部位
                            BasicsdatumMeasurement basicsdatumMeasurement = basicsdatumMeasurementMap.get(s);
                            if (basicsdatumMeasurement != null) {
                                packSize.setPartCode(basicsdatumMeasurement.getCode());
                                packSize.setPartName(basicsdatumMeasurement.getMeasurement());
                            }
                        } else if (k == 1) {
                            // 测量方法
                            packSize.setMethod(s);
                        }else if (k == 2) {
                            // 正公差
                            packSize.setPositive(s);
                        }else if (k == 3) {
                            // 负公差
                            packSize.setMinus(s);
                        }else if (k == 4) {
                            // 档差
                            packSize.setCodeError(s);
                        }else if (k == 5) {
                            // 单位
                            packSize.setUnit(s);
                        }else if(k == excel.getLastCellNum() - 1){
                            packSize.setRemark(s);
                        }else {
                            //动态表头部分
                            for (int j = 0; j < sizeList.size(); j++) {
                                String key = sizeList.get(j);
                                PackSizeDetail packSizeDetail = packSizeDetailMap.get(key);
                                if(packSizeDetail == null){
                                    packSizeDetail = new PackSizeDetail();
                                    packSizeDetail.insertInit();
                                    packSizeDetail.setCompanyCode(companyCode);
                                    packSizeDetail.setDelFlag("0");
                                    packSizeDetail.setForeignId(foreignId);
                                    packSizeDetail.setPackType(packType);
                                    packSizeDetail.setPackSizeId(id);
                                    packSizeDetail.setSize(key);
                                }

                                if(k == (j * criticalFactor) + 6){
                                    //样板尺寸
                                    packSizeDetail.setTemplate(s);
                                    packSizeDetailNumMap.put("template" + key, s);
                                }
                                if(k == (j * criticalFactor) + 7){
                                    //成衣尺寸
                                    packSizeDetail.setGarment(s);
                                    packSizeDetailNumMap.put("garment" + key, s);
                                }
                                if(StringUtils.equals(ifWashing, "1") && k == (j * criticalFactor) + 8){
                                    //洗后尺寸
                                    packSizeDetail.setWashing(s);
                                    packSizeDetailNumMap.put("washing" + key, s);
                                }
                                packSizeDetailMap.put(sizeList.get(j), packSizeDetail);
                            }
                        }

                    }
                }

                JSONObject jsonObject = new JSONObject(packSizeDetailNumMap);
                String jsonStr = jsonObject.toString();
                packSize.setStandard(jsonStr);
                packSize.setSize(packSizeConfigVo.getProductSizes());

                packSizeDetailList.addAll(new ArrayList<>(packSizeDetailMap.values()));
                packSizeList.add(packSize);
            }

            Boolean result = packSizeService.saveBatch(packSizeList);
            if(result){
                if(CollUtil.isNotEmpty(packSizeDetailList)){
                    packSizeDetailService.saveBatch(packSizeDetailList);
                }
                return ApiResult.success("导入成功！", 200);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.error("导入失败！", 200);
    }
}































