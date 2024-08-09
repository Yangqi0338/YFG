package com.base.sbc.config.constant;

import com.base.sbc.config.enums.UnitConverterEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailOrderStatusEnum;
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
    public static String noDevClass = "99";
    public static String yearCodeSuffix = ".0";
    public static String yearNameSuffix = "年";
    public static Integer monthRange = 1;
    public static UnitConverterEnum cmtUnit = UnitConverterEnum.KILOMETER;
    public static UnitConverterEnum fobUnit = UnitConverterEnum.PIECE;
    public static String totalPrefix = "total";
    public static String longTimeAgoPrefix = "longTimeAgo";
    public static String noMaterialCode = "0000";
    public static OrderBookDetailOrderStatusEnum orderBookStatus = OrderBookDetailOrderStatusEnum.ORDER;
    public static String packType = PackUtils.PACK_TYPE_BIG_GOODS;
    public static Boolean totalCurrentPage = false;
    public static int[] years = IntStream.rangeClosed(Year.now().minusYears(2).getValue(), Year.now().getValue()).toArray();

    public void setCategory1Code(String category1Code) {
        ReplayRatingProperties.category1Code = category1Code;
    }

    public void setNoDevClass(String noDevClass) {
        ReplayRatingProperties.noDevClass = noDevClass;
    }

    public void setYearCodeSuffix(String yearCodeSuffix) {
        ReplayRatingProperties.yearCodeSuffix = yearCodeSuffix;
    }

    public void setYearNameSuffix(String yearNameSuffix) {
        ReplayRatingProperties.yearNameSuffix = yearNameSuffix;
    }

    public void setMonthRange(Integer monthRange) {
        ReplayRatingProperties.monthRange = monthRange;
    }

    public void setCmtUnit(UnitConverterEnum cmtUnit) {
        ReplayRatingProperties.cmtUnit = cmtUnit;
    }

    public void setFobUnit(UnitConverterEnum fobUnit) {
        ReplayRatingProperties.fobUnit = fobUnit;
    }

    public void setTotalPrefix(String totalPrefix) {
        ReplayRatingProperties.totalPrefix = totalPrefix;
    }

    public void setLongTimeAgoPrefix(String longTimeAgoPrefix) {
        ReplayRatingProperties.longTimeAgoPrefix = longTimeAgoPrefix;
    }

    public void setNoMaterialCode(String noMaterialCode) {
        ReplayRatingProperties.noMaterialCode = noMaterialCode;
    }

    public void setOrderBookStatus(OrderBookDetailOrderStatusEnum orderBookStatus) {
        ReplayRatingProperties.orderBookStatus = orderBookStatus;
    }

    public void setPackType(String packType) {
        ReplayRatingProperties.packType = packType;
    }

    public void setYears(int[] years) {
        ReplayRatingProperties.years = years;
    }

    public void setTotalCurrentPage(Boolean totalCurrentPage) {
        ReplayRatingProperties.totalCurrentPage = totalCurrentPage;
    }


}
