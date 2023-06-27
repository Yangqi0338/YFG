package com.base.sbc.client.ccm.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.BaseConstant;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
    public String getDictInfo(@RequestParam("type") String type);

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
    @PostMapping("/ccm/api/saas/companyCodeGen/getGenCodeByRedis")
    String getGenCodeByRedis(@RequestParam("genCode") String genCode,@RequestParam("count") Integer count, @RequestBody Object dataMap);


    @PostMapping(value = "/ccm/api/saas/basicStructure/findByCategoryIds", headers = {"Content-type=application/json"})
    String findStructureTreeByCategoryIds(@RequestBody String categoryIds);

    @PostMapping(value = "/ccm/api/saas/basicStructure/getNameByIds", headers = {"Content-type=application/json"})
    String findStructureTreeNameByCategoryIds(@RequestBody String categoryIds);

    /**
     * 通过名称,级别获取id
     * @param structureName
     * @param names
     *  @param level
     * @return
     */
    @GetMapping(value = "/ccm/api/saas/basicStructure/getIdsByNameAndLevel")
    public String getIdsByNameAndLevel( @RequestParam("structureName") String structureName,@RequestParam("names") String names,@RequestParam("level") String level);

}
