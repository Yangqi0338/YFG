package com.base.sbc.config.utils;

import com.base.sbc.config.common.base.BaseGlobal;

/**
 * 拼接字符工具类
 * @author lizan
 * @date 2023-09-04 14:24
 */
public class ProducerNumUtil {



    /**
     * @param prefix prefix为自定义前缀，想要几位数可以自行修改0的数量
     * @param num    最大数量，查询长度补充前缀编码
     * @return 拼接好的字符
     */
    public static String getPrefixNum(String prefix, Integer num) {
        // 判断位数
        String str = num + "";
        int count = str.length();
        String producerNum = prefix;
        if (BaseGlobal.ONE == count) {
            producerNum += "0000" + str;
        } else if (BaseGlobal.TWO == count) {
            producerNum += "000" + str;
        } else if (BaseGlobal.THREE == count) {
            producerNum += "00" + str;
        } else if (BaseGlobal.FOUR == count) {
            producerNum += "0" + str;
        } else {
            producerNum += str;
        }
        return producerNum;
    }
}
