package com.base.sbc.client.amc.service;

import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.open.dto.SmpUserDto;
import com.base.sbc.open.entity.SmpDept;
import com.base.sbc.open.entity.SmpPost;
import com.base.sbc.open.entity.SmpUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * amc 远程调用
 *
 * @author lilele
 * @data 创建时间:2021/12/24
 */
@FeignClient(name = "amc", url = "http://" + "${baseGateAwayIpaddress}" + ":9151/", decode404 = true)
//@FeignClient(name = "amc", url = "http://" + "127.0.0.1" + ":9151/", decode404 = true)
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
    public String getCompanyUserInfoByUserIds(@RequestParam("userIds") String userIds, @RequestParam("dpj") String dpj);

    /**
     * 获取用户头像
     * @param ids 用户id
     * @return
     */
    @GetMapping("/amc/api/token/user/getUserAvatar")
    public String getUserAvatar( @RequestParam("ids") String ids);

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
    public String getJobByUserIdOrJobName(@RequestParam("userId") String userId,  @RequestParam("jobName") String jobName);

    /**
     * 查询企业列表树
     */
    @GetMapping("/amc/api/token/userCompany/getAdList")
    List<AdTree> getAdList();

    /**
     * 获取团队信息
     * @param seasonId 产品季节id
     * @return
     */
    @GetMapping("/amc/api/token/team/getBySeasonId")
    String getTeamBySeasonId(@RequestParam("seasonId") String seasonId);


    /**
     * hr-人员
     */
    @PostMapping("/amc/api/open/smp/hrUserSave")
    String hrUserSave(@RequestBody SmpUser smpUser);

    /**
     * hr-部门
     */
    @PostMapping("/amc/api/open/smp/hrUserSave")
    String hrDeptSave(@RequestBody SmpDept smpUser);

    /**
     * hr-岗位
     */
    @PostMapping("/amc/api/open/smp/hrUserSave")
    String hrPostSave(@RequestBody SmpPost smpUser);


    @PostMapping(value = "/amc/api/open/initData/dept",headers = {"Content-type=application/json"})
    String dept(@RequestBody List<SmpDept> list);

    @PostMapping(value = "/amc/api/open/initData/user",headers = {"Content-type=application/json"})
    String user(@RequestBody List<SmpUser> list);

    @PostMapping(value = "/amc/api/open/initData/post",headers = {"Content-type=application/json"})
    String post(@RequestBody List<SmpPost> list);

    /**
     * 获取团队里的人员，通过产品季id
     *
     * @param planningSeasonId 产品季id
     * @param dpj              是否查询部门岗位角色
     * @return
     */
    @GetMapping("/amc/api/token/team/getUsersBySeasonId")
    String getUsersBySeasonId(@RequestParam("planningSeasonId") String planningSeasonId, @RequestParam("dpj") String dpj);


    /**
     * 通过 用户id 查询产品季id
     *
     * @param userId
     * @return
     */
    @GetMapping("/amc/api/token/teamRelation/getPlanningSeasonIdByUserId")
    String getPlanningSeasonIdByUserId(@RequestParam("userId") String userId);


}
