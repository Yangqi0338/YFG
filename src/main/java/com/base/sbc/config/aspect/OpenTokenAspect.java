package com.base.sbc.config.aspect;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.TokenException;
import com.base.sbc.config.exception.TokenTimeException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 开放头部验证操作，切面处理类
 * @author: Fred
 * @date: 2021/9/23
 * @version: 1.0
 */
@Aspect
@Order(-10)
@Component
public class OpenTokenAspect extends BaseController {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OpenTokenAspect.class);

    /**
     * 切点
     * 扫描Controller层接口位置
     */
    @Pointcut("execution(* com.base.sbc.api.open.pdm.OpenProductByYiZhiYunController..*(..))")
    public void pointCut() {

    }
    /**
     * @Before 前置通知
     * 监听开放接口是否传jwt令牌(里面存在金狮的企业编码)
     */
    @Before(value = "pointCut()")
    public void doBefore(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        //获取jwt令牌
        String token = request.getHeader(BaseConstant.T_TOKEN);
        if (StringUtils.isBlank(token)) {
            LOG.info("头部令牌不存在");
            throw new TokenException(BaseErrorEnum.ERR_LACK_PARAMS_TOKEN);
        }
        try {
            Claims claims = TokenUtils.parseJwt(token);
            LOG.info("--------------企业编码为:"+ claims.getId()+"--------------");
        } catch (Exception e) {
            logger.info(e.getMessage());
            //jwt过期
            throw new TokenTimeException(BaseErrorEnum.ERR_LACK_HEAD_PARAMS_TOKEN_EXPIRATION);
        }
    }


}
