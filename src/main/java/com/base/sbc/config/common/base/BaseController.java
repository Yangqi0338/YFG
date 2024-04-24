package com.base.sbc.config.common.base;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.i18n.LocaleMessages;
import com.base.sbc.config.utils.DateUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terracotta.offheapstore.jdk8.Function;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.Map;

/**
 * @author fred
 * @data 创建时间:2020/2/3
 */
@Component
public class BaseController {
	/** 日志对象 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/** 国际化对象*/
	@Autowired
	private LocaleMessages localeMessages;

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;

	@Resource
	private UserUtils userUtils;

	/** 免登录访问接口 */
	public static final String OPEN_URL = "/api/open";

	/** 拥有登录Token才能访问 */
	public static final String TOKEN_URL = "/api/token";

	/** saas接口 */
	public static final String SAAS_URL = "/api/saas";

	public static final String USER_COMPANY = "userCompany";
	public static final String USER_ID= "userId";
	public static final String PAGE_NUM = "pageNum";
	public static final String PAGE_SIZE = "pageSize";
	public static final String COMPANY_CODE = "company_code";
	public static final String CATEGORY_NAME = "category_name";
	public static final String DEL_FLAG="del_flag";

	public String getUserCompany(){
		String header = request.getHeader(USER_COMPANY);
		if (StrUtil.isBlank(header)) {
			RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
			if (requestMapping != null && requestMapping.value()[0].startsWith(BaseController.OPEN_URL)) {
				return BaseConstant.DEF_COMPANY_CODE;
			}
		}
		return header;
	}
	public String getUserId(){
		return request.getHeader(USER_ID);
	}

	public GroupUser getUser(){
		return userUtils.getUser(getUserId());
	}

/****************************************国际化方法satrt************************************************/
	/**
	 * 获取国际化信息
	 * @param messageCcode  国际化编码 key
	 * @return
	 */
	protected String getMessage(String messageCcode) {
		String msg = localeMessages.getMessage(messageCcode);
		if (StringUtils.isNotBlank(msg)) {
			return msg;
		}
		return messageCcode;
	}

	/**
	 * 获取国际化信息
	 * @param messageCcode 国际化编码key
	 * @param args 默认值 取不到则返回本数据
	 * @return
	 */
	protected String getMessage(String messageCcode, Object[] args) {
		String msg = localeMessages.getMessage(messageCcode, args);
		if (StringUtils.isNotBlank(msg)) {
			return msg;
		}
		return messageCcode;
	}
/****************************************国际化方法end  统一返回值  ************************************************/

	/****************************************查询  ************************************************/
	/**查询成功:单个String，实体，分页对象*/
	protected <T> ApiResult<T> selectSuccess(T object) {
		return success(BaseErrorEnum.SUCCESS_SELECT.getErrorMessage(),object);
	}
	/**查询成功：多个实体，list等 */
	protected <T> ApiResult<T> selectSuccess(Map<String, Object> attributes) {
		return success(BaseErrorEnum.SUCCESS_SELECT.getErrorMessage(),attributes);
	}
	/**查询成功：单个主实体对象，附加的多个list */
	protected <T> ApiResult<T> selectSuccess(T object,Map<String, Object> attributes) {
		return success(BaseErrorEnum.SUCCESS_SELECT.getErrorMessage(),object,attributes);
	}
	/**查询失败：属性不满足要求(不存在，不符合，不在之内等) */
	protected <T> ApiResult<T> selectAttributeNotRequirements(T object) {
		return error(BaseErrorEnum.ERR_SELECT_ATTRIBUTE_NOT_REQUIREMENTS.getErrorMessage(),object);
	}
	/** 查询失败：找不到数据*/
	protected <T> ApiResult<T> selectNotFound(T object) {
		return error(BaseErrorEnum.ERR_SELECT_NOT_FOUND.getErrorMessage(),object);
	}
	/** 查询失败：找不到数据*/
	protected <T> ApiResult<T> selectNotFound() {
		return error(BaseErrorEnum.ERR_SELECT_NOT_FOUND.getErrorMessage());
	}

	/****************************************新增  ************************************************/
	/**新增成功:单个String，实体，分页对象*/
	protected <T> ApiResult<T> insertSuccess(T object) {
		return success(BaseErrorEnum.SUCCESS_INSERT.getErrorMessage(),object);
	}
	/**新增成功：多个实体，list等 */
	protected <T> ApiResult<T> insertSuccess(Map<String, Object> attributes) {
		return success(BaseErrorEnum.SUCCESS_INSERT.getErrorMessage(),attributes);
	}
	/**新增成功：单个主实体对象，附加的多个list */
	protected <T> ApiResult<T> insertSuccess(T object,Map<String, Object> attributes) {
		return success(BaseErrorEnum.SUCCESS_INSERT.getErrorMessage(),object,attributes);
	}

	/** 新增失败：属性不满足要求(不存在，不符合，不在之内等) */
	protected <T> ApiResult<T> insertAttributeNotRequirements(T object) {
		return error(BaseErrorEnum.ERR_INSERT_ATTRIBUTE_NOT_REQUIREMENTS.getErrorMessage(),object);
	}
	/** 新增失败：数据已存在*/
	protected <T> ApiResult<T> insertDataRepeat(T object) {
		return error(BaseErrorEnum.ERR_INSERT_DATA_REPEAT.getErrorMessage(),object);
	}
	/** 新增失败：数据已存在*/
	protected <T> ApiResult<T> insertDataRepeat() {
		return error(BaseErrorEnum.ERR_INSERT_DATA_REPEAT.getErrorMessage());
	}


	/****************************************修改 ************************************************/
	/**修改成功:单个String，实体，分页对象*/
	protected <T> ApiResult<T> update(T object) {
		return update(object, (it)-> ObjectUtil.isNotEmpty(object));
	}
	protected <T> ApiResult<T> update(T object, Function<T,Boolean> judgeFunc) {
		return judgeFunc.apply(object) ? updateSuccess(object) : updateNotFound(object);
	}
	/**修改成功:单个String，实体，分页对象*/
	protected <T> ApiResult<T> updateSuccess(T object) {
		return success(BaseErrorEnum.SUCCESS_UPDATE.getErrorMessage(),object);
	}
	/**修改成功：多个实体，list等 */
	protected <T> ApiResult<T> updateSuccess(Map<String, Object> attributes) {
		return success(BaseErrorEnum.SUCCESS_UPDATE.getErrorMessage(),attributes);
	}
	/**修改成功：单个主实体对象，附加的多个list */
	protected <T> ApiResult<T> updateSuccess(T object,Map<String, Object> attributes) {
		return success(BaseErrorEnum.SUCCESS_UPDATE.getErrorMessage(),object,attributes);
	}

	/**修改失败：属性不满足要求(不存在，不符合，不在之内等) */
	protected <T> ApiResult<T> updateAttributeNotRequirements(T object) {
		return error(BaseErrorEnum.ERR_UPDATE_ATTRIBUTE_NOT_REQUIREMENTS.getErrorMessage(),object);
	}
	/**修改失败：找不到数据*/
	protected <T> ApiResult<T> updateNotFound(T object) {
		return error(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND.getErrorMessage(),object);
	}
	/**修改失败：找不到数据*/
	protected <T> ApiResult<T> updateNotFound() {
		return error(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND.getErrorMessage());
	}

	/****************************************删除************************************************/
	/**删除成功:单个String，实体，分页对象*/
	protected <T> ApiResult<T> deleteSuccess(T object) {
		return success(BaseErrorEnum.SUCCESS_DELETE.getErrorMessage(),object);
	}
	/**删除成功：多个实体，list等 */
	protected <T> ApiResult<T> deleteSuccess(Map<String, Object> attributes) {
		return success(BaseErrorEnum.SUCCESS_DELETE.getErrorMessage(),attributes);
	}
	/**删除成功：单个主实体对象，附加的多个list */
	protected <T> ApiResult<T> deleteSuccess(T object,Map<String, Object> attributes) {
		return success(BaseErrorEnum.SUCCESS_DELETE.getErrorMessage(),object,attributes);
	}
	/** 删除失败：属性不满足要求(不存在，不符合，不在之内等) */
	protected <T> ApiResult<T> deleteAttributeNotRequirements(T object) {
		return error(BaseErrorEnum.ERR_DELETE_ATTRIBUTE_NOT_REQUIREMENTS.getErrorMessage(),object);
	}
	/** 删除失败：找不到数据*/
	protected <T> ApiResult<T> deleteNotFound(T object) {
		return error(BaseErrorEnum.ERR_DELETE_DATA_NOT_FOUND.getErrorMessage(),object);
	}
	/** 删除失败：找不到数据*/
	protected <T> ApiResult<T> deleteNotFound() {
		return error(BaseErrorEnum.ERR_DELETE_DATA_NOT_FOUND.getErrorMessage());
	}

	/****************************************公用************************************************/

	protected <T> ApiResult<T> success(String baseErrorEnumMessage) {
		return ApiResult.success(getMessage(baseErrorEnumMessage));
	}
	protected <T> ApiResult<T> success(String baseErrorEnumMessage,T object) {
		return ApiResult.success(getMessage(baseErrorEnumMessage),object);
	}
	protected <T> ApiResult<T> success(String baseErrorEnumMessage,Map<String, Object> attributes) {
		return ApiResult.success(getMessage(baseErrorEnumMessage),attributes);
	}
	protected <T> ApiResult<T> success(String baseErrorEnumMessage,T object,Map<String, Object> attributes) {
		return ApiResult.success(getMessage(baseErrorEnumMessage),object,attributes);
	}

	protected <T> ApiResult<T> error(String baseErrorEnumMessage,T object) {
		return ApiResult.error(getMessage(baseErrorEnumMessage),BaseErrorEnum.valueOf(baseErrorEnumMessage).getErrorCode(),object);
	}
	protected <T> ApiResult<T> error(String baseErrorEnumMessage) {
		return ApiResult.error(getMessage(baseErrorEnumMessage),BaseErrorEnum.valueOf(baseErrorEnumMessage).getErrorCode(),null);
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text);
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? DateUtils.formatDateTime((Date) value) : "";
			}
		});
	}
}
