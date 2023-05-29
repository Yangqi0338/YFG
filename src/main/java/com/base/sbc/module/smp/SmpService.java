package com.base.sbc.module.smp;

import com.base.sbc.config.restTemplate.RestTemplateUtils;
import com.base.sbc.module.smp.base.SmpBaseDto;
import com.base.sbc.module.smp.dto.*;
import com.base.sbc.module.smp.entity.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author 卞康
 * @date 2023/5/8 15:27:43
 * @mail 247967116@qq.com
 * 对接下发Smp主数据
 */
@Service
public class SmpService {


    //private  static final String URL ="http://10.88.34.25:7006/pdm";
    private static final String URL = "http://smp-i.eifini.com/service-manager/pdm";


    /**
     * 商品主数据下发
     */
    public Boolean goods(SmpGoodsDto smpGoodsDto) {
        return RestTemplateUtils.spmPost(URL + "/goods", smpGoodsDto);
    }

    /**
     * 物料主数据下发
     */
    public Boolean materials(SmpMaterialDto smpMaterialDto) {
        return RestTemplateUtils.spmPost(URL + "/materials", smpMaterialDto);
    }

    /**
     * bom下发
     */
    public Boolean bom(SmpBomDto smpBomDto) {
        return RestTemplateUtils.spmPost(URL + "/bom", smpBomDto);
    }

    /**
     * 颜色主数据下发
     */
    public Boolean color(SmpColorDto smpColorDto) {
        return RestTemplateUtils.spmPost(URL + "/color", smpColorDto);
    }

    /**
     * 工艺单下发
     */
    public Boolean processSheet(SmpProcessSheetDto smpProcessSheetDto) {
        return RestTemplateUtils.spmPost(URL + "/processSheet", smpProcessSheetDto);
    }

    /**
     * 样衣下发
     */
    public Boolean sample(SmpSampleDto smpSampleDto) {
        return RestTemplateUtils.spmPost(URL + "/sample", smpSampleDto);
    }

    /**
     * 修改尺码的时候验证
     */
    public Boolean style(PlmStyleSizeParam param) {
        return RestTemplateUtils.spmPost("http://10.8.250.100:1980/escm-app/information/plm/style", param);
    }


    public static void main(String[] args) {
        SmpService smpService = new SmpService();
        smpService.testGoods();
        //smpService.testMaterials();
        //smpService.testBom();
        //smpService.testColor();
        //smpService.testProcessSheet();
        //smpService.testSample();
        //smpService.testStyle();
    }

    public void testGoods() {
        SmpGoodsDto smpGoodsDto = new SmpGoodsDto();
        smpGoodsDto.setCreator("John Doe");
        smpGoodsDto.setCreateTime(new Date());
        smpGoodsDto.setModifiedPerson("Jane Smith");
        smpGoodsDto.setModifiedTime(new Date());
        smpGoodsDto.setPlmId("C24886483");
        smpGoodsDto.setSyncId("3504b7a1-cd2c-11ed-bd34-5b5396896b56");
        smpGoodsDto.setActive(true);
        smpGoodsDto.setProductTypeId("PT001");
        smpGoodsDto.setProductType("Shirt");
        smpGoodsDto.setBrandId("B001");
        smpGoodsDto.setBrandName("Nike");
        smpGoodsDto.setYear("2023");
        smpGoodsDto.setSeason("Summer");
        smpGoodsDto.setTheme("Sport");
        smpGoodsDto.setUnit("Piece");
        smpGoodsDto.setStyleMiddleClass("MiddleClass");
        smpGoodsDto.setStyleSmallClass("SmallClass");
        smpGoodsDto.setDesignNumber("DN001");
        smpGoodsDto.setStyleName("Sport Shirt");
        smpGoodsDto.setDesignerId("D001");
        smpGoodsDto.setDesigner("John Doe");
        smpGoodsDto.setTechnician("Jane Smith");
        smpGoodsDto.setTechnicianId("T001");
        smpGoodsDto.setTargetCost(new BigDecimal("100.50"));
        smpGoodsDto.setPlanningRate(new BigDecimal("1.2"));
        smpGoodsDto.setStyleCategory("Category1");
        smpGoodsDto.setStyleBigClass("BigClass");
        smpGoodsDto.setMainPush(true);
        smpGoodsDto.setProductionType("TypeA");
        smpGoodsDto.setRegion("Region1");
        smpGoodsDto.setSalesGroup("Group1");
        smpGoodsDto.setDesignScore(8);
        smpGoodsDto.setSizeGroupId("SG001");
        smpGoodsDto.setSizeGroupName("SizeGroup1");
        smpGoodsDto.setPatternMakerName("PatternMaker1");
        smpGoodsDto.setPatternMakerId("PM001");
        smpGoodsDto.setMaxClassName("MaxClass");
        smpGoodsDto.setMiddleClassName("MiddleClass");
        smpGoodsDto.setMinClassName("MinClass");
        smpGoodsDto.setStyleCode("SC001");
        smpGoodsDto.setCategoryName("Category1");
        smpGoodsDto.setLengthRangeId("LR001");
        smpGoodsDto.setLengthRangeName("LengthRange1");
        smpGoodsDto.setCoatLength("Long");
        smpGoodsDto.setWaistTypeId("WT001");
        smpGoodsDto.setWaistTypeName("WaistType1");
        smpGoodsDto.setSleeveLengthId("SL001");
        smpGoodsDto.setSleeveLengthName("SleeveLength1");
        smpGoodsDto.setSleeveId("S001");
        smpGoodsDto.setSleeveName("Sleeve1");
        smpGoodsDto.setBust("90");
        smpGoodsDto.setPlacketId("PI001");
        smpGoodsDto.setPlacketName("Placket1");
        smpGoodsDto.setYarnNeedleTypeId("YN001");
        smpGoodsDto.setYarnNeedleTypeName("YarnNeedleType1");
        smpGoodsDto.setYarnNeedleId("YN001");
        smpGoodsDto.setYarnNeedleName("YarnNeedle1");
        smpGoodsDto.setProfileId("PF001");
        smpGoodsDto.setProfileName("Profile1");
        smpGoodsDto.setFlowerId("FL001");
        smpGoodsDto.setFlowerName("Flower1");
        smpGoodsDto.setShapeName("Shape1");
        smpGoodsDto.setTextureId("TX001");
        smpGoodsDto.setTextureName("Texture1");
        smpGoodsDto.setPatternName("Pattern1");
        smpGoodsDto.setPriorityId("PR001");
        smpGoodsDto.setPriorityName("Priority1");
        smpGoodsDto.setColorCode("C001");
        smpGoodsDto.setColorName("Red");
        smpGoodsDto.setBandId("B001");
        smpGoodsDto.setBandName("Band1");
        smpGoodsDto.setPrice(new BigDecimal("99.99"));
        smpGoodsDto.setPriceConfirm(true);
        smpGoodsDto.setCost(new BigDecimal("50.50"));
        smpGoodsDto.setPlanCost(new BigDecimal("60.60"));
        smpGoodsDto.setActualRate(new BigDecimal("1.5"));
        smpGoodsDto.setPlanActualRate(new BigDecimal("1.3"));
        smpGoodsDto.setProcessCost(new BigDecimal("10.00"));
        smpGoodsDto.setLaborCosts(new BigDecimal("5.00"));
        smpGoodsDto.setMaterialCost(new BigDecimal("15.00"));
        smpGoodsDto.setProductName("Product1");
        smpGoodsDto.setUniqueCode("UC001");
        smpGoodsDto.setSeries("Series1");
        smpGoodsDto.setAccessories(false);
        smpGoodsDto.setManufacture("Manufacturer1");
        smpGoodsDto.setSaleTime(new Date());
        smpGoodsDto.setSeriesId("SI001");
        smpGoodsDto.setSeriesName("Series1");
        smpGoodsDto.setLuxury(true);
        smpGoodsDto.setBomPhase("Phase1");
        smpGoodsDto.setAuProcess(false);
        smpGoodsDto.setSupplierArticle("SA001");
        smpGoodsDto.setSupplierArticleColor("SAC001");
        smpGoodsDto.setPackageType("Type1");
        smpGoodsDto.setPackageSize("Size1");
        smpGoodsDto.setProdSeg("Segment1");
        smpGoodsDto.setSaleType("TypeA");
        smpGoodsDto.setBulkNumber("BN001");
        smpGoodsDto.setComposition("Composition1");
        smpGoodsDto.setMainCode("MC001");
        smpGoodsDto.setSecCode("SC001");
        smpGoodsDto.setLingXingId("LX001");
        smpGoodsDto.setLingXingName("LingXing1");
        smpGoodsDto.setIntegritySample(true);
        smpGoodsDto.setIntegrityProduct(true);

// 设置尺码集合
        List<SmpSize> itemList = new ArrayList<>();

        SmpSize smpSize = new SmpSize();
        smpSize.setSize("XS");
        smpSize.setSizeNumber("5");
        smpSize.setSizeDescription("Extra Small");
        smpSize.setCode("001");
        smpSize.setProductSizeName("XS");
        smpSize.setBaseSize(true);
        smpSize.setSkuFiller("100g");
        smpSize.setSpecialSpec("N/A");
        itemList.add(smpSize);
        SmpSize smpSize2 = new SmpSize();
        smpSize2.setSize("S");
        smpSize2.setSizeNumber("1");
        smpSize2.setSizeDescription("Small");
        smpSize2.setCode("002");
        smpSize2.setProductSizeName("S");
        smpSize2.setBaseSize(false);
        smpSize2.setSkuFiller("150g");
        smpSize2.setSpecialSpec("N/A");
        itemList.add(smpSize2);
        smpGoodsDto.setItemList(itemList);

// 设置图片地址集合
        List<String> imgList = new ArrayList<>();
        imgList.add("https://example.com/image1.jpg");
        imgList.add("https://example.com/image2.jpg");
        smpGoodsDto.setImgList(imgList);
        this.goods(smpGoodsDto);
    }

    public void testMaterials() {
        SmpMaterialDto smpMaterialDto = new SmpMaterialDto();

        // 设置 SmpBaseDto 属性
        smpMaterialDto.setCreator("John Doe");
        smpMaterialDto.setCreateTime(new Date());
        smpMaterialDto.setModifiedPerson("Jane Smith");
        smpMaterialDto.setModifiedTime(new Date());
        smpMaterialDto.setPlmId("C24886483");
        smpMaterialDto.setSyncId("3504b7a1-cd2c-11ed-bd34-5b5396896b56");
        smpMaterialDto.setActive(true);

        // 设置 SmpMaterialDto 属性
        smpMaterialDto.setMaterialCode("M12345");
        smpMaterialDto.setMaterialName("Material ABC");
        smpMaterialDto.setMaterialUnit("pcs");
        smpMaterialDto.setStockUnit("pcs");
        smpMaterialDto.setThirdLevelCategory("Category C");
        smpMaterialDto.setMaterialSource("Source A");
        smpMaterialDto.setSecondLevelCategory("Category B");
        smpMaterialDto.setSeasonYear("2023");
        smpMaterialDto.setSeasonQuarter("Q2");
        smpMaterialDto.setSeasonQuarterId("Q2-2023");
        smpMaterialDto.setSeasonBrand("Brand X");
        smpMaterialDto.setSeasonBrandId("BrandX-2023");
        smpMaterialDto.setKilogramsAndMeters("10");
        smpMaterialDto.setDeveloper("John Smith");
        smpMaterialDto.setBuyer("Jane Doe");
        smpMaterialDto.setBuyerTeam("Team A");
        smpMaterialDto.setLongitudinalShrinkage(new BigDecimal("5.2"));
        smpMaterialDto.setWeftShrinkage(new BigDecimal("3.8"));
        smpMaterialDto.setWeight("200");
        smpMaterialDto.setComposition("Cotton");
        smpMaterialDto.setJIT(true);
        smpMaterialDto.setProductTypeId("T123");
        smpMaterialDto.setProductType("Type A");
        smpMaterialDto.setProcurementMode("Mode B");
        smpMaterialDto.setTolerance(new BigDecimal("10.5"));
        smpMaterialDto.setSupplierComposition("Cotton");
        smpMaterialDto.setPickingMethod("Method X");

        // 设置颜色集合
        List<SmpColor> colorList = new ArrayList<>();
        SmpColor color1 = new SmpColor();
        color1.setColorCode("C001");
        color1.setColorName("Red");
        color1.setActive("Y");
        color1.setSupplierMatColor("Supplier Red");
        colorList.add(color1);
        SmpColor color2 = new SmpColor();
        color2.setColorCode("C002");
        color2.setColorName("Blue");
        color2.setActive("Y");
        color2.setSupplierMatColor("Supplier Blue");
        colorList.add(color2);
        smpMaterialDto.setColorList(colorList);

        // 设置材料尺码集合
        List<SmpModuleSize> moduleSizeList = new ArrayList<>();
        SmpModuleSize size1 = new SmpModuleSize();
        size1.setSizeCode("S");
        size1.setSizeName("Small");
        size1.setSortCode("1");
        size1.setCode("SC001");
        size1.setActive("Y");
        size1.setSizeUrl("https://example.com/size1");
        moduleSizeList.add(size1);

        SmpModuleSize size2 = new SmpModuleSize();
        size2.setSizeCode("M");
        size2.setSizeName("Medium");
        size2.setSortCode("2");
        size2.setCode("SC002");
        size2.setActive("Y");
        size2.setSizeUrl("https://example.com/size2");
        moduleSizeList.add(size2);

        smpMaterialDto.setModuleSizeList(moduleSizeList);

// 设置报价集合
        List<SmpQuot> quotList = new ArrayList<>();
        SmpQuot quot1 = new SmpQuot();
        quot1.setSupplierSize("1000mm");
        quot1.setSizeUrl("https://example.com/size1");
        quot1.setSupplierColorId("C001");
        quot1.setSupplierColorName("Red");
        quot1.setOrderGoodsDay("7");
        quot1.setProductionDay("14");
        quot1.setMoqInitial(100);
        quot1.setFobFullPrice(new BigDecimal("10.50"));
        quot1.setSupplierMaterial("M12345");
        quot1.setSupplierCode("SUP001");
        quot1.setSupplierName("Supplier A");
        quot1.setComment("Sample comment");
        quot1.setTradeTermKey("TT001");
        quot1.setDefaultQuote(true);
        quot1.setTradeTermName("Term A");
        quot1.setMaterialUom("pcs");
        quotList.add(quot1);

        SmpQuot quot2 = new SmpQuot();
        quot2.setSupplierSize("2000mm");
        quot2.setSizeUrl("https://example.com/size2");
        quot2.setSupplierColorId("C002");
        quot2.setSupplierColorName("Blue");
        quot2.setOrderGoodsDay("10");
        quot2.setProductionDay("21");
        quot2.setMoqInitial(200);
        quot2.setFobFullPrice(new BigDecimal("20.50"));
        quot2.setSupplierMaterial("M54321");
        quot2.setSupplierCode("SUP002");
        quot2.setSupplierName("Supplier B");
        quot2.setComment("Another comment");
        quot2.setTradeTermKey("TT002");
        quot2.setDefaultQuote(false);
        quot2.setTradeTermName("Term B");
        quot2.setMaterialUom("pcs");
        quotList.add(quot2);

        smpMaterialDto.setQuotList(quotList);

// 设置图片地址集合
        List<String> imgList = new ArrayList<>();
        imgList.add("https://example.com/image1.jpg");
        imgList.add("https://example.com/image2.jpg");
        imgList.add("https://example.com/image3.jpg");
        smpMaterialDto.setImgList(imgList);
        this.materials(smpMaterialDto);
    }

    public void testBom() {
        SmpBomDto smpBomDto = new SmpBomDto();
        smpBomDto.setCreator("John");
        smpBomDto.setCreateTime(new Date());
        smpBomDto.setModifiedPerson("Alice");
        smpBomDto.setModifiedTime(new Date());
        smpBomDto.setPlmId("PLM001");
        smpBomDto.setSyncId("SYNC001");
        smpBomDto.setActive(true);

        // 设置 SmpBomDto 的其他值
        smpBomDto.setBomCode("BOM001");
        smpBomDto.setPColorName("Red");
        smpBomDto.setPColorCode("C001");
        smpBomDto.setBulkNumber("BN001");
        smpBomDto.setColorName("Blue");
        smpBomDto.setColorCode("C002");
        smpBomDto.setBomStage("Stage A");
        smpBomDto.setMaterialCode("M001");
        smpBomDto.setMaterialName("Material A");
        smpBomDto.setMaterialUnit("pcs");
        smpBomDto.setPlaceOfUse("Front");
        smpBomDto.setLossRate(new BigDecimal("0.1"));
        smpBomDto.setSupplierMaterialCode("SUPM001");
        smpBomDto.setQuotationSupplierCode("SUP001");
        smpBomDto.setCollocation("Collocation A");
        smpBomDto.setBomLineItemId("BLI001");
        smpBomDto.setMainMaterial(true);

        // 设置单件用量集合
        List<SmpSizeQty> sizeQtyList = new ArrayList<>();
        SmpSizeQty sizeQty1 = new SmpSizeQty();
        sizeQty1.setPSizeCode("XS");
        sizeQty1.setSizeCode("SC001");
        sizeQty1.setItemSize("1000mm");
        sizeQty1.setMatSizeUrl("https://example.com/mat-size-url1");
        sizeQty1.setItemQty(new BigDecimal("2.5"));
        sizeQtyList.add(sizeQty1);

        SmpSizeQty sizeQty2 = new SmpSizeQty();
        sizeQty2.setPSizeCode("S");
        sizeQty2.setSizeCode("SC002");
        sizeQty2.setItemSize("2000mm");
        sizeQty2.setMatSizeUrl("https://example.com/mat-size-url2");
        sizeQty2.setItemQty(new BigDecimal("3.5"));
        sizeQtyList.add(sizeQty2);

        smpBomDto.setSizeQtyList(sizeQtyList);

        this.bom(smpBomDto);
    }

    public void testColor() {
        SmpColorDto smpColorDto = new SmpColorDto();

        // 设置 SmpColorDto 的值
        smpColorDto.setCreator("John");
        smpColorDto.setCreateTime(new Date());
        smpColorDto.setModifiedPerson("Alice");
        smpColorDto.setModifiedTime(new Date());
        smpColorDto.setPlmId("PLM001");
        smpColorDto.setSyncId("SYNC001");
        smpColorDto.setActive(true);

        smpColorDto.setColorName("Red");
        smpColorDto.setColorCode("C001");
        smpColorDto.setColorType("Type A");
        smpColorDto.setColorTypeName("Type A Name");
        smpColorDto.setColorChromaId("Chroma001");
        smpColorDto.setColorChroma("Chroma A");
        smpColorDto.setRange("Style");

        this.color(smpColorDto);
    }

    public void testProcessSheet(){
        SmpProcessSheetDto smpProcessSheetDto = new SmpProcessSheetDto();

        // 设置 SmpProcessSheetDto 的值
        smpProcessSheetDto.setCreator("John");
        smpProcessSheetDto.setCreateTime(new Date());
        smpProcessSheetDto.setModifiedPerson("Alice");
        smpProcessSheetDto.setModifiedTime(new Date());
        smpProcessSheetDto.setPlmId("PLM001");
        smpProcessSheetDto.setSyncId("SYNC001");
        smpProcessSheetDto.setActive(true);

        smpProcessSheetDto.setBulkNumber("B001");
        smpProcessSheetDto.setPdfUrl("path/to/pdf");
        this.processSheet(smpProcessSheetDto);
    }

    public void testSample(){
        SmpSampleDto smpSampleDto = new SmpSampleDto();
        smpSampleDto.setProofingDesigner("John Doe");
        smpSampleDto.setProofingDesignerId("123456");
        smpSampleDto.setBExtAuxiliary(true);
        smpSampleDto.setBarcode("SAMPLE001");
        smpSampleDto.setEAValidFromTime(new Date());
        smpSampleDto.setEAValidToTime("2023-05-20");
        smpSampleDto.setFinished(false);
        smpSampleDto.setMCDate("2023-05-25");
        smpSampleDto.setPmlId("PML001");
        smpSampleDto.setPatDiff("DIFF001");
        smpSampleDto.setPatDiffName("Difficult");
        smpSampleDto.setPatSeq("SEQ001");
        smpSampleDto.setPatSeqName("Sequential");
        smpSampleDto.setSampleNumber("SAMPLENUM001");
        smpSampleDto.setSampleNumberName("Sample Number 001");
        smpSampleDto.setColorwayCode("COLORWAY001");
        smpSampleDto.setColorwayPlmId("PLM001");
        smpSampleDto.setNodeName("Sample Node");
        smpSampleDto.setSampleReceivedDate("2023-05-15");
        smpSampleDto.setSampleStatus("STATUS001");
        smpSampleDto.setSampleStatusName("Sample Status");
        smpSampleDto.setSampleType("TYPE001");
        smpSampleDto.setSampleTypeName("Sample Type");
        smpSampleDto.setMajorCategories("CATEGORY001");
        smpSampleDto.setMajorCategoriesName("Major Category");
        smpSampleDto.setCategory("SUBCATEGORY001");
        smpSampleDto.setCategoryName("Subcategory");
        smpSampleDto.setBrandCode("BRAND001");
        smpSampleDto.setBrandName("Brand");
        smpSampleDto.setQuarterCode("QTR001");
        smpSampleDto.setYear("2023");
        smpSampleDto.setDesigner("Designer Name");
        smpSampleDto.setDesignerId("D123");
        smpSampleDto.setPatternMaker("Pattern Maker Name");
        smpSampleDto.setPatternMakerId("PM123");
        smpSampleDto.setTechnician("Technician Name");
        smpSampleDto.setTechnicianId("T123");
        smpSampleDto.setMiddleClassId("MID001");
        smpSampleDto.setMiddleClassName("Middle Class");
        smpSampleDto.setStyleUrl("https://example.com/style");
        smpSampleDto.setStyleCode("STYLE001");
        smpSampleDto.setSupplier("Supplier Name");
        smpSampleDto.setSupplierNumber("SUP001");

        List<String> imgList = new ArrayList<>();
        imgList.add("image1.jpg");
        imgList.add("image2.jpg");
        smpSampleDto.setImgList(imgList);

        this.sample(smpSampleDto);
    }

    public void testStyle(){
        PlmStyleSizeParam plmStyleSizeParam = new PlmStyleSizeParam();
        plmStyleSizeParam.setStyleNo("STYLE001");
        plmStyleSizeParam.setSizeCategory("Size Category");
        plmStyleSizeParam.setSizeNum(5);
        this.style(plmStyleSizeParam);
    }

}

