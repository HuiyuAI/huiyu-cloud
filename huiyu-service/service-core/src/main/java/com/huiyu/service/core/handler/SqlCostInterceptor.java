package com.huiyu.service.core.handler;

import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.huiyu.service.core.config.Monitor;
import com.huiyu.service.core.config.SpringContext;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * @author wAnG
 */

@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
@Component
public class SqlCostInterceptor implements Interceptor {

    private final Logger log = LoggerFactory.getLogger("DBInfo");

    private static final String MONITOR_COUNT_NAME = "sqlMethodCount";

    private static final String MONITOR_TIME_NAME = "sqlMethodTime";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();

        long startTime = System.currentTimeMillis();
        long sqlCost = 0;
        StatementHandler statementHandler = (StatementHandler) target;
        List<Object> resultList = null;
        try {
            Object proceed = invocation.proceed();
            long endTime = System.currentTimeMillis();
            sqlCost = endTime - startTime;
            resultList = Collections.singletonList(proceed);
            return proceed;
        } finally {
            String execName = getExecName(invocation);
            String simpleName = invocation.getClass().getSimpleName();
            String methodName = invocation.getMethod().getName();
            Monitor.recordOne(MONITOR_COUNT_NAME, execName);
            Monitor.recordTime(MONITOR_TIME_NAME, execName, sqlCost);
            printSqlLog(startTime, statementHandler, resultList, sqlCost);
        }
    }

    private void printSqlLog(long startTime, StatementHandler statementHandler,List<Object> resultList,Long sqlCost) {
        BoundSql boundSql = statementHandler.getBoundSql();
        Object parameterObject = boundSql.getParameterObject();
        String sql = boundSql.getSql();
        if (parameterObject != null) {
            sql = getFllSql(boundSql, parameterObject);
        }
        int total = resultList == null ? 0 : resultList.size();
        sql = sql.replace("\n", " ").replaceAll("\\s{1,}", " ");
        log.info("\n" +
                        "==================== MySQLStart ====================\n" +
                        "totalTime : {}ms ==> total : [{}]\n" +
                        "SQL : [{}]\n" +
                        "Result : [{}]\n" +
                        "====================  MySQLEnd  ====================",
                sqlCost, total, sql, resultList);
    }

    public String getFllSql(BoundSql boundSql, Object parameterObject) {
        SqlSessionFactory bean = SpringContext.getBean(SqlSessionFactory.class);

        Configuration configuration = bean.getConfiguration();

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        List<Object> paramList = new ArrayList<>();

        for (ParameterMapping parameterMapping : parameterMappings) {
            if (parameterMapping.getMode() == ParameterMode.OUT) {
                continue;
            }
            Object value = null;
            String propertyName = parameterMapping.getProperty();
            if (boundSql.hasAdditionalParameter(propertyName)) {
                value = boundSql.getAdditionalParameter(propertyName);
            } else if (parameterObject == null) {
                value = null;
            } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                value = parameterObject;
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                value = metaObject.getValue(propertyName);
            }
            paramList.add(value);
        }
        String sql = boundSql.getSql();
        for (Object o : paramList) {
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(formatParamValue(o)));
        }
        return sql;
    }

    private String formatParamValue(Object paramValue) {
        if (paramValue == null) {
            return "null";
        }
        if (paramValue instanceof String) {
            paramValue = "'" + paramValue + "'";
        }
        if (paramValue instanceof Date) {
            paramValue = "'" + paramValue + "'";
        }
        return paramValue.toString();
    }

    private String getExecName(Invocation invocation) throws Exception {
        MybatisParameterHandler parameterHandler = (MybatisParameterHandler) ((RoutingStatementHandler) invocation.getTarget()).getParameterHandler();
        Field mappedStatementClazz = parameterHandler.getClass().getDeclaredField("mappedStatement");
        mappedStatementClazz.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) mappedStatementClazz.get(parameterHandler);
        List<String> nameList = Splitter.on(".").splitToList(mappedStatement.getId());
        return Joiner.on("_").join(nameList.subList(nameList.size() - 2, nameList.size()));
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
