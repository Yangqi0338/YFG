package com.base.sbc.client.oauth.service;

import com.base.sbc.config.constant.BaseConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 
 * @author xiong
 *
 */
@FeignClient(name = "BASE-OAUTH2-CENTER", url = "http://" + "${baseGateAwayIpaddress}" + ":9151/", decode404 = true)
public interface OauthService {
	/**
	 * 获取用户信息
	 * @return
	 */
	@GetMapping("/amc/api/token/user/getUserInfo")
	public String getUserInfo();

	/**
	 * 获取当前用户企业菜单
	 * @return
	 */
	@GetMapping("/amc/api/token/user/getUserCompanyMenu")
	public String getUserCompanyMenu();

	/**
	 * 获取当前企业是否有b2b商城
	 * @param userCompany 企业编码
	 * @return
	 */
	@GetMapping("/amc/api/open/applyAndCompany/isBuy?applyId=4")
	public String isHaveB2b(@RequestHeader(BaseConstant.APPLY_COMPANY) String userCompany);

	/**
	 * 获取用户信息
	 * @return
	 */
	@GetMapping("/amc/api/token/companyUser/getCompanyUserInfoByUserName")
	public String getCompanyUserInfoByUserName();


}
