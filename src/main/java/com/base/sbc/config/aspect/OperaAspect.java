package com.base.sbc.config.aspect;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.common.annotation.DataIsolation;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.SpElParseUtil;
import com.base.sbc.config.utils.SpringContextHolder;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author 卞康
 * @date 2023/6/17 15:53:36
 * @mail 247967116@qq.com
 * 操作日志切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class OperaAspect {

    private final ApplicationContext applicationContext;

    private final OperaLogService operaLogService;
    /**
     * 单个修改保存操作日志切面
     * 新增 在controller层保存返回对象时单据id取返回对象id,
     * 修改 取方法参数的id属性
     *
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.base.sbc.config.annotation.OperaLog)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;

        OperaLog operaLog = getOperaLog(joinPoint);

        OperaLogEntity operaLogEntity = new OperaLogEntity();
        operaLogEntity.setPath(SpElParseUtil.generateKeyBySpEL(operaLog.pathSpEL(), joinPoint));
        operaLogEntity.setParentId(SpElParseUtil.generateKeyBySpEL(operaLog.parentIdSpEl(), joinPoint));
        if (StrUtil.isNotBlank(operaLog.valueSpEL())) {
            operaLogEntity.setName(SpElParseUtil.generateKeyBySpEL(operaLog.valueSpEL(), joinPoint));
        } else {
            operaLogEntity.setName(operaLog.value());
        }

        // 获取传入数据
        Object[] args = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder();

        JSONArray jsonArray =new JSONArray();
        // 获取操作类型
        if (operaLog.operationType() == OperationType.INSERT_UPDATE) {
            String documentId = "";
            Object pageDto = args[0];

            boolean isNew = false;
            for (Object arg : args) {
                if (null != arg) {
                    String idVal = BeanUtil.getProperty(arg, operaLog.idKey());
                    JSONObject fieldJson = CommonUtils.getFieldJson(arg);
                    if (idVal == null || CommonUtils.isInitId(idVal)) {
                        isNew = true;
                        operaLogEntity.setType("新增");
                        stringBuilder.append("新增");
                        stringBuilder.append(CommonUtils.newStr(fieldJson, pageDto));
                    } else {
                        documentId = BeanUtil.getProperty(pageDto, operaLog.idKey());
                        operaLogEntity.setType("修改");
                        Class<?> service = operaLog.service();
                        IService<?> bean = (IService<?>) applicationContext.getBean(service);
                        Object dbData = bean.getById(documentId);
                        stringBuilder.append("修改");
                        stringBuilder.append(CommonUtils.updateStr(dbData, pageDto, fieldJson,jsonArray));

                    }
                    operaLogEntity.setContent(stringBuilder.toString());
                    operaLogEntity.setJsonContent(jsonArray.toJSONString());
                }
            }

            proceed = joinPoint.proceed();
            //新增时获取id
            if (isNew && !ObjectUtil.isBasicType(proceed)) {
                documentId = BeanUtil.getProperty(proceed, "id");

            }
            operaLogEntity.setDocumentId(documentId);
            operaLogEntity.setDocumentName(BeanUtil.getProperty(pageDto, "name"));
        } else {
            //说明是删除操作
            String documentId = "";
            proceed = joinPoint.proceed();
            if (StrUtil.isNotBlank(operaLog.delIdSpEL())) {
                documentId = SpElParseUtil.generateKeyBySpEL(operaLog.delIdSpEL(), joinPoint);
            } else {
                Object[] arg1 = (Object[]) args[0];
                ArrayList<String> arrayList = new ArrayList<>();
                for (Object arg : arg1) {
                    arrayList.add((String) arg);
                }
                documentId = String.join(",", arrayList);
            }
            operaLogEntity.setType("删除");
            operaLogEntity.setDocumentId(documentId);
            operaLogEntity.setContent("删除id：{" + operaLogEntity.getDocumentId() + "}");
        }

        // 记录操作日志
        operaLogService.save(operaLogEntity);
        return proceed;
    }


    private OperaLog getOperaLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(OperaLog.class);
    }


}
