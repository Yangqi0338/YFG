/**
 * @Title：SampleBean.java
 * @Package com.eifini.mps.interfaces.server.plm.sample.bean
 * @Desc TODO
 * @Author lenovo
 * @Date 2020年6月12日
 * @Version V1.0
 */
package com.base.sbc.module.smp.dto;

import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Desc TODO
 * @Author lenovo
 * @Date 2020年6月12日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleBean {

	/**	样衣名	【例：	120S1103WW/PP样	】	*/
	@JsonProperty("NodeName")
	private String NodeName;

	/**	样衣类型	【例：	PP样	】	*/
	@JsonProperty("SampleType")
	private String SampleType;

	/**	样衣类型:名称	【例：	产前样	】	*/
	@JsonProperty("SampleTypeName")
	private String SampleTypeName;

	/**	打版难度	【例：	97	】	*/
	@JsonProperty("C8_Sample_PatDiff")
	private String C8_Sample_PatDiff;

	/**	打版难度:名称	【例：	Z(产前样)	】	*/
	@JsonProperty("C8_Sample_PatDiffName")
	private String C8_Sample_PatDiffName;

	/**	打样顺序	【例：	2401	】	*/
	@JsonProperty("C8_Sample_PatSeq")
	private String C8_Sample_PatSeq;

	/**	打样顺序:名称	【例：	Z1	】	*/
	@JsonProperty("C8_Sample_PatSeqName")
	private String C8_Sample_PatSeqName;

	/**	版号	【例：		】	*/
	@JsonProperty("C8_Sample_SampleNumber")
	private String C8_Sample_SampleNumber;

	/**	版号:名称	【例：		】	*/
	@JsonProperty("C8_Sample_SampleNumberName")
	private String C8_Sample_SampleNumberName;

	/**	外辅标志	【例：	FALSE	】	*/
	@JsonProperty("C8_Sample_BExtAuxiliary")
	private String C8_Sample_BExtAuxiliary;

	/**	外辅发出时间	【例：		】	*/
	@JsonProperty("C8_Sample_EAValidFrom")
	private String C8_Sample_EAValidFrom;

	/**	外辅接收时间	【例：		】	*/
	@JsonProperty("C8_Sample_EAValidTo")
	private String C8_Sample_EAValidTo;

	/**	样衣条码	【例：		】	*/
	@JsonProperty("C8_Sample_Barcode")
	private String C8_Sample_Barcode;

	/**	打样URL	【例：	C7425907	】	*/
	@JsonProperty("C8_Sample_PLMID")
	private String C8_Sample_PLMID;

	/**	设计收样日期	【例：	2019-12-7 12	】	*/
	@JsonProperty("SampleReceivedDate")
	private String SampleReceivedDate;

	/**	样衣结束标志	【例：	FALSE	】	*/
	@JsonProperty("C8_Sample_IfFinished")
	private String C8_Sample_IfFinished;

	/**	齐套时间	【例：		】	*/
	@JsonProperty("C8_Sample_MCDate")
	private String C8_Sample_MCDate;

	/**	打样设计师	【例：	刘文文	】	*/
	@JsonProperty("C8_ProductSample_ProofingDesigner")
	private String C8_ProductSample_ProofingDesigner;

	/**	打样设计师工号	【例：	1100395	】	*/
	@JsonProperty("C8_ProductSample_ProofingDesignerID")
	private String C8_ProductSample_ProofingDesignerID;

	/**	供应商	【例：	E后技术	】	*/
	@JsonProperty("Supplier")
	private String Supplier;

	/**	供应商编码	【例：	99CY0015	】	*/
	@JsonProperty("SupplierNumber")
	private String SupplierNumber;

	/**	状态	【例：		】	*/
	@JsonProperty("SampleStatus")
	private String SampleStatus;

	/**	状态名称	【例：		】	*/
	@JsonProperty("SampleStatusName")
	private String SampleStatusName;

	@JsonProperty("Colorway")
	private Colorway colorway;

	@JsonProperty("Style")
	private Style style;

	private String code;

	@JsonProperty("Images")
	private List<Map<String,String>> images;

	public String getNodeName() {
		return NodeName;
	}

	public void setNodeName(String nodeName) {
		NodeName = nodeName;
	}

	public String getSampleType() {
		return SampleType;
	}

	public void setSampleType(String sampleType) {
		SampleType = sampleType;
	}

	public String getSampleTypeName() {
		return SampleTypeName;
	}

	public void setSampleTypeName(String sampleTypeName) {
		SampleTypeName = sampleTypeName;
	}

	public String getC8_Sample_PatDiff() {
		return C8_Sample_PatDiff;
	}

	public void setC8_Sample_PatDiff(String c8_Sample_PatDiff) {
		C8_Sample_PatDiff = c8_Sample_PatDiff;
	}

	public String getC8_Sample_PatDiffName() {
		return C8_Sample_PatDiffName;
	}

	public void setC8_Sample_PatDiffName(String c8_Sample_PatDiffName) {
		C8_Sample_PatDiffName = c8_Sample_PatDiffName;
	}

	public String getC8_Sample_PatSeq() {
		return C8_Sample_PatSeq;
	}

	public void setC8_Sample_PatSeq(String c8_Sample_PatSeq) {
		C8_Sample_PatSeq = c8_Sample_PatSeq;
	}

	public String getC8_Sample_PatSeqName() {
		return C8_Sample_PatSeqName;
	}

	public void setC8_Sample_PatSeqName(String c8_Sample_PatSeqName) {
		C8_Sample_PatSeqName = c8_Sample_PatSeqName;
	}

	public String getC8_Sample_SampleNumber() {
		return C8_Sample_SampleNumber;
	}

	public void setC8_Sample_SampleNumber(String c8_Sample_SampleNumber) {
		C8_Sample_SampleNumber = c8_Sample_SampleNumber;
	}

	public String getC8_Sample_SampleNumberName() {
		return C8_Sample_SampleNumberName;
	}

	public void setC8_Sample_SampleNumberName(String c8_Sample_SampleNumberName) {
		C8_Sample_SampleNumberName = c8_Sample_SampleNumberName;
	}

	public String getC8_Sample_BExtAuxiliary() {
		return C8_Sample_BExtAuxiliary;
	}

	public void setC8_Sample_BExtAuxiliary(String c8_Sample_BExtAuxiliary) {
		C8_Sample_BExtAuxiliary = c8_Sample_BExtAuxiliary;
	}

	public String getC8_Sample_EAValidFrom() {
		return C8_Sample_EAValidFrom;
	}

	public void setC8_Sample_EAValidFrom(String c8_Sample_EAValidFrom) {
		C8_Sample_EAValidFrom = c8_Sample_EAValidFrom;
	}

	public String getC8_Sample_EAValidTo() {
		return C8_Sample_EAValidTo;
	}

	public void setC8_Sample_EAValidTo(String c8_Sample_EAValidTo) {
		C8_Sample_EAValidTo = c8_Sample_EAValidTo;
	}

	public String getC8_Sample_Barcode() {
		return C8_Sample_Barcode;
	}

	public void setC8_Sample_Barcode(String c8_Sample_Barcode) {
		C8_Sample_Barcode = c8_Sample_Barcode;
	}

	public String getC8_Sample_PLMID() {
		return C8_Sample_PLMID;
	}

	public void setC8_Sample_PLMID(String c8_Sample_PLMID) {
		C8_Sample_PLMID = c8_Sample_PLMID;
	}

	public String getSampleReceivedDate() {
		return SampleReceivedDate;
	}

	public void setSampleReceivedDate(String sampleReceivedDate) {
		SampleReceivedDate = sampleReceivedDate;
	}

	public String getC8_Sample_IfFinished() {
		return C8_Sample_IfFinished;
	}

	public void setC8_Sample_IfFinished(String c8_Sample_IfFinished) {
		C8_Sample_IfFinished = c8_Sample_IfFinished;
	}

	public String getC8_Sample_MCDate() {
		return C8_Sample_MCDate;
	}

	public void setC8_Sample_MCDate(String c8_Sample_MCDate) {
		C8_Sample_MCDate = c8_Sample_MCDate;
	}

	public String getC8_ProductSample_ProofingDesigner() {
		return C8_ProductSample_ProofingDesigner;
	}

	public void setC8_ProductSample_ProofingDesigner(String c8_ProductSample_ProofingDesigner) {
		C8_ProductSample_ProofingDesigner = c8_ProductSample_ProofingDesigner;
	}

	public String getC8_ProductSample_ProofingDesignerID() {
		return C8_ProductSample_ProofingDesignerID;
	}

	public void setC8_ProductSample_ProofingDesignerID(String c8_ProductSample_ProofingDesignerID) {
		C8_ProductSample_ProofingDesignerID = c8_ProductSample_ProofingDesignerID;
	}

	public String getSupplier() {
		return Supplier;
	}

	public void setSupplier(String supplier) {
		Supplier = supplier;
	}

	public String getSupplierNumber() {
		return SupplierNumber;
	}

	public void setSupplierNumber(String supplierNumber) {
		SupplierNumber = supplierNumber;
	}

	public String getSampleStatus() {
		return SampleStatus;
	}

	public void setSampleStatus(String sampleStatus) {
		SampleStatus = sampleStatus;
	}

	public String getSampleStatusName() {
		return SampleStatusName;
	}

	public void setSampleStatusName(String sampleStatusName) {
		SampleStatusName = sampleStatusName;
	}

	public Colorway getColorway() {
		return colorway;
	}

	public void setColorway(Colorway colorway) {
		this.colorway = colorway;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Colorway {

		/**	配色URL	【例：	C7408160	】	*/
		@JsonProperty("C8_Colorway_PLMID")
		private String C8_Colorway_PLMID;

		/**	大货款号	【例：	1BC811033A	】	*/
		@JsonProperty("C8_Colorway_Code")
		private String C8_Colorway_Code;

		public Colorway() {
		}

		public String getC8_Colorway_PLMID() {
			return C8_Colorway_PLMID;
		}

		public void setC8_Colorway_PLMID(String c8_Colorway_PLMID) {
			C8_Colorway_PLMID = c8_Colorway_PLMID;
		}

		public String getC8_Colorway_Code() {
			return C8_Colorway_Code;
		}

		public void setC8_Colorway_Code(String c8_Colorway_Code) {
			C8_Colorway_Code = c8_Colorway_Code;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Style {

		/**	设计款号	【例：	120S1103WW	】	*/
		@JsonProperty("StyleCode")
		private String StyleCode;

		/**	年度	【例：	2020	】	*/
		@JsonProperty("C8_Season_Year")
		private String C8_Season_Year;

		/**	季节	【例：	S	】	*/
		@JsonProperty("C8_Season_Quarter")
		private String C8_Season_Quarter;

		/**	品牌	【例：	1	】	*/
		@JsonProperty("C8_Season_Brand")
		private String C8_Season_Brand;

		/**	品牌:名称	【例：		】	*/
		@JsonProperty("C8_Season_BrandName")
		private String C8_Season_BrandName;

		/**	大类	【例：	A01	】	*/
		@JsonProperty("C8_Category2_1stCategory")
		private String C8_Category2_1stCategory;

		/**	大类:名称	【例：	外套	】	*/
		@JsonProperty("C8_Category2_1stCategoryName")
		private String C8_Category2_1stCategoryName;

		/**	品类	【例：	1	】	*/
		@JsonProperty("C8_Collection_ProdCategory")
		private String C8_Collection_ProdCategory;

		/**	品类:名称	【例：	上衣	】	*/
		@JsonProperty("C8_Collection_ProdCategoryName")
		private String C8_Collection_ProdCategoryName;

		/**	中类	【例：	13	】	*/
		@JsonProperty("C8_Style_2ndCategory")
		private String C8_Style_2ndCategory;

		/**	中类名称	【例：	小外套	】	*/
		@JsonProperty("C8_Style_2ndCategoryName")
		private String C8_Style_2ndCategoryName;

		/**	工艺员ID	【例：		】	*/
		@JsonProperty("C8_StyleAttr_TechnicianID")
		private String C8_StyleAttr_TechnicianID;

		/**	工艺员:名称	【例：		】	*/
		@JsonProperty("C8_StyleAttr_Technician")
		private String C8_StyleAttr_Technician;

		/**	设计师ID	【例：	1100395	】	*/
		@JsonProperty("C8_StyleAttr_DesignerID")
		private String C8_StyleAttr_DesignerID;

		/**	设计师:名称	【例：	刘文文	】	*/
		@JsonProperty("C8_StyleAttr_Designer")
		private String C8_StyleAttr_Designer;

		/**	版师	【例：		】	*/
		@JsonProperty("C8_StyleAttr_PatternMaker")
		private String C8_StyleAttr_PatternMaker;

		/**	版师工号	【例：		】	*/
		@JsonProperty("C8_StyleAttr_PatternMakerID")
		private String C8_StyleAttr_PatternMakerID;

		/**	款式URL	【例：	C6497171"	】	*/
		@JsonProperty("C8_Style_PLMID")
		private String C8_Style_PLMID;

		/**	款式状态	【例：	true	】	*/
		@JsonProperty("Style_Active")
		private String Style_Active;

		public Style() {
		}

		public String getStyleCode() {
			return StyleCode;
		}

		public void setStyleCode(String styleCode) {
			StyleCode = styleCode;
		}

		public String getC8_Season_Year() {
			return C8_Season_Year;
		}

		public void setC8_Season_Year(String c8_Season_Year) {
			C8_Season_Year = c8_Season_Year;
		}

		public String getC8_Season_Quarter() {
			return C8_Season_Quarter;
		}

		public void setC8_Season_Quarter(String c8_Season_Quarter) {
			C8_Season_Quarter = c8_Season_Quarter;
		}

		public String getC8_Season_Brand() {
			return C8_Season_Brand;
		}

		public void setC8_Season_Brand(String c8_Season_Brand) {
			C8_Season_Brand = c8_Season_Brand;
		}

		public String getC8_Season_BrandName() {
			return C8_Season_BrandName;
		}

		public void setC8_Season_BrandName(String c8_Season_BrandName) {
			C8_Season_BrandName = c8_Season_BrandName;
		}

		public String getC8_Category2_1stCategory() {
			return C8_Category2_1stCategory;
		}

		public void setC8_Category2_1stCategory(String c8_Category2_1stCategory) {
			C8_Category2_1stCategory = c8_Category2_1stCategory;
		}

		public String getC8_Category2_1stCategoryName() {
			return C8_Category2_1stCategoryName;
		}

		public void setC8_Category2_1stCategoryName(String c8_Category2_1stCategoryName) {
			C8_Category2_1stCategoryName = c8_Category2_1stCategoryName;
		}

		public String getC8_Collection_ProdCategory() {
			return C8_Collection_ProdCategory;
		}

		public void setC8_Collection_ProdCategory(String c8_Collection_ProdCategory) {
			C8_Collection_ProdCategory = c8_Collection_ProdCategory;
		}

		public String getC8_Collection_ProdCategoryName() {
			return C8_Collection_ProdCategoryName;
		}

		public void setC8_Collection_ProdCategoryName(String c8_Collection_ProdCategoryName) {
			C8_Collection_ProdCategoryName = c8_Collection_ProdCategoryName;
		}

		public String getC8_Style_2ndCategory() {
			return C8_Style_2ndCategory;
		}

		public void setC8_Style_2ndCategory(String c8_Style_2ndCategory) {
			C8_Style_2ndCategory = c8_Style_2ndCategory;
		}

		public String getC8_Style_2ndCategoryName() {
			return C8_Style_2ndCategoryName;
		}

		public void setC8_Style_2ndCategoryName(String c8_Style_2ndCategoryName) {
			C8_Style_2ndCategoryName = c8_Style_2ndCategoryName;
		}

		public String getC8_StyleAttr_TechnicianID() {
			return C8_StyleAttr_TechnicianID;
		}

		public void setC8_StyleAttr_TechnicianID(String c8_StyleAttr_TechnicianID) {
			C8_StyleAttr_TechnicianID = c8_StyleAttr_TechnicianID;
		}

		public String getC8_StyleAttr_Technician() {
			return C8_StyleAttr_Technician;
		}

		public void setC8_StyleAttr_Technician(String c8_StyleAttr_Technician) {
			C8_StyleAttr_Technician = c8_StyleAttr_Technician;
		}

		public String getC8_StyleAttr_DesignerID() {
			return C8_StyleAttr_DesignerID;
		}

		public void setC8_StyleAttr_DesignerID(String c8_StyleAttr_DesignerID) {
			C8_StyleAttr_DesignerID = c8_StyleAttr_DesignerID;
		}

		public String getC8_StyleAttr_Designer() {
			return C8_StyleAttr_Designer;
		}

		public void setC8_StyleAttr_Designer(String c8_StyleAttr_Designer) {
			C8_StyleAttr_Designer = c8_StyleAttr_Designer;
		}

		public String getC8_StyleAttr_PatternMaker() {
			return C8_StyleAttr_PatternMaker;
		}

		public void setC8_StyleAttr_PatternMaker(String c8_StyleAttr_PatternMaker) {
			C8_StyleAttr_PatternMaker = c8_StyleAttr_PatternMaker;
		}

		public String getC8_StyleAttr_PatternMakerID() {
			return C8_StyleAttr_PatternMakerID;
		}

		public void setC8_StyleAttr_PatternMakerID(String c8_StyleAttr_PatternMakerID) {
			C8_StyleAttr_PatternMakerID = c8_StyleAttr_PatternMakerID;
		}

		public String getC8_Style_PLMID() {
			return C8_Style_PLMID;
		}

		public void setC8_Style_PLMID(String c8_Style_PLMID) {
			C8_Style_PLMID = c8_Style_PLMID;
		}

		public String getStyle_Active() {
			return Style_Active;
		}

		public void setStyle_Active(String style_Active) {
			Style_Active = style_Active;
		}



	}

	public List<Map<String, String>> getImages() {
		return images;
	}

	public void setImages(List<Map<String, String>> images) {
		this.images = images;
	}


}
