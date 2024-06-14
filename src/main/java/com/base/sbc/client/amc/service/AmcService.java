package com.base.sbc.client.amc.service;

import com.base.sbc.client.amc.entity.Job;
import com.base.sbc.client.amc.entity.Team;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.dto.VirtualDept;
import com.base.sbc.open.dto.DesignerDto;
import com.base.sbc.open.entity.SmpDept;
import com.base.sbc.open.entity.SmpPost;
import com.base.sbc.open.entity.SmpUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * amc 远程调用
 *
 * @author lilele
 * @data 创建时间:2021/12/24
 */
@FeignClient(name = "amc", url = "http://" + "${baseGateAwayIpaddress}" + ":9151/", decode404 = true)
//@FeignClient(name = "amc", url = "http://" + "127.0.0.1" + ":9201/", decode404 = true)
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
     * @param deptId   部门id
     * @param userType 用户类型 1 部门主管 2 样衣组长
     * @return
     */
    @GetMapping("/amc/api/token/companyDept/deptManager")
    String getDeptManager(@RequestParam("deptId") String deptId, @RequestParam("userType") String userType);

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

    @GetMapping("/amc/api/token/user/getUserDeptInfoById")
    public String getUserDeptInfoById(@RequestParam("userId") String userId, @RequestParam("userType") String userType);

    /**
     * 批量根据用户id获取用户部门
     */
    @GetMapping("/amc/api/token/companyDept/getDeptList")
    public String getDeptList(@RequestHeader("Authorization") String token, @RequestParam("userIds") String[] userIds);

    @GetMapping("/amc/api/token/companyUser/getCompanyUserInfoByUserIds")
    public String getCompanyUserInfoByUserIds(@RequestParam("userIds") String userIds, @RequestParam("dpj") String dpj);

    /**
     * 获取用户头像
     *
     * @param ids 用户id
     * @return
     */
    @PostMapping("/amc/api/token/user/getUserAvatar")
    public String getUserAvatar(@RequestBody String ids);

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
    @PostMapping("/amc/api/open/smp/hrDeptSave")
    String hrDeptSave(@RequestBody SmpDept smpDept);

    /**
     * hr-岗位
     */
    @PostMapping("/amc/api/open/smp/hrPostSave")
    String hrPostSave(@RequestBody SmpPost smpPost);


    @PostMapping(value = "/amc/api/open/initData/dept",headers = {"Content-type=application/json"})
    String dept(@RequestBody List<SmpDept> list);

    @PostMapping(value = "/amc/api/open/initData/user",headers = {"Content-type=application/json"})
    String user(@RequestBody List<SmpUser> list);

    @PostMapping(value = "/amc/api/open/initData/designerCode",headers = {"Content-type=application/json"})
    String designerCode(@RequestBody List<DesignerDto> list);

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
    String getUsersBySeasonId(@RequestParam("planningSeasonId") String planningSeasonId, @RequestParam("dpj") String dpj, @RequestParam("post") String post);


    /**
     * 通过 用户id 查询产品季id
     *
     * @param userId
     * @return
     */
    @GetMapping("/amc/api/token/teamRelation/getPlanningSeasonIdByUserId")
    String getPlanningSeasonIdByUserId(@RequestParam("userId") String userId);

    /**
     * 获取读数据权限
     *
     * @param businessType
     * @return
     */
    @GetMapping("/amc/api/token/dataPermissions/getReadDataPermissions")
    ApiResult getReadDataPermissions(@RequestParam("businessType") String businessType,@RequestParam("operateType") String operateType);

    /**
     * 根据用户id集合获取用户工号(登录账号)集合
     */
    @GetMapping("/amc/api/token/user/getUsernamesByIds")
    Map<String,String> getUsernamesByIds(@RequestParam("ids") String ids);


    /**
     * 获取团队下的用户组用户
     */
    @GetMapping("/amc/api/token/teamRelation/getUserGroupUserId")
    String getUserGroupUserId(@RequestParam("seasonId") String seasonId,@RequestParam("teamId") String teamId,@RequestParam("groupName") String groupName);

    /**
     * 根据用户名称获取用户id
     */
    @PostMapping("/amc/api/open/user/getUserListByNames")
    ApiResult getUserListByNames(String names);

    /**
     * 团队保存
     *
     * @param team
     * @return
     */
    @PostMapping("/amc/api/token/team/save")
    ApiResult teamSave(@RequestBody Team team);

    /**
     * 产品季默认分配团队
     *
     * @param seasonId
     * @return
     */
    @GetMapping("/amc/api/token/seasonTeams/seasonSaveDefaultTeam")
    ApiResult seasonSaveDefaultTeam(@RequestParam("seasonId") String seasonId);


    /**
     * 查询用户
     * @param userId
     * @return
     */
    @GetMapping("/amc/api/token/user/getUserByUserId")
    public String getUserByUserId( @RequestParam("userId") String userId);

    /**
     * 查询用户
     * @param userId
     * @return
     */
    @GetMapping("/amc/api/token/user/getUserByUserIds")
    public String getUserByUserIds( @RequestParam("userIds") String userIds);


    /**
     * 根据用户Id查询用户组id集合
     */
    @GetMapping("/amc/api/token/group/getJobByUserId")
    public List<Job> getByUserId(@RequestParam("userId") String userId);

    /**
     * 根据用户Id查询用户组id集合
     */
    @GetMapping("/amc/api/token/group/list")
    ApiResult getUserGroupByUserId(@RequestParam("userId") String userId);

    /**
     * 查询用户-企业 user_code 不为空的数据
     */
    @GetMapping("/amc/api/open/user/getUserCodeNotNullUserList")
    String getUserCodeNotNullUserList();

    /**
     * 获取用户所在部门关系
     */
    @GetMapping("/amc/api/token/virtualDeptRelationships/getByUserId")
    ApiResult getDeptRelationByUserId(@RequestParam("userId") String userId,@RequestParam("type") String type);

    /**
     * 获取用户所在部门
     */
    @GetMapping("/amc/api/token/virtualDept/getVirtualDeptByUserId")
    ApiResult<List<VirtualDept>> getVirtualDeptByUserId(@RequestParam("userId") String userId);

}
