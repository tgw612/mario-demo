package com.mario.web.support.validator;

import javax.validation.ClockProvider;
import javax.validation.ConstraintViolation;
import javax.validation.ParameterNameProvider;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.ConstraintDescriptor;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Created with IntelliJ IDEA. User: qiujingwang Date: 2016/4/18 Description:
 */
public class CustomValidatorFactoryBean extends LocalValidatorFactoryBean {

  private boolean skip = true;

  @Override
  public ExecutableValidator forExecutables() {
    return null;
  }

  @Override
  public ParameterNameProvider getParameterNameProvider() {
    return null;
  }

  @Override
  public ClockProvider getClockProvider() {
    return null;
  }

/*public void validate(Object target, Errors errors, Object... validationHints) {
        super.validate(target, errors, validationHints);

    }*/

  /**
   * Process the given JSR-303 ConstraintViolations, adding corresponding errors to the provided
   * Spring {@link Errors} object.
   *
   * @param violations the JSR-303 ConstraintViolation results
   * @param errors     the Spring errors object to register to
   */
  protected void processConstraintViolations(Set<ConstraintViolation<Object>> violations,
      Errors errors) {
    for (ConstraintViolation<Object> violation : violations) {
      String field = violation.getPropertyPath().toString();
      FieldError fieldError = errors.getFieldError(field);
      if (fieldError == null || !fieldError.isBindingFailure()) {
        try {
          if (errors instanceof BindingResult) {
            BindingResult bindingResult = (BindingResult) errors;
            String nestedField = bindingResult.getNestedPath() + field;
            bindingResult.addError(new FieldError(
                errors.getObjectName(), nestedField, violation.getMessage()));
            if (skip) {
              break;
            }
          } else {
            ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
            Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
            String errorCode = cd.getAnnotation().annotationType().getSimpleName();
            errors.rejectValue(field, errorCode, errorArgs, violation.getMessage());
            if (skip) {
              break;
            }
          }
        } catch (NotReadablePropertyException ex) {
          throw new IllegalStateException("JSR-303 validated property '" + field +
              "' does not have a corresponding accessor for Spring data binding - " +
              "check your DataBinder's configuration (bean property versus direct field access)",
              ex);
        }
      }
    }
  }

  //---------------------------------------------------------------------
  // Implementation of JSR-303 Validator interface
  //---------------------------------------------------------------------

    /*@Override
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return super.validate(object, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        return super.validateProperty(object, propertyName, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(
            Class<T> beanType, String propertyName, Object value, Class<?>... groups) {

        return super.validateValue(beanType, propertyName, value, groups);
    }

    @Override
    public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        return super.getConstraintsForClass(clazz);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return super.unwrap(type);
    }*/

  public void setSkip(Boolean skip) {
    if (skip == null) {
      skip = false;
    }
    this.skip = skip;
  }
}
