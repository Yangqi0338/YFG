package com.base.sbc.module.pack.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.generator.utils.UtilFreemarker;
import com.base.sbc.module.pack.dto.PackTechAttachmentVo;
import com.base.sbc.module.pack.vo.PackSizeVo;
import com.base.sbc.module.pack.vo.PackTechSpecVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.layout.HtmlPageBreak;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.utils.GenTechSpecPdfFile
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-19 13:33
 */
@Data
public class GenTechSpecPdfFile {


    @ApiModelProperty(value = "minio文件路径")
    private String objectFileName;
    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "资料包编码")
    private String packCode;
    @ApiModelProperty(value = "编辑的尺码")
    private String activeSizes;

    @ApiModelProperty(value = "洗后尺码标识")
    private String washSkippingFlag;

    @ApiModelProperty(value = "品牌")
    private String brandName;
    @ApiModelProperty(value = "图片")
    private String stylePic;
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    @ApiModelProperty(value = "品名")
    private String productName;
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    @ApiModelProperty(value = "成分信息")
    private String ingredient;

    @ApiModelProperty(value = "执行标准")
    private String executeStandardCode;
    @ApiModelProperty(value = "执行标准名称")
    private String executeStandard;
    @ApiModelProperty(value = "外辅工艺")
    private String extAuxiliaryTechnics;

    @ApiModelProperty(value = "质量等级")
    private String qualityGrade;
    @ApiModelProperty(value = "注意事项")
    private String mattersAttention;
    @ApiModelProperty(value = "安全标题")
    private String saftyTitle;
    @ApiModelProperty(value = "洗唛材质备注")
    private String washingMaterialRemarksName;
    @ApiModelProperty(value = "安全类别")
    private String saftyType;
    @ApiModelProperty(value = "充绒量")
    private String downContent;

    @ApiModelProperty(value = "包装形式")
    private String packagingForm;
    @ApiModelProperty(value = "特殊规格")
    private String specialSpec;

    @ApiModelProperty(value = "包装袋标准")
    private String packagingBagStandard;
    @ApiModelProperty(value = "面料详情")
    private String fabricDetails;
    @ApiModelProperty(value = "工艺师名称")
    private String technologistName;
    @ApiModelProperty(value = "下单员")
    private String placeOrderStaffName;
    @ApiModelProperty(value = "描述信息")
    private String remarks;

    @ApiModelProperty(value = "放码师名称")
    private String gradingName;
    @ApiModelProperty(value = "号型类型名称")
    private String sizeRangeName;

    @ApiModelProperty(value = "样衣工名称")
    private String sampleMakerName;

    @ApiModelProperty(value = "模板部件")
    private String templatePart;

    @ApiModelProperty(value = "设计师")
    private String designer;

    @ApiModelProperty(value = "温馨提示")
    private String warmTips;
    @ApiModelProperty(value = "贮藏要求")
    private String storageDemandName;

    @ApiModelProperty(value = "产地")
    private String producer;

    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date produceDate;
    private String produceDateStr;

    @ApiModelProperty(value = "洗标")
    private String washingLabel;

    @ApiModelProperty(value = "洗标编码")
    private String washingCode;
    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date placeOrderDate;
    private String placeOrderDateStr;
    private String createDate;
    private String createTime;
    @ApiModelProperty(value = "二维码图片地址")
    private String qrCodeUrl;
    @ApiModelProperty(value = "尺码")
    private String defaultSize;
    @ApiModelProperty(value = "生产类型")
    private String devtType;

    public boolean isFob() {
        return StrUtil.equals(devtType, "FOB");
//        return true;
    }

    @ApiModelProperty(value = "工艺信息")
    private List<PackTechSpecVo> techSpecVoList;


    @ApiModelProperty(value = "图片")
    private List<PackTechAttachmentVo> picList;

    @ApiModelProperty(value = "尺寸表")
    private List<PackSizeVo> sizeList;

    private boolean pdfView = true;

    private float lrMargin = 20;

    public String toHtml() throws IOException, TemplateException {
        //处理设计师
        if (StrUtil.isNotBlank(designer)) {
            this.designer = CollUtil.getFirst(StrUtil.split(designer, CharUtil.COMMA));
        }
        this.placeOrderDateStr = DateUtil.format(placeOrderDate, DatePattern.NORM_DATETIME_PATTERN);
        this.produceDateStr = DateUtil.format(produceDate, DatePattern.NORM_DATETIME_PATTERN);

        List<String> sizeList = StrUtil.split(this.activeSizes, CharUtil.COMMA);
        boolean washSkippingFlag = StrUtil.equals(this.getWashSkippingFlag(), BaseGlobal.YES);
        Configuration config = new Configuration();
        config.setDefaultEncoding("UTF-8");
        config.setTemplateLoader(new ClassTemplateLoader(UtilFreemarker.class, "/"));
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template template = config.getTemplate("ftl/process.html.ftl");

        String str = JSON.toJSONString(this, JSONWriter.Feature.WriteNullStringAsEmpty);
        JSONObject dataModel = JSON.parseObject(str);
        dataModel.put("sizeList", sizeList);
        int sizeColspan = 1;
        if (washSkippingFlag) {
            sizeColspan++;
        }
        if (!isFob()) {
            sizeColspan++;
        }
        dataModel.put("sizeColspan", sizeColspan);
        int sizeTitleColspan = sizeColspan * CollUtil.size(sizeList) + 4;
        dataModel.put("sizeTitleColspan", sizeTitleColspan);
        dataModel.put("washSkippingFlag", washSkippingFlag);
        //处理尺码数据
        // 3个白色开始 2个灰色开始
        List<Map<String, Object>> dataList = new ArrayList<>(16);
        ArrayList<String> tdClassList = CollUtil.newArrayList("gb", "wb");
        Map<Object, Object> sizeClass = new LinkedHashMap<>();
        //计算尺寸表的背景颜色
        //默认尺码下标
        int defaultSizeIndex = CollUtil.indexOf(sizeList, (s) -> StrUtil.equals(s, defaultSize));
        //部位、描述、列无背景
        int sizeTdIndex = 0;
        sizeClass.put(sizeTdIndex++, "");
        sizeClass.put(sizeTdIndex++, "");
        String tdBg = "";
        int sizeResetFlg = defaultSizeIndex;
        boolean isDefaultSize = false;
        for (String s : sizeList) {
            isDefaultSize = StrUtil.equals(s, defaultSize);
            if (isDefaultSize) {
                tdBg = "dgb";
            } else {
                tdBg = CollUtil.get(tdClassList, sizeResetFlg % 2);
            }
            for (int j = 0; j < sizeColspan; j++) {
                sizeClass.put(sizeTdIndex++, tdBg);
            }
            if (isDefaultSize) {
                sizeResetFlg = 1;
            } else {
                sizeResetFlg++;
            }

        }
        // 公差-、公差+列无背景
        sizeClass.put(sizeTdIndex++, "");
        sizeClass.put(sizeTdIndex, "");
        if (CollUtil.isNotEmpty(this.getSizeList())) {
            for (int i = 0; i < this.getSizeList().size(); i++) {
                PackSizeVo packSize = this.getSizeList().get(i);
                SizeDataDetail rowData = new SizeDataDetail();
                rowData.setRowType(packSize.getRowType());
                rowData.setRemark(packSize.getRemark());
                List<Map<String, Object>> row = new ArrayList<>();
                row.add(new TdDetail(Opt.ofNullable(packSize.getPartName()).orElse(""), "partNameClass").toMap());
                row.add(new TdDetail(Opt.ofNullable(packSize.getMethod()).orElse(""), "td_lt methodClass").toMap());
                JSONObject jsonObject = JSONObject.parseObject(packSize.getStandard());
                for (String size : sizeList) {
                    if (!isFob()) {
                        row.add(new TdDetail(MapUtil.getStr(jsonObject, "template" + size, "-")).toMap());
                    }
                    row.add(new TdDetail(MapUtil.getStr(jsonObject, "garment" + size, "-")).toMap());
                    if (washSkippingFlag) {
                        row.add(new TdDetail(MapUtil.getStr(jsonObject, "washing" + size, "-")).toMap());
                    }
                }
                //公差-
                row.add(new TdDetail(Opt.ofBlankAble(packSize.getMinus()).map(minus -> minus.contains("-") ? minus : ("-" + minus)).orElse("")).toMap());
                //公差+
                row.add(new TdDetail(Opt.ofNullable(packSize.getPositive()).orElse("")).toMap());
                rowData.setRowData(row);
                dataList.add(rowData.toMap());
            }
        }
        Map<String, List<PackTechAttachmentVo>> picMap = new HashMap<>(16);
        if (CollUtil.isNotEmpty(this.getPicList())) {
            picMap = this.getPicList().stream().collect(Collectors.groupingBy(PackTechAttachmentVo::getSpecType));
        }
        Map<String, List<PackTechSpecVo>> gyMap = new LinkedHashMap<>(16);
        if (CollUtil.isNotEmpty(this.getTechSpecVoList())) {
            gyMap = JSON.parseArray(JSON.toJSONString(this.getTechSpecVoList(), JSONWriter.Feature.WriteNullStringAsEmpty))
                    .toJavaList(PackTechSpecVo.class)
                    .stream()
                    .collect(Collectors.groupingBy(PackTechSpecVo::getSpecType, LinkedHashMap::new, Collectors.toList()));
        }
        dataModel.put("isFob", isFob());
        dataModel.put("sizeDataList", dataList);
        dataModel.put("sizeClass", sizeClass.values());

        for (String key : gyMap.keySet()) {
            List<PackTechSpecVo> packTechSpecVos = Optional.ofNullable(gyMap.get(key)).orElse(CollUtil.newArrayList());
            // 匹配整数 小数
            Pattern pattern = Pattern.compile("(\\d+\\.\\d+)|(\\d+\\.?\\d*)");
            for (int i = 0; i < packTechSpecVos.size(); i++) {
                PackTechSpecVo vo = packTechSpecVos.get(i);
                String content = vo.getContent();
                Matcher matcher = pattern.matcher(content);
                while(matcher.find()) {
                    String number = matcher.group();
                    content = content.replace(number, "<strong>" + number + "</strong>");
                }
                vo.setContent(content);
            }
        }
        dataModel.put("ztbzDataList", Optional.ofNullable(gyMap.get("整烫包装")).orElse(CollUtil.newArrayList()));
        dataModel.put("cjgyDataList", Optional.ofNullable(gyMap.get("裁剪工艺")).orElse(CollUtil.newArrayList()));
        dataModel.put("cjgyImgList", Optional.ofNullable(picMap.get("裁剪工艺")).orElse(CollUtil.newArrayList()));
        List<PackTechSpecVo> xbjDataList = Optional.ofNullable(gyMap.get("小部件")).orElse(CollUtil.newArrayList());
        List<PackTechSpecVo> zysxDataList = Optional.ofNullable(gyMap.get("注意事项")).orElse(CollUtil.newArrayList());
        dataModel.put("xbjDataList", xbjDataList);
        dataModel.put("zysxDataList", zysxDataList);
        dataModel.put("xbjImgList", Optional.ofNullable(picMap.get("小部件")).orElse(null));
        dataModel.put("xbjRowsPan", xbjDataList.size());

        List<PackTechSpecVo> jcgyDataList = Optional.ofNullable(gyMap.get("基础工艺")).orElse(CollUtil.newArrayList());
        dataModel.put("jcgyDataList", jcgyDataList);
        List<PackTechAttachmentVo> jcgyImgList = Optional.ofNullable(picMap.get("基础工艺")).map(img -> {
            if (img.size() > 2) {
                return CollUtil.sub(img, 0, 2);
            }
            return img;
        }).orElse(CollUtil.newArrayList());

        dataModel.put("jcgyImgList", jcgyImgList);
        dataModel.put("jcgyImgHeight", jcgyImgList.size() > 0 ? 72 / jcgyImgList.size() : 72);
        dataModel.put("jcgyRowsPan", jcgyDataList.size());

        dataModel.put("zysxImgList", Optional.ofNullable(picMap.get("注意事项")).orElse(null));

        // 裁剪工艺是否显示
        dataModel.put("cjgyShow", !isFob() || ObjectUtil.isNotEmpty(dataModel.get("cjgyDataList")));
        dataModel.put("cjgyImgShow", !isFob() || ObjectUtil.isNotEmpty(dataModel.get("cjgyImgList")));
        // 小部件是否显示
        dataModel.put("xbjShow", !isFob() || ObjectUtil.isNotEmpty(dataModel.get("xbjDataList")));
        //注意事项是否显示
        dataModel.put("zysxShow", !isFob() || ObjectUtil.isNotEmpty(dataModel.get("zysxImgList")));
        // 基础工艺是否显示
        dataModel.put("jcgyShow", !isFob() || ObjectUtil.isNotEmpty(dataModel.get("jcgyDataList")));
        // 整烫包装 是否显示
        dataModel.put("ztbzShow", !isFob() || ObjectUtil.isNotEmpty(dataModel.get("ztbzDataList")));
        // 外辅工艺 是否显示
        dataModel.put("wfgyDataList", Optional.ofNullable(gyMap.get("外辅工艺")).orElse(CollUtil.newArrayList()));
        dataModel.put("wfgyShow", isFob() && ObjectUtil.isNotEmpty(dataModel.get("wfgyDataList")));
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        String html = writer.toString();
//        System.out.println("temp目录路径:" + FileUtil.getTmpDirPath() + "/" + designNo + "htmltoPdf.html");
//        FileUtil.writeString(html, new File(FileUtil.getTmpDirPath() + "/" + designNo + "htmltoPdf.html"), Charset.defaultCharset());
        return html;
    }

    public ByteArrayOutputStream gen() {
        try {
            String output = toHtml();
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            // 创建PDF写入器
            ConverterProperties props = new ConverterProperties();
            props.setCharset("UFT-8");
            FontProvider provider = new DefaultFontProvider(true, true, true);

            props.setFontProvider(provider);
            List<IElement> elements = HtmlConverter.convertToElements(output, props);

            PdfWriter writer1 = new PdfWriter(pdfOutputStream);
            IElement pageStart = CollUtil.getFirst(elements);
            PdfDocument pdfDocument = new PdfDocument(writer1);
            Document document = new Document(pdfDocument, PageSize.A4.rotate(), false);
            StartPdfPageEventHandler event = new StartPdfPageEventHandler(pdfDocument, document, pageStart);
            EndPdfPageEventHandler endPdfPageEventHandler = new EndPdfPageEventHandler(pdfDocument, document, pageStart);
            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, event);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, endPdfPageEventHandler);
            // 定义页眉
            document.setMargins(30, lrMargin, 30, lrMargin);
            for (int i = 1; i < elements.size(); i++) {
                IElement element = elements.get(i);
                // 分页符
                if (element instanceof HtmlPageBreak) {
                    document.add((HtmlPageBreak) element);
                    //普通块级元素
                } else {
                    IBlockElement blockElement = (IBlockElement) element;
                    document.add(blockElement);
                }
            }


            // 设置页眉中的总页数
            int numberOfPages = pdfDocument.getNumberOfPages();
            for (int i = 1; i <= numberOfPages; i++) {
                Paragraph pageNumber = new Paragraph(String.format(" %d  /  %d ", i, numberOfPages));
                pageNumber.setTextAlignment(TextAlignment.CENTER);
                float x = pdfDocument.getDefaultPageSize().getWidth() / 2 - 5;
                // 距离底部的距离
                float y = 25;
                document.showTextAligned(pageNumber, x, y, i, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            }

            document.close();
            return pdfOutputStream;
        } catch (Exception e) {
            throw new OtherException("生成工艺单失败:" + e.getMessage());
        }


    }

    @Data
    class SizeDataDetail {
        private String rowType;
        private String remark;
        private List<Map<String, Object>> rowData;

        public Map<String, Object> toMap() {
            String str = JSON.toJSONString(this, JSONWriter.Feature.WriteNullStringAsEmpty);
            JSONObject dataModel = JSON.parseObject(str);
            return dataModel;
        }
    }

    class TdDetail {
        public TdDetail() {
        }

        public TdDetail(String text) {
            this.text = text;
        }

        public TdDetail(String text, String className) {
            this.text = text;
            this.className = className;
        }

        private String text = "";
        private String className = "";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Map<String, Object> toMap() {
            return BeanUtil.beanToMap(this);
        }
    }

    class EndPdfPageEventHandler implements IEventHandler {
        private PdfDocument pdfDocument;
        private Document document;
        private IElement element;

        public EndPdfPageEventHandler(PdfDocument pdfDocument, Document document, IElement element) {
            this.pdfDocument = pdfDocument;
            this.document = document;
            this.element = element;
        }

        @Override
        public void handleEvent(Event event) {

        }
    }

    class StartPdfPageEventHandler implements IEventHandler {
        private PdfDocument pdfDocument;
        private Document document;
        private IElement element;

        public StartPdfPageEventHandler(PdfDocument pdfDocument, Document document, IElement element) {
            this.pdfDocument = pdfDocument;
            this.document = document;
            this.element = element;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            //通过 page 进行一些处理  ，这个需要去了解如何在page上进行添加内容
            //也可以传入  List<IElement> iElements ，直接添加 ，
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page);
            float pageWith = pageSize.getWidth() - lrMargin * 2;
            float footHeight = 30;
            float marginBottom = pageSize.getHeight() - footHeight - 10;

            Rectangle rectangle = new Rectangle(lrMargin, marginBottom, pageWith, footHeight);
            Canvas canvas = new Canvas(pdfCanvas, pdfDocument, rectangle);
            canvas.add((IBlockElement) element);
            pdfCanvas.release();
        }
    }
}
