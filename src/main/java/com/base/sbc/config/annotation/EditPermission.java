package com.base.sbc.config.annotation;

import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EditPermission {

    DataPermissionsBusinessTypeEnum type();

}
