package com.mall.discover.web.support.filter;

import com.doubo.common.generate.GenUUID;
import com.doubo.common.model.response.CommonResponse;
import com.doubo.common.util.ExceptionUtil;
import com.doubo.common.util.StringUtil;
import com.doubo.security.subject.Subject;
import com.mall.common.constants.AppConstants;
import com.mall.user.token.manager.UserAccessTokenManager;
import com.mall.user.token.request.AccessTokenValidationRequest;
import com.mall.user.token.response.TokenDataBasicResponse;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2017/4/6
 * Description:
 */
@Slf4j
public class TokenUidSubject implements Subject {

    private static final long serialVersionUID = -1655724323350159250L;
    protected volatile Boolean authenticated;
    protected Integer uid;
    protected String token;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public TokenUidSubject(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        init(request, response);
    }

    private void init(HttpServletRequest request, HttpServletResponse response) {
        token = request.getHeader(AppConstants.HEAD_TOKEN);
    }

    @Override
    public boolean isAuthenticated() {
        if (authenticated == null) {
            if (StringUtil.isNotBlank(token)) {
                AccessTokenValidationRequest validationRequest = new AccessTokenValidationRequest();
                validationRequest.setAccessToken(token);
                validationRequest.setInitiationID(GenUUID.get32UUID());
                authenticated = false;
                try {
                    CommonResponse<TokenDataBasicResponse> commonResponse = UserAccessTokenManager.accessTokenValidate(validationRequest);
                    if (commonResponse.isSuccess() && commonResponse.getResult().getUserId() != null) {
                        uid = commonResponse.getResult().getUserId();
                        authenticated = true;
                    }
                } catch (Exception e) {
                    log.error("用户token[{}]验证异常, Exception:{}", token, ExceptionUtil.getAsString(e));
                }
            } else {
                authenticated = false;
            }
        }
        return authenticated;
    }

    @Override
    public Object getPrincipal() {
        return uid;
    }

    public Integer getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }
}
