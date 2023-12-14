package com.base.sbc.module.moreLanguage.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.moreLanguage.dto.DataVerifyResultVO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageExportBaseDTO;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageMapExportMapping;
import com.base.sbc.module.moreLanguage.entity.Country;
import com.base.sbc.module.moreLanguage.entity.CountryModel;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.entity.StandardColumnTranslate;
import com.base.sbc.module.moreLanguage.service.CountryModelService;
import com.base.sbc.module.moreLanguage.service.CountryService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.moreLanguage.service.StandardColumnTranslateService;
import com.base.sbc.module.moreLanguage.service.impl.CountryModelServiceImpl;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.base.sbc.config.constant.Constants.COMMA;


@Slf4j
@Component
public class MoreLanguageImportListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 批次号前缀
     */
    public static final String importBatchSerialNumberPrefix = "MLI";
    /**
     * 日志输出前缀
     */
    public static final String logPrefix = "【多语言-数据导入】:";
    /**
     * 导入数据批次编号
     */
    public static ThreadLocal<String> importBatchNo = new ThreadLocal<>();
    public static ThreadLocal<Map<String, MoreLanguageMapExportMapping>> sheetExportMapping = new ThreadLocal<>();

    @Autowired
    private StandardColumnTranslateService standardColumnTranslateService;

    @Autowired
    private CountryModelService countryModelService;

    @Autowired
    private StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private StandardColumnService standardColumnService;

    @Setter
    private String countryLanguageId;


    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        getMapping(context,(mapExportMapping)-> {
            mapExportMapping.setHeadMap(headMap);
            return true;
        });
    }

    private <T> T getMapping(AnalysisContext context, Function<MoreLanguageMapExportMapping, T> function){
        Map<String, MoreLanguageMapExportMapping> exportMapping = sheetExportMapping.get();
        if (exportMapping == null) {
            exportMapping = new HashMap<>();
        }
        String sheetName = context.readSheetHolder().getSheetName();
        MoreLanguageMapExportMapping mapExportMapping = exportMapping.getOrDefault(sheetName, new MoreLanguageMapExportMapping(sheetName));
        T result = function.apply(mapExportMapping);
        exportMapping.put(sheetName,mapExportMapping);
        sheetExportMapping.set(exportMapping);;
        return result;
    }

    private void removeMapping(AnalysisContext context){
        String sheetName = context.readSheetHolder().getSheetName();
        Map<String, MoreLanguageMapExportMapping> exportMapping = sheetExportMapping.get();
        if (exportMapping == null || !exportMapping.containsKey(sheetName)) {
            return;
        }
        exportMapping.remove(sheetName);
        if (exportMapping.isEmpty()) {
            sheetExportMapping.remove();
        }
    }

    // 解析

    @Override
    public void invoke(Map<Integer, String> value, AnalysisContext context) {
        Pair<MoreLanguageMapExportMapping, MoreLanguageExportBaseDTO> exportPair = getMapping(context, (mapExportMapping)-> {
            MoreLanguageExportBaseDTO tempBaseDTO = mapExportMapping.initByFirstRow(value, MoreLanguageExportBaseDTO.class);
            return Pair.of(mapExportMapping, tempBaseDTO);
        });
        MoreLanguageMapExportMapping exportMapping = exportPair.getKey();
        MoreLanguageExportBaseDTO exportBaseDTO = exportPair.getValue();
        Map<String, String> map = exportMapping.buildMap(value);

        StandardColumnCountryTranslate baseSource;
        // 设置值
        if (exportBaseDTO != null) {
            // 初始化
            String key = exportBaseDTO.getKey();
            String keyName = exportBaseDTO.getKeyName();
            String standardColumnCode = exportBaseDTO.getStandardColumnCode();

            baseSource = new StandardColumnCountryTranslate();
            baseSource.setCountryLanguageId(countryLanguageId);
            baseSource.setTitleCode(standardColumnCode);
            baseSource.setPropertiesCode(key);
            baseSource.setPropertiesName(keyName);

            exportMapping.setBaseSourceData(baseSource);
        }else {
            baseSource = exportMapping.getBaseSourceData();
        }
        String content = map.get("content");
        String code = Arrays.stream(baseSource.getPropertiesCode().split("-"))
                .map(map::get).collect(Collectors.joining("-"));
        String name = Arrays.stream(baseSource.getPropertiesName().split("-"))
                .map(map::get).collect(Collectors.joining("-"));

        // 获取TableData,查看数据是否修改
        // 也可以使用隐藏列,缺点是会被修改移除

        if (StrUtil.isBlank(content) || StrUtil.isBlank(code)) return;

        StandardColumnCountryTranslate countryTranslate = BeanUtil.copyProperties(baseSource, StandardColumnCountryTranslate.class);
        countryTranslate.setPropertiesCode(code);
        countryTranslate.setPropertiesName(name);
        countryTranslate.setContent(content);

        List<StandardColumnCountryTranslate> translateList = exportMapping.getSourceData();
        if (CollectionUtil.isEmpty(translateList)) {
            translateList = new ArrayList<>();
        }
        translateList.add(countryTranslate);
        exportMapping.setSourceData(translateList);

//        if (CollectionUtil.isEmpty(dataVerifyResults.get())) {
//            dataVerifyResults.set(new ArrayList<>());
//        }

//        Integer rowIndex = context.readRowHolder().getRowIndex();
//        if (rowIndex < 3) {
//            return;
//        }
//        Map crmCustomerSource = redisUtils.getMapByKey(REDIS_KEY_DATA_DICTIONARY_AVAILABLE_LIST_PREFIX+"crmCustomerSource", CrmCustomerSource.class);

//        List<DataVerifyResultVO> dataVerifyResultVOS = dataVerifyResults.get();
//        crmCustomerImportDTO.setRowIndex(rowIndex + 1);
//        log.info(MoreLanguageImportListener.logPrefix + "正在读取{}行数据{}", crmCustomerImportDTO.getRowIndex(), JSONUtil.toJsonStr(crmCustomerImportDTO));
//        verifyRowData(crmCustomerImportDTO, dataVerifyResultVOS, source);
//        sourceData.get().add(crmCustomerImportDTO);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doAfterAllAnalysed(AnalysisContext context) {
        MoreLanguageMapExportMapping exportMapping = getMapping(context, (mapExportMapping) -> mapExportMapping);
        List<StandardColumnCountryTranslate> translateList = exportMapping.getSourceData();
        if (CollectionUtil.isEmpty(translateList)) return;
        StandardColumnCountryTranslate baseTranslate = exportMapping.getBaseSourceData();
        String countryLanguageId = baseTranslate.getCountryLanguageId();
        String titleCode = baseTranslate.getTitleCode();

        Country country = countryService.getById(countryLanguageId);
        if (country == null) throw new OtherException("未找到国家");

        Object titleObject = standardColumnService.findByCode(titleCode);
        if (titleObject == null) throw new OtherException("未找到标准列");
        StandardColumn standardColumn = (StandardColumn) titleObject;

        for (StandardColumnCountryTranslate it : translateList) {
            it.setTitleName(standardColumn.getName());
            String code = String.join(COMMA, it.getCountryLanguageId(), it.getTitleCode(), Opt.ofNullable(it.getPropertiesCode()).orElse(" "));
            StandardColumnCountryTranslate countryTranslate = standardColumnCountryTranslateService.findByCode(code);
            if (countryTranslate != null) {
                it.setId(countryTranslate.getId());
                it.setPropertiesName(countryTranslate.getPropertiesName());
                it.setTitleName(countryTranslate.getTitleName());
            }
        }

        standardColumnCountryTranslateService.saveOrUpdateBatch(translateList);

        // 号型和表头特殊 设置专门的表存储,数据较少,直接删除新增.
        countryModelService.remove(new BaseLambdaQueryWrapper<CountryModel>().eq(CountryModel::getCountryLanguageId, countryLanguageId));
        List<CountryModel> translateModelList = translateList.stream().filter(it -> "DP06".equals(it.getTitleCode())).map(it -> {
            CountryModel countryModel = new CountryModel();
            countryModel.setCountryLanguageId(countryLanguageId);
            String propertiesCode = it.getPropertiesCode();
            String propertiesName = it.getPropertiesName();
            countryModel.setModelCode(propertiesCode.split("-")[1]);
            countryModel.setModelName(propertiesName.split("-")[1]);
            countryModel.setBasicSizeCode(propertiesCode.split("-")[0]);
            countryModel.setBasicSizeName(propertiesName.split("-")[0]);
            countryModel.setContent(it.getContent());
            return countryModel;
        }).collect(Collectors.toList());
        countryModelService.saveBatch(translateModelList);

        standardColumnTranslateService.remove(new BaseLambdaQueryWrapper<StandardColumnTranslate>().eq(StandardColumnTranslate::getCountryLanguageId, countryLanguageId));
        List<StandardColumnTranslate> translateTitleList = translateList.stream().filter(it -> "DP00".equals(it.getTitleCode())).map(it -> {
            StandardColumnTranslate standardColumnTranslate = new StandardColumnTranslate();
            standardColumnTranslate.setCountryLanguageId(countryLanguageId);
            standardColumnTranslate.setStandardColumnCode(it.getPropertiesCode());
            standardColumnTranslate.setStandardColumnName(it.getPropertiesName());
            standardColumnTranslate.setContent(it.getContent());
            return standardColumnTranslate;
        }).collect(Collectors.toList());
        standardColumnTranslateService.saveBatch(translateTitleList);

        removeMapping(context);
//        //非空判断
//        List<DataVerifyResultVO> dataVerifyResultVOS = dataVerifyResults.get();
//        if (CollectionUtil.isNotEmpty(dataVerifyResultVOS)) {
//            destoryResource();
//            throw new DataCheckException(dataVerifyResultVOS);
//        }
//
//        //数据重复校验
//        ArrayList<CrmCustomerImportDTO> crmCustomerImportDTOS = sourceData.get();
//        List<DataVerifyResultVO> dataRepeatVerResult = dataRepeatCheck(crmCustomerImportDTOS);
//        if (CollectionUtil.isNotEmpty(dataRepeatVerResult)) {
//            destoryResource();
//            throw new DataCheckException(dataRepeatVerResult);
//        }
//
//        if (crmCustomerImportDTOS.size() > maxImportCount) {
//            throw new DataCheckException("最多可导入客户线索500条");
//        }
//
//
//        insertCrmClues(crmCustomerImportDTOS);
//        log.info(MoreLanguageImportListener.logPrefix + "本批次导入数据:{} 条", crmCustomerImportDTOS.size());

    }


//    /**
//     * 数据重复校验
//     *
//     * @param verDataList
//     * @return
//     */
//    private List<DataVerifyResultVO> dataRepeatCheck(List<CrmCustomerImportDTO> verDataList) {
//        List<DataVerifyResultVO> verResults = new ArrayList<>();
//
//        Map<String, Integer> map = new HashMap<>();
//        for (CrmCustomerImportDTO row : verDataList) {
//            //公司名,品牌名,手机号是否重复
//            StringBuilder unionKey = new StringBuilder(row.getCompanyName().trim());
//            unionKey.append("-");
//            unionKey.append(row.getBrandName().trim());
//            unionKey.append("-");
//            unionKey.append(row.getCellphone().trim());
//            String keyStr = unionKey.toString();
//            if (map.containsKey(keyStr)) {
//                DataVerifyResultVO verRes = new DataVerifyResultVO();
//                verRes.setRowIndex(row.getRowIndex());
//
//                Map<String, String> repeatColumn = new HashMap<>();
//                repeatColumn.put(keyStr, "数据重复");
//                verRes.setErrorMessageList(repeatColumn);
//                verResults.add(verRes);
//
//                Integer count = map.get(keyStr);
//                map.put(keyStr, count + 1);
//            } else {
//                map.put(keyStr, 1);
//            }
//        }
//        return verResults;
//    }


    /**
     * 数据非空判断,合法性校验
     *
     * @param crmCustomerImportDTO
     */
//    private void verifyRowData(CrmCustomerImportDTO crmCustomerImportDTO, List<DataVerifyResultVO> dataVerifyResults, Map<String, String> sourceMap) {
//        DataVerifyResultVO verResult = new DataVerifyResultVO();
//        verResult.setRowIndex(crmCustomerImportDTO.getRowIndex());
//        Map<String, String> columnVerifyResults = verResult.getErrorMessageList();
//
//        if (StringUtils.isBlank(crmCustomerImportDTO.getProvider())) {
//            columnVerifyResults.put("线索来源", "线索来源未填写");
//        } else {
//            if (!verSourceLegal(crmCustomerImportDTO.getProvider(), sourceMap)) {
//                columnVerifyResults.put("线索来源", "线索来源与系统规则不正确");
//            }
//        }
//
//        if (StringUtils.isBlank(crmCustomerImportDTO.getBasicClueExpandChannelId())) {
//            columnVerifyResults.put("线索来源", "线索来源渠道未填写");
//        } else {
//            List<BasicClueExpandChannel> list = basicClueExpandChannelMapper.selectList(new LambdaQueryWrapper<BasicClueExpandChannel>().eq(BasicClueExpandChannel::getName,crmCustomerImportDTO.getBasicClueExpandChannelId().trim()));
//            if(list.size()==0)
//            {
//                columnVerifyResults.put("线索来源", "线索来源渠道未找到");
//            }
//            else {
//                crmCustomerImportDTO.setBasicClueExpandChannelId(list.get(0).getId());
//            }
//        }
//
//        if (StringUtils.isBlank(crmCustomerImportDTO.getEmployeeId())) {
//            columnVerifyResults.put("商务", "商务人员未填写");
//        } else {
//            List<SysEmployee> list = sysEmployeeMapper.selectList(new LambdaQueryWrapper<SysEmployee>().eq(SysEmployee::getName,crmCustomerImportDTO.getEmployeeId().trim()));
//            if(list.size()==0)
//            {
//                columnVerifyResults.put("商务", "商务人员未找到");
//            }
//            else {
//                crmCustomerImportDTO.setEmployeeId(list.get(0).getId());
//            }
//        }
//
//        SysIdentityServe serve = null;
//        if (StringUtils.isNotBlank(crmCustomerImportDTO.getServeId())) {
//            List<SysIdentityServe> list = serveMapper.selectList(new LambdaQueryWrapper<SysIdentityServe>().eq(SysIdentityServe::getName,crmCustomerImportDTO.getServeId().trim()));
//            if(list.size()==0)
//            {
//                columnVerifyResults.put("服务商", "服务商未找到");
//            }
//            else {
//                serve = list.get(0);
//                crmCustomerImportDTO.setServeId(serve.getId());
//            }
//        }
//
//        if (StringUtils.isNotBlank(crmCustomerImportDTO.getSaleEmployeeId())) {
//            if (serve == null) columnVerifyResults.put("服务商", "未填写销售人员的服务商公司名称,不予处理销售人员");
//            else {
//                List<SysIdentityAccount> list = accountMapper.selectList(new LambdaQueryWrapper<SysIdentityAccount>().eq(SysIdentityAccount::getIdentityId, serve.getId())
//                        .eq(SysIdentityAccount::getName,crmCustomerImportDTO.getSaleEmployeeId().trim()).eq(SysIdentityAccount::getIdentityType, UserIdentity.SWK.getCode()));
//                if(list.size()==0)
//                {
//                    columnVerifyResults.put("销售", serve.getName() + "的销售人员未找到");
//                }
//                else {
//                    crmCustomerImportDTO.setSaleEmployeeId(list.get(0).getId());
//                }
//            }
//        }
//
//        List<String> followEmployeeIdList = ListUtil.toList(crmCustomerImportDTO.getFollowEmployeeId1(), crmCustomerImportDTO.getFollowEmployeeId2(), crmCustomerImportDTO.getFollowEmployeeId3());
//        followEmployeeIdList = followEmployeeIdList.stream().filter(StrUtil::isNotBlank).map(String::trim).collect(Collectors.toList());
//        if (CollectionUtil.isNotEmpty(followEmployeeIdList)) {
//            if (serve == null) columnVerifyResults.put("服务商", "未填写跟单人员的服务商公司名称,不予处理跟单人员");
//            else {
//                List<SysIdentityAccount> list = accountMapper.selectList(new LambdaQueryWrapper<SysIdentityAccount>().eq(SysIdentityAccount::getIdentityId, serve.getId())
//                        .in(SysIdentityAccount::getName, followEmployeeIdList).eq(SysIdentityAccount::getIdentityType, UserIdentity.SWK.getCode()));
//
//                crmCustomerImportDTO.setFollowEmployeeIdList(list.stream().map(SysIdentityAccount::getId).collect(Collectors.toList()));
//
//                for (String followEmployeeId : followEmployeeIdList) {
//                    if (list.stream().noneMatch(it -> it.getName().equals(followEmployeeId))) {
//                        columnVerifyResults.put("跟单", serve.getName() + "的跟单人员" + followEmployeeId + "未找到");
//                    }
//                }
//            }
//        }
//
//        if (StringUtils.isNotBlank(crmCustomerImportDTO.getQcEmployeeId())) {
//            if (serve == null) columnVerifyResults.put("服务商", "未填写QC人员的服务商公司名称,不予处理QC人员");
//            else {
//                List<SysIdentityAccount> list = accountMapper.selectList(new LambdaQueryWrapper<SysIdentityAccount>().eq(SysIdentityAccount::getIdentityId, serve.getId())
//                        .eq(SysIdentityAccount::getName,crmCustomerImportDTO.getQcEmployeeId().trim()).eq(SysIdentityAccount::getIdentityType, UserIdentity.SWK.getCode()));
//                if(list.size()==0)
//                {
//                    columnVerifyResults.put("QC", serve.getName() + "的QC人员未找到");
//                }
//                else {
//                    crmCustomerImportDTO.setQcEmployeeId(list.get(0).getId());
//                }
//            }
//        }
//
//        if (StringUtils.isNotBlank(crmCustomerImportDTO.getMarketEmployeeId())) {
//            List<SysEmployee> list = sysEmployeeMapper.selectList(new LambdaQueryWrapper<SysEmployee>().eq(SysEmployee::getName,crmCustomerImportDTO.getMarketEmployeeId().trim()));
//            if(list.size()==0)
//            {
//                columnVerifyResults.put("市场", "市场人员未找到");
//            }
//            else {
//                crmCustomerImportDTO.setMarketEmployeeId(list.get(0).getId());
//            }
//        }
//
//        if (StringUtils.isBlank(crmCustomerImportDTO.getCompanyName())) {
//            columnVerifyResults.put("公司名称", "公司名称未填写");
//        }
//        else{
//            Integer count = customerMapper.selectCount(new QueryWrapper<CrmCustomer>().lambda().eq(CrmCustomer::getCompanyName,crmCustomerImportDTO.getCompanyName().trim()));
//            if(count>0){
//                columnVerifyResults.put("公司名称", "公司名称已存在");
//            }
//            SysBlacklist check = sysBlacklistService.check(crmCustomerImportDTO.getCompanyName(), "", false);
//            if (check != null){
//                columnVerifyResults.put("黑名单限制", ZhCnErrorCodeEnums.INSERT_COMPANY_BLACK.getValue());
//            }
//        }
//
//        if (StringUtils.isBlank(crmCustomerImportDTO.getBrandName())) {
//            columnVerifyResults.put("品牌名称", "品牌名称未填写");
//        }
//        else{
//            Integer count = customerMapper.selectCount(new QueryWrapper<CrmCustomer>().lambda().eq(CrmCustomer::getBrandName,crmCustomerImportDTO.getBrandName().trim()));
//            if(count>0){
//                columnVerifyResults.put("品牌名称", "品牌名称已存在");
//            }
//            SysBlacklist check = sysBlacklistService.check("", crmCustomerImportDTO.getBrandName(), false);
//            if (check != null){
//                columnVerifyResults.put("黑名单限制", ZhCnErrorCodeEnums.INSERT_BRAND_BLACK.getValue());
//            }
//        }
//
//        if (StringUtils.isBlank(crmCustomerImportDTO.getContactsName())) {
//            columnVerifyResults.put("联系人", "联系人名称未填写");
//        }
//
//        if (StringUtils.isBlank(crmCustomerImportDTO.getCellphone())) {
//            columnVerifyResults.put("手机号", "手机号未填写");
//        } else {
//            if (crmCustomerImportDTO.getCellphone().trim().length() != 11) {
//                columnVerifyResults.put("手机号", "手机号格式不正确");
//            }
//            else{
//                Integer count = customerContactsMapper.selectCount(new QueryWrapper<CrmCustomerContacts>().lambda().eq(CrmCustomerContacts::getCellphone,crmCustomerImportDTO.getCellphone().trim()));
//                if(count>0){
//                    columnVerifyResults.put("手机号", "手机号已存在");
//                }
//            }
//        }
//        if (columnVerifyResults.size() > 0) {
//            dataVerifyResults.add(verResult);
//        }
//
//    }

    private String separator = System.getProperty("line.separator");
    /**
     * 导入数据
     */
//    private void insertCrmClues(List<CrmCustomerImportDTO> crmCustomerImportDTOS) {
//
//        //导入数据
//        for (CrmCustomerImportDTO customerImport : crmCustomerImportDTOS) {
//            /*1.处理客户关系信息*/
//            CrmCustomer customer = new CrmCustomer();
//            customer.setCompanyName(customerImport.getCompanyName());
//            customer.setBrandName(customerImport.getBrandName());
//            customer.setAreaCode("");
//            customer.setAvailableFlag(Flag.YES.getCode());
//            customer.setTransFlag(Flag.NO.getCode());
//            customer.setInsertTime(LocalDateTime.now());
//            customer.setInsertUser(IdUtils.getId());
//            customer.setStatus(CrmCustomerStatus.CLUE.getCode());
//            customer.setIdentityType(UserIdentity.BSL.getCode());
//            customer.setImportBatchSerialNumber(importBatchNo.get());
//            customer.setProvider(source.get(customerImport.getProvider()));
//            customer.setSaleEmployeeId(customerImport.getSaleEmployeeId());
//            customer.setMarketEmployeeId(customerImport.getMarketEmployeeId());
//            customer.setEmployeeId(customerImport.getEmployeeId());
//            customer.setBasicClueExpandChannelId(customerImport.getBasicClueExpandChannelId());
//            customer.setStreet(customerImport.getStreet());
//            customer.setComment(customerImport.getComment());
//            customerMapper.insert(customer);
//
//            /*2.处理客户联系人信息*/
//            CrmCustomerContacts contacts = contacts = new CrmCustomerContacts();
//            contacts.setName(customerImport.getContactsName());
//            contacts.setPosition(customerImport.getPosition());
//            contacts.setCellphone(customerImport.getCellphone());
//            contacts.setTelephone(customerImport.getTelephone());
//            contacts.setWxUsername(customerImport.getWxUsername());
//            contacts.setEmail(customerImport.getEmail());
//            contacts.setAvailableFlag(Flag.YES.getCode());
//            contacts.setDefaultFlag(Flag.YES.getCode());//
//            contacts.setCustomerId(customer.getId());
//            contacts.setInsertUser(IdUtils.getId());
//            contacts.setInsertTime(LocalDateTime.now());
//            customerContactsMapper.insert(contacts);
//
//            customer.setContactsId(contacts.getId());
//            customerMapper.updateById(customer);
//            log.info(MoreLanguageImportListener.logPrefix + "正在导入第{}行数据", customerImport.getRowIndex());
//        }
//
//    }

    /**
     * 销毁资源
     */
    public static void destoryResource() {
        importBatchNo.remove();
//        dataVerifyResults.remove();
        sheetExportMapping.remove();
        log.info(MoreLanguageImportListener.logPrefix + "线程{}资源销毁...", Thread.currentThread().getName());
    }


    /**
     * 线索类型数据合法性校验
     * @param source
     * @param sourceMap
     * @return
     */
    private Boolean verSourceLegal(String source, Map<String, String> sourceMap) {
        boolean contains = sourceMap.containsKey(source.trim());
        if (!contains) {
            return false;
        }
        return true;
    }


    public static String genImportBatchSerialNumber() {
        return importBatchSerialNumberPrefix + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + RandomUtil.randomNumbers(5);
    }

}

