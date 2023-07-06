package com.base.sbc.config.aspect;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.config.annotation.OperaLog;
import com.base.sbc.config.enums.OperationType;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.SpElParseUtil;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.operaLog.service.OperaLogService;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
     * 操作日志切面
     * 新增 在controller层保存返回对象时单据id取返回对象id,
     * 修改 去方法参数的id属性
     *
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.base.sbc.config.annotation.OperaLog)")
    public Object logMethod(ProceedingJoinPoint joinPoint) {
        Object proceed = null;
        try {

            OperaLog operaLog = getOperaLog(joinPoint);

            OperaLogEntity operaLogEntity = new OperaLogEntity();
            if (operaLog.SqEL()) {
                operaLogEntity.setName(SpElParseUtil.generateKeyBySpEL(operaLog.value(), joinPoint));
            } else {
                operaLogEntity.setName(operaLog.value());
            }
            // 获取传入数据
            Object[] args = joinPoint.getArgs();
            StringBuilder stringBuilder = new StringBuilder();
            // 获取操作类型
            if (operaLog.operationType() == OperationType.INSERT_UPDATE) {
                String documentId = "";
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(args[0]));
                boolean isNew = false;
                for (Object arg : args) {
                    if (null != arg) {
                        TableInfo tableInfo = TableInfoHelper.getTableInfo(arg.getClass());
                        Object idVal = tableInfo.getPropertyValue(arg, tableInfo.getKeyProperty());
                        JSONObject fieldJson = this.getFieldJson(arg);

                        if (idVal == null || CommonUtils.isInitId(idVal.toString())) {
                            isNew = true;
                            operaLogEntity.setType("新增");
                            stringBuilder.append("新增{");
                            ArrayList<String> arrayList = new ArrayList<>(fieldJson.keySet());
                            for (int i = 0; i < arrayList.size(); i++) {
                                stringBuilder.append(arrayList.get(i)).append(":").append(jsonObject.get(fieldJson.getString(arrayList.get(i))));
                                if (arrayList.size() - i > 1) {
                                    stringBuilder.append(";");
                                } else {
                                    stringBuilder.append("}");
                                }
                            }

                        } else {
                            documentId = jsonObject.getString("id");
                            operaLogEntity.setType("修改");
                            Class<?> service = operaLog.service();
                            IService<?> bean = (IService<?>) applicationContext.getBean(service);
                            Object o = bean.getById(jsonObject.getString("id"));
                            JSONObject jsonObject1 = JSON.parseObject(JSON.toJSONString(o));
                            stringBuilder = this.contrast(jsonObject1, jsonObject, fieldJson);

                        }
                        operaLogEntity.setContent(stringBuilder.toString());
                    }
                }

                proceed = joinPoint.proceed();
                //新增是获取id
                if (isNew && !ObjectUtil.isBasicType(proceed)) {
                    documentId = BeanUtil.getProperty(proceed, "id");
                }
                operaLogEntity.setDocumentId(documentId);
                operaLogEntity.setDocumentName(jsonObject.getString("name"));
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
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return proceed;
    }

    private OperaLog getOperaLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(OperaLog.class);
    }

    /**
     * 获取所有ApiModelProperty注解字段
     */
    private JSONObject getFieldJson(Object model) {
        Class<?> clazz = model.getClass();
        Field[] declaredFields = ReflectUtil.getFields(clazz);
//        Field[] fields = ReflectUtil.getFields(clazz);
        JSONObject jsonObject = new JSONObject();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                String value = annotation.value();
                value = value.split("[:：(（;]")[0];
                jsonObject.put(value, field.getName());
            }
        }
        return jsonObject;
    }

    /**
     * 比较变更字段
     *
     * @param oldObj
     * @param newObj
     * @return
     */
    private StringBuilder contrast(JSONObject oldObj, JSONObject newObj, JSONObject fieldJson) {
        ArrayList<String> arrayList = new ArrayList<>(fieldJson.keySet());

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            String name = arrayList.get(i);
            String key = fieldJson.getString(name);
            Object oldStr = oldObj.get(key);
            Object newStr = newObj.get(key);
            if (StringUtils.isEmpty(oldStr) && StringUtils.isEmpty(newStr)) {
                continue;
            }
            if (newStr.equals(oldStr)) {
                continue;
            }
            stringBuilder.append(name).append(":");
            stringBuilder.append(oldStr).append("->").append(newStr);

            stringBuilder.append(";");
        }
        return stringBuilder;
    }

}
