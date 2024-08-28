package com.base.sbc.config.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * PDF生成
 */
public class pdfUtils2 {
    /**
     * 模板路径
     */
    private static final String TEMPLATE_PATH = "D:\\upload\\1470.pdf";

    // 模板文件路径
    private static final  InputStream pdf = new ByteArrayInputStream(new byte[]{});

    /**
     * 生成的新文件路径
     */
    private static final String NEW_PDF_PATH = "D:\\upload\\test.pdf";

    /**
     * 判断当前系统是否是Windows系统
     *
     * @return true：Windows系统，false：Linux系统
     */
    public static boolean isWindowsSystem() {
        String property = System.getProperty("os.name").toLowerCase();
        return property.contains("windows");
    }

    /**
     * 填充PDF模板
     *
     * @param sourceMap 数据源Map
     */
    public static void fillPDFTemplate(Map<String, Object> sourceMap) {
        PdfReader reader = null;
        FileOutputStream out = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper;
        try {
            // 设置字体，解决中文问题1（推荐用第二种，不需要依赖字体文件）
            // BaseFont bf = BaseFont.createFont("D:\\bb2360\\simsun.ttf" , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // 解决中文问题2
            BaseFont bfChinese;
            if (isWindowsSystem()) {
                bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            } else {
                // 这里将simsun.ttf文件放在项目的resources/font下，也可以直接放到liunx上
                bfChinese = BaseFont.createFont("font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
            Font fontText = new Font(bfChinese, 8, Font.NORMAL);

            // 暂时没找到在哪里放入
            // Font fontChinese = new Font(bf, 3, Font.NORMAL);
            // Font font = new Font(bf, 32);
            out = new FileOutputStream(NEW_PDF_PATH);// 输出流

            reader = new PdfReader(pdf);// 读取pdf模板

            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            //文字类的内容处理
            Map<String, String> dataMap = (Map<String, String>) sourceMap.get("dataMap");
            form.addSubstitutionFont(bfChinese);
            for (String key : dataMap.keySet()) {
                String value = dataMap.get(key);
                form.setField(key, value);
            }

            //图片类的内容处理
            Map<String, Object> imageMap = (Map<String, Object>) sourceMap.get("imageMap");
            for (String key : imageMap.keySet()) {
                String imgpath = imageMap.get(key).toString();
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                //根据路径读取图片
                Image image = Image.getInstance(imgpath);
                //获取图片页面
                PdfContentByte under = stamper.getOverContent(pageNo);
                //图片大小自适应
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                //添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();

            // 生成文件
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

            // 比上面生成的文件会大一点
//            FileCopyUtils.copy(bos.toByteArray(), new FileOutputStream("D:\\test\\testOut2.pdf"));
        } catch (IOException | DocumentException e) {
            System.out.println("填充PDF异常.....");
            System.out.println(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 填充PDF模板并返回字节流
     *
     * @param sourceMap   数据源Map
     * @param inputStream PDF文件的字节流
     * @return 返回填充好的PDF文件字节流
     */
    public static byte[] fillPDFTemplate(Map<String, Object> sourceMap, ByteArrayInputStream inputStream) {
        PdfReader reader = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper;
        try {
            // 解决中文问题2
            BaseFont bfChinese;
            if (isWindowsSystem()) {
                bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            } else {
                // 这里将simsun.ttf文件放在项目的resources/font下，也可以直接放到liunx上
                bfChinese = BaseFont.createFont("font/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
            Font fontText = new Font(bfChinese, 12, Font.NORMAL);

            reader = new PdfReader(inputStream);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            //文字类的内容处理
            Map<String, String> dataMap = (Map<String, String>) sourceMap.get("dataMap");
            form.addSubstitutionFont(bfChinese);
            for (String key : dataMap.keySet()) {
                String value = dataMap.get(key);
                form.setField(key, value);
            }

            //图片类的内容处理
            Map<String, Object> imageMap = (Map<String, Object>) sourceMap.get("imageMap");
            for (String key : imageMap.keySet()) {
                String imgpath = imageMap.get(key).toString();
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                //根据路径读取图片
                Image image = Image.getInstance(imgpath);
                //获取图片页面
                PdfContentByte under = stamper.getOverContent(pageNo);
                //图片大小自适应
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                //添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();

            // 返回填充好的PDF文件字节流
            return bos.toByteArray();
        } catch (IOException | DocumentException e) {
            System.out.println("填充PDF异常.....");
            System.out.println(e);
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("idcard", "430159189802156596");
        map.put("data","壁赋\n" +
                "壬戌之秋1，七月既望2，苏子与客泛舟游于赤壁之下。清风徐来3，水波不兴4。举酒属客5，诵明月之诗6，歌窈窕之章7。少焉8，月出于东山之上，徘徊于斗牛之间9。白露横江10，水光接天。纵一苇之所如，凌万顷之茫然11。浩浩乎如冯虚御风12，而不知其所止；飘飘乎如遗世独立13， …\n" +
                "于是饮酒乐甚，扣舷而歌之15。歌曰：“桂棹兮兰桨16，击空明兮溯流光17。渺渺兮予怀18，望 …\n" +
                "苏子愀然26，正襟危坐27，而问客曰：“何为其然也28？”");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("image", "D:\\upload\\002.png");

        Map<String, Object> formMap = new HashMap<>();
        formMap.put("dataMap", map);
        formMap.put("imageMap", map2);
        fillPDFTemplate(formMap);
    }
}


