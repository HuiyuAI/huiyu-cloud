package com.huiyu.service.core.sd.validator;

import com.google.common.collect.Lists;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.sd.validator.base.CmdValidator;
import com.huiyu.service.core.sd.validator.propertyValidator.IPropertyValidator;
import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wAnG
 * @Date: 2023-06-11  22:17
 */
public abstract class AbstractCmdValidator<T extends Cmd> implements  IPropertyValidator<T>, InitializingBean {

    private  CmdValidator cmdValidator;

    private  Map<String,List<Pair<CmdValidator,IPropertyValidator<T>>>> validatorMap;
    @Resource
    private List<IPropertyValidator<T>> propertyValidators;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(propertyValidators)){
            return;
        }
        cmdValidator = getClass().getAnnotation(CmdValidator.class);

        validatorMap = propertyValidators.stream()
                .filter(validator -> Objects.nonNull(validator.getClass().getAnnotation(CmdValidator.class)))
                .map(validator -> new Pair<>(validator.getClass().getAnnotation(CmdValidator.class),validator))
                .collect(Collectors.groupingBy(pair -> pair.getKey().name(),Collectors.toList()));

    }

    @Override
    public boolean validatorProperty(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for (Field field: declaredFields){
            String name = field.getName();
            List<Pair<CmdValidator, IPropertyValidator<T>>> validators =
                    Optional.ofNullable(validatorMap.get(name)).orElse(Lists.newArrayList());

            IPropertyValidator<T> validator = validators.stream()
                    .filter(pair -> CollectionUtils.containsAny(Arrays.asList(pair.getKey().dealType()), Arrays.asList(cmdValidator.dealType())))
                    .findFirst()
                    .map(Pair::getValue)
                    .orElse(null);

            if(Objects.isNull(validator)){
                continue;
            }

            boolean result = validator.validatorProperty(t);
            if(!result){
                return false;
            }

        }
        return true;
    }

}
