package com.base.sbc.module.pack.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
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
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

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


    @ApiModelProperty(value = "企业名称")
    private String companyName;
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
    private String washingMaterialRemarks;
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
    @ApiModelProperty(value = "备注")
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
    private String storageDemand;

    @ApiModelProperty(value = "产地")
    private String producer;

    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date produceDate;
    private String produceDateStr;
    @ApiModelProperty(value = "洗标")
    private String washingLabel;
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
    @ApiModelProperty(value = "工艺信息")
    private List<PackTechSpecVo> techSpecVoList;


    @ApiModelProperty(value = "图片")
    private List<PackTechAttachmentVo> picList;

    @ApiModelProperty(value = "尺寸表")
    private List<PackSizeVo> sizeList;

    private float lrMargin = 20;

    public ByteArrayOutputStream gen() {
        try {
            //处理设计师
            if (StrUtil.isNotBlank(designer)) {
                this.designer = CollUtil.getFirst(StrUtil.split(designer, CharUtil.COMMA));
            }
            this.placeOrderDateStr = DateUtil.format(placeOrderDate, DatePattern.NORM_DATETIME_PATTERN);
            this.produceDateStr = DateUtil.format(produceDate, DatePattern.NORM_DATETIME_PATTERN);
            Date newDate = new Date();
            this.createDate = DateUtil.format(newDate, DatePattern.NORM_DATE_PATTERN);
            this.createTime = DateUtil.format(newDate, "a HH:mm");

            List<String> sizeList = StrUtil.split(this.activeSizes, CharUtil.COMMA);
            boolean washSkippingFlag = StrUtil.equals(this.getWashSkippingFlag(), BaseGlobal.YES);
//            washSkippingFlag=true;
            Configuration config = new Configuration();
            config.setDefaultEncoding("UTF-8");
            config.setTemplateLoader(new ClassTemplateLoader(UtilFreemarker.class, "/"));
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            Template template = config.getTemplate("ftl/process.html.ftl");

            String str = JSON.toJSONString(this, JSONWriter.Feature.WriteNullStringAsEmpty);
            JSONObject dataModel = JSON.parseObject(str);
            dataModel.put("sizeList", sizeList);
            int sizeColspan = washSkippingFlag ? 3 : 2;
            dataModel.put("sizeColspan", sizeColspan);
            dataModel.put("sizeTitleColspan", sizeColspan * CollUtil.size(sizeList) + 4);
            dataModel.put("washSkippingFlag", washSkippingFlag);
            //处理尺码数据
            // 3个白色开始 2个灰色开始
            List<Map<String, Object>> dataList = new ArrayList<>(16);
            ArrayList<String> tdClassList = CollUtil.newArrayList("gb", "");
            Map<Object, Object> sizeClass = new LinkedHashMap<>();
            int classIndex = sizeColspan;
            if (CollUtil.isNotEmpty(this.getSizeList())) {
                for (int i = 0; i < this.getSizeList().size(); i++) {
                    PackSizeVo packSize = this.getSizeList().get(i);
                    sizeDataDetail rowData = new sizeDataDetail();
                    rowData.setRowType(packSize.getRowType());
                    rowData.setRemark(packSize.getRemark());
                    List<Map<String, Object>> row = new ArrayList<>();

                    row.add(new TdDetail(Opt.ofNullable(packSize.getPartName()).orElse("")).toMap());
                    row.add(new TdDetail(Opt.ofNullable(packSize.getMethod()).orElse("")).toMap());
                    JSONObject jsonObject = JSONObject.parseObject(packSize.getStandard());
                    classIndex = sizeColspan;
                    for (int j = 0; j < sizeList.size(); j++) {
                        String size = sizeList.get(j);
                        boolean isDefaultSize = StrUtil.equals(size, defaultSize);
                        row.add(new TdDetail(MapUtil.getStr(jsonObject, "template" + size, "-"), isDefaultSize ? "gb" : CollUtil.get(tdClassList, classIndex % 2)).toMap());
                        sizeClass.put(row.size(), CollUtil.getLast(row).get("className"));
                        classIndex++;
                        row.add(new TdDetail(MapUtil.getStr(jsonObject, "garment" + size, "-"), isDefaultSize ? "gb" : CollUtil.get(tdClassList, classIndex % 2)).toMap());
                        sizeClass.put(row.size(), CollUtil.getLast(row).get("className"));
                        classIndex++;
                        if (washSkippingFlag) {
                            row.add(new TdDetail(MapUtil.getStr(jsonObject, "washing" + size, "-"), isDefaultSize ? "gb" : CollUtil.get(tdClassList, classIndex % 2)).toMap());
                            sizeClass.put(row.size(), CollUtil.getLast(row).get("className"));
                            classIndex++;
                        }
                        if (isDefaultSize) {
                            classIndex = 1;
                        }
                    }
                    //公差-
                    row.add(new TdDetail(Opt.ofNullable(packSize.getMinus()).orElse("")).toMap());
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
            Map<String, List<PackTechSpecVo>> gyMap = new HashMap<>(16);
            if (CollUtil.isNotEmpty(this.getTechSpecVoList())) {
//                gyMap = this.getTechSpecVoList().stream().collect(Collectors.groupingBy(PackTechSpecVo::getSpecType));
                gyMap = JSON.parseArray(JSON.toJSONString(this.getTechSpecVoList(), JSONWriter.Feature.WriteNullStringAsEmpty))
                        .toJavaList(PackTechSpecVo.class)
                        .stream()
                        .collect(Collectors.groupingBy(PackTechSpecVo::getSpecType));
            }

//            ArrayList<Object> sizeDataListAll = CollUtil.newArrayList();
//            ListUtil.page(dataList, 18, d -> sizeDataListAll.add(d));
            dataModel.put("sizeDataList", dataList);
            dataModel.put("sizeClass", sizeClass.values());
            dataModel.put("ztbzDataList", Optional.ofNullable(gyMap.get("整烫包装")).orElse(CollUtil.newArrayList()));
            dataModel.put("cjgyDataList", Optional.ofNullable(gyMap.get("裁剪工艺")).orElse(CollUtil.newArrayList()));
            dataModel.put("cjgyImgList", Optional.ofNullable(picMap.get("裁剪工艺")).orElse(CollUtil.newArrayList()));
            List<PackTechSpecVo> xbjDataList = Optional.ofNullable(gyMap.get("小部件")).orElse(CollUtil.newArrayList());
            dataModel.put("xbjDataList", xbjDataList);
            dataModel.put("xbjImgList", Optional.ofNullable(picMap.get("小部件")).orElse(CollUtil.newArrayList()));
            dataModel.put("xbjRowsPan", xbjDataList.size() + 1);

            List<PackTechSpecVo> jcgyDataList = Optional.ofNullable(gyMap.get("基础工艺")).orElse(CollUtil.newArrayList());
            dataModel.put("jcgyDataList", jcgyDataList);
            dataModel.put("jcgyImgList", Optional.ofNullable(picMap.get("基础工艺")).orElse(CollUtil.newArrayList()));
            dataModel.put("jcgyRowsPan", jcgyDataList.size());

            dataModel.put("zysxImgList", Optional.ofNullable(picMap.get("注意事项")).orElse(CollUtil.newArrayList()));
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            String output = writer.toString();
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
//            FileUtil.writeString(output, new File("d://process.html"), Charset.defaultCharset());
//            FileOutputStream fos=new FileOutputStream("D://htmltoPdf.pdf");

//            FileUtil.writeString(output, new File("C:/Users/ZCYLGZ/htmltoPdf.html"), Charset.defaultCharset());
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
            document.setMargins(40, lrMargin, 0, lrMargin);
            for (int i = 1; i < elements.size(); i++) {
                IElement element = elements.get(i);

                // 分页符
                if (element instanceof HtmlPageBreak) {
                    document.add((HtmlPageBreak) element);
                    //普通块级元素
                } else {
                    IBlockElement blockElement = (IBlockElement) element;
                    Table t = (Table) element;

                    if (t.getFooter() != null) {
                        List<IElement> children = t.getFooter().getCell(0, 0).getChildren();
                        IElement iElement = children.get(0);
                        Paragraph h = (Paragraph) iElement;
//                        String text = ((Text) h.getChildren().get(0)).getText();
//                        System.out.println("表格:"+text+":"+t.getNumberOfRows());
//                        h.getChildren().clear();
//                        h.add("你好33333");
                    }
                    document.add(blockElement);
                }
            }

            // 设置页眉中的总页数
            int numberOfPages = pdfDocument.getNumberOfPages();
            for (int i = 1; i <= numberOfPages; i++) {
                Paragraph pageNumber = new Paragraph(String.format(" %d  /  %d ", i, numberOfPages));
                pageNumber.setTextAlignment(TextAlignment.CENTER);
                float x = pdfDocument.getDefaultPageSize().getWidth() - lrMargin * 2 - 5;
                // 距离底部的距离
                float y = pdfDocument.getDefaultPageSize().getTop() - 15;
                document.showTextAligned(pageNumber, x, y, i, TextAlignment.CENTER, VerticalAlignment.TOP, 0);

            }

            document.close();
            return pdfOutputStream;
        } catch (Exception e) {
            throw new OtherException("生成工艺单失败:" + e.getMessage());
        }


    }

    @Data
    class sizeDataDetail {
        private String rowType;
        private String remark;
        private List<Map<String, Object>> rowData;

        public Map<String, Object> toMap() {
            return BeanUtil.beanToMap(this);
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
//            System.out.println(document.getClass().getSimpleName());
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
