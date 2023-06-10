package com.huiyu.service.core.handler;

import com.huiyu.service.core.constant.BaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import javax.swing.text.html.Option;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EnumTypeHandle<T> extends BaseTypeHandler<BaseEnum<T>> {
    private final Class<BaseEnum<T>> type;

    public EnumTypeHandle(Class<BaseEnum<T>> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, BaseEnum<T> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            preparedStatement.setObject(i, parameter.getDictKey());
        } else {
            preparedStatement.setObject(i, parameter.getDictKey(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public BaseEnum<T> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String result = resultSet.getString(columnName);
        return Optional.ofNullable(result).map(value -> BaseEnum.valueOfEnum(type,value)).orElse(null);
    }

    @Override
    public BaseEnum<T> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String result = resultSet.getString(columnIndex);
        return Optional.ofNullable(result).map(value -> BaseEnum.valueOfEnum(type,result)).orElse(null);
    }

    @Override
    public BaseEnum<T> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String result = callableStatement.getString(columnIndex);
        return Optional.ofNullable(result).map(value -> BaseEnum.valueOfEnum(type,value)).orElse(null);
    }
}
