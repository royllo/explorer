package org.royllo.explorer.core.test.core.service.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.util.K1Value;
import org.royllo.explorer.core.repository.util.K1ValueRepository;
import org.royllo.explorer.core.service.util.DatabaseK1Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.tbk.lnurl.auth.K1;
import org.tbk.lnurl.simple.auth.SimpleK1;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@DisplayName("DatabaseK1Manager tests")
public class DatabaseK1ManagerTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private K1ValueRepository k1ValueRepository;

    @Test
    @DisplayName("Create, retrieve and delete a K1 value")
    public void databaseK1Manager() {
        DatabaseK1Manager databaseK1Manager = new DatabaseK1Manager(k1ValueRepository);

        k1ValueRepository.deleteAll();

        // K1 value for test.
        String nonExistingK1Value = "e2af6254a8df433264fa23f67eb8188635d15ce883e8fc020989d5f82ae6f11e";
        K1 nonExistingK1 = SimpleK1.fromHex(nonExistingK1Value);

        // Checking k1 value creation.
        assertEquals(0, k1ValueRepository.count());
        final K1 k1Created = databaseK1Manager.create();
        assertEquals(1, k1ValueRepository.count());

        // Checking isValid() method.
        assertTrue(databaseK1Manager.isValid(k1Created));
        assertFalse(databaseK1Manager.isValid(nonExistingK1));

        // Checking invalidate() method.
        databaseK1Manager.invalidate(k1Created);
        databaseK1Manager.invalidate(nonExistingK1);
        assertEquals(0, k1ValueRepository.count());
        assertFalse(databaseK1Manager.isValid(k1Created));
        assertFalse(databaseK1Manager.isValid(nonExistingK1));
    }

    @Test
    @DisplayName("Purge old K1 values")
    public void oldK1Purge() throws SQLException {
        DatabaseK1Manager databaseK1Manager = new DatabaseK1Manager(k1ValueRepository);
        k1ValueRepository.deleteAll();

        // Test values.
        String firstK1Value = "e2af6254a8df433264fa23f67eb8188635d15ce883e8fc020989d5f82ae6f11e";
        String secondK1Value = "d4f067cf72b94baddac1b3856b231669c3239d59cce95ef260da58363fb01822";

        // Creating two values.
        k1ValueRepository.save(K1Value.builder().k1(firstK1Value).build());
        k1ValueRepository.save(K1Value.builder().k1(secondK1Value).build());
        assertTrue(databaseK1Manager.isValid(SimpleK1.fromHex(firstK1Value)));
        assertTrue(databaseK1Manager.isValid(SimpleK1.fromHex(secondK1Value)));

        // Updating the first value to change its creation date.
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().executeQuery("""
                    UPDATE  UTIL_K1_CACHE
                    SET     CREATED_ON = '2021-01-01 00:00:00'
                    WHERE   K1 = '""" + firstK1Value + "'");
        }

        // Checking isValid() method (will trigger purge so k1 won't exist anymore.
        assertFalse(databaseK1Manager.isValid(SimpleK1.fromHex(firstK1Value)));
        assertTrue(databaseK1Manager.isValid(SimpleK1.fromHex(secondK1Value)));

    }

}
