package com.base.sbc.client.amc.service;

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
import com.base.sbc.config.exception.OtherException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DataPermissionsService {
    @Autowired
    private AmcService amcService;

    /**
     * 获取数据权限
     *
     * @param businessType
     * @return
     * @see DataPermissionsBusinessTypeEnum
     */
    public DataPermissionVO getDataPermissions(String businessType,String operateType) {
        if (StringUtils.isEmpty(businessType)) {
            throw new OtherException("入参不可为空");
        }
        ApiResult apiResult = amcService.getReadDataPermissions(businessType,operateType);
        if (Objects.isNull(apiResult)) {
            throw new OtherException("获取用户数据权限异常");
        }
        return JSONArray.parseObject(JSON.toJSONString(apiResult.getData()), DataPermissionVO.class);
    }

    /**
     * 获取数据权限
     *
     * @param businessType
     * @return
     * @see DataPermissionsBusinessTypeEnum
     */
    public <T> Map getDataPermissionsForQw(String businessType,String operateType, String tablePre,String[] authorityFields) {
        Map<String,Object> ret=new HashMap<>();
        ret.put("authorityState",Boolean.TRUE);
        ret.put("authorityField","");
        DataPermissionVO dataPermissions = this.getDataPermissions(businessType,operateType);
        if (Objects.isNull(dataPermissions)) {
            return ret;
        }

        if (!DataPermissionsRangeEnum.CUSTOM.getK().equals(dataPermissions.getRange()) && DataPermissionsRangeEnum.ALL_INOPERABLE.getK().equals(dataPermissions.getRange())) {
            ret.put("authorityState",Boolean.FALSE);
            return ret;
        }
        if(tablePre == null){
            return ret;
        }
        List<FieldDataPermissionVO> fieldDataPermissions = dataPermissions.getFieldDataPermissions();
        if (CollectionUtils.isEmpty(fieldDataPermissions)) {
            return ret;
        }
        final String[] authorityField = {""};
        if (DataPermissionsSelectTypeEnum.AND.getK().equals(dataPermissions.getSelectType())) {
            fieldDataPermissions.forEach(fieldDataPermissionVO -> {
                authorityField[0] +=StringUtils.isNotBlank(authorityField[0])?" and ":" ";
                if(StringUtils.isNotBlank(fieldDataPermissionVO.getFieldName())){
                    String fieldName=searchField(authorityFields,fieldDataPermissionVO.getFieldName());
                    fieldName=(fieldDataPermissionVO.getFieldName().indexOf(".")!=-1)?fieldDataPermissionVO.getFieldName():StringUtils.isNotBlank(fieldName)?fieldName:tablePre+fieldDataPermissionVO.getFieldName();
                    if (DataPermissionsConditionTypeEnum.IN.getK().equals(fieldDataPermissionVO.getConditionType())) {
                        authorityField[0] += fieldName+" in " + (CollectionUtils.isEmpty(fieldDataPermissionVO.getFieldValues())?"()":(fieldDataPermissionVO.getFieldValues().stream().collect(Collectors.joining("','", "('", "')"))));
                    }else {
                        String finalFieldName = fieldName;
                        fieldDataPermissionVO.getFieldValues().forEach(e ->authorityField[0] += finalFieldName +"='"+e+"'");
                    }
                }
                if(StringUtils.isNotBlank(fieldDataPermissionVO.getSqlField())){
                    authorityField[0] +=fieldDataPermissionVO.getSqlField();
                }
            });
        }
        if (DataPermissionsSelectTypeEnum.OR.getK().equals(dataPermissions.getSelectType())) {
            fieldDataPermissions.forEach(fieldDataPermissionVO -> {
                authorityField[0] +=StringUtils.isNotBlank(authorityField[0])?" or ":" ";
                if(StringUtils.isNotBlank(fieldDataPermissionVO.getFieldName())){
                    String fieldName=searchField(authorityFields,fieldDataPermissionVO.getFieldName());
                    fieldName=(fieldDataPermissionVO.getFieldName().indexOf(".")!=-1)?fieldDataPermissionVO.getFieldName():StringUtils.isNotBlank(fieldName)?fieldName:tablePre+fieldDataPermissionVO.getFieldName();
                    if (DataPermissionsConditionTypeEnum.IN.getK().equals(fieldDataPermissionVO.getConditionType())) {
                        authorityField[0] += fieldName+" in " + (CollectionUtils.isEmpty(fieldDataPermissionVO.getFieldValues())?"()":(fieldDataPermissionVO.getFieldValues().stream().collect(Collectors.joining("','", "('", "')"))));
                    }else {
                        String finalFieldName = fieldName;
                        fieldDataPermissionVO.getFieldValues().forEach(e ->authorityField[0] += finalFieldName +"='"+e+"'");
                    }
                }
                if(StringUtils.isNotBlank(fieldDataPermissionVO.getSqlField())){
                    authorityField[0] +=fieldDataPermissionVO.getSqlField();
                }
            });
        }
        if(StringUtils.isNotBlank(authorityField[0])){
            ret.put("authorityField",authorityField[0]);
        }
        return ret;
    }
    private String searchField(String[] arr,String val){
        for (String s:arr) {
            if(s.indexOf(val)!=-1){
                return s;
            }
        }
        return null;
    }
}
