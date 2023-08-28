package com.base.sbc.open.timedTask;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.open.dto.OpenMaterialDto;
import com.base.sbc.open.dto.OpenStyleDto;
import com.base.sbc.open.service.OpenMaterialService;
import com.base.sbc.open.thirdToken.DsLinkMoreScm;
import org.commonmark.node.Code;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/18 10:58
 */
@EnableScheduling
@Component
public class DsLinkMoreTimedTask {

    protected final Logger logger = LoggerFactory.getLogger(DsLinkMoreTimedTask.class);

    @Autowired
    private OpenMaterialService materialService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private DsLinkMoreScm linkMoreScm;

    /**
     * 物料定时同步到领猫scm
     */
//    @Scheduled(cron = "10 * * * * ?")
    public void materialTask(){
        List<OpenMaterialDto> purchaseMaterialList = materialService.getMaterialList("677447590605750272");
        logger.info("需要同步的物料 :{}", JSON.toJSONString(purchaseMaterialList));

        List<String> errorList = new ArrayList<>();
        for (OpenMaterialDto materialDto : purchaseMaterialList) {
            HttpResponse authTokenOrSign = linkMoreScm.sendToLinkMore("v1/BaseInfo/updatematerial", JSON.toJSONString(materialDto));
            String body = authTokenOrSign.body();
            logger.info(materialDto.getMtCode() + "物料上传领猫返回信息 :{}", body);
            errorList = linkMoreScm.checkAndReturn(errorList,body,materialDto.getMtCode());
        }
        if (errorList.size() > 0){
            logger.info("同步失败的物料 :{}", StringUtils.convertListToString(errorList));
        }
    }

    /**
     * 款式（基础信息）定时同步领猫scm
     */
//    @Scheduled(cron = "10 * * * * ?")
    public void styleTask(){
        List<OpenStyleDto> styleDtoList = styleService.getStyleListForLinkMore("677447590605750272");
        logger.info("需要同步的款式资料包基础信息 :{}", JSON.toJSONString(styleDtoList));

        List<String> errorList = new ArrayList<>();
        for (OpenStyleDto style : styleDtoList) {
            HttpResponse authTokenOrSign = linkMoreScm.sendToLinkMore("v1/product/updateproduct", JSON.toJSONString(style));
            String body = authTokenOrSign.body();
            logger.info(style.getCode() + "款式上传领猫返回信息 :{}", body);
            errorList = linkMoreScm.checkAndReturn(errorList,body,style.getCode());
        }
        if (errorList.size() > 0){
            logger.info("同步失败的款式 :{}", StringUtils.convertListToString(errorList));
        }
    }

}
