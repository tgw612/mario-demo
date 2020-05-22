package com.mall.discover.common.validator.impl;

import com.mall.common.exception.ValidationException;
import com.mall.discover.common.validator.IValidator;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: qiujingwang
 * Date: 2016/2/27
 * Description:
 */
@Component
public class ValidatorImpl implements IValidator {

    @Autowired
    private LocalValidatorFactoryBean localValidator;

    /*private static ValidatorFactory factory = Validation.byProvider(org.hibernate.validator.HibernateValidator.class).configure().buildValidatorFactory();
    private static javax.validation.Validator localValidator = factory.getValidator();*/

    /**
     * 返回多个错误
     * @param obj
     * @throws ValidationException
     **/
    @Override
    public void validateMutil(Object obj) throws ValidationException {

        Set<ConstraintViolation<Object>> constraintViolations = localValidator.validate(obj);
        if (constraintViolations == null || constraintViolations.size() == 0) {
            return;
        }

        StringBuffer errorAppendStr = new StringBuffer();
        for (ConstraintViolation<Object> error : constraintViolations) {
            errorAppendStr.append(((PathImpl)error.getPropertyPath()).getLeafNode());
            errorAppendStr.append(":");
            errorAppendStr.append(error.getMessage());
            errorAppendStr.append(",");
        }
        throw new ValidationException(errorAppendStr.substring(0, errorAppendStr.length() - 1));
    }

    /**
     * 返回单个错误
     * @param obj
     * @throws ValidationException
     */
    @Override
    public void validate(Object obj) throws ValidationException {
        Set<ConstraintViolation<Object>> constraintViolations =
                localValidator.validate(obj);
        if (constraintViolations != null && constraintViolations.size() != 0) {
            ConstraintViolation<Object> error = constraintViolations.iterator().next();

            throw new ValidationException(((PathImpl)error.getPropertyPath()).getLeafNode() +":"
                    +error.getMessage() +",无效值："+error.getInvalidValue());
        }
    }

    public void setLocalValidator(LocalValidatorFactoryBean localValidator) {
        this.localValidator = localValidator;
    }
}

