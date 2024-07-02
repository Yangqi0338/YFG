/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.basicsdatum.dto.*;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.vo.*;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.pack.dto.MaterialSupplierInfo;
import com.base.sbc.module.pack.vo.BomSelMaterialVo;
import com.base.sbc.module.report.dto.MaterialColumnHeadDto;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 类描述：基础资料-物料档案 service类
 *
 * @author shenzhixiong
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:17
 */
public interface BasicsdatumMaterialService extends BaseService<BasicsdatumMaterial> {

	PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialList(BasicsdatumMaterialQueryDto dto);
	PageInfo<BasicsdatumMaterialPageVo> getBasicsdatumMaterialNewList(MaterialColumnHeadDto dto);

	List<FieldManagementVo> queryCoefficient(BasicsdatumMaterialPageVo pageVo);

	BasicsdatumMaterialVo saveBasicsdatumMaterial(BasicsdatumMaterialSaveDto dto);

	Boolean startStopBasicsdatumMaterial(StartStopDto dto);

	Boolean delBasicsdatumMaterial(RemoveDto removeDto);

	BasicsdatumMaterialVo getBasicsdatumMaterial(String id);

	PageInfo<BasicsdatumMaterialWidthPageVo> getBasicsdatumMaterialWidthList(BasicsdatumMaterialWidthQueryDto dto);

	Boolean saveBasicsdatumMaterialWidth(BasicsdatumMaterialWidthSaveDto dto);

	Boolean startStopBasicsdatumMaterialWidth(StartStopDto dto);

	Boolean delBasicsdatumMaterialWidth(RemoveDto removeDto);

	PageInfo<BasicsdatumMaterialColorPageVo> getBasicsdatumMaterialColorList(BasicsdatumMaterialColorQueryDto dto);

	Boolean saveBasicsdatumMaterialColor(BasicsdatumMaterialColorSaveDto dto);

	Boolean delBasicsdatumMaterialColor(RemoveDto removeDto);

	Boolean startStopBasicsdatumMaterialColor(StartStopDto dto);

	PageInfo<BasicsdatumMaterialPricePageVo> getBasicsdatumMaterialPriceList(BasicsdatumMaterialPriceQueryDto dto);

	Boolean saveBasicsdatumMaterialPrice(BasicsdatumMaterialPriceSaveDto dto);

	Boolean startStopBasicsdatumMaterialPrice(StartStopDto dto);

	Boolean delBasicsdatumMaterialPrice(String id);

	Map<String, Object> getBasicsdatumMaterialPriceColorWidthSelect(String materialCode);

	void exportBasicsdatumMaterial(HttpServletResponse response, BasicsdatumMaterialQueryDto dto) throws IOException;
	void exportBasicsdatumNewMaterial(HttpServletResponse response, MaterialColumnHeadDto dto) throws IOException;

	void exportBasicsdatumMaterialAndStyle(HttpServletResponse response, BasicsdatumMaterialPageAndStyleDto dto) throws IOException;

	PageInfo<BomSelMaterialVo> getBomSelMaterialList(BasicsdatumMaterialQueryDto dto);

	Boolean saveBasicsdatumMaterialWidthGroup(BasicsdatumMaterialWidthGroupSaveDto dto);

	Boolean saveBasicsdatumMaterialWidths(BasicsdatumMaterialWidthsSaveDto dto);

	PageInfo<BasicsdatumMaterialOldPageVo> getBasicsdatumMaterialOldList(BasicsdatumMaterialOldQueryDto dto);

	Boolean saveBasicsdatumMaterialOld(BasicsdatumMaterialOldSaveDto dto);

	Boolean delBasicsdatumMaterialOld(String id);

	PageInfo<WarehouseMaterialVo> getPurchaseMaterialList(BasicsdatumMaterialQueryDto dto);

	/**
	 * 修改物料询价编号、货期数据
	 * @param dto 物料档案主表保存实体
	 * @return 是否成功
	 */
	Boolean updateInquiryNumberDeliveryName(BasicsdatumMaterialSaveDto dto);

	/**
	 * 保存提交
	 * @param dto
	 */
	BasicsdatumMaterialVo saveSubmit(BasicsdatumMaterialSaveDto dto);

	/**
	 * 审批处理
	 * @param dto
	 * @return
	 */
	boolean approval(AnswerDto dto);

	/**
	 * 解锁下发
	 * @param id
	 * @return
	 */
	Boolean unlock(String id);
	/**
	 * 获取下一个编码
	 *
	 * @return
	 */
	String genMaterialCode(BasicsdatumMaterial material);

	/**
	 * 获取下一个编码
	 *
	 * @return
	 */
	String getMaxMaterialCode(GetMaxCodeRedis data, String userCompany);

	/**
	 * 通过物料编码获取来源 和成分
	 *
	 * @param materialCodes
	 * @return
	 */
	Map<String, BasicsdatumMaterial> getSourceAndIngredient(List<String> materialCodes);

	Boolean saveBasicsdatumMaterialColorList(List<BasicsdatumMaterialColorSaveDto> dtos);

	/**
	 * 修改供应商图片
	 * @param dto
	 * @return
	 */
	Boolean updateMaterialPic(BasicsdatumMaterialSaveDto dto);


	/**
	 * 获取物料编码
	 *
	 * @param id
	 * @return
	 */
	String getMaterialCodeById(String id);

	boolean resetImgUrl(MultipartFile file);

	PageInfo matchPic(int pageNum, int pageSize);

	PageInfo<BasicsdatumMaterialPageAndStyleVo> materialsBomStylePage(BasicsdatumMaterialPageAndStyleDto dto);


	/**
	 * 根据供应商信息获取物料编号
	 * @param materialSupplierInfo
	 * @return
	 */
	List<String> getMaterialCodeBySupplierInfo(MaterialSupplierInfo materialSupplierInfo);

    /**
	 * 通过物料编码获取来源 和成分
	 *
	 * @param materialCode
	 * @return
	 */
	BasicsdatumMaterial getMaterialByCode(String materialCode);

	/**
	 * 获取物料颜色
	 * @param materialCode
	 * @return
	 */
	List<BasicsdatumMaterialColorSelectVo> getMaterialCodes(String materialCode);

	FabricSummary getMaterialSummaryInfo(String materialCode);

	/**
	 * 修改
	 * 物料编码 改变材料名称
	 * 物料名称 改变材料名称
	 * 材料三级分类 修改物料编码前缀和材料
	 * @param basicsdatumMaterialUpdateDto
	 */
	BasicsdatumMaterialUpdateVo updateMaterialProperties(BasicsdatumMaterialUpdateDto basicsdatumMaterialUpdateDto);

	/**
	 * 检查物料是否被BOM引用
	 * @param materialCode
	 * @return
	 */
	Integer materialRelyOnBom(String materialCode);
}

