package com.mall.discover.common.exception;

import com.mall.common.exception.BusinessException;
import com.mall.discover.common.exception.code.TokenExceptionEnum;

/**
 * @author 陈志杭
 * @contact 279397942@qq.com
 * @date 2017/2/17
 * @description
 */
public class TokenResolveException extends BusinessException {
    public TokenResolveException() {
        super(TokenExceptionEnum.TokenResolveException.getId(),TokenExceptionEnum.TokenResolveException.getText());
    }

    public TokenResolveException(Throwable ex) {
        super(TokenExceptionEnum.TokenResolveException.getId(),TokenExceptionEnum.TokenResolveException.getText(), ex);

    }
}
