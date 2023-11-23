package com.base.sbc.module.pack.utils;

import com.base.sbc.module.pack.entity.PackTechSpec;
import com.base.sbc.module.pack.vo.PackTechSpecVo;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * 计算行数 (contentRows 方法):
 * 输入: 一行的长度、内容、是否只统计含数字的行
 * 过程:
 * 分解内容为段落
 * 遍历每个段落
 * 计算不同类型字符的数量
 * 计算行宽度
 * 判断是否需要换行
 * 对含数字的行进行特殊处理（如果 hasNumber 为 true）
 * 累加行数
 * 输出: 行数
 * 获取字符宽度 (getCharWidth 方法):
 *
 * 输入: 字符
 * 过程:
 * 根据字符类型（数字、中文、英文、中英文标点等）返回对应的宽度
 * 输出: 字符宽度
 * 辅助方法:
 * isDigit, isChinese, isEnglish, isChinesePunctuation, isEnglishPunctuation 等方法用于判断字符类型。
 * Author: weiql
 * Date: 2023 2023/11/21 10:17
 */
public class CharUtils {
    /**
     * 获取行数(裁剪工艺)
     *
     * @param list
     * @return
     */
    public static int getRows(List<PackTechSpec> list) {
        int totalRows = 0;
        for (int i = 0; i < list.size(); i++) {
            PackTechSpec packTechSpec = list.get(i);
            Integer itemRowCount = CharUtils.contentRows(132f, packTechSpec.getItem(), false);
            Integer contentRowCount = CharUtils.contentRows(912f, packTechSpec.getContent(), false);
            totalRows += itemRowCount > contentRowCount ? itemRowCount : contentRowCount;
        }
        return totalRows;
    }

    /**
     * 判断内容行数
     *
     * @param oneRowWidth 一行的长度，通常是px， 字符数量*fontSize
     * @param content     内容
     * @param hasNumber   是否只统计有数字的行
     * @return 行数
     */
    public static Integer contentRows(float oneRowWidth, String content, boolean hasNumber) {
        float chineseFontSize = 12f;
        float englishFontSize = 6.7f;
        float numberFontSize = 8.36f;
        List<String> strings = extractParagraphs(content);
        int rows = 0;
        for (int i = 0; i < strings.size(); i++) {
            List<Integer> count = countCharacters(strings.get(i));
            int chineseCount = count.get(0);
            int englishCount = count.get(1);
            int chinesePunctuationCount = count.get(2);
            int englishPunctuationCount = count.get(3);
            int numberCount = count.get(4);
            int otherCount = count.get(5);
            float v = chineseFontSize * (chineseCount + chinesePunctuationCount + otherCount);
            float v1 = englishFontSize * (englishCount + englishPunctuationCount);
            float v2 = numberFontSize * numberCount;
            if (v + v1 + v2 > oneRowWidth) {
                if (hasNumber && numberCount != 0) {
                    // 统计有数字的行数
                    // 遍历获取每一行的内容
                    float initWidth = 0;
                    int lastCutIndex = 0;
                    char[] charArray = strings.get(i).toCharArray();
                    for (int pointer = 0; pointer < charArray.length; pointer++) {
                        char ch = charArray[pointer];
                        float charWidth = getCharWidth(ch);
                        initWidth += charWidth;
                        if (initWidth > oneRowWidth) {
                            // 如果加上当前字符会换行
                            String substring = strings.get(i).substring(lastCutIndex, pointer);
                            if (countCharacters(substring).get(4) > 0) {
                                // 这一行存在数字
                                rows++;
                            }
                            lastCutIndex = pointer;
                            initWidth = 0; // 复位
                        }
                    }
                    String substring = strings.get(i).substring(lastCutIndex, charArray.length);
                    if (countCharacters(substring).get(4) > 0) {
                        // 这一行存在数字
                        rows++;
                    }
                } else {
                    rows += Math.ceil((v + v1 + v2) / oneRowWidth);
                }

            } else {
                if(!hasNumber) {
                    rows++;
                } else {
                    if(numberCount > 0) {
                        rows++;
                    }
                }
            }


        }
        return rows;
    }

    /**
     * 获取字符宽度
     *
     * @param ch 字符
     * @return float类型宽度
     */
    public static float getCharWidth(char ch) {
        if (isDigit(ch)) {
            return 8.36f;
        } else if (isChinese(ch)) {
            return 12f;
        } else if (isEnglish(ch)) {
            return 6.7f;
        } else if (isChinesePunctuation(ch)) {
            return 12f;
        } else if (isEnglishPunctuation(ch)) {
            return 6.7f;
        } else {
            return 12f;
        }
    }

    /**
     * 汇总字符串中内容成分： 中文，英文，其他字符
     *
     * @param str 判断的字符串
     * @return 返回list{中文字符数量，英文字符数量， 中文符号数量，英文符号数量，数字数量，其他字符数量}
     */
    public static List<Integer> countCharacters(String str) {
        int chineseCount = 0;
        int englishCount = 0;
        int chinesePunctuationCount = 0;
        int englishPunctuationCount = 0;
        int otherCount = 0;
        int numberCount = 0;
        for (char ch : str.toCharArray()) {
            if (isDigit(ch)) {
                numberCount++;
            } else if (isChinese(ch)) {
                chineseCount++;
            } else if (isEnglish(ch)) {
                englishCount++;
            } else if (isChinesePunctuation(ch)) {
                chinesePunctuationCount++;
            } else if (isEnglishPunctuation(ch)) {
                englishPunctuationCount++;
            } else {
                otherCount++;
            }
        }
        return Arrays.asList(chineseCount, englishCount, chinesePunctuationCount, englishPunctuationCount, numberCount, otherCount);
    }

    public static List<String> extractParagraphs(String html) {
        List<String> paragraphs = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements pElements = doc.select("p");
        if (pElements.size() == 0) {
            Document htmlDoc = Jsoup.parse(html);
            return Collections.singletonList(htmlDoc.text());
        }
        for (Element p : pElements) {
            paragraphs.add(p.text());
        }

        return paragraphs;
    }

    /**
     * 是否是汉字
     *
     * @param ch
     * @return
     */
    public static boolean isChinese(char ch) {
        if (Character.isSurrogate(ch)) {
            return false; // 代理字符单独无法判断，需要配对使用
        }
        int codePoint = ch;
        return (codePoint >= '\u4E00' && codePoint <= '\u9FA5') ||
                (codePoint >= '\u3400' && codePoint <= '\u4DBF') || // 扩展A区
                (codePoint >= '\uF900' && codePoint <= '\uFAFF'); // 兼容汉字及扩展B区
    }

    /**
     * 字符是否是英文字母
     *
     * @param ch
     * @return
     */
    public static boolean isEnglish(char ch) {
        if (Character.isSurrogate(ch)) {
            return false;
        }
        return (ch >= '\u0041' && ch <= '\u005A') || (ch >= '\u0061' && ch <= '\u007A');
    }

    /**
     * 是否是中文符号
     *
     * @param ch
     * @return
     */
    public static boolean isChinesePunctuation(char ch) {
        if (Character.isSurrogate(ch)) {
            return false;
        }
        return (ch >= '\u3000' && ch <= '\u303F') ||
                // 可以根据需要添加更多的中文符号范围
                (ch >= '\uFF00' && ch <= '\uFFEF'); // 全角ASCII、全角标点符号
    }

    /**
     * 是否是英文符号
     *
     * @param ch
     * @return
     */
    public static boolean isEnglishPunctuation(char ch) {
        if (Character.isSurrogate(ch)) {
            return false;
        }
        return (ch >= '\u0020' && ch <= '\u0040') ||
                (ch >= '\u005B' && ch <= '\u0060') ||
                (ch >= '\u007B' && ch <= '\u007E');
    }


    /**
     * 是否是数字
     *
     * @param ch
     * @return
     */
    public static boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

}
