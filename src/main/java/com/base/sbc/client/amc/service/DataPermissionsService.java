package com.base.sbc.client.amc.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.enums.DataPermissionsConditionTypeEnum;
import com.base.sbc.client.amc.enums.DataPermissionsRangeEnum;
import com.base.sbc.client.amc.enums.DataPermissionsSelectTypeEnum;
import com.base.sbc.client.amc.vo.DataPermissionVO;
import com.base.sbc.client.amc.vo.FieldDataPermissionVO;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisAmcUtils;
import com.base.sbc.config.redis.RedisUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

@Component
public class DataPermissionsService {
    @Autowired
    private AmcService amcService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RedisAmcUtils redisAmcUtils;

    /**
     * 获取数据权限
     *
     * @param businessType
     * @return
     * @see DataPermissionsBusinessTypeEnum
     */
    public List<DataPermissionVO> getDataPermissions(String businessType, String operateType) {
        if (StringUtils.isEmpty(businessType)) {
            throw new OtherException("入参不可为空");
        }
        ApiResult apiResult = amcService.getReadDataPermissions(businessType, operateType);
        if (Objects.isNull(apiResult)) {
            throw new OtherException("获取用户数据权限异常");
        }
        return JSONArray.parseArray(JSON.toJSONString(apiResult.getData()), DataPermissionVO.class);
    }

    /**
     * @param qw           查询构造器
     * @param businessType 业务对象编码
     */
    public void getDataPermissionsForQw(QueryWrapper qw, String businessType) {
        getDataPermissionsForQw(qw, businessType, "");
    }

    /**
     * @param qw           查询构造器
     * @param businessType 业务对象编码
     * @param tablePre     表别名
     */
    public void getDataPermissionsForQw(QueryWrapper qw, String businessType, String tablePre) {
        getDataPermissionsForQw(qw, businessType, tablePre,null,false);
    }
    /**
     * @param qw           查询构造器
     * @param businessType 业务对象编码
     * @param tablePre     表别名
     * @param authorityFields     自定义数据隔离字段（代表名的）
     */
    public void getDataPermissionsForQw(QueryWrapper qw, String businessType, String tablePre, String[] authorityFields) {
        getDataPermissionsForQw(qw, businessType, tablePre,authorityFields,false);
    }
    /**
     * @param qw           查询构造器
     * @param businessType 业务对象编码
     * @param tablePre     表别名
     * @param authorityFields     自定义数据隔离字段（代表名的）
     * @param isAssignFields     是否强制指定字段，配合authorityFields使用
     */
    public void getDataPermissionsForQw(QueryWrapper qw, String businessType, String tablePre, String[] authorityFields, boolean isAssignFields) {
        if (StrUtil.isBlank(businessType) || qw == null) {
            return;
        }
        UserCompany userCompany = companyUserInfo.get();
        Map read = getDataPermissionsForQw(userCompany.getCompanyCode(), userCompany.getUserId(), businessType, "read", tablePre, authorityFields, isAssignFields);
        boolean flg = MapUtil.getBool(read, "authorityState", false);
        String sql = MapUtil.getStr(read, "authorityField");

        if (flg && StrUtil.isNotBlank(sql)) {
            qw.apply(sql);
        }
        if (!flg) {
            qw.apply(" 1=0 ");
        }
    }

    /**
     * 获取sql
     *
     * @param businessType
     * @param tablePre
     * @param authorityFields
     * @param isAssignFields
     * @return
     */
    public String getDataPermissionsSql(String businessType, String tablePre, String[] authorityFields, boolean isAssignFields) {
        String sql = null;
        UserCompany userCompany = companyUserInfo.get();
        Map read = getDataPermissionsForQw(userCompany.getCompanyCode(), userCompany.getUserId(), businessType, "read", tablePre, authorityFields, isAssignFields);
        boolean flg = MapUtil.getBool(read, "authorityState", false);
        String sqlTemp = MapUtil.getStr(read, "authorityField");
        if (flg && StrUtil.isNotBlank(sqlTemp)) {
            sql = sqlTemp;
        }
        if (!flg) {
            sql = " 1=0 ";
        }
        return sql;
    }

    /**
     * 获取数据权限
     *
     * @param businessType
     * @return
     * @see DataPermissionsBusinessTypeEnum
     */
    public <T> Map getDataPermissionsForQw(String companyCode, String uerId, String businessType, String operateType, String tablePre, String[] authorityFields, boolean isAssignFields) {
        Map<String, Object> ret = new HashMap<>();
        List<DataPermissionVO> dataPermissionsList = getDataPermissionKey(companyCode, uerId, businessType, operateType, ret);
        if (CollectionUtils.isEmpty(dataPermissionsList)) {
            return ret;
        }
        List<String> rangeList = dataPermissionsList.stream().map(DataPermissionVO::getRange).distinct().collect(Collectors.toList());
        //如果有一个用户组有全部可见 权限，则全部可见
        if(rangeList.contains(DataPermissionsRangeEnum.ALL.getK())){
            ret.put("authorityState",Boolean.TRUE);
            return ret;
        }
        //如果都是全部不可见,则直接返回false
        if (rangeList.size()==1 && rangeList.get(0).equals(DataPermissionsRangeEnum.ALL_INOPERABLE.getK())) {
            ret.put("authorityState",Boolean.FALSE);
            return ret;
        }
        if(tablePre == null){
            tablePre = "";
        }
        List<String> authorityField=new ArrayList<>();
        //用户存在多个用户组，用户组之间用or
        boolean userGroupSize = dataPermissionsList.stream().filter(o-> CollUtil.isNotEmpty(o.getFieldDataPermissions())).count() != 1;
        for (DataPermissionVO dataPermissions : dataPermissionsList) {
            if (DataPermissionsRangeEnum.ALL.getK().equals(dataPermissions.getRange())) {
                //如果该用户有一个角色是全部 权限时
                authorityField.clear();
                break;
            }
            List<FieldDataPermissionVO> fieldDataPermissions = dataPermissions.getFieldDataPermissions();
            if (CollectionUtils.isNotEmpty(fieldDataPermissions) && !fieldDataPermissions.isEmpty()) {
                //判断配置是否分组
                Map<Integer, List<FieldDataPermissionVO>> permissionMap = fieldDataPermissions.stream().collect(Collectors.groupingBy(s->Integer.parseInt(StrUtil.isNotEmpty(s.getGroupIdx())?s.getGroupIdx():"0")));
                boolean permissionGroup = permissionMap.size() != 1;

                List<String> fieldArr = new ArrayList<>();
                //判断是否第一次  如果是第一次 添加()  ，否者添加  or() ，如果只有一个用户组  不添加括号
                if(userGroupSize){
                    String sqlType = authorityField.isEmpty() ? " ( " : " or ( ";
                    fieldArr.add(sqlType);
                }
                List<Integer> permissionMapKeys = new ArrayList<>(permissionMap.keySet());
                permissionMapKeys.sort(Integer::compare);
                boolean first = true;
                boolean isFieldFlag = false;
                for (Integer entry : permissionMapKeys) {
                    List<FieldDataPermissionVO> value = permissionMap.get(entry);
                    value.sort(Comparator.comparingInt(s-> StrUtil.isNotEmpty(s.getSortIdx()) ? Integer.parseInt(s.getSortIdx()) : 0));
                    //如果配置只有一组，不添加括号
                    if(permissionGroup){
                        if(first){
                            fieldArr.add(" ( ");
                        }else{
                            String groupSelectType = value.get(0).getGroupSelectType();
                            if ("and".equals(groupSelectType)) {
                                fieldArr.add(" and ( ");
                            }else{
                                fieldArr.add(" or ( ");
                            }
                        }
                    }
                    first = false;
                    boolean sqlType = false;
                    for (FieldDataPermissionVO fieldDataPermissionVO : value) {
                        if (StringUtils.isNotBlank(fieldDataPermissionVO.getFieldName())) {
                            String fieldName = Objects.isNull(authorityFields) ? null : searchField(authorityFields, fieldDataPermissionVO.getFieldName());
                            if (isAssignFields && StringUtils.isBlank(fieldName)) {
                                continue;
                            }
                            if(sqlType){
                                fieldArr.add(DataPermissionsSelectTypeEnum.OR.getK().equals(fieldDataPermissionVO.getSelectType()) ? " or " : " and ");
                            }
                            sqlType = true;
                            isFieldFlag = true;
                            if(StrUtil.isBlank(fieldName)){
                                fieldName = (fieldDataPermissionVO.getFieldName().contains(".")) ? fieldDataPermissionVO.getFieldName() :  tablePre + fieldDataPermissionVO.getFieldName();
                            }
                            if("create_id_dept".equals(fieldDataPermissionVO.getFieldName())){
                                //创建人部门 做一下特殊处理，表中没有保存创建人部门，所以这里关联用户部门表来判断
                                //SQL:create_id in ( select user_id from c_amc_data.sys_user_dept where dept_id in ('0004','0811','0838','0839'))
                                fieldName = tablePre + "create_id";
                                String deptList = CollectionUtils.isEmpty(fieldDataPermissionVO.getFieldValues()) ? "()" : (fieldDataPermissionVO.getFieldValues().stream().collect(Collectors.joining("','", "('", "')")));
                                if (DataPermissionsConditionTypeEnum.IN.getK().equals(fieldDataPermissionVO.getConditionType()) || DataPermissionsConditionTypeEnum.EQ.getK().equals(fieldDataPermissionVO.getConditionType())) {
                                    fieldArr.add(fieldName + " in " + "( select user_id from c_amc_data.sys_user_dept where dept_id in " + deptList + ")");
                                } else {
                                    fieldArr.add(fieldName + " not in " + "( select user_id from c_amc_data.sys_user_dept where dept_id in " + deptList + ")");
                                }
                            }else {
                                if (DataPermissionsConditionTypeEnum.IN.getK().equals(fieldDataPermissionVO.getConditionType()) || DataPermissionsConditionTypeEnum.EQ.getK().equals(fieldDataPermissionVO.getConditionType())) {
                                    fieldArr.add(fieldName + " in " + (CollectionUtils.isEmpty(fieldDataPermissionVO.getFieldValues()) ? "()" : (fieldDataPermissionVO.getFieldValues().stream().collect(Collectors.joining("','", "('", "')")))));
                                } else {
                                    fieldArr.add(fieldName + " not in " + (CollectionUtils.isEmpty(fieldDataPermissionVO.getFieldValues()) ? "()" : (fieldDataPermissionVO.getFieldValues().stream().collect(Collectors.joining("','", "('", "')")))));
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(fieldDataPermissionVO.getSqlField())) {
                            fieldArr.add(fieldDataPermissionVO.getSqlField());
                        }
                    }
                    if(sqlType){
                        if(permissionGroup){
                            fieldArr.add(" ) ");
                        }
                    }else{
                        first = true;
                        if(CollUtil.isNotEmpty(fieldArr)){
                            fieldArr.remove(fieldArr.size()-1);
                        }
                    }
                }
                if(userGroupSize){
                    fieldArr.add(" ) ");
                }
                if(isFieldFlag){
                    authorityField.addAll(fieldArr);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(authorityField)) {
            String authorityFieldStr = "(" + StringUtils.join(authorityField, " ") + ")";
            ret.put("authorityField", authorityFieldStr);
        }
        return ret;
    }

    public List<DataPermissionVO> getDataPermissionKey(String companyCode, String uerId, String businessType, String operateType, Map<String, Object> ret) {
        String dataPermissionsKey = "USERISOLATION:" + companyCode + ":";
        dataPermissionsKey = dataPermissionsKey + businessType + ":";
        dataPermissionsKey += uerId + ":";
        ret.put("authorityState", Boolean.TRUE);
        ret.put("authorityField","");
        ret.put("dataPermissionsKey",dataPermissionsKey);
        return this.getDataPermissions(businessType,operateType);
    }


    private String searchField(String[] arr,String val){
        for (String s:arr) {
            if(!s.contains(":") && (s.endsWith("."+val) || s.equals(val))) {
                return s;
            }
            if(s.contains(":")){
                String[] ss=s.split(":");
                if(ss[1].equals(val)){
                    return ss[0];
                }
            }
            if(val.endsWith("."+s)){
                return s;
            }
        }
        return null;
    }
}
