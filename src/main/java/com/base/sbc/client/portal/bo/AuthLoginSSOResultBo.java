package com.base.sbc.client.portal.bo;

import lombok.Data;
import sun.plugin.javascript.navig5.JSObject;

/**
 * @Create : 2024/7/4 18:46
 **/
@Data
public class AuthLoginSSOResultBo {

    private JSObject userInfo;

    private JSObject token;

}
