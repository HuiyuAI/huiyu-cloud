package com.huiyu.service.core.config.executor;

import com.huiyu.service.core.config.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author wAnG
 * @Date 2023-06-21  00:54
 */
@Component
@Slf4j
public class ThreadTransactionManager implements InitializingBean {

    private static DataSourceTransactionManager dataSourceTransactionManager;

    private static final InheritableThreadLocal<TransactionStatus> transactionStatusInheritableThreadLocal = new InheritableThreadLocal<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        dataSourceTransactionManager = SpringContext.getBean(DataSourceTransactionManager.class);
    }

    public static boolean startTransaction() {
        try {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus transaction = dataSourceTransactionManager.getTransaction(definition);
            transactionStatusInheritableThreadLocal.set(transaction);
        } catch (Exception e) {
            log.error("事务创建异常", e);
            return false;
        }
        return true;
    }

    private static void commit() {
        try {
            TransactionStatus transactionStatus = transactionStatusInheritableThreadLocal.get();
            dataSourceTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.error("事务提交异常", e);
            rollback();
        }
    }

    private static void rollback() {
        TransactionStatus transactionStatus = transactionStatusInheritableThreadLocal.get();
        dataSourceTransactionManager.rollback(transactionStatus);
    }

    public static Function<Throwable, Void> transactionRollback = (throwable -> {
        Exception exception = new Exception(throwable);
        log.error("异步流程出错rollBack", exception);
        rollback();
        return null;
    });

    public static BiFunction<Void, Throwable, Void> transactionCommit = ((result, throwable) -> {
        if (Objects.nonNull(throwable)) {
            transactionRollback.apply(throwable);
            return null;
        }
        commit();
        return null;
    });

}
