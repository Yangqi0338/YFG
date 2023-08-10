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

import java.util.List;
import java.util.Objects;

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
    public DataPermissionVO getDataPermissions(String businessType) {
        if (StringUtils.isEmpty(businessType)) {
            throw new OtherException("入参不可为空");
        }
        ApiResult apiResult = amcService.getReadDataPermissions(businessType);
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
    public <T> Boolean getDataPermissionsForQw(String businessType, QueryWrapper<T> qw) {
        DataPermissionVO dataPermissions = this.getDataPermissions(businessType);
        if (Objects.isNull(dataPermissions)) {
            return Boolean.FALSE;
        }

        if (!DataPermissionsRangeEnum.CUSTOM.getK().equals(dataPermissions.getRange())) {
            return DataPermissionsRangeEnum.ALL_INOPERABLE.getK().equals(dataPermissions.getRange());
        }
        List<FieldDataPermissionVO> fieldDataPermissions = dataPermissions.getFieldDataPermissions();
        if (CollectionUtils.isEmpty(fieldDataPermissions)) {
            return Boolean.FALSE;
        }

        if (DataPermissionsSelectTypeEnum.AND.getK().equals(dataPermissions.getSelectType())) {
            fieldDataPermissions.forEach(fieldDataPermissionVO -> {
                if (DataPermissionsConditionTypeEnum.IN.getK().equals(fieldDataPermissionVO.getConditionType())) {
                    qw.in(fieldDataPermissionVO.getFieldName(), fieldDataPermissionVO.getFieldValues());
                    return;
                }
                fieldDataPermissionVO.getFieldValues().forEach(e -> qw.eq(fieldDataPermissionVO.getFieldName(), e));
            });
            return Boolean.FALSE;
        }

        qw.and(wrapper -> fieldDataPermissions.forEach(fieldDataPermissionVO -> {
            if (DataPermissionsConditionTypeEnum.IN.getK().equals(fieldDataPermissionVO.getConditionType())) {
                wrapper.or().in(fieldDataPermissionVO.getFieldName(), fieldDataPermissionVO.getFieldValues());
                return;
            }
            fieldDataPermissionVO.getFieldValues().forEach(fieldValue -> wrapper.or().eq(fieldDataPermissionVO.getFieldName(), fieldValue));
        }));
        return Boolean.FALSE;
    }
}
