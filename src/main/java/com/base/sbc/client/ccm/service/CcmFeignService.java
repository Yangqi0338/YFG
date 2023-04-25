package com.base.sbc.client.ccm.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.config.constant.BaseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 类描述：ccm 远程调用
 * @address com.base.sbc.client.ccm.service.CcmFeignService
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 16:36
 * @version 1.0
 */
@Service
public class CcmFeignService {

    @Autowired
    private CcmService ccmService;

    public List<BasicStructureTreeVo> findStructureTreeByCategoryIds(String categoryIds){
        String str = ccmService.findStructureTreeByCategoryIds(categoryIds);
        if(StrUtil.isBlank(str)){
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if(jsonObject.getBoolean(BaseConstant.SUCCESS)){
            List<BasicStructureTreeVo> data = jsonObject.getJSONArray("data").toJavaList(BasicStructureTreeVo.class);
            return data;
        }
        return null;
    }
}
