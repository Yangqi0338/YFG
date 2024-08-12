/*
 * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.base.sbc.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

import java.util.Map;

/**
 * Jackson 实现 JSON 字段类型处理器
 *
 * @author hubin
 * @since 2019-08-25
 */
public class FastJson2ExtendHandler extends MybatisPlusExtendHandler {
    public FastJson2ExtendHandler(Class<?> type) {
        super(type);
    }

    @Override
    public Map<String, Object> parse(String json) {
        return JSON.parseObject(json, this.getFieldType());
    }

    @Override
    public String toJson(Object obj) {
        return JSON.toJSONString(obj, JSONWriter.Feature.WriteMapNullValue,
                JSONWriter.Feature.WriteNullListAsEmpty, JSONWriter.Feature.WriteNullStringAsEmpty);
    }

    /* ----------------------------上面直接复制MP的JacksonTypeHandler源码---------------------------- */

}
