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
import lombok.Data;

/**
 * @Desc TODO
 * @Author lenovo
 * @Date 2020年6月12日
 */
@Data
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
	private Colorway Colorway;

	@JsonProperty("Style")
	private Style Style;
	@JsonProperty("Code")
	private String Code;

	@JsonProperty("Images")
	private List<Map<String,String>> images;

	@Data

	public static class Colorway {

		/**	配色URL	【例：	C7408160	】	*/
		@JsonProperty("C8_Colorway_PLMID")
		private String C8_Colorway_PLMID;

		/**	大货款号	【例：	1BC811033A	】	*/
		@JsonProperty("C8_Colorway_Code")
		private String C8_Colorway_Code;

	}

	@Data
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

	}

}
