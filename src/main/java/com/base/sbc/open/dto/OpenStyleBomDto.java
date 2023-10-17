package com.base.sbc.open.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackBomColorVo;
import com.base.sbc.module.pack.vo.PackBomSizeVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Liu YuBin
 * @version 1.0
 * @date 2023/8/28 11:06
 */
@Data
public class OpenStyleBomDto {
    private String id;
    private String code;
    private String operator;

    /**图片附件表*/
    public OpenAttachmentDto attachment;

    /**尺寸表*/
    public OpenPackSizeDto packSize;

    /**物料清单*/
    public OpenPackBomDto packBom;

    /**工艺说明*/
    public OpenPackTechSpec packTechSpec;

    /**工序工价*/
    public OpenPackProcessDto packProcess;

    /**核价信息*/
    public OpenCostDto packCost;

    /*************************|**********************************图片附件表********************************************************/
    /**
     * 初始化附件，将上传的地址赋值到dto
     * @param attachments         附件集合
     * @param fileMap             上传的文件集合
     */
    public void initAttachmentList(List<Attachment> attachments, Map<String, String> fileMap) {
        if (CollectionUtil.isNotEmpty(attachments)) {
            OpenAttachmentDto dto = new OpenAttachmentDto();
            Attachment attachment = attachments.get(0);
            dto.setCode(this.code);
            dto.setOperator(attachment.getUpdateName());
            List<String> imgList = new ArrayList<>();
            int num = 1;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < attachments.size(); i++) {
                Attachment a = attachments.get(i);
                if (fileMap.get(a.getFileId()) != null) {
                    imgList.add(fileMap.get(a.getFileId()));
                }
                if (StringUtils.isNotBlank(a.getRemarks())){
                    sb.append("第").append(num).append("张：").append(a.getRemarks()).append(";");
                }
                num++;
            }
            if (attachments.size() == 1){
                dto.setRemark(attachment.getRemarks());
            }else{
                dto.setRemark(sb.toString());
            }
            dto.setImgFiles(imgList);
            this.attachment = dto;
        }
    }

    @Data
    public class OpenAttachmentDto{
        private String operator;
        private String code;
        private List<String> imgFiles;
        private String remark;
        private Integer clock = 0;
    }

    /***********************************************************尺寸表********************************************************/
    /**
     * 初始化尺寸表dto数据
     * @param sizes
     * @param sizeMap
     */
    public void initSizeList(List<PackSize> sizes, Map<String, String> sizeMap){
        if (sizes != null && sizes.size() > 0){
            OpenPackSizeDto sizeDto = new OpenPackSizeDto();
            sizeDto.init(sizes,sizeMap);
            sizeDto.setCode(this.code);
            this.packSize = sizeDto;
        }
    }
    @Data
    class OpenPackSizeDto{

        /**
         * 初始化尺寸表主表数据
         * @param sizes
         * @param sizeMap
         */
        public void init(List<PackSize> sizes,Map<String, String> sizeMap) {
            PackSize s = sizes.get(0);
            this.setOperator(s.getUpdateName());
            this.setGradingMan(s.getCreateName());
            this.setGradingDate(s.getCreateDate());
            this.setPatternMan(s.getCreateName());
            this.setMeaUnit(s.getUnit());
            this.setClock(0);

            List<SizeItemDto> sizeItemDtoList = new ArrayList<>();
            SizeItemDto itemDto;
            for (PackSize size : sizes) {
                //尺寸表明细
                size.setPackSizeDetailList(PackUtils.parseSizeDetail(size));

                itemDto = new SizeItemDto();
                itemDto.init(size,sizeMap);
                sizeItemDtoList.add(itemDto);
            }
            this.parts = sizeItemDtoList;
        }

        private String operator;//操作人
        private String code;//款式编码
        private String gradingMan;//放码人
        private Date gradingDate;//放码时间
        private String patternMan;//纸样人
        private String meaUnit;//测量单位
//        private String imgs;//图片地址集合
        private Integer clock;//锁定状态
        private List<SizeItemDto> parts;//尺寸指示测量方法

        @Data
        class SizeItemDto{

            /**
             * 初始化尺寸表详情
             * @param size
             * @param sizeMap
             */
            public void init(PackSize size,Map<String, String> sizeMap) {
                this.setPomCode(size.getPartCode());
                this.setPomName(size.getPartName());
                this.setMethod(size.getMethod());
                this.setErrRange(size.getPositive());//公差范围-正公差+
                this.setDiffRange(size.getCodeError());//档差范围-档差±
                /*
                存入detail尺码数据
                 */
                if (size.getPackSizeDetailList() != null){
                    List<SizeDetail> sizeDetails = new ArrayList<>();
                    SizeDetail sizeDetail;
                    for (PackSizeDetail detail : size.getPackSizeDetailList()) {
                        sizeDetail = new SizeDetail();
                        if (sizeMap != null && sizeMap.get(detail.getSize()) != null){
                            sizeDetail.setSizeCode(sizeMap.get(detail.getSize()));
                        }
                        sizeDetail.setSizeData(detail.getTemplate());
                        sizeDetails.add(sizeDetail);
                    }
                    this.details = sizeDetails;
                }
            }
            private String pomCode;//部位编号
            private String pomName;//部位名称
            private String method;//测量方法
            private String errRange;//误差范围
            private String diffRange;//档差
            private String tmpValue;//模板尺寸
            private List<SizeDetail> details;

            @Data
            class SizeDetail{
                private String sizeCode;//尺码编码
                private String sizeData;//尺码数据
            }
        }
    }

    /***********************************************************物料清单********************************************************/
    /**
     * 组装物料清单数据
     * @param packBoms          bom集合
     * @param packBomColors     bom颜色
     * @param packBomSizes      bom物料
     * @param materialMap       物料档案map
     * @param styleSizeMap   款式尺码编码map
     */
    public void initBomList(List<PackBom> packBoms,
                            List<PackBomColor> packBomColors,
                            List<PackBomSize> packBomSizes,
                            Map<String, BasicsdatumMaterial> materialMap,
                            Map<String, String> styleSizeMap) {
        Map<String, List<PackBomColor>> colorMap = new HashMap<>();
        Map<String, List<PackBomSize>> sizeMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(packBomColors)){
            colorMap = packBomColors.stream().collect(Collectors.groupingBy(c->c.getBomId()));
        }
        if (CollectionUtil.isNotEmpty(packBomSizes)){
            sizeMap = packBomSizes.stream().collect(Collectors.groupingBy(c->c.getBomId()));
        }
        OpenPackBomDto packBomDto = new OpenPackBomDto();
        packBomDto.init(this.code,packBoms,colorMap,sizeMap,materialMap,styleSizeMap);
        this.packBom = packBomDto;
    }
    @Data
    class OpenPackBomDto{
        private String operator;//操作人
        private String code;//款式编码
        private Integer clock;//锁定状态
        private List<OpenMaterialDto> materials;//物料子类

        /**
         * 初始化物料清单list
         * @param code          款号
         * @param packBoms      bom数据
         * @param colorMap      颜色map
         * @param sizeMap       尺码map
         * @param materialMap   物料档案map
         * @param styleSizeMap  款式尺码编码map
         */
        public void init(String code,
                         List<PackBom> packBoms,
                         Map<String, List<PackBomColor>> colorMap,
                         Map<String, List<PackBomSize>> sizeMap,
                         Map<String, BasicsdatumMaterial> materialMap,
                         Map<String, String> styleSizeMap) {
            PackBom p = packBoms.get(0);
            this.operator = p.getUpdateName();
            this.setCode(code);
            this.setClock(0);

            List<OpenMaterialDto> materialDtoList = new ArrayList<>();
            OpenMaterialDto materialDto;
            for (PackBom packBom : packBoms) {
                materialDto = new OpenMaterialDto();
                materialDto.init(packBom,
                        colorMap.get(packBom.getId()),
                        sizeMap.get(packBom.getId()),
                        materialMap.get(packBom.getMaterialCode()),
                        styleSizeMap);
                if (materialMap.get(packBom.getMaterialCode()) != null){
                    materialDto.setBuyType(materialMap.get(packBom.getMaterialCode()).getSourceType());
                }
                materialDtoList.add(materialDto);
            }
            this.materials = materialDtoList;
        }
        @Data
        class OpenMaterialDto{
            private String buyType;//采购类型：指定、自购、加工--物料来源
            private String spCode;//供应商编码--供应商编码
            private String mtCode;//物料编码--物料编号
            private String unit;//单位--采购单位
            private BigDecimal lossRate;//损耗--损耗
            private String usePart;//使用部位--使用部位
            private String width;//门幅，实际测量的门幅--门幅
            private String remark;//物料备注--备注
            private PkgBomEx pkgBomEx;//扩展字段
            private List<OpenMaterialDetail> details;


            /**
             * material初始化
             * @param p
             * @param colorList
             * @param sizeList
             * @param material
             * @param styleSizeMap
             */
            public void init(PackBom p,
                             List<PackBomColor> colorList,
                             List<PackBomSize> sizeList,
                             BasicsdatumMaterial material,
                             Map<String, String> styleSizeMap) {
                this.spCode = p.getSupplierId();
                this.mtCode = p.getMaterialCode();
                this.unit = p.getPurchaseUnitCode();
                this.lossRate = p.getLossRate();
                this.usePart = p.getPartName();
                this.width = material.getTranslate();
                this.remark = p.getRemarks();
                initDetail(p, colorList, sizeList, material, styleSizeMap);

            }

            /**
             * 初始化
             * @param p
             * @param colorList
             * @param sizeList
             * @param material
             * @param styleSizeMap
             */
            public void initDetail(PackBom p,
                             List<PackBomColor> colorList,
                             List<PackBomSize> sizeList,
                             BasicsdatumMaterial material,
                             Map<String, String> styleSizeMap) {
                List<OpenMaterialDetail> detailList = new ArrayList<>();
                OpenMaterialDetail detail;
                if (colorList != null && colorList.size()>0){
                    if (sizeList != null && sizeList.size()>0){
                        for (PackBomColor color : colorList) {
                            for (PackBomSize size : sizeList) {
                                if ("无".equals(size.getWidth())){
//                                    size.setWidthCode("");
                                    size.setWidthCode(null);
                                }
                                //重新set颜色编码
                                if (styleSizeMap.get(size.getSize()) != null){
                                    size.setSizeId(styleSizeMap.get(size.getSize()));
                                }else{
                                    size.setSizeId(null);
                                }
                                detail = new OpenMaterialDetail();
                                detail.init(p,color,size);
                                detailList.add(detail);
                            }
                        }
                    }else{
                        for (PackBomColor color : colorList) {
                            detail = new OpenMaterialDetail();
                            detail.init(p,color,null);
                            detailList.add(detail);
                        }
                    }
                }else if (sizeList != null && sizeList.size()>0){
                    for (PackBomSize size : sizeList) {
                        if ("无".equals(size.getWidth())){
//                            size.setWidthCode("");
                            size.setWidthCode(null);
                        }
                        //重新set颜色编码
                        if (styleSizeMap.get(size.getSize()) != null){
                            size.setSizeId(styleSizeMap.get(size.getSize()));
                        }else{
                            size.setSizeId(null);
                        }
                        detail = new OpenMaterialDetail();
                        detail.init(p,null,size);
                        detailList.add(detail);
                    }
                }
                this.details = detailList;
            }

            @Data
            class OpenMaterialDetail{
                private String size;//尺寸--款式尺码
                private String SkuColor;//SKU颜色--款式颜色
                private String MtColor;//物料颜色--颜色名称
                private String specCode;//物料规格--厂家有效门幅/规格
                private BigDecimal useAmount;//用量--数量

                /**
                 * 初始化配色配码数据
                 * @param p
                 * @param color
                 * @param size
                 */
                public void init(PackBom p, PackBomColor color, PackBomSize size) {
                    //ps 未配色码的物料如何处理？
                    if(size != null){
                        this.size = size.getSizeId();
                        this.specCode= size.getWidthCode();
                        this.useAmount = size.getQuantity();
                    }
                    if(color != null){
                        this.SkuColor = color.getColorCode();
                        this.MtColor = color.getMaterialColorCode();
                    }
                }
            }

            @Data
            class PkgBomEx{
            }
        }
    }

    /***********************************************************工艺说明********************************************************/
    /**
     * 工艺说明初始化
     * @param packTechSpecs
     */
    public void initTechSpecList(List<PackTechSpec> packTechSpecs) {
        if (CollectionUtil.isNotEmpty(packTechSpecs)){
            PackTechSpec techSpec = packTechSpecs.get(0);
            OpenPackTechSpec spec = new OpenPackTechSpec();
            spec.init(this.code,techSpec);
            for (PackTechSpec p : packTechSpecs) {
                if ("裁剪要求".equals(p.getItem())){
                    spec.setCutReqInfo(p.getContent());
                    continue;
                }else if ("后道工序".equals(p.getItem())){
                    spec.setBeolInfo(p.getContent());
                    continue;
                }else if ("验收标准".equals(p.getItem())){
                    spec.setAclInfo(p.getContent());
                    continue;
                }
                spec.init(p);
            }
            this.packTechSpec = spec;
        }

    }
    @Data
    class OpenPackTechSpec{
        private String operator;//操作人--取对应单据的最后修改人
        private String code;//款式编码--款式编码
        private String procMan;//工艺人--单据的首个修改人
        private Date procStartTime;//开始时间--不传
        private Date procEndTime;//结束时间--不传
        private String cutReqInfo;//剪裁要求--取注意事项：对应工艺项目为‘裁剪要求’的描述
//        private List<String> beolInfo;//后道工序--取注意事项：对应工艺项目为‘后道工序‘的描述
        private String beolInfo;//后道工序--取注意事项：对应工艺项目为‘后道工序‘的描述
        private String aclInfo;//验收标准--取注意事项：对应工艺项目为‘验收标准‘的描述
//        private String procBomImgs;//工艺指示bom图片--
//        private Integer clock = 0;//锁定状态--默认传：0

        private List<OpenTechSpecItem> parts;//标准物料清单-工艺指示

        public void init(String s, PackTechSpec p) {
            this.code = s;
            this.operator = p.getUpdateName();
            this.procMan = p.getCreateName();
        }

        public void init(PackTechSpec p) {
            if (this.parts == null){
                List<OpenTechSpecItem> list = new ArrayList<>();
                OpenTechSpecItem item = new OpenTechSpecItem();
                item.setProcType(p.getItem());
                item.setProcDesc(p.getContent());
                item.setImgs(p.getImg());
                list.add(item);
                this.parts = list;
            }else {
                OpenTechSpecItem item = new OpenTechSpecItem();
                item.setProcType(p.getItem());
                item.setProcDesc(p.getContent());
                item.setImgs(p.getImg());
                this.parts.add(item);
            }
        }



        @Data
        class OpenTechSpecItem{
            private String procType;//工艺类别--工艺项目（裁剪工艺、基础工艺、小部件、注意事项、整烫包装）判断对应的工艺类型下的是否有值，有值将工艺项目进行传输
            private String procDesc;//做法--描述
            private String imgs;//工艺指示相关图片--图片取各工艺类型上传的图片
            private Integer clock = 0;//锁定状态--默认传：0
//        private String filePath;//图片地址--
//        private String remark;//备注--不传
        }
    }
    /***********************************************************工序工价********************************************************/
    /**
     * 初始化
     * @param packProcessPrices
     */
    public void initPackProcessPriceList(List<PackProcessPrice> packProcessPrices) {
        if (CollectionUtil.isNotEmpty(packProcessPrices)){
            OpenPackProcessDto dto = new OpenPackProcessDto();
            dto.init(this.code,packProcessPrices);
            this.packProcess = dto;
        }
    }
    @Data
    class OpenPackProcessDto{
        public void init(String code, List<PackProcessPrice> priceList) {
            PackProcessPrice price = priceList.get(0);
            this.code = code;
            this.operator = price.getUpdateName();

            List<OpenPackProcessPartDto> partDtos = new ArrayList<>();
            OpenPackProcessPartDto dto;
            for (PackProcessPrice processPrice : priceList) {
                dto = new OpenPackProcessPartDto();
                dto.setProcName(processPrice.getProcessName());
                dto.setPrice(processPrice.getProcessPrice());
                dto.setRemark(processPrice.getRemarks());
                dto.setProcHours(processPrice.getProcessDate());
                partDtos.add(dto);
            }
            this.parts = partDtos;
        }

        private String operator;//操作人
        private String code;//款式编码
        private Boolean isPsuh = false;//推送到scm的款式是否推送到第三方平台，是true ，否：false 默认false
        private String procMan;//工艺人
        private String procStartTime;//开始时间
        private String procEndTime;//结束时间
        private String mulRate;//倍率

        private List<OpenPackProcessPartDto> parts;

        @Data
        class OpenPackProcessPartDto{
            private String procType;//工艺类别--不传
            private String ProcName;//工序名称--工序名称
            private String ProcGroup = "默认";//工序组--默认传：默认
            private String spCode;//供应商--不传
            private BigDecimal price;//价格--标准单价（元）
            private String remark;//备注--备注
            private BigDecimal procHours;//工时--工时（秒）
            private BigDecimal mulRate = new BigDecimal("1.13");//倍率--默认传值：1.13
            private Integer Seq;//序号--不需要传
            private Integer clock = 0;//锁定状态--默认=0

        }
    }

    /***********************************************************核价信息********************************************************/
    /**
     * 初始化主表信息
     * @param packPricings
     * @param otherCostsList
     * @param costBomList
     * @param materialMap
     * @param processCostsList
     */
    public void initCostList(List<PackPricing> packPricings, List<PackPricingOtherCosts> otherCostsList, List<PackBomVo> costBomList, Map<String, BasicsdatumMaterial> materialMap, List<PackPricingProcessCosts> processCostsList) {
        //初始化主表信息
        if (CollectionUtil.isNotEmpty(packPricings)){
            OpenCostDto cost = new OpenCostDto();
            cost.init(this.code,packPricings,otherCostsList,costBomList,materialMap,processCostsList);
            this.packCost = cost;
        }
    }

    @Data
    class OpenCostDto{

        /**
         * 核价单List
         * @param code                  款号
         * @param packPricings          核价单
         * @param otherCostsList        其他费用（运费）
         * @param costBomList           资料包物料
         * @param materialMap           物料档案map
         * @param processCostsList      加工费
         */
        public void init(String code, List<PackPricing> packPricings, List<PackPricingOtherCosts> otherCostsList, List<PackBomVo> costBomList, Map<String, BasicsdatumMaterial> materialMap, List<PackPricingProcessCosts> processCostsList) {
            PackPricing packPricing = packPricings.get(0);
            this.operator = packPricing.getUpdateName();
            this.code = code;
            Map<String, List<PackPricingProcessCosts>> processMap = processCostsList.stream().collect(Collectors.groupingBy(p -> p.getColorName()));

            //单色不做区分
            List<OpenCostItemDto> list = new ArrayList();
            OpenCostItemDto itemDto;
            for (PackPricing pricing : packPricings) {
                itemDto =  new OpenCostItemDto();
                itemDto.setColor(pricing.getColorCode());
                itemDto.setFeeVerName(pricing.getColorName() + "-核价单");

                //其他费用清单--运费
                List<FeeExt> feeExtList = new ArrayList<>();
                FeeExt feeExt;
                if (CollectionUtil.isNotEmpty(otherCostsList)){
                    //运费
                    for (PackPricingOtherCosts costs : otherCostsList) {
                        if ("trans".equals(costs.getCostsTypeId())
                                && costs.getColorCode() != null
                                && costs.getColorCode().equals(itemDto.getColor())){
                            feeExt = new FeeExt();
                            feeExt.initFreight(costs);
                            feeExtList.add(feeExt);
                        }
                    }
                }
                //其他费用清单--加工费
                List<PackPricingProcessCosts> processMapVar = processMap.get(pricing.getColorName());
                if (CollectionUtil.isNotEmpty(processMapVar)){
                    //加工费
                    feeExt = new FeeExt();
                    feeExt.initProcessing(processMapVar.get(0));
                    BigDecimal num = null;
                    for (PackPricingProcessCosts costs : processMapVar) {
                        num = BigDecimalUtil.add(num,this.processingPrice(costs.getProcessPrice(),costs.getMultiple()));
                    }
                    feeExt.setCleanPrice(num);
                    feeExtList.add(feeExt);
                }
                itemDto.setFeeExts(feeExtList);
                //费用清单--物料费用清单
                if (CollectionUtil.isNotEmpty(costBomList)){
                    List<Fees> feesList = new ArrayList<>();
                    Fees fees;
                    for (PackBomVo vo : costBomList) {
                        fees = new Fees();
                        fees.init(vo,materialMap,pricing.getColorName());
                        feesList.add(fees);
                    }
                    itemDto.setFees(feesList);
                }

                list.add(itemDto);
            }
            this.list = list;
        }

        /**
         * 处理加工费单价
         * @param price         加工费用对应工序行的标准单价
         * @param beiNum        倍数
         * @return
         */
        public BigDecimal processingPrice(BigDecimal price, BigDecimal beiNum){
            //加工费的不含税单价：（（加工费用对应工序行的标准单价 *倍数*1.9）/1.17*1.13）的合计值
            BigDecimal num1 = new BigDecimal("1.9");//固定值1
            BigDecimal num2 = new BigDecimal("1.17");//固定值2
            BigDecimal num3 = new BigDecimal("1.13");//固定值3
            if(price == null){
                price = BigDecimal.ZERO;
            }
            if(beiNum == null){
                beiNum = BigDecimal.ONE;
            }
            return BigDecimalUtil.mul(BigDecimalUtil.div(BigDecimalUtil.mul(BigDecimalUtil.mul(price, beiNum), num1), num2), num3);
        }

        private String operator;//操作人--取对应单据的最后修改人
        private String code;//款式编码--款式编码
        private List<OpenCostItemDto> list;//核价清单--核价的款式‘颜色名称’+‘-’+’核价单‘

        @Data
        class OpenCostItemDto{
            private String color;//款式颜色--颜色
            private String feeVerName;//核价清单版本--核价的款式‘颜色名称’+‘-’+’核价单‘
            private String isMFCheck;//面辅料是否已审核--不传
            private String isOtCheck;//其他费用是否已审核--不传
            private BigDecimal mulRate = new BigDecimal("1.08");//倍率--默认传：1.08
            private Integer clock = 0;//锁定状态--默认传：0

            private List<Fees> fees;//费用清单
            private List<FeeExt> feeExts;//其他费用清单

        }




        @Data
        class Fees{
            private String spCode;//供应商编码
            private String mtCode;//物料编码
            private String usePart;//使用部位
            private String useUnit;//使用单位
            private BigDecimal useAmount;//用量
//            private String lossRate;//损耗
            private BigDecimal lossRate;//损耗
            private BigDecimal cleanPrice;//不含税单价
            private String width;//门幅
            private String remark;//备注
            private BigDecimal taxRate = new BigDecimal("13");//税率
            private String colorInfo;//颜色编码（多个以逗号分割）
            private String specInfo;//规格编码（多个以逗号分割）

            /**
             * 物料费用
             * @param vo
             * @param materialMap
             * @param colorName
             */
            public void init(PackBomVo vo, Map<String, BasicsdatumMaterial> materialMap, String colorName) {
                this.spCode = vo.getSupplierId();
                this.mtCode = vo.getMaterialCode();//物料编码
                this.usePart = vo.getPartName();//使用部位
                this.useUnit = vo.getPurchaseUnitCode();//使用单位

                this.useAmount = vo.getBulkUnitUse();//用量
                this.lossRate = vo.getLossRate();//损耗
                this.cleanPrice = vo.getPrice();//不含税单价
                if (materialMap.get(this.mtCode) != null){
                    this.width = materialMap.get(this.mtCode).getTranslate();//门幅
                }else{
                    this.width = "";//门幅
                }
                this.remark = vo.getRemarks();//备注
                String str = "";
                String bomId = "";
                if (CollectionUtil.isNotEmpty(vo.getPackBomColorVoList())) {
                    for (PackBomColorVo colorVo : vo.getPackBomColorVoList()) {
                        if (colorVo.getColorName() != null && colorVo.getColorName().equals(colorName)){
                            str = colorVo.getMaterialColorCode();
                            bomId = colorVo.getBomId();
                            break;
                        }
                    }
                    this.colorInfo = str;//颜色编码（多个以逗号分割）
                }
                if (CollectionUtil.isNotEmpty(vo.getPackBomSizeList())) {
                    str = "";
                    for (PackBomSizeVo sizeVo : vo.getPackBomSizeList()) {
                        if (sizeVo.getBomId() != null && sizeVo.getBomId().equals(bomId)){
                            if (!"9999".equals(sizeVo.getWidthCode())) {
                                str = sizeVo.getWidthCode();
                            }
                            break;
                        }
                    }
                    this.specInfo = str;//规格编码（多个以逗号分割）
                }
            }
        }

        @Data
        class FeeExt{
            private String ctgCode;//费用编码
            private BigDecimal qty = new BigDecimal("1");//数量
            private String remark;//备注
            private BigDecimal cleanPrice;//不含税单价
            private BigDecimal taxRate = new BigDecimal("0");//税率
            private BigDecimal muRate = new BigDecimal("1");//倍率

            /**
             * 运费初始化
             * @param costs
             */
            public void initFreight(PackPricingOtherCosts costs) {
                this.ctgCode = "21";//费用编码
                this.cleanPrice = costs.getPrice();//不含税单价
                this.remark = costs.getRemarks();//备注
            }

            /**
             * 加工费初始化
             * @param costs
             */
            public void initProcessing(PackPricingProcessCosts costs) {
                this.ctgCode = "01";//费用编码
                this.remark = costs.getRemarks();//备注
            }
        }
    }
}
