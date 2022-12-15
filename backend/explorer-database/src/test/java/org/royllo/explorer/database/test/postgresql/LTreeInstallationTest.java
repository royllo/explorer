package org.royllo.explorer.database.test.postgresql;

import org.junit.Ignore;
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
@DisplayName("Postgresql LTree installation")
@Ignore
public class LTreeInstallationTest {

    @Autowired
    private DataSource dataSource;

    // TODO Activate this test
    @Test
    @DisplayName("LTree found in pg_extension")
    public void ltreeFoundInPGExtension() throws SQLException {
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
