package com.base.sbc.module.workbench.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.redis.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 类描述：
 * @address com.base.sbc.module.workbench.controller.WorkbenchController
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-15 16:56
 * @version 1.0
 */
@RestController
@Api(tags = "工作台")
@RequestMapping(value = BaseController.SAAS_URL + "/workbench", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WorkbenchController {
    String redisKey="WORKBENCH:";
    @Autowired
    RedisUtils redisUtils;

    @ApiOperation(value = "加载配置", notes = "")
    @GetMapping
    public List get(@RequestHeader(BaseController.USER_ID) String userId){
        String userRedisKey=redisKey+userId;
        Object o = redisUtils.get(userRedisKey);
        if(ObjectUtil.isNotEmpty(o)){
            return JSON.parseArray(o.toString());
        }else{
            return CollUtil.newArrayList();
        }
    }

    @ApiOperation(value = "保存配置", notes = "")
    @PostMapping
    public boolean save(@RequestHeader(BaseController.USER_ID) String userId,@RequestBody List<Map<String,Object>> list){
        if(CollUtil.isNotEmpty(list)){
            String userRedisKey=redisKey+userId;
            // 一年失效
            redisUtils.set(userRedisKey,JSON.toJSONString(list),1*60*60*24*365);
        }
        return true;
    }

    @ApiOperation(value = "删除配置", notes = "")
    @DeleteMapping
    public boolean del(@RequestHeader(BaseController.USER_ID) String userId){
        String userRedisKey=redisKey+userId;
        redisUtils.del(userRedisKey);
        return true;
    }
}
