package com.example.experiment.mysqlnamedlock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class NamedLockAop {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";

    private final DataSource dataSource;

    @Around("@annotation(com.example.experiment.mysqlnamedlock.NamedLock)")
    public void lock(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        NamedLock namedLock = method.getAnnotation(NamedLock.class);

        String lockKey = namedLock.lockKey();
        int timeout = namedLock.timeout();
        log.info("lockKey: {}, timeout: {}", lockKey, timeout);
        try (Connection connection = dataSource.getConnection()) {
            log.info("lock with aop datasource: {}, thread: {}", dataSource, Thread.currentThread().getName());
            try {
                getLock(connection, lockKey, timeout);
                log.info("getLock= {}, timeoutSeconds = {}, connection = {}, thread: {}", lockKey, timeout, connection, Thread.currentThread().getName());
                joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                releaseLock(connection, lockKey);
                log.info("releaseLock = {}, connection = {}, thread = {}", lockKey, connection, Thread.currentThread().getName());
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void getLock(Connection connection,
                         String userLockName,
                         int timeoutseconds) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LOCK)) {
            preparedStatement.setString(1, userLockName);
            preparedStatement.setInt(2, timeoutseconds);
            preparedStatement.executeQuery();
        }
    }

    private void releaseLock(Connection connection,
                             String userLockName) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(RELEASE_LOCK)) {
            preparedStatement.setString(1, userLockName);
            preparedStatement.executeQuery();
        }
    }
}
