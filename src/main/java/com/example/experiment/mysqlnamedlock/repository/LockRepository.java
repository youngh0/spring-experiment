package com.example.experiment.mysqlnamedlock.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class LockRepository {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";

    private final DataSource dataSource;

    public void executeWithLock(String userLockName,
                                int timeoutSeconds,
                                Runnable runnable) {

        try (Connection connection = dataSource.getConnection()) {
            log.info("lock datasource: {}", dataSource);
            try {
                getLock(connection, userLockName, timeoutSeconds);
                log.info("getLock= {}, timeoutSeconds = {}, connection = {}, thread: {}", userLockName, timeoutSeconds, connection, Thread.currentThread().getName());
                runnable.run();

            } finally {
                releaseLock(connection, userLockName);
                log.info("releaseLock = {}, connection = {}, thread = {}", userLockName, connection, Thread.currentThread().getName());
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
