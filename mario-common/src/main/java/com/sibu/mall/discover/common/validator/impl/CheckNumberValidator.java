package com.mall.discover.common.validator.impl;

import com.mall.common.exception.ValidationException;
import com.mall.discover.common.validator.IValidator;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2016/4/18
 * Description:
 */
@Repository("checkNumberValidate")
public class CheckNumberValidator implements IValidator {

    @Override
    public void validateMutil(Object obj) throws ValidationException {

    }

    @Override
    public void validate(Object target) throws ValidationException {

    }
}
