package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.universe.UniverseServerDTO;
import org.royllo.explorer.core.service.universe.UniverseServerService;
import org.royllo.explorer.core.util.exceptions.universe.UniverseServerCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@DisplayName("UniverseServerService tests")
public class UniverseServerServiceTest {

    @Autowired
    private UniverseServerService universeServerService;

    @Test
    @DisplayName("addUniverseServer()")
    public void addUniverseServer() {
        // =============================================================================================================
        // Adding a universe server with a null value.
        UniverseServerCreationException e = assertThrows(UniverseServerCreationException.class, () -> universeServerService.addUniverseServer(null));
        assertEquals("Server address cannot be null", e.getMessage());

        // =============================================================================================================
        // Adding a universe server with an invalid value.
        e = assertThrows(UniverseServerCreationException.class, () -> universeServerService.addUniverseServer("invalid"));
        assertEquals("Invalid server address invalid", e.getMessage());

        // =============================================================================================================
        // Adding a universe server with a valid value (web).
        final UniverseServerDTO universeServer1 = universeServerService.addUniverseServer("universe.royllo.org:8080");
        assertNotNull(universeServer1);
        assertNotNull(universeServer1.getId());
        final UniverseServerDTO universeServer1Bis = universeServerService.addUniverseServer("https://universe.royllo.org:8080");
        assertNotNull(universeServer1Bis);
        assertNotNull(universeServer1Bis.getId());

        // =============================================================================================================
        // Adding a universe server with a valid value (hostname).
        final UniverseServerDTO universeServer2 = universeServerService.addUniverseServer("1.1.1.1:8080");
        assertNotNull(universeServer2);
        assertNotNull(universeServer2.getId());
        final UniverseServerDTO universeServer2Bis = universeServerService.addUniverseServer("https://1.1.1.1:8080");
        assertNotNull(universeServer2Bis);
        assertNotNull(universeServer2Bis.getId());

        // =============================================================================================================
        // Trying to add a duplicated value in universe server.
        e = assertThrows(UniverseServerCreationException.class, () -> universeServerService.addUniverseServer("1.1.1.1:8080"));
        assertEquals("1.1.1.1:8080 is already in our database", e.getMessage());

        // =============================================================================================================
        // Trying to add a duplicated value in universe server (with space around).
        e = assertThrows(UniverseServerCreationException.class, () -> universeServerService.addUniverseServer("1.1.1.1:8080"));
        assertEquals("1.1.1.1:8080 is already in our database", e.getMessage());
    }

    @Test
    @DisplayName("getUniverseServerByServerAddress()")
    public void getUniverseServerByServerAddress() {
        // =============================================================================================================
        // Checking if a universe exists before we create it.
        assertTrue(universeServerService.getUniverseServerByServerAddress("test.royllo.org:8080").isEmpty());

        // =============================================================================================================
        // Adding a universe server with a valid value (hostname).
        assertDoesNotThrow(() -> universeServerService.addUniverseServer("test.royllo.org:8080"));

        // =============================================================================================================
        // Checking if a universe exists before after create it.
        assertTrue(universeServerService.getUniverseServerByServerAddress("test.royllo.org:8080").isPresent());
        assertTrue(universeServerService.getUniverseServerByServerAddress(" test.royllo.org:8080 ").isPresent());
    }

    @Test
    @DisplayName("getAllUniverseServers()")
    public void getAllUniverseServers() {
        // =============================================================================================================
        // Checking that there is not universe server.
        int universeServersCount = universeServerService.getAllUniverseServers().size();

        // =============================================================================================================
        // Creating three universe servers.
        universeServerService.addUniverseServer("1.royllo.org:8080");
        universeServerService.addUniverseServer("2.royllo.org:8080");
        universeServerService.addUniverseServer("3.royllo.org:8080");

        // =============================================================================================================
        // Checking that all servers are here.
        assertEquals(universeServersCount + 3, universeServerService.getAllUniverseServers().size());
    }

}
