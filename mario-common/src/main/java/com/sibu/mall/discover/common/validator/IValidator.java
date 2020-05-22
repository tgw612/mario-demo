package com.mall.discover.common.validator;

import com.mall.common.exception.ValidationException;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2016/2/27
 * Description:参数校验接口
 */
public interface IValidator {
    /**
     * 返回多个错误
     * @param obj
     * @throws ValidationException
     */
    public void validateMutil(Object obj) throws ValidationException;

    /**
     * 返回单个错误
     *
     * @param obj
     * @throws ValidationException
     */
    public void validate(Object obj) throws ValidationException;
}
