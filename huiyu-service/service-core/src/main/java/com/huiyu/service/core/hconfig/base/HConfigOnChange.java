package com.huiyu.service.core.hconfig.base;

/**
 * @author wAnG
 * @Date 2023-06-23  21:34
 */
public interface HConfigOnChange<T> {

    void onChange(T t);

}
