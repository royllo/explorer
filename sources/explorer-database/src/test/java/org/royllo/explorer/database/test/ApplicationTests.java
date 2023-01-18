package org.royllo.explorer.database.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
@DisplayName("Liquibase execution")
class ApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	@DisplayName("Test if liquibase was executed")
	void testLiquibase() throws SQLException {
		// Basic test checking we have no error running liquibase.
		final ResultSet results = dataSource.getConnection()
				.createStatement()
				.executeQuery("""
                        SELECT  count(*) as USER_COUNT
                        FROM    USERS
                        where   username = 'anonymous'
                        """);
		results.next();
		Assertions.assertEquals(1, results.getInt("USER_COUNT"));
	}

}
