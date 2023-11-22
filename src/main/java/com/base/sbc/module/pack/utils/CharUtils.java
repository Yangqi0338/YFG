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
 * Author: weiql
 * Date: 2023 2023/11/21 10:17
 */
public class CharUtils {
    /**
     * 注意：开发windows系统生成的字体长度比测试机和生产机器的要小，所以取值，必须从测试机器或者生产机器上测试行容纳字符数
     * 获取行数（基础工艺）,基础工艺的描述只有624，这个长度 = 最大中文字符串数量*fontSize, 工艺项目要求12个字符所以长度= 12*11
     * @param list
     * @return
     */
    public static int getRowsVO(List<PackTechSpecVo> list) {
        int totalRows = 0;
        for (int i = 0; i < list.size(); i++) {
            PackTechSpecVo packTechSpec = list.get(i);
            Integer itemRowCount = CharUtils.contentRows(132f, packTechSpec.getItem());
            Integer contentRowCount = CharUtils.contentRows(624f, packTechSpec.getContent());
            totalRows += itemRowCount > contentRowCount ? itemRowCount : contentRowCount;
            packTechSpec.setRows(totalRows);
        }
        return totalRows;
    }

    /**
     * 获取行数(裁剪工艺)
     * @param list
     * @return
     */
    public static int getRows(List<PackTechSpec> list) {
        int totalRows = 0;
        for (int i = 0; i < list.size(); i++) {
            PackTechSpec packTechSpec = list.get(i);
            Integer itemRowCount = CharUtils.contentRows(132f, packTechSpec.getItem());
            Integer contentRowCount = CharUtils.contentRows(912f, packTechSpec.getContent());
            totalRows += itemRowCount > contentRowCount ? itemRowCount : contentRowCount;
        }
        return totalRows;
    }

    /**
     * 判断内容行数
     * @param oneRowWidth 一行的长度，通常是px， 字符数量*fontSize
     * @param content 内容
     * @return 行数
     */
    public static Integer contentRows(float oneRowWidth, String content){
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
            if(v + v1 + v2 > oneRowWidth) {
                rows += Math.ceil((v + v1 + v2) / oneRowWidth);
            } else {
                rows++;
            }
        }
        return rows;
    }

    /**
     * 汇总字符串中内容成分： 中文，英文，其他字符
     * @param str
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
            if (isDigit(ch)){
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
        return Arrays.asList(chineseCount, englishCount, chinesePunctuationCount, englishPunctuationCount,numberCount, otherCount);
    }

    public static List<String> extractParagraphs(String html) {
        List<String> paragraphs = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements pElements = doc.select("p");
        if(pElements.size() == 0) {
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
     * @param ch
     * @return
     */
    public static boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

}
