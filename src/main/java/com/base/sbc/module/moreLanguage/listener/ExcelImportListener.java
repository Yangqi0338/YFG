//package com.base.sbc.module.moreLanguage.listener;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.collection.ListUtil;
//import cn.hutool.core.util.RandomUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONUtil;
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//import com.alibaba.excel.metadata.data.ReadCellData;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.base.sbc.module.moreLanguage.dto.DataVerifyResultVO;
//import com.base.sbc.module.moreLanguage.dto.MoreLanguageImportDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
//@Slf4j
//@Component
//public class ExcelImportListener<T> extends AnalysisEventListener<T> {
//
//    /**
//     * 最大可上传数据条数
//     */
//    public static final Integer maxImportCount = 500;
//    /**
//     * 批次号前缀
//     */
//    public static final String importBatchSerialNumberPrefix = "MLI";
//    /**
//     * 日志输出前缀
//     */
//    public static final String logPrefix = "【多语言-数据导入】:";
//    /**
//     * 可用线索类型
//     */
//    private static Map<String, String> source;
//    /**
//     * 导入数据批次编号
//     */
//    public static ThreadLocal<String> importBatchNo = new ThreadLocal<>();
//    /**
//     * 数据源
//     */
//    public static ThreadLocal<ArrayList<T>> sourceData = new ThreadLocal<>();
//    /**
//     * 数据合法性校验结果
//     */
//    private static ThreadLocal<ArrayList<DataVerifyResultVO>> dataVerifyResults = new ThreadLocal<>();
//
//    @PostConstruct
//    public void initMetadata() {
//        log.info(ExcelImportListener.logPrefix + "元数据初始化成功...");
//    }
//
//
//    /**
//     * 付费 1
//     * 活动 huodong
//     * 渠道 qudao
//     * 销售自拓 xszt
//     * 百度 baidu
//     * 抖音 douyin
//     */
//    static {
//        source = new HashMap<>();
//
//        source.put("市场拓展", "sctz");
//        source.put("商务拓展", "swtz");
//        source.put("销售拓展", "xszt");
//
//        log.info(ExcelImportListener.logPrefix + "初始化线索类型成功...");
//    }
//
//    @Override
//    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
//        super.invokeHead(headMap, context);
//    }
//
//    @Override
//    public void invoke(T crmCustomerImportDTO, AnalysisContext analysisContext) {
//
//        if (CollectionUtil.isEmpty(sourceData.get())) {
//            sourceData.set(new ArrayList<>());
//        }
//
//        if (CollectionUtil.isEmpty(dataVerifyResults.get())) {
//            dataVerifyResults.set(new ArrayList<>());
//        }
//
//        Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
//        if (rowIndex < 3) {
//            return;
//        }
////        Map crmCustomerSource = redisUtils.getMapByKey(REDIS_KEY_DATA_DICTIONARY_AVAILABLE_LIST_PREFIX+"crmCustomerSource", CrmCustomerSource.class);
//
//        List<DataVerifyResultVO> dataVerifyResultVOS = dataVerifyResults.get();
//        crmCustomerImportDTO.setRowIndex(rowIndex + 1);
//        log.info(ExcelImportListener.logPrefix + "正在读取{}行数据{}", crmCustomerImportDTO.getRowIndex(), JSONUtil.toJsonStr(crmCustomerImportDTO));
//        verifyRowData(crmCustomerImportDTO, dataVerifyResultVOS, source);
//        sourceData.get().add(crmCustomerImportDTO);
//
//    }
//
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
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
//        log.info(ExcelImportListener.logPrefix + "本批次导入数据:{} 条", crmCustomerImportDTOS.size());
//
//    }
//
//
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
//
//
//    /**
//     * 数据非空判断,合法性校验
//     *
//     * @param crmCustomerImportDTO
//     */
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
//
//    private String separator = System.getProperty("line.separator");
//    /**
//     * 导入数据
//     */
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
//            log.info(ExcelImportListener.logPrefix + "正在导入第{}行数据", customerImport.getRowIndex());
//        }
//
//    }
//
//    /**
//     * 销毁资源
//     */
//    public static void destoryResource() {
//        importBatchNo.remove();
//        dataVerifyResults.remove();
//        sourceData.remove();
//        log.info(ExcelImportListener.logPrefix + "线程{}资源销毁...", Thread.currentThread().getName());
//    }
//
//
//    /**
//     * 线索类型数据合法性校验
//     * @param source
//     * @param sourceMap
//     * @return
//     */
//    private Boolean verSourceLegal(String source, Map<String, String> sourceMap) {
//        boolean contains = sourceMap.containsKey(source.trim());
//        if (!contains) {
//            return false;
//        }
//        return true;
//    }
//
//
//    public static String genImportBatchSerialNumber() {
//        return importBatchSerialNumberPrefix + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + RandomUtil.randomNumbers(5);
//    }
//
//}
//
