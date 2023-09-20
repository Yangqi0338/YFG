package com.base.sbc.client.amc.service;

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
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.SpringContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceAdapter.ResponseControllerAdvice.companyUserInfo;

@Component
public class DataPermissionsService {
    @Autowired
    private AmcService amcService;
    @Resource
    private RedisUtils redisUtils;

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
        getDataPermissionsForQw(qw, businessType, tablePre,null);
    }
    /**
     * @param qw           查询构造器
     * @param businessType 业务对象编码
     * @param tablePre     表别名
     * @param authorityFields     自定义数据隔离字段（代表名的）
     */
    public void getDataPermissionsForQw(QueryWrapper qw, String businessType, String tablePre, String[] authorityFields) {
        UserCompany userCompany = companyUserInfo.get();
        String dataPermissionsKey = "USERISOLATION:" + userCompany.getCompanyCode() + ":" + userCompany.getUserId() + ":";
        Map read = getDataPermissionsForQw(businessType, "read", tablePre,authorityFields, dataPermissionsKey);
        boolean flg = MapUtil.getBool(read, "authorityState", false);
        String sql = MapUtil.getStr(read, "authorityField");
        if (flg && StrUtil.isNotBlank(sql)) {
            qw.apply(sql);
        }
        if(!flg){
            qw.apply(" 1=0 ");
        }
    }
    /**
     * 获取数据权限
     *
     * @param businessType
     * @return
     * @see DataPermissionsBusinessTypeEnum
     */
    public <T> Map getDataPermissionsForQw(String businessType, String operateType, String tablePre, String[] authorityFields, String dataPermissionsKey) {
        //删除amc的数据权限状态
        RedisUtils redisUtils1=new RedisUtils();
        redisUtils1.setRedisTemplate(SpringContextHolder.getBean("redisTemplateAmc"));
        boolean redisType=false;
        if(redisUtils1.hasKey(dataPermissionsKey+"POWERSTATE")){
            redisType=true;
            redisUtils1.del(dataPermissionsKey+"POWERSTATE");
        }
        redisUtils1.setRedisTemplate(SpringContextHolder.getBean("redisTemplate"));
        if (redisType){
            redisUtils.removePattern(dataPermissionsKey);
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("authorityState", Boolean.TRUE);
        ret.put("authorityField","");
        List<DataPermissionVO> dataPermissionsList = null;
        if (!redisUtils.hasKey(dataPermissionsKey+operateType+"@" + businessType)) {
            dataPermissionsList = this.getDataPermissions(businessType,operateType);
            //默认开启角色的数据隔离
            redisUtils.set(dataPermissionsKey +operateType+"@" + businessType, dataPermissionsList, 60 * 3);//如数据的隔离3分钟更新一次
        } else {
            dataPermissionsList = (List<DataPermissionVO>) redisUtils.get(dataPermissionsKey +operateType+"@" + businessType);
        }
        if (CollectionUtils.isEmpty(dataPermissionsList)) {
            return ret;
        }
        AtomicReference<Integer> authorityState= new AtomicReference<>(0);
        AtomicBoolean isField= new AtomicBoolean(true);
        dataPermissionsList.forEach(e->{
            if (!DataPermissionsRangeEnum.ALL_INOPERABLE.getK().equals(e.getRange()) && authorityState.get()!=2) {
                authorityState.set(1);
            }
            if (DataPermissionsRangeEnum.ALL_INOPERABLE.getK().equals(e.getRange()) && DataPermissionsSelectTypeEnum.AND.getK().equals(e.getSelectType())) {
                authorityState.set(2);
            }
            if(CollectionUtils.isNotEmpty(e.getFieldDataPermissions())){
                isField.set(true);
            }
        });
        if (authorityState.get()!=1) {
            ret.put("authorityState",Boolean.FALSE);
            return ret;
        }
        if(tablePre == null || !isField.get()){
            return ret;
        }
        List<String> authorityField=new ArrayList<>();
        dataPermissionsList.forEach(dataPermissions->{
            if(!DataPermissionsRangeEnum.ALL_INOPERABLE.getK().equals(dataPermissions.getRange())){
                List<FieldDataPermissionVO> fieldDataPermissions=dataPermissions.getFieldDataPermissions();
                if (CollectionUtils.isNotEmpty(fieldDataPermissions)) {
                    final String[] sqlType = {authorityField.size()>0?DataPermissionsSelectTypeEnum.OR.getK().equals(dataPermissions.getSelectType()) ? " or ( " : " and ( ":" ( "};
                    authorityField.add(sqlType[0]);
                    fieldDataPermissions.forEach(fieldDataPermissionVO -> {
                        if(StringUtils.isNotBlank(fieldDataPermissionVO.getFieldName()) || StringUtils.isNotBlank(fieldDataPermissionVO.getSqlField())){
                            authorityField.add(!(authorityField.get(authorityField.size()-1).equals(sqlType[0]))?DataPermissionsSelectTypeEnum.OR.getK().equals(fieldDataPermissionVO.getSelectType())?" or ":" and ":" ");
                            sqlType[0] ="fromtype2339";
                        }

                        if(StringUtils.isNotBlank(fieldDataPermissionVO.getFieldName())){
                            String fieldName=searchField(authorityFields,fieldDataPermissionVO.getFieldName());
                            fieldName=(fieldDataPermissionVO.getFieldName().indexOf(".")!=-1)?fieldDataPermissionVO.getFieldName():StringUtils.isNotBlank(fieldName)?fieldName:tablePre+fieldDataPermissionVO.getFieldName();
                            if (DataPermissionsConditionTypeEnum.IN.getK().equals(fieldDataPermissionVO.getConditionType())) {
                                authorityField.add(fieldName+" in " + (CollectionUtils.isEmpty(fieldDataPermissionVO.getFieldValues())?"()":(fieldDataPermissionVO.getFieldValues().stream().collect(Collectors.joining("','", "('", "')")))));
                            }else {
                                String finalFieldName = fieldName;
                                if(fieldDataPermissionVO.getFieldValues().size()>1){
                                    authorityField.add(finalFieldName+" in (");
                                    final String[] fieldValues = {""};
                                    fieldDataPermissionVO.getFieldValues().forEach(e ->{
                                        fieldValues[0] +=(StringUtils.isNotBlank(fieldValues[0])?"','":" '")+e;
                                    });
                                    authorityField.add(fieldValues[0] +"') ");
                                }
                                if(fieldDataPermissionVO.getFieldValues().size()==1) authorityField.add(" "+finalFieldName+"='"+fieldDataPermissionVO.getFieldValues().get(0)+"' ");
                            }
                        }
                        if(StringUtils.isNotBlank(fieldDataPermissionVO.getSqlField())){
                            authorityField.add(fieldDataPermissionVO.getSqlField());
                        }
                    });
                    authorityField.add(" ) ");
                }
            }
        });

        if(CollectionUtils.isNotEmpty(authorityField)){
            ret.put("authorityField",StringUtils.join(authorityField, " "));
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
