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
        Map read = getDataPermissionsForQw(userCompany.getCompanyCode(),userCompany.getUserId(),businessType, "read", tablePre,authorityFields,isAssignFields);
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
    public <T> Map getDataPermissionsForQw(String companyCode,String uerId,String businessType, String operateType, String tablePre, String[] authorityFields, boolean isAssignFields) {
        String dataPermissionsKey = "USERISOLATION:" + companyCode + ":" +businessType+":";
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
        dataPermissionsKey += uerId + ":";
        Map<String, Object> ret = new HashMap<>();
        ret.put("authorityState", Boolean.TRUE);
        ret.put("authorityField","");
        ret.put("dataPermissionsKey",dataPermissionsKey);
        List<DataPermissionVO> dataPermissionsList = null;
        if (!redisUtils.hasKey(dataPermissionsKey+operateType)) {
            dataPermissionsList = this.getDataPermissions(businessType,operateType);
            //默认开启角色的数据隔离
            redisUtils.set(dataPermissionsKey +operateType, dataPermissionsList, -1);//如数据的隔离不失效
        } else {
            dataPermissionsList = (List<DataPermissionVO>) redisUtils.get(dataPermissionsKey +operateType);
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
        datafor:for(DataPermissionVO dataPermissions:dataPermissionsList){
            if(!DataPermissionsRangeEnum.ALL_INOPERABLE.getK().equals(dataPermissions.getRange())){
                List<FieldDataPermissionVO> fieldDataPermissions=dataPermissions.getFieldDataPermissions();
                if (CollectionUtils.isNotEmpty(fieldDataPermissions) && fieldDataPermissions.size()>0) {
                    List<String> fieldArr=new ArrayList<>();
                    boolean isFieldFlag=false;
                    final String[] sqlType = {fieldArr.size()>0?DataPermissionsSelectTypeEnum.OR.getK().equals(dataPermissions.getSelectType()) ? " or ( " : " and ( ":" ( "};
                    fieldArr.add(sqlType[0]);
                    fieldfor:for(FieldDataPermissionVO fieldDataPermissionVO:fieldDataPermissions){
                        if(StringUtils.isNotBlank(fieldDataPermissionVO.getFieldName()) || StringUtils.isNotBlank(fieldDataPermissionVO.getSqlField())){
                            fieldArr.add(!(fieldArr.get(fieldArr.size()-1).equals(sqlType[0]))?DataPermissionsSelectTypeEnum.OR.getK().equals(fieldDataPermissionVO.getSelectType())?" or ":" and ":" ");
                            sqlType[0] ="fromtype2339";
                        }
                        if(StringUtils.isNotBlank(fieldDataPermissionVO.getFieldName())){
                            String fieldName=Objects.isNull(authorityFields)?null:searchField(authorityFields,fieldDataPermissionVO.getFieldName());
                            if(isAssignFields && StringUtils.isBlank(fieldName)){
                                fieldArr.remove(fieldArr.size()-1);
                                sqlType[0] =fieldArr.get(fieldArr.size()-1);
                                continue fieldfor;
                            }
                            isFieldFlag=true;
                            fieldName=(fieldDataPermissionVO.getFieldName().indexOf(".")!=-1)?fieldDataPermissionVO.getFieldName():StringUtils.isNotBlank(fieldName)?fieldName:tablePre+fieldDataPermissionVO.getFieldName();
                            if (DataPermissionsConditionTypeEnum.IN.getK().equals(fieldDataPermissionVO.getConditionType())) {
                                fieldArr.add(fieldName+" in " + (CollectionUtils.isEmpty(fieldDataPermissionVO.getFieldValues())?"()":(fieldDataPermissionVO.getFieldValues().stream().collect(Collectors.joining("','", "('", "')")))));
                            }else {
                                String finalFieldName = fieldName;
                                if(fieldDataPermissionVO.getFieldValues().size()>1){
                                    fieldArr.add(finalFieldName+" in (");
                                    final String[] fieldValues = {""};
                                    fieldDataPermissionVO.getFieldValues().forEach(e ->{
                                        fieldValues[0] +=(StringUtils.isNotBlank(fieldValues[0])?"','":" '")+e;
                                    });
                                    fieldArr.add(fieldValues[0] +"') ");
                                }
                                if(fieldDataPermissionVO.getFieldValues().size()==1) fieldArr.add(" "+finalFieldName+"='"+fieldDataPermissionVO.getFieldValues().get(0)+"' ");
                            }
                        }
                        if(StringUtils.isNotBlank(fieldDataPermissionVO.getSqlField())){
                            fieldArr.add(fieldDataPermissionVO.getSqlField());
                        }
                    };
                    if(isFieldFlag){
                        fieldArr.add(" ) ");
                        authorityField.addAll(fieldArr);
                    }
                }
            }
        };

        if(CollectionUtils.isNotEmpty(authorityField)){
            ret.put("authorityField",StringUtils.join(authorityField, " "));
        }
        return ret;
    }


    private String searchField(String[] arr,String val){
        for (String s:arr) {
            if(s.indexOf(":")==-1 && (s.endsWith("."+val) || s.equals(val))) return s;
            if(s.indexOf(":")!=-1){
                String[] ss=s.split(":");
                if(ss[1].equals(val)){
                    return ss[0];
                }
            }
        }
        return null;
    }
}
