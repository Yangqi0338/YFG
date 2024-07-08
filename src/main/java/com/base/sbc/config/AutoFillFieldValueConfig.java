package com.base.sbc.config;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.base.sbc.config.common.MFunc;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * mybatis-plus自动填充字段值配置
 *
 * @author 卞康
 * @date 2023/3/31 19:56:38
 */

@Component
public class AutoFillFieldValueConfig implements MetaObjectHandler {

    private static ThreadLocal<Map<String, List<MFunc<?, ?>>>> updateNullMapThreadLocal = new ThreadLocal<>();

    public static void setNull(String id, MFunc<?, ?> func) {
        Map<String, List<MFunc<?, ?>>> map = updateNullMapThreadLocal.get();
        if (map == null) map = new HashMap<>();
        List<MFunc<?, ?>> list = map.getOrDefault(id, new ArrayList<>());
        list.add(func);
        map.put(id, list);
        updateNullMapThreadLocal.set(map);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        UserCompany userCompany = userInfo();
        this.strictInsertFill(metaObject, "createDate", Date.class, new Date());
        this.strictInsertFill(metaObject, "createName", String.class, userCompany.getAliasUserName());
        this.strictInsertFill(metaObject, "createId", String.class, userCompany.getUserId());
        this.strictInsertFill(metaObject, "updateDate", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateName", String.class, userCompany.getAliasUserName());
        this.strictInsertFill(metaObject, "updateId", String.class, userCompany.getUserId());
        this.strictInsertFill(metaObject, "companyCode", String.class, userCompany.getCompanyCode());
        this.strictInsertFill(metaObject, "delFlag", String.class, "0");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        {
            UserCompany userCompany = userInfo();
            this.setFieldValByName("updateDate", new Date(),metaObject);
            this.setFieldValByName("updateName",  userCompany.getAliasUserName(),metaObject);
            this.setFieldValByName("updateId", userCompany.getUserId(),metaObject);
            Map<String, List<MFunc<?, ?>>> map = updateNullMapThreadLocal.get();
            if (MapUtil.isNotEmpty(map)) {
                String id = this.getFieldValByName("id", metaObject).toString();
                if (map.containsKey(id)) {
                    List<MFunc<?, ?>> funcList = map.get(id);
                    funcList.forEach(func -> {
                        this.setFieldValByName(func.getFieldName(), null, metaObject);
                    });
                    map.remove(id);
                }
            }
            updateNullMapThreadLocal.set(map);

//        this.strictUpdateFill(metaObject, "updateDate", Date.class,new Date());
//        this.strictUpdateFill(metaObject, "updateName", String.class, userCompany.getAliasUserName());
//        this.strictUpdateFill(metaObject, "updateId", String.class, userCompany.getUserId());
        }
    }

    private UserCompany userInfo() {
        UserCompany userCompany = 	companyUserInfo.get();
        if (userCompany == null) {
            userCompany =new UserCompany();
            userCompany.setAliasUserName("系统管理员");
            userCompany.setUserId("0");
            userCompany.setCompanyCode("0");
        }
        if (StringUtils.isEmpty(userCompany.getUserId()) || StringUtils.isEmpty(userCompany.getCompanyCode())) {
            throw new RuntimeException("非法操作，当前登录用户信息不完整");
        }
        return userCompany;
    }
}
