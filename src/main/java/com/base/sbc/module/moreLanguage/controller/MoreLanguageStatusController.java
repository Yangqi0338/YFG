package com.base.sbc.module.moreLanguage.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelResultDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelTemplateDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusQueryDto;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.moreLanguage.service.StyleCountryStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.*;
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
            // 获取总数和循环次数
            Integer count = MoreLanguageProperties.calculateImportCount();
            Integer size = (int) Math.ceil((double) MoreLanguageProperties.styleCountryStatusImportMaxSize / count);
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
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(String.format("多语言款号模板-%s.xlsx", System.currentTimeMillis()),"UTF-8").replaceAll("\\+", "%20");
//            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
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

/*    public void excelExportData(HttpServletResponse response, List<String> names, List<String> fieldEn, List<?> list , String sheetName, String fileName){
        //设置返回数据的值跟动态列一一对应
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .head(headData(CollectionUtil.isNotEmpty(names) ? names.toArray(new String[0]) : new String[0]))
                .registerWriteHandler(new AutoWidthHandler())
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25))
                .sheet(sheetName)
                .doWrite(setData(list,fieldEn));
    }

    private List<List<String>> setData(List<?> list,List<String> fieldEn){
        List<List<String>> datas = new ArrayList<>();
        //对象反射转map方法
        List<Map<Object, Object>> maps = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)){
            for (Object o : list) {
                Class<?> aClass = o.getClass();
                Field[] fields = aClass.getDeclaredFields();
                Map<Object, Object> map = new HashMap<>(40);

                for (Field field : fields) {
                    map.put(field.getName(), getResult(field.getName(), o));
                }
                maps.add(map);
            }
            for (Map<Object, Object> map : maps) {
                //用于接收返回数据行？
                List<String> data = new LinkedList<String>();
                for (int i = 0; i < fieldEn.size(); i++) {
                    Object o = map.get(fieldEn.get(i));
                    data.add(Objects.isNull(o) ? Constants.CHAR : o.toString());
                }
                datas.add(data);
            }
        }
        return datas;
    }

    *//**
     * 数据动态头传入
     *//*
    private List<List<String>> headData(String[] header) {
        List<String> head0;
        List<List<String>> list = new LinkedList<>();
        for (String h : header) {
            head0 = new LinkedList<>();
            head0.add(h);
            list.add(head0);
        }
        return list;
    }*/

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
}
