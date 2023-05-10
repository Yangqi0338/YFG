package com.base.sbc.config.constant;

/**
 * 基础常量类
 * @author xiong
 */
public class BaseConstant {
	/**
	 * 当前用户的企业ID
	 * 主要用于saas平台管理，登录后切换企业使用
	 * 前端传递到头部 userCompany
	 * */
	public static final String USER_COMPANY = "userCompany";
	/**
	 * 应用固定 绑定企业ID （一个应用多个企业一起使用）
	 * 主要用于saas 开放的页面
	 * 前端传递到头部 applyCompany   一般页面定义1个参数，传递到后台即可
	 * */
	public static final String APPLY_COMPANY = "applyCompany";

	/**
	 * 用户平台账号ID
	 * 主要用于记录当前账号的id  对应平台用户表的ID  也对应  saas用户表的user_id
	 * 前端传递到头部 userId
	 */
//	public static final String USER_GROUP_ID = "userId";


	/**
	 * 用户访问令牌
	 * 所有需要登录的请求 必须包含，内容为 : Authorization access_Token
	 */
	public static final String AUTHORIZATION = "Authorization";
	/**
	 * 开放订单接口令牌
	 */
	public static final  String T_TOKEN ="t-token";

	/**
	 * 商品入库单超入数量
	 */
	public static final String OVER_STORAGE_RATIO = "OVER_STORAGE_RATIO";

	/**
	 *  success
	 */
	public static final String SUCCESS = "success";
	public static final String DATA = "data";
	public static final String FLOWING = "flowing";

	/**
	 *  id
	 */
	public static final String ID = "id";

	public static final String COMPANY_CODE = "company_code";
}
