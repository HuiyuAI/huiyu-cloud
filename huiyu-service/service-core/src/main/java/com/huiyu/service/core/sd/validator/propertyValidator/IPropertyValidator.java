package com.huiyu.service.core.sd.validator.propertyValidator;

/**
 * @author wAnG
 * @Date 2023-06-12  00:26
 */
public interface IPropertyValidator<T> {

    boolean validatorProperty(T t);

}
