package com.base.sbc.config.enums;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
public enum BaseErrorEnum {

/******************************************************start 业务异常：  18000  -  18999  ***************************************************************/
	/**  定义的属性不能超过二十五个*/
	ERR_MAX_TYPE(18001,"ERR_MAX_TYPE"),
	/**  定义的属性为重要等级的不能超过三个*/
	ERR_LEVEL_THAN_THREE(18002,"ERR_LEVEL_THAN_THREE"),
	/**  用户为空*/
	ERR_USER_IS_NULL(18003,"ERR_USER_IS_NULL"),
	/**  产品编号不能重复*/
	ERR_PRODUCT_CODE_REPEAT(18004,"ERR_PRODUCT_CODE_REPEAT"),
	/**  默认类型不可修改*/
	ERR_DEFAULT_NOT_UPDATE(18005,"ERR_DEFAULT_NOT_UPDATE"),
	/**  该样品已在库或出库，请勿重复操作*/
	ERR_PRODUCT_STATUS(18006,"ERR_PRODUCT_STATUS"),
	/**  该操作只对未审核的状态有效*/
	ERR_STATUS_NOT_CHECK(18007,"ERR_STATUS_NOT_CHECK"),
	/** 状态只能修改为草稿或待审核*/
	ERR_UPDATE_STATUS(18008,"ERR_UPDATE_STATUS"),
	/** 该操作只对未审核和草稿状态的单据有效*/
	ERR_STATUS_DELETE(18009,"ERR_STATUS_DELETE"),
	/**只有草稿和驳回才能提交审核*/
	ERR_ORDER_STATUS_SUBMIT(18010,"ERR_ORDER_STATUS_SUBMIT"),
	/**库存数不足*/
	ERR_STOCK_INSUFFICIENT(18011,"ERR_STOCK_INSUFFICIENT"),
	/** 缺少头部参数:t-toekn(开放产品接口需要) */
	ERR_LACK_PARAMS_TOKEN(18012, "ERR_LACK_PARAMS_TOKEN"),
	/** 头部参数过期:t-toekn */
	ERR_LACK_HEAD_PARAMS_TOKEN_EXPIRATION(18013, "ERR_LACK_HEAD_PARAMS_TOKEN_EXPIRATION"),
	/**单据已完成入库*/
	ERR_ORDER_STATUS_IS_FINISH(18010,"ERR_ORDER_STATUS_IS_FINISH"),
	/**单据没有商品或库存不足*/
	ERR_ORDER_WITHOUT_SHOP_OR_OTHER(18010,"ERR_ORDER_WITHOUT_SHOP_OR_OTHER"),
	/** ID不能为空 */
	ERR_ID_IS_NULL(18014,"ERR_ID_IS_NULL"),
	/** 下发配饰款未关联大货款*/
	ERR_ACCESSORIES_NOT_RELATE_MAIN_STYLE(18018,"ERR_ACCESSORIES_NOT_RELATE_MAIN_STYLE"),
/******************************************************end 业务异常： 1001  -  9999  ***************************************************************/

	
/******************************************************start 平台通用异常：11001-11999***************************************************************/
	/** 查询成功   */
	SUCCESS_SELECT(0, "SUCCESS_SELECT"),
	/** 新增成功   */
	SUCCESS_INSERT(0, "SUCCESS_INSERT"),
	/** 修改成功   */
	SUCCESS_UPDATE(0, "SUCCESS_UPDATE"),
	/** 删除成功   */
	SUCCESS_DELETE(0, "SUCCESS_DELETE"),
	
	/** 缺少头部参数 */
	ERR_LACK_HEAD_PARAMS_APPLY_COMPANY(10001, "ERR_LACK_HEAD_PARAMS_APPLY_COMPANY"),
	/** 缺少头部参数: */
	ERR_LACK_HEAD_PARAMS_AUTHORIZATION(10002, "ERR_LACK_HEAD_PARAMS_AUTHORIZATION"),
	/** 缺少头部参数: userId */
	ERR_LACK_HEAD_PARAMS_USER_ID(10003, "ERR_LACK_HEAD_PARAMS_USER_ID"),
	/** 缺少头部参数：userCompany */
	ERR_LACK_HEAD_PARAMS_USER_COMPANY(10004, "ERR_LACK_HEAD_PARAMS_USER_COMPANY"),
	/** 用户没有该企业的权限 */
	ERR_USER_NOT_ENTERPRISE_AUTHORITY(10005, "ERR_USER_NOT_ENTERPRISE_AUTHORITY"),
	
	/** 请求缺少部分(检查文件名称、内容) */
	ERR_MISSING_SERVLET_REQUEST_PART_EXCEPTION(10006, "ERR_MISSING_SERVLET_REQUEST_PART_EXCEPTION"),
	/** 缺少请求参数(检查参数名称、内容)   */
	ERR_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION(10007, "ERR_MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION"),
	/** 参数解析失败(检查参数内容格式) */
	ERR_HTTP_MESSAGE_NOT_READABLE_EXCEPTION(10008, "ERR_HTTP_MESSAGE_NOT_READABLE_EXCEPTION"),
	/** 参数验证失败(@Valid对应实体类验证异常) */		
	ERR_METHOD_ARGUMENT_NOT_VALID_EXCEPTION(10009, "ERR_METHOD_ARGUMENT_NOT_VALID_EXCEPTION"),
	/** 参数验证失败(@Validated验证实体的异常) */
	ERR_CONSTRAINT_VIOLATIONEXCEPTION(10010, "ERR_CONSTRAINT_VIOLATIONEXCEPTION"),
	/** 参数验证失败(javax基础验证异常)  */
	ERR_VALIDATION_EXCEPTION(10011, "ERR_VALIDATION_EXCEPTION"),
	/** 参数绑定失败(检查参数名称)  */
	ERR_BIND_EXCEPTION(10012, "ERR_BIND_EXCEPTION"),
	/** API地址不存在(检查请求地址)*/
	ERR_NO_HANDLER_FOUND_EXCEPTION(10013, "ERR_NO_HANDLER_FOUND_EXCEPTION"),
	/** 不支持当前请求方法(检查请求方法) */
	ERR_HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(10014, "ERR_HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION"),
	/** 不支持当前媒体类型(检查下请求头contentType) */
	ERR_HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION(10015, "ERR_HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION"),
	
	
	
/**********************************************101xx 表示查询的异常 ***************************************************/	
	/** 查询失败：属性不满足要求(业务的判断：不存在，不符合，不在之内等错误)  */
	ERR_SELECT_ATTRIBUTE_NOT_REQUIREMENTS(10101, "ERR_SELECT_ATTRIBUTE_NOT_REQUIREMENTS"),
	/** 查询失败：找不到数据（查询返回0条） */
	ERR_SELECT_NOT_FOUND(10102, "ERR_SELECT_NOT_FOUND"),
	
	
/**********************************************102xx 表示新增的异常 ***************************************************/	
	/** 新增失败：属性不满足要求(业务的判断：不存在，不符合，不在之内等错误) */
	ERR_INSERT_ATTRIBUTE_NOT_REQUIREMENTS(10201, "ERR_INSERT_ATTRIBUTE_NOT_REQUIREMENTS"),
	/** 新增失败：数据重复 */
	ERR_INSERT_DATA_REPEAT(10202, "ERR_INSERT_DATA_REPEAT"),
	
	
	
/**********************************************103xx 表示修改的异常 ***************************************************/		
	/** 更新失败：属性不满足要求(业务的判断：不存在，不符合，不在之内等错误) */
	ERR_UPDATE_ATTRIBUTE_NOT_REQUIREMENTS(10301, "ERR_UPDATE_ATTRIBUTE_NOT_REQUIREMENTS"),
	/** 更新失败：数据不存在 */
	ERR_UPDATE_DATA_NOT_FOUND(10302, "ERR_UPDATE_DATA_NOT_FOUND"),
    PART_UPDATE_SUCCESS(10303, "PART_UPDATE_SUCCESS"),

	
	
/**********************************************104xx 表示删除的异常 ***************************************************/		
	/** 删除失败：属性不满足要求(业务的判断：不存在，不符合，不在之内等错误) */
	ERR_DELETE_ATTRIBUTE_NOT_REQUIREMENTS(10401, "ERR_DELETE_ATTRIBUTE_NOT_REQUIREMENTS"),
	/** 删除失败：数据不存在 */
	ERR_DELETE_DATA_NOT_FOUND(10402, "ERR_DELETE_DATA_NOT_FOUND"),
	
	
	
/**********************************************10000 表示 未定义的异常  需要统一拦截  ***************************************************/		
	/**  系统内部错误 */
	ERR_INTERNAL_SERVER_ERROR(10000, "ERR_INTERNAL_SERVER_ERROR");
/*******************************************************end 平台通用异常：11001-11999**************************************************************/

	/** 错误编码 */
	private int errorCode;
	/** 错误信息 */
	private String errorMessage;

	private BaseErrorEnum(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
