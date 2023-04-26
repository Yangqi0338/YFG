package com.base.sbc.client.amc.service;

import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.common.dto.AdTree;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * amc 远程调用
 *
 * @author lilele
 * @data 创建时间:2021/12/24
 */
//@FeignClient(name = "amc", url = "http://" + "${baseGateAwayIpaddress}" + ":9151/", decode404 = true)
@FeignClient(name = "amc", url = "http://" + "127.0.0.1" + ":9151/", decode404 = true)
public interface AmcService {
    /**
     * 获取用户信息
     *
     * @param token
     * @param userCompany
     * @return
     */
    @GetMapping("/amc/api/token/companyDept/getDesignerDeptUsers")
    public String getDesignerDeptUsers(@RequestHeader("Authorization") String token, @RequestHeader(BaseConstant.USER_COMPANY) String userCompany);

    /**
     * 获取企业信息
     *
     * @param companyCode
     * @return
     */
    @GetMapping("/amc/api/token/companyAbility/companyAbility")
    public String getCompanyInfo(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, @RequestParam("companyCode") String companyCode);

    /**
     * 根据部门id获取用户信息
     *
     * @param userCompany
     * @param deptId
     * @return
     */
    @GetMapping("/amc/api/token/companyDept/deptUserById")
    public String queryUserInfoByDeptId(@RequestParam("userCompany") String userCompany, @RequestParam("deptId") String deptId);

    /**
     * 批量根据用户id获取用户部门
     */
    @GetMapping("/amc/api/token/companyDept/getDeptList")
    public String getDeptList(@RequestHeader("Authorization") String token,@RequestParam("userIds") String[] userIds);

    @GetMapping("/amc/api/token/companyUser/getCompanyUserInfoByUserIds")
    public String getCompanyUserInfoByUserIds( @RequestParam("userIds") String userIds);

    /**
     * 获取用户头像
     * @param userIds
     * @return
     */
    @GetMapping("/amc/api/token/user/getUserAvatar")
    public String getUserAvatar( @RequestParam("userIds") String userIds);

    /**
     * 获取数据隔离信息
     *
     * @return
     */
    @RequestMapping(value = "/amc/api/token/manageGroupRole/userDataIsolation",method = RequestMethod.GET)
    public Object  userDataIsolation(@RequestHeader("Authorization") String token, @RequestParam("companyCode") String companyCode, @RequestParam("isolation") String isolation, @RequestParam("userId") String userId, @RequestParam("dataName") String dataName);


    /**
     * 查询职位
     * @param userId,jobName
     * @return
     */
    @GetMapping("/amc/api/token/companyJob/getJobByUserIdOrJobName")
    public String getJobByUserIdOrJobName(  @RequestParam("userId") String userId,  @RequestParam("jobName") String jobName);

    /**
     * 查询企业列表树
     */
    @GetMapping("/amc/api/token/userCompany/getAdList")
    List<AdTree> getAdList();
}
