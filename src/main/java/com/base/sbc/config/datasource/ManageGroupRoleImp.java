package com.base.sbc.config.datasource;

import com.base.sbc.client.amc.service.AmcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ManageGroupRoleImp {
    @Resource
    AmcService amcService;
    public Object userDataIsolation(String token, String companyCode, String isolation, String userId, String dataName) {
        return amcService.userDataIsolation(token,companyCode,isolation,userId,dataName);
    }
}
