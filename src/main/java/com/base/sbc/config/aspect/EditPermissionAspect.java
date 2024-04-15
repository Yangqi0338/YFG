package com.base.sbc.config.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.enums.DataPermissionsRangeEnum;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.client.amc.vo.DataPermissionVO;
import com.base.sbc.client.amc.vo.FieldDataPermissionVO;
import com.base.sbc.config.annotation.EditPermission;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.redis.RedisAmcUtils;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.ReflectUtil;
import com.base.sbc.config.vo.EditPermissionReturnVo;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class EditPermissionAspect {

    @Autowired
    private AmcService amcService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RedisAmcUtils redisAmcUtils;
    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Pointcut("@annotation(com.base.sbc.config.annotation.EditPermission)")
    public void infoScopePointCut() {
    }

    @AfterReturning(pointcut = "infoScopePointCut()", returning = "rvt")
    public void afterFunction(JoinPoint point, Object rvt) throws Exception {
        handleDataScope(point, rvt);
    }

    private void handleDataScope(JoinPoint point, Object rvt) throws Exception {
        if (!ObjectUtils.isEmpty(rvt)) {
            List<Object> objectList = getObjectList(rvt);
            EditPermission annotationLog = getAnnotationLog(point);
            if (CollUtil.isNotEmpty(objectList) && annotationLog != null && annotationLog.type() != null) {
                //获取到该数据权限-可编辑权限
                DataPermissionsBusinessTypeEnum type = annotationLog.type();
                UserCompany userCompany = companyUserInfo.get();
                Map<String, Object> ret = new HashMap<>();
                List<DataPermissionVO> dataPermissionsList = dataPermissionsService.getDataPermissionKey(userCompany.getCompanyCode(), userCompany.getUserId(), type.getK(), "write", ret);
                if(CollUtil.isEmpty(dataPermissionsList)){
                    return;
                }

                for (Object o : objectList) {
                    EditPermissionReturnVo editPermissionReturnVo = (EditPermissionReturnVo) o;
                    boolean hasEditPermission = false;

                    //第一个循环是按照用户组分组，多个用户组之间用or
                    a:
                    for (DataPermissionVO dataPermissions : dataPermissionsList) {
                        if (!DataPermissionsRangeEnum.ALL_INOPERABLE.getK().equals(dataPermissions.getRange())) {
                            List<FieldDataPermissionVO> fieldDataPermissions = dataPermissions.getFieldDataPermissions();
                            Map<String, List<FieldDataPermissionVO>> permissionMap = fieldDataPermissions.stream().collect(Collectors.groupingBy(FieldDataPermissionVO::getGroupIdx));
                            //组间 结果集
                            Boolean lastGroupEdit = null;
                            for (Map.Entry<String, List<FieldDataPermissionVO>> entry : permissionMap.entrySet()) {

                                List<FieldDataPermissionVO> value = entry.getValue();
                                String groupSelectType = value.get(0).getGroupSelectType();
                                //组内 结果集
                                Boolean lastEdit = null;
                                for (FieldDataPermissionVO fieldDataPermissionVO : value) {
                                    boolean hasEdit = hasEditPermission(o, fieldDataPermissionVO);
                                    if (lastEdit == null) {
                                        lastEdit = hasEdit;
                                    } else {
                                        String conditionType = fieldDataPermissionVO.getConditionType();
                                        if ("and".equals(conditionType)) {
                                            lastEdit = lastEdit && hasEdit;
                                        } else {
                                            if (lastEdit) {
                                                lastGroupEdit = true;
                                                break;
                                            } else {
                                                lastEdit = hasEdit;
                                            }
                                        }
                                    }
                                }
                                if (lastGroupEdit == null) {
                                    lastGroupEdit = lastEdit;
                                } else {
                                    if ("and".equals(groupSelectType)) {
                                        lastGroupEdit = lastGroupEdit && lastEdit;
                                    } else {
                                        if (lastGroupEdit) {
                                            hasEditPermission = true;
                                            break a;
                                        } else {
                                            lastGroupEdit = lastEdit;
                                        }
                                    }
                                }
                            }
                            if(Boolean.TRUE.equals(lastGroupEdit)){
                                hasEditPermission = true;
                                break;
                            }
                        }
                    }
                    editPermissionReturnVo.setIsEdit(hasEditPermission ? 0 : 1);
                }
            }
        }
    }

    //递归判断是否符合权限
    private static boolean hasEditPermission(Object o, FieldDataPermissionVO fieldDataPermissionVO) {
        //权限字段
        String fieldName = fieldDataPermissionVO.getFieldName();
        if (fieldName.contains(".")) {
            fieldName = fieldName.split("\\.")[1];
        }
        if(fieldName.contains("_")){
            fieldName = StrUtil.toCamelCase(fieldName);
        }
        //权限值
        List<String> fieldValues = fieldDataPermissionVO.getFieldValues();

        String fieldValue = (String) ReflectUtil.getFieldValue(o, fieldName);
        String conditionType = fieldDataPermissionVO.getConditionType();

        boolean isEdit = fieldValues.contains(fieldValue);
        if ("not in".equals(conditionType) || "!=".equals(conditionType)) {
            return !isEdit;
        }
        return isEdit;
    }

    private List<Object> getObjectList(Object rvt) {
        List<Object> list = new ArrayList<>();
        if (rvt instanceof ArrayList<?>) {
            list.addAll((List<?>) rvt);
        } else if (rvt instanceof PageInfo<?>) {
            List<?> records = ((PageInfo<?>) rvt).getList();
            list.addAll(records);
        } else {
            list.add(rvt);
        }
        list.removeAll(Collections.singleton(null));
        return list;
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private EditPermission getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(EditPermission.class);
        }
        return null;
    }

}
