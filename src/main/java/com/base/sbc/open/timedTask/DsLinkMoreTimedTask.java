package com.base.sbc.open.timedTask;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.open.dto.OpenMaterialDto;
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
    private DsLinkMoreScm linkMoreScm;

//    @Scheduled(cron = "10 * * * * ?")
    public void materialTask(){
        List<OpenMaterialDto> purchaseMaterialList = materialService.getMaterialList("677447590605750272");
        logger.info("需要同步的物料 :{}", JSON.toJSONString(purchaseMaterialList));

        List<OpenMaterialDto> errorList = new ArrayList<>();
        for (OpenMaterialDto materialDto : purchaseMaterialList) {
            HttpResponse authTokenOrSign = linkMoreScm.sendToLinkMore("v1/BaseInfo/updatematerial", JSON.toJSONString(materialDto));
            String body = authTokenOrSign.body();
            logger.info(materialDto + "物料上传领猫返回信息 :{}", body);
            if (StringUtils.isNotBlank(body)){
                JSONObject jsonObject = JSONObject.parseObject(body);
                try {
                    int code = jsonObject.getInteger("code");
                    if (code != 200){
                        errorList.add(materialDto);
                    }
                }catch (Exception e){
                    errorList.add(materialDto);
                }
            }else{
                errorList.add(materialDto);
            }
        }
        if (errorList.size() > 0){
            logger.info("同步失败的物料 :{}", JSON.toJSONString(errorList));
        }
    }

}
