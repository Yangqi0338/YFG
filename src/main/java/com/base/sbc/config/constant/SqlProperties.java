package com.base.sbc.config.constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.standard.entity.StandardColumn;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.INCORRECT_STANDARD_CODE;

/**
 * {@code 描述：生成工艺单属性}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/29
 */
@Configuration
@EnableConfigurationProperties(SqlProperties.class)
@ConfigurationProperties("sql")
@Data
public class SqlProperties {

    private List<String> excludePrintList;

}
