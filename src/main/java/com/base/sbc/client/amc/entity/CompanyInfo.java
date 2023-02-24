package com.base.sbc.client.amc.entity;

import java.math.BigDecimal;

/**
 *  amc 实体类
 * @author lilele
 * @data 创建时间:2022/07/6
 */
public class CompanyInfo {

    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区域
     */
    private String area;
    /**
     * 详细地址
     */
    private String detailAddress;
    /**
     * 联系人
     */
    private String concatPeople;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 是否勾选了设计能力(0没勾选 1勾选)
     */
    private String designDisabled;
    /**
     * 是否勾选了加工能力(0没勾选 1勾选)
     */
    private String machiningDisabled;
    /**
     * 是否勾选了面料能力(0没勾选 1勾选)
     */
    private String fabricDisabled;
    /**
     * 是否勾选了货运能力(0没勾选 1勾选)
     */
    private String transportDisabled;
    /**
     * 是否勾选了软件能力(0没勾选 1勾选)
     */
    private String softwareDisabled;
    /**
     * 是否勾选了硬件能力(0没勾选 1勾选)
     */
    private String hardwareDisabled;
    /**
     * 出新频率
     */
    private String designNewFrequency;
    /**
     * 设计师人数
     */
    private Integer designPeopleNum;
    /**
     * 打版和打样师人数
     */
    private Integer designPatternNum;
    /**
     * 设计师工作年限
     */
    private String designWorkYear;
    /**
     * 设计理念
     */
    private String designConcept;
    /**
     * 自主品牌名(多个,隔开)
     */
    private String designIndependentBrand;
    /**
     * 合作品牌名(多个,隔开)
     */
    private String designCooperationBrand;
    /**
     * 设计资质图片(多个,隔开)，四个图，无图中间也要用,隔开
     */
    private String designQualifications;
    /**
     * 设计能力备注
     */
    private String designRemarks;
    /**
     * 工厂类型
     */
    private String machiningFactoryType;
    /**
     * 代工模式
     */
    private String machiningFoundryModel;
    /**
     * 是否支持打样(0不支持，1支持)
     */
    private String machiningIsProofing;
    /**
     * top3生产品类
     */
    private String machiningTopCategory;
    /**
     * 工厂人数
     */
    private String machiningFactoryPeople;
    /**
     * 工厂流水线
     */
    private String machiningAssemblyLine;
    /**
     * 工厂面积
     */
    private String machiningFactoryArea;
    /**
     * 工厂年限
     */
    private String machiningFactoryWorkYear;
    /**
     * 单日最大产能(件/天)
     */
    private String machiningMaxCapacity;
    /**
     * 补货优势 x天能补货x件 两个数字用,隔开
     */
    private String machiningReplenishAdvantage;
    /**
     * 代工品牌 多个,隔开
     */
    private String machiningOemBrand;
    /**
     * 工厂营业执照
     */
    private String machiningBuinessLicense;
    /**
     * 工厂照片(多个,隔开)，六个图，无图中间也要用,隔开
     */
    private String machiningFactoryPhotos;
    /**
     * 加工能力备注
     */
    private String machiningRemarks;
    /**
     * 面料能力
     */
    private String fabricAbility;
    /**
     * 现有面料数量(种)
     */
    private String fabricExistingNum;
    /**
     * 长期面料合作商(多个,隔开)
     */
    private String fabricPartner;
    /**
     * 面料合作商证明
     */
    private String fabricPartnerProve;
    /**
     * 面料能力备注
     */
    private String fabricRemarks;
    /**
     * 大货车数量
     */
    private Integer transportBigTruck;
    /**
     * 中货车数量
     */
    private Integer transportMiddleTruck;
    /**
     * 小货车数量
     */
    private Integer transportSmallTruck;
    /**
     * 搬运小哥
     */
    private Integer transportCarryingBrother;
    /**
     * 基础费用
     */
    private BigDecimal transportBasicPrice;
    /**
     * 货运能力备注
     */
    private String transportRemarks;
    /**
     * 擅长开发类型
     */
    private String softwareGoodDevelop;
    /**
     * 平均开发周期
     */
    private String softwareDevelopCycle;
    /**
     * 开发人员人数
     */
    private String softwareDevelopNum;
    /**
     * 服务人员人数
     */
    private String softwareServiceNum;
    /**
     * 架构师工作年限
     */
    private String softwareArchitectWorkYear;
    /**
     * 使用技术 多个,隔开
     */
    private String softwareTechnologyUse;
    /**
     * 软件理念
     */
    private String softwareIdea;
    /**
     * 软件资质
     */
    private String softwareQualifications;
    /**
     * 现有软件
     */
    private String softwareExisting;
    /**
     * 软件能力备注
     */
    private String softwareRemarks;
    /**
     * 硬件能力
     */
    private String hardwareAbility;
    /**
     * 现有设备种类
     */
    private Integer hardwareEquipmentTypeNum;
    /**
     * 长期合作商 多个,隔开
     */
    private String hardwareCooperation;
    /**
     * 硬件合作商证明
     */
    private String hardwareCooperationProve;
    /**
     * 硬件能力备注
     */
    private String hardwareRemarks;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getConcatPeople() {
        return concatPeople;
    }

    public void setConcatPeople(String concatPeople) {
        this.concatPeople = concatPeople;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignDisabled() {
        return designDisabled;
    }

    public void setDesignDisabled(String designDisabled) {
        this.designDisabled = designDisabled;
    }

    public String getMachiningDisabled() {
        return machiningDisabled;
    }

    public void setMachiningDisabled(String machiningDisabled) {
        this.machiningDisabled = machiningDisabled;
    }

    public String getFabricDisabled() {
        return fabricDisabled;
    }

    public void setFabricDisabled(String fabricDisabled) {
        this.fabricDisabled = fabricDisabled;
    }

    public String getTransportDisabled() {
        return transportDisabled;
    }

    public void setTransportDisabled(String transportDisabled) {
        this.transportDisabled = transportDisabled;
    }

    public String getSoftwareDisabled() {
        return softwareDisabled;
    }

    public void setSoftwareDisabled(String softwareDisabled) {
        this.softwareDisabled = softwareDisabled;
    }

    public String getHardwareDisabled() {
        return hardwareDisabled;
    }

    public void setHardwareDisabled(String hardwareDisabled) {
        this.hardwareDisabled = hardwareDisabled;
    }

    public String getDesignNewFrequency() {
        return designNewFrequency;
    }

    public void setDesignNewFrequency(String designNewFrequency) {
        this.designNewFrequency = designNewFrequency;
    }

    public Integer getDesignPeopleNum() {
        return designPeopleNum;
    }

    public void setDesignPeopleNum(Integer designPeopleNum) {
        this.designPeopleNum = designPeopleNum;
    }

    public Integer getDesignPatternNum() {
        return designPatternNum;
    }

    public void setDesignPatternNum(Integer designPatternNum) {
        this.designPatternNum = designPatternNum;
    }

    public String getDesignWorkYear() {
        return designWorkYear;
    }

    public void setDesignWorkYear(String designWorkYear) {
        this.designWorkYear = designWorkYear;
    }

    public String getDesignConcept() {
        return designConcept;
    }

    public void setDesignConcept(String designConcept) {
        this.designConcept = designConcept;
    }

    public String getDesignIndependentBrand() {
        return designIndependentBrand;
    }

    public void setDesignIndependentBrand(String designIndependentBrand) {
        this.designIndependentBrand = designIndependentBrand;
    }

    public String getDesignCooperationBrand() {
        return designCooperationBrand;
    }

    public void setDesignCooperationBrand(String designCooperationBrand) {
        this.designCooperationBrand = designCooperationBrand;
    }

    public String getDesignQualifications() {
        return designQualifications;
    }

    public void setDesignQualifications(String designQualifications) {
        this.designQualifications = designQualifications;
    }

    public String getDesignRemarks() {
        return designRemarks;
    }

    public void setDesignRemarks(String designRemarks) {
        this.designRemarks = designRemarks;
    }

    public String getMachiningFactoryType() {
        return machiningFactoryType;
    }

    public void setMachiningFactoryType(String machiningFactoryType) {
        this.machiningFactoryType = machiningFactoryType;
    }

    public String getMachiningFoundryModel() {
        return machiningFoundryModel;
    }

    public void setMachiningFoundryModel(String machiningFoundryModel) {
        this.machiningFoundryModel = machiningFoundryModel;
    }

    public String getMachiningIsProofing() {
        return machiningIsProofing;
    }

    public void setMachiningIsProofing(String machiningIsProofing) {
        this.machiningIsProofing = machiningIsProofing;
    }

    public String getMachiningTopCategory() {
        return machiningTopCategory;
    }

    public void setMachiningTopCategory(String machiningTopCategory) {
        this.machiningTopCategory = machiningTopCategory;
    }

    public String getMachiningFactoryPeople() {
        return machiningFactoryPeople;
    }

    public void setMachiningFactoryPeople(String machiningFactoryPeople) {
        this.machiningFactoryPeople = machiningFactoryPeople;
    }

    public String getMachiningAssemblyLine() {
        return machiningAssemblyLine;
    }

    public void setMachiningAssemblyLine(String machiningAssemblyLine) {
        this.machiningAssemblyLine = machiningAssemblyLine;
    }

    public String getMachiningFactoryArea() {
        return machiningFactoryArea;
    }

    public void setMachiningFactoryArea(String machiningFactoryArea) {
        this.machiningFactoryArea = machiningFactoryArea;
    }

    public String getMachiningFactoryWorkYear() {
        return machiningFactoryWorkYear;
    }

    public void setMachiningFactoryWorkYear(String machiningFactoryWorkYear) {
        this.machiningFactoryWorkYear = machiningFactoryWorkYear;
    }

    public String getMachiningMaxCapacity() {
        return machiningMaxCapacity;
    }

    public void setMachiningMaxCapacity(String machiningMaxCapacity) {
        this.machiningMaxCapacity = machiningMaxCapacity;
    }

    public String getMachiningReplenishAdvantage() {
        return machiningReplenishAdvantage;
    }

    public void setMachiningReplenishAdvantage(String machiningReplenishAdvantage) {
        this.machiningReplenishAdvantage = machiningReplenishAdvantage;
    }

    public String getMachiningOemBrand() {
        return machiningOemBrand;
    }

    public void setMachiningOemBrand(String machiningOemBrand) {
        this.machiningOemBrand = machiningOemBrand;
    }

    public String getMachiningBuinessLicense() {
        return machiningBuinessLicense;
    }

    public void setMachiningBuinessLicense(String machiningBuinessLicense) {
        this.machiningBuinessLicense = machiningBuinessLicense;
    }

    public String getMachiningFactoryPhotos() {
        return machiningFactoryPhotos;
    }

    public void setMachiningFactoryPhotos(String machiningFactoryPhotos) {
        this.machiningFactoryPhotos = machiningFactoryPhotos;
    }

    public String getMachiningRemarks() {
        return machiningRemarks;
    }

    public void setMachiningRemarks(String machiningRemarks) {
        this.machiningRemarks = machiningRemarks;
    }

    public String getFabricAbility() {
        return fabricAbility;
    }

    public void setFabricAbility(String fabricAbility) {
        this.fabricAbility = fabricAbility;
    }

    public String getFabricExistingNum() {
        return fabricExistingNum;
    }

    public void setFabricExistingNum(String fabricExistingNum) {
        this.fabricExistingNum = fabricExistingNum;
    }

    public String getFabricPartner() {
        return fabricPartner;
    }

    public void setFabricPartner(String fabricPartner) {
        this.fabricPartner = fabricPartner;
    }

    public String getFabricPartnerProve() {
        return fabricPartnerProve;
    }

    public void setFabricPartnerProve(String fabricPartnerProve) {
        this.fabricPartnerProve = fabricPartnerProve;
    }

    public String getFabricRemarks() {
        return fabricRemarks;
    }

    public void setFabricRemarks(String fabricRemarks) {
        this.fabricRemarks = fabricRemarks;
    }

    public Integer getTransportBigTruck() {
        return transportBigTruck;
    }

    public void setTransportBigTruck(Integer transportBigTruck) {
        this.transportBigTruck = transportBigTruck;
    }

    public Integer getTransportMiddleTruck() {
        return transportMiddleTruck;
    }

    public void setTransportMiddleTruck(Integer transportMiddleTruck) {
        this.transportMiddleTruck = transportMiddleTruck;
    }

    public Integer getTransportSmallTruck() {
        return transportSmallTruck;
    }

    public void setTransportSmallTruck(Integer transportSmallTruck) {
        this.transportSmallTruck = transportSmallTruck;
    }

    public Integer getTransportCarryingBrother() {
        return transportCarryingBrother;
    }

    public void setTransportCarryingBrother(Integer transportCarryingBrother) {
        this.transportCarryingBrother = transportCarryingBrother;
    }

    public BigDecimal getTransportBasicPrice() {
        return transportBasicPrice;
    }

    public void setTransportBasicPrice(BigDecimal transportBasicPrice) {
        this.transportBasicPrice = transportBasicPrice;
    }

    public String getTransportRemarks() {
        return transportRemarks;
    }

    public void setTransportRemarks(String transportRemarks) {
        this.transportRemarks = transportRemarks;
    }

    public String getSoftwareGoodDevelop() {
        return softwareGoodDevelop;
    }

    public void setSoftwareGoodDevelop(String softwareGoodDevelop) {
        this.softwareGoodDevelop = softwareGoodDevelop;
    }

    public String getSoftwareDevelopCycle() {
        return softwareDevelopCycle;
    }

    public void setSoftwareDevelopCycle(String softwareDevelopCycle) {
        this.softwareDevelopCycle = softwareDevelopCycle;
    }

    public String getSoftwareDevelopNum() {
        return softwareDevelopNum;
    }

    public void setSoftwareDevelopNum(String softwareDevelopNum) {
        this.softwareDevelopNum = softwareDevelopNum;
    }

    public String getSoftwareServiceNum() {
        return softwareServiceNum;
    }

    public void setSoftwareServiceNum(String softwareServiceNum) {
        this.softwareServiceNum = softwareServiceNum;
    }

    public String getSoftwareArchitectWorkYear() {
        return softwareArchitectWorkYear;
    }

    public void setSoftwareArchitectWorkYear(String softwareArchitectWorkYear) {
        this.softwareArchitectWorkYear = softwareArchitectWorkYear;
    }

    public String getSoftwareTechnologyUse() {
        return softwareTechnologyUse;
    }

    public void setSoftwareTechnologyUse(String softwareTechnologyUse) {
        this.softwareTechnologyUse = softwareTechnologyUse;
    }

    public String getSoftwareIdea() {
        return softwareIdea;
    }

    public void setSoftwareIdea(String softwareIdea) {
        this.softwareIdea = softwareIdea;
    }

    public String getSoftwareQualifications() {
        return softwareQualifications;
    }

    public void setSoftwareQualifications(String softwareQualifications) {
        this.softwareQualifications = softwareQualifications;
    }

    public String getSoftwareExisting() {
        return softwareExisting;
    }

    public void setSoftwareExisting(String softwareExisting) {
        this.softwareExisting = softwareExisting;
    }

    public String getSoftwareRemarks() {
        return softwareRemarks;
    }

    public void setSoftwareRemarks(String softwareRemarks) {
        this.softwareRemarks = softwareRemarks;
    }

    public String getHardwareAbility() {
        return hardwareAbility;
    }

    public void setHardwareAbility(String hardwareAbility) {
        this.hardwareAbility = hardwareAbility;
    }

    public Integer getHardwareEquipmentTypeNum() {
        return hardwareEquipmentTypeNum;
    }

    public void setHardwareEquipmentTypeNum(Integer hardwareEquipmentTypeNum) {
        this.hardwareEquipmentTypeNum = hardwareEquipmentTypeNum;
    }

    public String getHardwareCooperation() {
        return hardwareCooperation;
    }

    public void setHardwareCooperation(String hardwareCooperation) {
        this.hardwareCooperation = hardwareCooperation;
    }

    public String getHardwareCooperationProve() {
        return hardwareCooperationProve;
    }

    public void setHardwareCooperationProve(String hardwareCooperationProve) {
        this.hardwareCooperationProve = hardwareCooperationProve;
    }

    public String getHardwareRemarks() {
        return hardwareRemarks;
    }

    public void setHardwareRemarks(String hardwareRemarks) {
        this.hardwareRemarks = hardwareRemarks;
    }
}
