package com.huiyu.service.core.constant;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public interface BaseEnum<T> {

    T getDictKey();

    default T getValue() {
        return this.getDictKey();
    }

    static <T> BaseEnum<T> valueOfEnum(Class<BaseEnum<T>> enumClass, String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        BaseEnum<T>[] enums = enumClass.getEnumConstants();
        Optional<BaseEnum<T>> optional = Arrays.stream(enums).filter(baseEnum -> baseEnum.getDictKey().toString().equals(value)).findFirst();
        return optional.orElse(null);
    }


}
