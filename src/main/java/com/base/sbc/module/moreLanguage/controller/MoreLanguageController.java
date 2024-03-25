package com.base.sbc.module.moreLanguage.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExcelQueryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageOperaLogDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageOperaLogEntity;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.listener.MoreLanguageImportListener;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.*;

/**
 * @author 孔祥基
 * @date 2023/3/22 15:51:24
 */
@RestController
@Api(value = "与多语言相关的所有接口信息", tags = {"多语言接口"})
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/moreLanguage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MoreLanguageController extends BaseController {

    private final MoreLanguageService moreLanguageService;

    private final UserUtils userUtils;

    private final FlowableService flowableService;

    private final RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MoreLanguageImportListener importListener;

    private final OperaLogService operaLogService;

    /**
     * 导入国家翻译
     */
    @SneakyThrows
    @PostMapping("/importExcel")
    @ApiOperation(value = "导入国家翻译", notes = "导入国家翻译")
    @DuplicationCheck(type = 1, time = 999)
    public ApiResult importExcel(@RequestParam("file") MultipartFile file, MoreLanguageExcelQueryDto excelQueryDto) {
//        moreLanguageService.importExcel(excelQueryDto);
        importListener.setExcelQueryDto(excelQueryDto);
        try {
            EasyExcel.read(file.getInputStream(), importListener).headRowNumber(MoreLanguageProperties.excelDataRowNum).doReadAllSync();
            String warnMsg = importListener.dataVerifyHandler();
            ApiResult<String> result = selectSuccess(MoreLanguageProperties.getMsg(SUCCESS_IMPORT,warnMsg));
            if (StrUtil.isNotBlank(warnMsg)) {
                result.setMessage(MoreLanguageProperties.getMsg(CHECK_REIMPORT,result.getMessage()));
            }else {
                result.setStatus(200);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.error(MoreLanguageProperties.getMsg(FAILURE_IMPORT,e.getMessage()), 0);
        }
    }
    /**
     * 导出国家翻译
     */
    @GetMapping("/exportExcel")
    @Transactional(rollbackFor = {Exception.class})
    @ApiOperation(value = "导出国家翻译", notes = "导出国家翻译")
    @DuplicationCheck(type = 1,time = 999)
    public ApiResult exportExcel(@Valid MoreLanguageExcelQueryDto excelQueryDto) {
        moreLanguageService.exportExcel(excelQueryDto);

        return selectSuccess("");
    }

    /**
     * 查询列表
     */
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @GetMapping("/listQuery")
    public ApiResult listQuery(@Validated({MoreLanguageService.class}) MoreLanguageQueryDto moreLanguageQueryDto) {
        if (moreLanguageQueryDto == null) {
            throw new OtherException("参数不能为空");
        }
        return selectSuccess(moreLanguageService.listQuery(moreLanguageQueryDto));
    }

    /**
     * 查询国家拥有的标准表表头
     */
    @ApiOperation(value = "查询国家拥有的标准表表头", notes = "查询国家拥有的标准表表头")
    @GetMapping("/queryCountryTitle")
    public ApiResult  queryCountryTitle(@Validated({MoreLanguageService.class}) MoreLanguageQueryDto moreLanguageQueryDto) {
        if (moreLanguageQueryDto == null) {
            throw new OtherException("参数不能为空");
        }
        List<StandardColumnDto> standardColumnDtoList = moreLanguageService.queryCountryTitle(moreLanguageQueryDto);
        MoreLanguageTableContext.clear();
        return selectSuccess(standardColumnDtoList);
    }

    @ApiOperation(value = "变更日志")
    @GetMapping("/operationLog")
    public ApiResult<PageInfo<? extends OperaLogEntity>> operationLog(@Valid MoreLanguageOperaLogDTO moreLanguageOperaLogDTO) {
        moreLanguageOperaLogDTO.setUserCompany(super.getUserCompany());
        moreLanguageOperaLogDTO.init();
        PageInfo<OperaLogEntity> page = operaLogService.listPage(moreLanguageOperaLogDTO);
        page.setList(page.getList().stream().map(it-> BeanUtil.copyProperties(it, MoreLanguageOperaLogEntity.class)).collect(Collectors.toList()));
        return selectSuccess(page);
    }
}
