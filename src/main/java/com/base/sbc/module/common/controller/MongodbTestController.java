package com.base.sbc.module.common.controller;

import cn.hutool.core.map.MapUtil;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.vo.UserInfoVo;
import io.swagger.annotations.Api;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "mongodb测试")
@RequestMapping(value = BaseController.SAAS_URL + "/mongodbTest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MongodbTestController {

    @Autowired
    MongoTemplate mongoTemplate;
    @RequestMapping("/test")
    public Object test(){
        String collectionName="c_attribute_value";
        List<Map> all = mongoTemplate.findAll(Map.class, collectionName);
        return all;
    }


}
