package com.base.sbc.client.ccm.service;

import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicStructureSearchDto;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.BaseConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Youkehai
 * @data 创建时间:2021/1/4
 */
 @FeignClient(name = "ccm", url = "http://" + "${baseGateAwayIpaddress}" + ":9151/", decode404 = true)
//@FeignClient(name = "ccm", url = "http://" + "127.0.0.1" + ":9151/", decode404 = true)
public interface CcmService {
    /**
     * 获取最后一级的品类信息
     *
     * @param token       登录令牌
     * @param userCompany 企业编码
     * @return
     */
    @GetMapping("/ccm/api/saas/basicBaseModeltypes/getEndName")
    public String getCategoryName(@RequestHeader("Authorization") String token, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany);

    /**
     * 获取字典信息
     *
     * @param token       登录令牌
     * @param userCompany 企业编码
     * @param type        类型
     * @return
     */
    @GetMapping("/ccm/api/saas/basicBaseDicts/selectDictByTypes")
    public String getDictInfo(@RequestHeader("Authorization") String token, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("type") String type);

    /**
     * 获取字典信息
     *
     * @param type 类型
     * @return
     */
    @GetMapping("/ccm/api/saas/basicBaseDicts/selectDictByTypes")
    public String getDictInfo(@RequestParam("type") String type, @RequestParam("status") String status);

    /**
     * 获取字典信息
     *
     * @param type 类型
     * @return
     */
    @GetMapping("/ccm/api/open/dict/selectDictByTypes")
    public String getDictInfoOpen(@RequestParam("initCompany") String initCompany,@RequestParam("type") String type);

    /**
     * 开放接口获取字典信息
     *
     * @param type 类型
     * @return
     */
    @GetMapping("/ccm/api/open/structure/selectDictByTypes")
    public String getOpenDictInfo(@RequestParam("userCompany") String userCompany,@RequestParam("type") String type);

    /**
     * 新增字典
     *
     * @param basicBaseDicts 类型
     * @return
     */
    @GetMapping("/ccm/api/saas/basicBaseDicts/batchInsert")
    public String batchInsert(@Valid @RequestBody List<BasicBaseDict> basicBaseDicts);

    /**
     * 新增字典
     *
     * @param type 类型
     * @return
     */
    @DeleteMapping("/ccm/api/saas/basicBaseDicts/batchDeleteDict")
    public String batchDeleteDict(@Valid @RequestParam("ids") String ids);

    /**
     * 获取颜色信息
     *
     * @param token
     * @param userCompany 企业编码
     * @param isLeaf
     * @return
     */
    @GetMapping("/ccm/api/saas/basicColor/selectColorList")
    public String selectColorPageList(@RequestHeader("Authorization") String token, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("isleaf") String isLeaf);

    /**
     * 根据系统参数编码查询配置项开启与否
     *
     * @param code 系统参数编码
     * @return
     */
    @GetMapping("/ccm/api/saas/companySetting/code")
    public String getCompanySettingData(@RequestParam("code") String code);

    /**
     * 根据名称返回编码
     *
     * @param companyCode 企业编码
     * @param type        类型
     * @param dictNames   名称
     * @return
     */
    @GetMapping(value = "/ccm/api/open/dict/codeByName")
    public String selectDictCodeByNameAndType(@RequestParam("companyCode") String companyCode, @RequestParam("type") String type, @RequestParam("dictNames") String dictNames);

    /**
     * 拿到ccm供应商信息
     *
     * @param token       头部令牌
     * @param userCompany 企业编码
     * @param pageNum     当前页
     * @param pageSize    每页条数
     * @return string
     */
    @GetMapping(value = "/ccm/api/saas/srmFactorys")
    public String selectSrmFactoryList(@RequestHeader("Authorization") String token, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize);

    /**
     * 根据系统参数编码查询配置项开启与否
     *
     * @param code 系统参数编码
     * @return json
     */
    @GetMapping("/ccm/api/saas/companySetting/code")
    public String selectCompanySetting(@RequestParam("code") String code);

    @GetMapping("/ccm/api/saas/washSay/getWashSayData")
    public String getWashSayData(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("ids") String ids);

    /**
     * 根据单据编码和数据，获取编码
     * @param genCode
     * @param dataMap
     * @return
     */
    @PostMapping("/ccm/api/saas/companyCodeGen/getGenCode")
    ApiResult genCodeByCodeRule(@RequestParam("genCode") String genCode, @RequestBody Object dataMap);

    /**
     * 根据单据编码和数据，获取编码
     * @param genCode
     * @param dataMap
     * @return
     */
    @PostMapping(value = "/ccm/api/saas/companyCodeGen/getGenCodeByRedis", headers = {"Content-type=application/json"})
    String getGenCodeByRedis(@RequestParam("genCode") String genCode, @RequestParam("count") Integer count, @RequestBody Object dataMap);

    /**
     * 根据单据编码和数据，获取编码
     * @param genCode
     * @param dataMap
     * @return
     */
    @PostMapping(value = "/ccm/api/saas/companyCodeGen/getGenCodeExistsRedis", headers = {"Content-type=application/json"})
    ApiResult getGenCodeExistsRedis(@RequestParam("genCode") String genCode, @RequestParam("count") Integer count, @RequestBody Object dataMap);


    @PostMapping(value = "/ccm/api/saas/basicStructure/findByCategoryIds", headers = {"Content-type=application/json"})
    String findStructureTreeByCategoryIds(@RequestBody String categoryIds);

    @PostMapping(value = "/ccm/api/saas/basicStructure/findByCodes", headers = {"Content-type=application/json"})
    String findStructureTreeByCodes(@RequestBody String codes);

    @PostMapping(value = "/ccm/api/saas/basicStructure/getNameByIds", headers = {"Content-type=application/json"})
    String findStructureTreeNameByCategoryIds(@RequestBody BasicStructureSearchDto dto);

    @PostMapping(value = "/ccm/api/saas/basicStructure/getNameByCodes", headers = {"Content-type=application/json"})
    String findStructureTreeNameByCodes(@RequestBody BasicStructureSearchDto dto);

    /**
     * 通过名称,级别获取id
     *
     * @param structureName
     * @param names
     * @param level
     * @return
     */
    @GetMapping(value = "/ccm/api/saas/basicStructure/getIdsByNameAndLevel")
    public String getIdsByNameAndLevel( @RequestParam("structureName") String structureName,@RequestParam("names") String names,@RequestParam("level") String level);

    /**
     * 通过类名id集合获取类目列表
     */
    @GetMapping(value = "/ccm/api/saas/basicStructure/listByIds")
    public ApiResult listByIds(@RequestParam("ids") String ids);

    /**
     * 通过名称,级别获取品类
     *
     * @param structureName
     * @param names
     * @param level
     * @return
     */
    @GetMapping(value = "/ccm/api/saas/basicStructure/getCategorySByNameAndLevel")
    public String getCategorySByNameAndLevel(@RequestParam("structureName") String structureName, @RequestParam("names") String names, @RequestParam("level") String level);

    /**
     * 通过类目编码查询类目树
     *
     * @param code
     * @param hasRoot
     * @param levels
     * @return
     */
    @GetMapping(value = "/ccm/api/saas/basicStructure/treeByCode")
    String basicStructureTreeByCode(@RequestParam("code") String code, @RequestParam("hasRoot") String hasRoot, @RequestParam("levels") String levels);

    /**
     * 通过类目名称查询类目树
     */
    @GetMapping(value = "/ccm/api/saas/basicStructure/treeByName")
    public String treeByName(@RequestParam("structureName") String structureName, @RequestParam("hasRoot") String hasRoot, @RequestParam("levels") String levels);


    @GetMapping(value = "/ccm/api/open/structure/getCategorySByNameAndLevel")
    public String getOpenCategorySByNameAndLevel(@RequestParam("structureName") String structureName, @RequestParam("code") String code, @RequestParam("level") String level);

    @GetMapping(value = "/ccm/api/saas/basicStructure/nextLevelList")
    public String queryBasicStructureNextLevelList(@RequestParam("structureCode") String structureCode, @RequestParam("treeCode") String treeCode, @RequestParam("level") Integer level);


    /**
     * 根据多个类型查询最后一级字典信息(联调)
     *
     * @param isLeaf         子节点
     * @param type           字典类型
     * @param dependCode     依赖字段编码
     * @param dependDictType 依赖字典类型
     */
    @GetMapping(value = "/ccm/api/saas/basicDictDepends/selectDictByTypes")
    String basicDictDependsByTypes(@RequestParam("isLeaf") String isLeaf, @RequestParam("type") String type, @RequestParam("dependDictType") String dependDictType,
                                   @RequestParam("dependCode") String dependCode);

    /**
     * 根据结构名称，查询指定level的下级列表
     * @param structureCode 结构名称
     * @param level 第几级
     */
    @GetMapping(value = "/ccm/api/saas/basicStructure/appointNextLevelList")
    String appointNextLevelList(@RequestParam("structureCode") String structureCode, @RequestParam("level") String level);

    /**
     * 查询所有单位列表，可根据类型筛选
     * @param type 类型
     *
     */
    @GetMapping(value = "/ccm/api/saas/unitConfig")
    String getAllUnitConfigList(@RequestParam("type")String type);


    /**
     * 查询字典依赖
     */
    @GetMapping(value ="/ccm/api/saas/basicDictDepends/getDictDependsList")
    String getDictDependsList(@RequestParam("dictTypeName")String dictTypeName,@RequestParam("pageNum")Integer pageNum,@RequestParam("pageSize")Integer pageSize);
}
