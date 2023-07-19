package org.royllo.explorer.core.test.integration.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Testcontainers
@SpringBootTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15:///explorer",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"})
@DisplayName("Postgresql test")
public class PostgreSQLTest {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Liquibase execution test")
    public void liquibaseExecutionTest() throws SQLException {
        final ResultSet results = dataSource.getConnection()
                .createStatement()
                .executeQuery("""
                        SELECT  count(*) as USERS_COUNT
                        FROM    USERS
                        WHERE   USERNAME = 'anonymous'
                        """);
        results.next();
        Assertions.assertEquals(1, results.getInt("USERS_COUNT"));
    }

    @Test
    @DisplayName("LTree found in pg_extension")
    public void treeFoundInPGExtension() throws SQLException {
        final ResultSet results = dataSource.getConnection()
                .createStatement()
                .executeQuery("""
                        SELECT  count(*) as LTREE_EXTENSION_COUNT
                        FROM    pg_extension
                        where   extname = 'ltree'
                        """);
        results.next();
        Assertions.assertEquals(1, results.getInt("LTREE_EXTENSION_COUNT"));
    }

}
