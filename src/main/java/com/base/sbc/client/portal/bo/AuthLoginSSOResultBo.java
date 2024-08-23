package com.base.sbc.client.portal.bo;

import lombok.Data;

/**
 * @Create : 2024/7/4 18:46
 **/
@Data
public class AuthLoginSSOResultBo {

//    private JSONObject userInfo;

    private TokenInfo token;


    @Data
    public static class TokenInfo{
        private String access_token;
    }

}
