package org.royllo.explorer.core.test.integration.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.search.SQLSearchServiceImplementation;
import org.royllo.explorer.core.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:16:///explorer",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
@DirtiesContext
@DisplayName("PostgreSQL test")
public class PostgreSQLTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SearchService searchService;

    @Test
    @DisplayName("Liquibase execution test")
    public void liquibaseExecutionTest() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            final ResultSet results = connection.createStatement()
                    .executeQuery("""
                             SELECT  count(*) as USER_COUNT
                             FROM    APPLICATION_USER
                             WHERE   USERNAME = 'anonymous'
                            """);
            results.next();
            Assertions.assertEquals(1, results.getInt("USER_COUNT"));
        }
    }

    @Test
    @DisplayName("PostgreSQL ILIKE query")
    public void ilikeQueryWithPostgreSQL() {
        assertTrue(((SQLSearchServiceImplementation) searchService).isUsingPostgreSQL());

        // We test a query.
        // Searching for an asset with its partial name corresponding to eight assets.
        Page<AssetDTO> results = searchService.queryAssets("royllo", 1, 4);
        assertEquals(8, results.getTotalElements());
        assertEquals(2, results.getTotalPages());
        assertThat(results.getContent())
                .extracting(AssetDTO::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

}
