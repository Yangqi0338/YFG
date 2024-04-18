package com.base.sbc.module.moreLanguage.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.moreLanguage.dto.*;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.EXCESS_STATUS_IMPORT;
import static com.base.sbc.module.common.convert.ConvertContext.MORE_LANGUAGE_CV;

/**
 * @author 孔祥基
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与多语言审核相关的所有接口信息", tags = {"多语言审核接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/moreLanguageStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MoreLanguageStatusController extends BaseController {

    private final StyleCountryStatusService styleCountryStatusService;

    private final RedisUtils redisUtils;


    /**
     * 导入吊牌款号
     */
    @PostMapping("/importExcel")
    @ApiOperation(value = "导入吊牌款号", notes = "导入吊牌款号")
    @DuplicationCheck(type = 1, time = 999)
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) {
        MoreLanguageStatusExcelResultWarpDTO moreLanguageStatusExcelResultWarpDTO = new MoreLanguageStatusExcelResultWarpDTO();
        List<MoreLanguageStatusExcelResultDTO> result = moreLanguageStatusExcelResultWarpDTO.getResult();
        try {
            // 暂且使用匿名内部类, 如果要处理的字段多了,需要单独拿出一个listener
            AtomicInteger num = new AtomicInteger(1);
            // 获取总数和循环次数
            Integer count = MoreLanguageProperties.calculateImportCount();
            int size = (int) Math.ceil((double) MoreLanguageProperties.styleCountryStatusImportMaxSize / count);
            EasyExcel.read(file.getInputStream(), MoreLanguageStatusExcelDTO.class,
                    // 分页读取, 可以添加多线程 TODO
                    new PageReadListener<MoreLanguageStatusExcelDTO>(dataList-> {
                        // 若数量超过,
                        if (num.get() > count) {
                            throw new RightException(MoreLanguageProperties.getMsg(EXCESS_STATUS_IMPORT, count * size));
                        }
                        List<MoreLanguageStatusExcelResultDTO> resultDTOList = styleCountryStatusService.importExcel(dataList);
                        // 根据导入数据的顺序排序处理后的数据
                        if (CollUtil.isNotEmpty(resultDTOList)) {
                            result.addAll(dataList.stream().map(excelDto->
                                    resultDTOList.stream().filter(it-> it.getBulkStyleNo().equals(excelDto.getBulkStyleNo()))
                                            .findFirst().orElse(MORE_LANGUAGE_CV.copy2ResultDTO(excelDto.getBulkStyleNo()))
                            ).collect(Collectors.toList()));
                        }else {
                            // 若没有返回结果,就是都成功,直接封装款号
                            result.addAll(MORE_LANGUAGE_CV.copyList2ResultDTO(
                                    dataList.stream().map(MoreLanguageStatusExcelDTO::getBulkStyleNo).collect(Collectors.toList())
                            ));
                        }
                        num.incrementAndGet();
                    }, size)).sheet().doRead();
            saveFailData(moreLanguageStatusExcelResultWarpDTO);
            return ApiResult.success("导入成功", moreLanguageStatusExcelResultWarpDTO);
        }catch (RightException e){
            saveFailData(moreLanguageStatusExcelResultWarpDTO);
            return ApiResult.success(e.getMessage(), moreLanguageStatusExcelResultWarpDTO);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.error(e.getMessage(), 0);
        }finally {
            StyleCountryStatusService.countryList.remove();
        }
    }

    /**
     * 保存失败的结果到 Redis
     *
     * @param warpDTO 返回的导入结果集合和唯一标识
     */
    private void saveFailData(MoreLanguageStatusExcelResultWarpDTO warpDTO) {
        // 保存失败的结果到 Redis
        // 存入redis, 方便接口查询
        List<MoreLanguageStatusExcelResultDTO> result = warpDTO.getResult();
        if (ObjectUtil.isNotEmpty(result)) {
            List<MoreLanguageStatusExcelResultDTO> failMoreLanguageStatusExcelResultDTO =
                    result.stream().filter(item -> item.getStatus().equals(2)).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(failMoreLanguageStatusExcelResultDTO)) {
                // 如果失败的数据不为空 则存储到 Redis 中
                // 生成导出失败信息的唯一标识 用作 Redis 查询
                String uniqueValue = IdUtil.getSnowflakeNextIdStr();
                logger.info("*************** 导入吊牌款号生成的唯一标识是：「{}」 ***************", uniqueValue);
                redisUtils.set("uniqueValue:" + uniqueValue, JSONUtil.parse(failMoreLanguageStatusExcelResultDTO), 60 * 60 *24);
                warpDTO.setUniqueValue(uniqueValue);
            }
        }
    }

    /**
     * 导出导入吊牌款号失败的数据
     * @param uniqueValue 唯一标识 用作 Redis 查询
     */
    @ApiOperation(value = "导出导入吊牌款号失败的数据")
    @GetMapping("/exportImportExcelFailData")
    public void exportImportExcelFailData(@RequestParam("uniqueValue") String uniqueValue, HttpServletResponse response) {
        styleCountryStatusService.exportImportExcelFailData(uniqueValue, response);
    }

    /**
     * 导出吊牌款号
     */
    @GetMapping("/exportExcel")
    @ApiOperation(value = "导出吊牌款号", notes = "导出吊牌款号")
    @DuplicationCheck(type = 1,time = 999)
    public ApiResult exportExcel(String template) {
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            String fileName = URLEncoder.encode(String.format("多语言款号模板-%s.xlsx", System.currentTimeMillis()),"UTF-8").replaceAll("\\+", "%20");
            Class<?> entityClass;
            List<?> dataList;
            if (YesOrNoEnum.YES.getValueStr().equals(template)) {
                entityClass = MoreLanguageStatusExcelTemplateDTO.class;
                dataList = CollUtil.newArrayList(new MoreLanguageStatusExcelTemplateDTO());
            }else {
                entityClass = MoreLanguageStatusExcelDTO.class;
                dataList = styleCountryStatusService.exportExcel();
            }
            ExcelUtils.exportExcel(dataList,  entityClass, fileName,new ExportParams() ,response);
            return selectSuccess("导出成功");
        }catch (Exception e) {
            // 重置response
            e.printStackTrace();
            return ApiResult.error("导出错误",0);
        }
    }

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult listQuery(@Valid MoreLanguageStatusQueryDto statusQueryDto) {
        if (statusQueryDto == null) {
            throw new OtherException("参数不能为空");
        }
        return selectSuccess(styleCountryStatusService.listQuery(statusQueryDto));
    }

    /**
     * 修改审核状态
     */
    @ApiOperation(value = "修改审核状态", notes = "修改审核状态")
    @PostMapping("/updateStatus")
    public ApiResult updateStatus(@RequestBody List<StyleCountryStatus> updateStatus) {
        return updateSuccess(styleCountryStatusService.updateStatus(updateStatus, new ArrayList<>(), true));
    }

    /**
     * 查询列表
     */
    @ApiOperation(value = "根据款号获取状态", notes = "根据款号获取状态")
    @GetMapping("/findPrintRecordByStyleNo")
    public ApiResult<StyleCountryStatusDto> findPrintRecordByStyleNo(@Valid HangTagMoreLanguageDTO languageDTO) {
        languageDTO.setUserCompany(super.getUserCompany());
        return selectSuccess(styleCountryStatusService.findPrintRecordByStyleNo(languageDTO));
    }
}
