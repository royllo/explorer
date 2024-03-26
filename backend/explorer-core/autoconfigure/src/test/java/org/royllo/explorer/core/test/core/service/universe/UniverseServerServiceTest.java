package org.royllo.explorer.core.test.core.service.universe;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.universe.UniverseServerDTO;
import org.royllo.explorer.core.service.universe.UniverseServerService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.royllo.explorer.core.util.exceptions.universe.UniverseServerCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@DisplayName("UniverseServerService tests")
public class UniverseServerServiceTest extends BaseTest {

    @Autowired
    private UniverseServerService universeServerService;

    @Test
    @DisplayName("addUniverseServer()")
    public void addUniverseServer() {
        // =============================================================================================================
        // Constraint tests.

        // Invalid null address.
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> universeServerService.addUniverseServer(null))
                .satisfies(violations -> {
                    assertEquals(1, violations.getConstraintViolations().size());
                    assertTrue(isPropertyInConstraintViolations(violations, "serverAddress"));
                });

        // Invalid server address.
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> universeServerService.addUniverseServer("invalid"))
                .satisfies(violations -> {
                    assertEquals(1, violations.getConstraintViolations().size());
                    assertTrue(isPropertyInConstraintViolations(violations, "serverAddress"));
                });

        // =============================================================================================================
        // Normal behavior tests.

        // -------------------------------------------------------------------------------------------------------------
        // Adding a universe server with a valid value (hostname).
        assertThat(universeServerService.addUniverseServer("universe.royllo.org:8080"))
                .isNotNull()
                .extracting(UniverseServerDTO::getId)
                .isNotNull();
        assertThat(universeServerService.addUniverseServer("https://universe.royllo.org:8080"))
                .isNotNull()
                .extracting(UniverseServerDTO::getId)
                .isNotNull();

        // -------------------------------------------------------------------------------------------------------------
        // Adding a universe server with a valid value (IP).
        assertThat(universeServerService.addUniverseServer("1.1.1.1:8080"))
                .isNotNull()
                .extracting(UniverseServerDTO::getId)
                .isNotNull();
        assertThat(universeServerService.addUniverseServer("https://1.1.1.1:8080"))
                .isNotNull()
                .extracting(UniverseServerDTO::getId)
                .isNotNull();

        // -------------------------------------------------------------------------------------------------------------
        // Trying to add a duplicated value in universe server.
        assertThatExceptionOfType(UniverseServerCreationException.class)
                .isThrownBy(() -> universeServerService.addUniverseServer("1.1.1.1:8080"))
                .withMessage("1.1.1.1:8080 is already in our database");
    }

    @Test
    @DisplayName("getUniverseServerByServerAddress()")
    public void getUniverseServerByServerAddress() {
        final String universeServerForTest = "test.royllo.org:8080";

        // Checking if a universe exists before we create it.
        assertTrue(universeServerService.getUniverseServerByServerAddress(universeServerForTest).isEmpty());

        // Adding a universe server with a valid value (hostname).
        assertDoesNotThrow(() -> universeServerService.addUniverseServer(universeServerForTest));

        // Checking if the universe exists after we create it.
        assertTrue(universeServerService.getUniverseServerByServerAddress(universeServerForTest).isPresent());
    }

    @Test
    @DisplayName("getAllUniverseServers()")
    public void getAllUniverseServers() {
        // Getting the number of universe existing.
        int universeServersCount = universeServerService.getAllUniverseServers().size();

        // Creating three new universe servers.
        universeServerService.addUniverseServer("1.royllo.org:8080");
        universeServerService.addUniverseServer("2.royllo.org:8080");
        universeServerService.addUniverseServer("3.royllo.org:8080");

        // Checking that all servers are here.
        assertThat(universeServerService.getAllUniverseServers())
                .hasSize(universeServersCount + 3)
                .extracting(UniverseServerDTO::getServerAddress)
                .contains("1.royllo.org:8080", "2.royllo.org:8080", "3.royllo.org:8080");
    }

}
