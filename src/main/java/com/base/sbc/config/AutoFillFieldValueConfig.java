package com.base.sbc.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.base.sbc.config.common.base.UserCompany;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.base.sbc.config.adviceadapter.ResponseControllerAdvice.companyUserInfo;

/**
 * mybatis-plus自动填充字段值配置
 *
 * @author 卞康
 * @date 2023/3/31 19:56:38
 */

@Component
public class AutoFillFieldValueConfig implements MetaObjectHandler {
    String userName;
    String userId;
    String companyCode;

    @Override
    public void insertFill(MetaObject metaObject) {
        userInfo();
        this.strictInsertFill(metaObject, "createDate", Date.class, new Date());
        this.strictInsertFill(metaObject, "createName", String.class, userName);
        this.strictInsertFill(metaObject, "createId", String.class, userId);
        this.strictInsertFill(metaObject, "updateDate", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateName", String.class, userName);
        this.strictInsertFill(metaObject, "updateId", String.class, userId);
        this.strictInsertFill(metaObject, "companyCode", String.class, companyCode);
        this.strictInsertFill(metaObject, "delFlag", String.class, "0");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        userInfo();
        this.setFieldValByName("updateDate", new Date(),metaObject);
        this.strictInsertFill(metaObject, "updateName", String.class, userName);
        this.strictInsertFill(metaObject, "updateId", String.class, userId);
    }

    private void userInfo() {
        UserCompany userCompany = 	companyUserInfo.get();
        if (userCompany == null) {
            this.userName = "系统管理员";
            this.userId = "0";
            this.companyCode = "0";
        } else {
            this.userName = userCompany.getAliasUserName();
            this.userId = userCompany.getUserId();
            this.companyCode = userCompany.getCompanyCode();
        }
    }
}
