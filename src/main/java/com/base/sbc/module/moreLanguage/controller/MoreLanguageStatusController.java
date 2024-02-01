package com.base.sbc.module.moreLanguage.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ModelBuildEventListener;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.util.MapUtils;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageOperaLogDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageOperaLogEntity;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelResultDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusQueryDto;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.listener.MoreLanguageImportListener;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import com.base.sbc.module.moreLanguage.strategy.MoreLanguageTableContext;
import com.base.sbc.module.operalog.entity.OperaLogEntity;
import com.base.sbc.module.operalog.service.OperaLogService;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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


    /**
     * 导入吊牌款号
     */
    @PostMapping("/importExcel")
    @ApiOperation(value = "导入吊牌款号", notes = "导入吊牌款号")
    @DuplicationCheck(type = 1, time = 999)
    public ApiResult importExcel(@RequestParam("file") MultipartFile file) {
        List<MoreLanguageStatusExcelResultDTO> result = new ArrayList<>();
        try {
            // 暂且使用匿名内部类, 如果要处理的字段多了,需要单独拿出一个listener
            AtomicInteger num = new AtomicInteger(1);
            EasyExcel.read(file.getInputStream(), MoreLanguageStatusExcelDTO.class,
                    new PageReadListener<MoreLanguageStatusExcelDTO>(dataList-> {
                        if (num.get() > 3) {
                            throw new RightException("仅能导入60条数据,后续款号不执行");
                        }
                        List<MoreLanguageStatusExcelResultDTO> resultDTOList = styleCountryStatusService.importExcel(dataList);
                        // 根据导入数据的顺序排序处理后的数据
                        if (CollUtil.isNotEmpty(resultDTOList)) {
                            result.addAll(dataList.stream().map(excelDto->
                                    resultDTOList.stream().filter(it-> it.getBulkStyleNo().equals(excelDto.getBulkStyleNo()))
                                            .findFirst().orElse(MORE_LANGUAGE_CV.copy2ResultDTO(excelDto.getBulkStyleNo()))
                            ).collect(Collectors.toList()));
                        }else {
                            result.addAll(MORE_LANGUAGE_CV.copyList2ResultDTO(
                                    dataList.stream().map(MoreLanguageStatusExcelDTO::getBulkStyleNo).collect(Collectors.toList())
                            ));
                        }
                        num.incrementAndGet();
                    }, 20)).sheet().doRead();
            // 存入redis, 方便接口查询
            return ApiResult.success("导入成功", result);
        }catch (RightException e){
            return ApiResult.success(e.getMessage(), result);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.error(e.getMessage(), 0);
        }finally {
            StyleCountryStatusService.countryList.remove();
        }
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
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(String.format("多语言款号模板-%s.xlsx", System.currentTimeMillis()),"UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), MoreLanguageStatusExcelDTO.class)
                    .sheet("模板")
                    .doWrite(YesOrNoEnum.YES.getValueStr().equals(template) ? new ArrayList<>() : styleCountryStatusService.exportExcel());
            return selectSuccess("导出成功");
        }catch (Exception e) {
            // 重置response
            response.reset();
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
        return updateSuccess(styleCountryStatusService.updateStatus(updateStatus));
    }
}
