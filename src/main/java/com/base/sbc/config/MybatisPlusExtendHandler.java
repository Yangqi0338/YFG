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

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Jackson 实现 JSON 字段类型处理器
 *
 * @author hubin
 * @since 2019-08-25
 */
@Slf4j
@MappedTypes({Map.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
@Component
public abstract class MybatisPlusExtendHandler extends AbstractJsonTypeHandler<Map<String, Object>> {
    protected static final TypeReference<Map<String, Object>> TYPE = new TypeReference<Map<String, Object>>() {
    };

    private MappedStatement statement;

    public MybatisPlusExtendHandler(Class<?> type) {
        super(type);
//        if (!this.getRawType().getTypeName().equals(type.getTypeName())) {
//            throw new OtherException("使用这个extend处理器,类型仅支持Map<String,Object>");
//        }
    }

    public void setStatement(MappedStatement statement) {
        this.statement = statement;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        super.setNonNullParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decorateObj(super.getNullableResult(rs, columnName), rs);
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decorateObj(super.getNullableResult(rs, columnIndex), rs);
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decorateObj(super.getNullableResult(cs, columnIndex), cs.getResultSet());
    }


    private Map<String, Object> decorateObj(Map<String, Object> extendMap, ResultSet rs) {
//        extendMap.forEach((key,value)-> {
//            ResultMap resultMap = statement.getResultMaps().stream().findFirst().orElseThrow(()-> new OtherException("没有找到"));
//
//            Class<?> type = resultMap.getType();
//
//        });
        return extendMap;
    }
}
