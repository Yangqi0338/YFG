package com.base.sbc.config.constant;

import com.base.sbc.module.pack.utils.PackUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Year;
import java.util.stream.IntStream;


/**
 * {@code 描述：生成工艺单属性}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/29
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(ReplayRatingProperties.PREFIX)
public class ReplayRatingProperties {
    public static final String PREFIX = "replay";

    public static String category1Code = "C4241";
    public static String packType = PackUtils.PACK_TYPE_BIG_GOODS;
    public static int[] years = IntStream.rangeClosed(Year.now().minusYears(2).getValue(), Year.now().getValue()).toArray();

    public void setCategory1Code(String category1Code) {
        ReplayRatingProperties.category1Code = category1Code;
    }

    public void setPackType(String packType) {
        ReplayRatingProperties.packType = packType;
    }

    public void setYears(int[] years) {
        ReplayRatingProperties.years = years;
    }


}
