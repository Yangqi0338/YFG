/******************************************************************************
 * Copyright (C) 2017 广州尚捷科技有限公司
 * All Rights Reserved.
 * 本软件为广州尚捷科技有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.config.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/** 
 * 类描述：
 * @address com.zcyl.scm.common.utils.Pinyin4jUtil
 * @author shenzhixiong  
 * @email 731139982@qq.com
 * @date 创建时间：2017年6月16日 上午10:34:50 
 * @version 1.0  
 */
public class Pinyin4jUtil {
    /** 
     * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，生成方式如（长沙市长:cssc,zssz,zssc,cssz） 
     *  
     * @param chines 
     *            汉字 
     * @return 拼音 
     */  
    public static String converterToFirstSpell(String chines) {
        if (StringUtils.isEmpty(chines)){
            return "";
        }
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : nameChar) {
            if (c > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            c, defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            // 取首字母
                            pinyinName.append(strs[j].charAt(0));
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                    // else {
                    // pinyinName.append(nameChar[i]);
                    // }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(c);
            }
            pinyinName.append(" ");
        }  
        // return pinyinName.toString();  
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));  
    }  

    /** 
     * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失 
     * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen 
     * ,chongdangshen,zhongdangshen,chongdangcan） 
     *  
     * @param chines 
     *            汉字 
     * @return 拼音 
     */  
    public static String converterToSpell(String chines) {  
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : nameChar) {
            if (c > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            c, defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            pinyinName.append(strs[j]);
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(c);
            }
            pinyinName.append(" ");
        }  
        // return pinyinName.toString();  
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));  
    }  

    /** 
     * 去除多音字重复数据 
     *  
     * @param theStr 
     * @return 
     */  
    private static List<Map<String, Integer>> discountTheChinese(String theStr) {  
        // 去除重复拼音后的拼音列表  
        List<Map<String, Integer>> mapList = new ArrayList<>();
        // 用于处理每个字的多音字，去掉重复  
        Map<String, Integer> onlyOne;
        String[] firsts = theStr.split(" ");  
        // 读出每个汉字的拼音  
        for (String str : firsts) {  
            onlyOne = new Hashtable<>();
            String[] china = str.split(",");  
            // 多音字处理  
            for (String s : china) {  
                Integer count = onlyOne.get(s);  
                if (count == null) {  
                    onlyOne.put(s, 1);
                } else {  
                    onlyOne.remove(s);  
                    count++;  
                    onlyOne.put(s, count);  
                }  
            }  
            mapList.add(onlyOne);  
        }  
        return mapList;  
    }  

    /** 
     * 解析并组合拼音，对象合并方案
     *  
     * @return 
     */  
    private static String parseTheChineseByObject(  
            List<Map<String, Integer>> list) {
        // 用于统计每一次,集合组合数据
        Map<String, Integer> first = null;
        // 遍历每一组集合
        for (Map<String, Integer> stringIntegerMap : list) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new Hashtable<>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : stringIntegerMap.keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (!temp.isEmpty()) {
                    first.clear();
                }
            } else {
                for (String s : stringIntegerMap.keySet()) {
                    temp.put(s, 1);
                }
            }
            // 保存组合数据以便下次循环使用  
            if (!temp.isEmpty()) {
                first = temp;
            }
        }  
        StringBuilder returnStr = new StringBuilder();
        if (first != null) {  
            // 遍历取出组合字符串  
            for (String str : first.keySet()) {  
                returnStr.append(str).append(",");
            }  
        }  
        if (returnStr.length() > 0) {
            returnStr = new StringBuilder(returnStr.substring(0, returnStr.length() - 1));
        }  
        return returnStr.toString();
    }


    public static void main(String[] args) {
        String str = "长沙市长";  
        String pinyin = Pinyin4jUtil.converterToSpell(str);  
        System.out.println(str+" pin yin ："+pinyin);
        pinyin = Pinyin4jUtil.converterToFirstSpell(str);
        System.out.println(str+" short pin yin ："+pinyin);
    }

}